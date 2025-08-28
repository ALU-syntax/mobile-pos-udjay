package com.udjaya.kasirudjay.model;

import java.util.List;

public class Transactions {
    private String id;
    private String outlet_id;
    private String user_id;
    private String customer_id;
    private String total;
    private String nominal_bayar;
    private String change;
    private String category_payment;
    private String tipe_pembayaran;
    private String nama_tipe_pembayaran;
    private String total_pajak;
    private String total_modifier;
    private String total_diskon;
    private String diskon_all_item;
    private String rounding_amount;
    private String tanda_rounding;
    private String catatan;
    private String patty_cash_id;
    private String deleted_at;
    private String created_at;
    private String updated_at;
    private Outlet outlet;
    private User user;
    private List<Tax> tax;
    private String potongan_point;
    private Customer customer;

    public List<Tax> getTax() {
        return tax;
    }

    public void setTax(List<Tax> tax) {
        this.tax = tax;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Outlet getOutlet() {
        return outlet;
    }

    public void setOutlet(Outlet outlet) {
        this.outlet = outlet;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOutlet_id() {
        return outlet_id;
    }

    public void setOutlet_id(String outlet_id) {
        this.outlet_id = outlet_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getNominal_bayar() {
        return nominal_bayar;
    }

    public void setNominal_bayar(String nominal_bayar) {
        this.nominal_bayar = nominal_bayar;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getCategory_payment() {
        return category_payment;
    }

    public void setCategory_payment(String category_payment) {
        this.category_payment = category_payment;
    }

    public String getTipe_pembayaran() {
        return tipe_pembayaran;
    }

    public void setTipe_pembayaran(String tipe_pembayaran) {
        this.tipe_pembayaran = tipe_pembayaran;
    }

    public String getNama_tipe_pembayaran() {
        return nama_tipe_pembayaran;
    }

    public void setNama_tipe_pembayaran(String nama_tipe_pembayaran) {
        this.nama_tipe_pembayaran = nama_tipe_pembayaran;
    }

    public String getTotal_pajak() {
        return total_pajak;
    }

    public void setTotal_pajak(String total_pajak) {
        this.total_pajak = total_pajak;
    }

    public String getTotal_modifier() {
        return total_modifier;
    }

    public void setTotal_modifier(String total_modifier) {
        this.total_modifier = total_modifier;
    }

    public String getTotal_diskon() {
        return total_diskon;
    }

    public void setTotal_diskon(String total_diskon) {
        this.total_diskon = total_diskon;
    }

    public String getDiskon_all_item() {
        return diskon_all_item;
    }

    public void setDiskon_all_item(String diskon_all_item) {
        this.diskon_all_item = diskon_all_item;
    }

    public String getRounding_amount() {
        return rounding_amount;
    }

    public void setRounding_amount(String rounding_amount) {
        this.rounding_amount = rounding_amount;
    }

    public String getTanda_rounding() {
        return tanda_rounding;
    }

    public void setTanda_rounding(String tanda_rounding) {
        this.tanda_rounding = tanda_rounding;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    public String getPatty_cash_id() {
        return patty_cash_id;
    }

    public void setPatty_cash_id(String patty_cash_id) {
        this.patty_cash_id = patty_cash_id;
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

    public String getPotongan_point() {
        return potongan_point;
    }

    public void setPotongan_point(String potongan_point) {
        this.potongan_point = potongan_point;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
