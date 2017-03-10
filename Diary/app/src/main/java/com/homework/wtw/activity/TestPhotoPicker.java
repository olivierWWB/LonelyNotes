package com.homework.wtw.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.homework.wtw.diary.R;
import com.homework.wtw.util.PictureUtil;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import me.iwf.photopicker.PhotoPicker;

/**
 * Created by ts on 2017/3/10.
 */

public class TestPhotoPicker extends BaseActivity{
    Button button;
    ImageView[] mThumbArray = new ImageView[6];
    ImageView addPicture;
    private int mImageLimit = 5;//设置照片数组的长度
    String mImgStrArray[] = {"", "", "", "", "", ""};
    private ArrayList<String> imageList;
    int[] mImageViewResourceId = {R.id.iv_one, R.id.iv_two, R.id.iv_three, R.id.iv_four, R.id.iv_five, R.id.iv_six};


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitiy_test);
//        mToolBar = initToolBar("问题详情");
        button = (Button) findViewById(R.id.test_btn2);
//        for (int i = 0; i < 6; i++) {
//            mThumbArray[i] = (ImageView) findViewById(mImageViewResourceId[i]);
//            mThumbArray[i].setOnClickListener(this);
//        }
//        initView();
//        initMenu(mToolBar);

        imageList = new ArrayList<>();


        addPicture = (ImageView) findViewById(R.id.add_picture);
//        registerForContextMenu(addPicture);
        addPicture.setOnCreateContextMenuListener(this);
        addPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sdStatus = Environment.getExternalStorageState();
                if (sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                    v.showContextMenu();
                }
            }
        });

//        release = (Button) findViewById(R.id.bt_release);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, 1, 0, "打开相机");
        menu.add(0, 2, 0, "打开相册");
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId() == 1) {
            //请求相册和相机

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // 指定存储照片的路径
            Uri imageUri = Uri.fromFile(PictureUtil.getTempImage());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            if(hasPermissionInManifest(this, Manifest.permission.CAMERA)){
                startActivityForResult(intent, 102);
            }
//            startActivityForResult(intent, 102);

        } else if (item.getItemId() == 2) {
            PhotoPicker.builder()
                    .setPhotoCount(6)//设置可多选择几张图片
                    .setShowCamera(false)
                    .setShowGif(true)
                    .setPreviewEnabled(false)
                    .setSelected(imageList)
                    .start(TestPhotoPicker.this, 100);
        }
        return super.onContextItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //完成照相后回调用此方法
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                switch (resultCode) {
                    case Activity.RESULT_OK: {//照相完成点击确定
                        if (data != null) {
                            imageList.clear();
                            ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                            imageList.addAll(photos);
                            //这是你选择所有图片的路径集合
                            for (int i = 0; i < imageList.size(); i++) {
                                Bitmap bitmap = PictureUtil.decodeSampledBitmapFromResource(photos.get(i), 400, 400);
                                mThumbArray[i].setVisibility(View.VISIBLE);
                                mThumbArray[i].setImageBitmap(bitmap);
                                //添加一张照片了
                                if (bitmap != null) {
                                    ByteArrayOutputStream baof = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 60, baof);
                                    // 将图片流以字符串形式存储下来
                                    mImgStrArray[i] = Base64.encodeToString(baof.toByteArray(), Base64.DEFAULT);
                                }
                                if (i >= mImageLimit) {
                                    addPicture.setVisibility(View.GONE);
                                } else {
                                    addPicture.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }
                    break;

                }
                break;
            case 102:
                if (resultCode == RESULT_OK) {
                    imageList.add(PictureUtil.getTempImage().getPath());
                    mThumbArray[imageList.size() - 1].setVisibility(View.VISIBLE);
                    Bitmap bitmap = PictureUtil.decodeSampledBitmapFromResource(PictureUtil.getTempImage().getPath(), 400, 400);
                    if (bitmap != null) {
                        ByteArrayOutputStream baof = new ByteArrayOutputStream();
                        mThumbArray[imageList.size() - 1].setImageBitmap(bitmap);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 60, baof);
                        // 将图片流以字符串形式存储下来
                        mImgStrArray[imageList.size() - 1] = Base64.encodeToString(baof.toByteArray(), Base64.DEFAULT);
                    }
                    if (imageList.size() - 1 >= mImageLimit) {
                        addPicture.setVisibility(View.GONE);
                    } else {
                        addPicture.setVisibility(View.VISIBLE);
                    }
                }
                break;
        }
    }

    public boolean hasPermissionInManifest(Context context, String permissionName) {
        final String packageName = context.getPackageName();
        try {
            final PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
            final String[] declaredPermisisons = packageInfo.requestedPermissions;
            if (declaredPermisisons != null && declaredPermisisons.length > 0) {
                for (String p : declaredPermisisons) {
                    if (p.equals(permissionName)) {
                        return true;
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        return false;
    }

}
