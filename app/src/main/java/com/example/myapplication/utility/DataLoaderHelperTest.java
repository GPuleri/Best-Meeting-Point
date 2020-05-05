package com.example.myapplication.utility;

import android.util.Log;


import androidx.annotation.Nullable;
import androidx.test.espresso.idling.CountingIdlingResource;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.example.myapplication.data.Group;
import com.example.myapplication.data.Group_Place_User;
import com.example.myapplication.data.Place;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper used to load data during the tests
 */
public class DataLoaderHelperTest {

    /**
     * Load the TestUser and instantiates groups and group_place_users as empty lists
     */
    public void loadUserData(String mail, String psw) {

        //EspressoIdlingResource.increment();
        Backendless.UserService.login(mail, psw, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                TestApplication.user = response;
                TestApplication.groups = new ArrayList<>();
                TestApplication.group_place_users = new ArrayList<>();
                TestApplication.place = new Place();
                String where= "ownerId='"+TestApplication.user.getObjectId()+"'";
                DataQueryBuilder queryBuilder = DataQueryBuilder.create();
                queryBuilder.setWhereClause(where);

                //EspressoIdlingResource.increment();
                Backendless.Persistence.of(Place.class).find(queryBuilder, new AsyncCallback<List<Place>>() {
                    @Override
                    public void handleResponse(List<Place> response) {
                        Log.i("posto", response.get(0).getFull_address());
                        TestApplication.place = response.get(0);
                        //EspressoIdlingResource.decrement();
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Log.e( "login", "server reported an error - " + fault.getMessage() );
                    }
                });
                //EspressoIdlingResource.decrement();
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
                EspressoIdlingResource.decrement();
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