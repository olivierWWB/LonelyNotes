package com.homework.wtw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.homework.wtw.diary.R;

/**
 * Created by ts on 2017/3/14.
 */

public class SettingActivity extends BaseActivity{

    private Toolbar toolbar;
    private TextView changePswText, aboutUsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

//        container = (LinearLayout) findViewById(R.id.container);
//        initSystemBar(container);

        toolbar = initMyToolBar("设置");

        changePswText = (TextView)findViewById(R.id.textview_setting_change_psw);
        aboutUsText = (TextView)findViewById(R.id.textview_setting_about_us);

        changePswText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("去修改密码啊！");
            }
        });

        aboutUsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, AboutUsActivity.class);
                startActivity(intent);
            }
        });
    }
}
