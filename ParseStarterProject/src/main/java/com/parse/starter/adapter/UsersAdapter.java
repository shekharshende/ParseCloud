package com.parse.starter.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parse.starter.R;

import java.util.List;

/**
 * Created by root on 4/1/16.
 */
public class UsersAdapter extends BaseAdapter {
    private final LayoutInflater layoutInflater;
    private Context context;
    private List<String> userList;

    public UsersAdapter(FragmentActivity context, List<String> userList) {
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
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        TextView userName = null;
        if (convertView == null) {

            row = layoutInflater.inflate(R.layout.fragment_one, parent, false);
            userName = (TextView) row.findViewById(R.id.txtview);

        } else
            row = convertView;
        String user = userList.get(position);
        userName.setText(user);

        return row;
    }
}
