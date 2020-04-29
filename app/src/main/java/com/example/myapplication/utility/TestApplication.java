package com.example.myapplication.utility;

import android.app.Application;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

import com.example.myapplication.data.Group;
import com.example.myapplication.data.Group_Place_User;
import com.example.myapplication.data.Place;

import java.util.List;

public class TestApplication extends Application {
    public static final String APPLICATION_ID = "CC93AEC0-074E-BCAE-FFFE-748FA924E000";
    public static final String API_KEY = "278AD5E3-D39E-49EA-ACB4-495652BF6203";
    public static final String SERVER_URL = "https://api.backendless.com";

    public static BackendlessUser user;
    public static List<Group_Place_User> group_place_users;
    public static List<Group> groups;
    public static List<Group> invitation_group;
    public static List<BackendlessUser> users_active;
    public static List<Place> places_active;
    public static Group_Place_User link;
    public static Group group;

    /**
     * it creates the link with the database
     */
    @Override
    public void onCreate() {
        super.onCreate();

        Backendless.setUrl( SERVER_URL );
        Backendless.initApp( getApplicationContext(),
                APPLICATION_ID,
                API_KEY );
    }

}
