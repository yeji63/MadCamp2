package com.example.madcamp2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.sql.Time;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddGroup extends AppCompatActivity {
    private Context mCon;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://192.249.18.145:80";
    private final int IMAGE_PICK_GALLERY_CODE = 200;
    private ImageView btn_gallery;
    private int meet_hour, meet_minute;
    private EditText et_date;
    private EditText et_time;
    private EditText et_place;
    private EditText et_headcount;
    private Button btn_cancel;
    private Button btn_submit;


//    public AddGroup(Context context) {
//        this.mCon = context;
//    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_group);

        et_date = findViewById(R.id.date);
        et_time = findViewById(R.id.et_time);
        et_place = findViewById(R.id.et_place);
        et_headcount = findViewById(R.id.et_headcount);
        btn_cancel = findViewById(R.id.bt_link_cancel);
        btn_submit = findViewById(R.id.bt_link_submit);
        btn_gallery = findViewById(R.id.profileIv);

        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGalleryIntent();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {finish();}
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
                retrofitInterface = retrofit.create(RetrofitInterface.class);
                HashMap<String, String> map = new HashMap<>();

                //date fix
                map.put("date", et_date.getText().toString());
                map.put("time", et_time.getText().toString());
                map.put("place", et_place.getText().toString());
                map.put("headcount", et_headcount.getText().toString());
                Call<Void> call = retrofitInterface.executeGroupAdd(map);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code() == 200) {
                            Toast.makeText(AddGroup.this, "add group successfully", Toast.LENGTH_LONG).show();
                        } else if (response.code() == 400) {
                            Toast.makeText(AddGroup.this, "Already registered", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(AddGroup.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
                finish();
            }
        });

    }

    private void openGalleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent. setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICK_GALLERY_CODE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            btn_gallery.setImageURI(selectedImageUri);
        }
    }


    public void popTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                meet_hour = hourOfDay;
                meet_minute = minute;
                et_time.setText(String.format(Locale.getDefault(), "%02d:%02d", meet_hour, meet_minute));
            }
        };
        int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(AddGroup.this, style, onTimeSetListener, meet_hour, meet_minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }
}
