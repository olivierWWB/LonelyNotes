package com.homework.wtw.model;

import android.graphics.Bitmap;

/**
 * Created by ts on 17/3/7.
 */

public class Image {
    private String url;
    private Bitmap bitmap;
    private int width;
    private int height;

    public Image(String url, int width, int height) {
        this.url = url;
        this.width = width;
        this.height = height;
    }

    public Image(Bitmap bitmap, int width, int height) {
        this.bitmap = bitmap;
        this.width = width;
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public String toString() {

        return "image---->>url="+url+"width="+width+"height"+height;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
