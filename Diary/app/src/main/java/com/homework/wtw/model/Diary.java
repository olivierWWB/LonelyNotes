package com.homework.wtw.model;

/**
 * Created by ts on 17/3/7.
 */

import java.util.ArrayList;

public class Diary {
    private ArrayList<DiaryMessage> diaryMessagesList = new ArrayList<>();

    public ArrayList<DiaryMessage> getDiaryMessagesList() {
        return diaryMessagesList;
    }

    private String picture;
    private String content;
    private long create_time;
    private String date;
    private String day;
    private int diary_id;
    private String tag;
    private int user_message;
    private String address;
    private String whether;
    private int whether_image;


    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public void setDiaryMessagesList(ArrayList<DiaryMessage> diaryMessagesList) {
        this.diaryMessagesList = diaryMessagesList;
    }


    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getUser_message() {
        return user_message;
    }

    public void setUser_message(int user_message) {
        this.user_message = user_message;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWhether() {
        return whether;
    }

    public void setWhether(String whether) {
        this.whether = whether;
    }

    public int getDiary_id() {
        return diary_id;
    }

    public void setDiary_id(int diary_id) {
        this.diary_id = diary_id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    public int getWhether_image() {
        return whether_image;
    }

    public void setWhether_image(int whether_image) {
        this.whether_image = whether_image;
    }
}

