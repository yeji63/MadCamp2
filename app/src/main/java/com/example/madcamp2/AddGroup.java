package com.example.madcamp2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
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

import com.bumptech.glide.load.engine.bitmap_recycle.ByteArrayAdapter;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.ByteArrayOutputStream;
import java.sql.Time;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.google.android.libraries.places.api.model.Place.*;

public class AddGroup extends AppCompatActivity {
    private static final String TAG = "google map log tag ::::";
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
    private int gallerypick;

    Uri selectedImageUri;

    private static int AUTOCOMPLETE_REQUEST_CODE = 63;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_group);

        Places.initialize(getApplicationContext(), "AIzaSyD5Z1N-Vq1ONJPxkSBSoPPpp4VgOAkT9zQ");
        PlacesClient placesClient = Places.createClient(this);

        gallerypick = 0;

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
//                if (selectedImageUri == null){
//                    btn_gallery.setImageResource(R.drawable.dish);
//                }

                retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
                retrofitInterface = retrofit.create(RetrofitInterface.class);
                HashMap<String, String> map = new HashMap<>();

                if(gallerypick == 0) {
                    btn_gallery.setImageResource(R.drawable.chat);
                }

                BitmapDrawable drawable = (BitmapDrawable) btn_gallery.getDrawable();
                Bitmap image_bitmap = drawable.getBitmap();
                Bitmap resizebitmap = Bitmap.createScaledBitmap(image_bitmap, 128, 128, true);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                resizebitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
                byte[] res = byteArrayOutputStream.toByteArray();
                String imgstring = Base64.encodeToString(res, Base64.NO_WRAP);

                //date fix
                map.put("date", et_date.getText().toString());
                map.put("time", et_time.getText().toString());
                map.put("place", et_place.getText().toString());
                map.put("headcount", et_headcount.getText().toString());
                map.put("image", imgstring);
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

        et_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Field> fields = Arrays.asList(Field.ID, Field.NAME, Field.ADDRESS, Field.LAT_LNG);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).setCountry("KR")
                        .build(getApplicationContext());
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
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
            selectedImageUri = data.getData();
            btn_gallery.setImageURI(selectedImageUri);
            gallerypick = 1;
        }

        //place edittext
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                et_place.setText(place.getName());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
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

//    public void startAutocompleteActivity(View view) {
//        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, //overlay랑 fullscreen 선택
//                Arrays.asList(Field.ID, Field.NAME))
//                .setCountry("KR")
//                .build(this);
//                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
//    }

}
