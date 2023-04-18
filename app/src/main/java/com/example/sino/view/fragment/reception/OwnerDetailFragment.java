package com.example.sino.view.fragment.reception;

import android.app.Dialog;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.example.sino.R;
import com.example.sino.databinding.FragmentOwnerDetailBinding;

public class OwnerDetailFragment extends Fragment {

    private FragmentOwnerDetailBinding binding;
    private NavOptions.Builder navBuilder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_owner_detail, container, false);

        navBuilder = new NavOptions.Builder();
        navBuilder.setEnterAnim(R.anim.slide_from_left).setExitAnim(R.anim.slide_out_right).setPopEnterAnim(R.anim.slide_from_right).setPopExitAnim(R.anim.slide_out_left);


        //binding.txtName.setText(" امید ذاکری ");
        binding.txtAddress.setText("تهران - تهران ");
        binding.txtNationalCode.setText("0536665545");
        binding.txtPhone.setText("09121235468  -  02156488956");

        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activationCodeDialog();
            }
        });
        return binding.getRoot();
    }

    public void activationCodeDialog() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_activation_code);
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.show();

        dialog.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(OwnerDetailFragment.this).navigate(R.id.takePictureFragment, null, navBuilder.build());
                dialog.dismiss();
            }
        });


    }
}