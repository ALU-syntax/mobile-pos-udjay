package com.udjaya.kasirudjay.model;

public class ModifierOpenBill {
    private int id;
    private String nama;
    private int harga;
    private String tmpIdProduct;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return nama;
    }

    public void setName(String name) {
        this.nama = name;
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
