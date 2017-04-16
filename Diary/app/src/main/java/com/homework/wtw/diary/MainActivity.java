package com.homework.wtw.diary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.homework.wtw.activity.DiaryListActivity;
import com.homework.wtw.activity.DiaryPublishActivity;
import com.homework.wtw.model.Diary;
import com.homework.wtw.model.DiaryMessage;
import com.homework.wtw.util.Constant;

import java.util.ArrayList;


import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.view.SurfaceHolder.Callback;

public class MainActivity extends AppCompatActivity {
    private  Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(MainActivity.this, DiaryListActivity.class);
        setContentView(R.layout.activity_main);
       // btn = (Button)this.findViewById(R.id.test_btn);
//
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {

//                startActivity(intent);
//            }
//        });



    }



}
