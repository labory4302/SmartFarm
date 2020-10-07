package com.smartfarm.www.activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.smartfarm.www.R;

import java.util.ArrayList;

public class DetectListViewAdapter extends BaseAdapter {

    private TextView detect_time, detect_content;
    private ImageView detect_image;


    private ArrayList<DetectListViewItem> DetectListViewItemList = new ArrayList<DetectListViewItem>();

    public DetectListViewAdapter(){

    }

    @Override
    public int getCount() {
        return DetectListViewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return DetectListViewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.detect_listview_item, parent, false);
        }
        detect_time = (TextView) convertView.findViewById(R.id.detect_time);
        detect_content = (TextView) convertView.findViewById(R.id.detect_content);

        DetectListViewItem detectListViewItem = DetectListViewItemList.get(position);

        detect_time.setText(detectListViewItem.getTitle());
        detect_content.setText(detectListViewItem.getContent());


        return convertView;
    }
    public void addItem(String detect_image, String detect_time, String detect_content){
        DetectListViewItem item = new DetectListViewItem();

        item.setTime(detect_image);
        item.setTitle(detect_time);
        item.setContent(detect_content);

        DetectListViewItemList.add(item);
    }
}
