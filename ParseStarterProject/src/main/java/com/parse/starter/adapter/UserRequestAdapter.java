package com.parse.starter.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.parse.starter.R;
import com.parse.starter.UserListActivity;

import java.util.List;

/**
 * Created by root on 7/1/16.
 */
public class UserRequestAdapter extends BaseAdapter implements ListAdapter {

    private final LayoutInflater layoutInflater;
    String user = null;
    private Context context;
    private List<String> userList;


    public UserRequestAdapter(FragmentActivity context, List<String> userList) {
        this.context = context;
        this.userList = userList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View row;
        final UserListActivity userListActivity = (UserListActivity) context;

        if (convertView == null) {
            row = layoutInflater.inflate(R.layout.fragment_three, parent, false);
        }
        else
            row= convertView;
        user = userList.get(position);
        TextView userName = (TextView) row.findViewById(R.id.text_username);
        ImageView accept = (ImageView) row.findViewById(R.id.approve);
        ImageView reject = (ImageView) row.findViewById(R.id.reject);
        userName.setText(user);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = userList.get(position);
                userListActivity.updateUserStatus(user);
                userList.remove(position);
                notifyDataSetChanged();
            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = userList.get(position);
                userListActivity.removeUser(user);
                userList.remove(position);
                notifyDataSetChanged();
            }
        });

        return row;
    }
}
