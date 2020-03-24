package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.backendless.persistence.LoadRelationsQueryBuilder;
import com.example.myapplication.data.Group;
import com.example.myapplication.data.Group_Place_User;

import java.util.List;

public class GroupsList extends AppCompatActivity {
    ListView lvList;
    GroupsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groups_activity);

        lvList = findViewById(R.id.lvList);

        LoadRelationsQueryBuilder<Group_Place_User> loadRelationsQueryBuilder;

// Generic reference to CHILDCLASS is needed so that related objects
// from the servers are returned to the client as instances of CHILDCLASS.
        loadRelationsQueryBuilder = LoadRelationsQueryBuilder.of(Group_Place_User.class);
        loadRelationsQueryBuilder.setRelationName("group_user");

        Backendless.Data.of(BackendlessUser.class).loadRelations(TestApplication.user.getObjectId(),
                loadRelationsQueryBuilder,
                new AsyncCallback<List<Group_Place_User>>() {
                    @Override
                    public void handleResponse(List<Group_Place_User> response) {
                        TestApplication.group_place_users = response;
                        StringBuilder whereClause = new StringBuilder();
                        for (int i = 0; i < TestApplication.group_place_users.size(); i++) {
                            whereClause.append( "group_group" );
                            whereClause.append( ".objectId='" ).append( TestApplication.group_place_users.get(i).getObjectId()).append( "'" );
                            if (i != TestApplication.group_place_users.size()-1) {
                                whereClause.append(" and ");
                            }
                        }
                        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
                        queryBuilder.setWhereClause( whereClause.toString() );
                        Log.i("query", whereClause.toString());
                        Backendless.Data.of( Group.class ).find( queryBuilder, new AsyncCallback<List<Group>>(){
                            @Override
                            public void handleResponse(List<Group> response) {
                                Log.i("empty", "" + response.isEmpty());
                                adapter = new GroupsAdapter(GroupsList.this, response);
                                lvList.setAdapter(adapter);
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {

                            }
                        });
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(GroupsList.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }


}
