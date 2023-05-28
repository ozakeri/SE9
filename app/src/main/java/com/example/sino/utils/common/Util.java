package com.example.sino.utils.common;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.sino.R;
import com.example.sino.SinoApplication;
import com.example.sino.db.entity.AttachFileCopy;
import com.example.sino.model.Device;
import com.example.sino.model.RequestBaseBean;
import com.example.sino.model.complaintreport.ComplaintReport;
import com.example.sino.model.db.TmpChatMessage;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Calendar;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;
import uk.co.deanwild.materialshowcaseview.ShowcaseTooltip;

public class Util {

    private static final String SHOWCASE_ID = "sequence example";

    public static class WSParameter {
        public String key;
        public Object value;

        public WSParameter(String key, Object value) {
            this.key = key;
            this.value = value;
        }
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

    public static String createJson(ArrayList<WSParameter> wsParameters, TmpChatMessage chatMessage) {
        System.out.println("createJson=111==");
        RequestBaseBean requestBaseBean = new RequestBaseBean();
        requestBaseBean.setChatMessage(chatMessage);
        JsonElement jsonElement = new Gson().toJsonTree(requestBaseBean);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        for (WSParameter wsParameter : wsParameters)
            jsonObject.addProperty(wsParameter.key, wsParameter.value + "");
        String json = jsonObject.toString();
        //json = json.replaceAll("\\\\", "");
        System.out.println("createJson=1==" + json);

        return json;
    }

    public static String createJsonReport(ArrayList<WSParameter> wsParameters, ComplaintReport complaintReport) {
        System.out.println("createJson=222==");
        RequestBaseBean requestBaseBean = new RequestBaseBean();
        requestBaseBean.setComplaintReport(complaintReport);
        JsonElement jsonElement = new Gson().toJsonTree(requestBaseBean);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        for (WSParameter wsParameter : wsParameters)
            jsonObject.addProperty(wsParameter.key, wsParameter.value + "");
        String json = jsonObject.toString();
        //json = json.replaceAll("\\\\", "");
        System.out.println("createJson=2==" + json);

        return json;
    }

    public static String createJsonReportAttachment(ArrayList<WSParameter> wsParameters, AttachFileCopy attachFile) {
        System.out.println("createJson=333==");
        RequestBaseBean requestBaseBean = new RequestBaseBean();
        requestBaseBean.setAttachFile(attachFile);
        JsonElement jsonElement = new Gson().toJsonTree(requestBaseBean);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        for (WSParameter wsParameter : wsParameters)
            jsonObject.addProperty(wsParameter.key, wsParameter.value + "");
        String json = jsonObject.toString();
        //json = json.replaceAll("\\\\", "");
        System.out.println("createJson=3==" + json);

        return json;
    }

    public static String createChatMessageJson(ArrayList<WSParameter> wsParameters) {

        JsonObject jsonObject = new JsonObject();
        for (WSParameter wsParameter : wsParameters)
            jsonObject.addProperty(wsParameter.key, wsParameter.value + "");
        String json = jsonObject.toString();
        System.out.println("createJson=4==" + json);
        return json;

    }


    public static Device getDevice() {

        return new Device(getImei(SinoApplication.getInstance().getApplicationContext()),
                getDeviceName(),
                "Android",
                getOSVersion());
    }

    @SuppressLint("HardwareIds")
    public static String getImei(Context context) {
        String imei = null;
        if (context != null) {

            // TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            //imei = telephonyManager.getDeviceId();

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                    imei = android.provider.Settings.Secure.getString(
                            context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
                } else {
                    TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                    imei = telephonyManager.getDeviceId();
                }
            }

            if (imei == null){
                imei = Settings.Secure.getString( context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }

            System.out.println("====imei1111======" + imei);
        }
        return imei;
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public static String getOSVersion() {
        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        return release + " (API: " + sdkVersion + ")";
    }


    public static void showProgress(CircularProgressView progressView) {
        progressView.setVisibility(View.VISIBLE);
    }

    public static void showProgress(ProgressBar progressView) {
        progressView.setVisibility(View.VISIBLE);
    }

    public static void hideProgress(CircularProgressView progressView) {
        progressView.setVisibility(View.GONE);
    }

    public static void hideProgress(ProgressBar progressView) {
        progressView.setVisibility(View.GONE);
    }

    @SuppressLint("ResourceAsColor")
    public static Toast showToast(Toast toast, Context context) {
        Typeface typeface = Typeface.create("BYekan.ttf", Typeface.BOLD);
        toast.setGravity(Gravity.CENTER, 0, 0);

        View view = LayoutInflater.from(context)
                .inflate(R.layout.toast_layout, null);

        TextView messageTextView = view.findViewById(R.id.tvMessage);
        messageTextView.setTextSize(15);
        messageTextView.setTextColor(R.color.mdtp_light_gray);
        messageTextView.setTypeface(typeface);
        return toast;
    }


    public static String farsiNumberReplacement(String text) {
        text = text.replaceAll("۰", "0");
        text = text.replaceAll("۱", "1");
        text = text.replaceAll("۲", "2");
        text = text.replaceAll("۳", "3");
        text = text.replaceAll("۴", "4");
        text = text.replaceAll("۵", "5");
        text = text.replaceAll("۶", "6");
        text = text.replaceAll("۷", "7");
        text = text.replaceAll("۸", "8");
        text = text.replaceAll("۹", "9");

        return text;
    }

    public static String farsiNumberReplacementCopy(String text) {
        text = text.replaceAll("۱۱۱۱", "کد 1111");


        return text;
    }

    public static void showSnack(Context context, View view, String message) {
        View parentLayout = view.findViewById(android.R.id.content);
        Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG)
                .setAction(R.string.closed, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                })
                .setActionTextColor(context.getResources().getColor(android.R.color.holo_red_light))
                .show();
    }


    public static Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }


    public static void actionSms(Context context, String number) {
        int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.SEND_SMS) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        } else {
            // Permission already granted. Enable the SMS button.
            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.putExtra("address", number);
            context.startActivity(smsIntent);


            Intent sendIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("smsto: " + "+989216074738"));
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
                sendIntent.setType("vnd.android-dir/mms-sms");
            sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(sendIntent);
        }
    }

    public static void actionCall(Context context, String number) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.CALL_PHONE},
                    1);

        } else {
            try {
                context.startActivity(callIntent);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    public static String convertLetter(String text) {
        text = text.replaceAll("الف", "01");
        text = text.replaceAll("ب", "02");
        text = text.replaceAll("پ", "03");
        text = text.replaceAll("ت", "04");
        text = text.replaceAll("ث", "05");
        text = text.replaceAll("ج", "06");
        text = text.replaceAll("چ", "07");
        text = text.replaceAll("ح", "08");
        text = text.replaceAll("خ", "09");
        text = text.replaceAll("د", "10");
        text = text.replaceAll("ذ", "11");
        text = text.replaceAll("ر", "12");
        text = text.replaceAll("ز", "13");
        text = text.replaceAll("ژ", "14");
        text = text.replaceAll("س", "15");
        text = text.replaceAll("ش", "16");
        text = text.replaceAll("ص", "17");
        text = text.replaceAll("ض", "18");
        text = text.replaceAll("ط", "19");
        text = text.replaceAll("ظ", "20");
        text = text.replaceAll("ع", "21");
        text = text.replaceAll("غ", "22");
        text = text.replaceAll("ف", "23");
        text = text.replaceAll("ق", "24");
        text = text.replaceAll("ک", "25");
        text = text.replaceAll("گ", "26");
        text = text.replaceAll("ل", "27");
        text = text.replaceAll("م", "28");
        text = text.replaceAll("ن", "29");
        text = text.replaceAll("و", "30");
        text = text.replaceAll("ه", "31");
        text = text.replaceAll("ی", "32");

        return text;
    }

    public static int daysBetween(Calendar day1, Calendar day2){
        Calendar dayOne = (Calendar) day1.clone(),
                dayTwo = (Calendar) day2.clone();

        if (dayOne.get(Calendar.YEAR) == dayTwo.get(Calendar.YEAR)) {
            return Math.abs(dayOne.get(Calendar.DAY_OF_YEAR) - dayTwo.get(Calendar.DAY_OF_YEAR));
        } else {
            if (dayTwo.get(Calendar.YEAR) > dayOne.get(Calendar.YEAR)) {
                //swap them
                Calendar temp = dayOne;
                dayOne = dayTwo;
                dayTwo = temp;
            }
            int extraDays = 0;

            int dayOneOriginalYearDays = dayOne.get(Calendar.DAY_OF_YEAR);

            while (dayOne.get(Calendar.YEAR) > dayTwo.get(Calendar.YEAR)) {
                dayOne.add(Calendar.YEAR, -1);
                // getActualMaximum() important for leap years
                extraDays += dayOne.getActualMaximum(Calendar.DAY_OF_YEAR);
            }

            return extraDays - dayTwo.get(Calendar.DAY_OF_YEAR) + dayOneOriginalYearDays ;
        }
    }

    public static String milliSecondsToTimer(long milliseconds){
        String secondsString = "";

        // Convert total duration into time
        int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);

        // Prepending 0 to seconds if it is one digit
        if(seconds < 10){
            secondsString = "0" + seconds;
        }else{
            secondsString = "" + seconds;}

        // return timer string
        return secondsString;
    }

 /*   public static void presentShowcaseView(Context context,View view,String str) {

       // MaterialShowcaseView.resetSingleUse(context, SHOWCASE_ID);

        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500);

        //MaterialShowcaseSequence sequence = new MaterialShowcaseSequence((Activity) context, SHOWCASE_ID);

        //sequence.setConfig(config);

        ShowcaseTooltip toolTip = ShowcaseTooltip.build(context)
                .corner(10)
                .textColor(Color.parseColor("#007686"))
                .text(str);

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder((Activity) context)
                        .setTarget(view)
                        .setToolTip(toolTip)
                        .setTooltipMargin(30)
                        .setShapePadding(50)
                        .setDismissOnTouch(true)
                        .setMaskColour(context.getColor(R.color.transparentBlack))
                        .build()
        );

        sequence.start();
    }*/
}
