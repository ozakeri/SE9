package com.example.sino.view.fragment.reception;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.example.sino.R;
import com.example.sino.databinding.FragmentCarAccessoriesBinding;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;


public class CarAccessoriesFragment extends Fragment {

    private FragmentCarAccessoriesBinding binding;
    private NavOptions.Builder navBuilder;
    private List<String> tagList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_car_accessories, container, false);
        navBuilder = new NavOptions.Builder();
        navBuilder.setEnterAnim(R.anim.slide_from_left).setExitAnim(R.anim.slide_out_right).setPopEnterAnim(R.anim.slide_from_right).setPopExitAnim(R.anim.slide_out_left);

        addChart();


        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tagList = new ArrayList<>();
                if (!binding.edtAdd.getText().toString().equals("")){
                    tagList.add(binding.edtAdd.getText().toString());
                    if (tagList.size()>0){
                        setTag(tagList);
                    }
                }
                binding.edtAdd.setText("");
            }
        });

        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(CarAccessoriesFragment.this).navigate(R.id.addCustomerDataFragment, null, navBuilder.build());
            }
        });


        return binding.getRoot();
    }

    public void addChart() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int height = display.getHeight();  // deprecated
        ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();
        yvalues.add(new PieEntry(25f, 0));
        yvalues.add(new PieEntry(25f, 1));
        yvalues.add(new PieEntry(25f, 2));
        yvalues.add(new PieEntry(25f, 3));


        PieDataSet dataSet = new PieDataSet(yvalues, "");

        PieData data = new PieData(dataSet);
        //data.setDrawValues(true);
        binding.pieChart.setUsePercentValues(true);
        data.setValueFormatter(new PercentFormatter());
        binding.pieChart.setData(data);
        //pieChart.setDescription("This is Pie Chart");

        binding.pieChart.setBackgroundColor(Color.TRANSPARENT);
        binding.pieChart.setHoleColor(Color.WHITE);
        binding.pieChart.setTransparentCircleColor(Color.WHITE);
        binding.pieChart.setTransparentCircleAlpha(0);
        //binding.pieChart.setHoleRadius(120f);
        binding.pieChart.setDrawCenterText(true);
        binding.pieChart.setRotationEnabled(false);
        binding.pieChart.isHighlightPerTapEnabled();
        binding.pieChart.setCenterTextOffset(0f, 0f);
        binding.pieChart.setEntryLabelColor(Color.WHITE);
        binding.pieChart.setEntryLabelTypeface(Typeface.DEFAULT);
        binding.pieChart.setEntryLabelTextSize(16f);
        binding.pieChart.setTransparentCircleRadius(11f);
        binding.pieChart.setCenterText("سوخت باک خودرو");//new line
        binding.pieChart.setHoleRadius(64f);//18f
        binding.pieChart.setCenterTextSize(10);//30
        binding.pieChart.setExtraOffsets(5f, 0f, 10f, -100f);

        // binding.pieChart.setDrawHoleEnabled(true);
        //binding.pieChart.getData().setDrawValues(false);
        //binding.pieChart.setDrawSliceText(false);



        binding.pieChart.setMaxAngle(180.0f);
        binding.pieChart.setRotationAngle(180.0f);
        binding.pieChart.setCenterTextSize(18);
        data.setValueTextSize(40f);

        //dataSet.setDrawValues(false);
        //dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        dataSet.setColors(getResources().getColor(R.color.fuel_four),
                getResources().getColor(R.color.fuel_three),
                getResources().getColor(R.color.fuel_two),
                getResources().getColor(R.color.fuel_one));
        data.setValueTextSize(13f);

        data.setValueTextColor(Color.DKGRAY);
    }

    private void setTag(List<String> tagListParam) {
        for (int index = 0; index < tagListParam.size(); index++) {
            final String tagName = tagListParam.get(index);
            final Chip chip = new Chip(getActivity());
            int paddingDp = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10,
                    getResources().getDisplayMetrics()
            );
            chip.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
            chip.setText(tagName);
            chip.setCloseIconResource(R.drawable.tag_close);
            chip.setCloseIconEnabled(true);
            //Added click listener on close icon to remove tag from ChipGroup
            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tagList.remove(tagName);
                    binding.chipGroup.removeView(chip);
                }
            });

            binding.chipGroup.addView(chip);
        }
    }
}