package com.example.madcamp2;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ParticipantsFragment extends DialogFragment {
    private Context mCon;
    private Listgroup listgroup;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://192.249.18.145:80";
    private ArrayList<ListviewItem> fromdb;
    private ParticipantsAdapter adapter;
    private ListView listview;
    private ArrayList<String> participants;

    public ParticipantsFragment(Context context, Listgroup listgroup) {
        this.mCon = context;
        this.listgroup = listgroup;
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.participants, container, false);
        getDialog().setCanceledOnTouchOutside(true);
        listview = rootView.findViewById(R.id.member_list);
        participants = listgroup.getParticipants();

        HashMap<String, ArrayList<String>> map = new HashMap<>();
        map.put("nickname", participants);
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        Call<ArrayList<ListviewItem>> callgetuserimg = retrofitInterface.executeGetUserimg(map);
        callgetuserimg.enqueue(new Callback<ArrayList<ListviewItem>>() {
            @Override
            public void onResponse(Call<ArrayList<ListviewItem>> callgetuserimg, Response<ArrayList<ListviewItem>> response) {
                if (response.code() == 200) {
                    fromdb = response.body();
                    adapter = new ParticipantsAdapter(fromdb);
                    listview.setAdapter(adapter);

                }
                else {
                    Toast.makeText(mCon, "test", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<ListviewItem>> callgetuserimg, Throwable t) {
                Toast.makeText(mCon, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        return rootView;
    }
}
