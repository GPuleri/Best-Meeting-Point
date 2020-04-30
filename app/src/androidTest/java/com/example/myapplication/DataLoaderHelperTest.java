package com.example.myapplication;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

    public void loadTestData() {
        Backendless.UserService.login("test@test.it", "test", new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                TestApplication.user = response;
                Log.e("user", TestApplication.user.toString());
                LoadRelationsQueryBuilder<Group_Place_User> loadRelationsQueryBuilder;
                loadRelationsQueryBuilder = LoadRelationsQueryBuilder.of(Group_Place_User.class);
                loadRelationsQueryBuilder.setRelationName("group_user");

                Backendless.Data.of(BackendlessUser.class).loadRelations(TestApplication.user.getObjectId(),
                        loadRelationsQueryBuilder,
                        new AsyncCallback<List<Group_Place_User>>() {

                            @Override
                            public void handleResponse(List<Group_Place_User> response) {
                                TestApplication.group_place_users = response;
                                Log.i("group_place", "utente creato");

                                StringBuilder whereClause = new StringBuilder();
                                for (int i = 0; i < TestApplication.group_place_users.size(); i++) {
                                    whereClause.append("group_group");
                                    whereClause.append(".objectId='").append(TestApplication.group_place_users.get(i).getObjectId()).append("'");
                                    if (i != TestApplication.group_place_users.size() - 1) {
                                        whereClause.append(" or ");
                                    }
                                }
                                DataQueryBuilder queryBuilder = DataQueryBuilder.create();
                                queryBuilder.setWhereClause(whereClause.toString());
                                Log.i("query", whereClause.toString());
                                Backendless.Data.of(Group.class).find(queryBuilder, new AsyncCallback<List<Group>>() {

                                    @Override
                                    public void handleResponse(List<Group> response) {
                                        if (!TestApplication.group_place_users.isEmpty()) {
                                            TestApplication.groups = response;
                                            Log.i("groups", "utente creato");

                                        }
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

            @Override
            public void handleFault(BackendlessFault fault) {
            }
        });

    }

    public void deleteGroups() {
//        TestApplication.user.
    }
}