package com.example.sino.view.fragment.reception;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sino.R;
import com.example.sino.SinoApplication;
import com.example.sino.databinding.FragmentProSerListBinding;
import com.example.sino.model.db.User;
import com.example.sino.model.reception.ProServiceResponse;
import com.example.sino.model.reception.ProSrv;
import com.example.sino.utils.GlobalValue;
import com.example.sino.utils.GsonGenerator;
import com.example.sino.utils.common.Util;
import com.example.sino.view.adapter.reseption.ProSerCarListAdapter;
import com.example.sino.viewmodel.MainViewModel;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class ProSerListFragment extends Fragment {

    private FragmentProSerListBinding binding;
    private ProSerCarListAdapter adapter;
    private String inputParam = "";
    private User user;
    private MainViewModel mainViewModel;
    private String ProcessStrucSettingId;
    private ProServiceResponse proServiceResponseTemp = null;
    private int carId = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pro_ser_list, container, false);
        user = SinoApplication.getInstance().getCurrentUser();
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        getProSrvList();
        // getProSrvById();
        return binding.getRoot();
    }


    private void setupAdapter(List<ProSrv> proSrvList) {
        adapter = new ProSerCarListAdapter(proSrvList);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ProSerCarListAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                ProSrv proSrv = proSrvList.get(position);
                carId = proSrv.car.id;
                Bundle bundle = new Bundle();
                bundle.putParcelable("proSrv", proSrv);
                NavOptions.Builder navBuilder = new NavOptions.Builder();
                navBuilder.setEnterAnim(R.anim.slide_from_left).setExitAnim(R.anim.slide_out_right).setPopEnterAnim(R.anim.slide_from_right).setPopExitAnim(R.anim.slide_out_left);
                NavHostFragment.findNavController(ProSerListFragment.this).navigate(R.id.recognizePlateFragment, bundle, navBuilder.build());

            }
        });
    }

    private void getProSrvList() {
        GlobalValue.proSrvId = null;
        System.out.println("===getProSrvList===");

        Util.showProgress(binding.waitProgress);
        binding.txtNull.setVisibility(View.GONE);
        inputParam = GsonGenerator.getProSrvList(user.getUsername(), user.getBisPassword(), Integer.parseInt(GlobalValue.companyCode));
        mainViewModel.getProSrvList(inputParam).subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ProServiceResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ProServiceResponse proServiceResponse) {

                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    // Stuff that updates the UI
                                    Util.hideProgress(binding.waitProgress);
                                    if (proServiceResponse.success != null) {
                                        if (proServiceResponse.result != null) {

                                            proServiceResponseTemp = proServiceResponse;
                                            System.out.println("nameFv=======" + proServiceResponse.result.proSrvList.get(0).car.nameFv);
                                            setupAdapter(proServiceResponse.result.proSrvList);
                                        }
                                    } else if (proServiceResponse.ERROR != null) {

                                        if (getActivity() != null) {
                                            getActivity().runOnUiThread(new Runnable() {
                                                public void run() {
                                                    Util.hideProgress(binding.waitProgress);
                                                    Toast toast = Toast.makeText(getActivity(), proServiceResponse.ERROR, Toast.LENGTH_LONG);
                                                    Util.showToast(toast, getActivity());
                                                    toast.show();
                                                }
                                            });
                                        }
                                    }
                                }
                            });
                        }


                        System.out.println("===onNext===");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    Util.hideProgress(binding.waitProgress);
                                    Toast toast = Toast.makeText(getActivity(),e.getLocalizedMessage(), Toast.LENGTH_LONG);
                                    Util.showToast(toast, getActivity());
                                    toast.show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("===onComplete===");
                    }
                });
    }

}