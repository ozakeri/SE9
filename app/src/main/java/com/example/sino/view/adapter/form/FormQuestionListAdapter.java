package com.example.sino.view.adapter.form;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sino.R;
import com.example.sino.db.entity.form.SurveyForm;
import com.example.sino.enumtype.SendingStatusEn;

import java.util.ArrayList;
import java.util.List;

public class FormQuestionListAdapter extends RecyclerView.Adapter<FormQuestionListAdapter.CustomView> {

    private List<SurveyForm> surveyFormList = new ArrayList<>();

    public FormQuestionListAdapter() {

    }

    private static onIemClickListener clickListener;

    @NonNull
    @Override
    public CustomView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.survey_form_list_item, parent, false);
        return new CustomView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomView holder, int position) {

        SurveyForm surveyForm = surveyFormList.get(position);

        if (surveyForm != null) {

            holder.txtTitle.setText(surveyForm.getName());

            if (surveyForm.getStatusEn().equals(0)) {
                holder.imgStatus.setBackgroundResource(R.drawable.formadd);
            } else if (surveyForm.getStatusEn().equals(1)) {
                holder.imgStatus.setBackgroundResource(R.drawable.formdoing);
            } else if (surveyForm.getStatusEn().equals(2)) {
                holder.imgStatus.setBackgroundResource(R.drawable.formcomplet);
            } else if (surveyForm.getSendingStatusEn().equals(SendingStatusEn.Sent.ordinal())) {
                holder.imgStatus.setBackgroundResource(R.drawable.formsend);
            }
        }

    }

    @Override
    public int getItemCount() {
        return surveyFormList.size();
    }

    class CustomView extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtTitle;
        ImageView imgStatus;

        public CustomView(@NonNull View view) {
            super(view);
            txtTitle = view.findViewById(R.id.txt_title);
            imgStatus = view.findViewById(R.id.img_status);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(), view);
        }
    }

    public void setOnItemClickListener(FormQuestionListAdapter.onIemClickListener clickListener) {
        FormQuestionListAdapter.clickListener = clickListener;
    }

    public interface onIemClickListener {
        void onItemClick(int position, View view);
    }

    public void setData(List<SurveyForm> surveyFormList){
        this.surveyFormList = surveyFormList;
    }
}
