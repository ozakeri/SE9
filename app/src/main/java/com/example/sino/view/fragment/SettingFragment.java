package com.example.sino.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sino.BuildConfig;
import com.example.sino.R;
import com.example.sino.SinoApplication;
import com.example.sino.databinding.FragmentSettingBinding;
import com.example.sino.model.db.User;
import com.example.sino.model.documentversion.DocumentVersion;
import com.example.sino.utils.GsonGenerator;
import com.example.sino.utils.JalaliCalendarUtil;
import com.example.sino.utils.common.Util;
import com.example.sino.view.activity.MainActivity;
import com.example.sino.viewmodel.MainViewModel;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;
import uk.co.deanwild.materialshowcaseview.ShowcaseTooltip;

@AndroidEntryPoint
public class SettingFragment extends Fragment {

    private MainViewModel mainViewModel;
    private String inputParam = "";
    private CompositeDisposable compositeDisposable;
    private FragmentSettingBinding binding;
    private String appFileName;
    private UpdateWithAsyncTask updateWithAsyncTask;
    private String updateUrl;
    MainActivity mainActivity;
    private int counter = 0;
    private User user;
    private MainViewModel viewModel;
    private static final String SHOWCASE_ID = "sequence example";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false);
        compositeDisposable = new CompositeDisposable();
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        inputParam = GsonGenerator.getLastDocumentVersionToGson();
        user = SinoApplication.getInstance().getCurrentUser();

        if (user.getAutoLogin()) {
            binding.switchButton.setChecked(false);
        } else {
            binding.switchButton.setChecked(true);
        }

        try {
            getCurrentVersionNo();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        askForPermission();
        getLastDocumentVersion();


        binding.BtnDownloadAndUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (appFileName == null || appFileName == "")
                    return;

                binding.waitProgress.setVisibility(View.VISIBLE);
                binding.BtnUpdateLocal.setVisibility(View.GONE);
                //ResetDownloadUI();
                DoUpdate();
            }
        });

        binding.BtnUpdateLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDownloads(getActivity());
            }
        });


        //********************************

        binding.switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                System.out.println("====b=====" + b);
                user = SinoApplication.getInstance().getCurrentUser();
                user.setAutoLogin(!b);
                viewModel.insertUser(user);
            }
        });

       // MaterialShowcaseView.resetSingleUse(getActivity(), SHOWCASE_ID);
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500);
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(getActivity(), SHOWCASE_ID);
        ShowcaseTooltip toolTip1 = ShowcaseTooltip.build(getActivity())
                .corner(30)
                .textColor(Color.parseColor("#007686"))
                .text(getString(R.string.guide_active_pass));

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder((Activity) getActivity())
                        .setTarget(binding.switchButton)
                        .setToolTip(toolTip1)
                        .setTooltipMargin(30)
                        .setShapePadding(50)
                        .setDismissOnTouch(true)
                        .setMaskColour(getActivity().getColor(R.color.transparentBlack))
                        .build()
        );

        sequence.start();

        return binding.getRoot();
    }

    public void getLastDocumentVersion() {
        binding.waitProgress.setVisibility(View.VISIBLE);
        mainViewModel.getLastDocumentVersionVM(inputParam).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DocumentVersion>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull DocumentVersion documentVersion) {
                        binding.waitProgress.setVisibility(View.GONE);
                        if (documentVersion.result != null) {
                            if (documentVersion.result.document != null) {
                                if (documentVersion.result.document.lastDocumentVersion != null) {
                                    if (documentVersion.result.document.lastDocumentVersion.versionNo != null) {
                                        try {
                                            binding.txtNewVersionNo.setText(documentVersion.result.document.lastDocumentVersion.versionName);
                                            int versionCode = Integer.parseInt(documentVersion.result.document.lastDocumentVersion.versionNo);
                                            updateUrl = documentVersion.result.document.lastDocumentVersion.pathUrl;
                                            //updateUrl = "https://91.98.112.159:54542/sino/sinoempty.png";
                                            //updateUrl = "http://www.appsapk.com/downloading/latest/Barcode%20Scanner-1.2.apk";

                                            System.out.println("updateUrl====" + updateUrl);

                                            int currentVersion = BuildConfig.VERSION_CODE;
                                            System.out.println("currentVersion====" + currentVersion);
                                            if (currentVersion < versionCode) {
                                                if (updateUrl != null) {
                                                    binding.cardNull.setVisibility(View.GONE);
                                                    appFileName = GetFileNameFromUrl(updateUrl);
                                                    try {
                                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                                                        Date date = sdf.parse(documentVersion.result.document.lastDocumentVersion.versionDate);
                                                        Calendar calendar = Calendar.getInstance();
                                                        calendar.setTime(date);
                                                        JalaliCalendarUtil jalaliCalendarUtil = new JalaliCalendarUtil(calendar);
                                                        binding.txtNewVersionDate.setText(jalaliCalendarUtil.getIranianYear() + "/" + jalaliCalendarUtil.getIranianMonth() + "/" + jalaliCalendarUtil.getIranianDay());
                                                        binding.cardNewVersion.setVisibility(View.VISIBLE);
                                                        Util.presentShowcaseView(getActivity(),binding.BtnDownloadAndUpdate,getString(R.string.guide_update));
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }
                                                } else {
                                                    binding.cardNull.setVisibility(View.VISIBLE);
                                                    binding.cardNewVersion.setVisibility(View.GONE);
                                                }
                                            } else {
                                                binding.cardNull.setVisibility(View.VISIBLE);
                                                binding.cardNewVersion.setVisibility(View.GONE);
                                            }
                                        } catch (Exception e) {
                                            Log.e("TAG", e.getLocalizedMessage());
                                        }

                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        System.out.println("onError===" + e.getLocalizedMessage());
                        binding.waitProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete===");
                        binding.waitProgress.setVisibility(View.GONE);
                    }
                });
    }

    public String GetFileNameFromUrl(String urlString) {
        return urlString.substring(urlString.lastIndexOf('/') + 1).split("\\?")[0].split("#")[0];
    }

    private void DoUpdate() {
        updateWithAsyncTask = new UpdateWithAsyncTask();
        updateWithAsyncTask.execute(updateUrl);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void ResetDownloadUI() {

        //SwitchBusyIcon(true);
    }

    class UpdateWithAsyncTask extends AsyncTask<String, Integer, String> {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected String doInBackground(String... params) {
            try {


                TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                }};

                SSLContext sc = null;
                try {
                    sc = SSLContext.getInstance("SSL");
                    sc.init(null, trustAllCerts, new SecureRandom());
                    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                    HostnameVerifier allHostsValid = new HostnameVerifier() {
                        @Override
                        public boolean verify(String s, SSLSession sslSession) {
                            return true;
                        }
                    };

                    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                }

                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setUseCaches(false);
                connection.connect();


                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode() + " " + connection.getResponseMessage();
                }

                File downloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File outputFile = new File(downloadPath.getPath(), appFileName);
                if (outputFile.exists()) {
                    outputFile.delete();
                }

                int lenghtOfFile = connection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(), 1024);
                OutputStream output = new FileOutputStream(downloadPath.toString() + "/" + appFileName);
                byte data[] = new byte[1024];
                long total = 0;
                int count;
                binding.stateLabel.setText("");

                int prog = 0;


                while ((count = input.read(data)) != -1) {
                    total += count;
                    int percent = (int) (total / lenghtOfFile); //0~100
                    publishProgress(percent);
                    output.write(data, 0, count);

                }
                output.flush();
                output.close();
                input.close();
            } catch (IOException e) {
                Log.d("mark", "Download io Error:" + e.getMessage());
            } catch (SecurityException e) {
                Log.d("mark", "Download security Error:" + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            binding.stateLabel.setText("در حال دانلود ... ");
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result == null) {
                binding.stateLabel.setText("دانلود با موفقیت انجام شد");
                //SwitchBusyIcon(false);
                binding.BtnUpdateLocal.setVisibility(View.VISIBLE);
                binding.BtnDownloadAndUpdate.setVisibility(View.GONE);
                binding.waitProgress.setVisibility(View.GONE);
                binding.BtnUpdateLocal.setVisibility(View.VISIBLE);
                // DoInstall();
            } else {
                binding.stateLabel.setText(result);
            }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            long b = getSizeInMB(values[0]);
            System.out.println("values.length====" + values.length);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            // SwitchBusyIcon(false);
            binding.stateLabel.setText("downloadCancel");
        }
    }

    private void askForPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int REQUEST_CODE = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

            for (String str : permissions) {
                if (getActivity().checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    this.requestPermissions(permissions, REQUEST_CODE);
                    return;
                }
            }
        }
    }

    public static float convertBYteToMegaByte(float BYte) {
        return (float) (BYte * 9.5367e-7);
    }

    public long getSizeInMB(long BYte) {

        long sizeInKB = BYte / 1024;
        long sizeInMBInt = sizeInKB / 1024;

        return sizeInMBInt;
    }

    @Override
    public void onAttach(@androidx.annotation.NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    public void getCurrentVersionNo() throws PackageManager.NameNotFoundException {
        long installed = getActivity()
                .getPackageManager()
                .getPackageInfo(getActivity().getPackageName(), 0)
                .firstInstallTime;
        binding.txtCurrentVersionNo.setText(BuildConfig.VERSION_NAME);

        String currentVersionName = BuildConfig.VERSION_NAME;
        binding.txtInstallDate.setText(currentVersionName);
    }


    public static void openDownloads(@NonNull Activity activity) {
        if (isSamsung()) {
            Intent intent = activity.getPackageManager()
                    .getLaunchIntentForPackage("com.sec.android.app.myfiles");
            intent.setAction("samsung.myfiles.intent.action.LAUNCH_MY_FILES");
            intent.putExtra("samsung.myfiles.intent.extra.START_PATH",
                    getDownloadsFile().getPath());
            activity.startActivity(intent);
        } else activity.startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
    }

    public static boolean isSamsung() {
        String manufacturer = Build.MANUFACTURER;
        if (manufacturer != null) return manufacturer.toLowerCase().equals("samsung");
        return false;
    }

    public static File getDownloadsFile() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        compositeDisposable.clear();
    }
}