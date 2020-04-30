package com.example.myapplication;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.test.espresso.idling.CountingIdlingResource;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.backendless.persistence.LoadRelationsQueryBuilder;
import com.example.myapplication.activity.CreateGroup;
import com.example.myapplication.activity.GroupList;
import com.example.myapplication.adapter.GroupAdapter;
import com.example.myapplication.data.Group;
import com.example.myapplication.data.Group_Place_User;
import com.example.myapplication.utility.TestApplication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class DataLoaderHelperTest {


    public void loadCreateGroupData() {
        Backendless.UserService.login("test@test.it", "test", new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                TestApplication.user = response;
                TestApplication.groups = new ArrayList<>();
                TestApplication.group_place_users = new ArrayList<>();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
            }
        });

    }


    public void deleteGroups() {
        Backendless.Data.of(Group_Place_User.class).remove(TestApplication.group_place_users.get(0), new AsyncCallback<Long>() {
            @Override
            public void handleResponse(Long response) {
                Log.i("Group_place_user", response.toString());

                Backendless.Data.of(Group.class).remove(TestApplication.groups.get(0), new AsyncCallback<Long>() {
                    @Override
                    public void handleResponse(Long response) {
                    Log.i("Group", response.toString());
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {

                    }
                });
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });
    }
}