package com.udjaya.kasirudjay.modul.printer;

import android.annotation.SuppressLint;
import android.graphics.Insets;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.udjaya.kasirudjay.AppDatabase;
import com.udjaya.kasirudjay.R;
import com.udjaya.kasirudjay.model.configprinter.ConfigPrinter;
import com.udjaya.kasirudjay.model.configprinter.ConfigPrinterDao;
import com.udjaya.kasirudjay.model.printer.Printer;
import com.udjaya.kasirudjay.model.printer.PrinterDao;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class PrinterFragment extends Fragment {

    private RecyclerView recyclerView;
    private ConcatAdapter concatAdapter;

    private PrinterDao printerDao;
    private ConfigPrinterDao configPrinterDao;
    private AppCompatButton btnTambahManual;
    private PrinterListAdapter printerListAdapter;

    public PrinterFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_printer, container, false);
    }

    @SuppressLint("NewApi")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars()).toPlatformInsets();
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        AppDatabase db = AppDatabase.getInstance(getContext());
        printerDao = db.printerDao();


        recyclerView = view.findViewById(R.id.recycler_printers);
        btnTambahManual = view.findViewById(R.id.btn_tambah_manual);

        printerListAdapter = new PrinterListAdapter(printerDao, new PrinterListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Printer printer) {
                Toast.makeText(getContext(), "Edit Printer: " + printer.getName(), Toast.LENGTH_SHORT).show();
                ModalPrinter formDialog = new ModalPrinter(getContext(), requireActivity(), new ModalPrinter.OnPrinterDataChangedListener() {
                    @Override
                    public void onPrinterDataChanged() {
                        refreshDataAndNotifyAdapter();
                    }
                });
                formDialog.setPrinter(printer); // Pastikan ada method setPrinter untuk edit mode
                formDialog.show();
            }
        });

        configPrinterDao = db.configPrinterDao();
        ConfigFooterAdapter configFooterAdapter = new ConfigFooterAdapter(configPrinterDao);


        concatAdapter = new ConcatAdapter(printerListAdapter, configFooterAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(concatAdapter);

        btnTambahManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModalPrinter formDialog = new ModalPrinter(getContext(), requireActivity(), new ModalPrinter.OnPrinterDataChangedListener() {
                    @Override
                    public void onPrinterDataChanged() {
                        // Refresh data printer dari database dan update adapter
                        refreshDataAndNotifyAdapter();
                    }
                });
                formDialog.show();
            }
        });
    }

    private void refreshDataAndNotifyAdapter() {
        Executor executor = Executors.newSingleThreadExecutor();
        Handler mainHandler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            // Ambil data di background thread
            List<Printer> newPrinterList = printerDao.getAllPrinters();

            // Update adapter di UI thread
            mainHandler.post(() -> {
                printerListAdapter.setData(newPrinterList);
                printerListAdapter.notifyDataSetChanged();
            });
        });
    }

}