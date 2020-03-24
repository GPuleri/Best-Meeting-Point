package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.UnitOfWork;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.backendless.transaction.OpResult;


import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button btnInvitationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnInvitationList=findViewById(R.id.btnInvitationList);

        btnInvitationList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,InvitationList.class));
                MainActivity.this.finish();
            }
        });


        /*

        Groups g1= new Groups();
        g1.setName("Piazzata");

        Backendless.Data.of(Groups.class).save(g1, new AsyncCallback<Groups>() {
            @Override
            public void handleResponse( Groups response) {
                Toast.makeText(MainActivity.this, "gruppo salvato", Toast.LENGTH_SHORT).show();
                Places p1=new Places();
                p1.setAddress("Via casiraghi");
                p1.setCity("Roma");
                p1.setCountry("Italia");

                final Places p2= new Places();
                final Groups savedGroup= response;

                p2.setAddress("Via Cazzo");
                p2.setCity("Lambrate");
                p2.setCountry("Italia");

                final List savedPlaces= new ArrayList<>();

                Backendless.Data.of(Places.class).save(p1, new AsyncCallback<Places>() {
                    @Override
                    public void handleResponse(Places savedPlace) {
                        Toast.makeText(MainActivity.this, "posto1 salvato", Toast.LENGTH_SHORT).show();
                        savedPlaces.add(savedPlace);

                        Backendless.Data.of(Places.class).save(p2, new AsyncCallback<Places>() {
                            @Override
                            public void handleResponse(Places savedPlaces2) {
                                Toast.makeText(MainActivity.this, "posto2 salvato", Toast.LENGTH_SHORT).show();
                                savedPlaces.add(savedPlaces2);

                                Backendless.Data.of(Groups.class).addRelation(savedGroup, "myPlaces", savedPlaces,
                                        new AsyncCallback<Integer>() {
                                            @Override
                                            public void handleResponse(Integer response) {
                                                Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();

                                            }

                                            @Override
                                            public void handleFault(BackendlessFault fault) {

                                            }
                                        });
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {

                            }
                        });
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(MainActivity.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.i("ERRORE",fault.getMessage());

                    }
                });
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });



         */


    }
}