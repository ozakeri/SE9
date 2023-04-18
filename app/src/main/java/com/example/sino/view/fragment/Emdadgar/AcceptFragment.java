package com.example.sino.view.fragment.Emdadgar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sino.R;
import com.example.sino.databinding.FragmentAcceptBinding;
import com.example.sino.utils.common.Util;


public class AcceptFragment extends Fragment {

    private FragmentAcceptBinding binding;
    private String address;
    private Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_accept, container, false);

        NavOptions.Builder navBuilder = new NavOptions.Builder();
        navBuilder.setEnterAnim(R.anim.slide_from_left).setExitAnim(R.anim.slide_out_right).setPopEnterAnim(R.anim.slide_from_right).setPopExitAnim(R.anim.slide_out_left);

        //Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.omid);
        //binding.imageView.setImageBitmap(Util.getCroppedBitmap(bitmap));

        if (getArguments()!= null){
            bundle = new Bundle();
            address = getArguments().getString("address");
            binding.txtAddress.setText(address);
            bundle.putString("address",address);
        }

        binding.btnArrived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(AcceptFragment.this).navigate(R.id.givingServiceFragment, bundle, navBuilder.build());
            }
        });

        binding.profileConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(AcceptFragment.this).navigate(R.id.customerProfileFragment, null, navBuilder.build());
            }
        });

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