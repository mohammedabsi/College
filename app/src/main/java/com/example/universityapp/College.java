package com.example.universityapp;

public class College {

    String college , club ;

    public College(String college, String club) {
        this.college = college;
        this.club = club;
    }

    public College() {
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }
}
