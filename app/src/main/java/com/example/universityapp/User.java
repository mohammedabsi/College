package com.example.universityapp;

public class User {
    public String userName, email, phone, password , college ,category;



    public User() {

    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public User(String username, String email, String phone, String password,String college, String category ) {

        this.userName = username;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.college = college;
        this.category = category;



    }

    public User(String email, String password, String college, String category) {
        this.email = email;
        this.password = password;
        this.college = college;
        this.category = category;
    }

    public String getPhone() {
        return phone;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }



    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userId) {
        this.userName = userId;
    }


}
