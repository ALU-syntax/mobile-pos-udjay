package com.udjaya.kasirudjay.model.shiftorder;

import com.udjaya.kasirudjay.model.Transactions;

import java.util.List;

public class Payment {
    private int id;
    private String name;
    private int status;
    private int category_payment_id;
    private String deleted_at;
    private String created_at;
    private String updated_at;
    private List<Transactions> transactions;

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCategory_payment_id() {
        return category_payment_id;
    }

    public void setCategory_payment_id(int category_payment_id) {
        this.category_payment_id = category_payment_id;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
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

    public List<Transactions> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transactions> transactions) {
        this.transactions = transactions;
    }
}
