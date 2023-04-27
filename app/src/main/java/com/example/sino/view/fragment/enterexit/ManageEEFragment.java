package com.example.sino.view.fragment.enterexit;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sino.R;
import com.example.sino.SinoApplication;
import com.example.sino.databinding.FragmentManageEEBinding;
import com.example.sino.model.carinfo.Car;
import com.example.sino.model.carinfo.SuccessCarInfoBean;
import com.example.sino.model.dailyEvent.DailyEvent;
import com.example.sino.model.dailyEvent.DailyEventRespons;
import com.example.sino.model.db.User;
import com.example.sino.utils.GlobalValue;
import com.example.sino.utils.GsonGenerator;
import com.example.sino.view.adapter.car.DailyEventListAdapter;
import com.example.sino.viewmodel.MainViewModel;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class ManageEEFragment extends Fragment {

    private FragmentManageEEBinding binding;
    private NavOptions.Builder navBuilder;
    private String plateText;
    private String inputParam = "";
    private User user;
    private MainViewModel viewModel;
    private SuccessCarInfoBean successCarInfoBeanCopy;
    private List<Car> carList;
    //private int eventType = -1; //enter
    private int typeGetCar = -1;
    private List<DailyEvent> eventList = null;
    private DailyEventListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_manage_e_e, container, false);
        user = SinoApplication.getInstance().getCurrentUser();
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        navBuilder = new NavOptions.Builder();
        navBuilder.setEnterAnim(R.anim.slide_from_left).setExitAnim(R.anim.slide_out_right).setPopEnterAnim(R.anim.slide_from_right).setPopExitAnim(R.anim.slide_out_left);

        binding.detectPlate.setOnClickListener(onCameraDemoClicked);
        binding.handPlate.setOnClickListener(handPlateClicked);
        binding.imgEventList.setOnClickListener(getListClicked);
        getList();


        /*OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                if (binding.frameLayout.getVisibility() == View.VISIBLE){
                    binding.frameLayout.setVisibility(View.GONE);
                }else {
                    NavHostFragment.findNavController(ManageEEFragment.this).navigate(R.id.manageEEFragment, null, navBuilder.build());

                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);*/

        return binding.getRoot();
    }

    private View.OnClickListener getListClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (eventList != null && eventList.size()>0){
                showDialog(eventList);
            }
        }
    };
    private View.OnClickListener onCameraDemoClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            GlobalValue.ManageEEFragment = null;
            typeGetCar = 1;
            NavHostFragment.findNavController(ManageEEFragment.this).navigate(R.id.captureActivity, null, navBuilder.build());
        }
    };

    private View.OnClickListener handPlateClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            GlobalValue.ManageEEFragment = null;
            typeGetCar = 0;
            Bundle bundle = new Bundle();
            //bundle.putInt("eventType", eventType);
            bundle.putInt("typeGetCar", typeGetCar);
            NavHostFragment.findNavController(ManageEEFragment.this).navigate(R.id.enterHandPlateFragment, bundle, navBuilder.build());
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (data != null) {
                plateText = data.getStringExtra("plateText");

                System.out.println("plateText2======" + plateText);

            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();

        System.out.println("plateNo=====" + GlobalValue.plateNo);
        System.out.println("onResume=====");
        if (GlobalValue.plateNo != null) {
            Bundle bundle = new Bundle();
            bundle.putString("plateNo" ,GlobalValue.plateNo);
            bundle.putInt("typeGetCar", typeGetCar);
            GlobalValue.plateNo = null;
            NavHostFragment.findNavController(ManageEEFragment.this).navigate(R.id.enterHandPlateFragment, bundle, navBuilder.build());
        }
    }

    public void getList(){
        ProgressDialog dialog = ProgressDialog.show(getActivity(), "","لطفا منتظر بمانید..." , true);
        inputParam = GsonGenerator.getDailyEventList(user.getUsername(), user.getBisPassword(),GlobalValue.companyCode);
        viewModel.dailyEvent_GetList_CarEnter_PS0(inputParam).subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DailyEventRespons>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull DailyEventRespons dailyEventRespons) {
                        if (dailyEventRespons.success != null){
                            if (dailyEventRespons.result != null){
                                eventList = dailyEventRespons.result.dailyEventList;

                                if (getActivity() != null) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        public void run() {
                                            dialog.dismiss();
                                            binding.imgEventList.setText("خودرو های بلاتکلیف " + " ( " +eventList.size()+ " ) "  );
                                        }
                                    });
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void showDialog(List<DailyEvent> eventList){
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_event_list);
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.show();

        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView);

        adapter = new DailyEventListAdapter(eventList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new DailyEventListAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                DailyEvent dailyEvent = eventList.get(position);
                //getProServiceByCarId(car.id);
                Bundle bundle = new Bundle();
                //bundle.putInt("dailEvn_id", dailyEvent.id);
                bundle.putParcelable("dailyEvent", dailyEvent);
                NavHostFragment.findNavController(ManageEEFragment.this).navigate(R.id.enterHandPlateFragment, bundle, navBuilder.build());
                dialog.dismiss();
            }
        });
    }
}