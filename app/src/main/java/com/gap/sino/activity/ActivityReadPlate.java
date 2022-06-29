package com.gap.sino.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.gap.sino.R;
import com.gap.sino.app.AppController;
import com.gap.sino.common.CommonUtil;
import com.gap.sino.common.Constants;
import com.gap.sino.db.manager.DatabaseManager;
import com.gap.sino.db.manager.IDatabaseManager;
import com.gap.sino.db.objectmodel.DeviceSetting;
import com.gap.sino.exception.WebServiceException;
import com.gap.sino.service.CoreService;
import com.gap.sino.util.DateUtils;
import com.gap.sino.webservice.MyPostJsonService;
import com.shahaabco.satpa.SatpaLicense;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
//import org.opencv.android.OpenCVLoader;

public class ActivityReadPlate extends AppCompatActivity {

    private static final String TAG = "ANPR_Demo";
    private TextView txt_three, txt_four, txt_plateText, txt_vinNo, txt_chassis;
    private String plateText, chassis;
    private IDatabaseManager databaseManager;
    private CoreService coreService;
    private ImageView img_car;
    private Bitmap bitmap;
    private String plateText_str, vinNo_str, chassis_str;
    private Button btn_cancel, btn_confirm;
    private boolean chassisImportant = false;

    static {
        //ANPRLIB Step 2
        //System.loadLibrary("satpa");

        /*if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
        }*/
    }

    Button btnActivate, btnPhotoDemo, btnCameraDemo, btn_custom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_plate);


        databaseManager = new DatabaseManager(this);
        coreService = new CoreService(databaseManager);

        btnActivate = (Button) findViewById(R.id.btn_activate);
        btnActivate.setOnClickListener(onActivateClicked);

        btnPhotoDemo = (Button) findViewById(R.id.btn_photo_demo);
        btnPhotoDemo.setOnClickListener(onPhotoDemoClicked);

        btnCameraDemo = (Button) findViewById(R.id.btn_camera_demo);
        btnCameraDemo.setOnClickListener(onCameraDemoClicked);

        btn_custom = findViewById(R.id.btn_custom);
        btn_custom.setOnClickListener(openDialog);

        // Asking for permissions
        String[] accessPermissions = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE
        };

        ArrayList<String> permissionNeededList = new ArrayList<String>();
        for (String access : accessPermissions) {
            int curPermission = ActivityCompat.checkSelfPermission(this, access);
            if (curPermission != PackageManager.PERMISSION_GRANTED) {
                permissionNeededList.add(access);
            }
        }

        if (permissionNeededList.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionNeededList.toArray(new String[permissionNeededList.size()]),
                    1);
        }
    }


    private View.OnClickListener onActivateClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(ActivityReadPlate.this, SatpaLicense.class);
            startActivityForResult(intent, 1);
        }
    };

    private View.OnClickListener onPhotoDemoClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(ActivityReadPlate.this, PhotoActivity.class);
            startActivityForResult(intent, 2);
        }
    };

    private View.OnClickListener onCameraDemoClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(ActivityReadPlate.this, VideoActivity.class);
            startActivityForResult(intent, 2);
        }
    };


    private View.OnClickListener openDialog = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

            Dialog dialog = new Dialog(ActivityReadPlate.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_enter_car_plate);
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setAttributes(layoutParams);
            dialog.show();

            EditText txt_shasi = dialog.findViewById(R.id.txt_shasi);
            EditText txt_one = dialog.findViewById(R.id.txt_one);
            EditText txt_two = dialog.findViewById(R.id.txt_two);
            txt_three = dialog.findViewById(R.id.txt_three);
            txt_four = dialog.findViewById(R.id.txt_four);
            btn_cancel = dialog.findViewById(R.id.btn_cancel);
            btn_confirm = dialog.findViewById(R.id.btn_confirm);

            txt_one.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (txt_one.getText().length() == 2) {
                        txt_two.requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            txt_two.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (txt_two.getText().length() == 3) {
                        txt_three.performClick();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            txt_three.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialogWords();
                }
            });

            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            btn_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (txt_shasi.getText().toString().isEmpty() || txt_shasi.getText().toString().trim().equals("")) {
                        Toast toast = Toast.makeText(ActivityReadPlate.this, "Shasi Is Null", Toast.LENGTH_LONG);
                        CommonUtil.showToast(toast, ActivityReadPlate.this);
                        toast.show();
                        return;
                    }

                    if (txt_one.getText().toString().isEmpty() || txt_one.getText().toString().trim().equals("")) {
                        Toast toast = Toast.makeText(ActivityReadPlate.this, "شماره پلاک را صحرح وارد کنید", Toast.LENGTH_LONG);
                        CommonUtil.showToast(toast, ActivityReadPlate.this);
                        toast.show();
                        return;
                    }

                    if (txt_two.getText().toString().isEmpty() || txt_two.getText().toString().trim().equals("")) {
                        Toast toast = Toast.makeText(ActivityReadPlate.this, "شماره پلاک را صحرح وارد کنید", Toast.LENGTH_LONG);
                        CommonUtil.showToast(toast, ActivityReadPlate.this);
                        toast.show();
                        return;
                    }

                    if (txt_four.getText().toString().isEmpty() || txt_four.getText().toString().trim().equals("")) {
                        Toast toast = Toast.makeText(ActivityReadPlate.this, "شماره پلاک را صحرح وارد کنید", Toast.LENGTH_LONG);
                        CommonUtil.showToast(toast, ActivityReadPlate.this);
                        toast.show();
                        return;
                    }

                    chassisImportant = true;
                    chassis = CommonUtil.farsiNumberReplacement(txt_shasi.getText().toString());
                    plateText = " ایران " + txt_one.getText().toString() + "-" + txt_two.getText().toString() + "" + txt_three.getText().toString() + "" + txt_four.getText().toString();
                    System.out.println("plateText======" + plateText);
                    new ASync().execute();

                }
            });
        }
    };

    public void showDialogWords() {
        final String[] strName = new String[1];
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(ActivityReadPlate.this);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ActivityReadPlate.this, android.R.layout.select_dialog_item);
        arrayAdapter.add("الف");
        arrayAdapter.add("ج");
        arrayAdapter.add("ع");
        arrayAdapter.add("ب");
        arrayAdapter.add("ی");
        arrayAdapter.add("ل");
        arrayAdapter.add("ا");
        arrayAdapter.add("ت");
        arrayAdapter.add("ن");
        arrayAdapter.add("م");
        arrayAdapter.add("ه");
        arrayAdapter.add("ص");
        arrayAdapter.add("ط");


        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                strName[0] = arrayAdapter.getItem(which);
                System.out.println("strName=====" + strName[0]);
                txt_three.setText(strName[0]);
                txt_four.requestFocus();
            }
        });
        builderSingle.show();
    }

    private class ASync extends AsyncTask<Void, Void, Void> {
        private String result;
        private String errorMsg;
        private ProgressDialog progressDialog = null;

        @SuppressLint("StringFormatInvalid")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(View.VISIBLE);
            progressDialog = new ProgressDialog(ActivityReadPlate.this);
            progressDialog.setMessage(getResources().getString(R.string.label_progress_dialog));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(true);
            progressDialog.show();

            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    ActivityReadPlate.ASync.this.cancel(true);
                    progressDialog.dismiss();
                }
            });

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            if (result != null) {
                chassisImportant = false;
                try {
                    JSONObject resultJson = new JSONObject(result);
                    if (errorMsg == null && !resultJson.isNull(Constants.SUCCESS_KEY)) {
                        if (!resultJson.isNull(Constants.RESULT_KEY)) {
                            bitmap = null;
                            JSONObject jsonObject = resultJson.getJSONObject(Constants.RESULT_KEY);
                            if (!jsonObject.isNull("car")) {
                                JSONObject carInfoJsonObject = jsonObject.getJSONObject("car");
                                if (!carInfoJsonObject.isNull("pictureAF")) {
                                    JSONObject pictureAFJSONObject = carInfoJsonObject.getJSONObject("pictureAF");
                                    if (!pictureAFJSONObject.isNull("pictureBytes")) {
                                        JSONArray pictureBytesJsonArray = pictureAFJSONObject.getJSONArray("pictureBytes");
                                        byte[] bytes = new byte[pictureBytesJsonArray.length()];
                                        for (int i = 0; i < pictureBytesJsonArray.length(); i++) {
                                            bytes[i] = Integer.valueOf(pictureBytesJsonArray.getInt(i)).byteValue();
                                        }
                                        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    }
                                }

                                if (!carInfoJsonObject.isNull("plateText")) {
                                    plateText_str = carInfoJsonObject.getString("plateText");
                                }

                                if (!carInfoJsonObject.isNull("vinNo")) {
                                    vinNo_str = carInfoJsonObject.getString("vinNo");
                                }

                                if (!carInfoJsonObject.isNull("chassis")) {
                                    chassis_str = carInfoJsonObject.getString("chassis");
                                }


                                detailDialog(bitmap, plateText_str, vinNo_str, chassis_str);
                            }

                        }
                    } else {
                        if (errorMsg == null) {
                            errorMsg = resultJson.getString(Constants.ERROR_KEY);
                        }
                        Toast toast = Toast.makeText(ActivityReadPlate.this, errorMsg, Toast.LENGTH_LONG);
                        CommonUtil.showToast(toast, ActivityReadPlate.this);
                        toast.show();
                    }
                } catch (JSONException e) {
                    Toast toast = Toast.makeText(ActivityReadPlate.this, getResources().getString(R.string.Some_error_accor_contact_admin), Toast.LENGTH_LONG);
                    CommonUtil.showToast(toast, ActivityReadPlate.this);
                    toast.show();
                }
            } else {
                Toast toast = Toast.makeText(ActivityReadPlate.this, (errorMsg != null) ? errorMsg : getResources().getString(R.string.Some_error_accor_contact_admin), Toast.LENGTH_LONG);
                CommonUtil.showToast(toast, ActivityReadPlate.this);
                toast.show();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isDeviceDateTimeValid()) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    AppController application = (AppController) getApplication();
                    jsonObject.put("username", application.getCurrentUser().getUsername());
                    jsonObject.put("tokenPass", application.getCurrentUser().getBisPassword());

                    jsonObject.put("plateText", plateText);
                    jsonObject.put("chassis", chassis);
                    jsonObject.put("chassisImportant", chassisImportant);

                    System.out.println("doInBackground==" + plateText);
                    //jsonObject.put("chassis", plateText);
                    //jsonObject.put("carInfoType", carInfoType);
                    MyPostJsonService postJsonService = new MyPostJsonService(databaseManager, ActivityReadPlate.this);
                    try {
                        result = postJsonService.sendData("getCarInfo", jsonObject, true);
                    } catch (SocketTimeoutException | SocketException e) {
                        errorMsg = getResources().getString(R.string.Some_error_accor_contact_admin);
                    } catch (WebServiceException e) {
                        Log.d("RegistrationFragment", e.getMessage());
                    }

                } catch (JSONException e) {
                    Log.d("RegistrationFragment", e.getMessage());
                }
            }
            return null;
        }


        ////******getServerDateTime*******////

        private boolean isDeviceDateTimeValid() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_TIME_FORMAT);
            try {
                JSONObject jsonObjectParam = new JSONObject();
                MyPostJsonService postJsonService = new MyPostJsonService(databaseManager, ActivityReadPlate.this);
                result = postJsonService.sendData("getServerDateTime", jsonObjectParam, true);

                if (result != null) {
                    JSONObject resultJson = new JSONObject(result);
                    if (!resultJson.isNull(Constants.SUCCESS_KEY)) {
                        JSONObject jsonObject = resultJson.getJSONObject(Constants.RESULT_KEY);
                        Date serverDateTime = simpleDateFormat.parse(jsonObject.getString("serverDateTime"));
                        if (DateUtils.isValidDateDiff(new Date(), serverDateTime, Constants.VALID_SERVER_AND_DEVICE_TIME_DIFF)) {
                            DeviceSetting deviceSetting = coreService.getDeviceSettingByKey(Constants.DEVICE_SETTING_KEY_LAST_CHANGE_DATE);
                            if (deviceSetting == null) {
                                deviceSetting = new DeviceSetting();
                                deviceSetting.setKey(Constants.DEVICE_SETTING_KEY_LAST_CHANGE_DATE);
                            }
                            deviceSetting.setValue(simpleDateFormat.format(new Date()));
                            deviceSetting.setDateLastChange(new Date());
                            coreService.saveOrUpdateDeviceSetting(deviceSetting);
                            return true;
                        } else {
                            errorMsg = getResources().getString(R.string.Invalid_Device_Date_Time);
                        }
                    }
                }
            } catch (SocketTimeoutException | SocketException e) {
                errorMsg = getResources().getString(R.string.Some_error_accor_contact_admin);
            } catch (JSONException | ParseException | WebServiceException e) {
                errorMsg = getResources().getString(R.string.Some_error_accor_contact_admin);
                Log.d("SyncActivity", e.getMessage());
            }
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (data != null) {
                plateText = data.getStringExtra("plateText");
                chassis = data.getStringExtra("chassis");
                System.out.println("plateText=====" + plateText);
                System.out.println("chassis=====" + chassis);

                if (plateText != null) {
                    new ASync().execute();
                }

            }

        }
    }

    public void detailDialog(Bitmap bitmap, String plateText_str, String vinNo_str, String chassis_str) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        Dialog dialog = new Dialog(ActivityReadPlate.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_detail_read_plate);
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.show();

        txt_plateText = dialog.findViewById(R.id.txt_plateText);
        txt_vinNo = dialog.findViewById(R.id.txt_vinNo);
        txt_chassis = dialog.findViewById(R.id.txt_chassis);
        img_car = dialog.findViewById(R.id.img_car);
        if (bitmap != null){
            img_car.setImageBitmap(bitmap);
        }
        txt_plateText.setText(" پلاک : " + plateText_str);
        txt_vinNo.setText(vinNo_str + " : vin ");
        txt_chassis.setText(" شاسی : " + chassis_str);
    }
}