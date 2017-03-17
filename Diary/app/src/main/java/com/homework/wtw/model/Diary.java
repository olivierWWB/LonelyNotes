package com.homework.wtw.model;

/**
 * Created by ts on 17/3/7.
 */

import java.util.ArrayList;

public class Diary {
    private double distance;
    private ArrayList<DiaryMessage> diaryMessagesList = new ArrayList<>();

    public ArrayList<DiaryMessage> getDiaryMessagesList() {
        return diaryMessagesList;
    }

    private int is_active; //-1-已经删除。1-没删除
    private String picture;
    private String content;
    private String create_time;
    private String day;
    private int diary_id;
    private String tag_content;
    private String tag;
    private int user_message;
    private String address;
    private String whether;
    private double latitudes;
    private double longitudes;
    private int whether_image;


    public Diary(int diary_id, String content, String tag, String create_time, String picture,  String address, String whether, int whetherImage, ArrayList<DiaryMessage> diaryMessages){
        this.diary_id = diary_id;
        this.content = content;
        this.tag_content = tag;
        this.create_time = create_time;
        this.picture = picture;
        this.address = address;
        this.whether = whether;
        this.diaryMessagesList = diaryMessages;
    }
    public Diary(int diary_id, String content, String tag, String create_time, String picture,  String address, String whether, int whether_image, ArrayList<DiaryMessage> diaryMessages, int is_active, String day){
        this.diary_id = diary_id;
        this.content = content;
        this.tag_content = tag;
        this.create_time = create_time;
        this.picture = picture;
        this.address = address;
        this.whether = whether;
        this.diaryMessagesList = diaryMessages;
        this.is_active = is_active;
        this.whether_image = whether_image;
        this.day = day;
    }


    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public double getLatitudes() {
        return latitudes;
    }

    public void setLatitudes(double latitudes) {
        this.latitudes = latitudes;
    }

    public double getLongitudes() {
        return longitudes;
    }

    public void setLongitudes(double longitudes) {
        this.longitudes = longitudes;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public void setDiaryMessagesList(ArrayList<DiaryMessage> diaryMessagesList) {
        this.diaryMessagesList = diaryMessagesList;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getTag_content() {
        return tag_content;
    }

    public void setTag_content(String tag_content) {
        this.tag_content = tag_content;
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

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getWhether_image() {
        return whether_image;
    }

    public void setWhether_image(int whether_image) {
        this.whether_image = whether_image;
    }
}

