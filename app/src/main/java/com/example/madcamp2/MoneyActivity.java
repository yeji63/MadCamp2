package com.example.madcamp2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MoneyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);

        EditText total = (EditText) findViewById(R.id.total);
        EditText heads = (EditText) findViewById(R.id.heads);
        ImageView calculate = (ImageView) findViewById(R.id.arrow);
        TextView one = (TextView) findViewById(R.id.one);

        //heads.setText(); headcount 받아서 set해주면 될듯

        int totalwon = Integer.parseInt(total.getText().toString());
        int totalhead = Integer.parseInt(heads.getText().toString());

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cal = totalwon/totalhead;
                one.setText(""+cal);
            }
        });
    }
}