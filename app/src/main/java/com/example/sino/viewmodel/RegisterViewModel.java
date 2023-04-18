package com.example.sino.viewmodel;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.airbnb.lottie.LottieAnimationView;
import com.example.sino.api.NetworkRepository;
import com.example.sino.model.SuccessActivationBean;
import com.example.sino.model.SuccessRegisterBean;
import com.example.sino.model.db.User;
import com.example.sino.utils.common.Util;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;


@HiltViewModel
public class RegisterViewModel extends AndroidViewModel {

    private static String TAG = RegisterViewModel.class.getSimpleName();
    MutableLiveData<SuccessRegisterBean> registerMutableLiveData = new MutableLiveData<>();
    MutableLiveData<SuccessActivationBean> activationMutableLiveData = new MutableLiveData<>();


    @Inject
    NetworkRepository repository;

    @Inject
    public RegisterViewModel(@NonNull Application application) {
        super(application);

    }


    public MutableLiveData<SuccessRegisterBean> getRegisterResult() {
        return registerMutableLiveData;
    }

    public MutableLiveData<SuccessActivationBean> getActivationResult() {
        return activationMutableLiveData;
    }

    public Observable<SuccessRegisterBean> sendPhoneNumber(String INPUT_PARAM) {
        return repository.mobileNoConfirmation(INPUT_PARAM + "&IS_ENCRYPED=false");
    }

    public Observable<SuccessActivationBean> sendActivationCode(String INPUT_PARAM) {
        return repository.activeCodeConfirmation(INPUT_PARAM + "&IS_ENCRYPED=false");
    }
   /* public void sendPhoneNumber(String INPUT_PARAM, LottieAnimationView lottieAnimationView) {
        System.out.println("INPUT_PARAM=" + INPUT_PARAM);

        lottieAnimationView.setVisibility(View.VISIBLE);

        repository.mobileNoConfirmation(INPUT_PARAM).subscribeOn(Schedulers.io())
                .map(new Function<SuccessRegisterBean, SuccessRegisterBean>() {
                    @Override
                    public SuccessRegisterBean apply(SuccessRegisterBean successRegisterBean) throws Throwable {
                        registerMutableLiveData.postValue(successRegisterBean);
                        lottieAnimationView.setVisibility(View.GONE);
                        return successRegisterBean;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> registerMutableLiveData.setValue(result)
                        , throwable -> Log.e(TAG, "sendPhoneNumber: " + throwable.getLocalizedMessage()));
    }*/

/*    public void sendActivationCode(String INPUT_PARAM, LottieAnimationView lottieAnimationView) {
        System.out.println("INPUT_PARAM=" + INPUT_PARAM);
        lottieAnimationView.setVisibility(View.VISIBLE);

        repository.activeCodeConfirmation(INPUT_PARAM).subscribeOn(Schedulers.io())
                .map(new Function<SuccessActivationBean, SuccessActivationBean>() {
                    @Override
                    public SuccessActivationBean apply(SuccessActivationBean successActivationBean) throws Throwable {
                        activationMutableLiveData.postValue(successActivationBean);
                        lottieAnimationView.setVisibility(View.GONE);
                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> activationMutableLiveData.setValue(result)
                        , throwable -> Log.e(TAG, "sendActivationCode: " + throwable.getLocalizedMessage()));
    }*/

    public User getUserByMobileNo(String mobileNo) {
        return repository.getUserByMobileNo(mobileNo);
    }
}
