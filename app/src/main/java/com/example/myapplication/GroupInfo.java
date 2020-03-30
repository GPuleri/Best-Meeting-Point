package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.backendless.persistence.LoadRelationsQueryBuilder;
import com.backendless.property.UserProperty;
import com.example.myapplication.data.Group;
import com.example.myapplication.data.Group_Place_User;

import java.util.List;

public class GroupInfo extends AppCompatActivity {
    TextView tvName, tvDescription, tvParticipants;
    ImageView ivInvite, ivDelete, ivEdit;
    LinearLayout llOptions;
    PartecipantAdapter adapter;
    ListView lvParticipants;
    EditText etName;
    Button btnSubmit;

    /**
     * Crea una vista in cui sono presenti i dettagli di un gruppo, contiene la lista di utenti che partecipano al gruppo
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
        tvName = findViewById(R.id.tvName);
        tvDescription = findViewById(R.id.tvDescription);
        ivInvite = findViewById(R.id.ivInvite);
        ivDelete = findViewById(R.id.ivDelete);
        llOptions = findViewById(R.id.llOptions);
        lvParticipants = findViewById(R.id.lvParticipants);
        etName = findViewById(R.id.etName);
        btnSubmit = findViewById(R.id.btnSubmit);
        ivEdit = findViewById(R.id.ivEdit);
        tvParticipants =findViewById(R.id.tvParticipants);

        final int index = getIntent().getIntExtra("index", 0);

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
                        Log.i("query", whereClause.toString());
                        Backendless.Data.of(BackendlessUser.class).find(queryBuilder, new AsyncCallback<List<BackendlessUser>>() {
                            @Override
                            public void handleResponse(List<BackendlessUser> response) {
                                Log.i("empty", "" + response.isEmpty());
                                adapter = new PartecipantAdapter(GroupInfo.this, response);
                                lvParticipants.setAdapter(adapter);
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Toast.makeText(GroupInfo.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(GroupInfo.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(GroupInfo.this);
                dialog.setMessage("Do you want do delete the group?");
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
                                                    Toast.makeText(GroupInfo.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {
                                        Toast.makeText(GroupInfo.this, "Error:" + fault.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                        Backendless.Persistence.of(Group.class).remove(TestApplication.groups.get(index), new AsyncCallback<Long>() {
                            @Override
                            public void handleResponse(Long response) {
                                TestApplication.groups.remove(index);
                                Toast.makeText(GroupInfo.this, "Group deleted", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                GroupInfo.this.finish();
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Toast.makeText(GroupInfo.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
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
                etName.setText(TestApplication.groups.get(index).getName());
                tvParticipants.setVisibility(View.GONE);
                lvParticipants.setVisibility(View.GONE);
                btnSubmit.setVisibility(View.VISIBLE);
                etName.setVisibility(View.VISIBLE);


                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (etName.getText().toString().isEmpty()) {
                            Toast.makeText(GroupInfo.this, "Insert all the details  requested!", Toast.LENGTH_SHORT).show();
                        } else {
                            TestApplication.groups.get(index).setName(etName.getText().toString().trim());
                            Backendless.Persistence.save(TestApplication.groups.get(index), new AsyncCallback<Group>() {
                                @Override
                                public void handleResponse(Group response) {
                                    tvName.setText(TestApplication.groups.get(index).getName());
                                    Toast.makeText(GroupInfo.this, "Updated!", Toast.LENGTH_SHORT).show();
                                    lvParticipants.setVisibility(View.VISIBLE);
                                    tvParticipants.setVisibility(View.VISIBLE);
                                    btnSubmit.setVisibility(View.GONE);
                                    etName.setVisibility(View.GONE);
                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    Toast.makeText(GroupInfo.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }
                });


            }
        });

    }
}
