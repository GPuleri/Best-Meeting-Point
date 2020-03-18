package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        User p1 = new User();
        p1.setName("Fabio");
        p1.setSurname("Ciao");

        Backendless.Data.of(User.class).save(p1, new AsyncCallback<User>() {
                    @Override
                    public void handleResponse(User response) {

                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {

                    }
                }
        );
    }
}
