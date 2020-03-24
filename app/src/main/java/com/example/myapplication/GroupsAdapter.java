package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.data.Group;

import java.util.List;

public class GroupsAdapter extends ArrayAdapter<Group>
{
    private Context context;
    private List<Group> groups;

    public GroupsAdapter(Context context, List<Group> list) {
        super(context, R.layout.row_groups, list);
        this.context = context;
        this.groups = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.row_groups, parent, false);
//        TextView tvChar = convertView.findViewById(R.id.tvChar);
        TextView tvName = convertView.findViewById(R.id.tvName);
//        TextView tvDescription = convertView.findViewById(R.id.tvDescription);

        tvName.setText(groups.get(position).getName());

        return convertView;
    }
}
