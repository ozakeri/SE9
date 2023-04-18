package com.example.sino.view.fragment.Emdadgar;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.example.sino.R;
import com.example.sino.databinding.FragmentGivingServiceBinding;
import com.example.sino.db.entity.AttachFile;
import com.example.sino.utils.common.Constant;
import com.example.sino.utils.common.Util;
import com.example.sino.viewmodel.DatabaseViewModel;

import java.io.File;
import java.util.Date;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class GivingServiceFragment extends Fragment {

    private FragmentGivingServiceBinding binding;
    private DatabaseViewModel databaseViewModel;
    private static final int REQUEST_CAMERA = 1;
    private static final int MY_PERMISSIONS_REQUEST = 100;
    private String path;
    private Uri mCapturedImageURI;
    private List<AttachFile> attachFileList;
    private boolean endTowing = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_giving_service, container, false);

        databaseViewModel = new ViewModelProvider(this).get(DatabaseViewModel.class);

        NavOptions.Builder navBuilder = new NavOptions.Builder();
        navBuilder.setEnterAnim(R.anim.slide_from_left).setExitAnim(R.anim.slide_out_right).setPopEnterAnim(R.anim.slide_from_right).setPopExitAnim(R.anim.slide_out_left);


        databaseViewModel.deleteAllEmdadAttachFile();

       // Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.omid);
       // binding.imageView.setImageBitmap(Util.getCroppedBitmap(bitmap));


        if (getArguments() != null) {
            binding.txtAddress.setText(getArguments().getString("address"));
        }

        binding.profileConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(GivingServiceFragment.this).navigate(R.id.customerProfileFragment, null, navBuilder.build());
            }
        });

        binding.btnTowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (endTowing) {
                    NavHostFragment.findNavController(GivingServiceFragment.this).navigate(R.id.endRequestFragment, null, navBuilder.build());
                }

                binding.btnTowing.setText("پایان یدک کشی");
                binding.btnRepairs.setText("توقف");
                endTowing = true;
            }
        });

        binding.btnAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        });

        binding.txtSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.actionSms(getActivity(), "+989216074738");
            }
        });

        binding.txtCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.actionCall(getActivity(), "+989216074738");
            }
        });

        return binding.getRoot();
    }


    //========= cameraIntent for attachment
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
            Uri outputFileUri;
            if (requestCode == REQUEST_CAMERA) {
                path = getPathCamera();
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

    public void saveAttachImageFile(String filePath) {
        File file = new File(String.valueOf(filePath));

        AttachFile attachFile = new AttachFile();
        String userFileName = file.getName();
        long length = file.length();
        length = length / 1024;
        System.out.println("File Path : " + file.getPath() + ", File size : " + length + " KB");
        String filePostfix = userFileName.substring(userFileName.indexOf("."), userFileName.length());
        String path = Environment.getExternalStorageDirectory().toString() + Constant.DEFAULT_OUT_PUT_DIR + Constant.DEFAULT_IMG_OUT_PUT_DIR;
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        attachFile.setAttachFileLocalPath(filePath);
        attachFile.setAttachFileUserFileName(userFileName);
        attachFile.setSendingStatusDate(new Date());

        databaseViewModel.insertAttachFileRepoVM(attachFile);

        refreshAttachAdapter();

    }

    public void refreshAttachAdapter() {
     /*   databaseViewModel.getAllAttachFile().observe(getViewLifecycleOwner(), new Observer<List<AttachFile>>() {
            @Override
            public void onChanged(List<AttachFile> attachFiles) {
                System.out.println("size====" + attachFiles.size());
                GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                binding.recyclerViewAttachList.setLayoutManager(linearLayoutManager);
                binding.recyclerViewAttachList.setAdapter(new EmdadAttachFileAdapter(attachFiles,databaseViewModel));
            }
        });*/

    }
}