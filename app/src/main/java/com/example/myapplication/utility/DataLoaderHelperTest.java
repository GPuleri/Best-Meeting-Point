package com.example.myapplication.utility;

import android.util.Log;


import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.myapplication.data.Group;
import com.example.myapplication.data.Group_Place_User;
import java.util.ArrayList;

/**
 * Helper used to load data during the tests
 */
public class DataLoaderHelperTest {

    /**
     * Load the TestUser and instantiates groups and group_place_users as empty lists
     */
    public void loadUserData() {
        Backendless.UserService.login("test@test.it", "test", new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                TestApplication.user = response;
                TestApplication.groups = new ArrayList<>();
                TestApplication.group_place_users = new ArrayList<>();
                EspressoIdlingResource.decrement();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
            }
        });

    }

    /**
     * Delete the data (group and group_place_user) created during the tests
     */
    public void deleteGroups() {
        Backendless.Data.of(Group.class).remove(TestApplication.groups.get(0), new AsyncCallback<Long>() {
            @Override
            public void handleResponse(Long response) {
                Log.i("Group", response.toString());

            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });
        Backendless.Data.of(Group_Place_User.class).remove(TestApplication.group_place_users.get(0), new AsyncCallback<Long>() {
            @Override
            public void handleResponse(Long response) {
                Log.i("Group_place_user", response.toString());
                EspressoIdlingResource.decrement();
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });

    }
}