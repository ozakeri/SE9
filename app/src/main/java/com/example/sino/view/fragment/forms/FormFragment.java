package com.example.sino.view.fragment.forms;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.sino.R;
import com.example.sino.SinoApplication;
import com.example.sino.databinding.FragmentFormBinding;
import com.example.sino.db.entity.form.FormQuestionGroup;
import com.example.sino.db.entity.form.SurveyForm;
import com.example.sino.db.entity.form.SurveyFormQuestion;
import com.example.sino.db.entity.form.SurveyFormQuestionTemp;
import com.example.sino.model.db.User;
import com.example.sino.model.form.FormRequentBean;
import com.example.sino.utils.GsonGenerator;
import com.example.sino.utils.SurveyFormStatusEn;
import com.example.sino.view.adapter.form.FormListAdapter;
import com.example.sino.viewmodel.DatabaseViewModel;
import com.example.sino.viewmodel.MainViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class FormFragment extends Fragment {

    private FragmentFormBinding binding;
    private MainViewModel viewModel;
    private DatabaseViewModel databaseViewModel;
    private String inputParam = "";
    private User user;
    private List<SurveyForm> surveyFormList = new ArrayList<>();
    private FormListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_form, container, false);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        databaseViewModel = new ViewModelProvider(this).get(DatabaseViewModel.class);

        fetchData();
        adapter = new FormListAdapter();
        adapter.setOnItemClickListener(new FormListAdapter.onIemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
               // NavOptions.Builder navBuilder = new NavOptions.Builder();
               // navBuilder.setEnterAnim(R.anim.slide_from_left).setExitAnim(R.anim.slide_out_right).setPopEnterAnim(R.anim.slide_from_right).setPopExitAnim(R.anim.slide_out_left);
                //Bundle bundle = new Bundle();
                //SurveyForm surveyForm = surveyFormList.get(position);
                //bundle.putParcelable("surveyForm", surveyForm);
                //NavHostFragment.findNavController(FormFragment.this).navigate(R.id.formQuestionFragment, bundle, navBuilder.build());
            }
        });

        return binding.getRoot();
    }


    private void fetchData() {
        user = SinoApplication.getInstance().getCurrentUser();
        inputParam = GsonGenerator.getUserSurveyFormList(user.getUsername(), user.getBisPassword(), 0);
        viewModel.getUserSurveyFormListVM(inputParam).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FormRequentBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.e("TAG", "onSubscribe: ");
                    }

                    @Override
                    public void onNext(@NonNull FormRequentBean formRequentBean) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

                        if (formRequentBean.success != null) {
                            if (formRequentBean.result != null) {
                                for (int i = 0; i < formRequentBean.result.surveyFormList.size(); i++) {
                                    SurveyForm surveyForm = viewModel.getSurveyFormByIdVM(formRequentBean.result.surveyFormList.get(i).id);
                                    if (surveyForm == null) {
                                        surveyForm = new SurveyForm();
                                        surveyForm.setId(formRequentBean.result.surveyFormList.get(i).id);
                                        surveyForm.setStatusEn(SurveyFormStatusEn.New.ordinal());
                                        surveyForm.setStatusDate(new Date());
                                    }
                                    surveyForm.setName(formRequentBean.result.surveyFormList.get(i).name);
                                    try {
                                        surveyForm.setStartDate(simpleDateFormat.parse(formRequentBean.result.surveyFormList.get(i).startDate));
                                        surveyForm.setEndDate(simpleDateFormat.parse(formRequentBean.result.surveyFormList.get(i).endDate));
                                        surveyForm.setMinScore(formRequentBean.result.surveyFormList.get(i).minScore);
                                        surveyForm.setMaxScore(formRequentBean.result.surveyFormList.get(i).maxScore);
                                        surveyForm.setFormStatus(formRequentBean.result.surveyFormList.get(i).status);
                                        surveyForm.setInputValuesDefault(formRequentBean.result.surveyFormList.get(i).inputValuesDefault);
                                        if (surveyForm.getStatusEn() == null) {
                                            surveyForm.setStatusEn(SurveyFormStatusEn.New.ordinal());
                                            surveyForm.setStatusDate(new Date());
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    databaseViewModel.insertSurveyFormVM(surveyForm);

                                    FormQuestionGroup formQuestionGroup;

                                    if (formRequentBean.result.surveyFormList.get(i).surveyFormQuestionList != null) {
                                        for (int j = 0; j < formRequentBean.result.surveyFormList.get(i).surveyFormQuestionList.size(); j++) {
                                            if (formRequentBean.result.surveyFormList.get(i).surveyFormQuestionList.get(j).id != null) {
                                                Long surveyFormQuestionId = formRequentBean.result.surveyFormList.get(i).surveyFormQuestionList.get(j).id;
                                                Long surveyFormQuestionGroupId = formRequentBean.result.surveyFormList.get(i).surveyFormQuestionList.get(j).groupId;
                                                String surveyFormQuestionGroupName = formRequentBean.result.surveyFormList.get(i).surveyFormQuestionList.get(j).groupName;
                                                long id = Long.parseLong(surveyForm.getId() + "" + surveyFormQuestionGroupId);
                                                formQuestionGroup = viewModel.getCheckListFormQuestionGroupByIdVM(id);
                                                if (formQuestionGroup == null) {
                                                    formQuestionGroup = new FormQuestionGroup();
                                                    formQuestionGroup.setGroupName(surveyFormQuestionGroupName);
                                                    formQuestionGroup.setId(id);
                                                    formQuestionGroup.setGroupId(surveyFormQuestionGroupId);
                                                    formQuestionGroup.setFormId(surveyForm.getId());
                                                    databaseViewModel.insertFormQuestionGroupVM(formQuestionGroup);
                                                }

                                                SurveyFormQuestion surveyFormQuestion = viewModel.getSurveyFormQuestionByIdVM(surveyFormQuestionId);
                                                SurveyFormQuestionTemp surveyFormQuestionTemp = viewModel.getSurveyFormQuestionTempByIdVM(surveyFormQuestionId);

                                                if (surveyFormQuestion == null) {
                                                    surveyFormQuestion = new SurveyFormQuestion();
                                                    surveyFormQuestion.setId(surveyFormQuestionId);
                                                    surveyFormQuestion.setSurveyFormId(surveyForm.getId());
                                                    surveyFormQuestion.setInputValuesDefault(surveyForm.getInputValuesDefault());
                                                    surveyFormQuestion.setFormQuestionGroupId(surveyFormQuestionGroupId);
                                                }

                                                if (formRequentBean.result.surveyFormList.get(i).surveyFormQuestionList.get(j).surveyQuestions != null) {
                                                    surveyFormQuestion.setQuestion(formRequentBean.result.surveyFormList.get(i).surveyFormQuestionList.get(j).surveyQuestions.question);
                                                    surveyFormQuestion.setAnswerTypeEn(formRequentBean.result.surveyFormList.get(i).surveyFormQuestionList.get(j).surveyQuestions.answerTypeEn);
                                                }

                                                databaseViewModel.insertSurveyFormQuestionVM(surveyFormQuestion);

                                                if (surveyFormQuestionTemp == null) {
                                                    surveyFormQuestionTemp = new SurveyFormQuestionTemp();
                                                    surveyFormQuestionTemp.setId(surveyFormQuestionId);
                                                    surveyFormQuestionTemp.setSurveyFormId(surveyForm.getId());
                                                    surveyFormQuestionTemp.setFormQuestionGroupId(surveyFormQuestionGroupId);
                                                }

                                                if (formRequentBean.result.surveyFormList.get(i).surveyFormQuestionList.get(j).surveyQuestions != null) {
                                                    surveyFormQuestionTemp.setQuestion(formRequentBean.result.surveyFormList.get(i).surveyFormQuestionList.get(j).surveyQuestions.question);
                                                    surveyFormQuestionTemp.setAnswerTypeEn(formRequentBean.result.surveyFormList.get(i).surveyFormQuestionList.get(j).surveyQuestions.answerTypeEn);
                                                }

                                                databaseViewModel.insertSurveyFormQuestionTempVM(surveyFormQuestionTemp);
                                            }
                                        }
                                    }

                                }

                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("TAG", "onError: ");
                    }

                    @Override
                    public void onComplete() {
                        databaseViewModel.getAllSurveyForm().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<List<SurveyForm>>() {
                            @Override
                            public void onChanged(List<SurveyForm> surveyForms) {
                                surveyFormList = surveyForms;
                                GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
                                binding.recyclerView.setLayoutManager(mLayoutManager);

                                adapter.setData(surveyForms);
                                binding.recyclerView.setAdapter(adapter);
                            }
                        });
                    }
                });
    }
}