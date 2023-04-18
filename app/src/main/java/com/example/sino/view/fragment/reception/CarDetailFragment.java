package com.example.sino.view.fragment.reception;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.example.sino.R;
import com.example.sino.SinoApplication;
import com.example.sino.databinding.FragmentCarDetailBinding;
import com.example.sino.model.Person;
import com.example.sino.model.PersonRequentBean;
import com.example.sino.model.SuccessRegisterBean;
import com.example.sino.model.carinfo.Car;
import com.example.sino.model.db.User;
import com.example.sino.model.form.FormRequentBean;
import com.example.sino.utils.GlobalValue;
import com.example.sino.utils.GsonGenerator;
import com.example.sino.utils.common.Util;
import com.example.sino.utils.services.LocationUpdatesService;
import com.example.sino.viewmodel.MainViewModel;
import com.example.sino.viewmodel.RegisterViewModel;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class CarDetailFragment extends Fragment {

    private FragmentCarDetailBinding binding;
    private Car car;
    private NavOptions.Builder navBuilder;
    private boolean userIfExist = false;
    private boolean isOwner = false;
    private MainViewModel viewModel;
    private RegisterViewModel registerViewModel;
    private String inputParam = "";
    private User user;
    private String mobileNo = null;
    private int personTypeEn = 0;
    private int serviceType = 1;
    private String kilometreStr = "";
    private Person personCopy;
    private MainViewModel mainViewModel;
    private SharedPreferences sharedPreferences;
    private int count = 5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_car_detail, container, false);

        navBuilder = new NavOptions.Builder();
        navBuilder.setEnterAnim(R.anim.slide_from_left).setExitAnim(R.anim.slide_out_right).setPopEnterAnim(R.anim.slide_from_right).setPopExitAnim(R.anim.slide_out_left);
        sharedPreferences = getActivity().getSharedPreferences("proModelId", MODE_PRIVATE);
        user = SinoApplication.getInstance().getCurrentUser();
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        if (getArguments() != null) {
            car = getArguments().getParcelable("car");
            binding.txtColor.setText(" رنگ خودرو : " + car.color.name);
            binding.txtChassis.setText(" شماره شاسی : " + car.chassis);
            binding.txtMotor.setText(" شماره موتور : " + car.engineNo);
            binding.txtPlate.setText(" شماره پلاک : " + car.plateText);
            binding.txtModel.setText(" مدل : " + car.productionYear);
            binding.txtPermission.setText(" مجوز : " + car.seLicPro.licTypeEn_text);
            binding.txtType.setText(" نوع خودرو : " + car.seProModel.proModelGroup.name);
            binding.txtTip.setText(" سیستم - تیپ : " + car.seProModel.name);
            binding.txtPermissionDate.setText(" تاریخ اعتبار : " + car.seLicPro.startDate);
            //System.out.println("car.id===" + car.id);
            GlobalValue.carId = car.id;
        }

      /*  binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(CarDetailFragment.this).navigate(R.id.addNewOwnerFragment, null, navBuilder.build());
            }
        });*/

      /*  binding.btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(CarDetailFragment.this).navigate(R.id.changePlateFragment, null, navBuilder.build());
            }
        });*/


        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //NavHostFragment.findNavController(CarDetailFragment.this).navigate(R.id.ownerDetailFragment, null, navBuilder.build());

                //    searchCustomerDialog();

                //  NavHostFragment.findNavController(CarDetailFragment.this).navigate(R.id.searchCustomerFragment, null, navBuilder.build());
                addServiceTypeDialog();

            }
        });
        return binding.getRoot();
    }

    public void addCustomerDialog(String nationalCode, String name, String family, String mobile, String phone, String address, boolean userIfExist) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_customer);
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.show();

        System.out.println("===dialog.show===");

        TextView txt_nationalCode = dialog.findViewById(R.id.txt_nationalCode);
        TextView txt_name = dialog.findViewById(R.id.txt_model);
        TextView txt_family = dialog.findViewById(R.id.txt_family);
        TextView txt_mobile = dialog.findViewById(R.id.txt_mobile);
        TextView txt_phone = dialog.findViewById(R.id.txt_phone);
        TextView txt_address = dialog.findViewById(R.id.txt_address);
        TextView btn_confirm = dialog.findViewById(R.id.btn_confirm);
        CheckBox checkBoxYes = dialog.findViewById(R.id.checkBoxYes);
        CheckBox checkBoxNo = dialog.findViewById(R.id.checkBoxNo);
        CircularProgressView waitProgress = dialog.findViewById(R.id.waitProgress);


        getLocation();


        if (userIfExist) {
            txt_nationalCode.setText(nationalCode);
            txt_name.setText(name);
            txt_family.setText(family);
            txt_mobile.setText(mobile);
            txt_phone.setText(phone);
            txt_address.setText(address);
            mobileNo = mobile;
        }


        checkBoxYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    checkBoxNo.setChecked(false);
                    checkBoxYes.setChecked(true);
                    isOwner = true;
                } else {
                    checkBoxNo.setChecked(true);
                    checkBoxYes.setChecked(false);
                    isOwner = false;
                }
            }
        });

        checkBoxNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    checkBoxNo.setChecked(true);
                    checkBoxYes.setChecked(false);
                    isOwner = false;
                } else {
                    checkBoxNo.setChecked(false);
                    checkBoxYes.setChecked(true);
                    isOwner = true;
                }
            }
        });

        if (userIfExist) {
            btn_confirm.setText("ویرایش");
        } else {
            btn_confirm.setText("ثبت");
        }

        dialog.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!txt_nationalCode.getText().toString().trim().isEmpty() &&
                        !txt_name.getText().toString().trim().isEmpty() &&
                        !txt_family.getText().toString().trim().isEmpty() &&
                        !txt_mobile.getText().toString().trim().isEmpty() &&
                        !txt_phone.getText().toString().trim().isEmpty() &&
                        !txt_address.getText().toString().trim().isEmpty()) {


                    //dialog.dismiss();
                    upsertPersonByParam(txt_nationalCode.getText().toString(), txt_name.getText().toString()
                            , txt_family.getText().toString(), txt_mobile.getText().toString(), txt_phone.getText().toString()
                            , txt_address.getText().toString(), 0, false, dialog, waitProgress);
                } else {
                    Toast toast = Toast.makeText(getActivity(), "اطلاعات را کامل پر کنید", Toast.LENGTH_LONG);
                    Util.showToast(toast, getActivity());
                    toast.show();
                }
            }
        });
    }


    public void searchCustomerDialog() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_search_customer);
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(layoutParams);
        TextView txt_nationalCode = dialog.findViewById(R.id.txt_nationalCode);
        RadioGroup selectedType = dialog.findViewById(R.id.selected_type);
        CircularProgressView progressView = dialog.findViewById(R.id.waitProgress);
        dialog.show();
        txt_nationalCode.setHint("کد ملی");
        selectedType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButtonOne:
                        txt_nationalCode.setHint("کد ملی");
                        personTypeEn = 0;
                        break;

                    case R.id.radioButtonTwo:
                        txt_nationalCode.setHint("شناسه ملی");
                        personTypeEn = 1;
                        break;
                }
            }
        });

        dialog.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //txt_nationalCode
                //txt_name
                //txt_family
                //txt_mobile
                //txt_address
                String nationalCode = txt_nationalCode.getText().toString();

                if (!nationalCode.trim().isEmpty()) {


                    getPersonByNationalCode(txt_nationalCode.getText().toString(), personTypeEn, progressView, dialog, false);
                } else {
                    Toast toast = Toast.makeText(getActivity(), "شماره ملی معتبر نیست", Toast.LENGTH_LONG);
                    Util.showToast(toast, getActivity());
                    toast.show();
                }



             /*   addCustomerDialog(personRequentBean.result.person.nationalCode,
                        personRequentBean.result.person.name,
                        personRequentBean.result.person.family,
                        "11111",
                        "address",
                        true);*/


               /* if (userIfExist) {
                    addCustomerDialog("nationalCode", "name", "family", "mobile", "address", userIfExist);
                } else {
                    addCustomerDialog(null, null, null, null, null, userIfExist);
                }*/

                //dialog.dismiss();
            }
        });


    }

    public void addServiceTypeDialog() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_service_type);
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(layoutParams);
        TextView txt_kilometre = dialog.findViewById(R.id.txt_kilometre);
        RadioGroup selectedType = dialog.findViewById(R.id.selected_type);
        dialog.show();

        selectedType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButtonFirstService:
                        serviceType = 1;
                        GlobalValue.serviceType = 1;
                        break;

                    case R.id.radioButtonPeriodicService:
                        serviceType = 2;
                        GlobalValue.serviceType = 2;

                        break;

                    case R.id.radioButtonRepairs:
                        serviceType = 3;
                        GlobalValue.serviceType = 3;
                        break;
                }
            }
        });

        dialog.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!txt_kilometre.getText().toString().isEmpty()) {
                    kilometreStr = txt_kilometre.getText().toString();
                    GlobalValue.kmCar = kilometreStr;
                    searchCustomerDialog();
                    dialog.dismiss();
                    addFuelDialog();

                } else {
                    Toast toast = Toast.makeText(getActivity(), "کیلومتر خودرو را وارد کنید", Toast.LENGTH_LONG);
                    Util.showToast(toast, getActivity());
                    toast.show();
                }

            }
        });
    }

    public void activationCodeDialog() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_activation_code);
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.show();


        EditText txt_number1 = dialog.findViewById(R.id.txt_number1);
        EditText txt_number2 = dialog.findViewById(R.id.txt_number2);
        EditText txt_number3 = dialog.findViewById(R.id.txt_number3);
        EditText txt_number4 = dialog.findViewById(R.id.txt_number4);
        EditText txt_number5 = dialog.findViewById(R.id.txt_number5);
        CircularProgressView waitProgressDialog = dialog.findViewById(R.id.waitProgressDialog);
        waitProgressDialog.setVisibility(View.GONE);

        txt_number1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (txt_number1.getText().length() == 1) {
                    txt_number2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        txt_number2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (txt_number2.getText().length() == 1) {
                    txt_number3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        txt_number3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (txt_number3.getText().length() == 1) {
                    txt_number4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        txt_number4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (txt_number4.getText().length() == 1) {
                    txt_number5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        dialog.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //progressView.setVisibility(View.VISIBLE);
                if (txt_number1.getText().toString().trim().isEmpty() ||
                        txt_number2.getText().toString().trim().isEmpty() ||
                        txt_number3.getText().toString().trim().isEmpty() ||
                        txt_number4.getText().toString().trim().isEmpty() ||
                        txt_number5.getText().toString().trim().isEmpty()) {
                    Toast toast = Toast.makeText(getActivity(), "کد را کامل وارد کنید", Toast.LENGTH_LONG);
                    Util.showToast(toast, getActivity());
                    toast.show();
                    waitProgressDialog.setVisibility(View.GONE);
                    return;

                }
                String codeActive = txt_number1.getText().toString() +
                        txt_number2.getText().toString() +
                        txt_number3.getText().toString() +
                        txt_number4.getText().toString() +
                        txt_number5.getText().toString();



                saveProModel(waitProgressDialog);

                //NavHostFragment.findNavController(CarDetailFragment.this).navigate(R.id.takePictureFragment, null, navBuilder.build());
                dialog.dismiss();


                String code = GsonGenerator.sendActivationCodeToGson(mobileNo, codeActive);

              /*  registerViewModel.sendActivationCode(code).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<SuccessActivationBean>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onNext(@NonNull SuccessActivationBean successActivationBean) {

                                if (successActivationBean.getERROR() == null && successActivationBean.getSUCCESS() != null) {
                                    progressView.setVisibility(View.GONE);
                                    Bundle bundle = new Bundle();
                                    bundle.putBoolean("isOwner", isOwner);
                                    bundle.putInt("personTypeEn", personTypeEn);
                                    bundle.putParcelable("personCopy", personCopy);
                                    System.out.println("===isOwner===" + isOwner);
                                    saveProModel();
                                    if (isOwner) {
                                        NavHostFragment.findNavController(CarDetailFragment.this).navigate(R.id.recognizePlateFragment, bundle, navBuilder.build());
                                    } else {
                                        // searchCustomerDialog();
                                        NavHostFragment.findNavController(CarDetailFragment.this).navigate(R.id.searchCustomerFragment, bundle, navBuilder.build());
                                    }
                                    //NavHostFragment.findNavController(CarDetailFragment.this).navigate(R.id.takePictureFragment, null, navBuilder.build());
                                    dialog.dismiss();
                                } else {
                                    Toast toast = Toast.makeText(getActivity(), "کد اعتبار سنجی صحیح نمی باشد", Toast.LENGTH_LONG);
                                    Util.showToast(toast, getActivity());
                                    toast.show();
                                    progressView.setVisibility(View.GONE);
                                }

                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                progressView.setVisibility(View.GONE);
                            }

                            @Override
                            public void onComplete() {
                                progressView.setVisibility(View.GONE);
                            }
                        });*/
            }
        });
    }

    public void getPersonByNationalCode(String nationalCode, int personType, CircularProgressView progressView, Dialog dialog, boolean isOwnerSearch) {
        user = SinoApplication.getInstance().getCurrentUser();

        inputParam = GsonGenerator.getPersonByNationalCode(user.getUsername(), user.getBisPassword(), personType, nationalCode);
        progressView.setVisibility(View.VISIBLE);
        viewModel.getPersonByNationalCodeRepo(inputParam).subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PersonRequentBean>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        System.out.println("===onSubscribe===");
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull PersonRequentBean personRequentBean) {

                        System.out.println("===onNext===");


                        if (personRequentBean.result.person != null) {

                            personCopy = personRequentBean.result.person;

                            if (getActivity() != null){
                                getActivity().runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {

                                        dialog.dismiss();
                                        progressView.setVisibility(View.GONE);
                                        addCustomerDialog(personRequentBean.result.person.nationalCode,
                                                personRequentBean.result.person.name,
                                                personRequentBean.result.person.family,
                                                personRequentBean.result.person.address.mobileNo,
                                                personRequentBean.result.person.address.telNo,
                                                personRequentBean.result.person.address.address,
                                                true);
                                    }
                                });
                            }



                        } else {

                            if (getActivity() != null){
                                getActivity().runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        dialog.dismiss();
                                        progressView.setVisibility(View.GONE);
                                        addCustomerDialog(null,
                                                null,
                                                null,
                                                null,
                                                null,
                                                null,
                                                false);
                                    }
                                });
                            }


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

    public void upsertPersonByParam(String nationalCode, String name, String family, String mobile, String phone, String address,
                                    int personType, boolean ifExist, Dialog dialog, CircularProgressView waitProgress) {
        user = SinoApplication.getInstance().getCurrentUser();
        inputParam = GsonGenerator.upsertPersonByParam(user.getUsername(), user.getBisPassword(), nationalCode, name, family,
                mobile, phone, address, personType);
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
                                        mobileNo = mobile;
                                        personCopy = personRequentBean.result.person;
                                        dialog.dismiss();
                                        waitProgress.setVisibility(View.GONE);
                                        ///getActivationCode();

                                        activationCodeDialog();
                                    }
                                }
                            });
                        }

                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        System.out.println("===onError===" + e.getMessage());
                        if (getActivity() != null){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    waitProgress.setVisibility(View.GONE);
                                }
                            });
                        }

                    }

                    @Override
                    public void onComplete() {
                        System.out.println("===onComplete===");
                        if (getActivity() != null){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    waitProgress.setVisibility(View.GONE);
                                }
                            });
                        }

                    }
                });
    }


    public void getActivationCode() {
        if (mobileNo != null) {
            inputParam = GsonGenerator.mobileNoConfirmationToGson(mobileNo);
            registerViewModel.sendPhoneNumber(inputParam).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<SuccessRegisterBean>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@NonNull SuccessRegisterBean successRegisterBean) {
                            if (successRegisterBean.getERROR() == null && successRegisterBean.getSUCCESS() != null) {

                                if (getActivity() != null){
                                    getActivity().runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {
                                            activationCodeDialog();
                                        }
                                    });
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

    }

    public void saveProModel(CircularProgressView waitProgressDialog) {
        int carId = GlobalValue.carId;
        String kmCar = GlobalValue.kmCar;

        waitProgressDialog.setVisibility(View.VISIBLE);
        System.out.println("==================" + GlobalValue.lat);
        System.out.println("==================" + GlobalValue.lang);

        inputParam = GsonGenerator.saveProModel(user.getUsername(), user.getBisPassword(), carId,1111, kmCar,GlobalValue.fuel, serviceType, GlobalValue.lat, GlobalValue.lang);
        mainViewModel.saveProService(inputParam).subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FormRequentBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull FormRequentBean formRequentBean) {
                        if (formRequentBean.result != null) {
                            waitProgressDialog.setVisibility(View.GONE);

                            NavHostFragment.findNavController(CarDetailFragment.this).navigate(R.id.proSerListFragment, null, navBuilder.build());

                            if (formRequentBean.result.proServiceVOId != null) {
                                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                myEdit.putString("proModelId", String.valueOf(formRequentBean.result.proServiceVOId));
                                myEdit.commit();
                                GlobalValue.proServiceId = formRequentBean.result.proServiceVOId;

                                Bundle bundle = new Bundle();
                                bundle.putBoolean("isOwner", isOwner);
                                bundle.putInt("personTypeEn", personTypeEn);
                                bundle.putParcelable("personCopy", personCopy);
                                System.out.println("===isOwner===" + isOwner);
                              /*  if (isOwner) {
                                    NavHostFragment.findNavController(CarDetailFragment.this).navigate(R.id.recognizePlateFragment, bundle, navBuilder.build());
                                } else {
                                    // searchCustomerDialog();
                                    NavHostFragment.findNavController(CarDetailFragment.this).navigate(R.id.searchCustomerFragment, bundle, navBuilder.build());
                                }*/
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

    public void addFuelDialog() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_fuel);
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.show();

        Button btn1 = dialog.findViewById(R.id.btn_one);
        Button btn2 = dialog.findViewById(R.id.btn_two);
        Button btn3 = dialog.findViewById(R.id.btn_three);
        Button btn4 = dialog.findViewById(R.id.btn_four);
        Button btn5 = dialog.findViewById(R.id.btn_five);
        Button btn6 = dialog.findViewById(R.id.btn_six);
        Button btn7 = dialog.findViewById(R.id.btn_seven);
        Button btn8 = dialog.findViewById(R.id.btn_eight);
        Button btn9 = dialog.findViewById(R.id.btn_nine);

        btn1.setBackgroundColor(getResources().getColor(R.color.toolbarLine));

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn1.setBackgroundColor(getResources().getColor(R.color.toolbarLine));
                btn2.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn3.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn4.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn5.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn6.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn7.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn8.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn9.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                count = 5;
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn1.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn2.setBackgroundColor(getResources().getColor(R.color.toolbarLine));
                btn3.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn4.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn5.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn6.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn7.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn8.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn9.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                count = 10;
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn1.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn2.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn3.setBackgroundColor(getResources().getColor(R.color.toolbarLine));
                btn4.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn5.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn6.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn7.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn8.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn9.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                count = 15;
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn1.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn2.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn3.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn4.setBackgroundColor(getResources().getColor(R.color.toolbarLine));
                btn5.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn6.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn7.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn8.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn9.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                count = 20;
            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn1.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn2.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn3.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn4.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn5.setBackgroundColor(getResources().getColor(R.color.toolbarLine));
                btn6.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn7.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn8.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn9.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                count = 25;
            }
        });

        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn1.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn2.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn3.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn4.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn5.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn6.setBackgroundColor(getResources().getColor(R.color.toolbarLine));
                btn7.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn8.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn9.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                count = 30;
            }
        });

        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn1.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn2.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn3.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn4.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn5.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn6.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn7.setBackgroundColor(getResources().getColor(R.color.toolbarLine));
                btn8.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn9.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                count = 35;
            }
        });

        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn1.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn2.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn3.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn4.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn5.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn6.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn7.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn8.setBackgroundColor(getResources().getColor(R.color.toolbarLine));
                btn9.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                count = 40;
            }
        });

        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn1.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn2.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn3.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn4.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn5.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn6.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn7.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn8.setBackgroundColor(getResources().getColor(R.color.mdtp_done_disabled_dark));
                btn9.setBackgroundColor(getResources().getColor(R.color.toolbarLine));
                count = 45;
            }
        });

        dialog.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalValue.fuel = String.valueOf(count);
                dialog.dismiss();
                // getCompanyInfo();
            }
        });
    }
}