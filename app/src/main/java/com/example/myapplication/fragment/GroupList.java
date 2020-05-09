package com.example.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.backendless.persistence.LoadRelationsQueryBuilder;
import com.example.myapplication.R;
import com.example.myapplication.adapter.GroupAdapter;
import com.example.myapplication.data.Group;
import com.example.myapplication.data.Group_Place_User;
import com.example.myapplication.utility.TestApplication;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class GroupList extends Fragment {

    private ListView lvList;
    private GroupAdapter adapter;

    /**
     * It creates an activity where there is the user groups list
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_group_list, container, false);

        lvList = view.findViewById(R.id.lvList);
        FloatingActionButton fab = view.findViewById(R.id.fab);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(() -> {
            view.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        });

        lvList.setOnItemClickListener((parent, view1, position, id) -> {
            view.setVisibility(View.GONE);
            Bundle bundle = new Bundle();
            bundle.putInt("index", position);
            GroupInfo dest = new GroupInfo();
            dest.setArguments(bundle);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment, dest);
            fragmentTransaction.commit();
        });

        fab.setOnClickListener(view12 -> {
            view.setVisibility(View.GONE);
            CreateGroup dest = new CreateGroup();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment, dest);
            fragmentTransaction.commit();
        });

        LoadRelationsQueryBuilder<Group_Place_User> loadRelationsQueryBuilder;
        loadRelationsQueryBuilder = LoadRelationsQueryBuilder.of(Group_Place_User.class);
        loadRelationsQueryBuilder.setRelationName("group_user");

        Backendless.Data.of(BackendlessUser.class).loadRelations(TestApplication.user.getObjectId(),
                loadRelationsQueryBuilder,
                new AsyncCallback<List<Group_Place_User>>() {

                    @Override
                    public void handleResponse(List<Group_Place_User> response) {
                        StringBuilder whereClause = new StringBuilder();
                        TestApplication.group_place_users = response;
                        if (!response.isEmpty()) {
                            for (int i = 0; i < TestApplication.group_place_users.size(); i++) {
                                whereClause.append("group_group");
                                whereClause.append(".objectId='").append(TestApplication.group_place_users.get(i).getObjectId()).append("'");
                                if (i != TestApplication.group_place_users.size() - 1) {
                                    whereClause.append(" or ");
                                }
                            }

                            DataQueryBuilder queryBuilder = DataQueryBuilder.create();
                            queryBuilder.setWhereClause(whereClause.toString());
                            Log.i("query_group_group", whereClause.toString());
                            Backendless.Data.of(Group.class).find(queryBuilder, new AsyncCallback<List<Group>>() {

                                @Override
                                public void handleResponse(List<Group> response) {
                                    TestApplication.groups = response;
                                    adapter = new GroupAdapter(getContext(), response, TestApplication.group_place_users);
                                    lvList.setAdapter(adapter);
                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    Log.e("error", "Error: " + fault.getMessage());
                                }
                            });
                        } else {
                            TestApplication.groups = new ArrayList<>();
                            adapter = new GroupAdapter(getContext(), TestApplication.groups, TestApplication.group_place_users);
                            lvList.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Log.e("error", "Error: " + fault.getMessage());
                    }
                });

        return view;
    }

    /**
     * if a group is modified it changes the data of the list
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            adapter.notifyDataSetChanged();
        }
    }
}
