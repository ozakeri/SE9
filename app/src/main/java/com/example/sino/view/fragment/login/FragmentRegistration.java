package com.example.sino.view.fragment.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.sino.R;
import com.example.sino.SinoApplication;
import com.example.sino.databinding.FragmentRegistrationBinding;
import com.example.sino.model.SuccessRegisterBean;
import com.example.sino.model.db.User;
import com.example.sino.utils.GsonGenerator;
import com.example.sino.viewmodel.MainViewModel;
import com.example.sino.viewmodel.RegisterViewModel;
import com.rejowan.cutetoast_custom.CuteToast;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class FragmentRegistration extends Fragment {
    private String mobileToGson = "";
    private RegisterViewModel viewModel;
    private MainViewModel mainViewModel;
    private FragmentRegistrationBinding binding;
    private CompositeDisposable disposable;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_registration, container, false);

        disposable = new CompositeDisposable();
        //Util.hideProgress(binding.progressView);
        binding.lottieMain.setVisibility(View.GONE);

        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.mobileNo.getText() != null && !binding.mobileNo.getText().toString().trim().isEmpty()) {
                    mobileToGson = GsonGenerator.mobileNoConfirmationToGson(binding.mobileNo.getText().toString());
                    binding.lottieMain.setVisibility(View.VISIBLE);
                    viewModel.sendPhoneNumber(mobileToGson).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<SuccessRegisterBean>() {
                                @Override
                                public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                                    disposable.add(d);
                                }

                                @Override
                                public void onNext(@io.reactivex.rxjava3.annotations.NonNull SuccessRegisterBean successRegisterBean) {
                                    if (successRegisterBean.getERROR() == null && successRegisterBean.getSUCCESS() != null) {
                                        User user = new User();
                                        user.setMobileNo(binding.mobileNo.getText().toString());
                                        mainViewModel.insertUser(user);
                                        SinoApplication.getInstance().setCurrentUser(user);
                                        NavHostFragment.findNavController(FragmentRegistration.this).navigateUp();
                                        NavHostFragment.findNavController(FragmentRegistration.this).navigate(R.id.fragmentActivation, null, null);

                                    } else {

                                        System.out.println("ERROR=" + successRegisterBean.getERROR());
                                        CuteToast.ct(getActivity(), successRegisterBean.getERROR(), CuteToast.LENGTH_SHORT, CuteToast.ERROR, R.drawable.sinoempty).show();

                                    }
                                }

                                @Override
                                public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                    CuteToast.ct(getActivity(), e.getLocalizedMessage(), CuteToast.LENGTH_SHORT, CuteToast.ERROR, R.drawable.sinoempty).show();
                                    binding.lottieMain.setVisibility(View.GONE);
                                    System.out.println("ERROR=" + e.getLocalizedMessage());
                                    System.out.println("ERROR=" + e.getMessage());
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
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }
}