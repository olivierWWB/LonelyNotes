package com.homework.wtw.diary;

import android.app.Application;
import android.content.Context;

import com.homework.wtw.database.DiarySQLiteOpenHelper;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;


/**
 * Created by wangwenbo on 2017/3/30.
 */

public class DiaryApplication extends Application {
    private RefWatcher mRefWatcher;
    public static DiarySQLiteOpenHelper diarySQLiteOpenHelper;
    @Override
    public void onCreate() {
        super.onCreate();
        diarySQLiteOpenHelper = DiarySQLiteOpenHelper.getInstance(this);
        mRefWatcher = LeakCanary.install(this);
    }
    public static RefWatcher getRefWatcher(Context context){
        DiaryApplication mApp = (DiaryApplication) context.getApplicationContext();
        return mApp.mRefWatcher;
    }
}
