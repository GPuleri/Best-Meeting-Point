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
        btnNew = findViewById(R.id.btnNew);
        etName = findViewById(R.id.etName);

        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etName.getText().toString().isEmpty()) {
                    Toast.makeText(CreateGroup.this, "Please enter the name", Toast.LENGTH_SHORT).show();
                } else {
                    Group_Place_User link = new Group_Place_User();

                    Backendless.Persistence.save(link, new AsyncCallback<Group_Place_User>() {
                        @Override
                        public void handleResponse(Group_Place_User response) {
                            String name = etName.getText().toString().trim();

                            Group group = new Group();
                            group.setName(name);

                            Backendless.Persistence.save(group, new AsyncCallback<Group>() {
                                @Override
                                public void handleResponse(Group response) {
                                    TestApplication.new_group = response;
                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    Toast.makeText(CreateGroup.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                            ArrayList list = new ArrayList<Group_Place_User>();
                            list.add(response);

                            Backendless.Data.of(Group.class).addRelation(TestApplication.new_group, "group_group", list,
                                    new AsyncCallback<Integer>() {
                                        @Override
                                        public void handleResponse(Integer response) {
                                            Log.i("add_relation", "group");
                                        }

                                        @Override
                                        public void handleFault(BackendlessFault fault) {
                                            Toast.makeText(CreateGroup.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                                            Log.e("add_relation", fault.getMessage());
                                        }
                                    });

                            Backendless.Data.of(BackendlessUser.class).addRelation(TestApplication.user, "group_user", list,
                                    new AsyncCallback<Integer>() {
                                        @Override
                                        public void handleResponse(Integer response) {
                                            Log.i("add_relation", "user");
                                        }

                                        @Override
                                        public void handleFault(BackendlessFault fault) {
                                            Log.e("add_relation", "group");
                                            Toast.makeText(CreateGroup.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            CreateGroup.this.finish();
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(CreateGroup.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }
}
