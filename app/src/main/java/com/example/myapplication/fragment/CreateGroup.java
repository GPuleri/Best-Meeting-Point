package com.example.myapplication.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.myapplication.R;
import com.example.myapplication.data.Group;
import com.example.myapplication.data.Group_Place_User;
import com.example.myapplication.utility.TestApplication;

import java.util.ArrayList;
import java.util.Objects;

public class CreateGroup extends Fragment {

    private EditText etName;
    private Spinner dropdown;


    /**
     * It creates the Create group activity, inside that you can insert the name of the new group and save it
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_group, container, false);

        Button btnNew = view.findViewById(R.id.btnNewGroup);
        etName = view.findViewById(R.id.etNameGroup);
        dropdown = view.findViewById(R.id.spnGroupType);
        ArrayAdapter<String> adapterTypes = new ArrayAdapter<String>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, TestApplication.kinds);
        dropdown.setAdapter(adapterTypes);


        btnNew.setOnClickListener(v -> {
            if (etName.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Please enter the name", Toast.LENGTH_SHORT).show();
            } else {
                TestApplication.link = new Group_Place_User();
                TestApplication.group_place_users.add(TestApplication.link);
                Backendless.Persistence.save(TestApplication.link, new AsyncCallback<Group_Place_User>() {
                    @Override
                    public void handleResponse(Group_Place_User response) {
                        TestApplication.link = response;
                        ArrayList<Group_Place_User> list = new ArrayList<>();
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
                                                ArrayList<Group_Place_User> list = new ArrayList<>();
                                                list.add(TestApplication.link);
                                                Backendless.Data.of(Group.class).addRelation(response, "group_group", list,
                                                        new AsyncCallback<Integer>() {
                                                            @Override
                                                            public void handleResponse(Integer response) {
                                                                TestApplication.groups.add(TestApplication.group);
                                                                Toast.makeText(getContext(), "Group created!", Toast.LENGTH_SHORT).show();
                                                                requireActivity().getSupportFragmentManager().popBackStack();
                                                            }

                                                            @Override
                                                            public void handleFault(BackendlessFault fault) {
                                                                Log.e("error group", fault.getMessage());
                                                                Toast.makeText(getContext(), "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }

                                            @Override
                                            public void handleFault(BackendlessFault fault) {
                                                Log.e("error", fault.getMessage());
                                                Toast.makeText(getContext(), "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {
                                        Log.e("error backendless", fault.getMessage());
                                        Toast.makeText(getContext(), "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Log.e("error create group user", fault.getMessage());
                        Toast.makeText(getContext(), "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return view;
    }
}
