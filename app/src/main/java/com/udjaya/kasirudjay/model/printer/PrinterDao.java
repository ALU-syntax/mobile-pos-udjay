package com.udjaya.kasirudjay.model.printer;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import java.util.List;

@Dao
public interface PrinterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Printer printer);

    @Update
    void update(Printer printer);

    @Delete
    void delete(Printer printer);

    @Query("SELECT * FROM printers")
    List<Printer> getAllPrinters();

    @Query("SELECT * FROM printers WHERE id = :printerId LIMIT 1")
    Printer getPrinterById(int printerId);

    @Query("SELECT * FROM printers LIMIT 1")
    Printer getFirstPrinter();
}