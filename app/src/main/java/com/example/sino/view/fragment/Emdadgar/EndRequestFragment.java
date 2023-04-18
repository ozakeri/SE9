package com.example.sino.view.fragment.Emdadgar;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sino.R;
import com.example.sino.databinding.FragmentEndRequestBinding;
import com.example.sino.view.fragment.HomeFragment;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.rejowan.cutetoast_custom.CuteToast;

public class EndRequestFragment extends Fragment {

    private FragmentEndRequestBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_end_request, container, false);
        NavOptions.Builder navBuilder = new NavOptions.Builder();
        navBuilder.setEnterAnim(R.anim.slide_from_left).setExitAnim(R.anim.slide_out_right).setPopEnterAnim(R.anim.slide_from_right).setPopExitAnim(R.anim.slide_out_left);

        binding.imgReload.setVisibility(View.GONE);

        binding.signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {
                binding.imgReload.setVisibility(View.VISIBLE);
            }

            @Override
            public void onClear() {
                binding.imgReload.setVisibility(View.GONE);
            }
        });

        binding.imgReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.signaturePad.clear();
                binding.imgReload.setVisibility(View.GONE);
            }
        });

        binding.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(EndRequestFragment.this).navigate(R.id.homeFragment, null, navBuilder.build());

            }
        });
        return binding.getRoot();
    }
}