package com.example.myapplication;

import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.local.UserIdStorageFactory;
import com.example.myapplication.data.Group;

import java.util.ArrayList;
import java.util.List;

public class TestApplication extends Application {
    public static final String APPLICATION_ID = "CC93AEC0-074E-BCAE-FFFE-748FA924E000";
    public static final String API_KEY = "278AD5E3-D39E-49EA-ACB4-495652BF6203";
    public static final String SERVER_URL = "https://api.backendless.com";

    public static BackendlessUser user;
    public static List<Group> invitation_group;

    @Override
    public void onCreate() {
        super.onCreate();

        Backendless.setUrl(SERVER_URL);
        Backendless.initApp(getApplicationContext(),
                APPLICATION_ID,
                API_KEY);



        Backendless.UserService.isValidLogin(new AsyncCallback<Boolean>() {
            @Override
            public void handleResponse(Boolean response) {

                if (response)
                {
                    String userObjectId = UserIdStorageFactory.instance().getStorage().get();


                    Backendless.Data.of(BackendlessUser.class).findById(userObjectId, new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response) {

                            Toast.makeText(TestApplication.this, "OK", Toast.LENGTH_SHORT).show();
                            TestApplication.user = response;
                           // startActivity(new Intent(TestApplication.this, MainActivity.class));
                           // TestApplication.this.finish();

                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {

                            Toast.makeText(TestApplication.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();


                        }
                    });
                }
                else
                {
                    Backendless.UserService.login("fabio.cimmo@gmail.com", "inter", new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response) {

                            // ApplicationClass.user = response;
                            Toast.makeText(TestApplication.this, "Logged in successfully!", Toast.LENGTH_SHORT).show();
                            TestApplication.user=response;

                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {

                            Toast.makeText(TestApplication.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }, true);
                }

            }

            @Override
            public void handleFault(BackendlessFault fault) {

                Toast.makeText(TestApplication.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });







        }

    }

