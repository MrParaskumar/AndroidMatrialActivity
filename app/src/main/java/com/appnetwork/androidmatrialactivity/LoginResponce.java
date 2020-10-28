package com.appnetwork.androidmatrialactivity;

public class LoginResponce {
    private String msg;
    private Userclass userclass;

    public LoginResponce(String msg, Userclass userclass) {
        this.msg = msg;
        this.userclass = userclass;
    }

    public String getMsg() {
        return msg;
    }

    public Userclass getUserclass() {
        return userclass;
    }
}
