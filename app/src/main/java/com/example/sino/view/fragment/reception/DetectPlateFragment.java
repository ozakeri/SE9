package com.example.sino.view.fragment.reception;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sino.BuildConfig;
import com.example.sino.R;
import com.example.sino.SinoApplication;
import com.example.sino.model.carinfo.Car;
import com.example.sino.model.carinfo.SuccessCarInfoBean;
import com.example.sino.model.db.User;
import com.example.sino.model.db.UserPermission;
import com.example.sino.model.form.FormRequentBean;
import com.example.sino.utils.Config;
import com.example.sino.utils.GlobalValue;
import com.example.sino.utils.GsonGenerator;
import com.example.sino.utils.common.Util;
import com.example.sino.utils.services.LocationUpdatesService;
import com.example.sino.view.adapter.car.CarListAdapter;
import com.example.sino.viewmodel.MainViewModel;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;
import uk.co.deanwild.materialshowcaseview.ShowcaseTooltip;


@AndroidEntryPoint
public class DetectPlateFragment extends Fragment {

    private static final String TAG = "ANPR_Demo";
    private TextView txt_three;
    private TextView txt_two;
    private TextView txt_four;
    private String plateText, chassis;
    private Bitmap bitmap;
    private String plateText_str, vinNo_str, chassis_str;
    private MainViewModel viewModel;
    private String inputParam = "";
    private User user;
    private SuccessCarInfoBean successCarInfoBeanResult;
    private CircularProgressView circularProgressView;
    private CircularProgressView circularProgressViewFragment;
    private CompositeDisposable compositeDisposable;
    private Car car;
    private NavOptions.Builder navBuilder;
    private RadioGroup radioGroup;
    private int selectedType = 1;
    private List<Car> carList;
    private SuccessCarInfoBean successCarInfoBeanCopy;
    private CarListAdapter adapter;
    private String companyCode;
    private SharedPreferences sharedPreferences;
    private Boolean mRequestingLocationUpdates;

    //***********************************location***********
    // location last updated time
    private String mLastUpdateTime;

    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    private static final int REQUEST_CHECK_SETTINGS = 100;

    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private static final String SHOWCASE_ID = "sequence example";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detect_plate, container, false);

        initLocation();
        startLocationButtonClick();
        navBuilder = new NavOptions.Builder();
        navBuilder.setEnterAnim(R.anim.slide_from_left).setExitAnim(R.anim.slide_out_right).setPopEnterAnim(R.anim.slide_from_right).setPopExitAnim(R.anim.slide_out_left);
        // Asking for permissions
        String[] accessPermissions = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE
        };

        ArrayList<String> permissionNeededList = new ArrayList<String>();
        for (String access : accessPermissions) {
            int curPermission = ActivityCompat.checkSelfPermission(getActivity(), access);
            if (curPermission != PackageManager.PERMISSION_GRANTED) {
                permissionNeededList.add(access);
            }
        }

        if (permissionNeededList.size() > 0) {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    permissionNeededList.toArray(new String[permissionNeededList.size()]),
                    1);
        }

        MaterialShowcaseView.resetSingleUse(getActivity(), SHOWCASE_ID);
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500);
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(getActivity(), SHOWCASE_ID);
        ShowcaseTooltip toolTip1 = ShowcaseTooltip.build(getActivity())
                .corner(30)
                .textColor(Color.parseColor("#007686"))
                .text("خواندن پلاک بصورت اتوماتیک");

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder((Activity) getActivity())
                        .setTarget(view.findViewById(R.id.btn_camera_demo))
                        .setToolTip(toolTip1)
                        .setTooltipMargin(30)
                        .setShapePadding(50)
                        .withRectangleShape()
                        .setDismissOnTouch(true)
                        .setMaskColour(getActivity().getColor(R.color.transparentBlack))
                        .build()
        );

        ShowcaseTooltip toolTip2 = ShowcaseTooltip.build(getActivity())
                .corner(30)
                .textColor(Color.parseColor("#007686"))
                .text("وارد کردن اطلاعات بصورت دستی");

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder((Activity) getActivity())
                        .setTarget(view.findViewById(R.id.btn_custom))
                        .setToolTip(toolTip2)
                        .setTooltipMargin(30)
                        .withRectangleShape()
                        .setShapePadding(50)
                        .setDismissOnTouch(true)
                        .setMaskColour(getActivity().getColor(R.color.transparentBlack))
                        .build()
        );

        ShowcaseTooltip toolTip3 = ShowcaseTooltip.build(getActivity())
                .corner(30)
                .textColor(Color.parseColor("#007686"))
                .text("لیست خودروهای در حال پذیرش");

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder((Activity) getActivity())
                        .setTarget(view.findViewById(R.id.btn_list))
                        .setToolTip(toolTip3)
                        .setTooltipMargin(30)
                        .withRectangleShape()
                        .setShapePadding(50)
                        .setDismissOnTouch(true)
                        .setMaskColour(getActivity().getColor(R.color.transparentBlack))
                        .build()
        );

        sequence.start();

        return view;
    }

    private void initView(View view) {

        user = SinoApplication.getInstance().getCurrentUser();
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        Button btnActivate = (Button) view.findViewById(R.id.btn_activate);
        btnActivate.setOnClickListener(onActivateClicked);
        //btnActivate.setVisibility(View.GONE);
        getPermissionList(user, btnActivate);

        sharedPreferences = getActivity().getSharedPreferences("companyCode",MODE_PRIVATE);

        if (user.getCompanyType() != null){
            companyCode = user.getCompanyType();
        }else {
            companyCode = sharedPreferences.getString("companyCode", "");
            if (companyCode.equals("")) {
                //addCompanyDialog();
            }
        }
        System.out.println("=====companyCode===" + companyCode);

        compositeDisposable = new CompositeDisposable();

        TextView btnCameraDemo = view.findViewById(R.id.btn_camera_demo);
        btnCameraDemo.setOnClickListener(onCameraDemoClicked);

        TextView btn_custom = view.findViewById(R.id.btn_custom);
        btn_custom.setOnClickListener(openDialog);

        TextView btn_list = view.findViewById(R.id.btn_list);
        btn_list.setOnClickListener(onBtnListClicked);

        circularProgressViewFragment = view.findViewById(R.id.waitProgress);

        GlobalValue.plateNo = null;
    }

    private View.OnClickListener onActivateClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            NavHostFragment.findNavController(DetectPlateFragment.this).navigate(R.id.saptaActivity, null, navBuilder.build());
        }
    };

    private View.OnClickListener onBtnListClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            NavHostFragment.findNavController(DetectPlateFragment.this).navigate(R.id.proSerListFragment, null, navBuilder.build());
        }
    };

    private View.OnClickListener onCameraDemoClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Bundle bundle = new Bundle();
            bundle.putInt("requestKey", 2);
            NavHostFragment.findNavController(DetectPlateFragment.this).navigate(R.id.captureActivity, bundle, navBuilder.build());
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }


    public void openDialogCarList(List<Car> carList, RecyclerView recyclerView, Dialog dialog) {
        adapter = new CarListAdapter(carList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new CarListAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Car car = carList.get(position);
                //getProServiceByCarId(car.id);
                Bundle bundle = new Bundle();
                bundle.putParcelable("car", car);
                NavHostFragment.findNavController(DetectPlateFragment.this).navigate(R.id.carDetailFragment, bundle, navBuilder.build());
                dialog.dismiss();
            }
        });
    }

    private View.OnClickListener openDialog = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
           // NavHostFragment.findNavController(DetectPlateFragment.this).navigate(R.id.recognizePlateFragment, null, null);

           /* sharedPreferences = getActivity().getSharedPreferences("companyCode",MODE_PRIVATE);
            companyCode = sharedPreferences.getString("companyCode", "");
            System.out.println("companyCode===" + companyCode);
            GlobalValue.companyCode = companyCode;
            if (companyCode.equals("")) {
                addCompanyDialog();
                return;
            }*/

            //GlobalValue.companyCode = "1111";

            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

            Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_enter_car_plate);
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setAttributes(layoutParams);
            dialog.show();

            EditText txt_shasi = dialog.findViewById(R.id.txt_shasi);
            EditText txt_plate = dialog.findViewById(R.id.txt_plate);

            txt_shasi.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                       hideKeyboard(v);
                    }
                }
            });
            txt_plate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        hideKeyboard(v);
                    }
                }
            });

            TextView txt_representation = dialog.findViewById(R.id.txt_representation);
            LinearLayout linearLayout = dialog.findViewById(R.id.linearLayout);
            LinearLayout cardLinearLayout = dialog.findViewById(R.id.cardLinearLayout);
            RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView);

            EditText txt_one = dialog.findViewById(R.id.txt_one);
            //EditText txt_two = dialog.findViewById(R.id.txt_two);
            circularProgressView = dialog.findViewById(R.id.waitProgress);
            txt_three = dialog.findViewById(R.id.txt_three);
            txt_two = dialog.findViewById(R.id.txt_two);
            txt_four = dialog.findViewById(R.id.txt_four);
            TextView btn_cancel = dialog.findViewById(R.id.btn_cancel);
            TextView btn_confirm = dialog.findViewById(R.id.btn_confirm);

            txt_one.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                       hideKeyboard(v);
                    }
                }
            });

            txt_two.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                       // hideKeyboard(v);
                    }
                }
            });

            txt_three.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                       // hideKeyboard(v);
                    }
                }
            });

            txt_four.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                       // hideKeyboard(v);
                    }
                }
            });

           // txt_representation.setText("  نمایندگی  " + GlobalValue.companyCode);

            radioGroup = dialog.findViewById(R.id.selected_type);
            cardLinearLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    switch (i) {
                        case R.id.radioButtonChassis:
                            txt_shasi.setVisibility(View.VISIBLE);
                            txt_plate.setVisibility(View.GONE);
                            linearLayout.setVisibility(View.GONE);
                            selectedType = 1;
                            break;

                        case R.id.radioButtonPlateNumber:
                            txt_shasi.setVisibility(View.GONE);
                            txt_plate.setVisibility(View.VISIBLE);
                            linearLayout.setVisibility(View.GONE);
                            selectedType = 2;
                            break;

                        case R.id.radioButtonPlate:
                            txt_shasi.setVisibility(View.GONE);
                            txt_plate.setVisibility(View.GONE);
                            linearLayout.setVisibility(View.VISIBLE);
                            selectedType = 3;
                            break;
                    }
                }
            });

            txt_four.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (txt_four.getText().length() == 2) {
                        txt_three.performClick();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            txt_four.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (txt_four.getText().length() == 2) {
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

            txt_two.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (txt_two.getText().length() == 3) {
                        txt_one.requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            txt_one.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (txt_one.getText().length() == 2) {
                        hideKeyboard(view);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

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

                  /*  if (selectedType == 1) {
                        if (txt_plate.getText().toString().trim().length() < 6 && txt_plate.getText().toString().trim().length() > 18) {
                            Toast toast = Toast.makeText(getActivity(), "ارقام شماره شاسی را صحیح وارد کنید", Toast.LENGTH_LONG);
                            Util.showToast(toast, getActivity());
                            toast.show();
                            return;
                        }
                    }*/
                    chassis = Util.farsiNumberReplacement(txt_shasi.getText().toString());
                    if (selectedType == 3) {
                        plateText = "1" + txt_four.getText().toString() + Util.convertLetter(txt_three.getText().toString()) + txt_two.getText().toString() + "99990" + txt_one.getText().toString();
                    } else {
                        plateText = txt_plate.getText().toString();
                    }
                    System.out.println("plateText1======" + plateText);


                    inputParam = GsonGenerator.getCarInfo(user.getUsername(), user.getBisPassword(), plateText, chassis, selectedType, Integer.parseInt(GlobalValue.companyCode));
                    Util.showProgress(circularProgressView);
                    viewModel.getCarInfo_ProSrv(inputParam).subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
                            .subscribe(new io.reactivex.rxjava3.core.Observer<SuccessCarInfoBean>() {
                                @Override
                                public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                                }

                                @Override
                                public void onNext(@io.reactivex.rxjava3.annotations.NonNull SuccessCarInfoBean successCarInfoBean) {

                                    successCarInfoBeanCopy = successCarInfoBean;
                                    if (getActivity() != null){
                                        getActivity().runOnUiThread(new Runnable() {

                                            @Override
                                            public void run() {

                                                if (successCarInfoBeanCopy.success != null) {

                                                    carList = successCarInfoBeanCopy.result.carList;

                                                    if (carList.size() == 1) {
                                                        car = carList.get(0);
                                                        dialog.dismiss();

                                                        Bundle bundle = new Bundle();
                                                        bundle.putParcelable("car", car);
                                                        NavHostFragment.findNavController(DetectPlateFragment.this).navigate(R.id.carDetailFragment, bundle, navBuilder.build());

                                                    } else {

                                                        cardLinearLayout.setVisibility(View.GONE);
                                                        recyclerView.setVisibility(View.VISIBLE);


                                                        openDialogCarList(carList, recyclerView, dialog);
                                                    }

                                                } else {

                                                    Toast toast = Toast.makeText(getActivity(), "خودرویی یافت نشد", Toast.LENGTH_LONG);
                                                    Util.showToast(toast, getActivity());
                                                    toast.show();

                                                    //dialog.dismiss();
                                                    //NavHostFragment.findNavController(DetectPlateFragment.this).navigate(R.id.changePlateFragment, null, navBuilder.build());
                                                }

                                                Util.hideProgress(circularProgressView);

                                            }
                                        });
                                    }


                                }

                                @Override
                                public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                   try {
                                       Util.hideProgress(circularProgressView);
                                       Toast toast = Toast.makeText(getActivity(), "خودرویی یافت نشد", Toast.LENGTH_LONG);
                                       Util.showToast(toast, getActivity());
                                       toast.show();
                                   }catch (Exception exception){
                                       System.out.println(exception.getLocalizedMessage());
                                   }
                                }

                                @Override
                                public void onComplete() {

                                }
                            });


                }
            });
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("======onActivityResult======");
        System.out.println("======requestCode======" + requestCode);
        System.out.println("======resultCode======" + resultCode);
        System.out.println("======data======" + data);

        if (requestCode == 2) {
            if (data != null) {
                plateText = data.getStringExtra("plateText");
                chassis = data.getStringExtra("chassis");

                System.out.println("plateText2======" + plateText);

            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    @Override
    public void onResume() {
        super.onResume();

        // Resuming location updates depending on button state and
        // allowed permissions
        if (mRequestingLocationUpdates && checkPermissions()) {
            startLocationUpdates();
        }

        updateLocationUI();

        System.out.println("plateNo=====" + GlobalValue.plateNo);
        System.out.println("onResume=====");
        if (GlobalValue.plateNo != null) {
            Util.showProgress(circularProgressViewFragment);
            inputParam = GsonGenerator.getCarInfo(user.getUsername(), user.getBisPassword(), GlobalValue.plateNo, null, 3,1111);
            viewModel.getCarInfo_ProSrv(inputParam).subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(new io.reactivex.rxjava3.core.Observer<SuccessCarInfoBean>() {
                        @Override
                        public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@io.reactivex.rxjava3.annotations.NonNull SuccessCarInfoBean successCarInfoBean) {

                            successCarInfoBeanCopy = successCarInfoBean;
                            if (getActivity() != null){
                                getActivity().runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        GlobalValue.plateNo = null;
                                        Util.hideProgress(circularProgressViewFragment);
                                        if (successCarInfoBeanCopy.success != null) {

                                            carList = successCarInfoBeanCopy.result.carList;
                                            car = carList.get(0);
                                            Bundle bundle = new Bundle();
                                            bundle.putParcelable("car", car);
                                            NavHostFragment.findNavController(DetectPlateFragment.this).navigate(R.id.carDetailFragment, bundle, navBuilder.build());

                                        }else {
                                            Toast toast = Toast.makeText(getActivity(), "خودرویی یافت نشد", Toast.LENGTH_LONG);
                                            Util.showToast(toast, getActivity());
                                            toast.show();
                                        }

                                        if (successCarInfoBeanCopy.error != null) {
                                            Toast toast = Toast.makeText(getActivity(), "خودرویی یافت نشد", Toast.LENGTH_LONG);
                                            Util.showToast(toast, getActivity());
                                            toast.show();
                                        }

                                    }
                                });
                            }


                        }

                        @Override
                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                            if (getActivity() != null){
                                getActivity().runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        GlobalValue.plateNo = null;
                                        Util.hideProgress(circularProgressViewFragment);
                                        Toast toast = Toast.makeText(getActivity(), "خودرویی یافت نشد", Toast.LENGTH_LONG);
                                        Util.showToast(toast, getActivity());
                                        toast.show();
                                    }
                                });
                            }

                        }

                        @Override
                        public void onComplete() {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    GlobalValue.plateNo = null;
                                    Util.hideProgress(circularProgressViewFragment);
                                }
                            });
                        }
                    });
        }
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
                    return;

                }

                //GlobalValue.companyCode = "1111";
                dialog.dismiss();
                getCompanyInfo(txt_companyCode.getText().toString());



            }
        });
    }

    public void getLocation() {

        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                checkBackgroundLocation()) {
            final Intent intent = new Intent(getActivity(), LocationUpdatesService.class);
            getActivity().startService(intent);
            System.out.println("intent====" + intent.getData());
            System.out.println("intent====" + intent);

        } else {
            requsetPermission();
        }
    }

    private void requsetPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                requestPermissions(new String[]
                        {Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            } else {
                requestPermissions(new String[]
                        {Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

            }
        }
    }

    public boolean checkBackgroundLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    public void getCompanyInfo(String text) {
        inputParam = GsonGenerator.getCompanyInfo(user.getUsername(), user.getBisPassword(), text);
        viewModel.getCompanyInfo(inputParam).subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.rxjava3.core.Observer<SuccessCarInfoBean>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull SuccessCarInfoBean successCarInfoBean) {
                        if (successCarInfoBean.success != null) {
                            if (successCarInfoBean.result != null) {

                                System.out.println("code======" + successCarInfoBean.result.company.code);
                                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                myEdit.putString("companyCode", successCarInfoBean.result.company.code);
                                myEdit.commit();
                                GlobalValue.companyCode = successCarInfoBean.result.company.code;
                            }
                        }

                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void startLocationButtonClick() {
        // Requesting ACCESS_FINE_LOCATION using Dexter library
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mRequestingLocationUpdates = true;
                        startLocationUpdates();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            // open device settings when the permission is
                            // denied permanently
                            openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void initLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mSettingsClient = LocationServices.getSettingsClient(getActivity());

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

                updateLocationUI();
            }
        };

        mRequestingLocationUpdates = false;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void startLocationUpdates() {
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");

                       // Toast.makeText(getActivity(), "Started location updates!", Toast.LENGTH_SHORT).show();

                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                        updateLocationUI();
                    }
                })
                .addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);

                                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                        }

                        updateLocationUI();
                    }
                });
    }

    private void updateLocationUI() {
        if (mCurrentLocation != null) {
            System.out.println("Lat: " + mCurrentLocation.getLatitude() + ", " +
                    "Lng: " + mCurrentLocation.getLongitude());

            GlobalValue.lat = String.valueOf(mCurrentLocation.getLatitude());
            GlobalValue.lang = String.valueOf(mCurrentLocation.getLongitude());
        }
    }

    public void stopLocationUpdates() {
        // Removing location updates
        mFusedLocationClient
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //Toast.makeText(getActivity(), "Location updates stopped!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mRequestingLocationUpdates) {
            // pausing location updates
            stopLocationUpdates();
        }
    }


    public void showDialogWords() {
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
                txt_three.setText(strName[0]);
                if (strName[0].contains("ا")){
                    GlobalValue.farsiWord = "الف";
                }

               txt_two.requestFocus();
            }
        });
        builderSingle.show();
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void getPermissionList(User user,Button btnActivate) {
        Disposable disposable = viewModel.getUserPermissionCopy()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<UserPermission>>() {
                    @Override
                    public void accept(List<UserPermission> userPermissions) throws Throwable {

                        for (UserPermission p: userPermissions) {
                            if (p.getPermissionName().equals("ROLE_APP_ADMIN_VIEW")){
                               // btnActivate.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });

    }

}