package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.backendless.persistence.LoadRelationsQueryBuilder;
import com.example.myapplication.data.Group;

import java.util.List;

public class InvitationList extends AppCompatActivity {

    ListView lvList;

    InvitationAdapter adapter;

    /**
     * this method is performed when the InvitationList activity is created and allows you to load
     * the list of invitations from the databse with the possibility to confirm or refit the invitation
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_list); //I set the xml file to be used for the layouts of this activity

        lvList=findViewById(R.id.lvList);

        // I prepare the query and set the name of the relationship (foreign key)
        LoadRelationsQueryBuilder<Group> loadRelationsQueryBuilder;
        loadRelationsQueryBuilder = LoadRelationsQueryBuilder.of( Group.class );
        loadRelationsQueryBuilder.setRelationName( "myInvitation" );

        //For the logged in user I recover all the groups I have been invited to
        Backendless.Data.of( "Users" ).loadRelations( TestApplication.user.getObjectId(),
                loadRelationsQueryBuilder,
                new AsyncCallback<List<Group>>()
                {
                    @Override
                    public void handleResponse( List<Group> group )
                    {
                        for( Group groups: group )
                            Log.i( "MYAPP", groups.getName() );

                        TestApplication.invitation_group=group;
                        //I set the adapter to use in the ListView
                        adapter = new InvitationAdapter(InvitationList.this, group);
                        lvList.setAdapter(adapter);


                    }

                    @Override
                    public void handleFault( BackendlessFault fault )
                    {
                        Log.e( "MYAPP", "server reported an error - " + fault.getMessage() );
                    }
                } );

        /*
        String where= "name = 'Pizzata'";
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(where);
       // queryBuilder.setGroupBy("name");

        Backendless.Persistence.of(Group.class).find(queryBuilder, new AsyncCallback<List<Group>>() {
            @Override
            public void handleResponse(List<Group> response) {
                TestApplication.invitation_group=response;
               // Log.i("FABIO", String.valueOf(response.size()));

                adapter = new InvitationAdapter(InvitationList.this, response);
                lvList.setAdapter(adapter);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(InvitationList.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


         */
    }
}
