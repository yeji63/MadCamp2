package com.example.madcamp2;

import java.util.ArrayList;

public class ChatResult {
    ArrayList<String> nickname;
    ArrayList<String> message;

    public ArrayList<String> getMessage() {
        return message;
    }

    public ArrayList<String> getNickname() {
        return nickname;
    }

    public void setMessage(ArrayList<String> message) {
        this.message = message;
    }

    public void setNickname(ArrayList<String> nickname) {
        this.nickname = nickname;
    }
}
