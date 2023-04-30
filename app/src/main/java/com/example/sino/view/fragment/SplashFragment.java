package com.example.sino.view.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.sino.R;
import com.example.sino.SinoApplication;
import com.example.sino.databinding.FragmentSplashBinding;
import com.example.sino.model.db.User;

import com.example.sino.utils.NetworkUtils;
import com.example.sino.viewmodel.MainViewModel;

import java.util.List;
import java.util.concurrent.TimeUnit;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class SplashFragment extends Fragment {

    private FragmentSplashBinding binding;
    private MainViewModel viewModel;
    private User user;
    private CompositeDisposable compositeDisposable;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_splash, container, false);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        compositeDisposable = new CompositeDisposable();
        //NavHostFragment.findNavController(SplashFragment.this).navigate(R.id.takePictureFragment, null, null);

        if (NetworkUtils.VpnConnectionCheck2()) {
            return null;
        }

        viewModel.getAllUser().observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(new io.reactivex.rxjava3.core.Observer<List<User>>() {
                            @Override
                            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                                compositeDisposable.add(d);
                            }

                            @Override
                            public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<User> users) {
                                if (users == null || users.size() == 0) {
                                    //Navigation.findNavController(binding.getRoot()).navigate(R.id.fragmentRegistration);
                                    if (getActivity() != null) {
                                        getActivity().runOnUiThread(new Runnable() {
                                            public void run() {
                                                NavHostFragment.findNavController(SplashFragment.this).navigate(R.id.fragmentRegistration, null, null);
                                            }
                                        });
                                    }

                                    return;
                                }

                                user = users.get(0);
                                SinoApplication.getInstance().setCurrentUser(user);


                                System.out.println("getLoginIs=" + user.getLoginIs());
                                if (!user.getLoginIs()) {
                                    Observable.just(true)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .map(o -> gotoActivationFragment())
                                            .subscribe();

                                    return;
                                }

                                System.out.println("=====getAutoLogin====" + user.getAutoLogin());
                                System.out.println("getAutoLogin=" + user.getAutoLogin());
                                if (!user.getAutoLogin()) {
                                    Observable.just(true)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .map(o -> gotoCreatePasswordFragment())
                                            .subscribe();

                                    return;
                                }

                                System.out.println("gotoHomeFragment=" + user.getAutoLogin());
                                System.out.println("gotoHomeFragment=" + user.getLoginIs());
                                if (user.getAutoLogin() && user.getLoginIs()) {
                                    Observable.just(true).delay(5000, TimeUnit.MILLISECONDS)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .map(o -> gotoHomeFragment())
                                            .subscribe();
                                }
                            }

                            @Override
                            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public boolean gotoHomeFragment() {
        NavHostFragment.findNavController(SplashFragment.this).navigate(R.id.homeFragment, null, null);
        return true;
    }

    public boolean gotoDetectFragment() {
        NavHostFragment.findNavController(SplashFragment.this).navigate(R.id.detectPlateFragment, null, null);
        return true;
    }

    public boolean gotoActivationFragment() {
        NavHostFragment.findNavController(SplashFragment.this).navigate(R.id.fragmentActivation, null, null);
        return true;
    }

    public boolean gotoCreatePasswordFragment() {
        NavHostFragment.findNavController(SplashFragment.this).navigate(R.id.createPasswordFragment, null, null);
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //compositeDisposable.clear();
    }
}