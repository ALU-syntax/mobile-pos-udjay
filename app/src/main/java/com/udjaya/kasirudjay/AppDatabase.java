package com.udjaya.kasirudjay;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.udjaya.kasirudjay.model.printer.Printer;
import com.udjaya.kasirudjay.model.printer.PrinterDao;
import com.udjaya.kasirudjay.model.configprinter.ConfigPrinter;
import com.udjaya.kasirudjay.model.configprinter.ConfigPrinterDao;

@Database(entities = {Printer.class, ConfigPrinter.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract PrinterDao printerDao();
    public abstract ConfigPrinterDao configPrinterDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "uddjaya_local_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
