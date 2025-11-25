package com.udjaya.kasirudjay.model;

public class User {
    private String id;
    private String name;
    private String username;
    private String email;
    private int status;
    private int role;
    private String email_verified_at;
    private String password;
    private int deleted;
    private String outlet_id;
    private String pin;
    private String remember_token;
    private String created_at;
    private String updated_at;

    public User() {
    }

    public User(String id, String name, String username, String email, int status, int role, String email_verified_at, String password, int deleted, String outlet_id, String pin, String remember_token, String created_at, String updated_at) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.status = status;
        this.role = role;
        this.email_verified_at = email_verified_at;
        this.password = password;
        this.deleted = deleted;
        this.outlet_id = outlet_id;
        this.pin = pin;
        this.remember_token = remember_token;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getEmail_verified_at() {
        return email_verified_at;
    }

    public void setEmail_verified_at(String email_verified_at) {
        this.email_verified_at = email_verified_at;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public String getOutlet_id() {
        return outlet_id;
    }

    public void setOutlet_id(String outlet_id) {
        this.outlet_id = outlet_id;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getRemember_token() {
        return remember_token;
    }

    public void setRemember_token(String remember_token) {
        this.remember_token = remember_token;
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
}
