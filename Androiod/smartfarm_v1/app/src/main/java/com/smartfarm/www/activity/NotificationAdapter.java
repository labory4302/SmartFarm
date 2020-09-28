//package com.smartfarm.www.activity;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.smartfarm.www.R;
//import com.smartfarm.www.data.NotificationData;
//import com.smartfarm.www.data.NotificationResponse;
//
//import java.util.ArrayList;
//
//public class NotificationAdapter extends BaseAdapter {
//
//    Context mContext = null;
//    LayoutInflater mLayoutInflater = null;
//    ArrayList<NotificationResponse> notification;
//
//    @Override
//    public int getCount() {
//        return notification.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return position;
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return notification.get(position);
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View view = mLayoutInflater.inflate(R.layout.notification_item, null);
//
//        ImageView imageView = (ImageView)view.findViewById(R.id.notification_mark);
//        TextView movieName = (TextView)view.findViewById(R.id.notification_title);
//        TextView grade = (TextView)view.findViewById(R.id.notification_contents);
//
//        movieName.setText(notification.get(position).getNotificationTitle());
//        grade.setText(notification.get(position).getNotificationContents());
//
//        return view;
//    }
//}
