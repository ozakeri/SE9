package com.example.sino.view.fragment.login;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.example.sino.R;
import com.example.sino.SinoApplication;
import com.example.sino.databinding.CopyFragmentActivationBinding;
import com.example.sino.model.SuccessActivationBean;
import com.example.sino.model.db.User;
import com.example.sino.utils.GsonGenerator;
import com.example.sino.utils.common.Constant;
import com.example.sino.view.fragment.HomeFragment;
import com.example.sino.viewmodel.DatabaseViewModel;
import com.example.sino.viewmodel.MainViewModel;
import com.example.sino.viewmodel.RegisterViewModel;
import com.rejowan.cutetoast_custom.CuteToast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class FragmentActivation extends Fragment {

    private RegisterViewModel viewModel;
    private MainViewModel mainViewModel;
    private String code = "";
    private User user;
    private CopyFragmentActivationBinding binding;
    private DatabaseViewModel databaseViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.copy_fragment_activation, container, false);
        databaseViewModel = new ViewModelProvider(this).get(DatabaseViewModel.class);
        binding.lottieMain.setVisibility(View.GONE);
        binding.activationCode.setText("");
        NavOptions.Builder navBuilder = new NavOptions.Builder();
        navBuilder.setEnterAnim(R.anim.slide_from_left).setExitAnim(R.anim.slide_out_right).setPopEnterAnim(R.anim.slide_from_right).setPopExitAnim(R.anim.slide_out_left);

        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        user = SinoApplication.getInstance().getCurrentUser();
        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.activationCode.getText() != null) {
                    System.out.println("getMobileNo=====" + user.getMobileNo());
                    if (user.getMobileNo() != null) {
                        code = GsonGenerator.sendActivationCodeToGson(user.getMobileNo(), binding.activationCode.getText().toString());
                        if (binding.activationCode.getText() != null && !binding.activationCode.getText().toString().trim().isEmpty()) {
                            binding.lottieMain.setVisibility(View.VISIBLE);
                            viewModel.sendActivationCode(code).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new io.reactivex.rxjava3.core.Observer<SuccessActivationBean>() {
                                        @Override
                                        public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                                        }

                                        @Override
                                        public void onNext(@io.reactivex.rxjava3.annotations.NonNull SuccessActivationBean successActivationBean) {
                                            if (successActivationBean.getERROR() == null && successActivationBean.getSUCCESS() != null) {
                                                user.setName(successActivationBean.getRESULT().getName());
                                                user.setServerUserId(successActivationBean.getRESULT().getServerUserId());
                                                user.setFamily(successActivationBean.getRESULT().getFamily());
                                                user.setUsername(successActivationBean.getRESULT().getUsername());
                                                user.setBisPassword(successActivationBean.getRESULT().getBisPassword());
                                                user.setCompanyName(successActivationBean.getRESULT().getCompanyName());
                                                user.setCompanyType(successActivationBean.getRESULT().getCompanyType());
                                                user.setPictureBytes(successActivationBean.getRESULT().getPictureBytes());
                                                user.setLoginIs(true);
                                                mainViewModel.insertUser(user);
                                                SinoApplication.getInstance().setCurrentUser(user);
                                                //createImageUserPath(successActivationBean);
                                                NavHostFragment.findNavController(FragmentActivation.this).navigateUp();
                                                NavHostFragment.findNavController(FragmentActivation.this).navigate(R.id.createPasswordFragment, null, null);

                                            } else {
                                                CuteToast.ct(getActivity(), successActivationBean.getERROR(), CuteToast.LENGTH_SHORT, CuteToast.ERROR, R.drawable.sinoempty).show();
                                            }
                                        }

                                        @Override
                                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                            binding.lottieMain.setVisibility(View.GONE);
                                            CuteToast.ct(getActivity(), e.getLocalizedMessage(), CuteToast.LENGTH_SHORT, CuteToast.ERROR, R.drawable.sinoempty).show();
                                        }

                                        @Override
                                        public void onComplete() {
                                            binding.lottieMain.setVisibility(View.GONE);
                                        }
                                    });
                        } else {
                            CuteToast.ct(getActivity(), getString(R.string.enter_compelete_data), CuteToast.LENGTH_SHORT, CuteToast.ERROR, R.drawable.sinoempty).show();
                        }
                    }
                }
            }
        });

        binding.txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseViewModel.deleteAllData();
                NavHostFragment.findNavController(FragmentActivation.this).navigate(R.id.splashFragment, null, navBuilder.build());
            }
        });


        return binding.getRoot().getRootView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void createImageUserPath(SuccessActivationBean successActivationBean) {
        if (successActivationBean.getRESULT().getPictureBytes() != null) {
            byte[] bytes = new byte[successActivationBean.getRESULT().getPictureBytes().size()];
            for (int i = 0; i < successActivationBean.getRESULT().getPictureBytes().size(); i++) {
                bytes[i] = successActivationBean.getRESULT().getPictureBytes().get(i).byteValue();
            }
            String path = Environment.getExternalStorageDirectory().toString() + Constant.DEFAULT_OUT_PUT_DIR + Constant.DEFAULT_USER_IMG_OUT_PUT_DIR;
            File dir = new File(path);
            int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                String picturePathUrl = path + "/user-pic.jpg";
                OutputStream outputStream = null;
                File file = new File(picturePathUrl); // the File to save to
                try {
                    outputStream = new FileOutputStream(file);
                    outputStream.write(bytes);
                    user.setPicturePathUrl(picturePathUrl);

                    mainViewModel.insertUser(user);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}