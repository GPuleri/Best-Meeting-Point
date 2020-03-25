package com.example.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.backendless.persistence.LoadRelationsQueryBuilder;
import com.example.myapplication.data.Group;

import java.util.List;
import java.util.Map;

public class InvitationAdapter extends ArrayAdapter<Group> {
    private Context context;
    private List<Group> groups;

    public InvitationAdapter (Context context, List <Group> list){
        super(context,R.layout.row_layout_invitation,list);
        this.context=context;
        this.groups=list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView= inflater.inflate(R.layout.row_layout_invitation,parent,false);

        TextView tvChar= convertView.findViewById(R.id.tvChar);
        TextView tvName= convertView.findViewById(R.id.tvName);
        final TextView tvCreator = convertView.findViewById(R.id.tvCreator);

        final String[] s = new String[1];

        tvChar.setText(groups.get(position).getName().toUpperCase().charAt(0)+ "");
        tvName.setText(groups.get(position).getName());

        String where= "objectId= '" +groups.get(position).getOwnerId()+"'";
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(where);

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




        return convertView;
    }
}
