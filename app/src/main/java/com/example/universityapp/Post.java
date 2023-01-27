package com.example.universityapp;

public class Post {

    String postname , postdesc , mImageurl ;

    public Post(String postname, String postdesc, String mImageurl) {
        this.postname = postname;
        this.postdesc = postdesc;
        this.mImageurl = mImageurl;
    }

    public Post() {
    }

    public String getPostname() {
        return postname;
    }

    public void setPostname(String postname) {
        this.postname = postname;
    }

    public String getPostdesc() {
        return postdesc;
    }

    public void setPostdesc(String postdesc) {
        this.postdesc = postdesc;
    }

    public String getmImageurl() {
        return mImageurl;
    }

    public void setmImageurl(String mImageurl) {
        this.mImageurl = mImageurl;
    }
}
