package com.gap.sino.adapter;

/**
 * Created by Mohamad Cheraghi on 08/01/2016.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gap.sino.R;
import com.gap.sino.common.HejriUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ComplaintAdapter extends ArrayAdapter<JSONObject> {
    private List<JSONObject> list;
    private Integer resourceId;

    public ComplaintAdapter(Context context, int resourceId,
                            List<JSONObject> list) {
        super(context, resourceId, list);
        this.list = list;
        this.resourceId = resourceId;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            //if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            //}
            final JSONObject complaintListJsonObject = list.get(position);
            String requestDescription = null;
            if (complaintListJsonObject != null) {
                if (!complaintListJsonObject.isNull("requestType_text")) {
                    requestDescription = complaintListJsonObject.getString("requestType_text");
                    ((TextView) convertView.findViewById(R.id.violationCode_TV)).setText(requestDescription);
                } else {
                    ((TextView) convertView.findViewById(R.id.violationCode_TV)).setText("---");
                }

                if (!complaintListJsonObject.isNull("complaintBaseSubject")) {
                    requestDescription = complaintListJsonObject.getString("complaintBaseSubject");
                    ((TextView) convertView.findViewById(R.id.violationDate_TV)).setText(requestDescription.substring(0,20) + "...");
                } else {
                    ((TextView) convertView.findViewById(R.id.violationDate_TV)).setText("---");
                }

                if (!complaintListJsonObject.isNull("requestDate")) {

                    @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    String complaintDate = complaintListJsonObject.getString("requestDate");
                    Date startDate = simpleDateFormat.parse(complaintDate);
                    String hejriStartDate = HejriUtil.chrisToHejri(startDate);
                    ((TextView) convertView.findViewById(R.id.violationActionText_TV)).setText(hejriStartDate);
                } else {
                    ((TextView) convertView.findViewById(R.id.violationActionText_TV)).setText("---");
                }
            }
        } catch (Exception e) {
            Log.i(ViolationListAdapter.class.toString(), e.getMessage());
        }

        if (position % 2 == 1) {
            convertView.setBackgroundColor(Color.parseColor("#e6eed5"));
        } else {
            convertView.setBackgroundColor(Color.parseColor("#eeeeee"));
        }

        return convertView;
    }

    @Override
    public long getItemId(int position) {

        try {
            JSONObject jsonObject = list.get(position);
            return jsonObject.getLong("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
