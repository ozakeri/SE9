package com.example.sino.view.fragment.forms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sino.R;
import com.example.sino.databinding.FragmentFormQuestionBinding;
import com.example.sino.db.entity.form.SurveyForm;
import com.example.sino.db.entity.form.SurveyFormQuestion;
import com.example.sino.utils.CalendarUtil;
import com.example.sino.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FormQuestionFragment extends Fragment {

    private FragmentFormQuestionBinding binding;
    private SurveyForm surveyForm;
    private MainViewModel mainViewModel;
    private List<SurveyFormQuestion> surveyFormQuestionList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_form_question, container, false);

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        if (getArguments() != null) {
            surveyForm = getArguments().getParcelable("surveyForm");
            if (surveyForm.getName() != null) {
                binding.formNameTV.setText(surveyForm.getName());
            }
            if (surveyForm.getStartDate() != null) {
                Date startDate = surveyForm.getStartDate();
                binding.formDateTV.setText(CalendarUtil.convertPersianDateTime(startDate, "yyyy/MM/dd"));
            }

            if (surveyForm.getEndDate() != null) {
                Date endDate = surveyForm.getEndDate();
                binding.creditDateTV.setText(CalendarUtil.convertPersianDateTime(endDate, "yyyy/MM/dd"));
            }

            surveyFormQuestionList = mainViewModel.getSurveyFormQuestionListByFormIdVM(surveyForm.getId());

            for (int i = 0; i < surveyFormQuestionList.size(); i++) {
                SurveyFormQuestion surveyFormQuestion = surveyFormQuestionList.get(i);
                System.out.println("surveyFormQuestion====" + surveyFormQuestion.getQuestion());
                System.out.println(surveyFormQuestion.getId());
                System.out.println(surveyFormQuestion.getSurveyFormId());
            }
        }


        return binding.getRoot();
    }
}