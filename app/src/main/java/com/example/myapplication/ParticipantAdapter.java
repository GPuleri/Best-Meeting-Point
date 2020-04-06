package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.backendless.BackendlessUser;

import java.util.List;

/**
 *
 */
public class ParticipantAdapter extends ArrayAdapter<BackendlessUser> {

    private Context context;
    private List<BackendlessUser> users;

    /**
     * Constructor that receives a Context object and a list of users
     */
    ParticipantAdapter(Context context, List<BackendlessUser> list) {
        super(context, R.layout.row_groups, list);
        this.context = context;
        this.users = list;

    }

    /**
     * It set the details of every user inside the list
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.row_participant, parent, false);

        TextView tvUsername = convertView.findViewById(R.id.tvUsername);
        TextView tvEmail = convertView.findViewById(R.id.tvEmail);
        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvSurname = convertView.findViewById(R.id.tvSurname);

        tvUsername.setText((String) users.get(position).getProperty("username"));
        tvEmail.setText((String) users.get(position).getEmail());
        tvName.setText((String) users.get(position).getProperty("name"));
        tvSurname.setText((String)users.get(position).getProperty("surname"));

        return convertView;
    }
}
