package com.homework.wtw.activity;

/**
 * Created by ts on 2017/3/8.
 */

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.homework.wtw.database.DiaryDataBaseOperate;
import com.homework.wtw.diary.DiaryApplication;
import com.homework.wtw.diary.MainActivity;
import com.thinkpage.lib.api.*;


import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.homework.wtw.adapter.DiaryPublishAdapter;
import com.homework.wtw.diary.R;
import com.homework.wtw.model.Diary;
import com.homework.wtw.model.DiaryMessage;
import com.homework.wtw.util.Constant;
import com.homework.wtw.util.PictureUtil;
import com.homework.wtw.util.TimeUtil;
import com.homework.wtw.util.Utils;

import org.json.JSONArray;


import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

public class DiaryPublishActivity extends BaseActivity2 {
    private DiaryDataBaseOperate diaryDataBaseOperate;
//    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
//    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int REQUEST_CAMERA_CODE = 10;
    private static final int REQUEST_PREVIEW_CODE = 20;
    private static String[] spinnerString = new String[]{"生活","工作","学习"};
    String filename = Environment.getExternalStorageDirectory().toString() + File.separator + "wtw/image/";
    Map<String, String> params = new HashMap<String, String>();
    private String TAG = "DiaryPublishActivity";
    private String imagePath = null;
    private GridView gridview;
    private LinearLayout ll_location; //获取城市和天气
    private DiaryPublishAdapter adapter;
    // 显示位置的TextView。定位布局哦吼吼～～～～～～～
    private TextView tv_location;
    private ImageView iv_weather;
    private TextView tv_temperature;
    private String mylocation;
    // 发送按钮
    private LinearLayout container;
    private ImageView mImageView;
    private TextView mSendMsgTextView;
    private Toolbar toolbar;
    // 文本输入
    private EditText et_content;
    //选择方向
    private Spinner spinner;
    private ArrayAdapter<String> spinnerAdapter;
    private String spinnerValue;
    private int tagID = 0;
    private int type = 0;
    private ProgressDialog dialog;
    private Intent intent;
    private ArrayList<String> paths = new ArrayList<String>();
    private ProgressDialog mProgressDialog;
    static String weather = "";
    static int temperature = 0;

    TPWeatherManager weatherManager = TPWeatherManager.sharedWeatherManager();
//使用心知天气官网获取的key和用户id初始化WeatherManager
    // 高德定位
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = new AMapLocationClientOption();
    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation loc) {
            if (null != loc) {
                //解析定位结果
                mylocation = Utils.getCity(loc, 0);

                String weathercity = Utils.getCity(loc, 1);

                weatherManager.getWeatherNow(new TPCity(weathercity)
                        , TPWeatherManager.TPWeatherReportLanguage.kSimplifiedChinese
                        , TPWeatherManager.TPTemperatureUnit.kCelsius
                        , new TPListeners.TPWeatherNowListener() {
                            @Override
                            public void onTPWeatherNowAvailable(TPWeatherNow weatherNow, String errorInfo) {
                                if (weatherNow != null) {
                                    //weatherNow 就是返回的当前天气信息。
                                    temperature = weatherNow.temperature;
                                    tv_location.setText(mylocation);
                                    Constant.imageId = Integer.valueOf(weatherNow.code);
                                    iv_weather.setImageResource(Constant.mImageViewResourceId[Constant.imageId]);
                                    tv_temperature.setText(temperature+"℃");
                                    stopLocation();
                                } else {
                                    weather = "";//错误信息
                                    tv_location.setText(mylocation);
                                    stopLocation();
                                }
                            }});
            } else {
                tv_location.setText("");
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_publish);//初始化定位
        weatherManager.initWithKeyAndUserId("stgqeqzd7smkfdzn","U831694032");
        diaryDataBaseOperate = new DiaryDataBaseOperate(DiaryApplication.diarySQLiteOpenHelper.getWritableDatabase());
        initLocation();

//        container = (LinearLayout) findViewById(R.id.container);
//        initSystemBar(container);

        toolbar = initToolBar("");
        mImageView = new ImageView(this);//右上角！！！！！！！
        mImageView.setVisibility(View.VISIBLE);
        initMenu(toolbar);
//        diariesList = new ArrayList<>();
        initView();

//        getTags();

    }

    /**
     * 初始化定位
     *
     */
    private void initLocation(){
        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        //设置定位参数
        locationClient.setLocationOption(getDefaultOption());
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }
    /**
     * 默认的定位参数
     *
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }
    private void initView() {
        gridview = (GridView) this.findViewById(R.id.gridview);

        Constant.publishImagePaths = new ArrayList<>();

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Constant.publishImagePaths.size() < 9 && position == Constant.publishImagePaths.size()) {
                    PhotoPicker.builder()
                            .setPhotoCount(9)
                            .setShowCamera(true)
                            .setShowGif(true)
                            .setPreviewEnabled(false)
                            .setSelected(Constant.publishImagePaths)
                            .start(DiaryPublishActivity.this, REQUEST_CAMERA_CODE);
                } else {
                    PhotoPreview.builder()
                            .setPhotos(Constant.publishImagePaths)
                            .setCurrentItem(position)
                            .start(DiaryPublishActivity.this, REQUEST_PREVIEW_CODE);
                }

            }
        });

        adapter = new DiaryPublishAdapter(DiaryPublishActivity.this, Constant.publishImagePaths);
        gridview.setAdapter(adapter);


        // 获取位置
        //以下是定位的布局哦吼吼～～～～～
        tv_location = (TextView) this.findViewById(R.id.text_diary_publish_get_city);
        tv_temperature = (TextView) this.findViewById(R.id.text_diary_publish_get_temperature);
        iv_weather = (ImageView) this.findViewById(R.id.text_diary_publish_get_weather_image);
        ll_location = (LinearLayout) this.findViewById(R.id.linear_diary_publish_get_city);

        ll_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                InitLocation();
                //在这里定位，获取天气！！！获取成功后，把tv_location文字换成城市名+当前气温
                showToast("Start Locating");
                startLocation();

            }
        });

        et_content = (EditText) this.findViewById(R.id.et_content);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerString);
        //设置下拉列表的风格
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将spinnerAdapter 添加到spinner中
        spinner.setAdapter(spinnerAdapter);


        //添加事件Spinner事件监听
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerValue = spinnerString[position];
                Log.i(TAG,"spinner=="+spinnerValue+"//position="+position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //设置默认值
        spinner.setVisibility(View.VISIBLE);

    }
    /**
     * 开始定位
     *
     */
    private void startLocation(){
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }

    /**
     * 停止定位
     *
     */
    private void stopLocation(){
        // 停止定位
        locationClient.stopLocation();
    }


    @SuppressLint("SdCardPath")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 选择照片
                case REQUEST_CAMERA_CODE:
                    ArrayList<String> list = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                    Log.d(TAG, "list: " + "list = [" + list.size());
                    loadAdpater(list);
                    break;
                // 预览
                case REQUEST_PREVIEW_CODE:
                    ArrayList<String> ListExtra = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                    Log.d(TAG, "ListExtra: " + "ListExtra = [" + ListExtra.size());
                    loadAdpater(ListExtra);
                    break;
                default:
                    break;

            }
            adapter.notifyDataSetChanged();
        }
    }

    private void loadAdpater(ArrayList<String> paths) {
        if (Constant.publishImagePaths != null && Constant.publishImagePaths.size() > 0) {
            Constant.publishImagePaths.clear();
        }
        Constant.publishImagePaths.addAll(paths);
        adapter = new DiaryPublishAdapter(DiaryPublishActivity.this, Constant.publishImagePaths);
        gridview.setAdapter(adapter);
        try {
            JSONArray obj = new JSONArray(Constant.publishImagePaths);
            Log.e(TAG, obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        destroyLocation();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
    private void destroyLocation(){
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }
    private void initMenu(Toolbar toolbar) {

        mSendMsgTextView = new TextView(this);
        mSendMsgTextView.setText("写好了");
        mSendMsgTextView.setTextColor(getResources().getColor(R.color.white));
        mSendMsgTextView.setTextSize(18);
        mSendMsgTextView.setPadding(0, 0, 30, 0);
        mSendMsgTextView.setGravity(Gravity.RIGHT);

        Toolbar.LayoutParams params = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT,
                Toolbar.LayoutParams.WRAP_CONTENT,
                Gravity.RIGHT);
        //设置外边界
        params.setMargins(0, 0, 15, 0);
        toolbar.addView(mSendMsgTextView, params);

        mSendMsgTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送
                String content = et_content.getText().toString().trim();
                //图片文件列表
                List<File> fileList = new ArrayList<File>();
                Constant.tempPublishImages = new ArrayList<String>();

                if (TextUtils.isEmpty(content)) {//不能没有文字
                    Toast.makeText(getApplicationContext(), "请输入文字内容....", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    mProgressDialog = ProgressDialog.show(DiaryPublishActivity.this, null, "加载中，请稍后……");
                    if (Constant.publishImagePaths.size() == 0) {//有文字没有图片
                        type = 1;//仅文字
                        Log.e("test ziduan", Constant.imageId+"");
                        Diary diary = new Diary();
                        diary.setContent(content);
                        diary.setAddress(tv_location.getText().toString().trim());
                        diary.setWhether(tv_temperature.getText().toString().trim());
                        diary.setWhether_image(Constant.imageId);
                        diary.setTag(spinnerValue);
                        diary.setCreate_time(System.currentTimeMillis());
                        diary.setDate(TimeUtil.getCurrentTime());
                        diary.setDay(TimeUtil.getCurrentDay());
                        diary.setUser_message(0);
                        diaryDataBaseOperate.insertToDiary(diary);
                    } else {//有文字也有图片
                        type = 2;//图文

                        //压缩图片
                        for (int i = 0; i < Constant.publishImagePaths.size(); i++) {
                            String imageUrl = Constant.publishImagePaths.get(i);
                            String imageName_temp = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
                            String fileName = filename + String.valueOf("small_" + imageName_temp);
                            File file = new File(fileName);
                            Constant.tempPublishImages.add(fileName);
                            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                                if (!file.getParentFile().exists()) {
                                    file.getParentFile().mkdirs();
                                }
                            }
                            // 生成小图
                            PictureUtil.save(imageUrl, 200, fileName);

//                            save(imageUrl, 60, "small_" + imageName_temp);

                            fileList.add(file);
                        }
                    }

                    //点了一次发送之后就别点了啊啊啊，要不然一下子发出去好多条一样的怎么办!
                    //那如果我非要发一样的呢，你还不让我发了，凭啥，能的你
                    mSendMsgTextView.setFocusable(false);
                    mSendMsgTextView.setClickable(false);
                    Intent intent = new Intent(DiaryPublishActivity.this,  MainActivity.class);
                    startActivity(intent);
                    //setTopic(type, content, fileList, spinnerValue, "北京市", "20", 0);

                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                exitDialog();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void exitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DiaryPublishActivity.this);
        builder.setMessage("当前退出是不会保存草稿的哟～");
        builder.setTitle("你确定要退出编辑吗？");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                DiaryPublishActivity.this.finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            exitDialog();
        }
        return false;
    }


/*
    private void setTopic(int type, String content, List<File> fileList, String tag, String address, String whether, int whetherImage) {

        int maxID = Constant.diariesList.get(Constant.diariesList.size()-1).getDiary_id();
//        int maxID = Constant.diariesList.size();
        String pictures = Constant.imagePathAli;

        ArrayList<DiaryMessage> diaryMessages = new ArrayList<>();
        Diary diary = new Diary(maxID+1, content,tag, TimeUtil.getCurrentTime(),pictures, address, whether, whetherImage, diaryMessages, TimeUtil.getCurrentDay());
        Constant.diariesList.add(0, diary);

        DiaryListActivity.diaryAdapter.notifyDataSetChanged();

        //清空本地暂存的小图文件
        for (int i = 0; i < Constant.tempPublishImages.size(); i++) {
            File tempFile = new File(Constant.tempPublishImages.get(i));
            PictureUtil.deleteFile(tempFile);
        }
        Constant.tempPublishImages.clear();
        Constant.publishImagePaths.clear();

        //既然已经发出去了。那就让发送按钮可以点吧。行吧。
        mSendMsgTextView.setFocusable(true);
        mSendMsgTextView.setClickable(true);

        finish();

        mProgressDialog.dismiss();
    }
    */
}
