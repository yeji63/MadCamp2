package com.example.madcamp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SubActivity extends AppCompatActivity
{
    private String strNick, strProfileImg;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://192.249.18.145:80";
    private static Context mCon;
    private GridAdapter adapter;
    private ListView listView;
    private Button enter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        mCon = getApplicationContext();
        Intent intent = getIntent();
        strNick = intent.getStringExtra("name");
        strProfileImg = intent.getStringExtra("profileImg");

        TextView tv_nick = findViewById(R.id.tv_nickName);
        ImageView iv_profile = findViewById(R.id.iv_profile);

        enter = (Button) findViewById(R.id.enter);

        // 닉네임 set
        tv_nick.setText(strNick);
        // 프로필 이미지 사진 set
        Glide.with(this).load(strProfileImg).into(iv_profile);

        //check account
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("nickname", strNick);
        Call<Void> callsignup = retrofitInterface.executeSignup(map);
        callsignup.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> callsignup, Response<Void> response) {
                if (response.code() == 200) {
                    Toast.makeText(SubActivity.this, "Signed up successfully", Toast.LENGTH_LONG).show();
                } else if (response.code() == 400) {
                    Toast.makeText(SubActivity.this, "Already registered", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Void> callsignup, Throwable t) {
                Toast.makeText(SubActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        listView = findViewById(R.id.listView);

        //gridview(init)
        AddGroup();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), DetailActivity.class);
                i.putExtra("id", position);
                startActivity(i);
            }
        });


        //add group dialog
        Button addbutton = findViewById(R.id.btn_add);
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(getApplicationContext(), AddGroup.class);
                i.putExtra("account nickname", strNick);
                startActivity(i);
            }
        });

        // logout
        findViewById(R.id.btn_logout).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                UserManagement.getInstance().requestLogout(new LogoutResponseCallback()
                {
                    @Override
                    public void onCompleteLogout()
                    {
                        // 로그아웃 성공시 수행하는 지점
                        finish(); // 현재 액티비티 종료
                    }
                });
            }
        });
    }

    private void AddGroup() {
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        Call<ArrayList<Listgroup>> callgroupget = retrofitInterface.executeGroupGet();
        callgroupget.enqueue(new Callback<ArrayList<Listgroup>>() {
            @Override
            public void onResponse(Call<ArrayList<Listgroup>> callgroupget, Response<ArrayList<Listgroup>> response) {
                if (response.code() == 200) {
                    ArrayList<Listgroup> fromdb = response.body();
                    //gridview
                    adapter = new GridAdapter(fromdb);
                    listView.setAdapter(adapter);
                }
                else {
                    Toast.makeText(SubActivity.this, "test", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Listgroup>> callgroupget, Throwable t) {
                Toast.makeText(SubActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        AddGroup();
    }

}