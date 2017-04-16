package com.homework.wtw.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.homework.wtw.diary.R;

/**
 * Created by ts on 2017/3/14.
 */

public class AboutUsActivity extends BaseActivity{
    private Toolbar toolbar;
    private TextView text1, text2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
//        container = (LinearLayout) findViewById(R.id.container);
//        initSystemBar(container);

        toolbar = initMyToolBar("关于我们");

    }
}
