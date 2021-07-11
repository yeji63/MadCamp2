package com.example.madcamp2;

public class Listgroup {
    String date;
    String time;
    String place;
    String headcount;
    String image;
    //이미지랑 날짜 추가 해야

    public Listgroup(String date, String time, String place, String headcount, String image) {
        this.date = date;
        this.time = time;
        this.place = place;
        this.headcount = headcount;
        this.image = image;
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

}
