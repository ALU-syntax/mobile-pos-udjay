package com.udjaya.kasirudjay.modul.printer;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udjaya.kasirudjay.R;
import com.udjaya.kasirudjay.model.printer.Printer;
import com.udjaya.kasirudjay.model.printer.PrinterDao;

import java.util.ArrayList;
import java.util.List;

public class PrinterListAdapter extends RecyclerView.Adapter<PrinterListAdapter.PrinterViewHolder> {

    private List<Printer> printers = new ArrayList<>();
    PrinterDao printerDao;

    public OnItemClickListener listener;


    public PrinterListAdapter(PrinterDao printerDao, OnItemClickListener listener) {
        this.printerDao = printerDao;
        this.listener = listener;
        loadData();
    }


    private void loadData() {
        new Thread(() -> {
            printers = printerDao.getAllPrinters();

            // Panggil notifyDataSetChanged di main thread agar onBindViewHolder dipanggil ulang
            new Handler(Looper.getMainLooper()).post(() -> notifyDataSetChanged());
        }).start();
    }

    // Method ini digunakan untuk update data list adapter
    public void setData(List<Printer> newPrinterList) {
        // Bersihkan data lama
        printers.clear();
        // Tambahkan data baru
        printers.addAll(newPrinterList);
        // lalu notify data changed agar RecyclerView refresh tampilannya
        notifyDataSetChanged();
    }

    public static class PrinterViewHolder extends RecyclerView.ViewHolder {
        TextView txtPrinterName, txtIpPort;
        public PrinterViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPrinterName = itemView.findViewById(R.id.txt_nama_printer);
            txtIpPort = itemView.findViewById(R.id.txt_ip_port);
        }

    }

    @NonNull
    @Override
    public PrinterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_printer, parent, false);
        return new PrinterListAdapter.PrinterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PrinterViewHolder holder, int position) {
        Printer printer  = printers.get(position);

        holder.txtPrinterName.setText(printer.getName());
        String ipAndPort = printer.getIp() + ":" + String.valueOf(printer.getPort());
        holder.txtIpPort.setText(ipAndPort);

        // Pasang klik listener di sini karena kita perlu mengakses posisi dan listener yang ada di adapter
        holder.itemView.setOnClickListener(v -> {
            if(listener != null && position != RecyclerView.NO_POSITION) {
                listener.onItemClick(printers.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return printers == null ? 0 : printers.size();
    }

    // interface listener
    public interface OnItemClickListener {
        void onItemClick(Printer printer);
    }
}
