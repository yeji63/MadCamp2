package com.example.madcamp2;

public class Listgroup {
    String date;
    String time;
    String place;
    String headcount;
    //이미지랑 날짜 추가 해야

    public Listgroup(String date, String time, String place, String headcount) {
        this.date = date;
        this.time = time;
        this.place = place;
        this.headcount = headcount;
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

}
