package com.example.chronos;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

import androidx.multidex.MultiDex;

public class MyApplication extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePal.initialize(this);
        MultiDex.install(this);
    }
    public static Context getContext() {
        return context;
    }
}
