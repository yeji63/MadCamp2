package com.example.madcamp2;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class AddGroup extends DialogFragment {
    private Context mCon;
    private GridAdapter adapter;

    public AddGroup(Context context, GridAdapter adapter) {
        this.adapter = adapter;
        this.mCon = context;
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @org.jetbrains.annotations.NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.add_group, container, false);


        EditText et_date = rootView.findViewById(R.id.et_link);
        EditText et_todo = rootView.findViewById(R.id.et_todo);
        Button btn_cancel = rootView.findViewById(R.id.bt_link_cancel);
        Button btn_submit = rootView.findViewById(R.id.bt_link_submit);

        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(1);
                int month = calendar.get(2);
                int day = calendar.get(5);
                DatePickerDialog.OnDateSetListener listener = (new DatePickerDialog.OnDateSetListener() {
                    public final void onDateSet(DatePicker $noName_0, int i, int i2, int i3) {
                        et_date.setText("" + i + '-' + (i2 + 1) + '-' + i3);
                    }
                });
                DatePickerDialog picker = new DatePickerDialog(mCon, listener, year, month, day);
                picker.show();

            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mCon, "Please Fill All the Data!", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
}
