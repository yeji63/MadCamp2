package com.example.madcamp2;

public class Listgroup {
    String place;
    String time;
    //이미지랑 날짜 추가 해야

    public Listgroup(String place, String time) {
        this.place = place;
        this.time = time;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
