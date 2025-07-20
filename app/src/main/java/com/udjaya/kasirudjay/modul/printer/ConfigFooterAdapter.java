package com.udjaya.kasirudjay.modul.printer;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.udjaya.kasirudjay.R;
import com.udjaya.kasirudjay.model.configprinter.ConfigPrinter;
import com.udjaya.kasirudjay.model.configprinter.ConfigPrinterDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfigFooterAdapter extends RecyclerView.Adapter<ConfigFooterAdapter.ConfigViewHolder> {

    ConfigPrinterDao configPrinterDao;
    ConfigPrinter currentConfigPrinter;

    public ConfigFooterAdapter(ConfigPrinterDao configPrinterDao) {
        this.configPrinterDao = configPrinterDao;
        loadData();
    }

    private void loadData() {
        new Thread(() -> {
            currentConfigPrinter = configPrinterDao.getFirstConfigPrinter();
            if(currentConfigPrinter == null){
                ConfigPrinter firstData = new ConfigPrinter();
                firstData.setJumlahTiketPesanan(1);
                firstData.setCetakTiketPesananSaatBayar(false);
                firstData.setCetakTiketPesananPerProduk(false);

                configPrinterDao.insert(firstData);

                currentConfigPrinter = firstData;
            }else{
                Log.d("ConfigFooterAdapter", String.valueOf(currentConfigPrinter.getJumlahTiketPesanan()));
                Log.d("ConfigFooterAdapter", String.valueOf(currentConfigPrinter.isCetakTiketPesananSaatBayar()));
                Log.d("ConfigFooterAdapter", String.valueOf(currentConfigPrinter.isCetakTiketPesananPerProduk()));
            }
            // jika ada data, lakukan notify untuk update UI dengan data ini di main thread

            // Panggil notifyDataSetChanged di main thread agar onBindViewHolder dipanggil ulang
            new Handler(Looper.getMainLooper()).post(() -> notifyDataSetChanged());
        }).start();
    }

    public void saveData(int jumlah, boolean newCetakTiketPesananSaatBayar, boolean newCetakTiketPesananPerProduk) {
        new Thread(() -> {
            ConfigPrinter configPrinter = configPrinterDao.getFirstConfigPrinter();
            configPrinter.setJumlahTiketPesanan(jumlah);
            configPrinter.setCetakTiketPesananSaatBayar(newCetakTiketPesananSaatBayar);
            configPrinter.setCetakTiketPesananPerProduk(newCetakTiketPesananPerProduk);

            configPrinterDao.update(configPrinter);
            currentConfigPrinter = configPrinter;
        }).start();
    }

    public static class ConfigViewHolder extends RecyclerView.ViewHolder {

        AppCompatEditText etJumlahTiketPesanan;
        SwitchCompat switchCetakTiketPesananSaatBayar, switchCetakTiketPerProduk;


        public ConfigViewHolder(@NonNull View itemView, ConfigFooterAdapter adapter) {
            super(itemView);
            etJumlahTiketPesanan = itemView.findViewById(R.id.et_jumlah_tiket_pesanan);
            switchCetakTiketPesananSaatBayar = itemView.findViewById(R.id.switch_cetak_tiket_pesanan_saat_bayar);
            switchCetakTiketPerProduk = itemView.findViewById(R.id.switch_cetak_tiket_per_produk);

            etJumlahTiketPesanan.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE ||
                            (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                        String input = etJumlahTiketPesanan.getText().toString().trim();
                        if (input.isEmpty()) {
                            v.setText("1");
                            adapter.saveData(1, adapter.currentConfigPrinter.isCetakTiketPesananSaatBayar(), adapter.currentConfigPrinter.isCetakTiketPesananPerProduk());
                            Toast.makeText(v.getContext(), "Mohon masukkan angka", Toast.LENGTH_SHORT).show();

                            return true;
                        }

                        try {
                            int jumlah = Integer.parseInt(input);
                            adapter.saveData(jumlah, adapter.currentConfigPrinter.isCetakTiketPesananSaatBayar(), adapter.currentConfigPrinter.isCetakTiketPesananPerProduk());
                            Toast.makeText(v.getContext(), "Jumlah tiket: " + jumlah, Toast.LENGTH_SHORT).show();

                            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        } catch (NumberFormatException e) {
                            Toast.makeText(v.getContext(), "Input harus berupa angka valid", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                    return false;
                }
            });

            switchCetakTiketPerProduk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.d("ConfigFooterAdapter", String.valueOf(isChecked));
                    adapter.saveData(adapter.currentConfigPrinter.getJumlahTiketPesanan(), adapter.currentConfigPrinter.isCetakTiketPesananSaatBayar(), isChecked);
                }
            });

            switchCetakTiketPesananSaatBayar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.d("ConfigFooterAdapter", String.valueOf(isChecked));
                    adapter.saveData(adapter.currentConfigPrinter.getJumlahTiketPesanan(), isChecked, adapter.currentConfigPrinter.isCetakTiketPesananPerProduk());
                }
            });

        }
    }

    @NonNull
    @Override
    public ConfigViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_config_printer, parent, false);
        return new ConfigViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ConfigViewHolder holder, int position) {
        if (currentConfigPrinter != null) {
            // Set data ke widget saat onBindViewHolder dipanggil
            holder.etJumlahTiketPesanan.setText(String.valueOf(currentConfigPrinter.getJumlahTiketPesanan()));
            holder.switchCetakTiketPesananSaatBayar.setChecked(currentConfigPrinter.isCetakTiketPesananSaatBayar());
            holder.switchCetakTiketPerProduk.setChecked(currentConfigPrinter.isCetakTiketPesananPerProduk());
            // Set data lain jika ada misal checkbox, switch dll
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
