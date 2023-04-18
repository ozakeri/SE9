package com.example.sino.view.fragment.reception;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.example.sino.R;
import com.example.sino.SinoApplication;
import com.example.sino.databinding.FragmentCustomerSearchBinding;
import com.example.sino.model.PersonRequentBean;
import com.example.sino.model.db.User;
import com.example.sino.utils.GsonGenerator;
import com.example.sino.utils.common.Util;
import com.example.sino.viewmodel.MainViewModel;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class SearchFragment extends Fragment {

    private FragmentCustomerSearchBinding binding;
    private boolean isOwner;
    private User user;
    private int personTypeEn = 0;
    private String inputParam = "";
    private MainViewModel viewModel;
    private NavOptions.Builder navBuilder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_customer_search, container, false);
        user = SinoApplication.getInstance().getCurrentUser();
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        binding.cardView1.setVisibility(View.VISIBLE);
        binding.cardView2.setVisibility(View.GONE);

        navBuilder = new NavOptions.Builder();
        navBuilder.setEnterAnim(R.anim.slide_from_left).setExitAnim(R.anim.slide_out_right).setPopEnterAnim(R.anim.slide_from_right).setPopExitAnim(R.anim.slide_out_left);


        if (getArguments() != null) {
            isOwner = getArguments().getBoolean("isOwner");
            //personTypeEn = getArguments().getInt("personTypeEn");
        }

        NavOptions.Builder navBuilder = new NavOptions.Builder();
        navBuilder.setEnterAnim(R.anim.slide_from_left).setExitAnim(R.anim.slide_out_right).setPopEnterAnim(R.anim.slide_from_right).setPopExitAnim(R.anim.slide_out_left);


        binding.selectedType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButtonOne:
                        binding.cardView1.setVisibility(View.VISIBLE);
                        binding.cardView2.setVisibility(View.GONE);
                        personTypeEn = 0;
                        break;

                    case R.id.radioButtonTwo:
                        binding.cardView2.setVisibility(View.VISIBLE);
                        binding.cardView1.setVisibility(View.GONE);
                        personTypeEn = 1;
                        break;
                }
            }
        });

        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (personTypeEn == 0){

                    if (!binding.txtNationalCode.getText().toString().trim().isEmpty() &&
                            !binding.txtModel.getText().toString().trim().isEmpty() &&
                            !binding.txtFamily.getText().toString().trim().isEmpty() &&
                            !binding.txtMobile.getText().toString().trim().isEmpty() &&
                            !binding.txtPhone.getText().toString().trim().isEmpty() &&
                            !binding.txtAddress.getText().toString().trim().isEmpty()) {

                        upsertPersonByParam(binding.txtNationalCode.getText().toString(),
                                binding.txtModel.getText().toString(),
                                binding.txtFamily.getText().toString(),
                                binding.txtMobile.getText().toString(),
                                binding.txtPhone.getText().toString(),
                                binding.txtAddress.getText().toString(),
                                personTypeEn,
                                false,
                                binding.waitProgress);
                    } else {
                        Toast toast = Toast.makeText(getActivity(), "اطلاعات را کامل پر کنید", Toast.LENGTH_LONG);
                        Util.showToast(toast, getActivity());
                        toast.show();
                    }

                }else {

                    if (!binding.txtCompanyId.getText().toString().trim().isEmpty() &&
                            !binding.txtCompanyName.getText().toString().trim().isEmpty() &&
                            !binding.txtCompanyPhone.getText().toString().trim().isEmpty() &&
                            !binding.txtCompanyAddress.getText().toString().trim().isEmpty()) {

                        upsertPersonByParam(binding.txtCompanyId.getText().toString(),
                                binding.txtCompanyName.getText().toString(),
                                "",
                                "",
                                binding.txtCompanyPhone.getText().toString(),
                                binding.txtCompanyAddress.getText().toString(),
                                personTypeEn,
                                false,
                                binding.waitProgress);
                    } else {
                        Toast toast = Toast.makeText(getActivity(), "اطلاعات را کامل پر کنید", Toast.LENGTH_LONG);
                        Util.showToast(toast, getActivity());
                        toast.show();
                    }

                }



               // NavHostFragment.findNavController(SearchFragment.this).navigate(R.id.recognizePlateFragment, null, navBuilder.build());
            }
        });

        return binding.getRoot();
    }


    public void upsertPersonByParam(String nationalCode,
                                    String name,
                                    String family,
                                    String mobile,
                                    String phone,
                                    String address,
                                    int personType,
                                    boolean ifExist,
                                    CircularProgressView waitProgress) {
        user = SinoApplication.getInstance().getCurrentUser();
        inputParam = GsonGenerator.upsertPersonByParam(user.getUsername(), user.getBisPassword(), nationalCode, name, family, mobile, phone, address, personType);
        waitProgress.setVisibility(View.VISIBLE);
        viewModel.upsertPersonByParam(inputParam).subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PersonRequentBean>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull PersonRequentBean personRequentBean) {

                        if (getActivity() != null){
                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    if (personRequentBean.result != null) {
                                        waitProgress.setVisibility(View.GONE);
                                        Bundle bundle = new Bundle();
                                        bundle.putBoolean("isOwner", isOwner);
                                        bundle.putInt("personTypeEn", personTypeEn);
                                        bundle.putString("address", address);
                                        bundle.putParcelable("personRequentBean", personRequentBean);
                                        NavHostFragment.findNavController(SearchFragment.this).navigate(R.id.recognizePlateFragment, bundle, navBuilder.build());

                                    }

                                }
                            });
                        }

                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        System.out.println("===onError===" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("===onComplete===");
                    }
                });
    }

}