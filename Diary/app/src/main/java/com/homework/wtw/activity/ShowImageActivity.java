package com.homework.wtw.activity;

/**
 * Created by ts on 17/3/7.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.homework.wtw.diary.R;
import com.homework.wtw.fragment.PictureSlideFragment;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ShowImageActivity extends AppCompatActivity {
    private String TAG = "ShowImageActivity";
    private ViewPager viewPager;
    private TextView tv_indicator;
    private ImageView saveImageView;
    private ImageView backImageView;
    private ArrayList<String> urlList;
    private int position = 0;
    private int currentPosition = 0;


    private Bitmap mBitmap;
    private ProgressDialog mSaveDialog = null;
    private String mFileName;
    private String mSaveMessage;
    private final static String ALBUM_PATH = Environment.getExternalStorageDirectory() + "/wtw/download/";

    private GestureDetector gd; // 手势

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pic_viewer);

        urlList = new ArrayList<>();

//        urlList = getIntent().getStringArrayListExtra("url");
        position = getIntent().getIntExtra("position", 0);
        currentPosition = position;

        backImageView = (ImageView) findViewById(R.id.topic_showimage_back);
        backImageView.setOnClickListener(new View.OnClickListener() {//返回按钮
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveImageView = (ImageView) findViewById(R.id.topic_showimage_save);
        saveImageView.setOnClickListener(new View.OnClickListener() {//保存图片
            @Override
            public void onClick(View v) {
//                Log.i(TAG,"url-->"+urlList.get(currentPosition));
                mBitmap = ImageLoader.getInstance().loadImageSync(urlList.get(currentPosition));

                mSaveDialog = ProgressDialog.show(ShowImageActivity.this, "保存图片", "图片正在保存中，请稍等...");
                String name = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
                mFileName = name + ".jpg";
                new Thread(saveFileRunnable).start();
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tv_indicator = (TextView) findViewById(R.id.tv_indicator);

        viewPager.setAdapter(new PictureSlidePagerAdapter(getSupportFragmentManager()));

        viewPager.setCurrentItem(position);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tv_indicator.setText(String.valueOf(position+1)+"/"+urlList.size());
            }

            @Override
            public void onPageSelected(int position) {
                Log.i(TAG, "select->" + position);
                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//        gd = new GestureDetector(this);

//        viewPager.setOnTouchListener(this);

        Context mContext = this.getApplicationContext();
    }






    private  class PictureSlidePagerAdapter extends FragmentStatePagerAdapter {

        public PictureSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PictureSlideFragment.newInstance(urlList.get(position));
        }

        @Override
        public int getCount() {
            return urlList.size();
        }
    }


    /**
     * 保存文件
     * @param bm
     * @param fileName
     * @throws IOException
     */
    public void saveFile(Bitmap bm, String fileName) throws IOException {
        File dirFile = new File(ALBUM_PATH);
        if(!dirFile.exists()){
            try{
                dirFile.mkdirs();
            }catch (Exception e){
                Log.i(TAG, "dirFile" + e.toString());
            }
        }
        File myCaptureFile = new File(ALBUM_PATH + fileName);
        if(!myCaptureFile.exists()){
            try{
                myCaptureFile.createNewFile();
            }catch (Exception e){
                Log.i(TAG,"myCaptureFile"+e.toString());
            }

        }


//        FileOutputStream fOut = new FileOutputStream(myCaptureFile);
//        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
//        fOut.flush();
//        fOut.close();


        //压缩一下再保存。因为imageLoader那整下来的bitmap直接存jpeg大好多。。。。。
        int size = 60;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        FileOutputStream fos = new FileOutputStream(myCaptureFile);

        int options = 100;
        // 如果大于80kb则再次压缩,最多压缩三次
        while (baos.toByteArray().length / 1024 > size && options > 10) {
            // 清空baos
            baos.reset();
            // 这里压缩options%，把压缩后的数据存放到baos中
            bm.compress(Bitmap.CompressFormat.JPEG, options, baos);
            options -= 30;
        }

        fos.write(baos.toByteArray());
        fos.close();
        baos.close();


        //这个广播的目的就是更新图库，发了这个广播进入相册就可以找到你保存的图片啦～～～～！！！！
//		Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//		Uri saveUri = Uri.fromFile(myCaptureFile);
//		intent.setData(saveUri);
//		this.sendBroadcast(intent);
    }

    private Runnable saveFileRunnable = new Runnable(){
        @Override
        public void run() {
            try {
                saveFile(mBitmap, mFileName);
                mSaveMessage = mFileName + "图片保存成功！";
            } catch (IOException e) {
                mSaveMessage = "图片保存失败！";
                e.printStackTrace();
            }
            messageHandler.sendMessage(messageHandler.obtainMessage());
        }
    };

    private Handler messageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mSaveDialog.dismiss();

            Log.d(TAG, mSaveMessage);
            Toast.makeText(ShowImageActivity.this, mSaveMessage, Toast.LENGTH_SHORT).show();
        }
    };



    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    public static void finishActivity(){
    }




}

