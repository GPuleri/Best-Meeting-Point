package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.servercode.annotation.Async;
import com.example.myapplication.data.Group;
import com.example.myapplication.data.Group_Place_User;

import java.util.ArrayList;

public class CreateGroup extends AppCompatActivity {

    EditText etName;
    Button btnNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        btnNew = findViewById(R.id.btnNewGroup);
        etName = findViewById(R.id.etNameGroup);

        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etName.getText().toString().isEmpty()) {
                    Toast.makeText(CreateGroup.this, "Please enter the name", Toast.LENGTH_SHORT).show();
                } else {
                    TestApplication.link = new Group_Place_User();
                    Backendless.Persistence.save(TestApplication.link, new AsyncCallback<Group_Place_User>() {
                        @Override
                        public void handleResponse(Group_Place_User response) {
                            TestApplication.link = response;
                            ArrayList<Group_Place_User> list = new ArrayList<Group_Place_User>();
                            list.add(response);
                            Backendless.Data.of(BackendlessUser.class).addRelation(TestApplication.user, "group_user", list,
                                    new AsyncCallback<Integer>() {
                                        @Override
                                        public void handleResponse(Integer response) {

                                        }

                                        @Override
                                        public void handleFault(BackendlessFault fault) {
                                            Log.e("error backendless", fault.getMessage());
                                            Toast.makeText(CreateGroup.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Log.e("error create group user", fault.getMessage());
                            Toast.makeText(CreateGroup.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    String name = etName.getText().toString().trim();
                    TestApplication.group = new Group();
                    TestApplication.group.setName(name);
                    TestApplication.group.saveAsync(new AsyncCallback<Group>() {
                        @Override
                        public void handleResponse(Group response) {
                            ArrayList<Group_Place_User> list = new ArrayList<Group_Place_User>();
                            list.add(TestApplication.link);
                            Backendless.Data.of(Group.class).addRelation(response, "group_group", list,
                                    new AsyncCallback<Integer>() {
                                        @Override
                                        public void handleResponse(Integer response) {
                                            TestApplication.groups.add(TestApplication.group);
                                            CreateGroup.this.finish();
                                        }

                                        @Override
                                        public void handleFault(BackendlessFault fault) {
                                            Log.e("error group", fault.getMessage());
                                            Toast.makeText(CreateGroup.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Log.e("error", fault.getMessage());
                            Toast.makeText(CreateGroup.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }
}
