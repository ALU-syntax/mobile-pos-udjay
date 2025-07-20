package com.udjaya.kasirudjay.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private static final String PREFS_NAME = "MyAppPrefs";
    private static final String KEY_IP = "ip_address";
    private static final String KEY_PORT = "port_number";

    private SharedPreferences sharedPreferences;
    private static SharedPrefManager instance;

    private SharedPrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    // Singleton instance getter
    public static synchronized SharedPrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefManager(context.getApplicationContext());
        }
        return instance;
    }

    public void saveIp(String ip) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_IP, ip);
        editor.apply();
    }

    public String getIp() {
        return sharedPreferences.getString(KEY_IP, "0.0.0.0");
    }

    public void savePort(int port) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_PORT, port);
        editor.apply();
    }

    public int getPort() {
        return sharedPreferences.getInt(KEY_PORT, 0);
    }

    public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
