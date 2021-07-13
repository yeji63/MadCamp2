package com.example.madcamp2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ParticipantsAdapter extends BaseAdapter {

    private ArrayList<ListviewItem> itemlist = new ArrayList<ListviewItem>();

    public ParticipantsAdapter(){

    }

    @Override
    public int getCount() {
        return itemlist.size();
    }

    @Override
    public Object getItem(int position) {
        return itemlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.participant_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView member_img = (ImageView) convertView.findViewById(R.id.member_img) ;
        TextView member_name = (TextView) convertView.findViewById(R.id.member_name) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListviewItem item = itemlist.get(position);

        // 아이템 내 각 위젯에 데이터 반영

        //set이미지인데, string이라 뭐로해야할지
        //member_img.setImage(item.getMember_img());
        Glide.with(context).load(item.getMember_img());
        member_name.setText(item.getMember_name());

        return convertView;
    }
}
