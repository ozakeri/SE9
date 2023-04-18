package com.example.sino.view.adapter.reseption;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sino.R;
import com.example.sino.databinding.ProServListItemBinding;
import com.example.sino.model.reception.ProSrv;
import com.example.sino.utils.JalaliCalendarUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ProSerCarListAdapter extends RecyclerView.Adapter<ProSerCarListAdapter.CustomView> {

    private static ClickListener clickListener;
    private List<ProSrv> proSrvList;

    public ProSerCarListAdapter(List<ProSrv> proSrvList) {
        this.proSrvList = proSrvList;
    }

    @NonNull
    @Override
    public CustomView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProServListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.pro_serv_list_item, parent, false);
        return new CustomView(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomView holder, int position) {
        ProSrv proSrv = proSrvList.get(position);
        holder.bind(proSrv);
    }

    @Override
    public int getItemCount() {
        return proSrvList.size();
    }

    class CustomView extends RecyclerView.ViewHolder implements View.OnClickListener {
        ProServListItemBinding binding;

        public CustomView(ProServListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(this);
        }

        public void bind(ProSrv proSrv) {
            binding.txtCar.setText(proSrv.car.plateText + "\n" + proSrv.car.chassis );


            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                Date date = sdf.parse(proSrv.dateCreation);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                JalaliCalendarUtil jalaliCalendarUtil = new JalaliCalendarUtil(calendar);
                binding.txtDate.setText(jalaliCalendarUtil.getIranianDate() + "  " + jalaliCalendarUtil.getTime(calendar));

            } catch (ParseException e) {
                e.printStackTrace();
            }


        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(), view);
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        ProSerCarListAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }
}
