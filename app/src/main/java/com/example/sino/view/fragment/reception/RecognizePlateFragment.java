package com.example.sino.view.fragment.reception;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sino.R;
import com.example.sino.SinoApplication;
import com.example.sino.databinding.FragmentRecognizePlateBinding;
import com.example.sino.model.Person;
import com.example.sino.model.PersonRequentBean;
import com.example.sino.model.db.User;
import com.example.sino.model.reception.PrcData;
import com.example.sino.model.reception.PrcSet;
import com.example.sino.model.reception.ProServiceResponse;
import com.example.sino.model.reception.ProSrv;
import com.example.sino.utils.GlobalValue;
import com.example.sino.utils.GsonGenerator;
import com.example.sino.utils.JalaliCalendarUtil;
import com.example.sino.utils.common.Util;
import com.example.sino.view.adapter.reseption.ButtonListAdapter;
import com.example.sino.view.adapter.reseption.ProSerStatusListAdapter;
import com.example.sino.viewmodel.MainViewModel;

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
public class RecognizePlateFragment extends Fragment {

    private FragmentRecognizePlateBinding binding;
    private NavOptions.Builder navBuilder;
    private boolean isOwner = false;
    private int personTypeEn = 0;
    private Person personCopy;
    private PersonRequentBean personRequentBean;
    private String address;
    private SharedPreferences sharedPreferences;
    private ButtonListAdapter buttonListAdapter;
    private ProSerStatusListAdapter statusListAdapter;
    private List<PrcSet> processBisSettingVOList;
    private List<PrcData> PrcDataList;
    private ProSrv proSrv = null;
    private String proSrvId;
    private String inputParam = "";
    private MainViewModel mainViewModel;
    private User user;
    private String prcDataId;
    private String prcSetId;
    private String description;

    private boolean isConfirm = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recognize_plate, container, false);
      /*  binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(RecognizePlateFragment.this).navigate(R.id.takePictureFragment, null, navBuilder.build());
            }
        });*/

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@androidx.annotation.NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navBuilder = new NavOptions.Builder();
        navBuilder.setEnterAnim(R.anim.slide_from_left).setExitAnim(R.anim.slide_out_right).setPopEnterAnim(R.anim.slide_from_right).setPopExitAnim(R.anim.slide_out_left);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        user = SinoApplication.getInstance().getCurrentUser();

        if (getArguments() != null) {
            isOwner = getArguments().getBoolean("isOwner");
            personTypeEn = getArguments().getInt("personTypeEn");
            personCopy = getArguments().getParcelable("personCopy");
            personRequentBean = getArguments().getParcelable("personRequentBean");
            address = getArguments().getString("address");
            proSrv = getArguments().getParcelable("proSrv");
        }

       /* if (proSrv == null){
            proSrv = GlobalValue.proSrv;
        }else {
            GlobalValue.proSrv = proSrv;
        }*/

        if (personTypeEn == 0) {
            //binding.cardView1.setVisibility(View.VISIBLE);
            binding.cardView2.setVisibility(View.GONE);
        } else {
            // binding.cardView1.setVisibility(View.GONE);
            binding.cardView2.setVisibility(View.VISIBLE);
        }

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isOwner", isOwner);
                NavHostFragment.findNavController(RecognizePlateFragment.this).navigate(R.id.addNewOwnerFragment, bundle, navBuilder.build());

            }
        });


        binding.btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isOwner", isOwner);
                NavHostFragment.findNavController(RecognizePlateFragment.this).navigate(R.id.addNewOwnerFragment, bundle, navBuilder.build());
            }
        });
    }

    private void setupBtnAdapter() {
        if (processBisSettingVOList == null)
            return;

        //proSrvId = GlobalValue.proSrv.proSrvId;

        buttonListAdapter = new ButtonListAdapter(processBisSettingVOList);
        // binding.btnRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.btnRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.btnRecyclerView.setAdapter(buttonListAdapter);
        buttonListAdapter.setOnItemClickListener(new ButtonListAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                prcSetId = processBisSettingVOList.get(position).prcSetId;
                //getAttachFileSettingItemList(false);
                prcDataId = null;
                isConfirm = false;
                if (prcSetId.equals("21921")) {
                    gotoAddCustomerDataFragment(false);
                } else if (prcSetId.equals("21922")) {
                    gotoExpertDataFragment(false);
                }
            }
        });
    }

    private void setupStatusAdapter(ProSrv proSrv) {
        PrcDataList = proSrv.prcDataList;
        if (PrcDataList != null) {
            statusListAdapter = new ProSerStatusListAdapter(PrcDataList);
            binding.serviceRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.serviceRecyclerView.setAdapter(statusListAdapter);
            statusListAdapter.setOnItemClickListener(new ProSerStatusListAdapter.ClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    if (proSrv.prcDataList.get(position).prcDataVO.processStatus == 1) {
                        isConfirm = true;
                        //return;
                    } else {
                        isConfirm = false;
                    }
                    if (proSrv.prcDataList.get(position).prcDataVO != null) {
                        prcDataId = proSrv.prcDataList.get(position).prcDataVO.prcDataId;
                        description = proSrv.prcDataList.get(position).prcDataVO.description;
                        prcSetId = proSrv.prcDataList.get(position).prcDataVO.ProcessStrucSettingId;

                        if (prcSetId.equals("21921")) {
                            gotoAddCustomerDataFragment(true);
                        } else if (prcSetId.equals("21922")) {
                            gotoExpertDataFragment(true);
                        }
                    }
                }
            });
        }

    }

    private void gotoAddCustomerDataFragment(boolean isEdit) {
        Bundle bundle = new Bundle();
        bundle.putString("prcSetId", prcSetId);
        GlobalValue.proSrvId = proSrv.proSrvId;
        GlobalValue.proSrv = proSrv;
        bundle.putString("proSrvId", proSrv.proSrvId);
        bundle.putBoolean("isEdit", isEdit);
        bundle.putBoolean("isConfirm", isConfirm);
        bundle.putString("prcDataId", prcDataId);
        bundle.putString("description", description);

        NavHostFragment.findNavController(RecognizePlateFragment.this).navigate(R.id.addCustomerDataFragment, bundle, navBuilder.build());
    }

    private void gotoExpertDataFragment(boolean isEdit) {
        Bundle bundle = new Bundle();
        bundle.putString("prcSetId", prcSetId);
        bundle.putString("proSrvId", proSrv.proSrvId);
        GlobalValue.proSrvId = proSrv.proSrvId;
        GlobalValue.proSrv = proSrv;
        bundle.putBoolean("isEdit", isEdit);
        bundle.putBoolean("isConfirm", isConfirm);
        bundle.putString("prcDataId", prcDataId);
        bundle.putString("description", description);
        System.out.println("isConfirm1=====" + isConfirm);
        NavHostFragment.findNavController(RecognizePlateFragment.this).navigate(R.id.addExpertDataFragment, bundle, navBuilder.build());

    }

    private void getProSrvById(String id) {

        Util.showProgress(binding.waitProgress);
        inputParam = GsonGenerator.getProSrvById(user.getUsername(), user.getBisPassword(), Long.parseLong(id));

        mainViewModel.getProSrvById(inputParam).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ProServiceResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ProServiceResponse proServiceResponse) {

                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void run() {
                                    Util.hideProgress(binding.waitProgress);
                                    binding.cardView1.setVisibility(View.VISIBLE);
                                    if (proServiceResponse.result.proSrv != null) {
                                        if (proServiceResponse.result.proSrv.car.plateText != null) {
                                            binding.txtPlate.setText("پلاک : " + proServiceResponse.result.proSrv.car.plateText);
                                        }
                                        if (proServiceResponse.result.proSrv.car.chassis != null) {
                                            binding.txtChassis.setText("شاسی : " + proServiceResponse.result.proSrv.car.chassis);
                                        }
                                        if (proServiceResponse.result.proSrv.car.autoCompleteLabel != null) {
                                            binding.txtTip.setText("تیپ : " + proServiceResponse.result.proSrv.car.autoCompleteLabel);
                                        }
                                        if (proServiceResponse.result.proSrv.car.productionYear != null) {
                                            binding.txtModel.setText("مدل : " + proServiceResponse.result.proSrv.car.productionYear);
                                        }
                                        if (proServiceResponse.result.proSrv.licPro != null && proServiceResponse.result.proSrv.licPro.licTypeEn_text != null) {
                                            binding.txtPermission.setText("مجوز : " + proServiceResponse.result.proSrv.licPro.licTypeEn_text);

                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                                            if (proServiceResponse.result.proSrv.licPro.endDatePlan != null) {
                                                try {
                                                    Date date = sdf.parse(proServiceResponse.result.proSrv.licPro.endDatePlan);
                                                    Calendar calendar = Calendar.getInstance();
                                                    calendar.setTime(date);
                                                    JalaliCalendarUtil jalaliCalendarUtil = new JalaliCalendarUtil(calendar);
                                                    binding.txtPermissionDate.setText("پایان : " + jalaliCalendarUtil.getIranianDate() + " ساعت " + jalaliCalendarUtil.getTime(calendar));


                                                    Date d1 = new Date();
                                                    Calendar calendar1 = Calendar.getInstance();
                                                    calendar1.setTime(d1);
                                                    Date d2 = sdf.parse(proServiceResponse.result.proSrv.licPro.endDatePlan);
                                                    Calendar calendar2 = Calendar.getInstance();
                                                    calendar2.setTime(d2);

                                                    long diff = d2.getTime() - d1.getTime();
                                                    //System.out.println ("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.DAYS));
                                                    System.out.println("Days: " + Util.daysBetween(calendar1, calendar2));
                                                    binding.txtRemind.setText(Util.daysBetween(calendar1, calendar2) + " روز تا پایان اعتبار ");


                                                } catch (ParseException e) {
                                                    throw new RuntimeException(e);
                                                }
                                            }

                                        }
                                        processBisSettingVOList = proServiceResponse.result.proSrv.prcSetList;
                                        proSrv = proServiceResponse.result.proSrv;
                                        proSrvId = proServiceResponse.result.proSrv.proSrvId;
                                        setupStatusAdapter(proSrv);
                                        setupBtnAdapter();
                                    }

                                }
                            });
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Util.hideProgress(binding.waitProgress);
                                    Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onComplete() {

                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Util.hideProgress(binding.waitProgress);
                                }
                            });
                        }
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (GlobalValue.proSrvId != null) {

            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        getProSrvById(GlobalValue.proSrvId);
                    }
                });
            }
        } else if (proSrv != null) {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        getProSrvById(proSrv.proSrvId);
                    }
                });
            }
        }

    }
}