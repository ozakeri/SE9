package com.example.sino.view.fragment.reception;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sino.R;
import com.example.sino.SinoApplication;
import com.example.sino.databinding.FragmentTakePictureBinding;
import com.example.sino.db.entity.AttachFile;
import com.example.sino.enumtype.SendingStatusEn;
import com.example.sino.model.ImageItem;
import com.example.sino.model.db.User;
import com.example.sino.model.enumType.EntityNameEn;
import com.example.sino.model.reception.JsonArrayAttach;
import com.example.sino.model.reception.ProServiceResponse;
import com.example.sino.utils.CameraPreview;
import com.example.sino.utils.Config;
import com.example.sino.utils.CustomProgressDialog;
import com.example.sino.utils.GlobalValue;
import com.example.sino.utils.GsonGenerator;
import com.example.sino.utils.ImageManager;
import com.example.sino.utils.common.Constant;
import com.example.sino.utils.services.ApiServiceAsync;
import com.example.sino.view.adapter.emdadadapter.OtherImageAdapter;
import com.example.sino.viewmodel.DatabaseViewModel;
import com.example.sino.viewmodel.MainViewModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
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
public class TakePictureFragment extends Fragment {

    private FragmentTakePictureBinding binding;
    private String description = "";
    private String descriptionOne = "";
    private String descriptionTwo = "";
    private String descriptionThree = "";
    private String descriptionFour = "";
    private int cameraPosition = 0;
    private int descPosition = 0;
    private static final int MY_PERMISSIONS_REQUEST = 100;
    private Uri mCapturedImageURI;
    private static final int REQUEST_CAMERA = 1;
    private String path;
    private NavOptions.Builder navBuilder;
    private AttachFile attachFile;
    private List<AttachFile> attachFilesList;
    private ApiServiceAsync apiServiceAsync;
    private User user;
    private MainViewModel mainViewModel;
    private String inputParam = "";
    private DatabaseViewModel databaseViewModel;

    private CompositeDisposable disposable;
    private String pathFront = "";
    private String pathRight = "";
    private String pathBack = "";
    private String pathLeft = "";
    private String pathKm = "";

    private boolean pathFrontIsChanged = false;
    private boolean pathRightIsChanged = false;
    private boolean pathBackIsChanged = false;
    private boolean pathLeftIsChanged = false;
    private boolean pathKmIsChanged = false;
    private Camera camera;
    private CameraPreview preview;
    private static final int REQUEST_CODE_CHANGE_SETTING = 1;
    private SharedPreferences sharedPreferences;
    private ProgressDialog pd;
    //private String prcSetId;
    //private String proSrvId;
    //private String prcDataId = null;
    private Bundle bundle;
    //private boolean isEdit = false;
    private boolean isConfirm = false;
    private String attachFileIdFront;
    private String attachFileIdRight;
    private String attachFileIdBack;
    private String attachFileIdLeft;

    private String attachFileIdOther;
    private String attachFileIdKM;
    private String imgDeleteKm;
    public List<JsonArrayAttach> jsonArrayAttachCopy = null;
    private Bitmap bitmap;
    private List<ImageItem> imageItemList;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_take_picture, container, false);
        navBuilder = new NavOptions.Builder();
        navBuilder.setEnterAnim(R.anim.slide_from_left).setExitAnim(R.anim.slide_out_right).setPopEnterAnim(R.anim.slide_from_right).setPopExitAnim(R.anim.slide_out_left);

        sharedPreferences = getActivity().getSharedPreferences("proModelId", MODE_PRIVATE);

        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putInt(Constant.STATE_Reception, 2);
        myEdit.commit();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerViewImg.setLayoutManager(linearLayoutManager);

        databaseViewModel = new ViewModelProvider(this).get(DatabaseViewModel.class);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        user = SinoApplication.getInstance().getCurrentUser();
        apiServiceAsync = new ApiServiceAsync();
        disposable = new CompositeDisposable();
        imageItemList = new ArrayList<>();

        binding.btnEdit.setText("ذخیره");

        binding.cameraFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraPosition = 1;
                startPreview(null);
            }
        });

        binding.cameraRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraPosition = 2;
                startPreview(null);
                //getCamera();
            }
        });

        binding.cameraBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraPosition = 3;
                startPreview(null);
                //getCamera();
            }
        });

        binding.cameraLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraPosition = 4;
                startPreview(null);
                //getCamera();
            }
        });

        binding.cameraKm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraPosition = 5;
                startPreview(null);
                //getCamera();
            }
        });

        binding.appCompatImageOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraPosition = 6;
                startPreview(null);
                //getCamera();
            }
        });

        binding.imgDescriptionFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                descPosition = 1;
                addDescriptionDialog();
            }
        });

        binding.imgDescriptionRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                descPosition = 2;
                addDescriptionDialog();
            }
        });

        binding.imgDescriptionLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                descPosition = 3;
                addDescriptionDialog();
            }
        });

        binding.imgDescriptionBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                descPosition = 4;
                addDescriptionDialog();
            }
        });

        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  binding.waitProgress.setVisibility(View.VISIBLE);


            }
        });

        binding.imgTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickCapture();
            }
        });

        binding.imgDeleteFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalValue.isEdit) {
                    deleteAttachFile("imgDeleteFront", attachFileIdFront);
                }

            }
        });

        binding.imgDeleteRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalValue.isEdit) {
                    deleteAttachFile("imgDeleteRight", attachFileIdRight);
                }
            }
        });

        binding.imgDeleteBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalValue.isEdit) {
                    deleteAttachFile("imgDeleteBack", attachFileIdBack);
                }

            }
        });

        binding.imgDeleteLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalValue.isEdit) {
                    deleteAttachFile("imgDeleteLeft", attachFileIdLeft);
                }

            }
        });

        binding.imgDeleteKm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalValue.isEdit) {
                    deleteAttachFile("imgDeleteKm", attachFileIdKM);
                }
            }
        });

        if (GlobalValue.isEdit) {
            binding.btnEdit.setText("ویرایش");
            getProSrvAttachFileFront(true);
        } else {
            binding.btnEdit.setText("ذخیره");
        }

        if (GlobalValue.isConfirm) {
            binding.btnEdit.setVisibility(View.GONE);
            binding.imgDeleteFront.setVisibility(View.GONE);
            binding.imgDeleteRight.setVisibility(View.GONE);
            binding.imgDeleteBack.setVisibility(View.GONE);
            binding.imgDeleteLeft.setVisibility(View.GONE);
            binding.imgDeleteKm.setVisibility(View.GONE);
        }

        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!GlobalValue.isEdit) {
                    if (pathLeft.equals("") ||
                            pathRight.equals("") ||
                            pathBack.equals("") ||
                            pathFront.equals("") ||
                            pathKm.equals("")) {
                        Toast.makeText(getActivity(), "تعداد تصاویر نادرست است", Toast.LENGTH_LONG).show();

                       // return;
                    }
                }
                if (pathFrontIsChanged || pathRightIsChanged || pathBackIsChanged || pathLeftIsChanged || pathKmIsChanged) {
                   //saveOrEdit();
                }
                saveOrEdit();
            }
        });
        return binding.getRoot();
    }

    public void getCamera() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat
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


    public void addDescriptionDialog() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_image_description);
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.show();

        TextView textView = dialog.findViewById(R.id.txt_desc);
        TextView txt_title = dialog.findViewById(R.id.txt_title);

        switch (descPosition) {
            case 1:
                txt_title.setText("توضیحات جلوی خودرو");
                break;

            case 2:
                txt_title.setText("توضیحات سمت راست خودرو");
                break;

            case 3:
                txt_title.setText("توضیحات سمت چپ خودرو");
                break;

            case 4:
                txt_title.setText("توضیحات عقب خودرو");
                break;
        }

        dialog.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                description = textView.getText().toString();
                switch (descPosition) {
                    case 1:
                        binding.txtDescriptionFront.setText(description);
                        break;

                    case 2:
                        binding.txtDescriptionRight.setText(description);
                        break;

                    case 3:
                        binding.txtDescriptionLeft.setText(description);
                        break;

                    case 4:
                        binding.txtDescriptionBack.setText(description);
                        break;
                }
                dialog.dismiss();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                path = getPathCamera();
                //saveAttachImageFile(path);
                Bitmap bitmap = resizeBitmap(path, 100, 100);

                switch (cameraPosition) {
                    case 1:
                        binding.imgFront.setImageBitmap(bitmap);
                        pathFront = path;
                        break;

                    case 2:
                        binding.imgRight.setImageBitmap(bitmap);
                        pathRight = path;
                        break;

                    case 3:
                        binding.imgBack.setImageBitmap(bitmap);
                        pathBack = path;
                        break;

                    case 4:
                        binding.imgLeft.setImageBitmap(bitmap);
                        pathLeft = path;
                        break;

                    case 5:
                        binding.imgKm.setImageBitmap(bitmap);
                        pathKm = path;
                        break;
                }
            }
        } else if (resultCode == REQUEST_CODE_CHANGE_SETTING) {
            if (resultCode == RESULT_OK) {
                // TODO jisun : 사진 저장 사이즈 변경하기
                String str = data.getStringExtra(Config.PREF_KEY_PICTURE_SIZE);

                binding.layoutPreview.removeView(preview);
                preview = null;

                startPreview(str);
            }
        }
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

    /*getPathCamera */
    private String getPathCamera() {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(mCapturedImageURI, projection, null, null, null);
        int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index_data);
    }

    private Bitmap resizeBitmap(String photoPath, int targetW, int targetH) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        }

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true; //Deprecated API 21

        return BitmapFactory.decodeFile(photoPath, bmOptions);
    }

    public void saveAttachImageFile(String filePath, Long attachFileSettingId) {

        System.out.println("=========filePath==" + filePath);

        File file = new File(filePath);
        // String userFileName = file.getName();


        file = saveBitmapToFile(file);
        attachFile = new AttachFile();
        String userFileName = file.getName();
        System.out.println("=========userFileName==" + userFileName);

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
        String path = Environment.getExternalStorageDirectory().toString() + Constant.DEFAULT_OUT_PUT_DIR + Constant.DEFAULT_IMG_OUT_PUT_DIR;
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }


        String newFilePath = path + "/" + attachFile.getId();

        try {
            InputStream inputStream = new FileInputStream(file);
            OutputStream outputStream = new FileOutputStream(newFilePath);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2; //try to decrease decoded image
            options.inPurgeable = true; //purgeable to disk
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream); //compressed bitmap to file
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

 /*   public void getAttachFileList() {
        databaseViewModel.getReportAttachFileList(GlobalValue.proServiceId).observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<List<AttachFile>>() {
            @Override
            public void onChanged(List<AttachFile> attachFiles) {
                if (!attachFiles.isEmpty()) {
                    int count = 0;
                    System.out.println("=attachFileList====" + attachFiles.size());
                    for (AttachFile attachFile : attachFiles) {
                        count++;
                        apiServiceAsync.resumeAttachFile(user, getActivity(), attachFile, databaseViewModel);
                    }
                    System.out.println("=attachFileList==for==" + attachFiles.size());
                    System.out.println("=count==" + count);
                    if (count == attachFiles.size()) {
                        binding.waitProgress.setVisibility(View.GONE);
                        NavHostFragment.findNavController(TakePictureFragment.this).navigate(R.id.addCustomerDataFragment, null, navBuilder.build());
                    }
                }
            }
        });

    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }

    private void startPreview(String pictureSizeStr) {
        binding.frameLayout.setVisibility(View.VISIBLE);
        binding.scrollView.setVisibility(View.GONE);
        if (camera != null) {
            camera.release();
            camera = null;
        }

        camera = getCameraInstance();

        if (camera == null) {
            Log.e("jisunLog", "Failed camera open");
        } else {

            if (preview != null) {
                binding.layoutPreview.removeView(preview);
                preview = null;
            }

            preview = new CameraPreview(getActivity(), camera);
            preview.setKeepScreenOn(true);

            // 저장 사진과 preview의 사이즈 등을 설정
            ImageManager.adjustCameraParameters(getActivity(), camera, pictureSizeStr);

            // preview가 보여지는 화면의 비율 설정
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            FrameLayout.LayoutParams p = (FrameLayout.LayoutParams) binding.layoutPreview.getLayoutParams();
            p.height = displayMetrics.widthPixels / 3 * 4;
            binding.layoutPreview.setLayoutParams(p);

            // preview를 layout에 추가하고, 날짜 영역을 화면 상위로 올림
            binding.layoutPreview.addView(preview);
            binding.txtDate.setText(new Date() + "--" + GlobalValue.lat + " " + "--" + GlobalValue.lang);
            binding.txtDate.bringToFront();
        }
    }

    public static int setCameraDisplayOrientation(Activity activity,
                                                  int cameraId, Camera camera) {
        Camera.CameraInfo info =
                new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }

        return result;
    }

    public Camera getCameraInstance() {
        Camera camera = null;

        int numCams = Camera.getNumberOfCameras();
        if (numCams > 0) {
            try {
                // Camera.CameraInfo.CAMERA_FACING_FRONT or Camera.CameraInfo.CAMERA_FACING_BACK
                int cameraFacing = Camera.CameraInfo.CAMERA_FACING_BACK;
                camera = Camera.open(cameraFacing);
                // camera orientation
                camera.setDisplayOrientation(setCameraDisplayOrientation(getActivity(), cameraFacing, camera));
                // get Camera parameters
                Camera.Parameters params = camera.getParameters();
                // picture image orientation
                params.setRotation(setCameraDisplayOrientation(getActivity(), cameraFacing, camera));
                camera.startPreview();

            } catch (RuntimeException ex) {
                // Toast.makeText(this, "camera_not_found ] " + ex.getMessage().toString(), Toast.LENGTH_LONG).show();
                // Log.d(Config.TAG, "camera_not_found ] " + ex.getMessage().toString());
            }
        }

        return camera;
    }

    public void onClickCapture() {
        camera.takePicture(null, null, new Camera.PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                new AsyncTask<byte[], Void, File>() {
                    public float textSize;
                    private CustomProgressDialog progressDialog;

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        progressDialog = new CustomProgressDialog(getActivity());
                        progressDialog.setCancelable(false);
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();

                        textSize = binding.txtDate.getTextSize();
                    }

                    @Override
                    protected File doInBackground(byte[]... params) {
                        byte[] data = params[0];
                        Bitmap bitmap = ImageManager.saveImageWithTimeStamp(getActivity(), data, 0, data.length, textSize, binding.txtDate.getText().toString());
                        File file = ImageManager.saveFile(bitmap);

                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                switch (cameraPosition) {
                                    case 1:
                                        binding.imgFront.setImageBitmap(bitmap);
                                        pathFront = file.getPath();
                                        pathFrontIsChanged = true;
                                        System.out.println("pathFront111===" + pathFront);
                                        break;

                                    case 2:
                                        binding.imgRight.setImageBitmap(bitmap);
                                        pathRight = file.getPath();
                                        pathRightIsChanged = true;
                                        break;

                                    case 3:
                                        binding.imgBack.setImageBitmap(bitmap);
                                        pathBack = file.getPath();
                                        pathBackIsChanged = true;
                                        break;

                                    case 4:
                                        binding.imgLeft.setImageBitmap(bitmap);
                                        pathLeft = file.getPath();
                                        pathLeftIsChanged = true;
                                        break;

                                    case 5:
                                        binding.imgKm.setImageBitmap(bitmap);
                                        pathKm = file.getPath();
                                        pathKmIsChanged = true;
                                        break;

                                    case 6:
                                        //binding.imgKm.setImageBitmap(bitmap);
                                        if (imageItemList.size() <= 5) {
                                            imageItemList.add(new ImageItem(file.getPath()));

                                            binding.recyclerViewImg.setAdapter(new OtherImageAdapter(imageItemList));
                                        }

                                        break;
                                }
                            }
                        });


                        //saveAttachImageFile(ImageManager.getMediaFilePath(), (long) 106);
                        // refreshGallery(file);
                        return file;
                    }

                    @Override
                    protected void onPostExecute(File file) {
                        progressDialog.dismiss();

                        if (file == null) {
                            Log.e(Config.TAG, "Error creating media file, check storage permissions");
                        } else {
                           /* ImageDialog dialog = new ImageDialog(EditAdvertActivity.this, file);
                            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    EditAdvertActivity.this.camera.startPreview();
                                }
                            });
                            dialog.show();*/
                            //saveAttachImageFile(file.getPath());
                            binding.frameLayout.setVisibility(View.GONE);
                            binding.scrollView.setVisibility(View.VISIBLE);
                            // progressDialogSentData.dismiss();
                        }

                        super.onPostExecute(file);
                    }
                }.execute(data);
            }
        });
    }

    private class UploadFile extends AsyncTask<String, String, String> {
        ProgressDialog dialog = ProgressDialog.show(getActivity(), "", "لطفا منتظر بمانید...", true);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //dialog.show();

            System.out.println("=====onPreExecute====");
        }

        @Override
        protected String doInBackground(String... urls) {
            //Copy you logic to calculate progress and call

            if (!pathFront.equals("") && pathFrontIsChanged) {
                saveAttachImageFile(pathFront, (long) 1078);
                apiServiceAsync.resumeAttachFile(user, getActivity(), attachFile, databaseViewModel);
                dialog.show();
                if (GlobalValue.isEdit){
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                getProSrvAttachFileFront(false);
                            }
                        });
                    }
                }

            }

            if (!pathRight.equals("") && pathRightIsChanged) {
                saveAttachImageFile(pathRight, (long) 1079);
                apiServiceAsync.resumeAttachFile(user, getActivity(), attachFile, databaseViewModel);

                if (GlobalValue.isEdit){
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                getProSrvAttachFileRight(false);
                            }
                        });
                    }
                }

            }

            if (!pathBack.equals("") && pathBackIsChanged) {
                saveAttachImageFile(pathBack, (long) 1080);
                apiServiceAsync.resumeAttachFile(user, getActivity(), attachFile, databaseViewModel);

                if (GlobalValue.isEdit){
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                getProSrvAttachFileBack(false);
                            }
                        });
                    }
                }

            }

            if (!pathLeft.equals("") && pathLeftIsChanged) {
                saveAttachImageFile(pathLeft, (long) 1081);
                apiServiceAsync.resumeAttachFile(user, getActivity(), attachFile, databaseViewModel);

                if (GlobalValue.isEdit){
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                getProSrvAttachFileLeft(false);
                            }
                        });
                    }
                }

            }

            if (!pathKm.equals("") && pathKmIsChanged) {
                saveAttachImageFile(pathKm, (long) 1097);
                apiServiceAsync.resumeAttachFile(user, getActivity(), attachFile, databaseViewModel);

                if (GlobalValue.isEdit){
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                getProSrvAttachFileKM();
                            }
                        });
                    }
                }

            }

            System.out.println("imageItemList=====" + imageItemList.size());
            if (imageItemList != null && imageItemList.size() > 0) {
                for (int i = 0; i < imageItemList.size() - 1; i++) {
                    ImageItem item = imageItemList.get(i);
                    saveAttachImageFile(item.getPath(), (long) 1108);
                    apiServiceAsync.resumeAttachFile(user, getActivity(), attachFile, databaseViewModel);
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
            System.out.println("=====onPostExecute====");
            dialog.dismiss();
            pathFrontIsChanged = false;
            pathRightIsChanged = false;
            pathBackIsChanged = false;
            pathLeftIsChanged = false;
            pathKmIsChanged = false;

            Toast.makeText(getActivity(), "در خواست با موفقیت انجام شد", Toast.LENGTH_LONG).show();
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
            Bundle bundle = new Bundle();
           // bundle.putString("prcDataId",prcDataId);
           // bundle.putString("prcSetId",prcSetId);
            //bundle.putString("proSrvId",proSrvId);
            NavHostFragment.findNavController(TakePictureFragment.this).navigate(R.id.addExpertDataFragment, bundle, navBuilder.build());
            //NavHostFragment.findNavController(TakePictureFragment.this).navigateUp();

        }
    }

    public void saveOrEdit() {
        inputParam = GsonGenerator.saveOrEditPrcData(user.getUsername(), user.getBisPassword(), GlobalValue.prcSetId, "238", GlobalValue.proSrvId, "", GlobalValue.prcDataId);
        mainViewModel.saveOrEditPrcData(inputParam).subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ProServiceResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ProServiceResponse proServiceResponse) {
                        System.out.println("=====onNext=====");
                        if (proServiceResponse.result != null && proServiceResponse.result.prcData.id != null) {
                            GlobalValue.prcDataId = proServiceResponse.result.prcData.id;
                            GlobalValue.proSrvId = proServiceResponse.result.prcData.entityId;
                            GlobalValue.isEdit = true;

                            if (getActivity() != null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(getActivity(), "در خواست با موفقیت انجام شد", Toast.LENGTH_LONG).show();
                                        new UploadFile().execute();
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        System.out.println("=====onError=====" + e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("=====onComplete=====");

                    }
                });
    }

    private void getProSrvAttachFileFront(boolean isNext) {

        progressDialog = ProgressDialog.show(getActivity(), "", "لطفا منتظر بمانید...", true);
        progressDialog.show();

        if (GlobalValue.prcDataId == null || GlobalValue.prcDataId.equals("null")) {
            return;
        }

        inputParam = GsonGenerator.getProSrvAttachFileList(user.getUsername(), user.getBisPassword(), GlobalValue.prcDataId, "1078");
        mainViewModel.getProSrvAttachFileList(inputParam).subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ProServiceResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ProServiceResponse proServiceResponse) {
                        jsonArrayAttachCopy = null;
                        if (proServiceResponse.result.jsonArrayAttach != null) {
                            jsonArrayAttachCopy = proServiceResponse.result.jsonArrayAttach;
                            try {
                                attachFileIdFront = String.valueOf(proServiceResponse.result.jsonArrayAttach.get(0).attachFileId);
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
                                    if (jsonArrayAttachCopy.size() != 0) {
                                        if (!GlobalValue.isConfirm){
                                            binding.imgDeleteFront.setVisibility(View.VISIBLE);
                                        }

                                        pathFrontIsChanged = false;
                                        binding.imgFront.setImageBitmap(bitmap);
                                    } else {
                                        binding.imgDeleteFront.setVisibility(View.GONE);
                                        binding.imgFront.setImageBitmap(null);
                                        binding.imgFront.setBackgroundResource(R.drawable.image_empty);
                                    }

                                    binding.btnEdit.setText("ویرایش");
                                    if (isNext) {
                                        getProSrvAttachFileRight(true);
                                    }
                                }
                            });
                        }

                    }
                });
    }

    private void getProSrvAttachFileRight(boolean isNext) {
        inputParam = GsonGenerator.getProSrvAttachFileList(user.getUsername(), user.getBisPassword(), GlobalValue.prcDataId, "1079");
        mainViewModel.getProSrvAttachFileList(inputParam).subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ProServiceResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ProServiceResponse proServiceResponse) {
                        jsonArrayAttachCopy = null;
                        if (proServiceResponse.result.jsonArrayAttach != null) {
                            jsonArrayAttachCopy = proServiceResponse.result.jsonArrayAttach;
                            try {
                                attachFileIdRight = String.valueOf(proServiceResponse.result.jsonArrayAttach.get(0).attachFileId);
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
                                    if (jsonArrayAttachCopy.size() != 0) {
                                        if (!GlobalValue.isConfirm){
                                            binding.imgDeleteRight.setVisibility(View.VISIBLE);
                                        }

                                        pathRightIsChanged = false;
                                        binding.imgRight.setImageBitmap(bitmap);
                                    } else {
                                        binding.imgDeleteRight.setVisibility(View.GONE);
                                        binding.imgRight.setImageBitmap(null);
                                        binding.imgRight.setBackgroundResource(R.drawable.image_empty);
                                    }

                                    binding.btnEdit.setText("ویرایش");
                                    if (isNext) {
                                        getProSrvAttachFileBack(true);
                                    }

                                }
                            });
                        }


                    }
                });
    }

    private void getProSrvAttachFileBack(boolean isNext) {
        inputParam = GsonGenerator.getProSrvAttachFileList(user.getUsername(), user.getBisPassword(), GlobalValue.prcDataId, "1080");
        mainViewModel.getProSrvAttachFileList(inputParam).subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ProServiceResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ProServiceResponse proServiceResponse) {
                        jsonArrayAttachCopy = null;
                        if (proServiceResponse.result.jsonArrayAttach != null) {
                            jsonArrayAttachCopy = proServiceResponse.result.jsonArrayAttach;
                            try {
                                attachFileIdBack = String.valueOf(proServiceResponse.result.jsonArrayAttach.get(0).attachFileId);
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
                                    if (jsonArrayAttachCopy.size() != 0) {
                                        if (!GlobalValue.isConfirm){
                                            binding.imgDeleteBack.setVisibility(View.VISIBLE);
                                        }

                                        pathBackIsChanged = false;
                                        binding.imgBack.setImageBitmap(bitmap);
                                    } else {
                                        binding.imgDeleteBack.setVisibility(View.GONE);
                                        binding.imgBack.setImageBitmap(null);
                                        binding.imgBack.setBackgroundResource(R.drawable.image_empty);
                                    }

                                    binding.btnEdit.setText("ویرایش");
                                    if (isNext) {
                                        getProSrvAttachFileLeft(true);
                                    }
                                }
                            });
                        }


                    }
                });
    }

    private void getProSrvAttachFileLeft(boolean isNext) {
        inputParam = GsonGenerator.getProSrvAttachFileList(user.getUsername(), user.getBisPassword(), GlobalValue.prcDataId, "1081");
        mainViewModel.getProSrvAttachFileList(inputParam).subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ProServiceResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ProServiceResponse proServiceResponse) {
                        jsonArrayAttachCopy = null;
                        if (proServiceResponse.result.jsonArrayAttach != null) {
                            jsonArrayAttachCopy = proServiceResponse.result.jsonArrayAttach;
                            try {
                                attachFileIdLeft = String.valueOf(proServiceResponse.result.jsonArrayAttach.get(0).attachFileId);
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
                                    if (jsonArrayAttachCopy.size() != 0) {
                                        if (!GlobalValue.isConfirm){
                                            binding.imgDeleteLeft.setVisibility(View.VISIBLE);
                                        }

                                        pathLeftIsChanged = false;
                                        binding.imgLeft.setImageBitmap(bitmap);
                                    } else {
                                        binding.imgLeft.setImageBitmap(null);
                                        binding.imgDeleteLeft.setVisibility(View.GONE);
                                        binding.imgLeft.setBackgroundResource(R.drawable.image_empty);
                                    }

                                    binding.btnEdit.setText("ویرایش");
                                    Toast.makeText(getActivity(), "در خواست با موفقیت انجام شد", Toast.LENGTH_LONG).show();
                                    if (isNext) {
                                        getProSrvAttachFileKM();
                                    }
                                }
                            });
                        }


                    }
                });
    }

    private void getProSrvAttachFileOther() {
        inputParam = GsonGenerator.getProSrvAttachFileList(user.getUsername(), user.getBisPassword(), GlobalValue.prcDataId, "1108");
        mainViewModel.getProSrvAttachFileList(inputParam).subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ProServiceResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ProServiceResponse proServiceResponse) {
                        jsonArrayAttachCopy = null;
                        if (proServiceResponse.result.jsonArrayAttach != null) {
                            jsonArrayAttachCopy = proServiceResponse.result.jsonArrayAttach;

                            System.out.println("=====size======" + proServiceResponse.result.jsonArrayAttach.size());
                            for (JsonArrayAttach attachFile : proServiceResponse.result.jsonArrayAttach) {
                                // try {
                                attachFileIdOther = String.valueOf(attachFile.attachFileId);

                                if (getActivity() != null) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        public void run() {
                                            for (int i = 0; i < proServiceResponse.result.jsonArrayAttach.size() - 1; i++) {
                                                byte[] bytes = new byte[0];
                                                bytes = new byte[proServiceResponse.result.jsonArrayAttach.get(i).attachFileJsonArray.size()];
                                                imageItemList.add(new ImageItem(proServiceResponse.result.jsonArrayAttach.get(i).attachFileJsonArray));
                                                for (int j = 0; j < proServiceResponse.result.jsonArrayAttach.get(i).attachFileJsonArray.size(); j++) {
                                                    bytes[j] = proServiceResponse.result.jsonArrayAttach.get(i).attachFileJsonArray.get(j).byteValue();
                                                }

                                            }
                                            // bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                            binding.recyclerViewImg.setAdapter(new OtherImageAdapter(imageItemList));

                                        }
                                    });
                                }


                                /*} catch (Exception e) {
                                    System.out.println(e.getLocalizedMessage());
                                } */
                            }

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

                                    //getProSrvAttachFileKM();
                                }
                            });
                        }


                    }
                });

        progressDialog.dismiss();
    }


    private void getProSrvAttachFileKM() {
        inputParam = GsonGenerator.getProSrvAttachFileList(user.getUsername(), user.getBisPassword(), GlobalValue.prcDataId, "1097");
        mainViewModel.getProSrvAttachFileList(inputParam).subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ProServiceResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ProServiceResponse proServiceResponse) {
                        jsonArrayAttachCopy = null;
                        if (proServiceResponse.result.jsonArrayAttach != null) {
                            jsonArrayAttachCopy = proServiceResponse.result.jsonArrayAttach;
                            try {
                                attachFileIdKM = String.valueOf(proServiceResponse.result.jsonArrayAttach.get(0).attachFileId);
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
                                    if (jsonArrayAttachCopy.size() != 0) {
                                        pathKmIsChanged = false;
                                        if (!GlobalValue.isConfirm){
                                            binding.imgDeleteKm.setVisibility(View.VISIBLE);
                                        }

                                        binding.imgKm.setImageBitmap(bitmap);
                                    } else {
                                        binding.imgDeleteKm.setVisibility(View.GONE);
                                        binding.imgKm.setImageBitmap(null);
                                        binding.imgKm.setBackgroundResource(R.drawable.image_empty);
                                    }

                                    binding.btnEdit.setText("ویرایش");
                                    Toast.makeText(getActivity(), "در خواست با موفقیت انجام شد", Toast.LENGTH_LONG).show();
                                    getProSrvAttachFileOther();
                                }
                            });
                        }


                    }
                });
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

                                    if (name.equals("imgDeleteFront")) {
                                        binding.imgDeleteFront.setVisibility(View.GONE);
                                        pathFrontIsChanged = false;
                                        //getProSrvAttachFileFront(false);
                                        binding.imgFront.setImageBitmap(null);
                                        binding.imgFront.setBackgroundResource(R.drawable.image_empty);

                                    } else if (name.equals("imgDeleteRight")) {
                                        binding.imgDeleteRight.setVisibility(View.GONE);
                                        pathRightIsChanged = false;
                                        //getProSrvAttachFileRight(false);
                                        binding.imgRight.setImageBitmap(null);
                                        binding.imgRight.setBackgroundResource(R.drawable.image_empty);

                                    } else if (name.equals("imgDeleteBack")) {
                                        binding.imgDeleteBack.setVisibility(View.GONE);
                                        pathBackIsChanged = false;
                                        //getProSrvAttachFileBack(false);
                                        binding.imgBack.setImageBitmap(null);
                                        binding.imgBack.setBackgroundResource(R.drawable.image_empty);

                                    } else if (name.equals("imgDeleteLeft")) {
                                        binding.imgDeleteLeft.setVisibility(View.GONE);
                                        pathLeftIsChanged = false;
                                        //getProSrvAttachFileLeft(false);
                                        binding.imgLeft.setImageBitmap(null);
                                        binding.imgLeft.setBackgroundResource(R.drawable.image_empty);

                                    } else if (name.equals("imgDeleteKm")) {
                                        binding.imgDeleteKm.setVisibility(View.GONE);
                                        pathKmIsChanged = false;
                                        //getProSrvAttachFileKM();
                                        binding.imgKm.setImageBitmap(null);
                                        binding.imgKm.setBackgroundResource(R.drawable.image_empty);
                                    }

                                    dialog.dismiss();
                                    Toast.makeText(getActivity(), "در خواست با موفقیت انجام شد", Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                    }
                });
    }

}