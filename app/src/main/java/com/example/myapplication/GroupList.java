package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
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


public class GroupList extends AppCompatActivity {
    ListView lvList;
    Button btnNew;
    GroupAdapter adapter;

    /**
     * It creates an activity where there is the user groups list
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        lvList = findViewById(R.id.lvList);
        btnNew = findViewById(R.id.btnNew);

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GroupList.this, GroupInfo.class);
                intent.putExtra("index", position);
                startActivityForResult(intent, 1);
            }
        });
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupList.this, CreateGroup.class);
                startActivityForResult(intent, 1);
            }
        });

        LoadRelationsQueryBuilder<Group_Place_User> loadRelationsQueryBuilder;
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
                                    adapter = new GroupAdapter(GroupList.this, response);
                                    lvList.setAdapter(adapter);
                                }
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Toast.makeText(GroupList.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(GroupList.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    /**
     * if a group is modified it changes the data of the list
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            adapter.notifyDataSetChanged();
        }
    }
}
