package com.udjaya.kasirudjay.model.shiftorder;

import java.util.List;

public class GetShiftOrderStruk {

    private List<DataProductTransaction> data_product_transaction;
    private List<DataModifierTransaction> data_modifier_transaction;
    private PattyCash patty_cash;
    private int sold_product;
    private int sold_modifier;
    private List<DataPayment> data_payment;
    private int rounding;

    public List<DataProductTransaction> getData_product_transaction() {
        return data_product_transaction;
    }

    public void setData_product_transaction(List<DataProductTransaction> data_product_transaction) {
        this.data_product_transaction = data_product_transaction;
    }

    public List<DataModifierTransaction> getData_modifier_transaction() {
        return data_modifier_transaction;
    }

    public void setData_modifier_transaction(List<DataModifierTransaction> data_modifier_transaction) {
        this.data_modifier_transaction = data_modifier_transaction;
    }

    public PattyCash getPatty_cash() {
        return patty_cash;
    }

    public void setPatty_cash(PattyCash patty_cash) {
        this.patty_cash = patty_cash;
    }

    public int getSold_product() {
        return sold_product;
    }

    public void setSold_product(int sold_product) {
        this.sold_product = sold_product;
    }

    public int getSold_modifier() {
        return sold_modifier;
    }

    public void setSold_modifier(int sold_modifier) {
        this.sold_modifier = sold_modifier;
    }

    public List<DataPayment> getData_payment() {
        return data_payment;
    }

    public void setData_payment(List<DataPayment> data_payment) {
        this.data_payment = data_payment;
    }

    public int getRounding() {
        return rounding;
    }

    public void setRounding(int rounding) {
        this.rounding = rounding;
    }
}
