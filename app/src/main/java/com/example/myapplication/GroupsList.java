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
import com.backendless.persistence.LoadRelationsQueryBuilder;
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
        loadRelationsQueryBuilder = LoadRelationsQueryBuilder.of( Group_Place_User.class );
        loadRelationsQueryBuilder.setRelationName( "group_user" );

        Backendless.Data.of( BackendlessUser.class ).loadRelations(TestApplication.user.getObjectId(),
                loadRelationsQueryBuilder,
                new AsyncCallback<List<Group_Place_User>>() {
                    @Override
                    public void handleResponse(List<Group_Place_User> response) {
                        TestApplication.groups = response;
                        adapter = new GroupsAdapter(GroupsList.this, TestApplication.groups);
                        lvList.setAdapter(adapter);


                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(GroupsList.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } );





    }


}
