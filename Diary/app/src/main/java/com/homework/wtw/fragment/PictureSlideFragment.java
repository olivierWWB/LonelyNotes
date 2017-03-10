package com.homework.wtw.fragment;

/**
 * Created by ts on 17/3/7.
 */

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.homework.wtw.diary.R;
import com.homework.wtw.listener.AnimateFirstDisplayListener;
import com.homework.wtw.view.ProgressWheel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;



/**
 * Created by ts on 2016/5/17.
 */
public class PictureSlideFragment extends Fragment {
    private String TAG = "PictureSlideFragment";
    private String url;
    //    private PhotoViewAttacher mAttacher;
    private ImageView imageView;
    public ProgressWheel progressWheel;

    private DisplayImageOptions options;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private LayoutInflater inflater;

    private Bitmap mBitmap;
    private ProgressDialog mSaveDialog = null;
    private String mFileName;
    private String mSaveMessage;
    private final static String ALBUM_PATH = Environment.getExternalStorageDirectory() + "/wtw/download/";


    public static PictureSlideFragment newInstance(String url) {
        PictureSlideFragment f = new PictureSlideFragment();

        Bundle args = new Bundle();
        args.putString("url", url);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getArguments() != null ? getArguments().getString("url") : "null";

        inflater = LayoutInflater.from(getActivity());
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ali)
                .showImageForEmptyUri(R.drawable.ali)
                .showImageOnFail(R.drawable.ali)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                        //.displayer(new CircleBitmapDisplayer(Color.WHITE, 5))
                .build();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_picture_slide,container,false);

        imageView= (ImageView) v.findViewById(R.id.iv_main_pic);
//        mAttacher = new PhotoViewAttacher(imageView);

        progressWheel = (ProgressWheel) v.findViewById(R.id.progress_wheel);
        progressWheel.setBarColor(getActivity().getResources().getColor(R.color.text_gray));
        progressWheel.setVisibility(View.VISIBLE);

        Log.i(TAG, "imageURL: " + url);

        Glide.with(getActivity()).load(url).crossFade().into(new GlideDrawableImageViewTarget(imageView) {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                super.onResourceReady(resource, animation);
//                mAttacher.update();
                progressWheel.setVisibility(View.GONE);
            }
        });


        return v;
    }


    private android.app.AlertDialog mDialog = null;
    private void showSaveImgDialog(final String imageUrl) {
        this.mDialog = new AlertDialog.Builder(getActivity())
                .setSingleChoiceItems(R.array.img_save, 1,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                mBitmap = ImageLoader.getInstance().loadImageSync(imageUrl);

                                mSaveDialog = ProgressDialog.show(getActivity(), "保存图片", "图片正在保存中，请稍等...");
                                String name = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
                                mFileName = name + ".jpg";
                                new Thread(saveFileRunnable).start();
                                dialog.dismiss();
                            }
                        }).create();
        this.mDialog.show();
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
            Toast.makeText(getActivity(), mSaveMessage, Toast.LENGTH_SHORT).show();
        }
    };

}
