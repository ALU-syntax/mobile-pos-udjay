package com.udjaya.kasirudjay.model.shiftorder;

import com.udjaya.kasirudjay.model.Outlet;
import com.udjaya.kasirudjay.model.User;

public class PattyCash {
    private int id;
    private String outlet_id;
    private String amount_awal;
    private String amount_akhir;
    private String user_id_started;
    private String user_id_ended;
    private String open;
    private String close;
    private String created_at;
    private String updated_at;
    private Outlet outlet;
    private User user_started;
    private User user_ended;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOutlet_id() {
        return outlet_id;
    }

    public void setOutlet_id(String outlet_id) {
        this.outlet_id = outlet_id;
    }

    public String getAmount_awal() {
        return amount_awal;
    }

    public void setAmount_awal(String amount_awal) {
        this.amount_awal = amount_awal;
    }

    public String getAmount_akhir() {
        return amount_akhir;
    }

    public void setAmount_akhir(String amount_akhir) {
        this.amount_akhir = amount_akhir;
    }

    public String getUser_id_started() {
        return user_id_started;
    }

    public void setUser_id_started(String user_id_started) {
        this.user_id_started = user_id_started;
    }

    public String getUser_id_ended() {
        return user_id_ended;
    }

    public void setUser_id_ended(String user_id_ended) {
        this.user_id_ended = user_id_ended;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
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

    public Outlet getOutlet() {
        return outlet;
    }

    public void setOutlet(Outlet outlet) {
        this.outlet = outlet;
    }

    public User getUser_started() {
        return user_started;
    }

    public void setUser_started(User user_started) {
        this.user_started = user_started;
    }

    public User getUser_ended() {
        return user_ended;
    }

    public void setUser_ended(User user_ended) {
        this.user_ended = user_ended;
    }
}
