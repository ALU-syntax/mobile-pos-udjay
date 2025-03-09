package com.udjaya.kasirudjay.model.shiftorder;

import com.udjaya.kasirudjay.model.Product;

import java.util.List;

public class DataProductTransaction {
    private int id;
    private String name;
    private int harga;
    private int stok;
    private int product_id;
    private String created_at;
    private String updated_at;
    private String deleted_at;
    private int total_transaction;
    private String total_transaction_amount;
    private List<ItemTransaction> item_transaction;
    private Product product;

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

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
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

    public List<ItemTransaction> getItem_transaction() {
        return item_transaction;
    }

    public void setItem_transaction(List<ItemTransaction> item_transaction) {
        this.item_transaction = item_transaction;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
