package com.example.sino.view.adapter.car;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sino.R;
import com.example.sino.model.carinfo.Car;

import java.util.List;

public class CarListAdapter extends RecyclerView.Adapter<CarListAdapter.CustomView> {

    private List<Car> carList;
    private static ClickListener clickListener;
    private View view;


    public CarListAdapter(List<Car> carList) {
        this.carList = carList;
    }


    @NonNull
    @Override
    public CustomView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_items, parent, false);
        return new CustomView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomView holder, int position) {
        Car car = carList.get(position);
        if (car != null) {
            holder.txt_type.setText(car.seProModel.proModelGroup.name);
            holder.txt_tip.setText(car.seProModel.name);
            holder.txt_permission.setText(car.licProTypeName);
            holder.txt_plate.setText(car.plateText);
        }
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public class CustomView extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_type,txt_tip,txt_permission,txt_plate;

        public CustomView(@NonNull View itemView) {
            super(itemView);

            txt_type = itemView.findViewById(R.id.txt_type);
            txt_tip = itemView.findViewById(R.id.txt_tip);
            txt_permission = itemView.findViewById(R.id.txt_permission);
            txt_plate = itemView.findViewById(R.id.txt_plate);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(),view);
        }
    }

    public void setOnItemClickListener(CarListAdapter.ClickListener clickListener) {
        CarListAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position,View v);
    }
}
