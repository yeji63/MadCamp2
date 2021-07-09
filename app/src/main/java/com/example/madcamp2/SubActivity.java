package com.example.madcamp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SubActivity extends AppCompatActivity
{
    private String strNick, strProfileImg, strEmail;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://192.249.18.145:80";
    private static Context mCon;

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

        // 닉네임 set
        tv_nick.setText(strNick);
        // 프로필 이미지 사진 set
        Glide.with(this).load(strProfileImg).into(iv_profile);

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("nickname", strNick);
        Call<Void> call = retrofitInterface.executeSignup(map);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    Toast.makeText(SubActivity.this,
                            "Signed up successfully", Toast.LENGTH_LONG).show();
                } else if (response.code() == 400) {
                    Toast.makeText(SubActivity.this,
                            "Already registered", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(SubActivity.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });


        //gridview
        GridView gridView = findViewById(R.id.gridView);
        GridAdapter adapter = new GridAdapter();

        adapter.addGroup(new Listgroup("우츠", "18:00"));
        gridView.setAdapter(adapter);

        Button addbutton = findViewById(R.id.btn_add);
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddGroup dialog = new AddGroup(mCon, adapter);
                dialog.show(getSupportFragmentManager(), "todoDialog");
            }
        });



        // 로그아웃
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
}