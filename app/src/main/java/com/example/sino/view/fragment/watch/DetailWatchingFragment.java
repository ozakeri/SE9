package com.example.sino.view.fragment.watch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.sino.R;
import com.example.sino.databinding.FragmentDetailWatchingBinding;

public class DetailWatchingFragment extends Fragment {


    private FragmentDetailWatchingBinding binding;

    private static final String[] statusProcess = {
            " منتظر بازدید خودرو ",
            " منتظر مراجعه ",
            " در حال اجرا در سوله ",
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_watching, container, false);

        binding.spinner.setItems(statusProcess);

        return binding.getRoot();
    }
}