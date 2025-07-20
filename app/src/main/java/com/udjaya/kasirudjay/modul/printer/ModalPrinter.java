package com.udjaya.kasirudjay.modul.printer;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.udjaya.kasirudjay.AppDatabase;
import com.udjaya.kasirudjay.R;
import com.udjaya.kasirudjay.api.ApiService;
import com.udjaya.kasirudjay.model.Category;
import com.udjaya.kasirudjay.model.GetCategory;
import com.udjaya.kasirudjay.model.printer.Printer;
import com.udjaya.kasirudjay.model.printer.PrinterDao;
import com.udjaya.kasirudjay.utils.PaddingDividerItemDecoration;
import com.udjaya.kasirudjay.utils.RClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ModalPrinter {

    private Context context;
    private Dialog dialog;

    // Referensi widget yang kamu definisikan jika perlu diakses
    private AppCompatButton btnCancel, btnSimpan;
    private RecyclerView rvCategory;
    private ListCategoryAdapter listCategoryAdapter;
    private AppCompatEditText etNamaPrinter, etIp, etPort;
    private SwitchCompat switchStrukDanBill, switchTiketPesanan, switchRekapShift;

    private OnPrinterDataChangedListener listener;

    private FragmentActivity activity;

    private Printer printer;

    public ModalPrinter(Context context, FragmentActivity activity, OnPrinterDataChangedListener listener) {
        this.context = context;
        this.activity = activity;
        this.listener = listener;
        initDialog();
        fetchCategoryData();
    }

    private void initDialog() {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.layout_dialog_printer);

        btnCancel = dialog.findViewById(R.id.btn_cancel);
        btnSimpan = dialog.findViewById(R.id.btn_simpan);
        rvCategory = dialog.findViewById(R.id.rvCategory);
        etNamaPrinter = dialog.findViewById(R.id.et_nama_printer);
        etIp = dialog.findViewById(R.id.et_ip);
        etPort = dialog.findViewById(R.id.et_port);
        switchStrukDanBill = dialog.findViewById(R.id.switch_struk_dan_bill);
        switchTiketPesanan = dialog.findViewById(R.id.switch_tiket_pesanan);
        switchRekapShift = dialog.findViewById(R.id.switch_rekap_shift);

        rvCategory.setLayoutManager(new LinearLayoutManager(context));
        listCategoryAdapter = new ListCategoryAdapter(new ArrayList<>());

        // Konversi dp ke px untuk padding divider kiri dan kanan
        int paddingInDp = 16;
        float density = rvCategory.getContext().getResources().getDisplayMetrics().density;
        int paddingInPx = (int) (paddingInDp * density + 0.5f);

        // Buat instance PaddingDividerItemDecoration dan tambahkan ke RecyclerView
        PaddingDividerItemDecoration decoration = new PaddingDividerItemDecoration(
                rvCategory.getContext(),
                RecyclerView.VERTICAL, // atau RecyclerView.HORIZONTAL
                paddingInPx,
                paddingInPx);

        rvCategory.addItemDecoration(decoration);

        rvCategory.setAdapter(listCategoryAdapter);

        // Set ukuran dialog besar seperti modal XL
        Window window = dialog.getWindow();
        if (window != null) {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            int width = (int) (metrics.widthPixels * 0.9); // 90% lebar layar
            int height = (int) (metrics.heightPixels * 1); // 90% tinggi layar
            window.setLayout(width, height);
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dapatkan context misalnya di Activity atau Fragment
                Context context = window.getContext(); // Contohnya

                if(validateInputs()){
                    // Persiapkan data printer dari inputan dialog
                    Printer p = new Printer();
                    p.setName(etNamaPrinter.getText().toString());
                    p.setIp(etIp.getText().toString());
                    p.setPort(Integer.parseInt(etPort.getText().toString()));
                    p.setPrintStruk(switchStrukDanBill.isChecked());
                    p.setPrintTicket(switchTiketPesanan.isChecked());
                    p.setPrintShift(switchRekapShift.isChecked());
                    p.setListCategory(listCategoryAdapter.getSelectedCategoryIds());
                    p.setManual(true);

                    AppDatabase db = AppDatabase.getInstance(context);
                    PrinterDao printerDao = db.printerDao();

                    new Thread(() -> {
                        // Jika printer sudah ada, update, jika tidak insertar baru
                        if(printer != null && printer.getId() != 0){
                            p.setId(printer.getId()); // Set id biar update
                            printerDao.update(p);
                        } else {
                            printerDao.insert(p);
                        }
                        activity.runOnUiThread(() -> {
                            if(listener != null){
                                listener.onPrinterDataChanged();
                            }
                            dialog.dismiss();
                        });
                    }).start();
                }
            }
        });
    }

    private boolean validateInputs() {
        boolean valid = true;

        // Validasi Nama - tidak boleh kosong
        String nama = etNamaPrinter.getText() != null ? etNamaPrinter.getText().toString().trim() : "";
        if (nama.isEmpty()) {
            etNamaPrinter.setError("Nama tidak boleh kosong");
            valid = false;
        } else {
            etNamaPrinter.setError(null);
        }

        // Validasi IP - cek format IP menggunakan regex sederhana
        String ip = etIp.getText() != null ? etIp.getText().toString().trim() : "";
        String ipPattern = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\.|$)){4}$";
        if (ip.isEmpty()) {
            etIp.setError("IP tidak boleh kosong");
            valid = false;
        } else if (!ip.matches(ipPattern)) {
            etIp.setError("Format IP tidak valid");
            valid = false;
        } else {
            etIp.setError(null);
        }

        // Validasi Port - harus angka dan di antara 1 - 65535
        String portStr = etPort.getText() != null ? etPort.getText().toString().trim() : "";
        if (portStr.isEmpty()) {
            etPort.setError("Port tidak boleh kosong");
            valid = false;
        } else {
            try {
                int port = Integer.parseInt(portStr);
                if (port < 1 || port > 65535) {
                    etPort.setError("Port harus antara 1 sampai 65535");
                    valid = false;
                } else {
                    etPort.setError(null);
                }
            } catch (NumberFormatException e) {
                etPort.setError("Port harus berupa angka");
                valid = false;
            }
        }

        return valid;
    }

    private void fetchCategoryData() {
        Retrofit retrofitClient = RClient.getRetrofitInstance();
        ApiService apiService =retrofitClient.create(ApiService.class);

        Call<GetCategory> call = apiService.getAllCategory();
        call.enqueue(new Callback<GetCategory>() {
            @Override
            public void onResponse(Call<GetCategory> call, Response<GetCategory> response) {
                if (response.isSuccessful() && response.body() != null) {

                    GetCategory categories = response.body();
                    List<Category> listCategory = categories.getData();
                    Log.d("Fetch Category", listCategory.get(1).getName());
                    listCategoryAdapter.setData(listCategory, printer);  // update adapter dengan data baru

                } else {
                    Toast.makeText(context, "Gagal mendapatkan data category", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<GetCategory> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Modal Printer", String.valueOf(t.getMessage()));
            }
        });
    }

    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

    public void setPrinter(Printer printer){
        this.printer = printer;

        if(etNamaPrinter != null){
            etNamaPrinter.setText(printer.getName());
            etIp.setText(printer.getIp());
            etPort.setText(String.valueOf(printer.getPort()));
            switchStrukDanBill.setChecked(printer.isPrintStruk());
            switchTiketPesanan.setChecked(printer.isPrintTicket());
            switchRekapShift.setChecked(printer.isPrintShift);


        }
    }

    public interface OnPrinterDataChangedListener {
        void onPrinterDataChanged();
    }

}
