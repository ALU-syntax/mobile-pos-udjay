package com.udjaya.kasirudjay;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dantsu.escposprinter.connection.DeviceConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
import com.dantsu.escposprinter.textparser.PrinterTextParserImg;
import com.udjaya.kasirudjay.api.ApiService;
import com.udjaya.kasirudjay.model.DeviceInfo;
import com.udjaya.kasirudjay.model.GetOpenBillStruk;
import com.udjaya.kasirudjay.model.GetStruk;
import com.udjaya.kasirudjay.model.ItemOpenBill;
import com.udjaya.kasirudjay.model.LogRequest;
import com.udjaya.kasirudjay.model.ModifierOpenBill;
import com.udjaya.kasirudjay.model.OpenBill;
import com.udjaya.kasirudjay.model.Tax;
import com.udjaya.kasirudjay.model.TransactionItems;
import com.udjaya.kasirudjay.model.Transactions;
import com.udjaya.kasirudjay.model.User;
import com.udjaya.kasirudjay.model.UserInfo;
import com.udjaya.kasirudjay.services.AsyncBluetoothEscPosPrint;
import com.udjaya.kasirudjay.services.AsyncEscPosPrint;
import com.udjaya.kasirudjay.services.AsyncEscPosPrinter;
import com.udjaya.kasirudjay.utils.RClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private ApiService apiService;
    private Retrofit retrofit;

    private BluetoothConnection selectedDevice;
    public BluetoothConnection[] bluetoothDevicesList = (new BluetoothPrintersConnections()).getList();


    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if(bluetoothDevicesList != null){
            if(bluetoothDevicesList.length > 0){
                selectedDevice = bluetoothDevicesList[0];
            }
        }

        retrofit = RClient.getRetrofitInstance();
        apiService = retrofit.create(ApiService.class);

        webView = findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.addJavascriptInterface(new WebAppInterface(this), "Android");

        MainWebViewClient viewClient = new MainWebViewClient();
        webView.setWebViewClient(viewClient);

        webView.loadUrl("https://udjaya.neidra.my.id/kasir");
    }

    private void logErrorToApi(Exception e, OpenBill data) {
        // Prepare log request
        DeviceInfo deviceInfo = new DeviceInfo(Build.BRAND, Build.MODEL, Build.VERSION.RELEASE);
        UserInfo userInfo = new UserInfo(data.getUser().getId(), data.getUser().getName());
        LogRequest logRequest = new LogRequest(new Date().toString(), e.getMessage(), Log.getStackTraceString(e), deviceInfo, userInfo);

        // Send log to API
        Call<Void> call = apiService.logError(logRequest);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("LogError", "Log sent successfully");
                } else {
                    Log.e("LogError", "Failed to send log: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("LogError", "Error sending log", t);
            }
        });
    }

    public static String formatRupiah(String angka, String prefix) {
        // Remove all non-digit and non-comma characters
        String numberString = angka.replaceAll("[^,\\d]", "");
        String[] split = numberString.split(",");
        int sisa = split[0].length() % 3;
        String rupiah = split[0].substring(0, sisa);
        String ribuan = split[0].substring(sisa);

        // Use regex to find all groups of 3 digits
        StringBuilder ribuanBuilder = new StringBuilder();
        for (int i = 0; i < ribuan.length(); i += 3) {
            if (ribuanBuilder.length() > 0) {
                ribuanBuilder.append(".");
            }
            ribuanBuilder.append(ribuan.substring(i, Math.min(i + 3, ribuan.length())));
        }

        if (ribuanBuilder.length() > 0) {
            String separator = sisa > 0 ? "." : "";
            rupiah += separator + ribuanBuilder.toString();
        }

        // Append the decimal part if it exists
        if (split.length > 1) {
            rupiah += "," + split[1];
        }

        // Return the formatted string with prefix if provided
        return prefix == null ? rupiah : "Rp. " + rupiah;
    }

    private class MainWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();
            Uri uri = request.getUrl();
            Log.d(TAG, "request: " + request);
            Log.d(TAG, "url: " + url);

            if(url.startsWith("intent://cetak-struk")){
                String id = uri.getQueryParameter("id");
                Log.d(TAG, "masok: " + id);
                getDataStruk(id, false);

                return true;
            }

            if (url.startsWith("intent://list-bluetooth-device")){
                Log.d(TAG, "masok pilih: ");
                browseBluetoothDevice();
                return true;
            }
            return false;

        }
    }

    public void getDataStruk(String id, boolean isOrder){
        Call<GetStruk> call = apiService.getStruk(id);
        call.enqueue(new Callback<GetStruk>() {
            @Override
            public void onResponse(Call<GetStruk> call, Response<GetStruk> response) {
                Log.d("Transaction API", "onResponse: " + response);
                Log.d("Transaction API", "onResponse: " + response.body());
                assert response.body() != null;
                Log.d("Transaction API", "onResponse: " + response.body().getTransaction());
                assert response.body() != null;
                Transactions transactions = response.body().getTransaction();

                List<TransactionItems> detail = response.body().getTransactionItems();
                User user = response.body().getUser();
                String device = response.body().getDevice();

                printBluetooth(transactions, detail, user, device,  isOrder);

            }

            @Override
            public void onFailure(Call<GetStruk> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void getOpenBillOrderStruk(String id){
        Call<GetOpenBillStruk> callApi = apiService.getOpenBillStrukOrder(id);
        callApi.enqueue(new Callback<GetOpenBillStruk>() {
            @Override
            public void onResponse(Call<GetOpenBillStruk> call, Response<GetOpenBillStruk> response) {
                assert response.body() != null;
                OpenBill openBill = response.body().getData();

                printBluetoothOpenBill(openBill);
            }

            @Override
            public void onFailure(Call<GetOpenBillStruk> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }


    /*==============================================================================================
    ======================================BLUETOOTH PART============================================
    ==============================================================================================*/

    public interface OnBluetoothPermissionsGranted {
        void onPermissionsGranted();
    }

    public static final int PERMISSION_BLUETOOTH = 1;
    public static final int PERMISSION_BLUETOOTH_ADMIN = 2;
    public static final int PERMISSION_BLUETOOTH_CONNECT = 3;
    public static final int PERMISSION_BLUETOOTH_SCAN = 4;

    public OnBluetoothPermissionsGranted onBluetoothPermissionsGranted;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case MainActivity.PERMISSION_BLUETOOTH:
                case MainActivity.PERMISSION_BLUETOOTH_ADMIN:
                case MainActivity.PERMISSION_BLUETOOTH_CONNECT:
                case MainActivity.PERMISSION_BLUETOOTH_SCAN:
                    this.checkBluetoothPermissions(this.onBluetoothPermissionsGranted);
                    break;
            }
        }
    }

    public void checkBluetoothPermissions(OnBluetoothPermissionsGranted onBluetoothPermissionsGranted) {
        this.onBluetoothPermissionsGranted = onBluetoothPermissionsGranted;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, PERMISSION_BLUETOOTH);
            } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, PERMISSION_BLUETOOTH_ADMIN);
            } else {
                this.onBluetoothPermissionsGranted.onPermissionsGranted();
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, PERMISSION_BLUETOOTH_CONNECT);
            } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, PERMISSION_BLUETOOTH_SCAN);
            } else {
                this.onBluetoothPermissionsGranted.onPermissionsGranted();
            }
        }


//        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.BLUETOOTH}, MainActivity.PERMISSION_BLUETOOTH);
//        } else if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.BLUETOOTH_ADMIN}, MainActivity.PERMISSION_BLUETOOTH_ADMIN);
//        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.BLUETOOTH_CONNECT}, MainActivity.PERMISSION_BLUETOOTH_CONNECT);
//        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, MainActivity.PERMISSION_BLUETOOTH_SCAN);
//        } else {
//            this.onBluetoothPermissionsGranted.onPermissionsGranted();
//        }
    }


    @SuppressLint("MissingPermission")
    public void browseBluetoothDevice() {
        this.checkBluetoothPermissions(() -> {
            final BluetoothConnection[] bluetoothDevicesList = (new BluetoothPrintersConnections()).getList();

            if (bluetoothDevicesList != null) {
                final String[] items = new String[bluetoothDevicesList.length + 1];
                items[0] = "Default printer";
                int i = 0;
                for (BluetoothConnection device : bluetoothDevicesList) {
                    items[++i] = device.getDevice().getName();
                }

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("Bluetooth printer selection");
                alertDialog.setItems(
                        items,
                        (dialogInterface, i1) -> {
                            int index = i1 - 1;
                            if (index == -1) {
                                selectedDevice = null;
                            } else {
                                selectedDevice = bluetoothDevicesList[index];
                            }
//                            Button button = (Button) findViewById(R.id.button_bluetooth_browse);
//                            button.setText(items[i1]);
                        }
                );

                AlertDialog alert = alertDialog.create();
                alert.setCanceledOnTouchOutside(false);
                alert.show();
            }
        });

    }

    public void printBluetoothOpenBill(OpenBill data){
        this.checkBluetoothPermissions(() -> {
            new AsyncBluetoothEscPosPrint(
                    this,
                    new AsyncEscPosPrint.OnPrintFinished() {
                        @Override
                        public void onError(AsyncEscPosPrinter asyncEscPosPrinter, int codeException) {
                            Log.e("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : An error occurred !");
                        }

                        @Override
                        public void onSuccess(AsyncEscPosPrinter asyncEscPosPrinter) {
                            Log.i("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : Print is finished !");
                        }
                    }
            ).execute(this.getAsyncPrintOpenBill(selectedDevice, data));
        });
    }

    public void printBluetooth(Transactions transactions, List<TransactionItems> transactionItems, User user, String device, boolean isOrder) {
        if(isOrder){
            this.checkBluetoothPermissions(() -> {
                new AsyncBluetoothEscPosPrint(
                        this,
                        new AsyncEscPosPrint.OnPrintFinished() {
                            @Override
                            public void onError(AsyncEscPosPrinter asyncEscPosPrinter, int codeException) {
                                Log.e("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : An error occurred !");
                            }

                            @Override
                            public void onSuccess(AsyncEscPosPrinter asyncEscPosPrinter) {
                                Log.i("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : Print is finished !");
                            }
                        }
                ).execute(this.getAsyncEscPosPrinterOrder(selectedDevice, transactions, transactionItems, user, device));
            });
        }else{
            this.checkBluetoothPermissions(() -> {
                new AsyncBluetoothEscPosPrint(
                        this,
                        new AsyncEscPosPrint.OnPrintFinished() {
                            @Override
                            public void onError(AsyncEscPosPrinter asyncEscPosPrinter, int codeException) {
                                Log.e("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : An error occurred !");
                            }

                            @Override
                            public void onSuccess(AsyncEscPosPrinter asyncEscPosPrinter) {
                                Log.i("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : Print is finished !");
                            }
                        }
                ).execute(this.getAsyncEscPosPrinter(selectedDevice, transactions, transactionItems));
            });
        }
    }

    @SuppressLint("SimpleDateFormat")
    public AsyncEscPosPrinter getAsyncEscPosPrinter(DeviceConnection printerConnection, Transactions transactions, List<TransactionItems> transactionItems) {
        String item = "";
        String taxItem = "";
        int totalPajak = 0;
        int subTotal = 0;
        for (TransactionItems data : transactionItems){
            if(data.getProduct() != null){
                if(Objects.equals(data.getProduct().getName(), data.getVariant().getName())){
                    item += "[L]<b>" + data.getProduct().getName()+"</b>\n";
                }else{
                    item += "[L]<b>" + data.getProduct().getName() + " - " + data.getVariant().getName() +"</b>\n";
                }
            }else{
                item += "[L]<b>" + "custom" +"</b>\n";
            }

            for(String namaModifier: data.getModifier()){
                item +=  "[C]<b>" +namaModifier+"</b>\n";
            }
            if(data.getVariant() != null){
                item += "[L]<b>"+ 1 + "x" + "</b>[C]<b>@" +data.getVariant().getHarga()+"</b>\n";
                subTotal += data.getVariant().getHarga();
            }

            item += "[C]--------------------------------\n";
        }

        for(Tax tax : transactions.getTax()){
            int valueTax = (Integer.parseInt(tax.getAmount()) * subTotal) / 100;
            taxItem += "[L]<b>"+ tax.getName() + "(" + tax.getAmount() + tax.getSatuan() + ")" + "</b>[R]<b>" + formatRupiah(String.valueOf(valueTax), "Rp. ") +"</b>\n";
            totalPajak += valueTax;
        }

//        subTotal -= totalPajak;
        // Format tanggal
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault());
        inputFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = inputFormat.parse(transactions.getCreated_at());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // Buat instance SimpleDateFormat untuk format tanggal
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        // Buat instance SimpleDateFormat untuk format waktu
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

        // Format tanggal dan waktu
        String formattedDate = dateFormat.format(date);
        String formattedTime = timeFormat.format(date);

        int resultTotal = Integer.parseInt(transactions.getTotal());

        SimpleDateFormat format = new SimpleDateFormat("'on' yyyy-MM-dd 'at' HH:mm:ss");
        AsyncEscPosPrinter printer = new AsyncEscPosPrinter(printerConnection, 203, 48f, 32);
        return printer.addTextToPrint(
                "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, this.getApplicationContext().getResources().getDrawableForDensity(R.drawable.logo_red, DisplayMetrics.DENSITY_MEDIUM)) + "</img>\n" +
                        "[L]\n" +
                        "[C]" + transactions.getOutlet().getAddress() + "\n" +
                        "[C]" + transactions.getOutlet().getPhone()+ "\n" +
                        "[L]" + formattedDate + "[C][R]" + formattedTime + "\n" +
                        "[L]Collected By [C][R]" + transactions.getUser().getName() + "\n" +
                        "[C]--------------------------------\n" +
                        "[C] *dine in* \n" +
                        item +
                        "[L]Subtotal :[C]" + formatRupiah(String.valueOf(subTotal), "Rp. ") + "\n" +
                        "[L]Discount :[C]" + formatRupiah(String.valueOf(transactions.getTotal_diskon()), "Rp. ") + "\n" +
                        "[L]Rounding :[C]" + formatRupiah(String.valueOf(transactions.getRounding_amount()), "Rp. ") +"\n" +
                        taxItem +
                        "[C]--------------------------------\n" +
                        "[L]Total :[C]" + formatRupiah(String.valueOf(resultTotal), "Rp. ") + "\n" +
                        "[C]================================\n" +
                        "[L]" + transactions.getNama_tipe_pembayaran() + "[C]" + formatRupiah(String.valueOf(transactions.getNominal_bayar()), "Rp. ") + "\n" +
                        "[L]Change [C]" + formatRupiah(String.valueOf(transactions.getChange()), "Rp. ") + "\n" +
                        "[L]\n" +
                        "[C]--------------------------------\n" +
                        "[L]Instagram: ud.djaya[C][R] \n" +
                        "\n" +
                        "[C]--------------------------------\n" +
                        "[C]<font size='small'>TERIMA KASIH</font>\n"
        );
    }

    @SuppressLint("MissingPermission")
    public AsyncEscPosPrinter getAsyncPrintOpenBill(DeviceConnection printerConnection, OpenBill data){
        try {
            String item = "";
            if (!data.getItem().isEmpty()){
                for (ItemOpenBill itemOpenBill : data.getItem()){
                    if(itemOpenBill.getProduct_id() != null || itemOpenBill.getProduct_id() != "null"){
                        item += "[L]<b>"+ 1 + "x " + itemOpenBill.getNama_product() + "-" + itemOpenBill.getNama_variant() + "</b>[C] \n";
                    }else{
                        item += "[L]<b>"+ 1 + "x " + "custom" +"</b>[C]\n";
                    }

                    for(ModifierOpenBill modifierOpenBill: itemOpenBill.getModifier()){
                        item +=  "[L]" +modifierOpenBill.getName()+"\n";
                    }
                }

            }


            String deviceBrand = Build.BRAND;
            System.out.println("Device Brand: " + deviceBrand);
            // Buat objek Date saat ini
            Date currentDate = new Date();

            // Buat instance SimpleDateFormat untuk format tanggal
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            // Buat instance SimpleDateFormat untuk format waktu
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

            // Format tanggal dan waktu
            String formattedDate = dateFormat.format(currentDate);
            String formattedTime = timeFormat.format(currentDate);

            AsyncEscPosPrinter printer = new AsyncEscPosPrinter(printerConnection, 203, 48f, 32);
            return printer.addTextToPrint(
                    "[C]ADDITIONAL ORDER\n" +
                            "[L]Test Print[C][R] " + selectedDevice.getDevice().getName() + "\n" +
                            "[L]" + formattedDate + "[C][R]" + formattedTime + "\n" +
                            "[L]" + data.getUser().getName() + "[C][R]" + deviceBrand + "\n" +
                            "[C]--------------------------------\n" +
                            "[C]Dine In\n" +
                            "[C]--------------------------------\n" +
                            item
            );

        } catch (Exception e) {
            logErrorToApi(e, data);
            throw new RuntimeException(e);
        }

    }

    @SuppressLint("MissingPermission")
    public AsyncEscPosPrinter getAsyncEscPosPrinterOrder(DeviceConnection printerConnection, Transactions transactions, List<TransactionItems> transactionItems, User user, String device){
        String item = "";
        String productIdBefore = "";
        String variantIdBefore = "";
        String catatanBefore = "";
        List<String> modifierBefore = Arrays.asList("");;
        int quantityProduct = 1;

        for (TransactionItems data : transactionItems){
            // Memeriksa apakah kedua list sama persis
            boolean areEqual = modifierBefore.equals(data.getModifier());
            modifierBefore = data.getModifier();

            // pengecekan untuk menghitung quantity
            if(Objects.equals(productIdBefore, data.getProduct_id()) && Objects.equals(variantIdBefore, data.getVariant_id()) && Objects.equals(catatanBefore, data.getCatatan())){
                if(areEqual){
                    quantityProduct += 1;
                }else{
                    quantityProduct = 1;
                }
            }else{
                quantityProduct = 1;
            }

            productIdBefore = data.getProduct_id();
            variantIdBefore = data.getVariant_id();
            catatanBefore = data.getCatatan();

            if(data.getProduct() != null){
                item += "[L]<b>"+ quantityProduct + "x " + data.getProduct().getName() + "-" +data.getVariant().getName() + "</b>[C] \n";
            }else{
                item += "[L]<b>"+ quantityProduct + "x " + "custom" +"</b>[C]\n";
            }

            for(String namaModifier: data.getModifier()){
                item +=  "[L]" +namaModifier+"\n";
            }
        }

        // Buat objek Date saat ini
        Date currentDate = new Date();

        // Buat instance SimpleDateFormat untuk format tanggal
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        // Buat instance SimpleDateFormat untuk format waktu
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

        // Format tanggal dan waktu
        String formattedDate = dateFormat.format(currentDate);
        String formattedTime = timeFormat.format(currentDate);

        AsyncEscPosPrinter printer = new AsyncEscPosPrinter(printerConnection, 203, 48f, 32);
        return printer.addTextToPrint(
                                "[C]<font >ADDITIONAL ORDER</font>\n" +
                                "[L]Test Print[C][R] " + selectedDevice.getDevice().getName() + "\n" +
                                "[L]" + formattedDate + "[C][R]" + formattedTime + "\n" +
                                "[L]" + user.getName() + "[C][R]" + device + "\n" +
                                "[C]--------------------------------\n" +
                                "[C]Dine In\n" +
                                "[C]--------------------------------\n" +
                                item
        );
    }

    private class WebAppInterface {
        Context mContext;

        /** Instantiate the interface and set the context */
        WebAppInterface(Context c) {
            mContext = c;
        }


        /** Metode baru untuk menerima data dari HTML */
        @JavascriptInterface
        public void handlePaymentSuccess(String id) {
            Log.d("WebAppInterface", "Payment success with ID: " + id);
            // Panggil metode getDataStruk dengan ID yang diterima
            getDataStruk(id, true);
        }

        @JavascriptInterface
        public void handlePrintOpenBill(String id){
            getOpenBillOrderStruk(id);
        }
    }
}