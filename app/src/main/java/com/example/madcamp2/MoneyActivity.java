package com.example.madcamp2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoneyActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);

        EditText total = (EditText) findViewById(R.id.total);
        EditText heads = (EditText) findViewById(R.id.heads);
        ImageView calculate = (ImageView) findViewById(R.id.arrow);
        TextView one = (TextView) findViewById(R.id.one);
        int participants = getIntent().getIntExtra("participants", 0);


        heads.setText(Integer.toString(participants));
        //현재 참여자 수 받기



        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(total.getText().toString().matches("")) {
                    Toast.makeText(MoneyActivity.this,"Put Total Price", Toast.LENGTH_SHORT).show();
                }
                else {
                    int totalwon = Integer.parseInt(total.getText().toString());
                    int totalhead = Integer.parseInt(heads.getText().toString());
                    int cal = totalwon/totalhead;
                    one.setText(NumberFormat.getInstance().format(cal));
                }
            }
        });
    }
}