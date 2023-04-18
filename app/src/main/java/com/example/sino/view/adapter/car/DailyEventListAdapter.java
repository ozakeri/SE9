package com.example.sino.view.adapter.car;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sino.R;
import com.example.sino.model.carinfo.Car;
import com.example.sino.model.dailyEvent.DailyEvent;

import java.util.List;

public class DailyEventListAdapter extends RecyclerView.Adapter<DailyEventListAdapter.CustomView> {

    private List<DailyEvent> dailyEventList;
    private static ClickListener clickListener;
    private View view;


    public DailyEventListAdapter(List<DailyEvent> dailyEventList) {
        this.dailyEventList = dailyEventList;
    }


    @NonNull
    @Override
    public CustomView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.daily_event_items, parent, false);
        return new CustomView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomView holder, int position) {
        DailyEvent dailyEvent = dailyEventList.get(position);
        if (dailyEvent != null) {
            holder.txt_type.setText(dailyEvent.carPlateText);
            holder.txt_tip.setText(dailyEvent.entityStrForAttach);
        }
    }

    @Override
    public int getItemCount() {
        return dailyEventList.size();
    }

    public class CustomView extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_type,txt_tip;

        public CustomView(@NonNull View itemView) {
            super(itemView);

            txt_type = itemView.findViewById(R.id.txt_type);
            txt_tip = itemView.findViewById(R.id.txt_tip);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(),view);
        }
    }

    public void setOnItemClickListener(DailyEventListAdapter.ClickListener clickListener) {
        DailyEventListAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position,View v);
    }
}
