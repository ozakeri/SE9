package com.example.sino.view.fragment.inspection;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sino.R;
import com.example.sino.SinoApplication;
import com.example.sino.databinding.FragmentFieldInspectionBinding;
import com.example.sino.db.entity.AttachFile;
import com.example.sino.enumtype.SendingStatusEn;
import com.example.sino.model.carinfo.SuccessCarInfoBean;
import com.example.sino.model.complaintreport.ComplaintReport;
import com.example.sino.model.db.User;
import com.example.sino.model.enumType.EntityNameEn;
import com.example.sino.model.form.FormRequentBean;
import com.example.sino.utils.GsonGenerator;
import com.example.sino.utils.common.Constant;
import com.example.sino.utils.services.ApiServiceAsync;
import com.example.sino.utils.services.LocationUpdatesService;
import com.example.sino.view.adapter.emdadadapter.EmdadAttachFileAdapter;
import com.example.sino.viewmodel.DatabaseViewModel;
import com.example.sino.viewmodel.MainViewModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class FieldInspectionFragment extends Fragment {

    private FragmentFieldInspectionBinding binding;
    private static final int MY_PERMISSIONS_REQUEST = 100;
    private static final int REQUEST_CAMERA = 1;
    private Uri mCapturedImageURI;
    private String path;
    private DatabaseViewModel databaseViewModel;
    private Location location;
    private MainViewModel mainViewModel;
    private String inputParam = "";
    private User user;
    private ComplaintReport complaintReport;
    private AttachFile attachFile;
    private ApiServiceAsync apiServiceAsync;
    private int counter = 1;
    private CompositeDisposable disposable;
    private List<AttachFile> attachFilesList;

    //record
    private int statusRecord = 0;
    private Chronometer recordTimer;
    private MediaRecorder mediaRecorder;
    private ImageButton record_btn;
    private boolean isNotRecording = true;
    private String recordFile;
    private static final int MY_PERMISSIONS_REQUEST_VOICE = 101;
    private String fileName = "";
    private String pathStr = "";
    private boolean isRecord = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_field_inspection, container, false);
        databaseViewModel = new ViewModelProvider(this).get(DatabaseViewModel.class);
        binding.codeTV.setText("کد خودرو");
        binding.layoutSelectedPlateText.setVisibility(View.VISIBLE);
        binding.layoutEnterCode.setVisibility(View.GONE);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        user = SinoApplication.getInstance().getCurrentUser();
        disposable = new CompositeDisposable();

        complaintReport = new ComplaintReport();
        complaintReport.setId(Long.valueOf(new Date().getTime() + user.getServerUserId().toString()));
        apiServiceAsync = new ApiServiceAsync();

        autoSelectText();
        getLocation();

        // getCompanyInfo();
        // getCarInfo();

        //*****************************************
        checkAudioPermission();

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(
                            new String[]{Manifest.permission
                                    .WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_VOICE);
                }
            }

        }
        //*****************************************


        binding.selectedReportType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButtonCar:
                        binding.codeTV.setText("کد خودرو");
                        binding.layoutSelectedPlateText.setVisibility(View.VISIBLE);
                        binding.layoutEnterCode.setVisibility(View.GONE);
                        break;

                    case R.id.radioButtonRepresentation:
                        binding.codeTV.setText("کد نمایندگی");
                        binding.layoutSelectedPlateText.setVisibility(View.GONE);
                        binding.layoutEnterCode.setVisibility(View.VISIBLE);
                        break;

                    case R.id.radioButtonEmdad:
                        binding.codeTV.setText("کد امدادگر");
                        binding.layoutSelectedPlateText.setVisibility(View.GONE);
                        binding.layoutEnterCode.setVisibility(View.VISIBLE);
                        break;

                    case R.id.radioButtonTechnicalExpert:
                        binding.codeTV.setText("کد کارشناس");
                        binding.layoutSelectedPlateText.setVisibility(View.GONE);
                        binding.layoutEnterCode.setVisibility(View.VISIBLE);
                        break;

                    case R.id.radioButtonOther:
                        binding.codeTV.setText("سایر");
                        binding.layoutSelectedPlateText.setVisibility(View.GONE);
                        binding.layoutEnterCode.setVisibility(View.GONE);
                        break;

                }
            }
        });

        binding.attachIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getActivity(),
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat
                        .checkSelfPermission(getActivity(),
                                Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale
                            (getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                            ActivityCompat.shouldShowRequestPermissionRationale
                                    (getActivity(), Manifest.permission.CAMERA)) {

                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(
                                    new String[]{Manifest.permission
                                            .WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                                    MY_PERMISSIONS_REQUEST);
                        }
                    }

                } else {
                    cameraIntent();
                }
            }
        });

        binding.sendReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendComplaintReport();
            }
        });

        binding.imgRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        binding.imgRecord.setBackgroundResource(R.drawable.ic_voice);

        binding.imgRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (statusRecord == 0) {
                    if (checkAudioPermission()) {
                        // Start rec
                        statusRecord = 1;
                        binding.imgRecord.setBackgroundResource(R.drawable.ic_stop);
                        startRecording();
                        isNotRecording = false;
                    }

                } else if (statusRecord == 1) {
                    statusRecord = 0;
                    binding.imgRecord.setBackgroundResource(R.drawable.ic_voice);
                    stopRecording();
                    isNotRecording = true;
                }

            }
        });

        return binding.getRoot();
    }

    public void cameraIntent() {
        String fileName = "temp.jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        mCapturedImageURI = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                path = getPathCamera();
                isRecord = false;
                saveAttachImageFile(path);
            }
        }
    }

    /*getPathCamera */
    private String getPathCamera() {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(mCapturedImageURI, projection, null, null, null);
        int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index_data);
    }


    /*public void refreshAttachAdapter() {
        databaseViewModel.getReportAttachFileList(complaintReport.getId()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<AttachFile>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull List<AttachFile> attachFiles) {
                        System.out.println("size====" + attachFiles.size());
                        attachFilesList = attachFiles;
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                        binding.attachRecyclerView.setLayoutManager(linearLayoutManager);
                        binding.attachRecyclerView.setAdapter(new EmdadAttachFileAdapter(attachFiles, databaseViewModel));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }*/

    public void getLocation() {

        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                checkBackgroundLocation()) {
            final Intent intent = new Intent(getActivity(), LocationUpdatesService.class);
            getActivity().startService(intent);
            System.out.println("intent====" + intent.getData());
            System.out.println("intent====" + intent);

        } else {
            requsetPermission();
        }
    }

    private void requsetPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                requestPermissions(new String[]
                        {Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            } else {
                requestPermissions(new String[]
                        {Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

            }
        }
    }

    public boolean checkBackgroundLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    public void showDialogWords() {
        final String[] strName = new String[1];
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item);
        arrayAdapter.add("الف");
        arrayAdapter.add("ج");
        arrayAdapter.add("ع");
        arrayAdapter.add("ب");
        arrayAdapter.add("ی");
        arrayAdapter.add("ل");
        arrayAdapter.add("ا");
        arrayAdapter.add("ت");
        arrayAdapter.add("ن");
        arrayAdapter.add("م");
        arrayAdapter.add("ه");
        arrayAdapter.add("ص");
        arrayAdapter.add("ط");


        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                strName[0] = arrayAdapter.getItem(which);
                System.out.println("strName=====" + strName[0]);
                binding.includeLinearLayout.txtThree.setText(strName[0]);
                binding.includeLinearLayout.txtFour.requestFocus();
            }
        });
        builderSingle.show();
    }

    public void autoSelectText() {
        binding.includeLinearLayout.txtOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.includeLinearLayout.txtOne.getText().length() == 2) {
                    binding.includeLinearLayout.txtTwo.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.includeLinearLayout.txtTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.includeLinearLayout.txtTwo.getText().length() == 3) {
                    binding.includeLinearLayout.txtThree.performClick();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.includeLinearLayout.txtThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogWords();
            }
        });
    }

    public void getCompanyInfo() {
        inputParam = GsonGenerator.getCompanyInfo(user.getUsername(), user.getBisPassword(), "1111");
        mainViewModel.getCompanyInfo(inputParam).subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.rxjava3.core.Observer<SuccessCarInfoBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull SuccessCarInfoBean successCarInfoBean) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void sendComplaintReport() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        //complaintReport.setId(3L);
        complaintReport.setIdentifier("NAB374527NA003253");
        complaintReport.setEntityNameEn(EntityNameEn.Car.ordinal());
        complaintReport.setReportStr("testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttest");
        complaintReport.setReportCode("");
        complaintReport.setSendingStatusDate(new Date());
        complaintReport.setLatitude("0.0");
        complaintReport.setLongitude("0.0");


        inputParam = GsonGenerator.sendComplaintReport(user.getUsername(), user.getBisPassword(), complaintReport);
        mainViewModel.sendComplaintReport(inputParam).subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.rxjava3.core.Observer<FormRequentBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull FormRequentBean formRequentBean) {
                        complaintReport.setServerId(formRequentBean.result.savedComplaintReport.id);
                        try {
                            complaintReport.setDeliverDate(simpleDateFormat.parse(formRequentBean.result.savedComplaintReport.dateCreation));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        complaintReport.setDeliverIs(Boolean.TRUE);
                        complaintReport.setSendingStatusEn(SendingStatusEn.Sent.ordinal());
                        complaintReport.setSendingStatusDate(new Date());

                        databaseViewModel.insertComplaintReportByParam(complaintReport);

                        List<AttachFile> attachFileList = databaseViewModel.getPendingAttachFileByEntityId(183L, complaintReport.getId());

                        for (AttachFile attachFile : attachFileList) {
                            attachFile.setServerEntityId(complaintReport.getId());
                            attachFile.setSendingStatusEn(SendingStatusEn.InProgress.ordinal());
                            databaseViewModel.updateAttachFileByParam(attachFile);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        complaintReport.setSendingStatusEn(SendingStatusEn.Fail.ordinal());
                        complaintReport.setSendingStatusDate(new Date());
                        databaseViewModel.updateComplaintReportByParam(complaintReport);
                    }

                    @Override
                    public void onComplete() {

                        System.out.println("pathStr==== " + pathStr);
                        System.out.println("path==== " + path);

                       sendComplaintAttachment();

                    }
                });
    }

    public void saveAttachImageFile(String filePath) {
        File file = new File(String.valueOf(filePath));

        if (!isRecord){
            file = saveBitmapToFile(file);
        }
        attachFile = new AttachFile();
        String userFileName = file.getName();

        attachFile.setAttachFileUserFileName(userFileName);
        attachFile.setSendingStatusDate(new Date());
        attachFile.setSendingStatusEn(SendingStatusEn.Pending.ordinal());
        attachFile.setEntityNameEn(EntityNameEn.ComplaintReport.ordinal());
        attachFile.setServerAttachFileSettingId((long) 101);
        attachFile.setEntityId(complaintReport.getId());
        attachFile.setServerEntityId(complaintReport.getId());
        attachFile.setAttachFileLocalPath(filePath);
        databaseViewModel.insertAttachFileRepoVM(attachFile);

        long length = file.length();
        length = length / 1024;
        System.out.println("File Path : " + file.getPath() + ", File size : " + length + " KB");
        String filePostfix = userFileName.substring(userFileName.indexOf("."), userFileName.length());
        String path = Environment.getExternalStorageDirectory().toString() + Constant.DEFAULT_OUT_PUT_DIR + Constant.DEFAULT_IMG_OUT_PUT_DIR;
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }



        String newFilePath = path + "/" + attachFile.getId() + filePostfix;

        try {
            InputStream inputStream = new FileInputStream(file);
            OutputStream outputStream = new FileOutputStream(newFilePath);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2; //try to decrease decoded image
            options.inPurgeable = true; //purgeable to disk

            if (!isRecord){
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                bitmap.compress(Bitmap.CompressFormat.PNG, 40, outputStream); //compressed bitmap to file
            }
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
            }
            inputStream.close();
            outputStream.close();
            Long fileSize = new File(newFilePath).length();
            attachFile.setAttachFileSize(fileSize.intValue() / 1024);
            databaseViewModel.updateAttachFileByParam(attachFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //sendComplaintAttachment();

        //refreshAttachAdapter();

    }

    public void sendComplaintAttachment() {

        apiServiceAsync.resumeAttachFileList(user, getActivity(), attachFilesList, databaseViewModel);

        System.out.println("complaintReport.getId()====" + complaintReport.getId());
        System.out.println("complaintReport.getId()====" + complaintReport.getServerId());
   /*     databaseViewModel.getReportAttachFileList(complaintReport.getId()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<AttachFile>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull List<AttachFile> attachFiles) {
                        if (!attachFiles.isEmpty()) {
                            System.out.println("=========onNext====");
                            apiServiceAsync.resumeAttachFileList(user, getActivity(), attachFiles, databaseViewModel);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });*/

    }


    private void startRecording() {
        binding.recordTimer.setBase(SystemClock.elapsedRealtime());
        binding.recordTimer.start();
        binding.txtFileName.setText("");

        String path = Environment.getExternalStorageDirectory().toString() + "/BisInspection" + "/audio";
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String recordPath = getActivity().getExternalFilesDir("/").getAbsolutePath();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_mm_ss");
        Date now = new Date();

        recordFile = "Recording_" + formatter.format(now) + ".mp3";

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(path + "/" + recordFile);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        fileName = recordFile;
        pathStr = path + "/" + recordFile;

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        mediaRecorder.start();
    }

    private void stopRecording() {
        binding.recordTimer.stop();

        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        binding.txtFileName.setText(fileName);

        isRecord = true;
        saveAttachImageFile(pathStr);
    }

    private boolean checkAudioPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, Constant.RECORD_AUDIO_PERMISSION);
            return false;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (!isNotRecording)
            startRecording();
    }

    public File saveBitmapToFile(File file) {
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE = 50;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);

            return file;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }
}