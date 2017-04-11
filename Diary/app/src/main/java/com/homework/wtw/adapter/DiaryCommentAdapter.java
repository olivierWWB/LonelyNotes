package com.homework.wtw.adapter;

/**
 * Created by ts on 2017/3/8.
 */

import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

//import com.android.volley.Request;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
import com.homework.wtw.diary.R;
import com.homework.wtw.listener.AnimateFirstDisplayListener;
import com.homework.wtw.model.DiaryMessage;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;


public class DiaryCommentAdapter extends BaseAdapter {
    private String TAG = "DiaryCommentAdapter";
    private List<DiaryMessage> commentsList = new ArrayList<>();;
    private Context context;
    private DisplayImageOptions options;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private LayoutInflater inflater;

    public DiaryCommentAdapter(Context context,List<DiaryMessage> list){
        this.context = context;
        Log.i(TAG,"addComment0");
        this.commentsList = list;
        inflater = LayoutInflater.from(context);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                //.displayer(new CircleBitmapDisplayer(Color.WHITE, 5))
                .build();
    }

    public void setData(List<DiaryMessage> list) {
        this.commentsList = list;
    }

    @Override
    public int getCount() {
        return commentsList.size();
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
        View view = convertView;
        ViewHolder holder;
        if (convertView == null) {
            view = inflater.inflate(R.layout.item_diary_comment, parent, false);
            holder = new ViewHolder();
            holder.mTextViewTime = (TextView) view.findViewById(R.id.textView_comment_time);
            holder.mTextViewDetail = (TextView)view.findViewById(R.id.textview_comment_detail);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.mTextViewTime.setText(commentsList.get(position).getCreate_time());
        holder.mTextViewDetail.setText(commentsList.get(position).getContent());

        return view;
    }

    class ViewHolder{
        TextView mTextViewTime,mTextViewDetail;
    }

}
