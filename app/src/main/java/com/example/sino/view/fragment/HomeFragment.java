package com.example.sino.view.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.sino.R;
import com.example.sino.SinoApplication;
import com.example.sino.databinding.FragmentHomeBinding;
import com.example.sino.enumtype.GeneralStatus;
import com.example.sino.model.SuccessPermissionBean;
import com.example.sino.model.db.User;
import com.example.sino.model.db.UserPermission;
import com.example.sino.utils.Config;
import com.example.sino.utils.GlobalValue;
import com.example.sino.utils.GsonGenerator;
import com.example.sino.utils.common.Constant;
import com.example.sino.utils.common.Util;
import com.example.sino.view.adapter.HomeAdapterRV;
import com.example.sino.viewmodel.MainViewModel;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


@AndroidEntryPoint
public class HomeFragment extends Fragment {

    private User user;
    private MainViewModel mainViewModel;
    private String inputParam = "";
    private CompositeDisposable compositeDisposable;
    private SuccessPermissionBean successPermissionBean;
    private List<String> permissionList = new ArrayList<>();
    private CircularProgressView progressView;
    private HomeAdapterRV adapterRV;
    private View view;
    private List<UserPermission> userPermissionList;
    private List<UserPermission> userPermissionListDb;
    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        if (binding == null) {

            binding= DataBindingUtil.inflate(inflater,R.layout.fragment_home, container, false);
            initVeiw(binding.getRoot());
            mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

            user = SinoApplication.getInstance().getCurrentUser();
            compositeDisposable = new CompositeDisposable();
            inputParam = GsonGenerator.getUserPermissionList(user.getUsername(), user.getBisPassword());

            binding.btnTryAgain.setVisibility(View.GONE);

            binding.txtUserName.setText(user.getName() + " " + user.getFamily());

            if (user != null && user.getCompanyCode() != null){
                GlobalValue.companyCode = String.valueOf(user.getCompanyCode());
                Config.putSharedPreference(getActivity(), Constant.COMPANY_CODE, String.valueOf(user.getCompanyCode()));
            }

            String getSharedData = Config.getSharedPreferenceString(getActivity(), Constant.COMPANY_CODE);
            if (user.getCompanyCode() == null && getSharedData.trim().isEmpty()){
               addCompanyDialog();
            }else {
                GlobalValue.companyCode = getSharedData;
            }


            if (user.getCompanyName() != null){
                binding.txtCompany.setText(user.getCompanyName() +" " + " کد نمایندگی : " + GlobalValue.companyCode);
            }else {
                binding.txtCompany.setText("نمایندگی "+" " + " کد : " + GlobalValue.companyCode);
            }


            callApiRequest();

            getDataFromServer();
        }

        return binding.getRoot();
    }

    private void setupRecyclerView(List<UserPermission> userPermissionList) {
        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1) {
            @Override
            protected boolean isLayoutRTL() {
                return true;
            }
        };
        binding.recyclerViewPermission.setLayoutManager(mLayoutManager);

        //userPermissionList = mainViewModel.getUserPermission(user.getId());

        for (UserPermission p:userPermissionList) {
            permissionList.add(p.getPermissionName());

            System.out.println("====p====" + p.getPermissionName());
        }

        //permissionList.add("TechnicalExpert");
        //permissionList.add("Rescuer");
        //permissionList.add("Car");
        //permissionList.add("Inspection");
       // permissionList.add("Representation");
        //permissionList.add("Watcher");
        //permissionList.add("Messages");
        //permissionList.add("Forms");
        //permissionList.add("CheckList");
        //permissionList.add("ManageEnterExit");
        //permissionList.add("Reception");
        permissionList.add("Setting");
        permissionList.add("Exit");

        binding.recyclerViewPermission.setVisibility(View.VISIBLE);
        adapterRV = new HomeAdapterRV(permissionList, GeneralStatus.IsHomeList);
        binding.recyclerViewPermission.setAdapter(adapterRV);
        adapterRV.setOnItemClickListener(new HomeAdapterRV.ClickListener() {
            @Override
            public void onItemClick(String permissionName, View v) {

                NavOptions.Builder navBuilder = new NavOptions.Builder();
                navBuilder.setEnterAnim(R.anim.slide_from_left).setExitAnim(R.anim.slide_out_right).setPopEnterAnim(R.anim.slide_from_right).setPopExitAnim(R.anim.slide_out_left);

                System.out.println("===========-----------=" + permissionName);

                switch (permissionName) {

                    case "TechnicalExpert":

                        break;

                        case "CheckList":

                        break;

                        case "Rescuer":
                            NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.startServiceFragment, null, navBuilder.build());
                        break;

                    case "Car":
                        NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.searchCarFragment, null, navBuilder.build());
                        break;

                    case "Representation":
                        NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.representationsFragment, null, navBuilder.build());
                        break;

                    case "Watcher":
                        NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.searchFragment, null, navBuilder.build());
                        break;

                    case "Forms":
                        NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.formFragment, null, navBuilder.build());

                        break;

                    case "Inspection":
                        NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.fieldInspectionFragment, null, navBuilder.build());
                        break;

                    case "Messages":
                        NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.chatGroupFragment, null, navBuilder.build());
                        break;

                    case "ROLE_APP_GET_AGENT_CAR_VIEW_RECP":
                        NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.detectPlateFragment, null, navBuilder.build());
                        break;

                        case "ROLE_APP_GET_AGENT_CAR_VIEW_SEC":
                        NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.manageEEFragment, null, navBuilder.build());
                        break;

                    case "Setting":
                        NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.settingFragment, null, navBuilder.build());
                        break;

                    case "Exit":
                        getActivity().finish();
                        break;
                }
            }
        });

    }

    private void initVeiw(View view) {
        progressView = view.findViewById(R.id.waitProgress);
        Util.showProgress(progressView);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    private void callApiRequest() {

        Util.showProgress(progressView);
        binding.btnTryAgain.setVisibility(View.GONE);

        mainViewModel.getUserPermissionListVM(inputParam)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SuccessPermissionBean>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull SuccessPermissionBean permissionBean) {
                        successPermissionBean = permissionBean;
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Log.e("TAG", "onError/Home=>: " + e.getLocalizedMessage());
                        Toast toast = Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG);
                        Util.showToast(toast, getActivity());
                        toast.show();
                        Util.hideProgress(progressView);
                        binding.btnTryAgain.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onComplete() {
                        Log.e("TAG", "onComplete: jsoup done");

                        if (successPermissionBean.getRESULT() != null) {
                            if (successPermissionBean.getRESULT().getUserPermissionList().size() > 0) {
                                mainViewModel.deletePermissionByUserId(user.getId());
                                List<String> userPermissionList = successPermissionBean.getRESULT().getUserPermissionList();
                                userPermissionListDb = new ArrayList<>();
                                for (String p : userPermissionList) {
                                    UserPermission userPermission = new UserPermission();
                                    userPermission.setUserId(user.getId());
                                    userPermission.setPermissionName(p);
                                    mainViewModel.insertPermission(userPermission);
                                    userPermissionListDb.add(userPermission);
                                }
                                Util.hideProgress(progressView);

                                setupRecyclerView(userPermissionListDb);
                            }
                        }
                    }
                });

    }

    public void getDataFromServer(){
        binding.btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callApiRequest();
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void addCompanyDialog() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_company);
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.setCancelable(false);
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
                GlobalValue.companyCode = txt_companyCode.getText().toString();
                Config.putSharedPreference(getActivity(), Constant.COMPANY_CODE, txt_companyCode.getText().toString());

                if (user.getCompanyName() != null){
                    binding.txtCompany.setText(user.getCompanyName() +" " + " کد : " + GlobalValue.companyCode );
                }else {
                    binding.txtCompany.setText("نمایندگی "+" " + GlobalValue.companyCode);
                }
                dialog.dismiss();
            }
        });
    }

}