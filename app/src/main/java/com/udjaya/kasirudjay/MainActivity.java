package com.udjaya.kasirudjay;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.dantsu.escposprinter.connection.DeviceConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
import com.dantsu.escposprinter.connection.tcp.TcpConnection;
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
import com.udjaya.kasirudjay.model.notereceiptscheduler.NoteReceiptScheduler;
import com.udjaya.kasirudjay.model.printer.Printer;
import com.udjaya.kasirudjay.model.printer.PrinterDao;
import com.udjaya.kasirudjay.model.shiftorder.DataModifierTransaction;
import com.udjaya.kasirudjay.model.shiftorder.DataPayment;
import com.udjaya.kasirudjay.model.shiftorder.DataProductTransaction;
import com.udjaya.kasirudjay.model.shiftorder.GetShiftOrderStruk;
import com.udjaya.kasirudjay.model.shiftorder.PattyCash;
import com.udjaya.kasirudjay.model.shiftorder.Payment;
import com.udjaya.kasirudjay.modul.setting.SettingActivity;
import com.udjaya.kasirudjay.services.AsyncBluetoothEscPosPrint;
import com.udjaya.kasirudjay.services.AsyncEscPosPrint;
import com.udjaya.kasirudjay.services.AsyncEscPosPrinter;
import com.udjaya.kasirudjay.services.AsyncTcpEscPosPrint;
import com.udjaya.kasirudjay.utils.RClient;
import com.udjaya.kasirudjay.utils.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private DrawerLayout drawerLayout;
    private EditText inputIP;
    private EditText inputPort;
    private Button btnTestPrinterWifi;
    private Button btnTestPrinterBluettoth;
    private Button btnSave;
    private AppCompatButton btnZoomIn, btnZoomOut, btnZoomIn10, btnZoomOut10;
    private TextView txtCurrentZoom;
    private AppCompatEditText etCurrentZoom;


    private BluetoothConnection selectedDevice;
    public BluetoothConnection[] bluetoothDevicesList;
    SharedPrefManager prefManager;

    AppDatabase db;

    String ip;
    int port;

    private int currentZoom; // 100% skala normal

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        prefManager = SharedPrefManager.getInstance(this);

        prefManager.saveIp("192.168.1.100");
        prefManager.savePort(8080);

        ip = prefManager.getIp();
        port = prefManager.getPort();

        currentZoom = prefManager.getZoom();
        Log.d(TAG, "onCreate: " + currentZoom);

        drawerLayout = findViewById(R.id.drawer_layout);
        inputIP = findViewById(R.id.input_ip_address);
        inputPort = findViewById(R.id.input_port_address);
        btnTestPrinterBluettoth = findViewById(R.id.btn_test_printer_bluetooth);
        btnTestPrinterWifi = findViewById(R.id.btn_test_printer_wifi);
        btnSave = findViewById(R.id.btnSave);
        btnZoomIn = findViewById(R.id.btn_zoom_in);
        btnZoomOut = findViewById(R.id.btn_zoom_out);
        btnZoomIn10 = findViewById(R.id.btn_zoom_in_10);
        btnZoomOut10 = findViewById(R.id.btn_zoom_out_10);
        txtCurrentZoom = findViewById(R.id.txt_current_zoom);
        etCurrentZoom = findViewById(R.id.et_current_zoom);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etCurrentZoom.setText(String.valueOf(currentZoom));

        requestBluetoothPermissions(this);
        checkBluetoothStatus();
        db = AppDatabase.getInstance(this);

        ImageButton btnToggleSidebar = findViewById(R.id.btn_toggle_sidebar);

        btnToggleSidebar.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(Gravity.START)) {
                drawerLayout.closeDrawer(Gravity.START);
            } else {
                drawerLayout.openDrawer(Gravity.START);

                // Ambil data IP dan Port dari SharedPreferences
                String savedIp = prefManager.getIp();
                int savedPort = prefManager.getPort();

                // Set data ke EditText input IP dan Port
                inputIP.setText(savedIp);
                inputPort.setText(String.valueOf(savedPort));
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefManager.saveIp(inputIP.getText().toString());
                prefManager.savePort(Integer.parseInt(inputPort.getText().toString()));
            }
        });

        btnTestPrinterWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataStruk("5100", false);

            }
        });

        btnTestPrinterBluettoth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(i);
            }
        });

//        bluetoothDevicesList = (new BluetoothPrintersConnections()).getList();
//        if(bluetoothDevicesList != null){
//            if(bluetoothDevicesList.length > 0){
//                selectedDevice = bluetoothDevicesList[0];
//            }
//        }

        btnZoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentZoom < 500) { // batas maksimal zoom (500%)
                    int newZoom = currentZoom;
                    currentZoom += 1;
                    prefManager.saveZoom(newZoom += 1);
                    setCustomZoom(currentZoom);
                    etCurrentZoom.setText(String.valueOf(currentZoom));
                }
                Log.d(TAG, String.valueOf(currentZoom));
            }
        });

        btnZoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentZoom > -100) { // batas minimal zoom 50% (bisa diatur sesuai kebutuhan)
                    int newZoom = currentZoom;
                    currentZoom -= 1;
                    prefManager.saveZoom(newZoom -= 1);
                    setCustomZoom(currentZoom);
                    etCurrentZoom.setText(String.valueOf(currentZoom));
                }
                Log.d(TAG, String.valueOf(currentZoom));
            }
        });

        btnZoomIn10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentZoom < 500) { // batas maksimal zoom (500%)
                    int newZoom = currentZoom;
                    currentZoom += 10;
                    prefManager.saveZoom(newZoom += 10);
                    setCustomZoom(currentZoom);
                    etCurrentZoom.setText(String.valueOf(currentZoom));
                }
                Log.d(TAG, String.valueOf(currentZoom));
            }
        });

        btnZoomOut10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentZoom > -100) { // batas minimal zoom 50% (bisa diatur sesuai kebutuhan)
                    int newZoom = currentZoom;
                    currentZoom -= 10;
                    prefManager.saveZoom(newZoom -= 10);
                    setCustomZoom(currentZoom);
                    etCurrentZoom.setText(String.valueOf(currentZoom));
                }
                Log.d(TAG, String.valueOf(currentZoom));
            }
        });

        retrofit = RClient.getRetrofitInstance();
        apiService = retrofit.create(ApiService.class);

        webView = findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();

        webSettings.setBuiltInZoomControls(false);
        webSettings.setSupportZoom(false);

        webSettings.setJavaScriptEnabled(true);


        webView.addJavascriptInterface(new WebAppInterface(this), "Android");

        MainWebViewClient viewClient = new MainWebViewClient();
        webView.setWebViewClient(viewClient);

//        webView.loadUrl("https://udjaya.neidra.my.id/kasir");
        webView.loadUrl("https://backoffice.uddjaya.com/kasir");

        setCustomZoom(currentZoom);
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

    private void setCustomZoom(int zoomPercent) {
        currentZoom = zoomPercent;
        webView.setInitialScale(currentZoom); // nilai dalam persen
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

    public float getAmount(String money) {
        // Step 1: Remove all non-numeric characters except commas and dots
        String cleanString = money.replaceAll("[^0-9\\.,]", "");

        // Step 2: Remove all non-numeric characters
        String onlyNumbersString = money.replaceAll("[^0-9]", "");

        // Step 3: Calculate the number of thousand separators to be removed
        int separatorsCountToBeErased = cleanString.length() - onlyNumbersString.length() - 1;

        // Step 4: Remove all commas and dots, but keep only the last one if it exists
        String stringWithCommaOrDot = removeOccurrences(cleanString, "[,\\.]", separatorsCountToBeErased);

        // Step 5: Remove thousand separators (commas or dots that are followed by three or more digits)
        String removedThousandSeparator = stringWithCommaOrDot.replaceAll("(\\.|,)(?=\\d{3,})", "");

        // Step 6: Replace any remaining commas with dots to handle decimal points
        String finalString = removedThousandSeparator.replace(',', '.');

        // Step 7: Convert the final string to a float
        return Float.parseFloat(finalString);
    }

    private static String removeOccurrences(String input, String regex, int limit) {
        String result = input;
        for (int i = 0; i < limit; i++) {
            String temp = result.replaceFirst(regex, "");
            if (temp.equals(result)) {
                break; // No more occurrences to replace
            }
            result = temp;
        }
        return result;
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

            if (url.startsWith("intent://struk-history-print")){
                String id = uri.getQueryParameter("id");
                Log.d(TAG, "masok ga pak eko: " + id);
                getDataStrukHistory(id);
                return true;
            }

            if(url.startsWith("intent://shift-order-print")){
                String id = uri.getQueryParameter("id");
                Log.d(TAG, "shift-order-print: ");
                getDataStrukOrderShift(id);
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
                List<NoteReceiptScheduler> noteReceiptSchedulers = response.body().getNoteReceiptScheduler();

                printBluetooth(transactions, detail, user, device,  isOrder, noteReceiptSchedulers);

            }

            @Override
            public void onFailure(Call<GetStruk> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void getDataStrukHistory(String id){
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

                printBluetoothHistoryTransaction(transactions, detail);

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
                printTcpOpenBill(openBill);
            }

            @Override
            public void onFailure(Call<GetOpenBillStruk> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void getDataStrukOrderShift(String id){
        Call<GetShiftOrderStruk> callApi = apiService.getShiftOrderStruk(id);
        callApi.enqueue(new Callback<GetShiftOrderStruk>() {
            @Override
            public void onResponse(Call<GetShiftOrderStruk> call, Response<GetShiftOrderStruk> response) {
                assert response.body() != null;
                GetShiftOrderStruk orderStruk = response.body();

                printShiftOrder(orderStruk.getPatty_cash(),orderStruk.getSold_product(), orderStruk.getSold_modifier(), orderStruk.getData_payment(), orderStruk.getData_product_transaction(), orderStruk.getData_modifier_transaction(), orderStruk.getRounding());
            }

            @Override
            public void onFailure(Call<GetShiftOrderStruk> call, Throwable t) {
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
    public static final int PERMISSION_ACCESS_FINE_LOCATION = 5;
    public static final int PERMISSION_ACCESS_COARSE_LOCATION = 6;

    public OnBluetoothPermissionsGranted onBluetoothPermissionsGranted;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_BLUETOOTH) {
            boolean allPermissionsGranted = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (allPermissionsGranted) {
                // Semua izin diberikan
                if (onBluetoothPermissionsGranted != null) {
                    onBluetoothPermissionsGranted.onPermissionsGranted();
                }
            } else {
                // Setidaknya satu izin ditolak
                // Berikan penjelasan kepada pengguna jika perlu
                Toast.makeText(this, "Izin Bluetooth diperlukan untuk mencetak.", Toast.LENGTH_SHORT).show();
            }
        }

//        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            switch (requestCode) {
//                case MainActivity.PERMISSION_BLUETOOTH:
//                case MainActivity.PERMISSION_BLUETOOTH_ADMIN:
//                case MainActivity.PERMISSION_BLUETOOTH_CONNECT:
//                case MainActivity.PERMISSION_BLUETOOTH_SCAN:
//                    this.checkBluetoothPermissions(this.onBluetoothPermissionsGranted);
//                    break;
//            }
//        }
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PERMISSION_BLUETOOTH:
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth diaktifkan, lanjutkan operasi
                    requestBluetoothPermissions(this);
                } else {
                    // Bluetooth tidak diaktifkan, informasikan kepada pengguna
                    startBluetoothOperations(this);
                    Toast.makeText(this, "Bluetooth harus diaktifkan untuk menggunakan printer thermal.", Toast.LENGTH_SHORT).show();
//                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                    this.startActivityForResult(enableBtIntent, PERMISSION_BLUETOOTH);
//                    requestBluetoothPermissions(this);
                }
                break;
            // Tambahkan kasus untuk kode hasil lainnya jika diperlukan
            default:
                break;
        }
    }


    private void requestBluetoothPermissions(Activity activity) {
        String[] permissions = {
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            permissions = new String[]{
//                    Manifest.permission.BLUETOOTH_CONNECT,
//                    Manifest.permission.BLUETOOTH_SCAN,
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//            };
//        } else {
//            permissions = new String[]{
//                    Manifest.permission.BLUETOOTH,
//                    Manifest.permission.BLUETOOTH_ADMIN,
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//            };
//        }


        // Daftar izin yang belum diberikan
        boolean allPermissionsGranted = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false;
                break;
            }
        }

        if (!allPermissionsGranted) {
            Log.d(TAG, "requestBluetoothPermissions: Masok Permission UnGranted");
            // Minta izin jika belum diberikan
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_BLUETOOTH);
//            checkBluetoothPermissions(this.onBluetoothPermissionsGranted);

        } else {
            bluetoothDevicesList = (new BluetoothPrintersConnections()).getList();
            if(bluetoothDevicesList != null){
                if(bluetoothDevicesList.length > 0){
                    selectedDevice = bluetoothDevicesList[0];
                }
            }
            // Semua izin sudah diberikan
            Log.d(TAG, "requestBluetoothPermissions: " + onBluetoothPermissionsGranted);
            if (onBluetoothPermissionsGranted != null) {
//                onBluetoothPermissionsGranted.onPermissionsGranted();
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                activity.startActivityForResult(enableBtIntent, PERMISSION_BLUETOOTH);
            }
        }
    }

    @SuppressLint("MissingPermission")
    private static void startBluetoothOperations(Activity activity) {
//         Kode untuk memulai operasi Bluetooth setelah permission diberikan
//         Misalnya, inisialisasi BluetoothAdapter, memulai pemindaian, dll.
//         Contoh:
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            if (!bluetoothAdapter.isEnabled()) {
                // Minta pengguna untuk mengaktifkan Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                activity.startActivityForResult(enableBtIntent, PERMISSION_BLUETOOTH);
            } else {
                // Mulai pemindaian perangkat Bluetooth
//                 startScan();
            }
        } else {
            // Perangkat tidak mendukung Bluetooth
            Toast.makeText(activity, "Perangkat tidak mendukung Bluetooth", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkBluetoothStatus() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            // Perangkat tidak mendukung Bluetooth
            Toast.makeText(this, "Perangkat ini tidak mendukung Bluetooth", Toast.LENGTH_SHORT).show();
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                // Bluetooth tidak aktif, tampilkan dialog untuk mengaktifkannya
                showBluetoothAlert();
            } else {
                // Bluetooth aktif, lanjutkan dengan logika aplikasi Anda
                Toast.makeText(this, "Bluetooth sudah aktif", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showBluetoothAlert() {
        new AlertDialog.Builder(this)
                .setTitle("Aktifkan Bluetooth")
                .setMessage("Bluetooth tidak aktif. Apakah Anda ingin mengaktifkannya?")
                .setPositiveButton("Ya", (dialog, which) -> {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, PERMISSION_BLUETOOTH);
                })
                .setNegativeButton("Tidak", (dialog, which) -> dialog.dismiss())
                .show();
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

    public void printBluetooth(Transactions transactions, List<TransactionItems> transactionItems, User user, String device, boolean isOrder, List<NoteReceiptScheduler> noteReceiptSchedulers) {
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

            printUsingFirstPrinter(transactions, transactionItems, user, device);

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
                ).execute(this.getAsyncEscPosPrinter(selectedDevice, transactions, transactionItems, noteReceiptSchedulers));
            });
        }
    }

    public void printBluetoothHistoryTransaction(Transactions transactions, List<TransactionItems> transactionItems) {
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
            ).execute(this.getAsyncEscPosPrinterHistoryTransaction(selectedDevice, transactions, transactionItems));
        });
    }

    public void printShiftOrder(PattyCash pattyCash, int soldProduct, int soldModifier, List<DataPayment> dataPayments, List<DataProductTransaction> dataProductTransactions, List<DataModifierTransaction> dataModifierTransactions, int rounding){
        this.checkBluetoothPermissions(() -> {
            new AsyncBluetoothEscPosPrint(this, new AsyncEscPosPrint.OnPrintFinished() {
                @Override
                public void onError(AsyncEscPosPrinter asyncEscPosPrinter, int codeException) {
                    Log.e("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : An error occurred !");
                }

                @Override
                public void onSuccess(AsyncEscPosPrinter asyncEscPosPrinter) {
                    Log.i("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : Print is finished !");
                }
            }).execute(this.getAsyncPrintShiftOrder(
                    selectedDevice,
                    pattyCash,
                    soldProduct,
                    soldModifier,
                    dataPayments,
                    dataProductTransactions,
                    dataModifierTransactions,
                    rounding));
        });
    }

    public void printBluetoothCetakNotBill(DeviceConnection printerConnection, String namaOutlet, String nomorTelfonOutlet, String userCollect ,JSONArray listItem, JSONArray listPajak, int nominalDiskon, int nominalRounding) {
        this.checkBluetoothPermissions(() -> {
            try {
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
                ).execute(this.getAsyncEscPosPrinterCetakNotBill(selectedDevice, namaOutlet, nomorTelfonOutlet, userCollect ,listItem, listPajak, nominalDiskon, nominalRounding));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
    }


    @SuppressLint("SimpleDateFormat")
    public AsyncEscPosPrinter getAsyncEscPosPrinter(DeviceConnection printerConnection, Transactions transactions, List<TransactionItems> transactionItems, List<NoteReceiptScheduler> noteReceiptSchedulers) {
        String item = "";
        String taxItem = "";
        String noteSchedulerText = "";
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
                item +=  "[L]<b>" +namaModifier+"</b>\n";
            }

            item += "[L]<b>"+ data.getTotal_count() + "x" + "</b>[C]<b>@" +data.getTotal_transaction()+"</b>\n";
            subTotal += Integer.parseInt(data.getTotal_transaction());
//            if(data.getVariant() != null){
//                item += "[L]<b>"+ data.getTotal_count() + "x" + "</b>[C]<b>@" +data.getTotal_transaction()+"</b>\n";
//                subTotal += Integer.parseInt(data.getTotal_transaction());
//            }else{
//                item += "[L]<b>"+ data.getTotal_count() + "x" + "</b>[C]<b>@" +data.getTotal_transaction()+"</b>\n";
//                subTotal += Integer.parseInt(data.getTotal_transaction());
//            }

            if(data.getCatatan() != null && !Objects.equals(data.getCatatan(), "")){
                item += "[L]" +data.getCatatan()+"\n";
            }

            Log.d(TAG, "getAsyncEscPosPrinter: " + data.getCatatan());

            item += "[C]--------------------------------\n";
        }

        for(NoteReceiptScheduler noteReceiptScheduler : noteReceiptSchedulers){
            noteSchedulerText += "[C]" + noteReceiptScheduler.getMessage() + "\n";
            noteSchedulerText += "[C]--------------------------------\n";
        }

        for(Tax tax : transactions.getTax()){
            int valueTax = (Integer.parseInt(tax.getAmount()) * (subTotal - Integer.parseInt(transactions.getTotal_diskon()))) / 100;
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

        String catatanNota = "";

        if(transactions.getOutlet().getCatatan_nota() != null){
            catatanNota += transactions.getOutlet().getCatatan_nota();
            catatanNota += "\n";
        }
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
                        "[C]--------------------------------\n" +
                        noteSchedulerText +
                        "\n" +
                        catatanNota +
                        "[C]--------------------------------\n" +
                        "[C]<font size='small'>TERIMA KASIH</font>\n"
        );
    }

    @SuppressLint("SimpleDateFormat")
    public AsyncEscPosPrinter getAsyncEscPosPrinterHistoryTransaction(DeviceConnection printerConnection, Transactions transactions, List<TransactionItems> transactionItems) {
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
                item +=  "[L]<b>" +namaModifier+"</b>\n";
            }

            item += "[L]<b>"+ data.getTotal_count() + "x" + "</b>[C]<b>@" +data.getTotal_transaction()+"</b>\n";
            subTotal += Integer.parseInt(data.getTotal_transaction());

//            if(data.getVariant() != null){
//                item += "[L]<b>"+ 1 + "x" + "</b>[C]<b>@" +data.getVariant().getHarga()+"</b>\n";
//                subTotal += data.getVariant().getHarga();
//            }

            if(data.getCatatan() != null && !Objects.equals(data.getCatatan(), "")){
                item += "[L]" +data.getCatatan()+"\n";
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

        String catatanNota = "";

        if(transactions.getOutlet().getCatatan_nota() != null){
            catatanNota += transactions.getOutlet().getCatatan_nota();
            catatanNota += "\n";
        }

        SimpleDateFormat format = new SimpleDateFormat("'on' yyyy-MM-dd 'at' HH:mm:ss");
        AsyncEscPosPrinter printer = new AsyncEscPosPrinter(printerConnection, 203, 48f, 32);
//        Default paper width biasanya bawaan dari hardware printer (misal 58mm, 72mm, 80mm).
//        Printer 58mm → 32 karakter per baris (default umum)
//        Printer 80mm → 48 karakter per baris
        return printer.addTextToPrint(
                "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, this.getApplicationContext().getResources().getDrawableForDensity(R.drawable.logo_red, DisplayMetrics.DENSITY_MEDIUM)) + "</img>\n" +
                        "[L]\n" +
                        "[C]" + transactions.getOutlet().getAddress() + "\n" +
                        "[C]" + transactions.getOutlet().getPhone()+ "\n" +
                        "[L]" + formattedDate + "[C][R]" + formattedTime + "\n" +
                        "[L]Collected By [C][R]" + transactions.getUser().getName() + "\n" +
                        "[C]--------------------------------\n" +
                        "[C]<font size='big'>THIS IS COPY</font>\n"+
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
                        catatanNota +
                        "[C]--------------------------------\n" +
                        "[C]<font size='small'>TERIMA KASIH</font>\n"
        );
    }

    @SuppressLint("MissingPermission")
    public AsyncEscPosPrinter getAsyncPrintOpenBill(DeviceConnection printerConnection, OpenBill data){
        try {
            String item = "";
            Log.d(TAG, "getAsyncPrintOpenBill: " + data);
            if (!data.getItem().isEmpty()){
                for (ItemOpenBill itemOpenBill : data.getItem()){
                    if(itemOpenBill.getProduct_id() != null || itemOpenBill.getProduct_id() != "null"){
                        if(Objects.equals(itemOpenBill.getNama_product(), itemOpenBill.getNama_variant())){
                            item += "[L]<b>"+ itemOpenBill.getQuantity() + "x " + itemOpenBill.getNama_product() + "</b>[C] \n";
                        }else{
                            item += "[L]<b>"+ itemOpenBill.getQuantity() + "x " + itemOpenBill.getNama_product() + "-" + itemOpenBill.getNama_variant() + "</b>[C] \n";
                        }

                    }else{
                        item += "[L]<b>"+ itemOpenBill.getQuantity() + "x " + "custom" +"</b>[C]\n";
                    }

                    for(ModifierOpenBill modifierOpenBill: itemOpenBill.getModifier()){
                        item +=  "[L]" +modifierOpenBill.getName()+"\n";
                    }

                    if(itemOpenBill.getCatatan() != null && !Objects.equals(itemOpenBill.getCatatan(), "")){
                        item += "[L]" +itemOpenBill.getCatatan()+"\n";
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
//            logErrorToApi(e, data);
            throw new RuntimeException(e);
        }
    }

    @SuppressLint("MissingPermission")
    public AsyncEscPosPrinter  getAsyncEscPosPrinterTcpOpenBill(DeviceConnection printerConnection, List<ItemOpenBill> data, OpenBill dataOpenBill){
        try {
            String item = "";
            Log.d(TAG, "getAsyncPrintOpenBill: " + data);
            if (!data.isEmpty()){
                for (ItemOpenBill itemOpenBill : data){
                    if(itemOpenBill.getProduct_id() != null || itemOpenBill.getProduct_id() != "null"){
                        if(Objects.equals(itemOpenBill.getNama_product(), itemOpenBill.getNama_variant())){
                            item += "[L]<b>"+ itemOpenBill.getQuantity() + "x " + itemOpenBill.getNama_product() + "</b>[C] \n";
                        }else{
                            item += "[L]<b>"+ itemOpenBill.getQuantity() + "x " + itemOpenBill.getNama_product() + "-" + itemOpenBill.getNama_variant() + "</b>[C] \n";
                        }

                    }else{
                        item += "[L]<b>"+ itemOpenBill.getQuantity() + "x " + "custom" +"</b>[C]\n";
                    }

                    for(ModifierOpenBill modifierOpenBill: itemOpenBill.getModifier()){
                        item +=  "[L]" +modifierOpenBill.getName()+"\n";
                    }

                    if(itemOpenBill.getCatatan() != null && !Objects.equals(itemOpenBill.getCatatan(), "")){
                        item += "[L]" +itemOpenBill.getCatatan()+"\n";
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
                            "[L]" + dataOpenBill.getUser().getName() + "[C][R]" + deviceBrand + "\n" +
                            "[C]--------------------------------\n" +
                            "[C]Dine In\n" +
                            "[C]--------------------------------\n" +
                            item
            );

        } catch (Exception e) {
//            logErrorToApi(e, data);
            throw new RuntimeException(e);
        }
    }

    @SuppressLint("MissingPermission")
    public AsyncEscPosPrinter getAsyncEscPosPrinterOrder(DeviceConnection printerConnection, Transactions transactions, List<TransactionItems> transactionItems, User user, String device){
        String item = "";
        String deviceBrand = Build.BRAND;

        for (TransactionItems data : transactionItems){
            if(data.getProduct() != null){
                if(Objects.equals(data.getProduct().getName(), data.getVariant().getName())){
                    item += "[L]<b>"+ data.getTotal_count() + "x " + data.getProduct().getName() + "</b>[C] \n";
                }else{
                    item += "[L]<b>"+ data.getTotal_count() + "x " + data.getProduct().getName() + " - " +data.getVariant().getName() + "</b>[C] \n";
                }

            }else{
                item += "[L]<b>"+ 1 + "x " + "custom" +"</b>[C]\n";
            }

            for(String namaModifier: data.getModifier()){
                item +=  "[L]" +namaModifier+"\n";
            }
            if(data.getCatatan() != null && !Objects.equals(data.getCatatan(), "")){
                item += "[L]" +data.getCatatan()+"\n";
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
                        "[L]" + user.getName() + "[C][R]" + deviceBrand + "\n" +
                        "[C]--------------------------------\n" +
                        "[C]Dine In\n" +
                        "[C]--------------------------------\n" +
                        item
        );
    }

    public AsyncEscPosPrinter getAsyncPrintShiftOrder(DeviceConnection printerConnection,
                                                      PattyCash pattyCash,
                                                      int soldProduct,
                                                      int soldModifier,
                                                      List<DataPayment> dataPayments,
                                                      List<DataProductTransaction> listProductTransaction,
                                                      List<DataModifierTransaction> listModifierTransaction,
                                                      int rounding){
        String userClose = pattyCash.getUser_ended() != null ? pattyCash.getUser_ended().getName() : "-";
        String closeTime = pattyCash.getClose() != null ? pattyCash.getClose() : "-";

        int totalAmount = 0;
        int totalCash = 0;

        String actualEndingCash = pattyCash.getAmount_akhir() != null ? formatRupiah(String.valueOf(pattyCash.getAmount_akhir()), "Rp. ") : "";

        String paymentDetail = "";
        int totalTransaction = 0;

        for (DataPayment dataPayment : dataPayments){
            paymentDetail += "[L]<b>"+ dataPayment.getName() + " PAYMENT" + "</b>\n";

            if ("Cash".equals(dataPayment.getName())) {
                for(Transactions transaction : dataPayment.getTransactions()){
                    totalCash += Integer.parseInt(transaction.getTotal());
                    totalTransaction += Integer.parseInt(transaction.getTotal());
                }
                paymentDetail += "[L]Cash Sales:[C]" + formatRupiah(String.valueOf(totalCash), "Rp. ") + "\n" +
                        "[C]--------------------------------\n";
            }else{
                int totalPayment = 0;
                for(Payment payment : dataPayment.getPayment()){
                    int total = 0;
                    for(Transactions paymentTransaction : payment.getTransactions()){
                        total += Integer.parseInt(paymentTransaction.getTotal());
                        totalPayment += Integer.parseInt(paymentTransaction.getTotal());
                    }
                    paymentDetail += "[L]<b>" + payment.getName() + "</b>[C]" + formatRupiah(String.valueOf(total), "Rp. ") + "\n";
                }
                paymentDetail += "[L]<b>TOTAL AMOUNT</b>[C]" + formatRupiah(String.valueOf(totalPayment), "Rp. ") + "\n" +
                        "[C]--------------------------------\n";
                totalTransaction += totalPayment;
            }
        }

        int expectedEndingCash = totalCash + Integer.parseInt(pattyCash.getAmount_awal());

        int convertActualEndingCash = Objects.equals(actualEndingCash, "") ? 0 : Math.round(getAmount(actualEndingCash));

        int calculateCashDifference = convertActualEndingCash - expectedEndingCash;
        Log.d(TAG, "getAsyncPrintShiftOrder: " + calculateCashDifference);
        String cashDifference = !Objects.equals(actualEndingCash, "") ? formatRupiah(String.valueOf(calculateCashDifference), "Rp. ") : "";

        String productItem = "";
        for (DataProductTransaction dataProductTransaction : listProductTransaction){
            if(Objects.equals(dataProductTransaction.getProduct().getName(), dataProductTransaction.getName())){
                productItem += "[L]" + dataProductTransaction.getProduct().getName()+"\n" +
                        "[L]"+ dataProductTransaction.getTotal_transaction() +"[C]" + dataProductTransaction.getTotal_transaction_amount() + "\n";
            }else{
                productItem += "[L]" + dataProductTransaction.getProduct().getName() + " - " + dataProductTransaction.getName() +"\n" +
                        "[L]"+ dataProductTransaction.getTotal_transaction() +"[C]" + dataProductTransaction.getTotal_transaction_amount() + "\n";
            }
            int totalHargaProduct = dataProductTransaction.getHarga() * dataProductTransaction.getTotal_transaction();
            totalAmount += totalHargaProduct;
        }

        String modifierItem = "";
        for(DataModifierTransaction dataModifierTransaction : listModifierTransaction){
            modifierItem += "[L]" + dataModifierTransaction.getName()+"\n" +
                    "[L]"+ dataModifierTransaction.getTotal_transaction() +"[C]" + dataModifierTransaction.getTotal_transaction_amount() + "\n";

            int totalHargaModifier = dataModifierTransaction.getHarga() * dataModifierTransaction.getTotal_transaction();
            totalAmount += totalHargaModifier;
        }


        AsyncEscPosPrinter printer = new AsyncEscPosPrinter(printerConnection, 203, 48f, 32);
        return printer.addTextToPrint(
                "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, this.getApplicationContext().getResources().getDrawableForDensity(R.drawable.logo_red, DisplayMetrics.DENSITY_MEDIUM)) + "</img>\n" +
                        "[L]\n" +
                        "[C]" + pattyCash.getOutlet().getAddress() + "\n" +
                        "[C]" + pattyCash.getOutlet().getPhone()+ "\n" +
                        "[C]--------------------------------\n" +
                        "[C]<font size='medium'>SHIFT PRINT</font>\n"+
                        "[C]--------------------------------\n" +
                        "[L]Open Name :[C]" + pattyCash.getUser_started().getName() + "\n" +
                        "[L]Close Name :[C]" + userClose + "\n" +
                        "[L]Start Date :[C]" + pattyCash.getOpen() + "\n" +
                        "[L]End Date :[C]" + closeTime +"\n" +
                        "[L]Sold Product :[C]" + soldProduct +"\n" +
                        "[L]Sold Modifier :[C]" + soldModifier +"\n" +
                        "[C]--------------------------------\n" +
                        "[C]<font size='medium'>CASH MANAGEMENT</font>\n"+
                        "[C]--------------------------------\n" +
                        "[L]<font size='medium'>Starting Cash Drawer:</font>[C]" + formatRupiah(String.valueOf(pattyCash.getAmount_awal()), "Rp. ") + "\n" +
                        "[L]Cash Payment :[C]" + formatRupiah(String.valueOf(totalCash), "Rp. ") + "\n" +
                        "[L]Expected Ending Cash :[C]" + formatRupiah(String.valueOf(expectedEndingCash), "Rp. ") + "\n" +
                        "[L]Actual Ending Cash :[C]" + actualEndingCash +"\n" +
                        "[L]Cash Difference :[C]" + cashDifference +"\n" +
                        "[C]--------------------------------\n" +
                        "[C]<font size='medium'>Order Details</font>\n"+
                        "[C]--------------------------------\n" +
                        "[L]<b>Sold Product</b>\n" +
                        productItem +
                        "\n" +
                        "[L]<b>Sold Modifier</b>\n" +
                        modifierItem +
                        "\n" +
                        "[L]Rounding :[C]" + formatRupiah(String.valueOf(rounding), "Rp. ") +"\n" +
                        "[L]Total Amount :[C]" + formatRupiah(String.valueOf(totalAmount), "Rp. ") +"\n" +
                        "[C]--------------------------------\n" +
                        "[C]<font size='medium'>PAYMENT DETAILS</font>\n"+
                        "[C]--------------------------------\n" +
                        paymentDetail +
                        "[L]<b>TOTAL TRANSACTION</b>[C]" + formatRupiah(String.valueOf(totalTransaction), "Rp. ") +"\n"
        );
    }

    @SuppressLint("SimpleDateFormat")
    public AsyncEscPosPrinter getAsyncEscPosPrinterCetakNotBill(DeviceConnection printerConnection, String namaOutlet, String nomorTelfonOutlet, String userCollect ,JSONArray listItem, JSONArray listPajak, int nominalDiskon, int nominalRounding) throws JSONException {

        String item = "";
        String taxItem = "";
        int totalPajak = 0;
        int subTotal = 0;
        int total = 0;

        // Mengolah array item
        for (int i = 0; i < listItem.length(); i++) {
            JSONObject product = listItem.getJSONObject(i);
            String namaProduct = product.getString("namaProduct");
            String namaVariant = product.getString("namaVariant");
            if(!namaProduct.equals("custom")){
                if(Objects.equals(namaProduct, namaVariant)){
                    item += "[L]<b>" + namaProduct +"</b>\n";
                }else{
                    item += "[L]<b>" + namaProduct + " - " + namaVariant +"</b>\n";
                }
            }else{
                item += "[L]<b>" + "custom" +"</b>\n";
            }
            String qty = product.getString("quantity");

            JSONArray modifiers = product.optJSONArray("modifier");
            int totalHargaModifier = 0;
            if (modifiers != null) {
                for (int j = 0; j < modifiers.length(); j++) {
                    JSONObject modifier = modifiers.getJSONObject(j);
                    String namaModifier = modifier.getString("nama");
                    int hargaModifier = modifier.getInt("harga");
                    int resultHargaModifier = Integer.parseInt(qty) * hargaModifier;
                    totalHargaModifier += resultHargaModifier;

                    item +=  "[C]<b>" +namaModifier+"</b>[R]"+ formatRupiah(String.valueOf(resultHargaModifier), "Rp. ") +"\n";
                }
            }


            int harga = product.getInt("harga");
            int hargaTotal = Integer.parseInt(qty) * harga;
            int resultHargaTotal = hargaTotal + totalHargaModifier;

            total += resultHargaTotal;

            item += "[L]<b>"+ qty + "x" + "</b>[C]<b>@" + harga +"</b>[R]"+ formatRupiah(String.valueOf(resultHargaTotal), "Rp. ") +"\n";

        }

//        Mengolah Array Pajak
        for (int i = 0; i < listPajak.length(); i++) {
            JSONObject pajak = listPajak.getJSONObject(i);
            String namaPajak = pajak.getString("name");
            int resultPajak = pajak.getInt("total");

            taxItem += "[L]<b>"+ namaPajak + "(%)" + "</b>[R]<b>" + formatRupiah(String.valueOf(resultPajak), "Rp. ") +"</b>\n";
            totalPajak += resultPajak;

            total += totalPajak;
        }


        // Buat objek Date (misalnya, saat ini)
        Date date = new Date();

        // Buat instance SimpleDateFormat untuk format tanggal
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        // Buat instance SimpleDateFormat untuk format waktu
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

        // Format tanggal dan waktu
        String formattedDate = dateFormat.format(date);
        String formattedTime = timeFormat.format(date);

        total -= nominalDiskon;
        total += nominalRounding;

        SimpleDateFormat format = new SimpleDateFormat("'on' yyyy-MM-dd 'at' HH:mm:ss");
        AsyncEscPosPrinter printer = new AsyncEscPosPrinter(printerConnection, 203, 48f, 32);
        return printer.addTextToPrint(
                "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, this.getApplicationContext().getResources().getDrawableForDensity(R.drawable.logo_red, DisplayMetrics.DENSITY_MEDIUM)) + "</img>\n" +
                        "[L]\n" +
                        "[C]" + namaOutlet + "\n" +
                        "[C]" + nomorTelfonOutlet+ "\n" +
                        "[L]" + formattedDate + "[C][R]" + formattedTime + "\n" +
                        "[L]Collected By [C][R]" + userCollect + "\n" +
                        "[C]--------------------------------\n" +
                        "[C]<font size='medium'>THIS IS NOT BILL</font>\n"+
                        "[C]--------------------------------\n" +
                        "[C] *dine in* \n" +
                        item +
                        "[L]Subtotal :[C]" + formatRupiah(String.valueOf(subTotal), "Rp. ") + "\n" +
                        "[L]Discount :[C]" + formatRupiah(String.valueOf(nominalDiskon), "Rp. ") + "\n" +
                        "[L]Rounding :[C]" + formatRupiah(String.valueOf(nominalRounding), "Rp. ") +"\n" +
                        taxItem +
                        "[C]--------------------------------\n" +
                        "[L]Total :[C]" + formatRupiah(String.valueOf(total), "Rp. ") + "\n"

        );
    }

//    public void printTcp() {
//        try {
//            new AsyncTcpEscPosPrint(
//                    this,
//                    new AsyncEscPosPrint.OnPrintFinished() {
//                        @Override
//                        public void onError(AsyncEscPosPrinter asyncEscPosPrinter, int codeException) {
//                            Log.e("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : An error occurred !");
//                        }
//
//                        @Override
//                        public void onSuccess(AsyncEscPosPrinter asyncEscPosPrinter) {
//                            Log.i("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : Print is finished !");
//                        }
//                    }
//            )
//                    .execute(
//                            this.getAsyncEscPosPrinterOrder(
//                                    new TcpConnection(
//                                            ip,
//                                            port
//                                    ),  transactions, transactionItems, user, device
//                            )
//                    );
//        } catch (NumberFormatException e) {
//            new AlertDialog.Builder(this)
//                    .setTitle("Invalid TCP port address")
//                    .setMessage("Port field must be an integer.")
//                    .show();
//            e.printStackTrace();
//        }
//    }

    @SuppressLint("SimpleDateFormat")
    public AsyncEscPosPrinter getAsyncEscPosPrinterTest(DeviceConnection printerConnection) {
        SimpleDateFormat format = new SimpleDateFormat("'on' yyyy-MM-dd 'at' HH:mm:ss");
        AsyncEscPosPrinter printer = new AsyncEscPosPrinter(printerConnection, 203, 48f, 32);
        return printer.addTextToPrint(
                "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, this.getApplicationContext().getResources().getDrawableForDensity(R.drawable.logo_red, DisplayMetrics.DENSITY_MEDIUM)) + "</img>\n" +
                        "[L]\n" +
                        "[C]<u><font size='big'>ORDER N°045</font></u>\n" +
                        "[L]\n" +
                        "[C]<u type='double'>" + format.format(new Date()) + "</u>\n" +
                        "[C]\n" +
                        "[C]================================\n" +
                        "[L]\n" +
                        "[L]<b>BEAUTIFUL SHIRT</b>[R]9.99€\n" +
                        "[L]  + Size : S\n" +
                        "[L]\n" +
                        "[L]<b>AWESOME HAT</b>[R]24.99€\n" +
                        "[L]  + Size : 57/58\n" +
                        "[L]\n" +
                        "[C]--------------------------------\n" +
                        "[R]TOTAL PRICE :[R]34.98€\n" +
                        "[R]TAX :[R]4.23€\n" +
                        "[L]\n" +
                        "[C]================================\n" +
                        "[L]\n" +
                        "[L]<u><font color='bg-black' size='tall'>Customer :</font></u>\n" +
                        "[L]Raymond DUPONT\n" +
                        "[L]5 rue des girafes\n" +
                        "[L]31547 PERPETES\n" +
                        "[L]Tel : +33801201456\n" +
                        "\n" +
                        "[C]<barcode type='ean13' height='10'>831254784551</barcode>\n" +
                        "[L]\n" +
                        "[C]<qrcode size='20'>https://dantsu.com/</qrcode>\n"
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

        @JavascriptInterface
        public void handleMenuDetailSetting(){
            Intent i = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(i);
        }

        @JavascriptInterface
        public void handleCetakBillNotReceipt(String jsonData){
            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                Log.d(TAG, "handleCetakBillNotReceipt: " + jsonObject);
                JSONArray listItem = jsonObject.getJSONArray("listItem");
                JSONObject outlet = jsonObject.getJSONObject("outlet");
                JSONArray listPajak = jsonObject.getJSONArray("listPajak");
                int nominalDiskon = jsonObject.getInt("nominalDiskon");
                int nominalRounding = jsonObject.getInt("nominalRounding");


                String namaOutlet = outlet.getString("name");
                String telfon = outlet.getString("phone");

                JSONObject userCollect = jsonObject.getJSONObject("userCollect");
                String nameUserCollect = userCollect.getString("name");

                printBluetoothCetakNotBill(selectedDevice, namaOutlet, telfon, nameUserCollect ,listItem, listPajak, nominalDiskon, nominalRounding);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void printUsingFirstPrinter(Transactions transactions, List<TransactionItems> transactionItems, User user, String device) {
        PrinterDao printerDao = db.printerDao();

        new Thread(() -> {
            Printer firstPrinter = printerDao.getFirstPrinter();

            List<TransactionItems> filterItem = filterTransactionItemsByPrinterCategory(transactionItems, firstPrinter);


            if (firstPrinter != null && filterItem.size() > 0) {
                String ip = firstPrinter.getIp();
                int port = firstPrinter.getPort();

                runOnUiThread(() -> {
                    try {
                        new AsyncTcpEscPosPrint(
                                this,
                                new AsyncEscPosPrint.OnPrintFinished() {
                                    @Override
                                    public void onError(AsyncEscPosPrinter printer, int codeException) {
                                        Log.e("Async.OnPrintFinished", "An error occurred! Code: " + codeException);
                                    }

                                    @Override
                                    public void onSuccess(AsyncEscPosPrinter printer) {
                                        Log.i("Async.OnPrintFinished", "Print finished successfully!");
                                    }
                                }
                        ).execute(
                                getAsyncEscPosPrinterOrder(
                                        new TcpConnection(ip, port, 30000),
                                        transactions, filterItem, user, device
                                )
                        );
                    } catch (NumberFormatException e) {
                        new AlertDialog.Builder(this)
                                .setTitle("Invalid TCP port address")
                                .setMessage("Port field must be an integer.")
                                .show();
                        e.printStackTrace();
                    }
                });
            } else {
                runOnUiThread(() -> {
                    Toast.makeText(this, "No printer wifi data found", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private void printTcpOpenBill(OpenBill openBill){
        PrinterDao printerDao = db.printerDao();

        new Thread(() -> {
            Printer firstPrinter = printerDao.getFirstPrinter();

            List<ItemOpenBill> filterItem = filterOpenBillItemByPrinterCategory(openBill.getItem(), firstPrinter);


            if (firstPrinter != null && filterItem.size() > 0) {
                String ip = firstPrinter.getIp();
                int port = firstPrinter.getPort();

                runOnUiThread(() -> {
                    try {
                        new AsyncTcpEscPosPrint(
                                this,
                                new AsyncEscPosPrint.OnPrintFinished() {
                                    @Override
                                    public void onError(AsyncEscPosPrinter printer, int codeException) {
                                        Log.e("Async.OnPrintFinished", "An error occurred! Code: " + codeException);
                                    }

                                    @Override
                                    public void onSuccess(AsyncEscPosPrinter printer) {
                                        Log.i("Async.OnPrintFinished", "Print finished successfully!");
                                    }
                                }
                        ).execute(
                                getAsyncEscPosPrinterTcpOpenBill(new TcpConnection(ip, port, 30000), filterItem, openBill)
                        );
                    } catch (NumberFormatException e) {
                        new AlertDialog.Builder(this)
                                .setTitle("Invalid TCP port address")
                                .setMessage("Port field must be an integer.")
                                .show();
                        e.printStackTrace();
                    }
                });
            } else {
                if(firstPrinter != null){
                    runOnUiThread(() -> {
                        Toast.makeText(this, "No printer wifi data found", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        }).start();

    }

    // Fungsi filter transactionItems sesuai listCategory printer
    public List<TransactionItems> filterTransactionItemsByPrinterCategory(List<TransactionItems> transactionItems, Printer printer) {
        if (printer == null) {
            Log.e("filterTransactionItems", "Printer object is null");
            return transactionItems; // fallback: kembalikan semua jika printer null
        }

        List<Integer> allowedCategories = printer.getListCategory();
        if (allowedCategories == null || allowedCategories.isEmpty()) {
            // Jika listCategory printer kosong, kembalikan semua item tanpa filter
            return transactionItems;
        }

        List<TransactionItems> filteredList = new ArrayList<>();

        for (TransactionItems item : transactionItems) {
            Log.d("Check Category Item Transaksi", item.getProduct().getCategory_id());
            if (item.getProduct() != null && allowedCategories.contains(Integer.parseInt(item.getProduct().getCategory_id()))) {
                filteredList.add(item);
            }
        }

        return filteredList;
    }

    public List<ItemOpenBill> filterOpenBillItemByPrinterCategory(List<ItemOpenBill> openBillItems, Printer printer){
        if(printer == null){
            Log.e("filterTransactionItems", "Printer object is null");
            return openBillItems; // fallback: kembalikan semua jika printer null
        }

        List<ItemOpenBill> filteredList = new ArrayList<>();

        List<Integer> allowedCategories = printer.getListCategory();
        if(allowedCategories == null || allowedCategories.isEmpty()){
            // Jika listCategory printer kosong, kembalikan item kosong
            return filteredList;
        }


        for(ItemOpenBill item : openBillItems){
            Log.d("Check Category Item Transaksi", item.getProduct().getCategory_id());
            if (item.getProduct() != null && allowedCategories.contains(Integer.parseInt(item.getProduct().getCategory_id()))) {
                filteredList.add(item);
            }
        }

        return filteredList;
    }
}