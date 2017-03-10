package com.homework.wtw.view;

/**
 * Created by ts on 17/3/7.
 */

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;


import com.homework.wtw.diary.R;
import com.homework.wtw.listener.AnimateFirstDisplayListener;
import com.homework.wtw.util.ImageShowManager;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;



public class CustomImageView extends ImageView {
    private String url;
    private boolean isAttachedToWindow;
    private Context context;
    private ImageShowManager imageManager;

    private DisplayImageOptions options;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private LayoutInflater inflater;

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
//        this.imageManager = ImageShowManager.from(context);

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

    public CustomImageView(Context context) {
        super(context);
        this.context = context;
//        this.imageManager = ImageShowManager.from(context);

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

    public void setImageManager(ImageShowManager imageManager){
        this.imageManager = imageManager;
    }


    @Override
    public void onAttachedToWindow() {
        isAttachedToWindow = true;
        setImageUrl(url);
        Log.i("Load", "onAttachedToWindow");
        super.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
        isAttachedToWindow = false;
        setImageBitmap(null);
        Log.i("Load", "onDetachedFromWindow");

        super.onDetachedFromWindow();
    }


    public void setImageUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            this.url = url;
            if (isAttachedToWindow) {
//                //初始化ImageLoader
                ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.init(ImageLoaderConfiguration.createDefault(context));
                imageLoader.displayImage(url, this, options, animateFirstListener);
                imageLoader.destroy();

//                ImageLoader.getInstance().displayImage(url, this, options, animateFirstListener);
            }
        }
    }
}
