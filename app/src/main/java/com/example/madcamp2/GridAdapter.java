package com.example.madcamp2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {
    ArrayList<Listgroup> groups = new ArrayList<Listgroup>();
    Context context;

    GridAdapter(ArrayList<Listgroup> groups) {
        this.groups = groups;
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
        TextView timetext = convertView.findViewById(R.id.time);
        TextView placetext = convertView.findViewById(R.id.place);

        timetext.setText(listgroup.getTime());
        placetext.setText(listgroup.getPlace());

        return convertView;
    }


}
