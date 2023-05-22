package com.example.sino.view.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;

import com.developer.kalert.KAlertDialog;
import com.example.sino.BuildConfig;
import com.example.sino.R;
import com.example.sino.databinding.ActivityMainBinding;
import com.example.sino.enumtype.GeneralStatus;
import com.example.sino.model.db.User;
import com.example.sino.model.db.UserPermission;
import com.example.sino.utils.Config;
import com.example.sino.utils.GlobalValue;
import com.example.sino.utils.common.Constant;
import com.example.sino.utils.common.Util;
import com.example.sino.view.adapter.HomeAdapterRV;
import com.example.sino.viewmodel.DatabaseViewModel;
import com.example.sino.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;
    private DatabaseViewModel databaseViewModel;
    private User user;
    private ActivityMainBinding binding;
    public DrawerLayout drawerLayout;
    AppBarConfiguration appBarConfiguration;
    private CompositeDisposable compositeDisposable;
    private NavController navController;
    private KAlertDialog kAlertDialog;
    private ImageView img_header;
    private NavDestination navDestinationCopy;
    private String companyCode;
    private SharedPreferences sharedPreferences;
    private NavOptions.Builder navBuilder;
    private TextView txtName;
    private TextView txtCode;
    private NavHostFragment navHostFragment;
    private boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        drawerLayout = binding.drawerlayout;

        img_header = findViewById(R.id.img_header);
        txtName = findViewById(R.id.txt_header_name);
        txtCode = findViewById(R.id.txt_header_code);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        databaseViewModel = new ViewModelProvider(this).get(DatabaseViewModel.class);
        compositeDisposable = new CompositeDisposable();


        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        appBarConfiguration = new AppBarConfiguration.Builder(R.id.homeFragment, R.id.settingFragment)
                .setOpenableLayout(binding.drawerlayout)
                .build();
        NavigationUI.setupWithNavController(binding.navigationView, navController);

        navBuilder = new NavOptions.Builder();
        navBuilder.setEnterAnim(R.anim.slide_from_left).setExitAnim(R.anim.slide_out_right).setPopEnterAnim(R.anim.slide_from_right).setPopExitAnim(R.anim.slide_out_left);

        viewModel.getAllUser().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new io.reactivex.rxjava3.core.Observer<List<User>>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<User> users) {
                if (users != null && users.size() > 0) {
                    user = users.get(0);

                    txtName.setText(user.getName() + " " + user.getFamily());
                    if (user.getCompanyName() != null){
                        txtCode.setText(" ( کد )" + user.getCompanyName());
                    }else {
                        if (GlobalValue.companyCode != null){
                            txtCode.setText(GlobalValue.companyCode + " کد "+" نمایندگی ");
                        }

                    }

                    getPermissionList(user);

                }
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {

                System.out.println("navDestination====" + navDestination.getLabel());
                navDestinationCopy = navDestination;

                if (navDestination.getLabel().equals("fragment_home")) {
                    binding.include.imgBack.setVisibility(View.GONE);
                    binding.include.imgLogo.setVisibility(View.GONE);
                    binding.include.txtCode.setVisibility(View.GONE);
                    binding.include.txtEnterExit.setVisibility(View.GONE);
                    binding.include.imgUser.setVisibility(View.VISIBLE);
                    Util.presentShowcaseView(MainActivity.this, binding.include.imgMenu,"انتخاب منو");
                } else {
                    binding.include.imgBack.setVisibility(View.VISIBLE);
                    binding.include.imgLogo.setVisibility(View.VISIBLE);
                    binding.include.txtCode.setVisibility(View.VISIBLE);
                    binding.include.imgUser.setVisibility(View.GONE);
                }

                if (navDestination.getLabel().equals("EnterHandPlateFragment")) {
                    binding.include.txtEnterExit.setVisibility(View.VISIBLE);
                }

                if (navDestination.getLabel().equals("SplashFragment") || navDestination.getLabel().equals("FragmentRegistration")
                        || navDestination.getLabel().equals("FragmentActivation") || navDestination.getLabel().equals("fragment_create_password")) {
                    binding.include.appbarLayout.setVisibility(View.GONE);
                    drawerLayout.setEnabled(false);
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                } else {
                    binding.include.appbarLayout.setVisibility(View.VISIBLE);
                    drawerLayout.setEnabled(true);
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                }

            }
        });
        binding.include.imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });

        binding.include.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*if (navController.getCurrentDestination().getLabel().equals("fragment_pro_ser_list")) {
                    navController.navigateUp();
                    navController.navigate(R.id.detectPlateFragment, null, navBuilder.build());
                    return;
                }*/
                if (navController.getCurrentDestination().getLabel().equals("fragment_setting") || navController.getCurrentDestination().getLabel().equals("ManageEEFragment") || navController.getCurrentDestination().getLabel().equals("fragment_detect_plate")) {
                    navController.navigateUp();
                    navController.navigate(R.id.homeFragment, null, navBuilder.build());
                    return;
                    // navDestination.getLabel().equals("fragment_detect_plate")
                }else {
                    onBackPressed();
                }

                if (navController.getCurrentDestination().getLabel().equals("fragment_home")) {
                    finish();
                    return;
                }

            }
        });

    }

    public void getPermissionList(User user) {
        Disposable disposable = viewModel.getUserPermissionCopy()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<UserPermission>>() {
                    @Override
                    public void accept(List<UserPermission> userPermissions) throws Throwable {

                        System.out.println("Main=====" + userPermissions.size());
                        setupRecyclerView(userPermissions,user);
                    }
                });

        compositeDisposable.add(disposable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    @SuppressLint("SetTextI18n")
    private void setupRecyclerView(List<UserPermission> userPermissionList, User user) {


        String getSharedData = Config.getSharedPreferenceString(MainActivity.this, Constant.COMPANY_CODE);
        if (!getSharedData.isEmpty()){
            GlobalValue.companyCode = getSharedData;
            binding.include.txtCode.setText( " نمایندگی " + GlobalValue.companyCode );
        }

        binding.txtVersion.setText(" شماره ورژن " + BuildConfig.VERSION_NAME);
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        binding.recyclerViewItemMenu.setLayoutManager(mLayoutManager);
        List<String> permissionList = new ArrayList<>();
        userPermissionList = viewModel.getUserPermission(user.getId());
        //permissionList.add("TechnicalExpert");
        //permissionList.add("Rescuer");
        //permissionList.add("Car");
        //permissionList.add("Inspection");
       // permissionList.add("Representation");
       // permissionList.add("Watcher");
       // permissionList.add("Messages");
       // permissionList.add("Forms");
       // permissionList.add("CheckList");
        for (UserPermission p:userPermissionList) {
            permissionList.add(p.getPermissionName());
        }

        //permissionList.add("ManageEnterExit");
        //permissionList.add("Reception");

        permissionList.add("Setting");
        permissionList.add("Exit");

        binding.recyclerViewItemMenu.setVisibility(View.VISIBLE);
        HomeAdapterRV adapterRV = new HomeAdapterRV(this,permissionList, GeneralStatus.IsMenu);
        binding.recyclerViewItemMenu.setAdapter(adapterRV);

        adapterRV.setOnItemClickListener(new HomeAdapterRV.ClickListener() {
            @Override
            public void onItemClick(String permissionName, View v) {



                switch (permissionName) {

                    case "ROLE_APP_GET_AGENT_CAR_VIEW_SEC":
                        navController.navigate(R.id.manageEEFragment, null, navBuilder.build());
                        binding.drawerlayout.closeDrawers();
                        break;

                        case "ROLE_APP_GET_AGENT_CAR_VIEW_RECP":
                        navController.navigate(R.id.detectPlateFragment, null, navBuilder.build());
                        binding.drawerlayout.closeDrawers();
                        break;

                    case "Setting":
                        navController.navigate(R.id.settingFragment, null, navBuilder.build());
                        binding.drawerlayout.closeDrawers();
                        break;

                    case "Exit":
                        binding.drawerlayout.closeDrawers();
                        kAlertDialog = new KAlertDialog(MainActivity.this, KAlertDialog.WARNING_TYPE, 0);
                        kAlertDialog.setTitleText("هشدار")
                                .setContentText("کلیه ی اطلاعات حذف خواهد شد.آیا ادامه می دهید؟")
                                .setConfirmText("بلی")
                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                    @Override
                                    public void onClick(KAlertDialog kAlertDialog) {
                                        Config.putSharedPreference(MainActivity.this, Constant.COMPANY_CODE, null);
                                        GlobalValue.companyCode = null;
                                        databaseViewModel.deleteAllData();
                                        navController.navigate(R.id.splashFragment, null, navBuilder.build());
                                        kAlertDialog.dismissWithAnimation();
                                    }
                                })
                                .setCancelText("خیر")
                                .confirmButtonColor(R.color.pino_two, MainActivity.this) // you can change the color of confirm button
                                .cancelButtonColor(R.color.pino_two, MainActivity.this) // you can change the color of cancel button
                                .show();
                        break;

                }
            }
        });

    }

    @Override
    public void onBackPressed() {


        if (navHostFragment.getNavController().getCurrentDestination().getLabel().equals("AddExpertDataFragment")) {
            navController.navigateUp();
            navController.navigate(R.id.recognizePlateFragment, null, navBuilder.build());
            return;
        }

        if (navHostFragment.getNavController().getCurrentDestination().getLabel().equals("TakePictureFragment")) {
            navController.navigateUp();
            navController.navigate(R.id.addExpertDataFragment, null, navBuilder.build());
            return;
        }

        if (navHostFragment.getNavController().getCurrentDestination().getLabel().equals("RecognizePlateFragment")) {
            navController.navigateUp();
            navController.navigate(R.id.proSerListFragment, null, navBuilder.build());
            return;
        }

        if (navHostFragment.getNavController().getCurrentDestination().getLabel().equals("fragment_pro_ser_list")) {
            navController.navigateUp();
            navController.navigate(R.id.detectPlateFragment, null, navBuilder.build());
            return;
        }

        if (navHostFragment.getNavController().getCurrentDestination().getLabel().equals("fragment_detect_plate")) {
            navController.navigateUp();
            navController.navigate(R.id.homeFragment, null, navBuilder.build());
            return;
        }

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {

                System.out.println("------navDestination====" + navDestination.getLabel());
                navDestinationCopy = navDestination;
                binding.include.txtCode.setVisibility(View.GONE);


                if (navDestination.getLabel().equals("fragment_home")) {
                    binding.include.imgBack.setVisibility(View.GONE);
                    binding.include.imgLogo.setVisibility(View.GONE);
                    binding.include.txtCode.setVisibility(View.GONE);
                    binding.include.imgUser.setVisibility(View.VISIBLE);
                }else {
                    binding.include.imgBack.setVisibility(View.VISIBLE);
                    binding.include.imgLogo.setVisibility(View.VISIBLE);
                    binding.include.txtCode.setVisibility(View.VISIBLE);
                    binding.include.imgUser.setVisibility(View.GONE);
                }

                if (navDestination.getLabel().equals("fragment_detect_plate") ||
                        navDestination.getLabel().equals("CarDetailFragment") ||
                        navDestination.getLabel().equals("AddCustomerDataFragment") ||
                        navDestination.getLabel().equals("AddExpertDataFragment") ||
                        navDestination.getLabel().equals("TakePictureFragment") ||
                        navDestination.getLabel().equals("RecognizePlateFragment")) {
                    sharedPreferences = getSharedPreferences("companyCode", MODE_PRIVATE);
                    companyCode = sharedPreferences.getString("companyCode", "");
                    binding.include.imgBack.setVisibility(View.VISIBLE);
                    binding.include.imgUser.setVisibility(View.GONE);
                    binding.include.txtCode.setVisibility(View.VISIBLE);
                }

                if (navDestination.getLabel().equals("SplashFragment") || navDestination.getLabel().equals("FragmentRegistration")
                        || navDestination.getLabel().equals("FragmentActivation") || navDestination.getLabel().equals("fragment_create_password")) {
                    binding.include.appbarLayout.setVisibility(View.GONE);
                    drawerLayout.setEnabled(false);
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                } else {
                    binding.include.appbarLayout.setVisibility(View.VISIBLE);
                    drawerLayout.setEnabled(true);
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                }
            }
        });

        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            binding.drawerlayout.closeDrawers();
        } else if (navDestinationCopy.getLabel().equals("fragment_home")) {
            if (doubleBackToExitPressedOnce) {
                finish();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
}