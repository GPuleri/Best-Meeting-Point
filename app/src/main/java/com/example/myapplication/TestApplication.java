package com.example.myapplication;

import android.app.Application;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

public class TestApplication extends Application {
    public static final String APPLICATION_ID = "CC93AEC0-074E-BCAE-FFFE-748FA924E000";
    public static final String API_KEY = "278AD5E3-D39E-49EA-ACB4-495652BF6203";
    public static final String SERVER_URL = "https://api.backendless.com";

    public static BackendlessUser user;

    @Override
    public void onCreate() {
        super.onCreate();

        Backendless.setUrl( SERVER_URL );
        Backendless.initApp( getApplicationContext(),
                APPLICATION_ID,
                API_KEY );
    }

}
