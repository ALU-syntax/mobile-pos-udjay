package com.udjaya.kasirudjay.model.configprinter;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "config_printer")
public class ConfigPrinter {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int jumlahTiketPesanan;
    private boolean isCetakTiketPesananSaatBayar;
    private boolean isCetakTiketPesananPerProduk;

    // Getter dan Setter

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getJumlahTiketPesanan() {
        return jumlahTiketPesanan;
    }

    public void setJumlahTiketPesanan(int jumlahTiketPesanan) {
        this.jumlahTiketPesanan = jumlahTiketPesanan;
    }

    public boolean isCetakTiketPesananSaatBayar() {
        return isCetakTiketPesananSaatBayar;
    }

    public void setCetakTiketPesananSaatBayar(boolean cetakTiketPesananSaatBayar) {
        isCetakTiketPesananSaatBayar = cetakTiketPesananSaatBayar;
    }

    public boolean isCetakTiketPesananPerProduk() {
        return isCetakTiketPesananPerProduk;
    }

    public void setCetakTiketPesananPerProduk(boolean cetakTiketPesananPerProduk) {
        isCetakTiketPesananPerProduk = cetakTiketPesananPerProduk;
    }
}
