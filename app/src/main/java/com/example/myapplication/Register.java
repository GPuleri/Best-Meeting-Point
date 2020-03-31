package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.ArrayList;
import java.util.Arrays;

public class Register extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    EditText etName,etMail,etPassword,etReEnter,etUsername,etSurname, etAddress;
    Button btnRegister;
    AutocompleteSupportFragment autocompleteFragment;

    final String[] address = new String[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        etName=findViewById(R.id.etName);
        etMail=findViewById(R.id.etMail);
        etPassword=findViewById(R.id.etPassword);
        etReEnter=findViewById(R.id.etReEnter);
        etUsername=findViewById(R.id.etUsername);
        etSurname=findViewById(R.id.etSurname);
        etAddress = findViewById(R.id.etAddress);
        autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        btnRegister=findViewById(R.id.btnRegister);

        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        // Create a new Places client instance
        PlacesClient placesClient = Places.createClient(this);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .hide(autocompleteFragment)
                .commit();

        etAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction()
                        .show(autocompleteFragment)
                        .commit();
                autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.ADDRESS));
                // Set up a PlaceSelectionListener to handle the response.
                autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(Place place) {
                        etAddress.setText(place.getAddress());
                        address[0] = place.getId();
                        address[1] = place.getAddress();
                        FragmentManager fm = getSupportFragmentManager();
                        fm.beginTransaction()
                                .hide(autocompleteFragment)
                                .commit();
                    }

                    @Override
                    public void onError(Status status) {
                        Toast.makeText(getBaseContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }});

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etName.getText().toString().isEmpty() || etMail.getText().toString().isEmpty() ||
                     etReEnter.getText().toString().isEmpty() ||  etUsername.getText().toString().isEmpty()
                     || etSurname.getText().toString().isEmpty() || address[0].isEmpty())
                {
                    Toast.makeText(Register.this, "Please enter all details", Toast.LENGTH_LONG).show();
                }
                else
                {
                    if (etPassword.getText().toString().equals(etReEnter.getText().toString())){
                        String name = etName.getText().toString().trim();
                        String surname= etSurname.getText().toString().trim();
                        String email = etMail.getText().toString().trim();
                        String username= etUsername.getText().toString().trim();
                        String password = etPassword.getText().toString().trim();

                        BackendlessUser user= new BackendlessUser();
                        user.setEmail(email);
                        user.setPassword(password);
                        user.setProperty("name",name);
                        user.setProperty("username",username);
                        user.setProperty("surname",surname);


                        final String em= email, pass=password;
                        final com.example.myapplication.data.Place p1 = new com.example.myapplication.data.Place();
                        p1.setFull_address(address[1]);
                        p1.setId_google_place(address[0]);

                        showProgress(true);

                        Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>() {
                            @Override
                            public void handleResponse(BackendlessUser response) {
                                    showProgress(false);
                                Toast.makeText(Register.this, "User successfully register", Toast.LENGTH_LONG).show();

                                Backendless.UserService.login(em, pass, new AsyncCallback<BackendlessUser>() {
                                    @Override
                                    public void handleResponse(final BackendlessUser usLogged) {
                                        Backendless.Data.of(com.example.myapplication.data.Place.class).save(p1, new AsyncCallback<com.example.myapplication.data.Place>() {
                                            @Override
                                            public void handleResponse(com.example.myapplication.data.Place response) {
                                                ArrayList l = new ArrayList<com.example.myapplication.data.Place>();
                                                l.add(response);
                                                Backendless.Data.of(BackendlessUser.class).addRelation(usLogged, "user_place", l, new AsyncCallback<Integer>() {
                                                    @Override
                                                    public void handleResponse(Integer response) {

                                                    }

                                                    @Override
                                                    public void handleFault(BackendlessFault fault) {

                                                    }
                                                });
                                            }

                                            @Override
                                            public void handleFault(BackendlessFault fault) {
                                                Log.e("FABIO",fault.getMessage());
                                                Toast.makeText(Register.this, "Error: "+ fault.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {
                                        Log.e("FABIO",fault.getMessage());
                                    }
                                },false);

                                Register.this.finish();
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                    Toast.makeText(Register.this, "Error: "+ fault.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });









                    }
                    else {
                        Toast.makeText(Register.this, "Please make sure that your password and re-type password is the same", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}
