package com.example.sino.view.fragment.car;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.sino.R;
import com.example.sino.databinding.FragmentSearchCarBinding;

public class SearchCarFragment extends Fragment {

    private FragmentSearchCarBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_car, container, false);

        autoSelectText();

        return binding.getRoot();
    }


    public void showDialogWords() {
        final String[] strName = new String[1];
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item);
        arrayAdapter.add("الف");
        arrayAdapter.add("ج");
        arrayAdapter.add("ع");
        arrayAdapter.add("ب");
        arrayAdapter.add("ی");
        arrayAdapter.add("ل");
        arrayAdapter.add("ا");
        arrayAdapter.add("ت");
        arrayAdapter.add("ن");
        arrayAdapter.add("م");
        arrayAdapter.add("ه");
        arrayAdapter.add("ص");
        arrayAdapter.add("ط");


        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                strName[0] = arrayAdapter.getItem(which);
                System.out.println("strName=====" + strName[0]);
                binding.includeLinearLayout.txtThree.setText(strName[0]);
                binding.includeLinearLayout.txtFour.requestFocus();
            }
        });
        builderSingle.show();
    }

    public void autoSelectText(){
        binding.includeLinearLayout.txtOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.includeLinearLayout.txtOne.getText().length() == 2) {
                    binding.includeLinearLayout.txtTwo.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.includeLinearLayout.txtTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.includeLinearLayout.txtTwo.getText().length() == 3) {
                    binding.includeLinearLayout.txtThree.performClick();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.includeLinearLayout.txtThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogWords();
            }
        });
    }
}