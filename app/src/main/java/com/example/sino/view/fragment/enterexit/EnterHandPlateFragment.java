package com.example.sino.view.fragment.enterexit;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.example.sino.R;
import com.example.sino.SinoApplication;
import com.example.sino.databinding.FragmentEnterHandPlateBinding;
import com.example.sino.db.entity.AttachFile;
import com.example.sino.enumtype.SendingStatusEn;
import com.example.sino.model.carinfo.Car;
import com.example.sino.model.carinfo.SuccessCarInfoBean;
import com.example.sino.model.dailyEvent.DailyEvent;
import com.example.sino.model.dailyEvent.DailyEventRespons;
import com.example.sino.model.db.User;
import com.example.sino.model.enumType.EntityNameEn;
import com.example.sino.utils.CameraPreview;
import com.example.sino.utils.Config;
import com.example.sino.utils.CustomProgressDialog;
import com.example.sino.utils.GlobalValue;
import com.example.sino.utils.GsonGenerator;
import com.example.sino.utils.ImageManager;
import com.example.sino.utils.JalaliCalendarUtil;
import com.example.sino.utils.common.Constant;
import com.example.sino.utils.common.Util;
import com.example.sino.utils.services.ApiServiceAsync;
import com.example.sino.viewmodel.DatabaseViewModel;
import com.example.sino.viewmodel.MainViewModel;
import com.rejowan.cutetoast_custom.CuteToast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class EnterHandPlateFragment extends Fragment {

    private FragmentEnterHandPlateBinding binding;
    private int eventTypeEn = 15;
    private int typeGetCar = -1;

    private int typePrc = 0;
    private int carType = 0;

    private int ObjActionEn = -1;
    private int carId = 0;
    private int dailEvn_id = 0;
    private int parentId = 0;
    private String plateNo = null;
    private String description = "";

    private String inputParam = "";

    private User user;
    private MainViewModel viewModel;
    private List<Car> carList;
    private SuccessCarInfoBean successCarInfoBeanCopy;
    private Car car;

    private Camera camera;
    private CameraPreview preview;
    private static final int REQUEST_CODE_CHANGE_SETTING = 1;

    private String filePath = null;
    private AttachFile attachFile;

    private DatabaseViewModel databaseViewModel;
    private ApiServiceAsync apiServiceAsync;
    private NavOptions.Builder navBuilder;
    private SimpleDateFormat sdf;

    private int selectPlateType = 0;
    private  DailyEventRespons dailyEventResponsCopy;

    private int valTypePrcEn = 0;
    private int valCarTypeMdlEn = 0;
    private String valCarPlateNo = null;
    private String valCarPlateText = null;
    private DailyEvent valDailyEvent = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_enter_hand_plate, container, false);
        user = SinoApplication.getInstance().getCurrentUser();
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        databaseViewModel = new ViewModelProvider(this).get(DatabaseViewModel.class);
        apiServiceAsync = new ApiServiceAsync();
        navBuilder = new NavOptions.Builder();
        navBuilder.setEnterAnim(R.anim.slide_from_left).setExitAnim(R.anim.slide_out_right).setPopEnterAnim(R.anim.slide_from_right).setPopExitAnim(R.anim.slide_out_left);

        /*if (GlobalValue.ManageEEFragment != null){
            GlobalValue.ManageEEFragment = null;
            NavHostFragment.findNavController(EnterHandPlateFragment.this).navigate(R.id.homeFragment, null, navBuilder.build());
            return null;
        }*/
        sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");

        if (getArguments()!= null){
            //eventType = getArguments().getInt("eventType");
            typeGetCar = getArguments().getInt("typeGetCar");
            plateNo = getArguments().getString("plateNo");
            valDailyEvent = getArguments().getParcelable("dailyEvent");
            
            
            if (valDailyEvent != null){
                ObjActionEn = 3;

                binding.linearLayoutPlate1.setVisibility(View.GONE);
                binding.layoutSelectPlateType.setVisibility(View.GONE);
                plateNo = valDailyEvent.carPlateNo;

                switch (valDailyEvent.carTypeMdlEn){
                    case 0:
                        binding.radioButtonTruck.setChecked(true);
                        carType = 0;
                        break;

                    case 1:
                        binding.radioButtonBus.setChecked(true);
                        carType = 1;
                        break;

                    case 2:
                        binding.radioButtonMiniBus.setChecked(true);
                        carType = 2;
                        break;

                    case 3:
                        binding.radioButtonPickup.setChecked(true);
                        carType = 3;
                        break;

                    case 4:
                        binding.radioButtonSedan.setChecked(true);
                        carType = 4;
                        break;

                    case 6:
                        binding.radioButtonMechanicalCar.setChecked(true);
                        carType = 6;
                        break;

                        case 8:
                            binding.radioButtonTruckSemi.setChecked(true);
                            carType = 8;
                        break;
                }


                switch (valDailyEvent.typePrcEn){
                    case 0:
                        typePrc = 0;
                        binding.radioButtonRepair.setChecked(true);
                        break;

                    case 1:
                        typePrc = 1;
                        binding.radioButtonGuest.setChecked(true);
                        break;

                    case 2:
                        typePrc = 2;
                        binding.radioButtonCompany.setChecked(true);
                        break;

                    case 3:
                        typePrc = 3;
                        binding.radioButtonSecurityCar.setChecked(true);
                        break;
                }

                inputParam = GsonGenerator.saveDailyEventCopy(user.getUsername(), user.getBisPassword(),valDailyEvent.id,GlobalValue.companyCode,ObjActionEn);
                ProgressDialog dialog = ProgressDialog.show(getActivity(), "","لطفا منتظر بمانید..." , true);

                viewModel.dailyEvent_MngAction(inputParam).subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<DailyEventRespons>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onNext(@NonNull DailyEventRespons dailyEventRespons) {

                                dailyEventResponsCopy = dailyEventRespons;

                                if (dailyEventRespons.result != null){
                                    if (dailyEventRespons.result.dailyEvent != null) {
                                        //System.out.println("entityStrForAttach=====" + dailyEventRespons.result.dailyEvent.entityStrForAttach);
                                        //System.out.println("eventTypeEn=====" + dailyEventRespons.result.dailyEvent.event.eventTypeEn);
                                        //System.out.println("processStatus=====" + dailyEventRespons.result.dailyEvent.processStatus);

                                        if (getActivity() != null) {
                                            getActivity().runOnUiThread(new Runnable() {
                                                public void run() {
                                                    binding.txtCarIsNull.setVisibility(View.INVISIBLE);
                                                    dialog.dismiss();

                                                    dailEvn_id = dailyEventRespons.result.dailyEvent.id;

                                                    if (eventTypeEn == 16){
                                                        NavHostFragment.findNavController(EnterHandPlateFragment.this).navigate(R.id.manageEEFragment, null, navBuilder.build());
                                                        Toast.makeText(getActivity(), " درخواست با شناسه کار " + dailEvn_id +  " ثبت شد ", Toast.LENGTH_LONG).show();
                                                        GlobalValue.ManageEEFragment = "ManageEEFragment";
                                                        return;
                                                    }

                                                    if (dailyEventRespons.result.dailyEvent.car != null){
                                                        binding.carTypeLayout.setVisibility(View.GONE);
                                                        binding.carInfoLayout.setVisibility(View.VISIBLE);
                                                        binding.txtCarIsNull.setVisibility(View.INVISIBLE);

                                                        if (dailyEventRespons.result.dailyEvent.car.plateText != null){
                                                            binding.txtPlate.setText(" شماره پلاک : " + dailyEventRespons.result.dailyEvent.car.plateText);
                                                        }else {
                                                            binding.txtPlate.setVisibility(View.GONE);
                                                        }

                                                        if (dailyEventRespons.result.dailyEvent.car.chassis != null){
                                                            binding.txtChassiss.setText(" شماره شاسی : " + dailyEventRespons.result.dailyEvent.car.chassis);
                                                        }else {
                                                            binding.txtChassiss.setVisibility(View.GONE);
                                                        }

                                                        if (dailyEventRespons.result.dailyEvent.car.color != null){
                                                            binding.txtColor.setText(" رنگ خودرو : " + dailyEventRespons.result.dailyEvent.car.color.name);
                                                        }else {
                                                            binding.txtColor.setVisibility(View.GONE);
                                                        }

                                                        if (dailyEventRespons.result.dailyEvent.car.seProModel != null){
                                                            binding.txtCarType.setText(" نوع خودرو : " + dailyEventRespons.result.dailyEvent.car.seProModel.proModelGroup.name);
                                                        }else {
                                                            binding.txtCarType.setVisibility(View.GONE);
                                                        }

                                                        if (dailyEventRespons.result.dailyEvent.car != null){
                                                            if (dailyEventRespons.result.dailyEvent != null){
                                                                if (dailyEventRespons.result.dailyEvent.car.seLicPro != null){
                                                                    if (dailyEventRespons.result.dailyEvent.car.seLicPro.endDatePlan != null){
                                                                        try {
                                                                            Date date = sdf.parse(dailyEventRespons.result.dailyEvent.car.seLicPro.endDatePlan);
                                                                            Calendar calendar = Calendar.getInstance();
                                                                            calendar.setTime(date);
                                                                            JalaliCalendarUtil jalaliCalendarUtil = new JalaliCalendarUtil(calendar);
                                                                            binding.txtLice.setText(" گارانتی : " + dailyEventRespons.result.dailyEvent.car.seLicPro.licTypeEn_text + " " +
                                                                                    jalaliCalendarUtil.getIranianYear() + "/" + jalaliCalendarUtil.getIranianMonth() + "/" + jalaliCalendarUtil.getIranianDay());
                                                                        } catch (ParseException e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                    }

                                                                }
                                                            }
                                                        }else {
                                                            binding.txtLice.setVisibility(View.GONE);
                                                        }
                                                    }else {
                                                        if (typeGetCar == 0){
                                                            binding.carTypeLayout.setVisibility(View.VISIBLE);
                                                            //binding.txtCarIsNull.setVisibility(View.VISIBLE);
                                                        }

                                                    }

                                                    if (dailyEventRespons.result.dailyEvent.processStatus != null && dailyEventRespons.result.dailyEvent.processStatus == 0){

                                                        binding.btnConfirmTwo.setVisibility(View.VISIBLE);
                                                        binding.btnCancel.setVisibility(View.VISIBLE);
                                                        if (typeGetCar == 0){
                                                            binding.takePhotoLayout.setVisibility(View.VISIBLE);
                                                        }

                                                        binding.btnSave.setVisibility(View.GONE);
                                                        binding.carEnterInfoLayout.setVisibility(View.GONE);
                                                        System.out.println("dailEvn_id=====" + dailEvn_id);
                                                        Toast.makeText(getActivity(), " درخواست با شناسه کار " + dailEvn_id +  " ثبت شد ", Toast.LENGTH_LONG).show();

                                                        binding.txtId.setVisibility(View.VISIBLE);
                                                        binding.txtDateTime.setVisibility(View.VISIBLE);

                                                        binding.txtId.setText(" درخواست با شناسه کار " + dailEvn_id + " ثبت شد ");
                                                        binding.txtDateTime.setText(dailyEventRespons.result.dailyEvent.entityStrForAttach);


                                                        if (false){
                                                            Toast.makeText(getActivity(), " درخواست با شناسه کار " + dailEvn_id +  " ثبت شد ", Toast.LENGTH_LONG).show();
                                                            NavHostFragment.findNavController(EnterHandPlateFragment.this).navigate(R.id.manageEEFragment, null, navBuilder.build());
                                                            GlobalValue.ManageEEFragment = "ManageEEFragment";
                                                        }else {
                                                            showDialog(dailyEventRespons.result.dailyEvent.entityStrForAttach);
                                                        }
                                                    }else  if (dailyEventRespons.result.dailyEvent.processStatus != null && dailyEventRespons.result.dailyEvent.processStatus == 5){

                                                        binding.cardEnterType.setVisibility(View.GONE);
                                                        binding.cardEnterTypeResult.setVisibility(View.VISIBLE);

                                                        // binding.txtCarIsNull.setVisibility(View.VISIBLE);
                                                        // binding.txtCarIsNull.setText(dailyEventRespons.result.dailyEvent.event.name +" با پلاگ "+dailyEventRespons.result.dailyEvent.car.plateText+ " با شناسه :"+ " " + dailyEventRespons.result.dailyEvent.event.id);

                                                        binding.btnExit.setVisibility(View.VISIBLE);
                                                        binding.carEnterInfoLayout.setVisibility(View.VISIBLE);
                                                        binding.btnSave.setVisibility(View.GONE);
                                                        binding.txtEventName.setText(" فرایند : " + dailyEventRespons.result.dailyEvent.event.name + " با شناسه :"+ " " + dailyEventRespons.result.dailyEvent.event.id);
                                                        //binding.txtEventId.setText(" شناسه فرایند : " + dailyEventRespons.result.dailyEvent.event.id);

                                                        Date startData = null;
                                                        Date endData = null;
                                                        try {
                                                            startData = sdf.parse(dailyEventRespons.result.dailyEvent.startTime);
                                                            Calendar calendar = Calendar.getInstance();
                                                            calendar.setTime(startData);
                                                            JalaliCalendarUtil jalaliCalendarUtil = new JalaliCalendarUtil(calendar);
                                                            binding.txtEventTime.setText( " زمان ورود :  " + jalaliCalendarUtil.getIranianYear() + "/" + jalaliCalendarUtil.getIranianMonth() + "/" + jalaliCalendarUtil.getIranianDay()
                                                                    + " ساعت : " + jalaliCalendarUtil.getTime(calendar));

                                                            String format = sdf.format(new Date());
                                                            endData = sdf.parse(format);

                                                            long difference_In_Time = endData.getTime() - startData.getTime();

                                                            // Calculate time difference in
                                                            // seconds, minutes, hours, years,
                                                            // and days
                                                            long difference_In_Seconds
                                                                    = (difference_In_Time
                                                                    / 1000)
                                                                    % 60;

                                                            long difference_In_Minutes
                                                                    = (difference_In_Time
                                                                    / (1000 * 60))
                                                                    % 60;

                                                            long difference_In_Hours
                                                                    = (difference_In_Time
                                                                    / (1000 * 60 * 60))
                                                                    % 24;

                                                            long difference_In_Years
                                                                    = (difference_In_Time
                                                                    / (1000l * 60 * 60 * 24 * 365));

                                                            long difference_In_Days
                                                                    = (difference_In_Time
                                                                    / (1000 * 60 * 60 * 24))
                                                                    % 365;


                                                            if (difference_In_Days == 0){
                                                                if (difference_In_Hours == 0){
                                                                    binding.txtDuration.setText(difference_In_Minutes +" دقیقه "  );
                                                                }else {
                                                                    binding.txtDuration.setText("مدت ورود : " + difference_In_Hours + " ساعت " +" و " + difference_In_Minutes +  " دقیقه ");
                                                                }

                                                            }else {
                                                                binding.txtDuration.setText("مدت ورود : " + difference_In_Days +" روز "  +" و "+  difference_In_Hours + " ساعت ");
                                                            }

                                                        } catch (ParseException e) {
                                                            e.printStackTrace();
                                                        }

                                                        parentId = dailyEventRespons.result.dailyEvent.id;
                                                        if (dailyEventRespons.result.dailyEvent.typePrcEn == 0){
                                                            binding.txtType.setText(" نوع ورود :  تعمیرات");
                                                        }else  if (dailyEventRespons.result.dailyEvent.typePrcEn == 1){
                                                            binding.txtType.setText(" نوع ورود :  مهمان");
                                                        }else if (dailyEventRespons.result.dailyEvent.typePrcEn == 2){
                                                            binding.txtType.setText(" نوع ورود :  خودرو امانی");
                                                        }
                                                        Toast.makeText(getActivity(), " درخواست با شناسه کار " + parentId +  " با موفقیت انجام شد ", Toast.LENGTH_LONG).show();

                                                    } else if (dailyEventRespons.result.dailyEvent.objActionEnFP != null && dailyEventRespons.result.dailyEvent.objActionEnFP == 9){
                                                        Toast.makeText(getActivity(), " درخواست با شناسه کار " + dailEvn_id +  " با موفقیت انجام شد ", Toast.LENGTH_LONG).show();
                                                        NavHostFragment.findNavController(EnterHandPlateFragment.this).navigate(R.id.manageEEFragment, null, navBuilder.build());
                                                        GlobalValue.ManageEEFragment = "ManageEEFragment";

                                                    }
                                                    else if (dailyEventRespons.result.dailyEvent.objActionEnFP != null && dailyEventRespons.result.dailyEvent.objActionEnFP == 10){
                                                        System.out.println("=====objActionEnFP=====" + dailyEventRespons.result.dailyEvent.objActionEnFP);
                                                        Toast.makeText(getActivity(), " درخواست با شناسه کار " + dailEvn_id +  " لغو شد ", Toast.LENGTH_LONG).show();
                                                        NavHostFragment.findNavController(EnterHandPlateFragment.this).navigate(R.id.manageEEFragment, null, navBuilder.build());
                                                        GlobalValue.ManageEEFragment = "ManageEEFragment";
                                                    }/*else if (dailyEventRespons.result.dailyEvent.processStatus != null && (dailyEventRespons.result.dailyEvent.processStatus == 6 || dailyEventRespons.result.dailyEvent.processStatus == 1)){

                                                if (isCancel){
                                                    System.out.println("=====objActionEnFP=====" + dailyEventRespons.result.dailyEvent.objActionEnFP);
                                                    Toast.makeText(getActivity(), " درخواست با شناسه کار " + dailEvn_id +  " لغو شد ", Toast.LENGTH_LONG).show();
                                                    NavHostFragment.findNavController(EnterHandPlateFragment.this).navigate(R.id.manageEEFragment, null, navBuilder.build());
                                                    GlobalValue.ManageEEFragment = "ManageEEFragment";
                                                }
                                            }*/
                                                }
                                            });
                                        }
                                    }
                                }else if (dailyEventRespons.ERROR != null){

                                    if (getActivity() != null) {
                                        getActivity().runOnUiThread(new Runnable() {
                                            public void run() {
                                                Toast toast = Toast.makeText(getActivity(), dailyEventRespons.ERROR, Toast.LENGTH_LONG);
                                                Util.showToast(toast, getActivity());
                                                toast.show();
                                                dialog.dismiss();
                                            }
                                        });
                                    }

                                }


                            }

                            @Override
                            public void onError(@NonNull Throwable e) {

                                if (getActivity() != null) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        public void run() {

                                        }
                                    });
                                }

                            }

                            @Override
                            public void onComplete() {
                                if (getActivity() != null) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        public void run() {
                                            dialog.dismiss();
                                            if (dailyEventResponsCopy.ERROR != null){
                                                Toast toast = Toast.makeText(getActivity(), dailyEventResponsCopy.ERROR, Toast.LENGTH_LONG);
                                                Util.showToast(toast, getActivity());
                                                toast.show();
                                            }
                                        }
                                    });
                                }
                            }
                        });
            }
        }

        binding.cardNoResult.setVisibility(View.VISIBLE);
        binding.cardEnterType.setVisibility(View.VISIBLE);
        binding.cardEnterTypeResult.setVisibility(View.GONE);
        binding.carInfoLayout.setVisibility(View.GONE);
        binding.txtCarIsNull.setVisibility(View.GONE);
        binding.carEnterInfoLayout.setVisibility(View.GONE);
        if (typeGetCar == 0){
            binding.carTypeLayout.setVisibility(View.VISIBLE);
        }else {
            binding.carTypeLayout.setVisibility(View.GONE);
        }

        autoSelectText();

        binding.txtFour.requestFocus();


        binding.radioButtonTruck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carType = 0;
                binding.radioButtonTruck.setChecked(true);
                binding.radioButtonTruckSemi.setChecked(false);
                binding.radioButtonBus.setChecked(false);
                binding.radioButtonMiniBus.setChecked(false);
                binding.radioButtonMechanicalCar.setChecked(false);
                binding.radioButtonPickup.setChecked(false);
                binding.radioButtonSedan.setChecked(false);
            }
        });

        binding.radioButtonTruckSemi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carType = 8;
                binding.radioButtonTruck.setChecked(false);
                binding.radioButtonTruckSemi.setChecked(true);
                binding.radioButtonBus.setChecked(false);
                binding.radioButtonMiniBus.setChecked(false);
                binding.radioButtonMechanicalCar.setChecked(false);
                binding.radioButtonPickup.setChecked(false);
                binding.radioButtonSedan.setChecked(false);
            }
        });

        binding.radioButtonBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carType = 1;
                binding.radioButtonTruck.setChecked(false);
                binding.radioButtonTruckSemi.setChecked(false);
                binding.radioButtonBus.setChecked(true);
                binding.radioButtonMiniBus.setChecked(false);
                binding.radioButtonMechanicalCar.setChecked(false);
                binding.radioButtonPickup.setChecked(false);
                binding.radioButtonSedan.setChecked(false);
            }
        });

        binding.radioButtonMiniBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carType = 2;
                binding.radioButtonTruck.setChecked(false);
                binding.radioButtonTruckSemi.setChecked(false);
                binding.radioButtonBus.setChecked(false);
                binding.radioButtonMiniBus.setChecked(true);
                binding.radioButtonMechanicalCar.setChecked(false);
                binding.radioButtonPickup.setChecked(false);
                binding.radioButtonSedan.setChecked(false);
            }
        });

        binding.radioButtonMechanicalCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carType = 6;
                binding.radioButtonTruck.setChecked(false);
                binding.radioButtonTruckSemi.setChecked(false);
                binding.radioButtonBus.setChecked(false);
                binding.radioButtonMiniBus.setChecked(false);
                binding.radioButtonMechanicalCar.setChecked(true);
                binding.radioButtonPickup.setChecked(false);
                binding.radioButtonSedan.setChecked(false);
            }
        });

        binding.radioButtonPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carType = 3;
                binding.radioButtonTruck.setChecked(false);
                binding.radioButtonTruckSemi.setChecked(false);
                binding.radioButtonBus.setChecked(false);
                binding.radioButtonMiniBus.setChecked(false);
                binding.radioButtonMechanicalCar.setChecked(false);
                binding.radioButtonPickup.setChecked(true);
                binding.radioButtonSedan.setChecked(false);
            }
        });


        binding.radioButtonSedan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carType = 4;
                binding.radioButtonTruck.setChecked(false);
                binding.radioButtonTruckSemi.setChecked(false);
                binding.radioButtonBus.setChecked(false);
                binding.radioButtonMiniBus.setChecked(false);
                binding.radioButtonMechanicalCar.setChecked(false);
                binding.radioButtonPickup.setChecked(false);
                binding.radioButtonSedan.setChecked(true);
            }
        });


        binding.selectedPlateType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.irPlateRadioButton:
                        selectPlateType = 0;

                        binding.linearLayoutPlate1.setVisibility(View.VISIBLE);
                        binding.linearLayoutPlate2.setVisibility(View.GONE);
                        break;

                    case R.id.temporaryPlateRadioButton:
                        selectPlateType = 1;
                        binding.linearLayoutPlate2.setVisibility(View.VISIBLE);
                        binding.linearLayoutPlate1.setVisibility(View.GONE);
                        break;
                }
            }
        });


        binding.selectedEnterTypeThree.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioButtonRepair:
                        typePrc = 0;
                        break;

                    case R.id.radioButtonGuest:
                        typePrc = 1;
                        break;

                    case R.id.radioButtonCompany:
                        typePrc = 2;
                        break;

                    case R.id.radioButtonSecurityCar:
                        typePrc = 3;
                        break;
                }
            }
        });
        typePrc = 1;


        binding.imgTakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPreview(null);
            }
        });

        binding.imgTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickCapture();
            }
        });


        binding.btnConfirmTwo.setVisibility(View.GONE);
        binding.btnCancel.setVisibility(View.GONE);

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjActionEn = 0;
                saveDailyEvent(false,false);
            }
        });

        binding.btnConfirmTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjActionEn = 9;

                if (typeGetCar == 0){
                    if (filePath == null){
                        Toast.makeText(getActivity(), "افزودن فایل پیوست الزامیست", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                saveDailyEvent(true,false);
            }
        });

    /*    binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjActionEn = 1;
                saveDailyEvent();
            }
        });*/

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjActionEn = 10;
                saveDailyEvent(true,true);
            }
        });

        binding.btnExit.setVisibility(View.GONE);
        binding.btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjActionEn = 0;
                eventTypeEn = 16;
                dailEvn_id = 0;
                saveDailyEvent(false,false);
            }
        });


        if (typeGetCar == 1){
            ObjActionEn = 0;
            binding.txtOne.setText(GlobalValue.txtOne);
            binding.txtTwo.setText(GlobalValue.txtTwo);
            binding.txtThree.setText(GlobalValue.farsiWord);
            binding.txtFour.setText(GlobalValue.txtFour);
            binding.txtOne.setEnabled(false);
            binding.txtTwo.setEnabled(false);
            binding.txtThree.setEnabled(false);
            binding.txtFour.setEnabled(false);
            saveDailyEvent(false,false);
        }

        binding.edtDescriptionTwo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    //hideKeyboard(v);
                }
            }
        });

        binding.txtFour.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    //hideKeyboard(v);
                }
            }
        });

        binding.txtTwo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    //hideKeyboard(v);
                }
            }
        });

        binding.txtOne.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    //hideKeyboard(v);
                }
            }
        });

        binding.txtOneTempo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    //hideKeyboard(v);
                }
            }
        });

        binding.txtTwoTempo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    //hideKeyboard(v);
                }
            }
        });

        binding.txtThreeTempo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    //hideKeyboard(v);
                }
            }
        });

        binding.txtFourTempo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    //hideKeyboard(v);
                }
            }
        });

        return binding.getRoot();
    }

    public void autoSelectText(){

        binding.txtFour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (typeGetCar == 0 && binding.txtFour.getText().length() == 2) {
                    binding.txtThree.performClick();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.txtTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.txtTwo.getText().length() == 3) {
                    binding.txtOne.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.txtThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (typeGetCar == 0){
                    showDialogWords();
                }

            }
        });


        binding.txtFourTempo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (typeGetCar == 0 && binding.txtFourTempo.getText().length() == 2) {
                    binding.txtThreeTempo.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.txtThreeTempo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (typeGetCar == 0 && binding.txtThreeTempo.getText().length() == 3) {
                    binding.txtTwoTempo.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.txtTwoTempo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (typeGetCar == 0 && binding.txtTwoTempo.getText().length() == 2) {
                    binding.txtOneTempo.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void showDialogWords() {
        System.out.println("=-=-=typeGetCar-=-=-=" + typeGetCar);
        final String[] strName = new String[1];
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item);

        arrayAdapter.add("الف");
        arrayAdapter.add("ع");
        arrayAdapter.add("ی");
        arrayAdapter.add("ه");
        arrayAdapter.add("و");
        arrayAdapter.add("ن");
        arrayAdapter.add("م");
        arrayAdapter.add("ل");
        arrayAdapter.add("ط");
        arrayAdapter.add("ق");
        arrayAdapter.add("ص");
        arrayAdapter.add("س");
        arrayAdapter.add("د");
        arrayAdapter.add("ج");
        arrayAdapter.add("ب");
        arrayAdapter.add("پ");
        arrayAdapter.add("ت");
        arrayAdapter.add("ث");
        arrayAdapter.add("ز");
        arrayAdapter.add("ژ");
        arrayAdapter.add("ش");
        arrayAdapter.add("ف");
        arrayAdapter.add("ک");
        arrayAdapter.add("گ");


        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                strName[0] = arrayAdapter.getItem(which);
                System.out.println("strName=====" + strName[0]);
                GlobalValue.farsiWord = strName[0];
                binding.txtThree.setText(strName[0]);
                if (strName[0].contains("ا")){
                    GlobalValue.farsiWord = "الف";
                }

                binding.txtTwo.requestFocus();
            }
        });
        builderSingle.show();
    }

    private void startPreview(String pictureSizeStr) {
        binding.frameLayout.setVisibility(View.VISIBLE);
        if (camera != null) {
            camera.release();
            camera = null;
        }

        camera = getCameraInstance();

        if (camera == null) {
            Log.e("jisunLog", "Failed camera open");
        } else {

            if (preview != null) {
                binding.layoutPreview.removeView(preview);
                preview = null;
            }

            preview = new CameraPreview(getActivity(), camera);
            preview.setKeepScreenOn(true);

            // 저장 사진과 preview의 사이즈 등을 설정
            ImageManager.adjustCameraParameters(getActivity(), camera, pictureSizeStr);

            // preview가 보여지는 화면의 비율 설정
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            FrameLayout.LayoutParams p = (FrameLayout.LayoutParams) binding.layoutPreview.getLayoutParams();
            p.height = displayMetrics.widthPixels / 3 * 4;
            binding.layoutPreview.setLayoutParams(p);

            // preview를 layout에 추가하고, 날짜 영역을 화면 상위로 올림
            binding.layoutPreview.addView(preview);
            binding.txtDate.setText(new Date() + "--" + GlobalValue.lat + " " + "--" + GlobalValue.lang);
            binding.txtDate.bringToFront();
        }
    }

    public static int setCameraDisplayOrientation(Activity activity,
                                                  int cameraId, Camera camera) {
        Camera.CameraInfo info =
                new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }

        return result;
    }

    public Camera getCameraInstance() {
        Camera camera = null;

        int numCams = Camera.getNumberOfCameras();
        if (numCams > 0) {
            try {
                // Camera.CameraInfo.CAMERA_FACING_FRONT or Camera.CameraInfo.CAMERA_FACING_BACK
                int cameraFacing = Camera.CameraInfo.CAMERA_FACING_BACK;
                camera = Camera.open(cameraFacing);
                // camera orientation
                camera.setDisplayOrientation(setCameraDisplayOrientation(getActivity(), cameraFacing, camera));
                // get Camera parameters
                Camera.Parameters params = camera.getParameters();
                // picture image orientation
                params.setRotation(setCameraDisplayOrientation(getActivity(), cameraFacing, camera));
                camera.startPreview();

            } catch (RuntimeException ex) {
                // Toast.makeText(this, "camera_not_found ] " + ex.getMessage().toString(), Toast.LENGTH_LONG).show();
                // Log.d(Config.TAG, "camera_not_found ] " + ex.getMessage().toString());
            }
        }

        return camera;
    }

    public void onClickCapture() {
        camera.takePicture(null, null, new Camera.PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                new AsyncTask<byte[], Void, File>() {
                    public float textSize;
                    private CustomProgressDialog progressDialog;

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        progressDialog = new CustomProgressDialog(getActivity());
                        progressDialog.setCancelable(false);
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();

                        textSize = binding.txtDate.getTextSize();
                    }

                    @Override
                    protected File doInBackground(byte[]... params) {
                        byte[] data = params[0];
                        Bitmap bitmap = ImageManager.saveImageWithTimeStamp(getActivity(), data, 0, data.length, textSize, binding.txtDate.getText().toString());
                        File file = ImageManager.saveFile(bitmap);

                        getActivity().runOnUiThread(new Runnable()
                        {
                            public void run()
                            {

                                binding.imgPreview.setBackground(null);
                                binding.imgPreview.setImageBitmap(bitmap);
                                filePath = file.getPath();

                                System.out.println("filePath====" + filePath);
                                if (filePath != null && !filePath.trim().equals("")){
                                    new UploadFile().execute();
                                }
                            }
                        });



                        //saveAttachImageFile(ImageManager.getMediaFilePath(), (long) 106);
                        // refreshGallery(file);
                        return file;
                    }

                    @Override
                    protected void onPostExecute(File file) {
                        progressDialog.dismiss();

                        if (file == null) {
                            Log.e(Config.TAG, "Error creating media file, check storage permissions");
                        } else {
                           /* ImageDialog dialog = new ImageDialog(EditAdvertActivity.this, file);
                            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    EditAdvertActivity.this.camera.startPreview();
                                }
                            });
                            dialog.show();*/
                            //saveAttachImageFile(file.getPath());
                            binding.frameLayout.setVisibility(View.GONE);
                            //binding.scrollView.setVisibility(View.VISIBLE);
                            // progressDialogSentData.dismiss();
                        }

                        super.onPostExecute(file);
                    }
                }.execute(data);
            }
        });
    }


    private class UploadFile extends AsyncTask<String, String, String> {
        ProgressDialog dialog = ProgressDialog.show(getActivity(), "","لطفا منتظر بمانید..." , true);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();

            System.out.println("=====onPreExecute====");
        }

        @Override
        protected String doInBackground(String... urls) {
            //Copy you logic to calculate progress and call

            if (!filePath.equals("")) {
                saveAttachImageFile(filePath, (long) 47);
                apiServiceAsync.resumeAttachFile(user, getActivity(), attachFile, databaseViewModel);
            }

            System.out.println("=====doInBackground====");
            return null;
        }

        protected void onProgressUpdate(String... progress) {
            System.out.println("=====onProgressUpdate====");
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println("=====onPostExecute====");
            dialog.dismiss();
            Toast.makeText(getActivity(), "پیوست با موفقیت ثبت شد", Toast.LENGTH_LONG).show();

            // saveDailyEvent();
            //NavHostFragment.findNavController(EnterHandPlateFragment.this).navigate(R.id.homeFragment, null, navBuilder.build());

        }
    }

    public void saveAttachImageFile(String filePath, Long attachFileSettingId) {

        System.out.println("=========filePath==" + filePath);

        File file = new File(filePath);
        // String userFileName = file.getName();


        file = saveBitmapToFile(file);
        attachFile = new AttachFile();
        String userFileName = file.getName();
        System.out.println("=========userFileName==" + userFileName);

        attachFile.setAttachFileUserFileName(userFileName);
        attachFile.setSendingStatusDate(new Date());
        attachFile.setSendingStatusEn(SendingStatusEn.Pending.ordinal());
        attachFile.setEntityNameEn(EntityNameEn.DailyEvent.ordinal());
        attachFile.setServerAttachFileSettingId(attachFileSettingId);
        attachFile.setEntityId((long) dailEvn_id);
        attachFile.setServerEntityId((long) dailEvn_id);
        attachFile.setAttachFileLocalPath(filePath);
        databaseViewModel.insertAttachFileRepoVM(attachFile);

        long length = file.length();
        length = length / 1024;
        System.out.println("File Path : " + file.getPath() + ", File size : " + length + " KB");
        String filePostfix = userFileName.substring(userFileName.indexOf("."), userFileName.length());
        String path = Environment.getExternalStorageDirectory().toString() + Constant.DEFAULT_OUT_PUT_DIR + Constant.DEFAULT_IMG_OUT_PUT_DIR;
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }


        String newFilePath = path + "/" + attachFile.getId();

        try {
            InputStream inputStream = new FileInputStream(file);
            OutputStream outputStream = new FileOutputStream(newFilePath);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2; //try to decrease decoded image
            options.inPurgeable = true; //purgeable to disk
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream); //compressed bitmap to file
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
            }
            inputStream.close();
            outputStream.close();
            Long fileSize = new File(newFilePath).length();
            attachFile.setAttachFileSize(fileSize.intValue() / 1024);
            databaseViewModel.updateAttachFileByParam(attachFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //sendComplaintAttachment();

    }

    public File saveBitmapToFile(File file) {
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE = 50;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);

            return file;
        } catch (Exception e) {
            return null;
        }
    }

    public void saveDailyEvent(boolean confirm,boolean isCancel){

        if (plateNo == null){
            if (selectPlateType == 0){
                if (binding.txtFour.getText().length() != 2 || binding.txtFour.getText().toString().equals("")){
                    Toast toast = Toast.makeText(getActivity(), "شماره پلاک را کامل وارد کنید", Toast.LENGTH_LONG);
                    Util.showToast(toast, getActivity());
                    toast.show();
                    return;
                }

                if (binding.txtThree.getText().length() != 1 || binding.txtThree.getText().toString().equals("")){
                    Toast toast = Toast.makeText(getActivity(), "شماره پلاک را کامل وارد کنید", Toast.LENGTH_LONG);
                    Util.showToast(toast, getActivity());
                    toast.show();
                    return;
                }

                if (binding.txtTwo.getText().length() != 3 || binding.txtTwo.getText().toString().equals("")){
                    Toast toast = Toast.makeText(getActivity(), "شماره پلاک را کامل وارد کنید", Toast.LENGTH_LONG);
                    Util.showToast(toast, getActivity());
                    toast.show();
                    return;
                }

                if (binding.txtOne.getText().length() != 2 || binding.txtOne.getText().toString().equals("")){
                    Toast toast = Toast.makeText(getActivity(), "شماره پلاک را کامل وارد کنید", Toast.LENGTH_LONG);
                    Util.showToast(toast, getActivity());
                    toast.show();
                    return;
                }

                if (typePrc == -1){
                    Toast toast = Toast.makeText(getActivity(), "نوع ورود را مشخص کنید", Toast.LENGTH_LONG);
                    Util.showToast(toast, getActivity());
                    toast.show();
                    return;
                }

                if (carType == -1){
                    Toast toast = Toast.makeText(getActivity(), "نوع خودرو را مشخص کنید", Toast.LENGTH_LONG);
                    Util.showToast(toast, getActivity());
                    toast.show();
                    return;
                }

                plateNo = "1" + binding.txtFour.getText().toString() + Util.convertLetter(GlobalValue.farsiWord)
                        + binding.txtTwo.getText().toString() + "99990" + binding.txtOne.getText().toString();


            }else if (selectPlateType == 1){

                if (binding.txtFourTempo.getText().length() != 2 || binding.txtFourTempo.getText().toString().equals("")){
                    Toast toast = Toast.makeText(getActivity(), "شماره پلاک را کامل وارد کنید", Toast.LENGTH_LONG);
                    Util.showToast(toast, getActivity());
                    toast.show();
                    return;
                }

                if (binding.txtThreeTempo.getText().length() != 3 || binding.txtThreeTempo.getText().toString().equals("")){
                    Toast toast = Toast.makeText(getActivity(), "شماره پلاک را کامل وارد کنید", Toast.LENGTH_LONG);
                    Util.showToast(toast, getActivity());
                    toast.show();
                    return;
                }

                if (binding.txtTwoTempo.getText().length() != 2 || binding.txtTwoTempo.getText().toString().equals("")){
                    Toast toast = Toast.makeText(getActivity(), "شماره پلاک را کامل وارد کنید", Toast.LENGTH_LONG);
                    Util.showToast(toast, getActivity());
                    toast.show();
                    return;
                }

                if (binding.txtOneTempo.getText().length() != 2 || binding.txtOneTempo.getText().toString().equals("")){
                    Toast toast = Toast.makeText(getActivity(), "شماره پلاک را کامل وارد کنید", Toast.LENGTH_LONG);
                    Util.showToast(toast, getActivity());
                    toast.show();
                    return;
                }

                plateNo = "8" + binding.txtFourTempo.getText().toString() + "26" + binding.txtThreeTempo.getText().toString()
                        + binding.txtTwoTempo.getText().toString() + binding.txtOneTempo.getText().toString() + "011";
            }
        }


        System.out.println("typeGetCar====" + typeGetCar);
        System.out.println("plateNo====" + plateNo);
        if (typeGetCar == 1){
            carType = 0;
        }

        description = binding.edtDescriptionTwo.getText().toString();
        if (GlobalValue.companyCode == null){
            addCompanyDialog();
            return;
        }
        inputParam = GsonGenerator.saveDailyEvent(user.getUsername(), user.getBisPassword(), eventTypeEn,carType,typePrc,typeGetCar,ObjActionEn,
                carId,dailEvn_id,plateNo,description,parentId,GlobalValue.companyCode);

        ProgressDialog dialog = ProgressDialog.show(getActivity(), "","لطفا منتظر بمانید..." , true);

        viewModel.dailyEvent_MngAction(inputParam).subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DailyEventRespons>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull DailyEventRespons dailyEventRespons) {

                        dailyEventResponsCopy = dailyEventRespons;

                        if (dailyEventRespons.result != null){
                            if (dailyEventRespons.result.dailyEvent != null) {
                                //System.out.println("entityStrForAttach=====" + dailyEventRespons.result.dailyEvent.entityStrForAttach);
                                //System.out.println("eventTypeEn=====" + dailyEventRespons.result.dailyEvent.event.eventTypeEn);
                                //System.out.println("processStatus=====" + dailyEventRespons.result.dailyEvent.processStatus);

                                if (getActivity() != null) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        public void run() {
                                            binding.txtCarIsNull.setVisibility(View.INVISIBLE);
                                            dialog.dismiss();

                                            dailEvn_id = dailyEventRespons.result.dailyEvent.id;

                                            if (eventTypeEn == 16){
                                                NavHostFragment.findNavController(EnterHandPlateFragment.this).navigateUp();
                                                Toast.makeText(getActivity(), " درخواست با شناسه کار " + dailEvn_id +  " ثبت شد ", Toast.LENGTH_LONG).show();
                                                GlobalValue.ManageEEFragment = "ManageEEFragment";
                                                return;
                                            }

                                            if (dailyEventRespons.result.dailyEvent.car != null){
                                                binding.carTypeLayout.setVisibility(View.GONE);
                                                binding.carInfoLayout.setVisibility(View.VISIBLE);
                                                binding.txtCarIsNull.setVisibility(View.INVISIBLE);

                                                if (dailyEventRespons.result.dailyEvent.car.plateText != null){
                                                    binding.txtPlate.setText(" شماره پلاک : " + dailyEventRespons.result.dailyEvent.car.plateText);
                                                }else {
                                                    binding.txtPlate.setVisibility(View.GONE);
                                                }

                                                if (dailyEventRespons.result.dailyEvent.car.chassis != null){
                                                    binding.txtChassiss.setText(" شماره شاسی : " + dailyEventRespons.result.dailyEvent.car.chassis);
                                                }else {
                                                    binding.txtChassiss.setVisibility(View.GONE);
                                                }

                                                if (dailyEventRespons.result.dailyEvent.car.color != null){
                                                    binding.txtColor.setText(" رنگ خودرو : " + dailyEventRespons.result.dailyEvent.car.color.name);
                                                }else {
                                                    binding.txtColor.setVisibility(View.GONE);
                                                }

                                                if (dailyEventRespons.result.dailyEvent.car.seProModel != null){
                                                    binding.txtCarType.setText(" نوع خودرو : " + dailyEventRespons.result.dailyEvent.car.seProModel.proModelGroup.name);
                                                }else {
                                                    binding.txtCarType.setVisibility(View.GONE);
                                                }

                                                if (dailyEventRespons.result.dailyEvent.car.seLicPro != null){
                                                    try {
                                                        Date date = sdf.parse(dailyEventRespons.result.dailyEvent.car.seLicPro.endDatePlan);
                                                        Calendar calendar = Calendar.getInstance();
                                                        calendar.setTime(date);
                                                        JalaliCalendarUtil jalaliCalendarUtil = new JalaliCalendarUtil(calendar);
                                                        binding.txtLice.setText(" گارانتی : " + dailyEventRespons.result.dailyEvent.car.seLicPro.licTypeEn_text + " " +
                                                                jalaliCalendarUtil.getIranianYear() + "/" + jalaliCalendarUtil.getIranianMonth() + "/" + jalaliCalendarUtil.getIranianDay());
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }


                                                }else {
                                                    binding.txtLice.setVisibility(View.GONE);
                                                }
                                            }else {
                                                if (typeGetCar == 0){
                                                    binding.carTypeLayout.setVisibility(View.VISIBLE);
                                                    //binding.txtCarIsNull.setVisibility(View.VISIBLE);
                                                }

                                            }

                                            if (dailyEventRespons.result.dailyEvent.processStatus != null && dailyEventRespons.result.dailyEvent.processStatus == 0){

                                                binding.btnConfirmTwo.setVisibility(View.VISIBLE);
                                                binding.btnCancel.setVisibility(View.VISIBLE);
                                                if (typeGetCar == 0){
                                                    binding.takePhotoLayout.setVisibility(View.VISIBLE);
                                                }

                                                binding.btnSave.setVisibility(View.GONE);
                                                binding.carEnterInfoLayout.setVisibility(View.GONE);
                                                System.out.println("dailEvn_id=====" + dailEvn_id);
                                                Toast.makeText(getActivity(), " درخواست با شناسه کار " + dailEvn_id +  " ثبت شد ", Toast.LENGTH_LONG).show();

                                                binding.txtId.setVisibility(View.VISIBLE);
                                                binding.txtDateTime.setVisibility(View.VISIBLE);

                                                binding.txtId.setText(" درخواست با شناسه کار " + dailEvn_id + " ثبت شد ");
                                                binding.txtDateTime.setText(dailyEventRespons.result.dailyEvent.entityStrForAttach);


                                                if (confirm){
                                                    Toast.makeText(getActivity(), " درخواست با شناسه کار " + dailEvn_id +  " ثبت شد ", Toast.LENGTH_LONG).show();
                                                    NavHostFragment.findNavController(EnterHandPlateFragment.this).navigateUp();
                                                    GlobalValue.ManageEEFragment = "ManageEEFragment";
                                                }else {
                                                    showDialog(dailyEventRespons.result.dailyEvent.entityStrForAttach);
                                                }
                                            }else  if (dailyEventRespons.result.dailyEvent.processStatus != null && dailyEventRespons.result.dailyEvent.processStatus == 5){

                                                binding.cardEnterType.setVisibility(View.GONE);
                                                binding.cardEnterTypeResult.setVisibility(View.VISIBLE);

                                               // binding.txtCarIsNull.setVisibility(View.VISIBLE);
                                               // binding.txtCarIsNull.setText(dailyEventRespons.result.dailyEvent.event.name +" با پلاگ "+dailyEventRespons.result.dailyEvent.car.plateText+ " با شناسه :"+ " " + dailyEventRespons.result.dailyEvent.event.id);

                                                binding.btnExit.setVisibility(View.VISIBLE);
                                                binding.carEnterInfoLayout.setVisibility(View.VISIBLE);
                                                binding.btnSave.setVisibility(View.GONE);
                                                binding.txtEventName.setText(" فرایند : " + dailyEventRespons.result.dailyEvent.event.name + " با شناسه :"+ " " + dailyEventRespons.result.dailyEvent.event.id);
                                                //binding.txtEventId.setText(" شناسه فرایند : " + dailyEventRespons.result.dailyEvent.event.id);

                                                Date startData = null;
                                                Date endData = null;
                                                try {
                                                    startData = sdf.parse(dailyEventRespons.result.dailyEvent.startTime);
                                                    Calendar calendar = Calendar.getInstance();
                                                    calendar.setTime(startData);
                                                    JalaliCalendarUtil jalaliCalendarUtil = new JalaliCalendarUtil(calendar);
                                                    binding.txtEventTime.setText( " زمان ورود :  " + jalaliCalendarUtil.getIranianYear() + "/" + jalaliCalendarUtil.getIranianMonth() + "/" + jalaliCalendarUtil.getIranianDay()
                                                            + " ساعت : " + jalaliCalendarUtil.getTime(calendar));

                                                    String format = sdf.format(new Date());
                                                    endData = sdf.parse(format);

                                                    long difference_In_Time = endData.getTime() - startData.getTime();

                                                    // Calculate time difference in
                                                    // seconds, minutes, hours, years,
                                                    // and days
                                                    long difference_In_Seconds
                                                            = (difference_In_Time
                                                            / 1000)
                                                            % 60;

                                                    long difference_In_Minutes
                                                            = (difference_In_Time
                                                            / (1000 * 60))
                                                            % 60;

                                                    long difference_In_Hours
                                                            = (difference_In_Time
                                                            / (1000 * 60 * 60))
                                                            % 24;

                                                    long difference_In_Years
                                                            = (difference_In_Time
                                                            / (1000l * 60 * 60 * 24 * 365));

                                                    long difference_In_Days
                                                            = (difference_In_Time
                                                            / (1000 * 60 * 60 * 24))
                                                            % 365;


                                                    if (difference_In_Days == 0){
                                                        if (difference_In_Hours == 0){
                                                            binding.txtDuration.setText(difference_In_Minutes +" دقیقه "  );
                                                        }else {
                                                            binding.txtDuration.setText("مدت ورود : " + difference_In_Hours + " ساعت " +" و " + difference_In_Minutes +  " دقیقه ");
                                                        }

                                                    }else {
                                                        binding.txtDuration.setText("مدت ورود : " + difference_In_Days +" روز "  +" و "+  difference_In_Hours + " ساعت ");
                                                    }

                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }

                                                parentId = dailyEventRespons.result.dailyEvent.id;
                                                if (dailyEventRespons.result.dailyEvent.typePrcEn == 0){
                                                    binding.txtType.setText(" نوع ورود :  تعمیرات");
                                                }else  if (dailyEventRespons.result.dailyEvent.typePrcEn == 1){
                                                    binding.txtType.setText(" نوع ورود :  مهمان");
                                                }else if (dailyEventRespons.result.dailyEvent.typePrcEn == 2){
                                                    binding.txtType.setText(" نوع ورود :  خودرو امانی");
                                                }
                                                Toast.makeText(getActivity(), " درخواست با شناسه کار " + parentId +  " با موفقیت انجام شد ", Toast.LENGTH_LONG).show();

                                            } else if (dailyEventRespons.result.dailyEvent.objActionEnFP != null && dailyEventRespons.result.dailyEvent.objActionEnFP == 9){
                                                Toast.makeText(getActivity(), " درخواست با شناسه کار " + dailEvn_id +  " با موفقیت انجام شد ", Toast.LENGTH_LONG).show();
                                                NavHostFragment.findNavController(EnterHandPlateFragment.this).navigate(R.id.manageEEFragment, null, navBuilder.build());
                                                GlobalValue.ManageEEFragment = "ManageEEFragment";

                                            }
                                            else if (dailyEventRespons.result.dailyEvent.objActionEnFP != null && dailyEventRespons.result.dailyEvent.objActionEnFP == 10){
                                                System.out.println("=====objActionEnFP=====" + dailyEventRespons.result.dailyEvent.objActionEnFP);
                                                Toast.makeText(getActivity(), " درخواست با شناسه کار " + dailEvn_id +  " لغو شد ", Toast.LENGTH_LONG).show();
                                                NavHostFragment.findNavController(EnterHandPlateFragment.this).navigateUp();
                                                GlobalValue.ManageEEFragment = "ManageEEFragment";
                                            }/*else if (dailyEventRespons.result.dailyEvent.processStatus != null && (dailyEventRespons.result.dailyEvent.processStatus == 6 || dailyEventRespons.result.dailyEvent.processStatus == 1)){

                                                if (isCancel){
                                                    System.out.println("=====objActionEnFP=====" + dailyEventRespons.result.dailyEvent.objActionEnFP);
                                                    Toast.makeText(getActivity(), " درخواست با شناسه کار " + dailEvn_id +  " لغو شد ", Toast.LENGTH_LONG).show();
                                                    NavHostFragment.findNavController(EnterHandPlateFragment.this).navigate(R.id.manageEEFragment, null, navBuilder.build());
                                                    GlobalValue.ManageEEFragment = "ManageEEFragment";
                                                }
                                            }*/
                                        }
                                    });
                                }
                            }
                        }else if (dailyEventRespons.ERROR != null){

                            if (getActivity() != null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        /*Toast toast = Toast.makeText(getActivity(), dailyEventRespons.ERROR, Toast.LENGTH_LONG);
                                        Util.showToast(toast, getActivity());
                                        toast.show();*/
                                        CuteToast.ct(getActivity(), dailyEventRespons.ERROR, CuteToast.LENGTH_LONG, CuteToast.ERROR, R.drawable.sinoempty).show();
                                        dialog.dismiss();
                                    }
                                });
                            }

                        }


                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {

                                }
                            });
                        }

                    }

                    @Override
                    public void onComplete() {
                      /*  if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    dialog.dismiss();
                                    if (dailyEventResponsCopy.ERROR != null){
                                        Toast toast = Toast.makeText(getActivity(), dailyEventResponsCopy.ERROR, Toast.LENGTH_LONG);
                                        Util.showToast(toast, getActivity());
                                        toast.show();
                                    }
                                }
                            });
                        }*/
                    }
                });
    }

    public void showDialog(String value) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_enter_exit);
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.show();

        TextView txt_event = dialog.findViewById(R.id.txt_event);

        txt_event.setText(value);

        dialog.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void addCompanyDialog() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_company);
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.show();

        EditText txt_companyCode = dialog.findViewById(R.id.txt_companyCode);

        dialog.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txt_companyCode.getText().toString().trim().isEmpty()) {
                    Toast toast = Toast.makeText(getActivity(), "کد را وارد کنید", Toast.LENGTH_LONG);
                    Util.showToast(toast, getActivity());
                    toast.show();
                }
                GlobalValue.companyCode = txt_companyCode.getText().toString();
                Config.putSharedPreference(getActivity(), Constant.COMPANY_CODE, txt_companyCode.getText().toString());
            }
        });
    }

}