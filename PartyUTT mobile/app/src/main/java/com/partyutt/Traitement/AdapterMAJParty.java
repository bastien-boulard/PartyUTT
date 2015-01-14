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

public class AdapterMAJParty extends BaseAdapter {

    private List<String> mMail;
    private List<String> mRole;
    private List<String> mPresence;
    private List<String> mApport;
    private List<String> mQte;
    private Activity mContext;

    public AdapterMAJParty(Activity context, ArrayList<String> mail, ArrayList<String> role, ArrayList<String> presence, ArrayList<String> apport, ArrayList<String> qte) {
        mContext = context;
        mMail = mail;
        mRole = role;
        mPresence = presence;
        mApport = apport;
        mQte = qte;
    }

    private class ViewHolder {

        public TextView textMail;
        public TextView textRole;
        public TextView textPresence;
        public TextView textApport;
        public TextView textQte;
    }

    @Override
    public View getView(int position, View view, ViewGroup container) {
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.invite_list_layout_more_textviews, container, false);
        }

        ViewHolder viewHolder = (ViewHolder) view.getTag();
        if(viewHolder == null){
            viewHolder = new ViewHolder();
            viewHolder.textMail = (TextView) view.findViewById(R.id.textmail);
            viewHolder.textRole = (TextView) view.findViewById(R.id.textrole);
            viewHolder.textPresence = (TextView) view.findViewById(R.id.textpresence);
            viewHolder.textApport = (TextView) view.findViewById(R.id.textapport);
            viewHolder.textQte = (TextView) view.findViewById(R.id.textqte);
            view.setTag(viewHolder);
        }

        // setting here values to the fields of my items from my fan object
        viewHolder.textMail.setText(mMail.get(position));
        viewHolder.textRole.setText(mRole.get(position));
        viewHolder.textPresence.setText(mPresence.get(position));
        viewHolder.textApport.setText(mApport.get(position));
        viewHolder.textQte.setText(mQte.get(position));


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