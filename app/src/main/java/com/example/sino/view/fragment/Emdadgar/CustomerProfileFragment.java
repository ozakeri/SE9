package com.example.sino.view.fragment.Emdadgar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sino.R;
import com.example.sino.databinding.FragmentCustomerProfileBinding;
import com.example.sino.utils.common.Util;


public class CustomerProfileFragment extends Fragment {

    private FragmentCustomerProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_customer_profile, container, false);

        //Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.omid);
        //binding.imageView4.setImageBitmap(Util.getCroppedBitmap(bitmap));

        binding.txtSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.actionSms(getActivity(),"+989216074738");
            }
        });

        binding.txtCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.actionCall(getActivity(),"+989216074738");
            }
        });

        return binding.getRoot();
    }
}