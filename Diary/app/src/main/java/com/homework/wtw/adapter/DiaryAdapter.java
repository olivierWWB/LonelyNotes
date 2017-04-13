package com.homework.wtw.adapter;

/**
 * Created by ts on 17/3/7.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.homework.wtw.activity.DiaryDetailActivity;
import com.homework.wtw.activity.ShowImageActivity;
import com.homework.wtw.diary.R;
import com.homework.wtw.listener.AnimateFirstDisplayListener;
import com.homework.wtw.model.Diary;
import com.homework.wtw.model.Image;
import com.homework.wtw.util.Constant;
import com.homework.wtw.util.ImageShowManager;
import com.homework.wtw.util.ScreenTools;
import com.homework.wtw.view.CustomImageView;
import com.homework.wtw.view.NineGridlayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;


import java.util.ArrayList;
import java.util.List;

public class DiaryAdapter extends BaseAdapter {
    private String TAG = "DiaryAdapter";
    private Context context;
    private ImageShowManager imageManager;
    private List<Diary> diariesList = new ArrayList<>();
    private byte[] picture;
    private Bitmap bmp;
    private DisplayImageOptions options;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private LayoutInflater inflater;

    public DiaryAdapter(Context context, List<Diary> list) {
        this.context = context;

        this.diariesList = list;
        this.imageManager = ImageShowManager.from(context);

        Log.i(TAG, "Create Diary Adapter");

        inflater = LayoutInflater.from(context);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ali)
                .showImageForEmptyUri(R.drawable.ali)
                .showImageOnFail(R.drawable.ali)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                        //.displayer(new CircleBitmapDisplayer(Color.WHITE, 5))
                .build();
    }

    public void setData(List<Diary> list) {
        this.diariesList = list;
    }

    @Override
    public int getCount() {
        Log.i(TAG, "AdapterSize=" + diariesList.size());
        return diariesList.size();

    }

    @Override
    public Object getItem(int position) {
        return diariesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_diary, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.ivMore = (NineGridlayout) convertView.findViewById(R.id.iv_ngrid_layout);
            viewHolder.ivOne = (CustomImageView) convertView.findViewById(R.id.iv_oneimage);
            viewHolder.commentImage = (ImageView) convertView.findViewById(R.id.imageView_remark);
            viewHolder.weatherImage = (ImageView) convertView.findViewById(R.id.imageView_weather);
            viewHolder.linearDirection = (LinearLayout) convertView.findViewById(R.id.linear_direction);
            viewHolder.textContent = (TextView) convertView.findViewById(R.id.textview_detail);
            viewHolder.textWeather = (TextView) convertView.findViewById(R.id.textView_weather);
            viewHolder.textRemarkNum = (TextView) convertView.findViewById(R.id.textView_num_remark);
            viewHolder.textAddress = (TextView) convertView.findViewById(R.id.textView_topic_address);
            viewHolder.textDate = (TextView) convertView.findViewById(R.id.textView_date);
            viewHolder.textDirection = (TextView) convertView.findViewById(R.id.text_direction);
            viewHolder.linearComment = (LinearLayout) convertView.findViewById(R.id.linear_topic_item_comment);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        picture = diariesList.get(position).getPicture();
        if (picture != null) {
            bmp = BitmapFactory.decodeByteArray(picture, 0, picture.length);

            viewHolder.ivMore.setVisibility(View.GONE);
            viewHolder.ivOne.setVisibility(View.VISIBLE);

            viewHolder.ivOne.setImageManager(imageManager);
            Image image = new Image(bmp,bmp.getWidth(),bmp.getHeight());
            handlerOneImage(viewHolder, image);
        }else{
            viewHolder.ivMore.setVisibility(View.GONE);
            viewHolder.ivOne.setVisibility(View.GONE);
        }
/*
        if(bmp == null){
            viewHolder.ivMore.setVisibility(View.GONE);
            viewHolder.ivOne.setVisibility(View.GONE);
        }else{
            viewHolder.ivMore.setVisibility(View.GONE);
            viewHolder.ivOne.setVisibility(View.VISIBLE);

            //设置为同一个imageShowerManager.其实吧。。。没什么用。。嗯。
            viewHolder.ivOne.setImageManager(imageManager);

//            final String url = itemList.get(0).getUrl();

//            viewHolder.ivOne.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context, ShowImageActivity.class);
//                    Bundle bundle = new Bundle();
//                    pictureList = new ArrayList<>();
//                    pictureList.add(url);
//                    bundle.putStringArrayList("url", pictureList);
//                    bundle.putInt("position", 0);
//                    intent.putExtras(bundle);
//                    context.startActivity(intent);
//                }
//            });

            Image image = new Image(bmp,bmp.getWidth(),bmp.getHeight());
            handlerOneImage(viewHolder, image);
        }*/
/*
        if (itemList.isEmpty()) {//没有图片，只显示文字
            viewHolder.ivMore.setVisibility(View.GONE);
            viewHolder.ivOne.setVisibility(View.GONE);
        } else if (itemList.size() == 1) {//只有一张图片，长方形。
            viewHolder.ivMore.setVisibility(View.GONE);
            viewHolder.ivOne.setVisibility(View.VISIBLE);

            //设置为同一个imageShowerManager.其实吧。。。没什么用。。嗯。
            viewHolder.ivOne.setImageManager(imageManager);


            final String url = itemList.get(0).getUrl();

            viewHolder.ivOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ShowImageActivity.class);
                    Bundle bundle = new Bundle();
                    pictureList = new ArrayList<>();
                    pictureList.add(url);
                    bundle.putStringArrayList("url", pictureList);
                    bundle.putInt("position", 0);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
            handlerOneImage(viewHolder, itemList.get(0));

        } else {//有多张图片，正方形。
            viewHolder.ivMore.setVisibility(View.VISIBLE);
            viewHolder.ivOne.setVisibility(View.GONE);

            //设置为同一个imageShowerManager
            viewHolder.ivMore.setImageManager(imageManager);

            viewHolder.ivMore.setImagesData(itemList);
        }
        */
        String[] date = diariesList.get(position).getDate().split(" ");


        viewHolder.textDirection.setText(diariesList.get(position).getTag());
//        viewHolder.textDate.setText(TimeUtil.getFinalTime(diariesList.get(position).getCreate_time()));
//        viewHolder.textDate.setText(diariesList.get(position).getCreate_time());
        viewHolder.textDate.setText(date[0] + " " + diariesList.get(position).getDay());
        viewHolder.textContent.setText(diariesList.get(position).getContent());
        viewHolder.textRemarkNum.setText(String.valueOf(diariesList.get(position).getUser_message()));
        if(diariesList.get(position).getWeather() != null){
            viewHolder.textWeather.setText(diariesList.get(position).getWeather());
        }else{
            viewHolder.textWeather.setText("100℃");// ℃   - ° C
        }
        if(diariesList.get(position).getAddress() != null) {
            viewHolder.textAddress.setText(diariesList.get(position).getAddress());
        }else{
            viewHolder.textAddress.setText("在梦里");
        }
        Log.e("position:"+position, diariesList.get(position).getWeather_image()+"");
        viewHolder.weatherImage.setImageResource(Constant.mImageViewResourceId[diariesList.get(position).getWeather_image()]);

        final int id = position;

        if (diariesList.get(position).getTag() == null || diariesList.get(position).getTag().equals("")) {
            viewHolder.linearDirection.setVisibility(View.GONE);
        } else {
            viewHolder.linearDirection.setVisibility(View.VISIBLE);
        }

        viewHolder.linearComment.setOnClickListener(new View.OnClickListener() {//评论，跳到评论页
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, DiaryDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("fromwhere", 1);// 从列表页点评论按钮跳过去的
                bundle.putInt("diaryId", Constant.diariesList.get(id).getDiary_id());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    private void handlerOneImage(ViewHolder viewHolder, Image image) {
        int totalWidth;
        int imageWidth;
        int imageHeight;
        ScreenTools screentools = ScreenTools.instance(context);
        totalWidth = screentools.getScreenWidth() - screentools.dip2px(80);
        imageWidth = screentools.dip2px(image.getWidth());
        imageHeight = screentools.dip2px(image.getHeight());
        if (image.getWidth() <= image.getHeight()) {
            if (imageHeight > totalWidth) {
                imageHeight = totalWidth;
                imageWidth = (imageHeight * image.getWidth()) / image.getHeight();
            }
        } else {
            if (imageWidth > totalWidth) {
                imageWidth = totalWidth;
                imageHeight = (imageWidth * image.getHeight()) / image.getWidth();
            }
        }
        ViewGroup.LayoutParams layoutparams = viewHolder.ivOne.getLayoutParams();

        if (imageWidth > 100) {
            layoutparams.height = imageHeight;
            layoutparams.width = imageWidth;
            viewHolder.ivOne.setScaleType(ImageView.ScaleType.FIT_XY);
        } else {
            layoutparams.height = imageHeight;
            layoutparams.width = 180;
            viewHolder.ivOne.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        Log.i(TAG, "图片宽度：" + imageWidth + "--图片高度：" + imageHeight);
        viewHolder.ivOne.setLayoutParams(layoutparams);
        viewHolder.ivOne.setClickable(true);

        //直接加载啊哈哈哈哈哈哈如果图大就超慢啊哈哈可能会OOM啊哈哈
        //异步加载
        Log.i(TAG, "图片地址：" +image.getUrl());
//        viewHolder.ivOne.setImageUrl(image.getUrl());
        viewHolder.ivOne.setImageBitmap(image.getBitmap());
    }


    class ViewHolder {
        public NineGridlayout ivMore;
        public CustomImageView ivOne;
        public ImageView commentImage, weatherImage;
        public TextView textContent, textAddress, textDate, textRemarkNum, textWeather,textDirection;
        public LinearLayout linearDirection, linearComment;
    }


}

