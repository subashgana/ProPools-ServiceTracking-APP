package com.servicetracker.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.servicetracker.R;

import java.util.List;


public class SpinnerAdapter extends BaseAdapter{
	private Context mContext;
	private List<String> arlList;
	public SpinnerAdapter(Context mContext, List<String> arlList) {
		super();
		this.mContext = mContext;
		this.arlList=arlList;
	}
	public class ViewHolder
	{
		TextView tvName;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		LayoutInflater mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if(convertView==null)
		{
			holder=new ViewHolder();
			convertView=mInflater.inflate(R.layout.row_spinner, null);
			holder.tvName=(TextView)convertView.findViewById(R.id.tvValues);
			holder.tvName.setTypeface(null, Typeface.BOLD);
			holder.tvName.setGravity(Gravity.CENTER);
			convertView.setTag(holder);
		}	
		else {
			holder=(ViewHolder) convertView.getTag();
		}
		if(holder!=null ){
			holder.tvName.setText(arlList.get(position));
		}
		return convertView;
	}

}
