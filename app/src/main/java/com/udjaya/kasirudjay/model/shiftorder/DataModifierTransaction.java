package com.udjaya.kasirudjay.model.shiftorder;

import java.util.List;

public class DataModifierTransaction {
    private int id;
    private String name;
    private int harga;
    private String stok;
    private int modifiers_group_id;
    private String created_at;
    private String updated_at;
    private String deleted_at;
    private List<ItemTransaction> item_transactions;
    private int total_transaction;
    private String total_transaction_amount;

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

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public String getStok() {
        return stok;
    }

    public void setStok(String stok) {
        this.stok = stok;
    }

    public int getModifiers_group_id() {
        return modifiers_group_id;
    }

    public void setModifiers_group_id(int modifiers_group_id) {
        this.modifiers_group_id = modifiers_group_id;
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

    public List<ItemTransaction> getItem_transactions() {
        return item_transactions;
    }

    public void setItem_transactions(List<ItemTransaction> item_transactions) {
        this.item_transactions = item_transactions;
    }

    public int getTotal_transaction() {
        return total_transaction;
    }

    public void setTotal_transaction(int total_transaction) {
        this.total_transaction = total_transaction;
    }

    public String getTotal_transaction_amount() {
        return total_transaction_amount;
    }

    public void setTotal_transaction_amount(String total_transaction_amount) {
        this.total_transaction_amount = total_transaction_amount;
    }
}
