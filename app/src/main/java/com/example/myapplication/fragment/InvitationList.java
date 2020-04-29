package com.example.myapplication.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.LoadRelationsQueryBuilder;
import com.example.myapplication.R;
import com.example.myapplication.adapter.InvitationAdapter;
import com.example.myapplication.data.Group;
import com.example.myapplication.utility.TestApplication;

import java.util.List;

public class InvitationList extends Fragment {

    ListView lvList;
    InvitationAdapter adapter;

    /**
     * this method is performed when the InvitationList activity is created and allows you to load
     * the list of invitations from the databse with the possibility to confirm or refit the invitation
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_invitation_list, container, false);

        lvList = view.findViewById(R.id.lvList);

        // I prepare the query and set the name of the relationship (foreign key)
        LoadRelationsQueryBuilder<Group> loadRelationsQueryBuilder;
        loadRelationsQueryBuilder = LoadRelationsQueryBuilder.of( Group.class );
        loadRelationsQueryBuilder.setRelationName( "myInvitation" );

        // For the logged in user I retrieve all the groups I have been invited to
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
                        adapter = new InvitationAdapter(getActivity(), group);
                        lvList.setAdapter(adapter);

                    }

                    @Override
                    public void handleFault( BackendlessFault fault )
                    {
                        Log.e( "MYAPP", "server reported an error - " + fault.getMessage() );
                    }
                } );

        return view;
    }
}
