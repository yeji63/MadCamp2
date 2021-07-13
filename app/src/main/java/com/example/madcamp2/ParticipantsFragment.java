package com.example.madcamp2;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.NotNull;

public class ParticipantsFragment extends DialogFragment {
    private Context mCon;
    private Listgroup listgroup;

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

        ListView listview;
        ParticipantsAdapter adapter;

        adapter = new ParticipantsAdapter();

        listview = (ListView) rootView.findViewById(R.id.member_list);
        listview.setAdapter(adapter);

        return rootView;
    }
}
