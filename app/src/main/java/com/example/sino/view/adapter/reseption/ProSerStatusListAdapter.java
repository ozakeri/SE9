package com.example.sino.view.adapter.reseption;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sino.R;
import com.example.sino.model.reception.PrcData;
import com.example.sino.utils.JalaliCalendarUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ProSerStatusListAdapter extends RecyclerView.Adapter<ProSerStatusListAdapter.CustomView> {

    private static ClickListener clickListener;
    private List<PrcData> PrcDataList;
    private View view;

    public ProSerStatusListAdapter(List<PrcData> prcDataList) {
        PrcDataList = prcDataList;
    }

    @NonNull
    @Override
    public CustomView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pro_serv_status_list_item, parent, false);
        return new CustomView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomView holder, int position) {
        PrcData prcData = PrcDataList.get(position);
        holder.txt_doing.setText(prcData.prcDataVO.employeeDo);
        holder.txt_name.setText(prcData.prcDataVO.prcSet.name);
        System.out.println("doSuccess=====" + prcData.prcDataVO.doSuccess);
        if (prcData.prcDataVO.doSuccess != null){
            holder.imgConfirm.setVisibility(View.VISIBLE);
            if (prcData.prcDataVO.doSuccess){
                holder.imgConfirm.setBackgroundResource(R.drawable.ic_confirm);
            }else {
                holder.imgConfirm.setBackgroundResource(R.drawable.ic_cancel);
            }
        }else {
            holder.imgConfirm.setVisibility(View.GONE);
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            Date date = sdf.parse(prcData.prcDataVO.dateCreation);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            JalaliCalendarUtil jalaliCalendarUtil = new JalaliCalendarUtil(calendar);
            holder.txt_date.setText(jalaliCalendarUtil.getIranianDate() + "  " + jalaliCalendarUtil.getTime(calendar));

        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return PrcDataList.size();
    }

    public class CustomView extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_name,txt_doing,txt_date;
        LinearLayout linearLayout;
        ImageView imgConfirm;
        public CustomView(@NonNull View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_doing = itemView.findViewById(R.id.txt_doing);
            txt_date = itemView.findViewById(R.id.txt_date);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            imgConfirm = itemView.findViewById(R.id.imgConfirm);
            itemView.setOnClickListener(this);
            linearLayout.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(),view);
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        ProSerStatusListAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

}
