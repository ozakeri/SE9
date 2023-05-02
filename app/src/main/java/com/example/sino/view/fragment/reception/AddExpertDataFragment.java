package com.example.sino.view.fragment.reception;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.example.sino.R;
import com.example.sino.SinoApplication;
import com.example.sino.databinding.FragmentAddExpertDataBinding;
import com.example.sino.db.entity.AttachFile;
import com.example.sino.enumtype.SendingStatusEn;
import com.example.sino.model.db.User;
import com.example.sino.model.enumType.EntityNameEn;
import com.example.sino.model.reception.JsonArrayAttach;
import com.example.sino.model.reception.ProServiceResponse;
import com.example.sino.utils.GlobalValue;
import com.example.sino.utils.GsonGenerator;
import com.example.sino.utils.common.Constant;
import com.example.sino.utils.services.ApiServiceAsync;
import com.example.sino.view.activity.MainActivity;
import com.example.sino.viewmodel.DatabaseViewModel;
import com.example.sino.viewmodel.MainViewModel;
import com.github.gcacace.signaturepad.views.SignaturePad;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class AddExpertDataFragment extends Fragment {

    private FragmentAddExpertDataBinding binding;

    //record
    private int statusRecord = 0;
    private Chronometer recordTimer;
    private MediaRecorder mediaRecorder;
    private ImageButton record_btn;
    private boolean isNotRecording = true;
    private String recordFile;
    private static final int MY_PERMISSIONS_REQUEST = 100;
    private String fileName = "";
    private String pathStr = "";
    private User user;
    private DatabaseViewModel databaseViewModel;
    private AttachFile attachFile;
    private String signPath = null;
    private String audioPath = null;
    private ApiServiceAsync apiServiceAsync;
    private NavOptions.Builder navBuilder;
    private String inputParam = "";
    private MainViewModel viewModel;
    private String proModelId = "94415";
    private SharedPreferences sharedPreferences;
    //private String proSrvId;
    //private String prcDataId;
    private Bundle bundle;
    // private boolean isEdit;
    private MainViewModel mainViewModel;
    private MediaPlayer mediaPlayer;
    private String attachFileSignId;
    private String attachFileAudioId;
    public List<JsonArrayAttach> jsonArrayAttachCopy = null;
    //private String prcSetId;
    private byte[] bytes;
    private Bitmap bitmap;
    private boolean confirm = false;
    //private boolean isConfirm = false;
    private boolean signPathIsChanged = false;
    private boolean audioPathIsChanged = false;
    private boolean editTextIsChanged = false;
    private MainActivity mainActivity;
    private long elapsedMillis = 0;
    private Bitmap signatureBitmap;

    @Override
    public void onViewCreated(@androidx.annotation.NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bundle = new Bundle();
        checkAudioPermission();
        mainActivity = (MainActivity) getActivity();
        apiServiceAsync = new ApiServiceAsync();
        user = SinoApplication.getInstance().getCurrentUser();
        databaseViewModel = new ViewModelProvider(this).get(DatabaseViewModel.class);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        sharedPreferences = getActivity().getSharedPreferences("proModelId", MODE_PRIVATE);
        // proModelId = sharedPreferences.getString("proModelId", "");
        mediaPlayer = new MediaPlayer();
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putInt(Constant.STATE_Reception, 4);
        myEdit.commit();

        binding.txtPlate.setText(GlobalValue.plateText);
        binding.txtTip.setText(GlobalValue.carType);
        binding.recordTimer.setTextColor(getResources().getColor(R.color.mdtp_done_text_color_dark_disabled));
        navBuilder = new NavOptions.Builder();
        navBuilder.setEnterAnim(R.anim.slide_from_left).setExitAnim(R.anim.slide_out_right).setPopEnterAnim(R.anim.slide_from_right).setPopExitAnim(R.anim.slide_out_left);

        binding.editTextTextPersonName.setText("");
        binding.editTextTextPersonName.setHint("توضیحات");
        if (GlobalValue.description != null) {
            binding.editTextTextPersonName.setText(GlobalValue.description);
        }

        binding.editTextTextPersonName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editTextIsChanged = true;
                GlobalValue.description = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


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
                            MY_PERMISSIONS_REQUEST);
                }
            }

        }

        binding.lottieRecord.setVisibility(View.GONE);
        binding.lottieRecordTwo.setVisibility(View.GONE);


        binding.carOpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signDialog();
            }
        });


        binding.imgRecord.setBackgroundResource(R.drawable.ic_voice);

        binding.recordTimer.setText("رکورد صدای کارشناس");

        binding.imgRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.reset();
                }
                if (statusRecord == 0) {
                    if (checkAudioPermission()) {
                        // Start rec
                        statusRecord = 1;
                        binding.imgRecord.setBackgroundResource(R.drawable.ic_stop);
                        startRecording();
                        isNotRecording = false;
                        binding.lottieRecord.setVisibility(View.VISIBLE);
                        binding.lottieRecordTwo.setVisibility(View.VISIBLE);
                    }

                } else if (statusRecord == 1) {

                    elapsedMillis = SystemClock.elapsedRealtime() - binding.recordTimer.getBase();
                    elapsedMillis = elapsedMillis / 1000;

                    if (elapsedMillis <= 10) {
                        Toast.makeText(getActivity(), "حداقل صدای ضبط شده 10 ثانیه می باشد", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    statusRecord = 0;
                    binding.imgRecord.setBackgroundResource(R.drawable.ic_voice);
                    try {
                        stopRecording();
                    } catch (RuntimeException stopException) {
                        // handle cleanup here
                    }
                    isNotRecording = true;
                    binding.lottieRecord.setVisibility(View.GONE);
                    binding.lottieRecordTwo.setVisibility(View.GONE);

                }

            }
        });

        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm = true;

                if (statusRecord != 0) {
                    Toast.makeText(getActivity(), "در حال رکورد صدا...", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (audioPath == null) {
                    Toast.makeText(getActivity(), "افزودن صدای مشتری الزامیست", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (signPath == null) {
                    Toast.makeText(getActivity(), "افزودن امضاء الزامیست", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (GlobalValue.pathFront == null){
                    Toast.makeText(getActivity(), "تصویر جلوی خودرو الزامیست", Toast.LENGTH_LONG).show();
                    return;
                }

                if (GlobalValue.pathRight == null){
                    Toast.makeText(getActivity(), "تصویر سمت راست خودرو الزامیست", Toast.LENGTH_LONG).show();
                    return;
                }

                if (GlobalValue.pathBack == null){
                    Toast.makeText(getActivity(), "تصویر عقب خودرو الزامیست", Toast.LENGTH_LONG).show();
                    return;
                }

                if (GlobalValue.pathLeft == null){
                    Toast.makeText(getActivity(), "تصویر سمت چپ خودرو الزامیست", Toast.LENGTH_LONG).show();
                    return;
                }

                if (GlobalValue.pathKm == null){
                    Toast.makeText(getActivity(), "تصویر کیلومتر خودرو الزامیست", Toast.LENGTH_LONG).show();
                    return;
                }
                saveOrEdit();
            }
        });

      /*  binding.btnNonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm = false;
                confirmPrcData();
            }
        });*/


        /*if (isEdit) {
            binding.btnEdit.setText("ویرایش");

            binding.btnConfirm.setVisibility(View.VISIBLE);
            binding.btnNonConfirm.setVisibility(View.VISIBLE);
            binding.btnEdit.setVisibility(View.VISIBLE);

            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        getProSrvAttachFileSign();
                    }
                });
            }

        } else {
            binding.btnEdit.setText("ذخیره");
            binding.imgDelete.setVisibility(View.GONE);
            binding.imgDeleteRecord.setVisibility(View.GONE);
            binding.cardViewPlayer.setVisibility(View.GONE);
            binding.btnConfirm.setVisibility(View.GONE);
            binding.btnNonConfirm.setVisibility(View.GONE);
            binding.btnEdit.setVisibility(View.GONE);
        }*/

        if (GlobalValue.isConfirm) {
            binding.btnConfirm.setVisibility(View.GONE);
            // binding.btnNonConfirm.setVisibility(View.GONE);
            binding.btnEdit.setVisibility(View.GONE);
            binding.imgDelete.setVisibility(View.GONE);
            binding.imgDeleteRecord.setVisibility(View.GONE);
            binding.gotoTakePic.setText("مشاهده تصاویر");
            //binding.signaturePad.setEnabled(false);
            binding.carOpenDialog.setVisibility(View.GONE);
        } else if (GlobalValue.isEdit) {
            binding.gotoTakePic.setText("ویرایش تصاویر");
        }


        boolean isPlay = false;
        if (isPlay) {
            binding.imagePlay.setBackgroundResource(R.drawable.pause);
        } else {
            binding.imagePlay.setBackgroundResource(R.drawable.play);
        }

        binding.lottieRecordTwo2.pauseAnimation();
        binding.imagePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    binding.imagePlay.setBackgroundResource(R.drawable.play);
                    binding.lottieRecordTwo2.pauseAnimation();
                } else {
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                    binding.imagePlay.setBackgroundResource(R.drawable.pause);
                    binding.lottieRecordTwo2.playAnimation();
                }
            }
        });

        binding.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAttachFile("sign", attachFileSignId);
            }
        });

        binding.imgDeleteRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAttachFile("audio", attachFileAudioId);
            }
        });

        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm = false;
                if (statusRecord != 0) {
                    Toast.makeText(getActivity(), "در حال رکورد صدا...", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (audioPath == null) {
                    Toast.makeText(getActivity(), "افزودن صدای مشتری الزامیست", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (signPath == null) {
                    Toast.makeText(getActivity(), "افزودن امضاء الزامیست", Toast.LENGTH_SHORT).show();
                    return;
                }

                saveOrEdit();
            }
        });

        binding.gotoTakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (statusRecord != 0) {
                    Toast.makeText(getActivity(), "در حال رکورد صدا...", Toast.LENGTH_SHORT).show();
                    return;
                }

                gotoTakePicFragment(GlobalValue.isEdit);
            }
        });
    }

    @Override
    public View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_expert_data, container, false);
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void startRecording() {
        binding.recordTimer.setBase(SystemClock.elapsedRealtime());
        binding.recordTimer.start();
        binding.txtFileName.setText("");

        String path;

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU ||
                Build.VERSION.SDK_INT == Build.VERSION_CODES.S_V2 ||
                Build.VERSION.SDK_INT == Build.VERSION_CODES.S ||
                Build.VERSION.SDK_INT == Build.VERSION_CODES.R) {
            path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_AUDIOBOOKS).toString() + "/Sino" + "/audio";
        } else {
            path = Environment.getExternalStorageDirectory().toString() + "/Sino" + "/audio";
        }

        //path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_AUDIOBOOKS).toString() + "/Sino" + "/audio";

        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        fileName = "AUDIO_" + timestamp + ".mp3";
        File file = new File(path, fileName);

        fileName = recordFile;
        pathStr = path + "/" + recordFile;
        System.out.println("pathStr222====" + pathStr);
        audioPath = file.getAbsolutePath();
        audioPathIsChanged = true;
        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
            mediaRecorder.setOutputFile(file.getAbsolutePath());
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void stopRecording() {

        binding.recordTimer.stop();
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        binding.txtFileName.setText(fileName);
        //audioPath = pathStr;
        //saveAttachImageFile(pathStr);

        try {
            mediaPlayer.reset();
            FileInputStream fis = new FileInputStream(audioPath);
            mediaPlayer.setDataSource(fis.getFD());
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        binding.imgPlayPause.setVisibility(View.VISIBLE);
        if (audioPath != null) {
            binding.imgPlayPause.setBackgroundResource(R.drawable.play);
            binding.imgPlayPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        binding.imgPlayPause.setBackgroundResource(R.drawable.play);
                        binding.lottieRecordTwo.setVisibility(View.GONE);
                        binding.lottieRecordTwo.pauseAnimation();
                    } else {
                        mediaPlayer.start();
                        binding.imgPlayPause.setBackgroundResource(R.drawable.pause);
                        binding.lottieRecordTwo.setVisibility(View.VISIBLE);
                        binding.lottieRecordTwo.playAnimation();
                    }
                }
            });
        }
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

    private void tryToSaveImage(Bitmap image) {
        try {

            String path;

            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU ||
                    Build.VERSION.SDK_INT == Build.VERSION_CODES.S_V2 ||
                    Build.VERSION.SDK_INT == Build.VERSION_CODES.S ||
                    Build.VERSION.SDK_INT == Build.VERSION_CODES.R) {
                path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + Constant.DEFAULT_OUT_PUT_DIR + Constant.DEFAULT_SIGN_OUT_PUT_DIR;
            } else {
                path = Environment.getExternalStorageDirectory().toString() + Constant.DEFAULT_OUT_PUT_DIR + Constant.DEFAULT_SIGN_OUT_PUT_DIR;
            }

            // path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + Constant.DEFAULT_OUT_PUT_DIR + Constant.DEFAULT_SIGN_OUT_PUT_DIR;

            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            int quality = 100;
            signPath = path + "/" + "userSign.png";

            signPathIsChanged = true;
            FileOutputStream fos = new FileOutputStream(new File(signPath));
            image.compress(Bitmap.CompressFormat.JPEG, quality, fos);
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveAttachImageFile(String filePath, long attachFileSettingId) {
        File file = new File(String.valueOf(filePath));
        attachFile = new AttachFile();
        String userFileName = file.getName();

        attachFile.setAttachFileUserFileName(userFileName);
        attachFile.setSendingStatusDate(new Date());
        attachFile.setSendingStatusEn(SendingStatusEn.Pending.ordinal());
        attachFile.setEntityNameEn(EntityNameEn.ProcessStrucData.ordinal());
        attachFile.setServerAttachFileSettingId(attachFileSettingId);
        attachFile.setEntityId(Long.valueOf(GlobalValue.prcDataId));
        attachFile.setServerEntityId(Long.valueOf(GlobalValue.prcDataId));
        attachFile.setAttachFileLocalPath(filePath);
        databaseViewModel.insertAttachFileRepoVM(attachFile);

        long length = file.length();
        length = length / 1024;
        System.out.println("File Path : " + file.getPath() + ", File size : " + length + " KB");
        String filePostfix = userFileName.substring(userFileName.indexOf("."), userFileName.length());

        String path;

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU ||
                Build.VERSION.SDK_INT == Build.VERSION_CODES.S_V2 ||
                Build.VERSION.SDK_INT == Build.VERSION_CODES.S ||
                Build.VERSION.SDK_INT == Build.VERSION_CODES.R) {
            path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + Constant.DEFAULT_OUT_PUT_DIR + Constant.DEFAULT_IMG_OUT_PUT_DIR;
        } else {
            path = Environment.getExternalStorageDirectory().toString() + Constant.DEFAULT_OUT_PUT_DIR + Constant.DEFAULT_IMG_OUT_PUT_DIR;
        }

        // path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + Constant.DEFAULT_OUT_PUT_DIR + Constant.DEFAULT_IMG_OUT_PUT_DIR;


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

    }

    private class UploadFile extends AsyncTask<String, String, String> {
        ProgressDialog dialog = ProgressDialog.show(getActivity(), "", "لطفا منتظر بمانید...", true);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();

            System.out.println("=====onPreExecute====");
        }

        @Override
        protected String doInBackground(String... urls) {
            //Copy you logic to calculate progress and call

            if (audioPathIsChanged || signPathIsChanged) {
                if (signPath != null && signPathIsChanged) {
                    saveAttachImageFile(signPath, (long) 1077);
                    apiServiceAsync.resumeAttachFile(user, getActivity(), attachFile, databaseViewModel);

                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                getProSrvAttachFileSign();
                            }
                        });
                    }
                }

                if (audioPath != null && audioPathIsChanged) {
                    try {
                        saveAttachImageFile(audioPath, (long) 1076);
                    } catch (Exception e) {
                        System.out.println(e.getLocalizedMessage());
                    }

                    apiServiceAsync.resumeAttachFile(user, getActivity(), attachFile, databaseViewModel);
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                getProSrvAttachFileAudio();
                            }
                        });
                    }
                }
            }


            System.out.println("=====doInBackground====");
            return null;
        }

        protected void onProgressUpdate(String... progress) {
            System.out.println("=====onProgressUpdate====");
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            Toast.makeText(getActivity(), "درخواست با موفقیت انجام شد", Toast.LENGTH_SHORT).show();
            binding.btnEdit.setText("ویرایش اظهارات");
            binding.btnConfirm.setVisibility(View.VISIBLE);
            //binding.btnNonConfirm.setVisibility(View.VISIBLE);
            //binding.imgDelete.setVisibility(View.VISIBLE);
            //binding.imgDeleteRecord.setVisibility(View.VISIBLE);
            audioPathIsChanged = false;
            signPathIsChanged = false;
            editTextIsChanged = false;
            GlobalValue.isEdit = true;

            System.out.println("confirm=======" + confirm);
            if (confirm) {
                confirmPrcData();
            }
        }
    }

    public void saveOrEdit() {
        ProgressDialog dialog = ProgressDialog.show(getActivity(), "", "لطفا منتظر بمانید...", true);
        dialog.show();
        inputParam = GsonGenerator.saveOrEditPrcData(user.getUsername(), user.getBisPassword(), GlobalValue.prcSetId, "238", GlobalValue.proSrvId, binding.editTextTextPersonName.getText().toString(), GlobalValue.prcDataId);
        mainViewModel.saveOrEditPrcData(inputParam).subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ProServiceResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ProServiceResponse proServiceResponse) {

                        if (proServiceResponse.result != null && proServiceResponse.result.prcData.id != null) {
                            GlobalValue.prcDataId = proServiceResponse.result.prcData.id;
                            GlobalValue.isEdit = true;
                        }

                        if (proServiceResponse.ERROR != null) {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), proServiceResponse.ERROR, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        dialog.dismiss();
                        System.out.println("======onError=====");
                    }

                    @Override
                    public void onComplete() {
                        dialog.dismiss();
                        System.out.println("======onComplete=====");
                        System.out.println("======isEdit=====" + GlobalValue.isEdit);
                        if (GlobalValue.prcDataId != null) {
                            if (getActivity() != null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        new UploadFile().execute();
                                    }
                                });
                            }
                        }
                    }
                });
    }

    private void getProSrvAttachFileSign() {
        ProgressDialog dialog = ProgressDialog.show(getActivity(), "", "لطفا منتظر بمانید...", true);
        dialog.show();
        inputParam = GsonGenerator.getProSrvAttachFileList(user.getUsername(), user.getBisPassword(), GlobalValue.prcDataId, "1077");
        mainViewModel.getProSrvAttachFileList(inputParam).subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ProServiceResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ProServiceResponse proServiceResponse) {
                        jsonArrayAttachCopy = null;
                        if (proServiceResponse.result.jsonArrayAttach != null && proServiceResponse.result.jsonArrayAttach.size() > 0) {
                            dialog.dismiss();
                            jsonArrayAttachCopy = proServiceResponse.result.jsonArrayAttach;
                            try {
                                attachFileSignId = String.valueOf(proServiceResponse.result.jsonArrayAttach.get(0).attachFileId);
                                for (int i = 0; i < proServiceResponse.result.jsonArrayAttach.size(); i++) {
                                    byte[] bytes = new byte[0];
                                    bytes = new byte[proServiceResponse.result.jsonArrayAttach.get(i).attachFileJsonArray.size()];
                                    for (int j = 0; j < proServiceResponse.result.jsonArrayAttach.get(i).attachFileJsonArray.size(); j++) {
                                        bytes[j] = proServiceResponse.result.jsonArrayAttach.get(i).attachFileJsonArray.get(j).byteValue();
                                    }
                                    bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                }


                            } catch (Exception e) {
                                System.out.println(e.getLocalizedMessage());
                            }
                        } else {
                            signPath = null;
                        }

                        if (proServiceResponse.ERROR != null) {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), proServiceResponse.ERROR, Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        dialog.dismiss();
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    if (jsonArrayAttachCopy != null && jsonArrayAttachCopy.size() > 0) {
                                        if (!GlobalValue.isConfirm) {
                                            binding.imgDelete.setVisibility(View.VISIBLE);
                                        }

                                        signPath = "signPath";
                                        if (bitmap != null) {
                                            //binding.signaturePad.setSignatureBitmap(bitmap);
                                            binding.imgSignature.setImageBitmap(bitmap);
                                            signPathIsChanged = false;
                                            // binding.signaturePad.setEnabled(false);
                                            binding.carOpenDialog.setVisibility(View.GONE);
                                        }

                                    } else {
                                        signPath = null;
                                        binding.imgDelete.setVisibility(View.GONE);
                                        //binding.signaturePad.setEnabled(true);
                                        binding.carOpenDialog.setVisibility(View.VISIBLE);
                                    }

                                    getProSrvAttachFileAudio();
                                }
                            });
                        }

                    }
                });
    }

    private void getProSrvAttachFileAudio() {

        ProgressDialog dialog = ProgressDialog.show(getActivity(), "", "لطفا منتظر بمانید...", true);
        dialog.show();
        inputParam = GsonGenerator.getProSrvAttachFileList(user.getUsername(), user.getBisPassword(), GlobalValue.prcDataId, "1076");
        mainViewModel.getProSrvAttachFileList(inputParam).subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ProServiceResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ProServiceResponse proServiceResponse) {

                        jsonArrayAttachCopy = null;
                        if (proServiceResponse.result.jsonArrayAttach != null && proServiceResponse.result.jsonArrayAttach.size() > 0) {
                            dialog.dismiss();
                            jsonArrayAttachCopy = proServiceResponse.result.jsonArrayAttach;
                            try {
                                attachFileAudioId = String.valueOf(proServiceResponse.result.jsonArrayAttach.get(0).attachFileId);
                                for (int i = 0; i < proServiceResponse.result.jsonArrayAttach.size(); i++) {
                                    bytes = new byte[0];
                                    bytes = new byte[proServiceResponse.result.jsonArrayAttach.get(i).attachFileJsonArray.size()];
                                    for (int j = 0; j < proServiceResponse.result.jsonArrayAttach.get(i).attachFileJsonArray.size(); j++) {
                                        bytes[j] = proServiceResponse.result.jsonArrayAttach.get(i).attachFileJsonArray.get(j).byteValue();
                                    }

                                }
                            } catch (Exception e) {
                                System.out.println(e.getLocalizedMessage());
                            }
                        } else {
                            audioPath = null;
                        }

                        if (proServiceResponse.ERROR != null) {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), proServiceResponse.ERROR, Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        dialog.dismiss();
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @RequiresApi(api = Build.VERSION_CODES.Q)
                                public void run() {
                                    if (jsonArrayAttachCopy != null && jsonArrayAttachCopy.size() > 0) {

                                        binding.imgPlayPause.setVisibility(View.VISIBLE);
                                        if (!GlobalValue.isConfirm) {
                                            binding.imgDeleteRecord.setVisibility(View.VISIBLE);
                                        }

                                        binding.cardViewPlayer.setVisibility(View.VISIBLE);
                                        binding.constraintLayoutRecord.setVisibility(View.GONE);
                                        audioPath = "audioPath";
                                        if (bytes != null) {
                                            mp3File(bytes);
                                        }
                                    } else {
                                        binding.imgDeleteRecord.setVisibility(View.GONE);
                                        binding.cardViewPlayer.setVisibility(View.GONE);
                                        binding.constraintLayoutRecord.setVisibility(View.VISIBLE);
                                        audioPath = null;
                                        binding.imgPlayPause.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void mp3File(byte[] bytearray) {
        try {
            //mediaPlayer = new MediaPlayer();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_mm_ss");
            Date now = new Date();
            String s = "Recording_" + formatter.format(now) + ".mp3";

            String path;

            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU ||
                    Build.VERSION.SDK_INT == Build.VERSION_CODES.S_V2 ||
                    Build.VERSION.SDK_INT == Build.VERSION_CODES.S ||
                    Build.VERSION.SDK_INT == Build.VERSION_CODES.R) {
                path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_AUDIOBOOKS).toString() + Constant.DEFAULT_OUT_PUT_DIR + Constant.DEFAULT_AUDIO_OUT_PUT_DIR;
            } else {
                path = Environment.getExternalStorageDirectory().toString() + Constant.DEFAULT_OUT_PUT_DIR + Constant.DEFAULT_AUDIO_OUT_PUT_DIR;
            }

            File dir = new File(path + "/" + s);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(path + "/" + s);
            File tempFile = File.createTempFile("mobile", ".mp3", file);
            tempFile.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(tempFile);
            fos.write(bytearray);
            fos.close();
            mediaPlayer.reset();
            System.out.println("tempFile=====" + tempFile.getPath());
            FileInputStream fis = new FileInputStream(tempFile);
            mediaPlayer.setDataSource(fis.getFD());
            mediaPlayer.setLooping(true);
            mediaPlayer.prepare();
            //mediaPlayer.start();
            binding.constraintLayoutRecord.setVisibility(View.GONE);
            binding.cardViewPlayer.setVisibility(View.VISIBLE);

        } catch (IOException ex) {
            String s = ex.toString();
            ex.printStackTrace();
        }
    }

    private void deleteAttachFile(String name, String id) {
        if (id == null) {
            return;
        }
        ProgressDialog dialog = ProgressDialog.show(getActivity(), "", "لطفا منتظر بمانید...", true);
        dialog.show();
        inputParam = GsonGenerator.deleteAttachFile(user.getUsername(), user.getBisPassword(), id);
        mainViewModel.deleteAttachFile(inputParam).subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ProServiceResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        // disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull ProServiceResponse proServiceResponse) {

                        if (proServiceResponse.result.attachFileId != null) {

                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    if (name.equals("sign")) {
                                        binding.imgDelete.setVisibility(View.GONE);
                                        //binding.signaturePad.clear();
                                        //binding.signaturePad.setEnabled(true);
                                        binding.carOpenDialog.setVisibility(View.VISIBLE);
                                        binding.imgSignature.setImageBitmap(null);
                                        binding.imgSignature.setBackgroundResource(R.drawable.image_empty);
                                        getProSrvAttachFileSign();
                                        signPathIsChanged = false;
                                    } else {
                                        binding.imgDeleteRecord.setVisibility(View.GONE);
                                        binding.constraintLayoutRecord.setVisibility(View.VISIBLE);
                                        binding.cardViewPlayer.setVisibility(View.GONE);
                                        getProSrvAttachFileAudio();
                                        audioPathIsChanged = false;
                                        binding.recordTimer.setText("رکورد صدای کارشناس");
                                    }
                                    dialog.dismiss();
                                    Toast.makeText(getActivity(), "درخواست با موفقیت انجام شد", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
    }

    private void gotoTakePicFragment(boolean isEdit) {

        //bundle.putString("prcSetId", prcSetId);
        //bundle.putString("proSrvId", proSrvId);
        //bundle.putBoolean("isEdit", isEdit);
        //bundle.putBoolean("isConfirm", isConfirm);
        //bundle.putString("prcDataId", prcDataId);
        NavHostFragment.findNavController(AddExpertDataFragment.this).navigate(R.id.takePictureFragment, null, navBuilder.build());
    }

    public void confirmPrcData() {
        ProgressDialog dialog = ProgressDialog.show(getActivity(), "", "لطفا منتظر بمانید...", true);
        dialog.show();
        inputParam = GsonGenerator.confirmPrcData(user.getUsername(), user.getBisPassword(), binding.editTextTextPersonName.getText().toString(), GlobalValue.prcDataId, confirm);
        mainViewModel.confirmPrcData(inputParam).subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ProServiceResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ProServiceResponse proServiceResponse) {
                        if (proServiceResponse.success != null) {
                            if (getActivity() != null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        dialog.dismiss();
                                        Toast.makeText(getActivity(), proServiceResponse.success, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                        if (proServiceResponse.ERROR != null) {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), proServiceResponse.ERROR, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    dialog.dismiss();
                                    Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    dialog.dismiss();
                                    NavHostFragment.findNavController(AddExpertDataFragment.this).navigate(R.id.recognizePlateFragment, null, navBuilder.build());
                                }
                            });
                        }
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (GlobalValue.prcDataId != null) {
            if (!GlobalValue.isConfirm) {
                binding.btnEdit.setText("ویرایش اظهارات");
                binding.gotoTakePic.setText("ویرایش تصاویر");
                binding.btnConfirm.setVisibility(View.VISIBLE);
                // binding.btnNonConfirm.setVisibility(View.VISIBLE);
                binding.btnEdit.setVisibility(View.VISIBLE);
            }
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        getProSrvAttachFileSign();
                    }
                });
            }
        } else {
            binding.btnEdit.setText("ذخیره");
            binding.imgDelete.setVisibility(View.GONE);
            binding.imgDeleteRecord.setVisibility(View.GONE);
            binding.cardViewPlayer.setVisibility(View.GONE);
            binding.btnConfirm.setVisibility(View.GONE);
            // binding.btnNonConfirm.setVisibility(View.GONE);
            binding.btnEdit.setVisibility(View.GONE);
        }
    }

    public void signDialog() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_sign);
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.setCancelable(false);
        dialog.show();

        SignaturePad signaturePad = dialog.findViewById(R.id.signature_pad);
        ImageView reload = dialog.findViewById(R.id.img_reload);
        TextView save = dialog.findViewById(R.id.btnSave);


        signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {
                reload.setVisibility(View.VISIBLE);
                signatureBitmap = signaturePad.getSignatureBitmap();
                tryToSaveImage(signatureBitmap);
            }

            @Override
            public void onClear() {
                reload.setVisibility(View.GONE);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.imgSignature.setImageBitmap(signatureBitmap);
                dialog.dismiss();
            }
        });

        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signaturePad.clear();
                signPathIsChanged = false;
                signPath = null;
            }
        });
    }
}