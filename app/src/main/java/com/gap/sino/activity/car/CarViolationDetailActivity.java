package com.gap.sino.activity.car;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.gap.sino.R;
import com.gap.sino.common.HejriUtil;
import com.gap.sino.webservice.GetJsonService;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CarViolationDetailActivity extends AppCompatActivity {
    TextView codeTV, watchingDateTV, timeTV, locateTV, actionTextTV, nameTV;
    private String result;
    private String jsonData;
    ImageView backIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_car_violation_detail);
        init();
        Bundle bundle = getIntent().getExtras();
        jsonData = bundle.getString("violationCodeJsonObject");
        new ResultTask().execute();
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void init() {
        codeTV = (TextView) findViewById(R.id.code_TV);
        watchingDateTV = (TextView) findViewById(R.id.watchingDate_TV);
        timeTV = (TextView) findViewById(R.id.time_TV);
        locateTV = (TextView) findViewById(R.id.locateType_TV);
        actionTextTV = (TextView) findViewById(R.id.actionText_TV);
        nameTV = (TextView) findViewById(R.id.name_TV);
        backIcon = (ImageView) findViewById(R.id.backIcon);
    }


    ////******get violationCodeJsonObject from bundle*******////
    private class ResultTask extends AsyncTask<Void, Void, Void> {

        ResultTask() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {

            GetJsonService getJson = new GetJsonService();
            result = getJson.JsonReguest("");
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // progress.setVisibility(View.INVISIBLE);

            result = jsonData;
            if (result != null) {
                try {
                    JSONObject resultJson = new JSONObject(result);
                    if (!resultJson.isNull("violation")) {
                        JSONObject violationObject = resultJson.getJSONObject("violation");

                        if (!violationObject.isNull("watchingDate")) {
                            String strStartDate = violationObject.getString("watchingDate");
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                            Date startDate = simpleDateFormat.parse(strStartDate);
                            String hejriStartDate = HejriUtil.chrisToHejri(startDate);
                            watchingDateTV.setText(hejriStartDate);
                        }
                        else {
                            watchingDateTV.setText("---");
                        }

                        if (!violationObject.isNull("violationLocateTypeEn")) {
                            int type = violationObject.getInt("violationLocateTypeEn");
                            switch (type){
                                case 0:
                                    locateTV.setText(R.string.enumType_ViolationLocateTypeEn_Line);
                                    break;
                                case 1:
                                    locateTV.setText(R.string.enumType_ViolationLocateTypeEn_Terminal);
                                    break;
                                case 2:
                                    locateTV.setText(R.string.enumType_ViolationLocateTypeEn_GeoNet);
                                    break;
                                case 3:
                                    locateTV.setText(R.string.enumType_ViolationLocateTypeEn_Other);
                                    break;
                            }
                        }else {
                            locateTV.setText("---");
                        }
                    }
                    if (!resultJson.isNull("violationBaseAction")) {
                        JSONObject violationBaseAction = resultJson.getJSONObject("violationBaseAction");
                        if (!violationBaseAction.isNull("actionText")) {
                            actionTextTV.setText(violationBaseAction.getString("actionText"));
                        }else {
                            actionTextTV.setText("---");
                        }

                        if (!violationBaseAction.isNull("violationTime")) {
                            timeTV.setText(violationBaseAction.getString("violationTime"));
                        }else {
                            timeTV.setText("---");
                        }
                    }
                    if (!resultJson.isNull("violationBase")) {
                        JSONObject violationBase = resultJson.getJSONObject("violationBase");
                        if (!violationBase.isNull("code")) {
                            codeTV.setText(violationBase.getString("code"));
                        }else {
                            codeTV.setText("---");
                        }

                        if (!violationBase.isNull("name")) {
                            nameTV.setText(violationBase.getString("name"));
                        }else {
                            nameTV.setText("---");
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
