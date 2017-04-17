package com.homework.wtw.adapter;

/**
 * Created by ts on 2017/3/8.
 */

import java.util.ArrayList;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.homework.wtw.diary.R;

public class DiaryPublishAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Context context;
    private ArrayList<String> listUrls;
    private String TAG = "DiaryPublishAdapter";


    public DiaryPublishAdapter(Context context, ArrayList<String> listUrls) {
        this.context = context;
        this.listUrls = listUrls;
//        if(listUrls.size() == 10){
        if(listUrls.size() == 2){
            listUrls.remove(listUrls.size()-1);
        }
        Log.i(TAG,"listUrls.size="+listUrls.size());
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        int total = listUrls.size();
//        if (total < 9)
        if (total < 1)
            total++;
        return total;

//        return listUrls.size();
    }

//    @Override
//    public Uri getItem(int position) {
//        return list.get(position);
//    }

    @Override
    public String getItem(int position) {
        return listUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({ "ViewHolder", "InflateParams" })
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.item_diary_gridview_image, null);
        ImageView sdv_image = (ImageView) convertView.findViewById(R.id.sdv_image);

//        if (position == listUrls.size() && listUrls.size() < 9) {
        if (position == listUrls.size() && listUrls.size() < 1) {
            sdv_image.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_add_picture));
        } else {
            Glide.with(context)
                    .load(listUrls.get(position))
                    .placeholder(R.color.item_gray)
                    .error(R.color.item_gray)
                    .centerCrop()
                    .crossFade()
                    .into(sdv_image);
        }

        return convertView;
    }

}
