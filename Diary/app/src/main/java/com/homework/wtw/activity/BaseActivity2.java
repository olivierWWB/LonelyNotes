package com.homework.wtw.activity;

/**
 * Created by ts on 2017/3/9.
 */

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.homework.wtw.diary.R;
import com.homework.wtw.util.SystemBarTintManager;


public class BaseActivity2 extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 设置状态栏的样式
     * @param container 布局
     */
    public void initSystemBar(View container) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//当系统版本等于4.4
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintResource(R.color.light_blue);
            tintManager.setStatusBarTintEnabled(true);
            SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
            container.setPadding(0, config.getPixelInsetTop(true), 0, config.getPixelInsetBottom());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//当系统的版本大于5.0
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.light_blue));
        }
    }

    /**
     * 初始化Toolbar
     */
    protected Toolbar initToolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        return toolbar;
    }

    protected Toolbar initToolBar(String title){
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        return toolbar;
    }


    public void showToast(String content){
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }
}
