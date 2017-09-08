package com.servicetracker.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.servicetracker.R;
import com.servicetracker.constants.CircleTransformation;
import com.servicetracker.model.UserListModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewUserAdapter extends BaseAdapter {

    private Context context;
    private List<UserListModel> arlUserList;

    public NewUserAdapter(Context context, List<UserListModel> arlUserList) {
        this.context = context;
        this.arlUserList = arlUserList;
    }

    @Override
    public int getCount() {
        return arlUserList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return arlUserList.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_new_user, parent, false);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            holder.ivProfilePic = (ImageView) convertView.findViewById(R.id.ivProfilePic);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (arlUserList.get(position).getName() != null && arlUserList.get(position).getName().length() > 0) {
            holder.tvName.setText(arlUserList.get(position).getName());
        } else {
            holder.tvName.setText("");
        }

        if (arlUserList.get(position).getImage1() != null && !arlUserList.get(position).getImage1().equalsIgnoreCase("")) {
            if (arlUserList.get(position).getImage1().contains("http:")) {
                Log.e("image", "image" + arlUserList.get(position).getImage1());
                if (!arlUserList.get(position).getImage1().equalsIgnoreCase("http://172.16.0.9/PROJECTS/ServiceTrackingApp/trunk/sites/default/files/chemicalimages/") && !arlUserList.get(position).getImage1().equalsIgnoreCase("http://ec2-52-1-133-240.compute-1.amazonaws.com/PROJECTS/ServiceTrackingApp/trunk/sites/default/files/chemicalimages/")) {
                    Picasso.with(context).load(arlUserList.get(position).getImage1()).placeholder(R.mipmap.pro).transform(new CircleTransformation()).into(holder.ivProfilePic);
                } else {
                    holder.ivProfilePic.setBackgroundResource(R.mipmap.pro);
                }
            } else {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(arlUserList.get(position).getImage1(), options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                Bitmap bm = BitmapFactory.decodeFile(arlUserList.get(position).getImage1(), options);
                Log.e("image", "image" + arlUserList.get(position).getImage1());
                holder.ivProfilePic.setImageBitmap(bm);
            }
        } else {
            holder.ivProfilePic.setBackgroundResource(R.mipmap.pro);
        }
        return convertView;
    }


    public class ViewHolder {
        TextView tvName;
        ImageView ivProfilePic;
    }

}