package com.example.madcamp2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback{

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://192.249.18.145:80";

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView marketimg = (ImageView) findViewById(R.id.marketimg);
        TextView date = (TextView) findViewById(R.id.date);
        TextView marketname = (TextView) findViewById(R.id.marketname);
        TextView time = (TextView) findViewById(R.id.time);

        Intent i = getIntent();
        int position = i.getExtras().getInt("id");

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        Call<ArrayList<Listgroup>> callgroupget = retrofitInterface.executeGroupGet();
        callgroupget.enqueue(new Callback<ArrayList<Listgroup>>() {
            @Override
            public void onResponse(Call<ArrayList<Listgroup>> callgroupget, Response<ArrayList<Listgroup>> response) {
                if (response.code() == 200) {
                    ArrayList<Listgroup> fromdb = response.body();
                    Listgroup listgroup = fromdb.get(position);
                    time.setText(listgroup.getTime());
                    marketname.setText(listgroup.getPlace());
                    date.setText(listgroup.getDate());

                }
                else {
                    Toast.makeText(DetailActivity.this, "test", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Listgroup>> callgroupget, Throwable t) {
                Toast.makeText(DetailActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        // google map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }
}