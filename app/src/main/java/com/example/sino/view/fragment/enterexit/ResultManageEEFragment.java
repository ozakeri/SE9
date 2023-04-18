package com.example.sino.view.fragment.enterexit;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sino.R;
import com.example.sino.databinding.FragmentResultManageEEBinding;
import com.example.sino.model.carinfo.Car;
import com.example.sino.utils.GlobalValue;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ResultManageEEFragment extends Fragment {

    private FragmentResultManageEEBinding binding;
    private Car car;
    private String plateNo = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_result_manage_e_e, container, false);

        GlobalValue.plateNo = null;

        if (getArguments() != null) {
            //car = getArguments().getParcelable("car");
            plateNo = getArguments().getString("plateNo");
            binding.cardResultOk.setVisibility(View.VISIBLE);
            binding.cardNoResult.setVisibility(View.GONE);

            binding.txtPlate.setText(" شماره پلاک : " + car.plateText);
            binding.txtChassiss.setText(" شماره شاسی : " + car.chassis);
            binding.txtColor.setText(" رنگ خودرو : " + car.color.name);
            binding.txtCarType.setText(" نوع خودرو : " + car.seProModel.proModelGroup.name);

        }else {
            binding.cardResultOk.setVisibility(View.GONE);
            binding.cardNoResult.setVisibility(View.VISIBLE);
        }
        return binding.getRoot();
    }
}