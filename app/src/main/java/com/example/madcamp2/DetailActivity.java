package com.example.madcamp2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.media.Image;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://192.249.18.145:80";

    private Listgroup listgroup;
    private GoogleMap mMap;
    private Geocoder geocoder;

    TextView marketname;
    int position;
    String nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView marketimg = (ImageView) findViewById(R.id.marketimg);
        TextView date = (TextView) findViewById(R.id.date);
        marketname = (TextView) findViewById(R.id.marketname);
        TextView time = (TextView) findViewById(R.id.time);
        ImageButton chat = (ImageButton) findViewById(R.id.chat);
        ImageButton money = (ImageButton) findViewById(R.id.money);
        Button leavegroup = (Button) findViewById(R.id.leave_group);

        Intent i = getIntent();
        position = i.getExtras().getInt("id");
        nickname = i.getExtras().getString("nickname");

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        Call<ArrayList<Listgroup>> callgroupget = retrofitInterface.executeGroupGet();
        callgroupget.enqueue(new Callback<ArrayList<Listgroup>>() {
            @Override
            public void onResponse(Call<ArrayList<Listgroup>> callgroupget, Response<ArrayList<Listgroup>> response) {
                if (response.code() == 200) {
                    ArrayList<Listgroup> fromdb = response.body();
                    listgroup = fromdb.get(position);
                    time.setText(listgroup.getTime());
                    marketname.setText(listgroup.getPlace());
                    Log.d("yejieyejieyeji", "" + marketname.getText());

                    date.setText(listgroup.getDate());

                    String getimg = listgroup.getImage();
                    byte[] decodedString = Base64.decode(getimg, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    marketimg.setImageBitmap(decodedByte);

                    // google map
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(DetailActivity.this::onMapReady);
                } else {
                    Toast.makeText(DetailActivity.this, "test", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Listgroup>> callgroupget, Throwable t) {
                Toast.makeText(DetailActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 10);

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ChatActivity.class);
                i.putExtra("name", nickname);
                startActivity(i);
            }
        });

        money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MoneyActivity.class);
                //headcount 넘기면 될듯
                startActivity(i);
            }
        });

        leavegroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> map = new HashMap<>();
                map.put("nickname", nickname);
                map.put("place", listgroup.getPlace());
                map.put("time", listgroup.getTime());
                map.put("headcount", listgroup.getHeadcount());
                map.put("date", listgroup.getDate());
                retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
                retrofitInterface = retrofit.create(RetrofitInterface.class);
                Call<Void> callaccountdelete = retrofitInterface.executeAccountDelete(map);
                callaccountdelete.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> callaccountdelete, Response<Void> response) {
                        if (response.code() == 200) {
                            Toast.makeText(DetailActivity.this, "group out", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(DetailActivity.this, "test12", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> callgroupget, Throwable t) {
                        Toast.makeText(DetailActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
                finish();
            }
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        geocoder = new Geocoder(DetailActivity.this);

        List<Address> addressList = null;
        final String[] str = new String[1];

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        Call<ArrayList<Listgroup>> callgroupget = retrofitInterface.executeGroupGet();
        callgroupget.enqueue(new Callback<ArrayList<Listgroup>>() {
            @Override
            public void onResponse(Call<ArrayList<Listgroup>> callgroupget, Response<ArrayList<Listgroup>> response) {
                if (response.code() == 200) {
                    ArrayList<Listgroup> fromdb = response.body();
                    Listgroup listgroup = fromdb.get(position);
                    str[0] = listgroup.getPlace();
                } else {
                    Toast.makeText(DetailActivity.this, "test", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Listgroup>> callgroupget, Throwable t) {
                Toast.makeText(DetailActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        try {
            // editText에 입력한 텍스트(주소, 지역, 장소 등)을 지오 코딩을 이용해 변환
            addressList = geocoder.getFromLocationName(
                    marketname.getText().toString(), // 주소
                    10); // 최대 검색 결과 개수
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(addressList.get(0).toString());
        // 콤마를 기준으로 split
        String[] splitStr = addressList.get(0).toString().split(",");
        String address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1, splitStr[0].length() - 2); // 주소
        System.out.println(address);

        String latitude = splitStr[10].substring(splitStr[10].indexOf("=") + 1); // 위도
        String longitude = splitStr[12].substring(splitStr[12].indexOf("=") + 1); // 경도
        System.out.println(latitude);
        System.out.println(longitude);

        // 좌표(위도, 경도) 생성
        LatLng point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        // 마커 생성
        MarkerOptions mOptions2 = new MarkerOptions();
        mOptions2.title("search result");
        mOptions2.snippet(address);
        mOptions2.position(point);
        // 마커 추가
        mMap.addMarker(mOptions2);
        // 해당 좌표로 화면 줌
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 15));
    }


//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions()
//                .position(sydney)
//                .title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//
//    }
}