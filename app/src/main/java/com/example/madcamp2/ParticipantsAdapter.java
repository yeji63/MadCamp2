package com.example.madcamp2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ParticipantsAdapter extends BaseAdapter {

    private ArrayList<ListviewItem> users = new ArrayList<ListviewItem>();
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://192.249.18.145:80";
    private ListviewItem fromdb;

    public ParticipantsAdapter(ArrayList<ListviewItem> users){
        this.users = users;

    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final int pos = position;
        final Context context = parent.getContext();
        ListviewItem user = users.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.participant_item, parent, false);
        }

        ImageView member_img = (ImageView) convertView.findViewById(R.id.member_img);
        TextView member_name = (TextView) convertView.findViewById(R.id.member_name);

        Glide.with(context).load(user.getMember_img()).into(member_img);
        member_name.setText(user.getMember_name());

        
        return convertView;
    }
}
