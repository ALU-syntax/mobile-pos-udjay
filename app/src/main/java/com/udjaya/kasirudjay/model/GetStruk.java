package com.udjaya.kasirudjay.model;

import com.udjaya.kasirudjay.model.notereceiptscheduler.NoteReceiptScheduler;

import java.util.List;

public class GetStruk {

    private Transactions transaction;
    private List<TransactionItems> transactionItems;
    private User user;
    private String device;
    private boolean status;
    private List<NoteReceiptScheduler> noteReceiptScheduler;


    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Transactions getTransaction() {
        return transaction;
    }

    public void setTransaction(Transactions transaction) {
        this.transaction = transaction;
    }

    public List<TransactionItems> getTransactionItems() {
        return transactionItems;
    }

    public void setTransactionItems(List<TransactionItems> transactionItems) {
        this.transactionItems = transactionItems;
    }

    public List<NoteReceiptScheduler> getNoteReceiptScheduler() {
        return noteReceiptScheduler;
    }

    public void setNoteReceiptScheduler(List<NoteReceiptScheduler> noteReceiptScheduler) {
        this.noteReceiptScheduler = noteReceiptScheduler;
    }
}
