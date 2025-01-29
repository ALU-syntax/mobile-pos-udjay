package com.udjaya.kasirudjay.model;

public class ModifierOpenBill {
    private int id;
    private String name;
    private int harga;
    private String tmpIdProduct;

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

    public String getTmpIdProduct() {
        return tmpIdProduct;
    }

    public void setTmpIdProduct(String tmpIdProduct) {
        this.tmpIdProduct = tmpIdProduct;
    }
}
