package com.example.myapplication.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.example.myapplication.R;
import com.example.myapplication.utility.TestApplication;
import com.example.myapplication.data.Group;
import com.example.myapplication.data.Group_Place_User;

import java.util.ArrayList;
import java.util.List;


public class InvitationAdapter extends ArrayAdapter<Group> {

    private Context context;
    private List<Group> groups;


    /**
     * Adapter constructor. In Android, Adapter is a bridge between UI component and data source that
     * helps us to fill data in UI component.
     * To initialize the adapter I pass him the context of the activity and the list of groups
     * that I have recovered
     *
     */
    public InvitationAdapter (Context context, List <Group> list){
        super(context, R.layout.row_layout_invitation,list);
        this.context=context;
        this.groups=list;
    }

    /**
     * Get a View that displays the data at the specified position in the data set.
     */
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //specify a root view
        convertView= inflater.inflate(R.layout.row_layout_invitation,parent,false);


        Button btnConfirm=convertView.findViewById(R.id.btnConfirm);
        Button btnDelete=convertView.findViewById(R.id.btnDelete);
        TextView tvChar= convertView.findViewById(R.id.tvChar);
        TextView tvName= convertView.findViewById(R.id.tvName);
        final TextView tvCreator = convertView.findViewById(R.id.tvCreator);

        final String[] s = new String[1];

        // I write in the TextView the first letter and the name of the i-th group
        tvChar.setText(groups.get(position).getName().toUpperCase().charAt(0)+ "");
        tvName.setText(groups.get(position).getName());

        // I prepare the query to search, for the logged in user, by who has been invited
        String where= "objectId= '" +groups.get(position).getOwnerId()+"'";
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(where);

        // I run the query which will return a list of users. Since for each group only the creator
        // of the group can invite me, the list will consist of 1 element.
        Backendless.Persistence.of(BackendlessUser.class).find(queryBuilder, new AsyncCallback<List<BackendlessUser>>() {
            @Override
            public void handleResponse(List<BackendlessUser> response) {
               s[0] = response.get(0).getProperty("username").toString();
                tvCreator.setText("invited by: "+ s[0]);
               Log.i("MYAPP", s[0]);

            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e("MYAPP", fault.getMessage());
            }
        });

        // if the user confirms the invitation, I insert the user into that group by making the appropriate
        // joins in the db and then I delete the invitation
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "invitation accepted", Toast.LENGTH_SHORT).show();
                Log.i("MYAPP", "bottone confirm cliccato");

                Group_Place_User gpu = new Group_Place_User();
                Backendless.Data.of(Group_Place_User.class).save(gpu, new AsyncCallback<Group_Place_User>() {
                    @Override
                    public void handleResponse(Group_Place_User response) {

                        ArrayList l= new ArrayList<Group_Place_User>();
                        l.add(response);
                        Backendless.Data.of(Group.class).addRelation(groups.get(position), "group_group", l,
                                new AsyncCallback<Integer>() {
                                    @Override
                                    public void handleResponse(Integer response) {
                                        Log.i("MYAPP", "aggiunta group_group fatta in group");
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {
                                        Log.e( "MYAPP", "server reported an error - " + fault.getMessage() );
                                    }
                                });




                        Backendless.Data.of(BackendlessUser.class).addRelation(TestApplication.user, "group_user", l,
                                new AsyncCallback<Integer>() {
                                    @Override
                                    public void handleResponse(Integer response) {
                                        Log.i("MYAPP", "aggiunta group_user fatta in user");
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {
                                        Log.e( "MYAPP", "server reported an error - " + fault.getMessage() );
                                    }
                                });



                        ArrayList<Group> groupCollection = new ArrayList <Group>();
                        groupCollection.add(groups.get(position));
                        Backendless.Data.of("Users").deleteRelation(TestApplication.user.getProperties(), "myInvitation", groupCollection,
                                new AsyncCallback<Integer>() {
                                    @Override
                                    public void handleResponse(Integer response) {
                                        Log.i( "MYAPP", "relation has been deleted");
                                        groups.remove(position);
                                        notifyDataSetChanged();
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {
                                        Log.e( "MYAPP", "server reported an error - " + fault.getMessage() );
                                    }
                                });



                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {

                    }
                });
            }
        });



        //if the user refuses the invitation I delete it
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "invitation declined", Toast.LENGTH_SHORT).show();
                Log.i("MYAPP", "bottone delete cliccato");

                ArrayList<Group> groupCollection = new ArrayList <Group>();
                groupCollection.add(groups.get(position));
                Backendless.Data.of("Users").deleteRelation(TestApplication.user.getProperties(), "myInvitation", groupCollection,
                        new AsyncCallback<Integer>() {
                            @Override
                            public void handleResponse(Integer response) {
                                Log.i( "MYAPP", "relation has been deleted");
                                groups.remove(position);
                                notifyDataSetChanged();
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Log.e( "MYAPP", "server reported an error - " + fault.getMessage() );
                            }
                        });
            }
        });




        return convertView;
    }
}
