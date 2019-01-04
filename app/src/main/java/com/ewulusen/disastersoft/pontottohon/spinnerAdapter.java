package com.ewulusen.disastersoft.pontottohon;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class spinnerAdapter extends BaseAdapter {
private List<String>spinnerList;
private Context mContext;
private LayoutInflater layoutInflater;

    public spinnerAdapter(List<String> spinnerList, Context ct) {
        this.spinnerList = spinnerList;
       this.mContext=ct;

    }

    @Override
    public int getCount() {
        return spinnerList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=convertView;
        if(convertView == null) {
           view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.spinnerrow, parent, false);
        }
            TextView name=view.findViewById(R.id.yearAndHonap);
            name.setText(spinnerList.get(position));


        return view;
    }
}
