package com.udjaya.kasirudjay.model;

public class Outlet {
    private int id;
    private String name;
    private String address;
    private String phone;
    private String created_at;
    private String updated_at;
    private User user_started;
    private String catatan_nota;

    public Outlet() {
    }

    public Outlet(int id, String name, String address, String phone, String created_at, String updated_at, User user_started, String catatan_nota) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.user_started = user_started;
        this.catatan_nota = catatan_nota;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public User getUser_started() {
        return user_started;
    }

    public void setUser_started(User user_started) {
        this.user_started = user_started;
    }

    public String getCatatan_nota() {
        return catatan_nota;
    }

    public void setCatatan_nota(String catatan_nota) {
        this.catatan_nota = catatan_nota;
    }
}
