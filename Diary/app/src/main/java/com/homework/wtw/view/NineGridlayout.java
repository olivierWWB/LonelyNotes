package com.homework.wtw.view;

/**
 * Created by ts on 17/3/7.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.homework.wtw.activity.ShowImageActivity;
import com.homework.wtw.model.Image;
import com.homework.wtw.util.ImageShowManager;
import com.homework.wtw.util.ScreenTools;

import java.util.ArrayList;
import java.util.List;


public class NineGridlayout extends ViewGroup {

    /**
     * 图片之间的间隔
     */
    private int gap = 5;
    private int columns;//
    private int rows;//
    private List listData;
    private int totalWidth;
    private Context context;
    private ImageShowManager imageManager;

    ArrayList<String> pictureList = new ArrayList<>();

    public NineGridlayout(Context context) {
        super(context);
        this.context = context;
    }

    public NineGridlayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        ScreenTools screenTools= ScreenTools.instance(getContext());
        totalWidth=screenTools.getScreenWidth()-screenTools.dip2px(80);
    }

    public void setImageManager(ImageShowManager imageManager){
        this.imageManager = imageManager;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
    private void layoutChildrenView(){
        int childrenCount = listData.size();

        int singleWidth = (totalWidth - gap * (3 - 1)) / 3;
        int singleHeight = singleWidth;

        //根据子view数量确定高度
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = singleHeight * rows + gap * (rows - 1);
        setLayoutParams(params);

        for (int i = 0; i < childrenCount; i++) {
            CustomImageView childrenView = (CustomImageView) getChildAt(i);
            childrenView.setImageManager(imageManager);

            childrenView.setImageUrl(((Image) listData.get(i)).getUrl());
            int[] position = findPosition(i);
            int left = (singleWidth + gap) * position[1];
            int top = (singleHeight + gap) * position[0];
            int right = left + singleWidth;
            int bottom = top + singleHeight;

            childrenView.layout(left, top, right, bottom);

            final String url = ((Image) listData.get(i)).getUrl();
            final int id = i;
            childrenView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,ShowImageActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("url", pictureList);
                    bundle.putInt("position",id);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }

    }

    private int[] findPosition(int childNum) {
        int[] position = new int[2];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if ((i * columns + j) == childNum) {
                    position[0] = i;//行
                    position[1] = j;//列
                    break;
                }
            }
        }
        return position;
    }

    public int getGap() {
        return gap;
    }

    public void setGap(int gap) {
        this.gap = gap;
    }


    public void setImagesData(List<Image> lists) {
        if (lists == null || lists.isEmpty()) {
            return;
        }

        pictureList = new ArrayList<>();
        for(int i=0; i<lists.size(); i++){
            pictureList.add(lists.get(i).getUrl());
        }

        //初始化布局
        generateChildrenLayout(lists.size());
        int flag = 0;
        //这里做一个重用view的处理
        if (listData == null) {
            int i = 0;
            while (i < lists.size()) {
                CustomImageView iv = generateImageView(flag);
                iv.setImageManager(imageManager);
                addView(iv, generateDefaultLayoutParams());
                i++;
                flag++;
            }
        } else {
            int oldViewCount = listData.size();
            int newViewCount = lists.size();
            if (oldViewCount > newViewCount) {
                removeViews(newViewCount - 1, oldViewCount - newViewCount);
            } else if (oldViewCount < newViewCount) {
                for (int i = 0; i < newViewCount - oldViewCount; i++) {
                    CustomImageView iv = generateImageView(flag);
                    iv.setImageManager(imageManager);
                    addView(iv, generateDefaultLayoutParams());
                }
            }
        }
        listData = lists;

        layoutChildrenView();
    }


    /**
     * 根据图片个数确定行列数量
     * 对应关系如下
     * num	row	column
     * 1	   1	1
     * 2	   1	2
     * 3	   1	3
     * 4	   2	2
     * 5	   2	3
     * 6	   2	3
     * 7	   3	3
     * 8	   3	3
     * 9	   3	3
     *
     * @param length
     */
    private void generateChildrenLayout(int length) {
        if (length <= 3) {
            rows = 1;
            columns = length;
        } else if (length <= 6) {
            rows = 2;
            columns = 3;
            if (length == 4) {
                columns = 2;
            }
        } else {
            rows = 3;
            columns = 3;
        }
    }

    private CustomImageView generateImageView(int i) {
        CustomImageView iv = new CustomImageView(getContext());
        iv.setImageManager(imageManager);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        final int q = i;
        iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.i("LOG","HERE");
//                Log.i("LOG", String.valueOf(q));
            }
        });
        iv.setBackgroundColor(Color.parseColor("#f5f5f5"));
        return iv;
    }


}
