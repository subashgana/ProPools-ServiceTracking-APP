package com.servicetracker.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.servicetracker.R;

import java.util.List;

public class CustomSpinnerAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> arlList;
    private int Selected;

    public CustomSpinnerAdapter(Context mContext, List<String> arlList) {
        super();
        this.mContext = mContext;
        this.arlList = arlList;
    }

    @Override
    public int getCount() {
        return arlList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.row_custom_spinner, null);
            holder.tvName = (TextView) convertView.findViewById(R.id.tvCountry);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (holder != null) {
            holder.tvName.setText(arlList.get(position));
        }
        return convertView;
    }

    public class ViewHolder {
        TextView tvName;
    }
}