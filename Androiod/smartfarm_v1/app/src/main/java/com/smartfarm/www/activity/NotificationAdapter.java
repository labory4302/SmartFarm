package com.smartfarm.www.activity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smartfarm.www.R;

import java.util.List;

public class NotificationAdapter extends BaseAdapter {

    private Context context;
    private List<NotificationItem> NotificationList;

    public NotificationAdapter(Context context, List<NotificationItem> noticeList) {
        this.context = context;
        this.NotificationList = noticeList;
    }

    @Override
    public int getCount() {
        return NotificationList.size();
    }

    @Override
    public Object getItem(int i) {
        return NotificationList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.notification_page, null);

        TextView noticeText = (TextView) v.findViewById(R.id.notification_title);
        TextView nameText = (TextView) v.findViewById(R.id.notification_contents);

        noticeText.setText(NotificationList.get(i).getNotificationTitle());
        nameText.setText(NotificationList.get(i).getNotificationContents());

        v.setTag(NotificationList.get(i).getNotificationTitle());
        return v;
    }
}
