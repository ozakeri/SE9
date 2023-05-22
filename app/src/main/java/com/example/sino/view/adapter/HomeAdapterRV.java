package com.example.sino.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sino.R;
import com.example.sino.enumtype.GeneralStatus;
import com.example.sino.utils.common.Util;

import java.util.List;

public class HomeAdapterRV extends RecyclerView.Adapter<HomeAdapterRV.CustomView> {

    private List<String> userPermissionList;
    private static ClickListener clickListener;
    private View view;
    private GeneralStatus generalStatus;
    private String permissionName;

    private Context context;


    public HomeAdapterRV(Context context,List<String> userPermissionList, GeneralStatus generalStatus) {
        this.userPermissionList = userPermissionList;
        this.generalStatus = generalStatus;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (generalStatus.equals(GeneralStatus.IsMenu)) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_items, parent, false);
        }
        if (generalStatus.equals(GeneralStatus.IsHomeList)) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.permission_items, parent, false);
        }
        return new CustomView(view,generalStatus);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomView holder, int position) {
        permissionName = userPermissionList.get(position);
        if (permissionName != null) {

            System.out.println("onBindViewHolder===" + permissionName);

            if (permissionName.equals("ROLE_APP_GET_AGENT_CAR_VIEW_RECP")) {
                if (generalStatus.equals(GeneralStatus.IsHomeList)) {
                    holder.layout_chart.setVisibility(View.VISIBLE);
                   // Util.presentShowcaseView(context, holder.layout_chart,"کارشناسی پذیرش");
                }
                holder.permissionName.setText("ROLE_APP_GET_AGENT_CAR_VIEW_RECP");
                holder.txt_permissionTitle.setText("کارشناسی پذیرش");
                holder.img_permissionPic.setBackgroundResource(R.drawable.reception);
                if (generalStatus.equals(GeneralStatus.IsMenu)) {
                    holder.img_permissionPic.setBackgroundResource(R.drawable.reception);
                }

            }else if (permissionName.equals("ROLE_APP_GET_AGENT_CAR_VIEW_SEC")) {
                if (generalStatus.equals(GeneralStatus.IsHomeList)) {
                    holder.layout_chart.setVisibility(View.VISIBLE);
                    //Util.presentShowcaseView(context, holder.layout_chart,"مدیریت ورود و خروج");
                }
                holder.permissionName.setText("ROLE_APP_GET_AGENT_CAR_VIEW_SEC");
                holder.txt_permissionTitle.setText("مدیریت ورود و خروج");
                holder.img_permissionPic.setBackgroundResource(R.drawable.enter_icon);
                if (generalStatus.equals(GeneralStatus.IsMenu)) {
                    holder.img_permissionPic.setBackgroundResource(R.drawable.enter_icon);
                }

            } else if (permissionName.equals("Setting")) {

                if (generalStatus.equals(GeneralStatus.IsMenu)) {
                    holder.permissionName.setText("Setting");
                    holder.txt_permissionTitle.setText("تنظیمات");
                    holder.img_permissionPic.setBackgroundResource(R.drawable.setting);
                    holder.img_permissionPic.setBackgroundResource(R.drawable.setting);
                }
            } else if (permissionName.equals("Exit")) {

                if (generalStatus.equals(GeneralStatus.IsMenu)) {
                    holder.permissionName.setText("Exit");
                    holder.txt_permissionTitle.setText("خروج از حساب");
                    holder.img_permissionPic.setBackgroundResource(R.drawable.exit);
                    holder.img_permissionPic.setBackgroundResource(R.drawable.exit);
                }
            } else {
                holder.txt_permissionTitle.setVisibility(View.GONE);
                holder.img_permissionPic.setVisibility(View.GONE);
                holder.layout_chart.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return userPermissionList.size();
    }

    public class CustomView extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView img_permissionPic;
        TextView txt_permissionTitle, permissionName;
        LinearLayout layout_chart;

        public CustomView(@NonNull View itemView,GeneralStatus generalStatus) {
            super(itemView);


           // if (generalStatus.equals())

            img_permissionPic = itemView.findViewById(R.id.img_permissionPic);
            txt_permissionTitle = itemView.findViewById(R.id.txt_permissionTitle);
            permissionName = itemView.findViewById(R.id.permissionName);
            layout_chart = itemView.findViewById(R.id.layout_chart);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            System.out.println("====onClick===="+permissionName.getText().toString());
            clickListener.onItemClick(permissionName.getText().toString(), view);
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        HomeAdapterRV.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(String permissionName, View v);
    }
}
