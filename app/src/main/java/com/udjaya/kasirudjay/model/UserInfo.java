package com.udjaya.kasirudjay.model;

public class UserInfo {
    private String user_id;
    private String username;

    public UserInfo(String id, String name) {
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
