package com.udjaya.kasirudjay.model.configprinter;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.udjaya.kasirudjay.model.printer.Printer;

import java.util.List;

@Dao
public interface ConfigPrinterDao {
    @Query("SELECT * FROM config_printer")
    List<ConfigPrinter> getAll();

    @Insert
    void insert(ConfigPrinter option);

    @Update
    void update(ConfigPrinter option);

    @Query("SELECT * FROM config_printer WHERE id = :printerId LIMIT 1")
    ConfigPrinter getConfigPrinterById(int printerId);

    @Query("SELECT * FROM config_printer ORDER BY id ASC LIMIT 1")
    ConfigPrinter getFirstConfigPrinter();
}
