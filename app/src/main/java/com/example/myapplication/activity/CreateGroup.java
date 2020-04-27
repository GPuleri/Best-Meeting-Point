package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.myapplication.R;
import com.example.myapplication.utility.TestApplication;
import com.example.myapplication.data.Group;
import com.example.myapplication.data.Group_Place_User;

import java.util.ArrayList;

public class CreateGroup extends AppCompatActivity {

    EditText etName;
    Button btnNew;
    Spinner dropdown;

    /**
     * It creates the Create group activity, inside that you can insert the name of the new group and save it
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        btnNew = findViewById(R.id.btnNewGroup);
        etName = findViewById(R.id.etNameGroup);
        dropdown = findViewById(R.id.spnGroupType);
        ArrayAdapter<String> adapterTypes = new ArrayAdapter<String>(CreateGroup.this,
                android.R.layout.simple_spinner_dropdown_item, TestApplication.kinds);
        dropdown.setAdapter(adapterTypes);

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
                                            String name = etName.getText().toString().trim();
                                            TestApplication.group = new Group();
                                            TestApplication.group.setName(name);
                                            int kindsIndex = java.util.Arrays.binarySearch(TestApplication.kinds,
                                                    dropdown.getSelectedItem().toString());
                                            TestApplication.group.setType(TestApplication.kind_codes[kindsIndex]);
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
                }
            }
        });
    }
}
