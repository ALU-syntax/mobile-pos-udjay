package com.udjaya.kasirudjay.modul.setting;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.udjaya.kasirudjay.R;
import com.udjaya.kasirudjay.model.printer.Printer;
import com.udjaya.kasirudjay.modul.printer.PrinterFragment;

public class SettingActivity extends AppCompatActivity {

    private TextView menuPrinter;
    private TextView[] menus;

    private ImageButton btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnBack = findViewById(R.id.button_back);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Akhiri activity saat icon back ditekan
            }
        });

        // Inisialisasi views sidebar
        menuPrinter = findViewById(R.id.printer);

        menus = new TextView[]{menuPrinter};

        // Set click listener untuk menu
        menuPrinter.setOnClickListener(menuClickListener);

        // Default pilih menu All House
        selectMenu(menuPrinter);
        replaceFragment(new PrinterFragment());
    }


    // Event click untuk menu sidebar
    private View.OnClickListener menuClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            selectMenu((TextView) view);

            if (view == menuPrinter) {
                replaceFragment(new PrinterFragment());
            }
//            } else if (view == menuLivingRoom) {
//                replaceFragment(new LivingRoomFragment());
//            } else if (view == menuKitchen) {
//                replaceFragment(new KitchenFragment());
//            } else if (view == menuBedroom) {
//                replaceFragment(new BedroomFragment());
//            }
        }
    };

    private void selectMenu(TextView selectedMenu) {
        // Reset semua menu ke warna default
        for (TextView menu : menus) {
            menu.setTextColor(Color.parseColor("#999999"));
            menu.setBackgroundColor(Color.TRANSPARENT);
        }
        // Highlight menu yang dipilih
        selectedMenu.setTextColor(Color.WHITE);
        selectedMenu.setBackgroundColor(Color.parseColor("#333333"));
    }

    private void replaceFragment(PrinterFragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
    }
}