package com.example.myapplication.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.example.myapplication.activity.MapsActivity;
import com.example.myapplication.adapter.ParticipantAdapter;
import com.example.myapplication.data.Group;
import com.example.myapplication.data.Group_Place_User;
import com.example.myapplication.data.Place;
import com.example.myapplication.utility.TestApplication;

import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class GroupInfo extends Fragment {

    private TextView tvName, tvParticipants;
    private ParticipantAdapter adapter;
    private ListView lvParticipants;
    private EditText etName;
    private Button btnSubmit;

    /**
     * it creates a view where there are the group details, it also contains the list of the participants
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_group_info, container, false);

        ImageView ivInvite, ivNavigate, ivDelete, ivEdit;
        LinearLayout llOptions;

        tvName = view.findViewById(R.id.tvName);
        ivInvite = view.findViewById(R.id.ivInvite);
        ivNavigate = view.findViewById(R.id.ivNavigate);
        ivDelete = view.findViewById(R.id.ivDelete);
        llOptions = view.findViewById(R.id.llOptions);
        lvParticipants = view.findViewById(R.id.lvParticipants);
        etName = view.findViewById(R.id.etName);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        ivEdit = view.findViewById(R.id.ivEdit);
        tvParticipants = view.findViewById(R.id.tvParticipants);

        final int index = Objects.requireNonNull(getArguments()).getInt("index");

        tvName.setText(TestApplication.groups.get(index).getName());
        if (TestApplication.user.getObjectId().equals(TestApplication.groups.get(index).getOwnerId())) {
            llOptions.setVisibility(View.VISIBLE);
        }

        LoadRelationsQueryBuilder<Group_Place_User> loadRelationsQueryBuilder;
        loadRelationsQueryBuilder = LoadRelationsQueryBuilder.of(Group_Place_User.class);
        loadRelationsQueryBuilder.setRelationName("group_group");

        Backendless.Data.of(Group.class).loadRelations(TestApplication.groups.get(index).getObjectId(),
                loadRelationsQueryBuilder,
                new AsyncCallback<List<Group_Place_User>>() {
                    @Override
                    public void handleResponse(List<Group_Place_User> response) {
                        TestApplication.group_place_users = response;
                        StringBuilder whereClause = new StringBuilder();
                        for (int i = 0; i < TestApplication.group_place_users.size(); i++) {
                            whereClause.append("group_user");
                            whereClause.append(".objectId='").append(TestApplication.group_place_users.get(i).getObjectId()).append("'");
                            if (i != TestApplication.group_place_users.size() - 1) {
                                whereClause.append(" or ");
                            }
                        }
                        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
                        queryBuilder.setWhereClause(whereClause.toString());
                        queryBuilder.setSortBy("objectId");
                        Log.i("query", whereClause.toString());
                        Backendless.Data.of(BackendlessUser.class).find(queryBuilder, new AsyncCallback<List<BackendlessUser>>() {
                            @Override
                            public void handleResponse(List<BackendlessUser> response) {
                                Log.i("empty", "" + response.isEmpty());
                                TestApplication.users_active = response;
                                adapter = new ParticipantAdapter(getActivity(), response);
                                lvParticipants.setAdapter(adapter);
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Toast.makeText(getActivity(), "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                        whereClause = new StringBuilder();
                        for (int i = 0; i < TestApplication.group_place_users.size(); i++) {
                            whereClause.append("group_place");
                            whereClause.append(".objectId='").append(TestApplication.group_place_users.get(i).getObjectId()).append("'");
                            if (i != TestApplication.group_place_users.size() - 1) {
                                whereClause.append(" or ");
                            }
                        }
                        queryBuilder = DataQueryBuilder.create();
                        queryBuilder.setWhereClause(whereClause.toString());
                        queryBuilder.setSortBy("ownerId");
                        Log.i("query", whereClause.toString());
                        Backendless.Data.of(Place.class).find(queryBuilder, new AsyncCallback<List<Place>>() {
                            @Override
                            public void handleResponse(List<Place> response) {
                                Log.i("empty", "" + response.size());
                                TestApplication.places_active = response;
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Toast.makeText(getActivity(), "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(getActivity(), "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setMessage("Do you want to delete this group?");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LoadRelationsQueryBuilder<Group_Place_User> loadRelationsQueryBuilder;
                        loadRelationsQueryBuilder = LoadRelationsQueryBuilder.of(Group_Place_User.class);
                        loadRelationsQueryBuilder.setRelationName("group_group");

                        Backendless.Data.of(Group.class).loadRelations(TestApplication.groups.get(index).getObjectId(),
                                loadRelationsQueryBuilder,
                                new AsyncCallback<List<Group_Place_User>>() {

                                    @Override
                                    public void handleResponse(List<Group_Place_User> response) {
                                        for (final Group_Place_User toDelete : response) {

                                            Backendless.Persistence.of(Group_Place_User.class).remove(toDelete, new AsyncCallback<Long>() {
                                                @Override
                                                public void handleResponse(Long response) {
                                                    TestApplication.group_place_users.remove(toDelete);
                                                }

                                                @Override
                                                public void handleFault(BackendlessFault fault) {
                                                    Toast.makeText(getActivity(), "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {
                                        Toast.makeText(getActivity(), "Error:" + fault.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                        Backendless.Persistence.of(Group.class).remove(TestApplication.groups.get(index), new AsyncCallback<Long>() {
                            @Override
                            public void handleResponse(Long response) {
                                TestApplication.groups.remove(index);
                                Toast.makeText(getActivity(), "Group deleted", Toast.LENGTH_SHORT).show();
                                getActivity().setResult(RESULT_OK);
                                GroupList dest = new GroupList();
                                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(getId(), dest);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Toast.makeText(getActivity(), "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
            }
        });
        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etName.getVisibility() == View.GONE) {

                    etName.setText(TestApplication.groups.get(index).getName());
                    tvParticipants.setVisibility(View.GONE);
                    lvParticipants.setVisibility(View.GONE);
                    btnSubmit.setVisibility(View.VISIBLE);
                    etName.setVisibility(View.VISIBLE);

                    btnSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (etName.getText().toString().isEmpty()) {
                                Toast.makeText(getActivity(), "Insert all the details requested!", Toast.LENGTH_SHORT).show();
                            } else {
                                TestApplication.groups.get(index).setName(etName.getText().toString().trim());
                                Backendless.Persistence.save(TestApplication.groups.get(index), new AsyncCallback<Group>() {
                                    @Override
                                    public void handleResponse(Group response) {
                                        tvName.setText(TestApplication.groups.get(index).getName());
                                        Toast.makeText(getActivity(), "Updated!", Toast.LENGTH_SHORT).show();
                                        lvParticipants.setVisibility(View.VISIBLE);
                                        tvParticipants.setVisibility(View.VISIBLE);
                                        btnSubmit.setVisibility(View.GONE);
                                        etName.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {
                                        Toast.makeText(getActivity(), "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        }
                    });
                }
                else {
                    tvParticipants.setVisibility(View.VISIBLE);
                    lvParticipants.setVisibility(View.VISIBLE);
                    btnSubmit.setVisibility(View.GONE);
                    etName.setVisibility(View.GONE);
                }
            }
        });

        ivNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        ivInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getActivity(), MapsActivity.class);
                //startActivityForResult(intent, 1);
            }
        });

        return view;
    }
}
