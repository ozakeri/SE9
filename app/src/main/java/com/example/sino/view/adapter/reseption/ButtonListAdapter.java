package com.example.sino.view.adapter.reseption;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sino.R;
import com.example.sino.databinding.BtnListItemBinding;
import com.example.sino.model.reception.PrcSet;
import com.example.sino.view.adapter.car.CarListAdapter;

import java.util.List;

public class ButtonListAdapter extends RecyclerView.Adapter<ButtonListAdapter.CustomView> {

    private static ClickListener clickListener;
    List<PrcSet> processBisSettingVOList;
    private View view;

    public ButtonListAdapter(List<PrcSet> processBisSettingVOList) {
        this.processBisSettingVOList = processBisSettingVOList;
    }

    @NonNull
    @Override
    public CustomView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.btn_list_item, parent, false);
        return new ButtonListAdapter.CustomView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomView holder, int position) {
        PrcSet prcSet = processBisSettingVOList.get(position);
        holder.btn.setText(prcSet.prcSetVO.name);

        System.out.println("=====prcSetId=====" + prcSet.prcSetId);
        if (prcSet.prcSetId.equals("21921")) {
            holder.btn.setText("ثبت اظهارات مشتری");
        }else if (prcSet.prcSetId.equals("21922")){
            holder.btn.setText("ثبت اظهارات کارشناس");
        }
    }

    @Override
    public int getItemCount() {
        return processBisSettingVOList.size();
    }


    public class CustomView extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView btn;
        public CustomView(@NonNull View itemView) {
            super(itemView);
            btn = itemView.findViewById(R.id.btn);
            itemView.setOnClickListener(this);
            btn.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(),view);
        }
    }

    public void setOnItemClickListener(ButtonListAdapter.ClickListener clickListener) {
        ButtonListAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }
}
