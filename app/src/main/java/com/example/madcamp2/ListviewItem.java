package com.example.madcamp2;

public class ListviewItem {
    private String nickname;
    private String profileimg;

    public ListviewItem(String nickname, String profileimg) {
        this.profileimg = profileimg;
        this.nickname = nickname;
    }

    public String getMember_img() {
        return profileimg;
    }

    public String getMember_name() {
        return nickname;
    }

    public void setMember_img(String profileimg) {
        this.profileimg = profileimg;
    }

    public void setMember_name(String nickname) {
        this.nickname = nickname;
    }
}
