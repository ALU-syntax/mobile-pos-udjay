package com.udjaya.kasirudjay.model;

public class Product {
    private int id;
    private String name;
    private String category_id;
    private String status;
    private String photo;
    private String harga_modal;
    private String deleted_at;
    private String outlet_id;
    private String creted_at;
    private String updated_at;

    public Product() {
    }

    public Product(int id, String name, String category_id, String status, String photo, String harga_modal, String deleted_at, String outlet_id, String creted_at, String updated_at) {
        this.id = id;
        this.name = name;
        this.category_id = category_id;
        this.status = status;
        this.photo = photo;
        this.harga_modal = harga_modal;
        this.deleted_at = deleted_at;
        this.outlet_id = outlet_id;
        this.creted_at = creted_at;
        this.updated_at = updated_at;
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

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getHarga_modal() {
        return harga_modal;
    }

    public void setHarga_modal(String harga_modal) {
        this.harga_modal = harga_modal;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public String getOutlet_id() {
        return outlet_id;
    }

    public void setOutlet_id(String outlet_id) {
        this.outlet_id = outlet_id;
    }

    public String getCreted_at() {
        return creted_at;
    }

    public void setCreted_at(String creted_at) {
        this.creted_at = creted_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
