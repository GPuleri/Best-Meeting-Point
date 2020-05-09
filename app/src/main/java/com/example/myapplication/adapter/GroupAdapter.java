package com.example.myapplication.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.backendless.persistence.LoadRelationsQueryBuilder;
import com.example.myapplication.R;
import com.example.myapplication.data.Group;
import com.example.myapplication.data.Group_Place_User;
import com.example.myapplication.utility.TestApplication;

import java.util.ArrayList;
import java.util.List;

public class GroupAdapter extends ArrayAdapter<Group>
{
    private Context context;
    private List<Group> groups;
    private List<Group_Place_User> gpu;

    /**
     * Constructor that receive a context object and a list of groups
     */
    public GroupAdapter(Context context, List<Group> list, List<Group_Place_User> gpu) {
        super(context, R.layout.row_groups, list);
        this.context = context;
        this.groups = list;
        this.gpu = gpu;
    }

    /**
     * it set the details of every group inside the list
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.row_groups, parent, false);

        TextView tvName = convertView.findViewById(R.id.tvName);
        ImageView ivParticipant = convertView.findViewById(R.id.ivParticipant);

        tvName.setText(groups.get(position).getName());

        Drawable participant = ivParticipant.getResources().getDrawable(R.drawable.participant);
        Drawable participant_no = ivParticipant.getResources().getDrawable(R.drawable.participant_no);


        LoadRelationsQueryBuilder<Group_Place_User> loadRelationsQueryBuilder;
        loadRelationsQueryBuilder = LoadRelationsQueryBuilder.of(Group_Place_User.class);
        loadRelationsQueryBuilder.setRelationName("group_user");

        /*Backendless.Data.of(BackendlessUser.class).loadRelations(TestApplication.user.getObjectId(),
                loadRelationsQueryBuilder,
                new AsyncCallback<List<Group_Place_User>>() {

                    @Override
                    public void handleResponse(List<Group_Place_User> response) {
                        StringBuilder whereClause = new StringBuilder();
                        TestApplication.group_place_users = response;
                        if (!response.isEmpty()) {
                            for (int i = 0; i < TestApplication.group_place_users.size(); i++) {
                                whereClause.append("group_group");
                                whereClause.append(".objectId='").append(TestApplication.group_place_users.get(i).getObjectId()).append("'");
                                if (i != TestApplication.group_place_users.size() - 1) {
                                    whereClause.append(" or ");
                                }
                            }

                            DataQueryBuilder queryBuilder = DataQueryBuilder.create();
                            queryBuilder.setWhereClause(whereClause.toString());
                            Log.i("query_group_group", whereClause.toString());
                            Backendless.Data.of(Group.class).find(queryBuilder, new AsyncCallback<List<Group>>() {

                                @Override
                                public void handleResponse(List<Group> response) {
                                    TestApplication.groups = response;
                                    //adapter = new GroupAdapter(getContext(), response, TestApplication.group_place_users);
                                    //lvList.setAdapter(adapter);
                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    Log.e("error", "Error: " + fault.getMessage());
                                }
                            });
                        } else {
                            TestApplication.groups = new ArrayList<>();
                            //adapter = new GroupAdapter(getContext(), TestApplication.groups, TestApplication.group_place_users);
                            //lvList.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Log.e("error", "Error: " + fault.getMessage());
                    }
                });*/


        return convertView;
    }
}
