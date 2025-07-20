package com.udjaya.kasirudjay.model.printer;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import java.util.List;

@Entity(tableName = "printers")
@TypeConverters({Converters.class})
public class Printer {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    @Nullable
    public String macAddress;
    public String ip;
    public int port;
    public boolean isTcp;
    public boolean isBluetooth;
    public boolean isPrintShift;

    // Menyimpan listCategory sebagai List<Integer>
    public List<Integer> listCategory;

    public boolean printStruk;
    public boolean printTicket;

    public boolean isManual;

    // Constructor, getters and setters di sini (atau gunakan lombok/dto sesuai kebutuhan)

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

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isTcp() {
        return isTcp;
    }

    public void setTcp(boolean tcp) {
        isTcp = tcp;
    }

    public boolean isBluetooth() {
        return isBluetooth;
    }

    public void setBluetooth(boolean bluetooth) {
        isBluetooth = bluetooth;
    }

    public boolean isPrintShift() {
        return isPrintShift;
    }

    public void setPrintShift(boolean printShift) {
        isPrintShift = printShift;
    }

    public List<Integer> getListCategory() {
        return listCategory;
    }

    public void setListCategory(List<Integer> listCategory) {
        this.listCategory = listCategory;
    }

    public boolean isPrintStruk() {
        return printStruk;
    }

    public void setPrintStruk(boolean printStruk) {
        this.printStruk = printStruk;
    }

    public boolean isPrintTicket() {
        return printTicket;
    }

    public void setPrintTicket(boolean printTicket) {
        this.printTicket = printTicket;
    }

    public boolean isManual() {
        return isManual;
    }

    public void setManual(boolean manual) {
        isManual = manual;
    }
}