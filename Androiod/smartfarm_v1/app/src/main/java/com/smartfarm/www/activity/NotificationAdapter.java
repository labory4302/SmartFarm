package com.smartfarm.www.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartfarm.www.R;

import java.util.ArrayList;

public class NotificationAdapter extends BaseAdapter {

    private ImageView notification_mark;
    private TextView notification_title;
    private TextView notification_contents;

    private ArrayList<NotificationItem> notificationItemList = new ArrayList<NotificationItem>();

    public NotificationAdapter(){};

    @Override
    public int getCount() {
        return notificationItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return notificationItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }
        
        notification_title = (TextView) convertView.findViewById(R.id.notification_title);
        notification_contents = (TextView) convertView.findViewById(R.id.notification_contents);

        return null;
    }

    public void addItem(String notificationTitle, String notificationContents){
        NotificationItem item = new NotificationItem();

       // item.setNotificationTitle(title);
       // item.setNotificationContents(contents);

        notificationItemList.add(item);
    }
}
