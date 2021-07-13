package com.example.madcamp2;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatActivity extends AppCompatActivity implements TextWatcher {

    private Socket mSocket;
    String roomname;
    String date;
    String market;
    private String nickname;
    private String SERVER_PATH = "ws://192.249.18.145:443";
    private EditText messageEdit;
    private View sendBtn;
    private RecyclerView recyclerView;
//    private int IMAGE_REQUEST_ID = 1;
    private MessageAdapter messageAdapter;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://192.249.18.145:80";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        date=getIntent().getStringExtra("date");
        market=getIntent().getStringExtra("market");
        roomname = date + " / "+ market;

        nickname = getIntent().getStringExtra("name");

        recyclerView = findViewById(R.id.recyclerView);
        messageEdit = findViewById(R.id.messageEdit);
        sendBtn = findViewById(R.id.sendBtn);


        try {
            //connect to socket
            mSocket = IO.socket("http://192.249.18.145:80");
            mSocket.connect();
            System.out.println("asdasd");

            initializeView(roomname);

            //join room
            mSocket.emit("join_chat", nickname, roomname);

            //if joined, get notification
            mSocket.on("user_join_notifi", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String msg = (String) args[0];
                            Toast.makeText(ChatActivity.this, msg, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });

            //when click send button, send new message to server
            sendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //send data to server through socket io
                    String new_message = messageEdit.getText().toString();
                    mSocket.emit("new_message", roomname, nickname, new_message);

                    ArrayList<String> send = new ArrayList<>();
                    send.add("send"); send.add("msg"); send.add(roomname); send.add(nickname); send.add(new_message);

                    //store chat to database
                    store_chat(send);

                    //visualize
                    messageAdapter.addItem(send);
                    recyclerView.smoothScrollToPosition(messageAdapter.getItemCount()-1);
                    messageEdit.setText("");
                }
            });

            //if received new message
            mSocket.on("updatechat", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //get data from server through socket io
                            JSONObject data = (JSONObject) args[0];
                            String roominfo = null;
                            try {
                                roominfo = data.getString("roomname");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            String userinfo = null;
                            try {
                                userinfo = data.getString("nickname");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            String message = null;
                            try {
                                message = data.getString("message");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            //should not be user's own chat
                            if(!userinfo.equals(nickname)){
                                ArrayList<String> send = new ArrayList<>();
                                send.add("receive"); send.add("msg"); send.add(roominfo); send.add(userinfo); send.add(message);

                                //store chat to database
                                //show_chat(roomname);

                                //visualize
                                messageAdapter.addItem(send);
                                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount()-1);
                            }
                        }
                    });
                }
            });
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String string = s.toString().trim() ;
        if(string.isEmpty()){
            resetMessageEdit();
        } else{
            sendBtn.setVisibility(View.VISIBLE);
        }
    }

    private void resetMessageEdit() {
        messageEdit.removeTextChangedListener(this);
        messageEdit.setText("");
        sendBtn.setVisibility(View.INVISIBLE);

        messageEdit.addTextChangedListener(this);
    }


    private void initializeView(String roomname) { //여기서 데베에 쌓인 데이터 받아와서 다 출력
        messageAdapter = new MessageAdapter(getLayoutInflater());
        recyclerView.setAdapter(messageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        show_chat(roomname);

//        messageEdit.addTextChangedListener(this);
//
//        sendBtn.setOnClickListener(v-> {
//            JSONObject jsonObject = new JSONObject();
//            try {
//                jsonObject.put("name", name);
//                jsonObject.put("message", messageEdit.getText().toString());
//
//                webSocket.send(jsonObject.toString());
//
//                jsonObject.put("isSent", true);
//                messageAdapter.addItem(jsonObject);
//
//                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
//
//                resetMessageEdit();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        });

    }

    private void show_chat(String roomname){
        HashMap<String, String> map = new HashMap<>();

        map.put("roomname", roomname);

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        Call<ChatResult> call = retrofitInterface.showChat(map);

        call.enqueue(new Callback<ChatResult>() {
            @Override
            public void onResponse(Call<ChatResult> call, retrofit2.Response<ChatResult> response) {

                if (response.code() == 200) {

                    ChatResult result = response.body();

                    ArrayList<String> db_nickname = result.getNickname();
                    ArrayList<String> message = result.getMessage();

                    for(int i=0; i<db_nickname.size(); i++){
                        ArrayList<String> data = new ArrayList<>();
                        if( nickname.equals(db_nickname.get(i)) ) {
                            data.add("send");
                            data.add("msg");
                            data.add(roomname);
                            data.add(db_nickname.get(i));
                            data.add(message.get(i));
                            messageAdapter.addItem(data);
                            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount()-1);
                        }
                        else {
                            data.add("receive");
                            data.add("msg");
                            data.add(roomname);
                            data.add(db_nickname.get(i));
                            data.add(message.get(i));
                            messageAdapter.addItem(data);
                            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount()-1);
                        }
                    }

                } else if (response.code() == 400) {
                    Toast.makeText(getApplicationContext(), "Let's start our first chat!", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<ChatResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void store_chat(List<String> data){
        HashMap<String, List<String>> map = new HashMap<>();

        ArrayList<String> arrayList1 = new ArrayList<>();
        arrayList1.add(data.get(2));
        map.put("roomname", arrayList1);

        ArrayList<String> arrayList2 = new ArrayList<>();
        arrayList2.add(data.get(3));
        map.put("nickname", arrayList2);

        ArrayList<String> arrayList3 = new ArrayList<>();
        arrayList3.add(data.get(4));
        map.put("message", arrayList3);

        Call<Void> call = retrofitInterface.storeChat(map);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                if (response.code() == 200) { }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode == IMAGE_REQUEST_ID && resultCode == RESULT_OK){
//            try {
//                InputStream is =getContentResolver().openInputStream(data.getData());
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}