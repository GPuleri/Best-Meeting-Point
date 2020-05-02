package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.backendless.persistence.LoadRelationsQueryBuilder;
import com.example.myapplication.R;
import com.example.myapplication.data.Group;
import com.example.myapplication.utility.TestApplication;
import com.example.myapplication.adapter.ForwardingInvitationAdapter;

import java.util.ArrayList;
import java.util.List;

public class ForwardingInvitations extends AppCompatActivity {

    SearchView searchView;
    ListView listView;
    ForwardingInvitationAdapter adapter;

    /**
     * This method is performed when the ForwardingInvitations activity is created and allows you to load
     * the list of users from the databse with the possibility to invite them.
     * The users returned are those not belonging to the group or not yet invited. it is possible to filter them
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forwarding_invitations);

        searchView = (SearchView) findViewById(R.id.searchView);
        listView = (ListView) findViewById(R.id.lv1);

        String where= "";
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();

        for (int i=0; i< TestApplication.users_active.size(); i++){
            where += "objectId != '" + TestApplication.users_active.get(i).getObjectId()+"'";
            if (i < TestApplication.users_active.size() -1)
                where += " and ";
        }
        queryBuilder.setWhereClause(where);

        // I prepare the query and set the name of the relationship (foreign key)
        LoadRelationsQueryBuilder<Group> loadRelationsQueryBuilder;
        loadRelationsQueryBuilder = LoadRelationsQueryBuilder.of( Group.class );
        loadRelationsQueryBuilder.setRelationName( "myInvitation" );
        List<BackendlessUser> listuser= new ArrayList<BackendlessUser>();

        // I search for users (excluding those already belonging to the group), after which I filter
        // them further excluding users who have already been invited
        Backendless.Persistence.of(BackendlessUser.class).find(queryBuilder, new AsyncCallback<List<BackendlessUser>>() {
            @Override
            public void handleResponse(List<BackendlessUser> response) {


                for ( BackendlessUser us : response) {
                    Backendless.Data.of("Users").loadRelations(us.getObjectId(), loadRelationsQueryBuilder,
                            new AsyncCallback<List<Group>>() {
                                @Override
                                public void handleResponse(List<Group> response) {

                                    boolean flag = false;
                                    for (int i = 0; i < response.size(); i++) {
                                        if (response.get(i).getName().equals(TestApplication.groups.get(TestApplication.position_selected_group).getName()))
                                            flag = true;
                                    }
                                    if (!flag)
                                        listuser.add(us);

                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    Log.e( "MYAPP", "server reported an error - " + fault.getMessage() );
                                }
                            });
                }

                //I set the adapter to use in the ListView
                adapter= new ForwardingInvitationAdapter(ForwardingInvitations.this, listuser);
                listView.setAdapter(adapter);



            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e( "MYAPP", "server reported an error - " + fault.getMessage() );
            }
        });



        // When the query in the search bar changes, I filter the users contained in the listView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });


    }
}
