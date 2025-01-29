package com.udjaya.kasirudjay.model;

import android.content.ClipData;

import java.util.List;

public class OpenBill {
    private int id;
    private String name;
    private int user_id;
    private int outlet_id;
    private int queue_order;
    private String created_at;
    private String updated_at;
    private String deleted_at;
    private List<ItemOpenBill> item;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getOutlet_id() {
        return outlet_id;
    }

    public void setOutlet_id(int outlet_id) {
        this.outlet_id = outlet_id;
    }

    public int getQueue_order() {
        return queue_order;
    }

    public void setQueue_order(int queue_order) {
        this.queue_order = queue_order;
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

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public List<ItemOpenBill> getItem() {
        return item;
    }

    public void setItem(List<ItemOpenBill> item) {
        this.item = item;
    }
}
