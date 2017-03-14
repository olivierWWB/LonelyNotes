package com.homework.wtw.activity;

/**
 * Created by ts on 17/3/7.
 */

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.homework.wtw.diary.R;

import com.homework.wtw.util.SystemBarTintManager;

public class BaseActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ColorDrawable drawable = new ColorDrawable(getResources().getColor(R.color.light_blue));
//         android.support.v7.app.ActionBar bar = this.getSupportActionBar();
//        bar.setBackgroundDrawable(drawable);
//        bar.setDisplayHomeAsUpEnabled(true);
//        bar.setDisplayShowHomeEnabled(false);
//        Constant.finishActivities.add(this);



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();

            default:
                return super.onOptionsItemSelected(item);
        }
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

    /**
     * 初始化Toolbar
     */
    protected Toolbar initMainToolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        return toolbar;
    }

    protected Toolbar initMainToolBar2(String title){
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        return toolbar;
    }

    /**
     * 初始化Toolbar
     */
    protected Toolbar initMyToolBar(String title){//ts写的啊哈哈。她是为了用在话题详情页哦吼吼～～～
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        return toolbar;
    }

    /**
     * 初始化Toolbar
     */
    protected Toolbar initToolBar(String title, String subtitle){//ts写的啊哈哈。她好像用在了话题分类页面和个人主页页面上。主要为了设置标题
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.white));
        toolbar.setSubtitle(subtitle);
        return toolbar;
    }

    /**
     * 实现点击空白处，软键盘消失事件
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     *
     * @param token
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void showToast(String content){
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }
}
