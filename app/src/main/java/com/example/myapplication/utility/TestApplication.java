package com.example.myapplication.utility;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.RequiresApi;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

import com.example.myapplication.data.Group;
import com.example.myapplication.data.Group_Place_User;
import com.example.myapplication.data.Place;

import java.util.HashMap;
import java.util.List;

public class TestApplication extends Application {
    public static final String APPLICATION_ID = "CC93AEC0-074E-BCAE-FFFE-748FA924E000";
    public static final String API_KEY = "278AD5E3-D39E-49EA-ACB4-495652BF6203";
    public static final String SERVER_URL = "https://api.backendless.com";

    public static BackendlessUser user;
    public static Place place;
    public static List<Group_Place_User> group_place_users;
    public static List<Group> groups;
    public static List<Group> invitation_group;
    public static List<BackendlessUser> users_active;
    public static List<Place> places_active;
    public static List<Place> best_places;
    public static Group_Place_User link;
    public static Group group;
    public static final String[] kind_codes = {"bar", "cafe", "movie_theater", "night_club", "park", "restaurant"};
    public static final String[] kinds = {"Bar", "Cafe", "Movie Theater", "Night Club", "Park", "Restaurant"};
    public static int position_selected_group;

    /**
     * it creates the link with the database
     */
    @Override
    public void onCreate() {
        super.onCreate();

        Backendless.setUrl(SERVER_URL);
        Backendless.initApp(getApplicationContext(),
                APPLICATION_ID,
                API_KEY);
    }

    /**
     * it hides the keyboard
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void hideSoftKeyboard(Activity activity) {
        if (activity.getCurrentFocus() == null) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

}

