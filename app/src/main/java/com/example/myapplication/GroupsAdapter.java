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

import com.example.myapplication.data.Group_Place_User;

import java.util.List;

public class GroupsAdapter extends ArrayAdapter<Group_Place_User>
{
    private Context context;
    private List<Group_Place_User> groups;

    public GroupsAdapter(Context context, List<Group_Place_User> list) {
        super(context, R.layout.row_groups, list);
        this.context = context;
        this.groups = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.row_groups, parent, false);
        TextView tvChar = convertView.findViewById(R.id.tvChar);
        TextView tvName = convertView.findViewById(R.id.tvName);
//        TextView tvDescription = convertView.findViewById(R.id.tvDescription);


        tvChar.setText(Character.toString(groups.get(position).getObjectId().charAt(0)));
        tvName.setText(groups.get(position).getObjectId());

        return convertView;
    }
}
