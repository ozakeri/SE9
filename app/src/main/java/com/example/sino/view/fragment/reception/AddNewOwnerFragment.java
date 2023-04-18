package com.example.sino.view.fragment.reception;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.example.sino.R;
import com.example.sino.SinoApplication;
import com.example.sino.databinding.FragmentAddNewOwnerBinding;
import com.example.sino.db.entity.AttachFile;
import com.example.sino.enumtype.SendingStatusEn;
import com.example.sino.model.db.User;
import com.example.sino.model.enumType.EntityNameEn;
import com.example.sino.utils.GlobalValue;
import com.example.sino.utils.common.Constant;
import com.example.sino.utils.services.ApiServiceAsync;
import com.example.sino.viewmodel.DatabaseViewModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddNewOwnerFragment extends Fragment {

    private FragmentAddNewOwnerBinding binding;
    private NavOptions.Builder navBuilder;
    private int cameraPosition = 0;
    private static final int MY_PERMISSIONS_REQUEST = 100;
    private static final int REQUEST_CAMERA = 1;
    private Uri mCapturedImageURI;
    private String path;
    private boolean isOwner = false;
    private String frontPath = "";
    private String backPath = "";
    private User user;
    private DatabaseViewModel databaseViewModel;
    private AttachFile attachFile;
    private ApiServiceAsync apiServiceAsync;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_new_owner, container, false);

        navBuilder = new NavOptions.Builder();
        navBuilder.setEnterAnim(R.anim.slide_from_left).setExitAnim(R.anim.slide_out_right).setPopEnterAnim(R.anim.slide_from_right).setPopExitAnim(R.anim.slide_out_left);
        apiServiceAsync = new ApiServiceAsync();
        databaseViewModel = new ViewModelProvider(this).get(DatabaseViewModel.class);
        user = SinoApplication.getInstance().getCurrentUser();
        if (getArguments() != null) {
            isOwner = getArguments().getBoolean("isOwner");

        }

        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (frontPath != null) {
                    saveAttachImageFile(frontPath,(long) 114);
                    apiServiceAsync.resumeAttachFile(user, getActivity(), attachFile, databaseViewModel);
                }

                if (backPath != null) {
                    saveAttachImageFile(backPath,(long) 115);
                    apiServiceAsync.resumeAttachFile(user, getActivity(), attachFile, databaseViewModel);
                }

                NavHostFragment.findNavController(AddNewOwnerFragment.this).navigate(R.id.recognizePlateFragment, null, navBuilder.build());

            }
        });

        binding.imgOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraPosition = 1;
                getCamera();
            }
        });

        binding.imgTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraPosition = 2;
                getCamera();
            }
        });

        return binding.getRoot();
    }


    public void addCustomerDialog() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_customer);
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.show();

        dialog.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activationCodeDialog();
                dialog.dismiss();
            }
        });


    }

    public void activationCodeDialog() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_activation_code);
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.show();

        dialog.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(AddNewOwnerFragment.this).navigate(R.id.takePictureFragment, null, navBuilder.build());
                dialog.dismiss();
            }
        });

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                path = getPathCamera();
                //saveAttachImageFile(path);
                Bitmap bitmap = resizeBitmap(path, 100, 100);
                switch (cameraPosition) {
                    case 1:
                        binding.imgOne.setImageBitmap(bitmap);
                        backPath = path;
                        break;

                    case 2:
                        binding.imgTwo.setImageBitmap(bitmap);
                        frontPath = path;
                        break;
                }
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

    public void saveAttachImageFile(String filePath,long attachFileSettingId) {
        File file = new File(String.valueOf(filePath));
        attachFile = new AttachFile();
        String userFileName = file.getName();

        attachFile.setAttachFileUserFileName(userFileName);
        attachFile.setSendingStatusDate(new Date());
        attachFile.setSendingStatusEn(SendingStatusEn.Pending.ordinal());
        attachFile.setEntityNameEn(EntityNameEn.SeProService.ordinal());
        attachFile.setServerAttachFileSettingId(attachFileSettingId);
        attachFile.setEntityId(GlobalValue.proServiceId);
        attachFile.setServerEntityId(GlobalValue.proServiceId);
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
}