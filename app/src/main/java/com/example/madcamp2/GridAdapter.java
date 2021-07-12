package com.example.madcamp2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GridAdapter extends BaseAdapter {
    ArrayList<Listgroup> groups = new ArrayList<Listgroup>();
    String accountnickname;
    Context context;
    Retrofit retrofit;
    RetrofitInterface retrofitInterface;
    String BASE_URL = "http://192.249.18.145:80";

    GridAdapter(ArrayList<Listgroup> groups, String accountnickname) {
        this.groups = groups;
        this.accountnickname = accountnickname;
    }

    public void addGroup(Listgroup group) {
        groups.add(group);
    }

    @Override
    public int getCount() {
        return groups.size();
    }

    @Override
    public Object getItem(int position) {
        return groups.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        context = parent.getContext();
        Listgroup listgroup = groups.get(position);


        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.group_card, parent, false);
        }
        TextView datetext = convertView.findViewById(R.id.date);
        TextView timetext = convertView.findViewById(R.id.time);
        TextView placetext = convertView.findViewById(R.id.place);
        TextView headcounttext = convertView.findViewById(R.id.headcount);
        CircleImageView imageview = convertView.findViewById(R.id.circle_img);
        TextView now = convertView.findViewById(R.id.now);
        Button enter = convertView.findViewById(R.id.enter);

        datetext.setText(listgroup.getDate());
        timetext.setText(listgroup.getTime());
        placetext.setText(listgroup.getPlace());
        headcounttext.setText(listgroup.getHeadcount());
        now.setText(Integer.toString(listgroup.getParticipants().size()));


        String getimg= listgroup.getImage();
        byte[] decodedString = Base64.decode(getimg, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imageview.setImageBitmap(decodedByte);


        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
                retrofitInterface = retrofit.create(RetrofitInterface.class);
                HashMap<String, String> map = new HashMap<>();
                map.put("nickname", accountnickname);
                map.put("place", listgroup.getPlace());
                Call<Void> callaccountadd = retrofitInterface.executeAccountAdd(map);
                callaccountadd.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> callgroupget, Response<Void> response) {
                        if (response.code() == 200) {
                            Toast.makeText(context, "add participants successfully", Toast.LENGTH_LONG).show();
                        } else if (response.code() == 400) {
                            Toast.makeText(context, "already participated", Toast.LENGTH_LONG).show();
                        }


                        Intent i = new Intent(context, DetailActivity.class);
                        i.putExtra("id", position);
                        context.startActivity(i);
                    }
                    @Override
                    public void onFailure(Call<Void> callgroupget, Throwable t) {
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


        return convertView;
    }

}
