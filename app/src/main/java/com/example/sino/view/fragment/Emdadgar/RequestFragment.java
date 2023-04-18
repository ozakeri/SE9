package com.example.sino.view.fragment.Emdadgar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.carto.styles.AnimationStyle;
import com.carto.styles.AnimationStyleBuilder;
import com.carto.styles.AnimationType;
import com.carto.styles.MarkerStyle;
import com.carto.styles.MarkerStyleBuilder;
import com.example.sino.R;
import com.example.sino.databinding.FragmentRequestBinding;
import com.example.sino.model.neshan.NeshanRequentBean;
import com.example.sino.utils.common.Util;
import com.example.sino.viewmodel.MainViewModel;

import org.neshan.common.model.LatLng;
import org.neshan.mapsdk.internal.utils.BitmapUtils;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class RequestFragment extends Fragment {

    private FragmentRequestBinding binding;

    // marker animation style
    private AnimationStyle animSt;

    private LatLng first;
    private LatLng second;
    private MainViewModel mainViewModel;
    private CompositeDisposable disposable;
    private Bundle bundle;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_request, container, false);
        disposable = new CompositeDisposable();
        first = new LatLng(35.751722, 51.411131);
        second = new LatLng(35.779923, 51.449674);

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

       // Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.omid);
       // binding.imageView.setImageBitmap(Util.getCroppedBitmap(bitmap));


        NavOptions.Builder navBuilder = new NavOptions.Builder();
        navBuilder.setEnterAnim(R.anim.slide_from_left).setExitAnim(R.anim.slide_out_right).setPopEnterAnim(R.anim.slide_from_right).setPopExitAnim(R.anim.slide_out_left);

        binding.waitProgress.setVisibility(View.VISIBLE);
        binding.waitProgressAddress.setVisibility(View.VISIBLE);
        binding.txtDistance.setVisibility(View.GONE);
        binding.txtTime.setVisibility(View.GONE);
        binding.txtAddress.setVisibility(View.GONE);

        distanceMatrixAPI("car", "35.751722,51.411131", "35.779923,51.449674");
        reverseGeocoding("35.779923", "51.449674");

        binding.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(RequestFragment.this).navigate(R.id.acceptFragment, bundle, navBuilder.build());
            }
        });


        binding.txtSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Util.actionSms(getActivity(), "+989216074738");
            }

        });

        binding.txtCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.actionCall(getActivity(), "+989216074738");
            }
        });

        binding.profileConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(RequestFragment.this).navigate(R.id.customerProfileFragment, null, navBuilder.build());
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        initLayoutReferences();
    }

    // Initializing layout references (views, map and map events)
    private void initLayoutReferences() {

        binding.map.addMarker(createMarker(second));
        binding.map.moveCamera(second, 0);
        binding.map.setZoom(18, 0);

    }


    // This method gets a LatLng as input and adds a marker on that position
    private org.neshan.mapsdk.model.Marker createMarker(LatLng loc) {
        // Creating animation for marker. We should use an object of type AnimationStyleBuilder, set
        // all animation features on it and then call buildStyle() method that returns an object of type
        // AnimationStyle
        AnimationStyleBuilder animStBl = new AnimationStyleBuilder();
        animStBl.setFadeAnimationType(AnimationType.ANIMATION_TYPE_SMOOTHSTEP);
        animStBl.setSizeAnimationType(AnimationType.ANIMATION_TYPE_SPRING);
        animStBl.setPhaseInDuration(0.5f);
        animStBl.setPhaseOutDuration(0.5f);
        animSt = animStBl.buildStyle();

        // Creating marker style. We should use an object of type MarkerStyleCreator, set all features on it
        // and then call buildStyle method on it. This method returns an object of type MarkerStyle
        MarkerStyleBuilder markStCr = new MarkerStyleBuilder();
        markStCr.setSize(30f);
        markStCr.setBitmap(BitmapUtils.createBitmapFromAndroidBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_marker_copy)));
        // AnimationStyle object - that was created before - is used here
        markStCr.setAnimationStyle(animSt);
        MarkerStyle markSt = markStCr.buildStyle();

        // Creating marker
        return new org.neshan.mapsdk.model.Marker(loc, markSt);
    }

    private void changeMarkerToBlue(org.neshan.mapsdk.model.Marker redMarker) {
        // create new marker style
        MarkerStyleBuilder markStCr = new MarkerStyleBuilder();
        markStCr.setSize(30f);
        // Setting a new bitmap as marker
        markStCr.setBitmap(BitmapUtils.createBitmapFromAndroidBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_baseline_lens_24)));
        markStCr.setAnimationStyle(animSt);
        MarkerStyle blueMarkSt = markStCr.buildStyle();

        // changing marker style using setStyle
        redMarker.setStyle(blueMarkSt);
    }

    public void distanceMatrixAPI(String type, String latLngList, String destinations) {

        mainViewModel.getDetailLocation(type, latLngList, destinations).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NeshanRequentBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull NeshanRequentBean neshanRequentBean) {
                        if (neshanRequentBean.rows.get(0).elements.get(0).status.equals("Ok")) {
                            binding.txtDistance.setText("فاصله تا امدادخواه : " + neshanRequentBean.rows.get(0).elements.get(0).distance.text);
                            binding.txtTime.setText("زمان رسیدن تا امدادخواه : " + neshanRequentBean.rows.get(0).elements.get(0).duration.text);

                            binding.waitProgress.setVisibility(View.GONE);
                            binding.txtDistance.setVisibility(View.VISIBLE);
                            binding.txtTime.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public void reverseGeocoding(String lat, String lng) {

        mainViewModel.getReverseGeocoding(lat, lng).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NeshanRequentBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull NeshanRequentBean neshanRequentBean) {
                        if (neshanRequentBean.status.equals("OK")) {
                            binding.txtAddress.setText(neshanRequentBean.state + "-" + neshanRequentBean.formatted_address);
                            binding.waitProgressAddress.setVisibility(View.GONE);
                            binding.txtAddress.setVisibility(View.VISIBLE);
                            bundle = new Bundle();
                            bundle.putString("address",neshanRequentBean.state + "-" + neshanRequentBean.formatted_address);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }
}