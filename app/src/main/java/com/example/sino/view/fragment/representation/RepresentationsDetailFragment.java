package com.example.sino.view.fragment.representation;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.carto.styles.AnimationStyle;
import com.carto.styles.AnimationStyleBuilder;
import com.carto.styles.AnimationType;
import com.carto.styles.MarkerStyle;
import com.carto.styles.MarkerStyleBuilder;
import com.example.sino.R;
import com.example.sino.databinding.FragmentRepresentationsDetailBinding;
import com.example.sino.utils.common.Util;

import org.neshan.common.model.LatLng;
import org.neshan.mapsdk.internal.utils.BitmapUtils;

public class RepresentationsDetailFragment extends Fragment {

    private FragmentRepresentationsDetailBinding binding;
    private AnimationStyle animSt;
    private LatLng second;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_representations_detail, container, false);

        second = new LatLng(35.779923, 51.449674);


        binding.imgCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.actionCall(getActivity(),"+989216074738");
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


    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}