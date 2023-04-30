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
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.example.sino.R;
import com.example.sino.SinoApplication;
import com.example.sino.databinding.FragmentCreatePasswordBinding;
import com.example.sino.model.db.User;
import com.example.sino.viewmodel.DatabaseViewModel;
import com.example.sino.viewmodel.MainViewModel;
import com.rejowan.cutetoast_custom.CuteToast;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CreatePasswordFragment extends Fragment {

    private User user;
    private MainViewModel mainViewModel;
    private FragmentCreatePasswordBinding binding;
    private DatabaseViewModel databaseViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_password, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        databaseViewModel = new ViewModelProvider(this).get(DatabaseViewModel.class);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        user = SinoApplication.getInstance().getCurrentUser();
        binding.username.setText(user.getUsername());

        NavOptions.Builder navBuilder = new NavOptions.Builder();
        navBuilder.setEnterAnim(R.anim.slide_from_left).setExitAnim(R.anim.slide_out_right).setPopEnterAnim(R.anim.slide_from_right).setPopExitAnim(R.anim.slide_out_left);


        if (user.getPassword() != null){
            binding.confirmPassword.setVisibility(View.GONE);
        }
        view.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (user.getPassword() != null){
                    if (binding.password.getText() != null || !binding.password.getText().toString().trim().isEmpty()){
                        if (binding.password.getText().toString().equals(user.getPassword())){
                            SinoApplication.getInstance().setCurrentUser(user);
                            NavHostFragment.findNavController(CreatePasswordFragment.this).navigate(R.id.homeFragment, null, null);
                            return;
                        }else {
                            CuteToast.ct(getActivity(),"رمز عبور اشتباه است", CuteToast.LENGTH_SHORT, CuteToast.ERROR, R.drawable.sinoempty).show();
                        }
                    }else {
                        CuteToast.ct(getActivity(), getString(R.string.enter_compelete_data), CuteToast.LENGTH_SHORT, CuteToast.ERROR, R.drawable.sinoempty).show();
                    }
                }

                if (binding.password.getText() == null || binding.password.getText().toString().trim().isEmpty()) {
                    CuteToast.ct(getActivity(), getString(R.string.enter_compelete_data), CuteToast.LENGTH_SHORT, CuteToast.ERROR, R.drawable.sinoempty).show();
                    return;
                }

                if (binding.confirmPassword.getText() == null || binding.confirmPassword.getText().toString().trim().isEmpty()) {
                    CuteToast.ct(getActivity(), getString(R.string.enter_compelete_data), CuteToast.LENGTH_SHORT, CuteToast.ERROR, R.drawable.sinoempty).show();
                    return;
                }

                if (binding.password.getText().toString().trim().length() <4 || binding.confirmPassword.getText().toString().trim().length() <4) {
                    CuteToast.ct(getActivity(), getString(R.string.enter_min_password), CuteToast.LENGTH_SHORT, CuteToast.ERROR, R.drawable.sinoempty).show();
                    return;
                }

                if (!binding.password.getText().toString().trim().equals(binding.confirmPassword.getText().toString().trim())) {
                    CuteToast.ct(getActivity(), getString(R.string.mistake_enter_data), CuteToast.LENGTH_SHORT, CuteToast.ERROR, R.drawable.sinoempty).show();
                    return;
                }
                user.setPassword(binding.password.getText().toString());
                user.setAutoLogin(false);
                user.setLoginIs(true);
                mainViewModel.insertUser(user);
                SinoApplication.getInstance().setCurrentUser(user);
                NavHostFragment.findNavController(CreatePasswordFragment.this).navigate(R.id.homeFragment, null, null);

            }
        });

        binding.txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseViewModel.deleteAllData();
                NavHostFragment.findNavController(CreatePasswordFragment.this).navigate(R.id.splashFragment, null, navBuilder.build());
            }
        });


    }
}