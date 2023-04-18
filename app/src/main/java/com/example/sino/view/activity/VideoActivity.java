package com.example.sino.view.activity;


import static com.shahaabco.satpa.SatpaSDK.anpr_create;
import static com.shahaabco.satpa.SatpaSDK.anpr_recognize_mat;
import static com.shahaabco.satpa.SatpaSDK.anpr_set_params;
import static org.opencv.core.Core.flip;
import static org.opencv.core.Core.subtract;
import static org.opencv.imgproc.Imgproc.FONT_HERSHEY_SIMPLEX;
import static org.opencv.imgproc.Imgproc.putText;
import static org.opencv.imgproc.Imgproc.rectangle;
import static java.lang.Math.max;
import static java.lang.Math.min;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.view.ViewCompat;

import com.example.sino.R;
import com.example.sino.utils.GlobalValue;
import com.example.sino.utils.common.Util;
import com.shahaabco.satpa.SatpaLicense;
import com.shahaabco.satpa.SatpaSDK;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VideoActivity extends CameraActivity implements CameraBridgeViewBase.CvCameraViewListener2, SensorEventListener {

    static final String TAG = "SATPA-Demo-Video";
    private TextView txt_one, txt_two, txt_three, txt_four;
    private String plateText;

    static {
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
        }

        System.loadLibrary("satpa");
    }


    private static final int PERMISSION_REQUEST_CODE_CAMERA = 1;
    Button btnPlay, btn_finish;
    Button imgBack;
    TextView tvPlate;
    LinearLayout panel;
    //////////////////////////////////////
    private Mat mRgba, gray_mask;
    private int counter;
    private CameraBridgeViewBase mOpenCvCameraView;
    private SensorManager mSensorManager;
    float mGravity[] = new float[3];//accelerometer values: in landscape mode, X is about 10 others are 0, in portrait mode, Y is about 10
    Sensor accelerometer;
    private SatpaSDK.SPlate plate, final_plate;
    private boolean pause = false;
    int prev_state = -1;//0: portrait, 1: landscape
    //String mcs_path;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            /*switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i("OpenCV", "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                    // mOpenCvCameraView.setOnTouchListener(ColorBlobDetectionActivity.this);
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }*/
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1)
            if (resultCode == RESULT_OK)
                initialize_satpa();
    }

    private void initialize_satpa() {
        SatpaLicense.anpr_check_lic_file(this); //TODO: 1. First step is to check if a valid license exists

        short result = anpr_create((byte) 0, "www.shahaab-co.com02331099", (byte) 0); //TODO: 2. create a new instance of SATPA library

        if (result >= 0) {
            //Toast.makeText(this, "SATPA Initialized Correctly", Toast.LENGTH_SHORT).show();
            if (tvPlate != null) {
                tvPlate.setText("ٍنتیجه پلاک");
                tvPlate.setTextColor(Color.WHITE);
            }
        } else {
            Toast.makeText(this, "SATPA Failed to Load", Toast.LENGTH_SHORT).show();

            if (tvPlate != null) {
                tvPlate.setText("Failed");
                tvPlate.setTextColor(Color.RED);
            }
        }

        anpr_set_params((byte) 0, (byte) 8, (byte) 5, (byte) 1, (short) 500); //TODO: 3. Apply Satpa Settings
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        bundle.getInt("requestKey");

        initialize_satpa();

        Context ctx = getApplicationContext();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        File dir = ctx.getExternalFilesDir(null);
        //mcs_path = dir.getPath();// + "/IRP.mcs";

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.fragment_capture);

        // Asking for permissions
        String[] accessPermissions = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE
        };

       // PrepareCameraAndUI();

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
                    PERMISSION_REQUEST_CODE_CAMERA);
        } else
            PrepareCameraAndUI();
    }

    private void PrepareCameraAndUI() {
        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.surfaceView);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        mOpenCvCameraView.enableView();


        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);

        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String cameraId : manager.getCameraIdList()) {
                CameraCharacteristics characteristics
                        = manager.getCameraCharacteristics(cameraId);

                Log.d("Img", "INFO_SUPPORTED_HARDWARE_LEVEL " + characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL));
                Log.d("Img", "INFO_REQUIRED_HARDWARE_LEVEL FULL" + CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_FULL);
                Log.d("Img", "INFO_REQUIRED_HARDWARE_LEVEL 3" + CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_3);
                Log.d("Img", "INFO_REQUIRED_HARDWARE_LEVEL LIMITED" + CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LIMITED);
                Log.d("Img", "INFO_REQUIRED_HARDWARE_LEVEL LEGACY" + CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        panel = (LinearLayout) findViewById(R.id.panel);
        btn_finish = (Button) findViewById(R.id.btn_finish);
        imgBack = findViewById(R.id.btnBack);
        btnPlay = (Button) findViewById(R.id.btn_process);
        btnPlay.setOnClickListener(onPlayClicked);
        btn_finish.setOnClickListener(goBack);

        tvPlate = (TextView) findViewById(R.id.txtPlate);
        //tvSite = (TextView) findViewById(R.id.txtSite);

        txt_one = (TextView) findViewById(R.id.txt_one);
        txt_two = (TextView) findViewById(R.id.txt_two);
        txt_three = (TextView) findViewById(R.id.txt_three);
        txt_four = (TextView) findViewById(R.id.txt_four);

        findViewById(R.id.incluse_layout).setRotation(-90);
        panel.setRotation(-90);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE_CAMERA:
              //  PrepareCameraAndUI();
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PrepareCameraAndUI();
                } else {
                    Toast.makeText(getApplicationContext(), "Some Permissions Failed", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    //Camera not working if this method is not implemented (took several days to find it!! 1400/01/11 Khosravi)
    @Override
    protected List<? extends CameraBridgeViewBase> getCameraViewList() {
        return Collections.singletonList(mOpenCvCameraView);
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }

        if (mSensorManager != null)
            mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);

        if (mOpenCvCameraView != null)
            mOpenCvCameraView.enableView();
    }

    private View.OnClickListener onPlayClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final_plate = plate;
            pause = !pause;
            if (pause)
                btnPlay.setText("شروع");
            else
                btnPlay.setText("متوقف");
        }
    };

    private View.OnClickListener goBack = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.putExtra("plateText", plateText);
            setResult(2, intent);
            finish();//finishing activity
        }
    };

    @Override
    public void onCameraViewStarted(int width, int height) {
        //Toast.makeText(getApplicationContext(), "Cam started: " + String.format("(%d, %d)", height, width), Toast.LENGTH_LONG).show();
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        gray_mask = new Mat(mRgba.size(), CvType.CV_8UC4, Scalar.all(50));
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                mGravity = event.values;
                break;
        }
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        if (pause)
            return mRgba;
        Scalar color = new Scalar(255, 255, 255);

        //input width is always 1280 (or something like this). if application is landscape its OK otherwise it must be rotated for correct processing
        int inW = mRgba.cols();//1280
        int inH = mRgba.rows();//720
        float scale = inW / 1280.0f;

        int x, y = (int) (20 * scale), w = min((int) (500 * scale), inW), h;
        boolean rtl = (ViewCompat.getLayoutDirection(findViewById(android.R.id.content)) == ViewCompat.LAYOUT_DIRECTION_RTL);
        if (rtl)
            x = max(0, (inW - w + tvPlate.getWidth()) / 2);
        else
            x = max(0, (inW - w - tvPlate.getWidth()) / 2);

        h = min((int) (175 * scale), inH - y);
        //following 3-lines doesn't work because screen orientation is set to landscape for correct camera image
        //int orientation = getResources().getConfiguration().orientation;
        //int rotation = getWindowManager().getDefaultDisplay().getRotation();
        //final int screenOrientation = ((WindowManager)  getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation();

        //String s = mGravity[0] + " ** " + mGravity[1] + " ** " + mGravity[2];
        String s = inW + "x" + inH;
        Point pt_start = new Point(30, 30);
        if (rtl)
            pt_start.x += tvPlate.getWidth();

        putText(mRgba, s, pt_start, FONT_HERSHEY_SIMPLEX, 1, new Scalar(0, 0, 200), 1, 16, false);

        Rect rc;
        boolean portrait = Math.abs(mGravity[1]) > Math.abs(mGravity[0] * 2);
        if (portrait) //portrait mode (swap parameters)
        {
            int t = w;
            w = h;
            h = t;
            x = (int) (20 * scale);
            if (rtl)
                x += tvPlate.getWidth();
            y = max(0, (inH - h) / 2);
            ;
            rc = new Rect(x, y, w, h);
            if (prev_state != 0) {//Screen State Changed
                /*tvPlate.setRotation(-90);
                tvSite.setRotation(-90);
                btnActivate.setRotation(-90);
                btnPlay.setRotation(-90);*/
                gray_mask.setTo(Scalar.all(50));
                gray_mask.submat(rc).setTo(Scalar.all(0));
            }
            prev_state = 0;
        } else {
            rc = new Rect(x, y, w, h);
            if (prev_state != 1) {//Screen State Changed
                /*tvPlate.setRotation(0);
                tvSite.setRotation(0);
                btnActivate.setRotation(0);
                btnPlay.setRotation(0);*/
                gray_mask.setTo(Scalar.all(50));
                gray_mask.submat(rc).setTo(Scalar.all(0));
            }
            prev_state = 1;
        }
        subtract(mRgba, gray_mask, mRgba);
        rectangle(mRgba, rc.tl(), rc.br(), color, 2, 8, 0);
        counter++;
        Mat sub = mRgba.submat(rc);
        if (portrait)
            flip(sub.t(), sub, 1);
        /*if((counter % 50)== 0) {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath();

            path += "/ANPR/Mat_" + counter + ".jpg";
            File file = new File(path);

            Uri imgUri = Uri.fromFile(file);
            Imgcodecs.imwrite(path, mRgba);

            path = Environment.getExternalStorageDirectory().getAbsolutePath();
            path += "/ANPR/Mat_sub_" + counter + ".jpg";
            file = new File(path);

            imgUri = Uri.fromFile(file);
            Imgcodecs.imwrite(path, sub);
        }*/

        long startTime = System.currentTimeMillis();
        final SatpaSDK.SPlate p = anpr_recognize_mat(sub);
        long endTime = System.currentTimeMillis();
        final long elapsed = endTime - startTime;
        if (p.str.length() > 4 && (p.cnf[0] > 0.5)) {
            plate = p;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //tvPlate.setText(p.str + "\n" + p.str_fa + "\n" + elapsed + "ms\ncnf: " + String.format("%.2f", p.cnf[0]));
                    tvPlate.setText(p.str + "\n" + "" + "\n" + p.str_fa);

                    System.out.println("1============" + p.str);
                    System.out.println("2============" + p.str_fa);

                    try {

                        String words = p.str_fa;
                        char index1 = words.charAt(0);
                        char index2 = words.charAt(1);
                        char index3 = words.charAt(2);
                        char index4 = words.charAt(3);
                        char index5 = words.charAt(4);
                        char index6 = words.charAt(5);
                        char index7 = words.charAt(6);
                        char index8 = words.charAt(7);

                        txt_one.setText(index7 + " " + index8);
                        txt_two.setText(index4 + " " + index5 + " " + index6);
                        GlobalValue.farsiWord = String.valueOf(index3);

                        if (String.valueOf(index3).contains("ا")){
                            GlobalValue.farsiWord = "الف";
                        }
                        txt_three.setText(String.valueOf(index3));
                        txt_four.setText(index1 + " " + index2);

                        System.out.println("index1===========" + index1);
                        System.out.println("index2===========" + index2);
                        System.out.println("index3===========" + index3);
                        System.out.println("index4===========" + index4);
                        System.out.println("index5===========" + index5);
                        System.out.println("index6===========" + index6);
                        System.out.println("index7===========" + index7);
                        System.out.println("index8===========" + index8);

                        GlobalValue.txtFour =index1+""+index2;
                        GlobalValue.txtTwo =index4+""+index5+""+index6;
                        GlobalValue.txtOne = index7+""+index8;

                        //   121212729999036
                        //   182214479999025

                        plateText = "1" + Util.farsiNumberReplacement(String.valueOf(index1)) +
                                "" + Util.farsiNumberReplacement(String.valueOf(index2)) +
                                Util.convertLetter(GlobalValue.farsiWord) +
                                Util.farsiNumberReplacement(String.valueOf(index4)) +
                                "" + Util.farsiNumberReplacement(String.valueOf(index5)) +
                                "" + Util.farsiNumberReplacement(String.valueOf(index6))
                                + "99990" + Util.farsiNumberReplacement(String.valueOf(index7)) +
                                "" + Util.farsiNumberReplacement(String.valueOf(index8));

                        GlobalValue.plateNo = plateText;

                        //117262179999055
                        //182214479999025

                      /*  plateText = " ایران "
                                + CommonUtil.farsiNumberReplacement(String.valueOf(index7))
                                + ""
                                + CommonUtil.farsiNumberReplacement(String.valueOf(index8))
                                + "-"
                                + CommonUtil.farsiNumberReplacement(String.valueOf(index4))
                                + ""
                                + CommonUtil.farsiNumberReplacement(String.valueOf(index5))
                                + ""
                                + CommonUtil.farsiNumberReplacement(String.valueOf(index6))
                                + ""
                                + CommonUtil.farsiNumberReplacement(String.valueOf(index3))
                                + ""
                                + CommonUtil.farsiNumberReplacement(String.valueOf(index1))
                                + ""
                                + CommonUtil.farsiNumberReplacement(String.valueOf(index2));*/

                    } catch (IndexOutOfBoundsException e) {
                        Log.e(TAG, "IndexOutOfBoundsException: " + e.getMessage());
                    }
                }
            });
            s = p.str + "\n" + elapsed + " ms";
            pt_start.y += 30;
            putText(mRgba, p.str, pt_start, FONT_HERSHEY_SIMPLEX, 1, new Scalar(0, 0, 200), 1, 16, false);
        }

        return mRgba;
    }

}
