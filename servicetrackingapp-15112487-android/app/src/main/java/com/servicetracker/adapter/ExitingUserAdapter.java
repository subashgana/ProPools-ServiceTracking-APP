package com.servicetracker.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.servicetracker.R;
import com.servicetracker.constants.CircleTransformation;
import com.servicetracker.constants.CommonUtils;
import com.servicetracker.model.UserListModel;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ExitingUserAdapter extends BaseAdapter {

    private Context context;
    private List<UserListModel> arlExitingUsers;
    ViewHolder holder = null;

    public ExitingUserAdapter(Context context, List<UserListModel> arlExitingUsers){
        this.context = context;
        this.arlExitingUsers = arlExitingUsers;
    }

    @Override
    public int getCount() {
        return arlExitingUsers.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {

        return arlExitingUsers.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_exiting_user,parent, false);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            holder.ivProfilePic = (ImageView) convertView.findViewById(R.id.ivProfilePic);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (arlExitingUsers.get(position).getTimestamp()!= null && arlExitingUsers.get(position).getTimestamp().length()>0) {
            long difference = System.currentTimeMillis()/1000 - Long.parseLong(arlExitingUsers.get(position).getTimestamp());
            int  days = (int) (difference / (60*60*24));
            int hours = (int) ((difference - (60*60*24*days)) / (60*60));
            int  min = (int) (difference - (60*60*24*days) - (60*60*hours)) / (60);
            hours = (hours < 0 ? -hours : hours);
            Log.i("======= Hours"," :: "+hours);
            Log.e("timestamp is", "timestamp is" + arlExitingUsers.get(position).getTimestamp()+"hours"+hours+"dayse"+days);
            if(days==0&&hours==0&&min!=0&&min>0){
                holder.tvTime.setText(min + context.getString(R.string.mins_ago));
            }else if(days==0&&hours!=0) {
                holder.tvTime.setText(hours + context.getString(R.string.hours_ago));
            }else if(days!=0 &&hours!=0){
                holder.tvTime.setText(days + context.getString(R.string.days_and) + hours + context.getString(R.string.hours_ago));
            }else if(days!=0){
                holder.tvTime.setText(days+context.getString(R.string.days_ago));
            }else{
                holder.tvTime.setText(context.getString(R.string.few_seconds_ago));
            }
        } else {
            holder.tvTime.setText("");
        }
        if (arlExitingUsers.get(position).getTimestamp()!= null && arlExitingUsers.get(position).getName().length()>0) {
            holder.tvName.setText(arlExitingUsers.get(position).getName());
        } else {
            holder.tvName.setText("");
        }
        if (arlExitingUsers.get(position).getImage1() != null && !arlExitingUsers.get(position).getImage1() .equalsIgnoreCase("")) {
            if(arlExitingUsers.get(position).getImage1().contains("http:")){
                Picasso.with(context).load(arlExitingUsers.get(position).getImage1()).placeholder(R.mipmap.pro).transform(new CircleTransformation()).into(holder.ivProfilePic);
             // holder.ivProfilePic.setImageBitmap(getBitmapFromURL(arlExitingUsers.get(position).getImage1()));
              //  new AsyncSettingNotifyArt().execute(arlExitingUsers.get(position).getImage1());
            }else {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(arlExitingUsers.get(position).getImage1(), options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                Bitmap bm = BitmapFactory.decodeFile(arlExitingUsers.get(position).getImage1(), options);
                Log.e("image", "image" + arlExitingUsers.get(position).getImage1());
                holder.ivProfilePic.setImageBitmap(bm);
            }// Picasso.with(context).load(new File(arlExitingUsers.get(position).getImage1())).transform(new CircleTransformation()).into(holder.ivProfilePic);
        } else {
            holder.ivProfilePic.setBackgroundResource(R.mipmap.pro);
        }
        return convertView;
    }
    public class ViewHolder{
        TextView tvName,tvTime;
        ImageView ivProfilePic;
    }
    private class AsyncSettingNotifyArt extends AsyncTask<String, Void, Void> {
        Bitmap bitmap = null;
        @Override
        protected Void doInBackground(String... params) {
            String fileUrl = params[0];
            //this method will be running on background thread so don't update UI from here
            try {
                if (fileUrl!=null&&fileUrl.length()>0) {
                    URL myFileUrl = new URL(fileUrl);
                    HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (bitmap != null){
                holder.ivProfilePic.setImageBitmap(bitmap);
            }else {
                holder.ivProfilePic.setImageBitmap(CommonUtils.decodeSampledBitmapFromResource(context.getResources(), R.mipmap.pro, 100, 100));
            }
        }
    }
}
