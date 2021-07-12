package com.example.madcamp2;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Listgroup {
    String date;
    String time;
    String place;
    String headcount;
    String image;
    //이미지랑 날짜 추가 해야
    ArrayList<String> participants;

    public Listgroup(String date, String time, String place, String headcount, String image, ArrayList<String> participants) {
        this.date = date;
        this.time = time;
        this.place = place;
        this.headcount = headcount;
        this.image = image;
        this.participants = participants;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getHeadcount() {
        return headcount;
    }

    public void setHeadcount(String headcount) {
        this.headcount = headcount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<String> getParticipants() {return participants;}

    public void setParticipants(ArrayList<String> Participants) { this.participants = participants; }


}
