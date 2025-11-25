package com.udjaya.kasirudjay.model;

public class Tax {
    private int id;
    private String name;
    private String amount;
    private String satuan;
    private int outlet_id;

    public Tax() {
    }

    public Tax(int id, String name, String amount, String satuan) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.satuan = satuan;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

}
