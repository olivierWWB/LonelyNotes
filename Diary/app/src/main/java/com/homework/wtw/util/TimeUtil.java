package com.homework.wtw.util;

/**
 * Created by ts on 2017/3/8.
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {
    private static String finalTime = "now";
    private String topicTime;
    private String commentTime;

    public TimeUtil(String topicTime) {
        this.topicTime = topicTime;
    }

    public static String getFinalTime(String TopicTime) {
        String[] dateTime = TopicTime.split(" ");
        String[] date = dateTime[0].split("-");
        String[] time = dateTime[1].split(":");

        String year = date[0];
        String month = date[1];
        String day = date[2];
        String hour = time[0];
        String minute = time[1];
        String second = time[2];


        //获取系统时间
        Calendar c = Calendar.getInstance();
        int systemYear = c.get(Calendar.YEAR);
        int systemMonth = c.get(Calendar.MONTH) + 1;
        int systemDay = c.get(Calendar.DAY_OF_MONTH);
        int systemHour = c.get(Calendar.HOUR_OF_DAY);
        int systemMinute = c.get(Calendar.MINUTE);
        int systemSecond = c.get(Calendar.SECOND);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        int days = 0;
        int minutes = 0;
        int hours = 0;

        try {
            days = daysBetween(dateTime[0], sdf.format(new Date()));
            hours = hoursBetween(TopicTime, sdf.format(new Date()));
            minutes = minutesBetween(TopicTime, sdf.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //同一天，同一小时，同一分钟
        if (days <= 0 && hours <= 0 && minutes <= 0) {
            finalTime = "刚刚";
        } else {
            if (days == 0 && hours == 0) {
                finalTime = String.valueOf(minutes) + "分钟前";
            } else if (days == 0) {
                finalTime = String.valueOf(hours) + "小时前";
            } else {
                finalTime = String.valueOf(days) + "天前";
            }
        }

        return finalTime;
    }

    public void setFinalTime(String finalTime) {
        this.finalTime = finalTime;
    }

    public static int daysBetween(String smdate, String bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(smdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(bdate));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }


    public static int hoursBetween(String smdate, String bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(smdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(bdate));
        long time2 = cal.getTimeInMillis();
        long between_minutes = (time2 - time1) / (1000 * 3600);

        return Integer.parseInt(String.valueOf(between_minutes));
    }

    public static int minutesBetween(String smdate, String bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(smdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(bdate));
        long time2 = cal.getTimeInMillis();
        long between_minutes = (time2 - time1) / (1000 * 60);

        return Integer.parseInt(String.valueOf(between_minutes));
    }

    public static String getMyCommentTime() {
        SimpleDateFormat df = new SimpleDateFormat("yy-MM-dd HH:mm");//设置日期格式
//        System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
        return df.format(new Date());
    }

    public static String getCurrentTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//        System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
        return df.format(new Date());
    }

    public static String getCurrentDay() {
        SimpleDateFormat df = new SimpleDateFormat("EEEE");//设置日期格式
        String day = df.format(new Date());
        if(day.equals("Monday")){
            return "星期一";
        }else if(day.equals("Tuesday")){
            return "星期二";
        }else if(day.equals("Wednesday")){
            return "星期三";
        }else if(day.equals("Thursday")){
            return "星期四";
        }else if(day.equals("Friday")){
            return "星期五";
        }else if(day.equals("Saturday")){
            return "星期六";
        }else if(day.equals("Sunday")){
            return "星期日";
        }
        return df.format(new Date());
    }
}
