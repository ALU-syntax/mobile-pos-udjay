package com.udjaya.kasirudjay.model;

import java.util.List;

public class ItemOpenBill {
    private int id;
    private int open_bill_id;
    private String catatan;
    private String diskon;
    private int harga;
    private String product_id;
    private String variant_id;
    private List<ModifierOpenBill> modifier;
    private String nama_product;
    private String nama_variant;
    private List<PilihanOpenBill> pilihan;
    private String promo;
    private String quantity;
    private int result_total;
    private String sales_type;
    private String tmp_id;
    private int queue_order;
    private String created_at;
    private String updated_at;
    private String deleted_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOpen_bill_id() {
        return open_bill_id;
    }

    public void setOpen_bill_id(int open_bill_id) {
        this.open_bill_id = open_bill_id;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    public String getDiskon() {
        return diskon;
    }

    public void setDiskon(String diskon) {
        this.diskon = diskon;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getVariant_id() {
        return variant_id;
    }

    public void setVariant_id(String variant_id) {
        this.variant_id = variant_id;
    }

    public List<ModifierOpenBill> getModifier() {
        return modifier;
    }

    public void setModifier(List<ModifierOpenBill> modifier) {
        this.modifier = modifier;
    }

    public String getNama_product() {
        return nama_product;
    }

    public void setNama_product(String nama_product) {
        this.nama_product = nama_product;
    }

    public String getNama_variant() {
        return nama_variant;
    }

    public void setNama_variant(String nama_variant) {
        this.nama_variant = nama_variant;
    }

    public List<PilihanOpenBill> getPilihan() {
        return pilihan;
    }

    public void setPilihan(List<PilihanOpenBill> pilihan) {
        this.pilihan = pilihan;
    }

    public String getPromo() {
        return promo;
    }

    public void setPromo(String promo) {
        this.promo = promo;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public int getResult_total() {
        return result_total;
    }

    public void setResult_total(int result_total) {
        this.result_total = result_total;
    }

    public String getSales_type() {
        return sales_type;
    }

    public void setSales_type(String sales_type) {
        this.sales_type = sales_type;
    }

    public String getTmp_id() {
        return tmp_id;
    }

    public void setTmp_id(String tmp_id) {
        this.tmp_id = tmp_id;
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
}
