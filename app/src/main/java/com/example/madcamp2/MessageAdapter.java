package com.example.madcamp2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter {
    private static final int TYPE_MESSAGE_SENT = 0;
    private static final int TYPE_MESSAGE_RECEIVED = 1;

    private LayoutInflater inflater;
    private ArrayList<ArrayList<String>> messages = new ArrayList<>();

    public MessageAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {

        TextView messageTxt;

        public SentMessageHolder(@NonNull View itemView) {
            super(itemView);

            messageTxt = itemView.findViewById(R.id.sentTxt);
        }
    }


    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {

        TextView nameTxt, messageTxt;

        public ReceivedMessageHolder(@NonNull View itemView) {
            super(itemView);

            nameTxt = itemView.findViewById(R.id.nameTxt);
            messageTxt = itemView.findViewById(R.id.receivedTxt);
        }
    }

    @Override
    public int getItemViewType(int position) {

        //JSONObject message = messages.get(position);
        ArrayList<String> message = messages.get(position);
        if (message.get(0).equals("send")) {
            if (message.get(1).equals("msg"))
                return TYPE_MESSAGE_SENT;
        }
        else {
            if (message.get(1).equals("msg"))
                return TYPE_MESSAGE_RECEIVED;
        }
        return -1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;

        switch (viewType) {
            case TYPE_MESSAGE_SENT:
                view = inflater.inflate(R.layout.item_sent_message, parent, false);
                return new SentMessageHolder(view);
            case TYPE_MESSAGE_RECEIVED:

                view = inflater.inflate(R.layout.item_received_message, parent, false);
                return new ReceivedMessageHolder(view);

        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ArrayList<String> message = messages.get(position);

        if (message.get(0).equals("send")) {

            if (message.get(1).equals("msg")) {
                SentMessageHolder messageHolder = (SentMessageHolder) holder;
                messageHolder.messageTxt.setText(message.get(4));
            }

        } else {
            if (message.get(1).equals("msg")) {
                ReceivedMessageHolder messageHolder = (ReceivedMessageHolder) holder;
                messageHolder.nameTxt.setText(message.get(3));
                messageHolder.messageTxt.setText(message.get(4));
            }
        }
    }


    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void addItem (ArrayList<String> strlist) {
        messages.add(strlist);
        notifyDataSetChanged();
    }

//    public void addItem (JSONObject jsonObject) {
//        messages.add(jsonObject);
//        notifyDataSetChanged();
//    }
}
