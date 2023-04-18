package com.example.sino.view.fragment.reception;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sino.R;
import com.example.sino.databinding.FragmentAddNewCarBinding;
import com.example.sino.db.entity.AttachFile;
import com.example.sino.enumtype.SendingStatusEn;
import com.example.sino.model.db.User;
import com.example.sino.model.enumType.EntityNameEn;
import com.example.sino.utils.GlobalValue;
import com.example.sino.utils.common.Constant;
import com.example.sino.utils.common.Util;
import com.example.sino.utils.services.ApiServiceAsync;
import com.example.sino.viewmodel.DatabaseViewModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

public class ChangePlateFragment extends Fragment {

    private FragmentAddNewCarBinding binding;
    private static final int MY_PERMISSIONS_REQUEST = 100;
    private Uri mCapturedImageURI;
    private static final int REQUEST_CAMERA = 1;
    private String path;
    private int cameraPosition = 0;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_new_car, container, false);
        NavOptions.Builder navBuilder = new NavOptions.Builder();
        navBuilder.setEnterAnim(R.anim.slide_from_left).setExitAnim(R.anim.slide_out_right).setPopEnterAnim(R.anim.slide_from_right).setPopExitAnim(R.anim.slide_out_left);

        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //NavHostFragment.findNavController(ChangePlateFragment.this).navigate(R.id.carDetailFragment, null, navBuilder.build());


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
                        break;

                    case 2:
                        binding.imgTwo.setImageBitmap(bitmap);
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
}