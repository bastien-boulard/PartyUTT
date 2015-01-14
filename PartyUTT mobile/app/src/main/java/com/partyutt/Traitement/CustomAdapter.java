package com.partyutt.Traitement;

/**
 * Created by Bastien on 10/01/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.partyutt.R;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends BaseAdapter {

    private List<String> mMail;
    private List<String> mRole;
    private Activity mContext;

    public CustomAdapter(Activity context, ArrayList<String> mail, ArrayList<String> role) {
        mContext = context;
        mMail = mail;
        mRole = role;
    }

    private class ViewHolder {

        public TextView firstName;
        public TextView lastName;
    }

    @Override
    public View getView(int position, View view, ViewGroup container) {
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.invite_list_layout, container, false);
        }

        ViewHolder viewHolder = (ViewHolder) view.getTag();
        if(viewHolder == null){
            viewHolder = new ViewHolder();
            viewHolder.firstName = (TextView) view.findViewById(R.id.textmail);
            viewHolder.lastName = (TextView) view.findViewById(R.id.textrole);
            view.setTag(viewHolder);
        }

        // setting here values to the fields of my items from my fan object
        viewHolder.firstName.setText(mMail.get(position));
        viewHolder.lastName.setText(mRole.get(position));


        return view;
    }

    @Override
    public int getCount() {
        if (mMail != null && mRole != null) {
            return mMail.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return mMail.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}