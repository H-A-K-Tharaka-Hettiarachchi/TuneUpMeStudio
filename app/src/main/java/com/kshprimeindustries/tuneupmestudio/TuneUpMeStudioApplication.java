package com.kshprimeindustries.tuneupmestudio;

import android.app.Application;
import android.content.Intent;

import androidx.appcompat.app.AppCompatDelegate;

import com.kshprimeindustries.tuneupmestudio.listeners.FlipService;


public class TuneUpMeStudioApplication extends Application {

    public static String ngrock_url = "https://9578-111-223-181-202.ngrok-free.app";

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        startService(new Intent(this, FlipService.class));
    }

}
