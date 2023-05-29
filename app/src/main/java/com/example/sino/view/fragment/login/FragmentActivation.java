package com.example.sino.view.fragment.login;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.example.sino.R;
import com.example.sino.SinoApplication;
import com.example.sino.databinding.CopyFragmentActivationBinding;
import com.example.sino.model.SuccessActivationBean;
import com.example.sino.model.SuccessRegisterBean;
import com.example.sino.model.db.User;
import com.example.sino.utils.Config;
import com.example.sino.utils.GlobalValue;
import com.example.sino.utils.GsonGenerator;
import com.example.sino.utils.common.Constant;
import com.example.sino.utils.common.Util;
import com.example.sino.view.fragment.HomeFragment;
import com.example.sino.view.fragment.SplashFragment;
import com.example.sino.viewmodel.DatabaseViewModel;
import com.example.sino.viewmodel.MainViewModel;
import com.example.sino.viewmodel.RegisterViewModel;
import com.rejowan.cutetoast_custom.CuteToast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
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

    private CompositeDisposable disposable;
    //private SuccessActivationBean successActivationBeanTemp;

    private CountDownTimer countDownTimer = null;

    private String mobileToGson = "";
    private Boolean resendCode = false;

    private Boolean fromRegister = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.copy_fragment_activation, container, false);
        databaseViewModel = new ViewModelProvider(this).get(DatabaseViewModel.class);
        binding.lottieMain.setVisibility(View.GONE);
        binding.activationCode.setText("");
        NavOptions.Builder navBuilder = new NavOptions.Builder();
        navBuilder.setEnterAnim(R.anim.slide_from_left).setExitAnim(R.anim.slide_out_right).setPopEnterAnim(R.anim.slide_from_right).setPopExitAnim(R.anim.slide_out_left);
        disposable = new CompositeDisposable();
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        //user = SinoApplication.getInstance().getCurrentUser();

        System.out.println();
        if (getArguments() != null) {
            fromRegister = getArguments().getBoolean("fromRegister");
            if (fromRegister){
                startTimer();
            }
        }


        List<User> users = mainViewModel.getAllUser();
        if (users != null && users.size() > 0) {
            user = users.get(0);
        }

        /*mainViewModel.getAllUser().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new io.reactivex.rxjava3.core.Observer<List<User>>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                disposable.add(d);
            }

            @Override
            public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<User> users) {
                if (users != null && users.size() > 0) {
                    user = users.get(0);
                }
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });*/

        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!resendCode){
                    CuteToast.ct(getActivity(), getString(R.string.invalid_code), CuteToast.LENGTH_SHORT, CuteToast.ERROR, R.drawable.sinoempty).show();
                    return;
                }
                if (binding.activationCode.getText() != null) {

                    if (user.getMobileNo() != null) {
                        System.out.println("getMobileNo=====" + user.getMobileNo());
                        code = GsonGenerator.sendActivationCodeToGson(user.getMobileNo(), binding.activationCode.getText().toString());
                        if (binding.activationCode.getText() != null && !binding.activationCode.getText().toString().trim().isEmpty()) {
                            binding.lottieMain.setVisibility(View.VISIBLE);
                            viewModel.sendActivationCode(code).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new io.reactivex.rxjava3.core.Observer<SuccessActivationBean>() {
                                        @Override
                                        public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                                            disposable.add(d);
                                        }

                                        @Override
                                        public void onNext(@io.reactivex.rxjava3.annotations.NonNull SuccessActivationBean successActivationBean) {

                                            if (successActivationBean.getSUCCESS() != null) {
                                                if (successActivationBean.getRESULT().getName() != null){
                                                    user.setName(successActivationBean.getRESULT().getName());
                                                }

                                                if (successActivationBean.getRESULT().getServerUserId() != null){
                                                    user.setServerUserId(successActivationBean.getRESULT().getServerUserId());
                                                }

                                                if (successActivationBean.getRESULT().getFamily() != null){
                                                    user.setFamily(successActivationBean.getRESULT().getFamily());
                                                }

                                                if (successActivationBean.getRESULT().getUsername() != null){
                                                    user.setUsername(successActivationBean.getRESULT().getUsername());
                                                }

                                                if (successActivationBean.getRESULT().getBisPassword() != null){
                                                    user.setBisPassword(successActivationBean.getRESULT().getBisPassword());
                                                }

                                                if (successActivationBean.getRESULT().getCompanyName() != null){
                                                    user.setCompanyName(successActivationBean.getRESULT().getCompanyName());
                                                }

                                                if (successActivationBean.getRESULT().getCompanyType() != null){
                                                    user.setCompanyType(successActivationBean.getRESULT().getCompanyType());
                                                }

                                                if (successActivationBean.getRESULT().getPictureBytes() != null){
                                                    user.setPictureBytes(successActivationBean.getRESULT().getPictureBytes());
                                                }

                                                user.setLoginIs(true);
                                                mainViewModel.updateUser(user);
                                                SinoApplication.getInstance().setCurrentUser(user);
                                                //createImageUserPath(successActivationBean);

                                                cancelTimer();
                                                NavHostFragment.findNavController(FragmentActivation.this).navigateUp();
                                                NavHostFragment.findNavController(FragmentActivation.this).navigate(R.id.createPasswordFragment, null, null);

                                            }
                                            if (successActivationBean.getERROR() != null) {
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

        binding.txtResendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCode(user);
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

                    mainViewModel.updateUser(user);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposable.clear();
        disposable.dispose();
    }

    public boolean gotoCreatePasswordFragment() {
        NavHostFragment.findNavController(FragmentActivation.this).navigate(R.id.createPasswordFragment, null, null);
        return true;
    }

    void startTimer() {
        binding.txtResendCode.setEnabled(false);
        resendCode = true;
        countDownTimer = new CountDownTimer(120000, 1000) {
            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                long time = millisUntilFinished / 1000;
                long min = (time / 60);
                long sec = (time % 60);
                binding.txtResendCode.setText(Util.latinToFarsiNumberReplacement(String.valueOf("0")) + Util.latinToFarsiNumberReplacement(String.valueOf(min)) +":"+ Util.latinToFarsiNumberReplacement(String.valueOf(sec)));
            }
            public void onFinish() {
                binding.txtResendCode.setText(getString(R.string.resend_activationcode));
                resendCode = false;
                binding.txtResendCode.setEnabled(true);
            }
        };
        countDownTimer.start();
    }


    //cancel timer
    void cancelTimer() {
        if(countDownTimer!=null)
            countDownTimer.cancel();
    }

    private void sendCode(User user) {
        System.out.println("getMobileNo====" + user.getMobileNo());
        mobileToGson = GsonGenerator.mobileNoConfirmationToGson(user.getMobileNo());
        viewModel.sendPhoneNumber(mobileToGson).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SuccessRegisterBean>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull SuccessRegisterBean successRegisterBean) {
                        if (successRegisterBean.getERROR() == null && successRegisterBean.getSUCCESS() != null) {
                            user.setLoginIs(false);
                            mainViewModel.updateUser(user);
                            SinoApplication.getInstance().setCurrentUser(user);
                            CuteToast.ct(getActivity(),getString(R.string.send_activationcode), CuteToast.LENGTH_SHORT, CuteToast.ERROR, R.drawable.sinoempty).show();
                            Config.putSharedPreference(getActivity(), Constant.FORGET_PASS,true);
                            startTimer();
                        } else {
                            CuteToast.ct(getActivity(), successRegisterBean.getERROR(), CuteToast.LENGTH_SHORT, CuteToast.ERROR, R.drawable.sinoempty).show();
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        CuteToast.ct(getActivity(), e.getLocalizedMessage(), CuteToast.LENGTH_SHORT, CuteToast.ERROR, R.drawable.sinoempty).show();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}