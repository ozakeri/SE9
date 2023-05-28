package com.example.sino.view.fragment.enterexit;

import static com.example.sino.view.fragment.SettingFragment.getDownloadsFile;
import static com.example.sino.view.fragment.SettingFragment.isSamsung;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sino.R;
import com.example.sino.SinoApplication;
import com.example.sino.databinding.FragmentManageEEBinding;
import com.example.sino.model.carinfo.Car;
import com.example.sino.model.carinfo.SuccessCarInfoBean;
import com.example.sino.model.dailyEvent.DailyEvent;
import com.example.sino.model.dailyEvent.DailyEventRespons;
import com.example.sino.model.db.User;
import com.example.sino.utils.GlobalValue;
import com.example.sino.utils.GsonGenerator;
import com.example.sino.utils.common.Util;
import com.example.sino.view.activity.MainActivity;
import com.example.sino.view.adapter.car.DailyEventListAdapter;
import com.example.sino.view.fragment.SettingFragment;
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
import java.util.List;

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
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;
import uk.co.deanwild.materialshowcaseview.ShowcaseTooltip;

@AndroidEntryPoint
public class ManageEEFragment extends Fragment {

    private FragmentManageEEBinding binding;
    private NavOptions.Builder navBuilder;
    private String plateText;
    private String inputParam = "";
    private User user;
    private MainViewModel viewModel;
    private SuccessCarInfoBean successCarInfoBeanCopy;
    private List<Car> carList;
    //private int eventType = -1; //enter
    private int typeGetCar = -1;
    private List<DailyEvent> eventList = null;
    private DailyEventListAdapter adapter;
    private static final String SHOWCASE_ID = "SHOWCASE_ID_TWO";
    private String appFileName;
    private String updateUrl = "https://91.92.131.11:54542/guide/ورود_خروج.pdf";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_manage_e_e, container, false);
        user = SinoApplication.getInstance().getCurrentUser();
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        navBuilder = new NavOptions.Builder();
        navBuilder.setEnterAnim(R.anim.slide_from_left).setExitAnim(R.anim.slide_out_right).setPopEnterAnim(R.anim.slide_from_right).setPopExitAnim(R.anim.slide_out_left);

        binding.detectPlate.setOnClickListener(onCameraDemoClicked);
        binding.handPlate.setOnClickListener(handPlateClicked);
        binding.imgEventList.setOnClickListener(getListClicked);
        getList();

        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500);
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(getActivity(), SHOWCASE_ID);
        ShowcaseTooltip toolTip1 = ShowcaseTooltip.build(getActivity())
                .corner(30)
                .textColor(Color.parseColor("#007686"))
                .text(getString(R.string.guide_detect_plate_auto));

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder((Activity) getActivity())
                        .setTarget(binding.detectPlate)
                        .setToolTip(toolTip1)
                        .setTooltipMargin(30)
                        .setShapePadding(50)
                        .setDismissOnTouch(true)
                        .setMaskColour(getActivity().getColor(R.color.transparentBlack))
                        .build()
        );

        ShowcaseTooltip toolTip2 = ShowcaseTooltip.build(getActivity())
                .corner(30)
                .textColor(Color.parseColor("#007686"))
                .text(getString(R.string.guide_detect_plate_hand));

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder((Activity) getActivity())
                        .setTarget(binding.handPlate)
                        .setToolTip(toolTip2)
                        .setTooltipMargin(30)
                        .setShapePadding(50)
                        .setDismissOnTouch(true)
                        .setMaskColour(getActivity().getColor(R.color.transparentBlack))
                        .build()
        );

        ShowcaseTooltip toolTip3 = ShowcaseTooltip.build(getActivity())
                .corner(30)
                .textColor(Color.parseColor("#007686"))
                .text(getString(R.string.showcase_event_list));

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder((Activity) getActivity())
                        .setTarget(binding.imgEventList)
                        .setToolTip(toolTip3)
                        .setTooltipMargin(30)
                        .setShapePadding(50)
                        .withRectangleShape()
                        .setDismissOnTouch(true)
                        .setMaskColour(getActivity().getColor(R.color.transparentBlack))
                        .build()
        );

        ShowcaseTooltip toolTip4 = ShowcaseTooltip.build(getActivity())
                .corner(30)
                .textColor(Color.parseColor("#007686"))
                .text(getString(R.string.showcase_download_guide));

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder((Activity) getActivity())
                        .setTarget(binding.txtGuide)
                        .setToolTip(toolTip4)
                        .setTooltipMargin(30)
                        .setShapePadding(50)
                        .withRectangleShape()
                        .setDismissOnTouch(true)
                        .setMaskColour(getActivity().getColor(R.color.transparentBlack))
                        .build()
        );

        sequence.start();
        /*OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                if (binding.frameLayout.getVisibility() == View.VISIBLE){
                    binding.frameLayout.setVisibility(View.GONE);
                }else {
                    NavHostFragment.findNavController(ManageEEFragment.this).navigate(R.id.manageEEFragment, null, navBuilder.build());

                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);*/

        binding.txtGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                appFileName = GetFileNameFromUrl(updateUrl);

                DownloadFile downloadFile = new DownloadFile();
                downloadFile.execute(updateUrl);
            }
        });

        return binding.getRoot();
    }

    private View.OnClickListener getListClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (eventList != null && eventList.size()>0){
                showDialog(eventList);
            }
        }
    };
    private View.OnClickListener onCameraDemoClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            GlobalValue.ManageEEFragment = null;
            typeGetCar = 1;
            NavHostFragment.findNavController(ManageEEFragment.this).navigate(R.id.captureActivity, null, navBuilder.build());
        }
    };

    private View.OnClickListener handPlateClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            GlobalValue.ManageEEFragment = null;
            typeGetCar = 0;
            Bundle bundle = new Bundle();
            //bundle.putInt("eventType", eventType);
            bundle.putInt("typeGetCar", typeGetCar);
            NavHostFragment.findNavController(ManageEEFragment.this).navigate(R.id.enterHandPlateFragment, bundle, navBuilder.build());
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (data != null) {
                plateText = data.getStringExtra("plateText");

                System.out.println("plateText2======" + plateText);

            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();

        System.out.println("plateNo=====" + GlobalValue.plateNo);
        System.out.println("onResume=====");
        if (GlobalValue.plateNo != null) {
            Bundle bundle = new Bundle();
            bundle.putString("plateNo" ,GlobalValue.plateNo);
            bundle.putInt("typeGetCar", typeGetCar);
            GlobalValue.plateNo = null;
            NavHostFragment.findNavController(ManageEEFragment.this).navigate(R.id.enterHandPlateFragment, bundle, navBuilder.build());
        }
    }

    public void getList(){
        ProgressDialog dialog = ProgressDialog.show(getActivity(), "",getString(R.string.please_wait), true);
        dialog.show();
        inputParam = GsonGenerator.getDailyEventList(user.getUsername(), user.getBisPassword(),GlobalValue.companyCode);
        viewModel.dailyEvent_GetList_CarEnter_PS0(inputParam).subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DailyEventRespons>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull DailyEventRespons dailyEventRespons) {
                        if (dailyEventRespons.success != null){
                            if (dailyEventRespons.result != null){
                                eventList = dailyEventRespons.result.dailyEventList;

                                if (getActivity() != null) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        public void run() {
                                            dialog.dismiss();
                                            binding.imgEventList.setText("خودرو های بلاتکلیف " + " ( " +eventList.size()+ " ) "  );
                                        }
                                    });
                                }
                            }
                        }else if (dailyEventRespons.ERROR != null){

                            if (getActivity() != null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        dialog.dismiss();
                                        Toast toast = Toast.makeText(getActivity(), dailyEventRespons.ERROR, Toast.LENGTH_LONG);
                                        Util.showToast(toast, getActivity());
                                        toast.show();
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void showDialog(List<DailyEvent> eventList){
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_event_list);
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.show();

        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView);

        adapter = new DailyEventListAdapter(eventList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new DailyEventListAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                DailyEvent dailyEvent = eventList.get(position);
                //getProServiceByCarId(car.id);
                Bundle bundle = new Bundle();
                //bundle.putInt("dailEvn_id", dailyEvent.id);
                bundle.putParcelable("dailyEvent", dailyEvent);
                NavHostFragment.findNavController(ManageEEFragment.this).navigate(R.id.enterHandPlateFragment, bundle, navBuilder.build());
                dialog.dismiss();
            }
        });
    }

    class DownloadFile extends AsyncTask<String, Integer, String> {

        ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "", getString(R.string.please_wait_for_download), true);

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected String doInBackground(String... params) {
            try {
                progressDialog.show();

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
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result == null) {
                progressDialog.dismiss();
                Toast toast = Toast.makeText(getActivity(), getString(R.string.success_request), Toast.LENGTH_LONG);
                Util.showToast(toast, getActivity());
                toast.show();
                //SwitchBusyIcon(false);
               // binding.BtnUpdateLocal.setVisibility(View.VISIBLE);
                //binding.BtnDownloadAndUpdate.setVisibility(View.GONE);
                //binding.waitProgress.setVisibility(View.GONE);
               // binding.BtnUpdateLocal.setVisibility(View.VISIBLE);
                // DoInstall();
                openDownloads(getActivity());
            } else {
                Toast toast = Toast.makeText(getActivity(), result, Toast.LENGTH_LONG);

                Util.showToast(toast, getActivity());
                toast.show();
            }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            // SwitchBusyIcon(false);
            //binding.stateLabel.setText("downloadCancel");
        }
    }

    public String GetFileNameFromUrl(String urlString) {
        return urlString.substring(urlString.lastIndexOf('/') + 1).split("\\?")[0].split("#")[0];
    }

    public static void openDownloads(@io.reactivex.rxjava3.annotations.NonNull Activity activity) {
        if (isSamsung()) {
            Intent intent = activity.getPackageManager()
                    .getLaunchIntentForPackage("com.sec.android.app.myfiles");
            intent.setAction("samsung.myfiles.intent.action.LAUNCH_MY_FILES");
            intent.putExtra("samsung.myfiles.intent.extra.START_PATH",
                    getDownloadsFile().getPath());
            activity.startActivity(intent);
        } else activity.startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
    }
}