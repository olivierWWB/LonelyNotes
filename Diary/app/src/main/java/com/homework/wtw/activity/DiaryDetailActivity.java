package com.homework.wtw.activity;

/**
 * Created by ts on 17/3/7.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.homework.wtw.adapter.DiaryCommentAdapter;
import com.homework.wtw.database.DiaryDataBaseOperate;
import com.homework.wtw.diary.DiaryApplication;
import com.homework.wtw.diary.R;
import com.homework.wtw.listener.AnimateFirstDisplayListener;
import com.homework.wtw.model.Diary;
import com.homework.wtw.model.DiaryMessage;
import com.homework.wtw.model.Image;
import com.homework.wtw.util.Constant;
import com.homework.wtw.util.ScreenTools;
import com.homework.wtw.util.TimeUtil;
import com.homework.wtw.view.CustomImageView;
import com.homework.wtw.view.MyListView;
import com.homework.wtw.view.NineGridlayout;
import com.homework.wtw.view.ProgressWheel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;


public class DiaryDetailActivity extends BaseActivity {
    private String TAG = "DiaryDetailActivity";
    private LinearLayout container;
    private LinearLayout commentLayout;
    private ScrollView scrollView;
    private RelativeLayout relativeLayout;

    private DiaryDataBaseOperate diaryDataBaseOperate;

    private DisplayImageOptions options;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private LayoutInflater inflater;
    private ProgressWheel progressWheel;
    private SwipeRefreshLayout swipeRefreshLayout;

    private ImageView remarkImage, weatherImagev;//
    private TextView textContent, textDate, textAddress, textWeather, textRemarkNum, textTag;
    private List<Image> imagesList;
    private MyListView remarkListView;
    private NineGridlayout ivMore;
    private CustomImageView ivOne;
    private EditText commentEdit;
    private Button commentButton;
    private LinearLayout linearComment;
    private Bitmap bmp;

    private DiaryCommentAdapter commentAdapter;
    private ArrayList<String> pictureList = new ArrayList<>();

    private int commentID = 0;
    private int diaryID;
    private int fromWhere; //0-item,1-评论图标

    private String tag;
    private String content;
    private byte[] pictures = null;
    private int commentNum = 0;
    private String date,address,weather,day;
    private int weatherImage;

    private static List<DiaryMessage> diaryCommentsList = new ArrayList<>();

    private Intent intent;
    private ImageView mImageView;


    private long mLastTime;
    private long mCurTime;

    private Diary diary = new Diary();

    private ActionBar actionBar = null;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_detail);
        diaryDataBaseOperate = new DiaryDataBaseOperate(DiaryApplication.diarySQLiteOpenHelper.getWritableDatabase());

        intent = getIntent();
        diaryID = intent.getIntExtra("diaryId", -1);
        diary = diaryDataBaseOperate.findDiaryById(diaryID);
        tag = diary.getTag();
        content = diary.getContent();
        commentNum = diary.getUser_message();
        date = diary.getDate();
        day = diary.getDay();
        address = diary.getAddress();
        weather = diary.getWeather();
        weatherImage = diary.getWeather_image();
        pictures = diary.getPicture();
        if (pictures != null) {
            bmp = BitmapFactory.decodeByteArray(pictures, 0, pictures.length);
        }
        fromWhere = intent.getIntExtra("fromwhere", -1);

        diaryCommentsList = diaryDataBaseOperate.findMessageByDiaryId(diaryID);

//        container = (LinearLayout) findViewById(R.id.container);
//        initSystemBar(container);

        inflater = LayoutInflater.from(this);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                //.displayer(new CircleBitmapDisplayer(Color.WHITE, 5))
                .build();

        toolbar = initToolBar(date, day);
        mImageView = new ImageView(this);
        //.setImageBitmap(bmp);//
        initMenu(toolbar);

        //双击toolbar回到列表顶端
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLastTime = mCurTime;
                mCurTime = System.currentTimeMillis();
                if (mCurTime - mLastTime < 1000) {
                    //双击事件
                    scrollView.smoothScrollTo(0, 0);
                }
            }
        });

        progressWheel = (ProgressWheel) findViewById(R.id.progress_wheel);
        progressWheel.setVisibility(View.VISIBLE);

        relativeLayout = (RelativeLayout) findViewById(R.id.relative_layout);
        relativeLayout.setVisibility(View.GONE);

        Log.i(TAG, "diaryID= "+diaryID);
        inflater = LayoutInflater.from(this);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                //.displayer(new CircleBitmapDisplayer(Color.WHITE, 5))
                .build();

        commentLayout = (LinearLayout)findViewById(R.id.layout_comment_edit);
        textAddress = (TextView)findViewById(R.id.textView_diary_detail_address);
        textContent = (TextView)findViewById(R.id.textview_diary_detail_content);
        textDate = (TextView)findViewById(R.id.textView_diary_detail_date);
        textWeather = (TextView)findViewById(R.id.textView_diary_detail_weather);
        textTag = (TextView)findViewById(R.id.text_diary_detail_direction);
        weatherImagev = (ImageView)findViewById(R.id.imageView_diary_detail_weather);
        textRemarkNum = (TextView)findViewById(R.id.textView_num_remark);
        remarkImage = (ImageView)findViewById(R.id.imageView_remark);
        remarkListView = (MyListView)findViewById(R.id.remark_listview);
        commentEdit = (EditText)findViewById(R.id.edit_comment);
        commentButton = (Button)findViewById(R.id.comment_button);
        linearComment = (LinearLayout) findViewById(R.id.linear_topic_detail_comment);
        scrollView = (ScrollView)findViewById(R.id.scrollView);
        commentButton.setOnClickListener(onClickListener);
        linearComment.setOnClickListener(onClickListener);

        ivMore = (NineGridlayout) findViewById(R.id.iv_ngrid_layout);
        ivOne = (CustomImageView) findViewById(R.id.iv_oneimage);

        commentLayout.setVisibility(View.GONE);

        scrollView.smoothScrollTo(0, 20);

        updateUI();

//        getTopic(diaryID);//获取数据

        if(fromWhere == 1){//从列表页点了某条的小评论图标过来的哦～～～
            //直接给他弹个评论窗。
//            commentLayout.setVisibility(View.VISIBLE);
            commentEdit.setFocusable(true);
            commentEdit.setFocusableInTouchMode(true);
            commentEdit.requestFocus();
            commentEdit.requestFocusFromTouch();

            // 对于刚跳到一个新的界面就要弹出软键盘的情况,可能由于界面为加载完全而无法弹出软键盘。
            // 此时应该适当的延迟弹出软键盘（保证界面的数据加载完成）
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                               public void run() {
//                                   InputMethodManager inputManager =
//                                           (InputMethodManager) commentEdit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                                   inputManager.showSoftInput(commentEdit, 0);
                               }
                           },
                    2000);
        }

        setListViewHeight(remarkListView);

        //键盘不要挡住输入框
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        remarkListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DiaryDetailActivity.this);

                //评论可以删除
                builder.setItems(new String[]{"删除"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {//删除
                            diaryDataBaseOperate.updateMessageCount(diaryID, 1);
                            diaryDataBaseOperate.deleteMessage(diaryCommentsList.get(position).getDiary_message_id());
                            diaryCommentsList = diaryDataBaseOperate.findMessageByDiaryId(diaryID);
                            diary = diaryDataBaseOperate.findDiaryById(diaryID);
                            DiaryListActivity.diaryAdapter.setData(diaryDataBaseOperate.findAll());
                            DiaryListActivity.diaryAdapter.notifyDataSetChanged();
                            commentAdapter.setData(diaryCommentsList);
                            commentAdapter.notifyDataSetChanged();
                            textRemarkNum.setText(diary.getUser_message()+"");
                        }
                    }
                });


                builder.show();
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        //设置刷新时动画的颜色，可以设置4个
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
//                        getTopic(diaryID);

                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });
    }

    private void handlerOneImage(Image image) {
        int totalWidth;
        int imageWidth;
        int imageHeight;
        ScreenTools screentools = ScreenTools.instance(this);
        totalWidth = screentools.getScreenWidth() - screentools.dip2px(80);
        imageWidth = screentools.dip2px(image.getWidth());
        imageHeight = screentools.dip2px(image.getHeight());
        if (image.getWidth() <= image.getHeight()) {
            if (imageHeight > totalWidth) {
                imageHeight = totalWidth;
                imageWidth = (imageHeight * image.getWidth()) / image.getHeight();
            }
        } else {
            if (imageWidth > totalWidth) {
                imageWidth = totalWidth;
                imageHeight = (imageWidth * image.getHeight()) / image.getWidth();
            }
        }
        ViewGroup.LayoutParams layoutparams = ivOne.getLayoutParams();
        layoutparams.height = imageHeight;
        layoutparams.width = imageWidth;
        ivOne.setLayoutParams(layoutparams);
        ivOne.setClickable(true);
        ivOne.setScaleType(ImageView.ScaleType.FIT_XY);
//        ivOne.setImageUrl(image.getUrl());
        if (bmp != null) {
            ivOne.setImageBitmap(bmp);
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.linear_topic_detail_comment://评论
                    //                        commentLayout.setVisibility(View.VISIBLE);
                    commentEdit.setFocusable(true);
                    commentEdit.setFocusableInTouchMode(true);
                    commentEdit.requestFocus();
                    commentEdit.requestFocusFromTouch();
                    commentEdit.setHint("");

                    InputMethodManager inputManager = (InputMethodManager)commentEdit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.showSoftInput(commentEdit, 0);

//                    commentType = 0;

                    break;

                case R.id.comment_button://评论按钮
                    String comment_content = commentEdit.getText().toString();

                    if(TextUtils.isEmpty(commentEdit.getText())){
                        Toast.makeText(DiaryDetailActivity.this, "输入评论内容哟～～", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    DiaryMessage diaryMessage = new DiaryMessage();
                    diaryMessage.setContent(comment_content);
                    diaryMessage.setSource_diary_id(diaryID);
                    diaryMessage.setIs_active(1);
                    diaryMessage.setCreate_time(TimeUtil.getCurrentTime());

                    diaryDataBaseOperate.publishMessage(diaryMessage);
                    diaryDataBaseOperate.updateMessageCount(diaryID, 0);
                    diary = diaryDataBaseOperate.findDiaryById(diaryID);
                    diaryCommentsList = diaryDataBaseOperate.findMessageByDiaryId(diaryID);
                    DiaryListActivity.diaryAdapter.setData(diaryDataBaseOperate.findAll());
                    DiaryListActivity.diaryAdapter.notifyDataSetChanged();
                    //TO DO
                    commentAdapter.setData(diaryCommentsList);

                    commentAdapter.notifyDataSetChanged();
                    remarkListView.setSelection(commentAdapter.getCount() - 1);
                    setListViewHeight(remarkListView);
                    remarkImage.setImageDrawable(getResources().getDrawable(R.drawable.comment_blue));
                    textRemarkNum.setText(diary.getUser_message()+"");
                    commentEdit.setText("");
                    commentEdit.setHint("");

                    break;

                default:
                    break;
            }
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



    public void setListViewHeight(ListView listView) {

        // 获取ListView对应的Adapter
        DiaryCommentAdapter listAdapter = (DiaryCommentAdapter) listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + ((listView.getDividerHeight()) * (listAdapter.getCount()-1));
        listView.setLayoutParams(params);
    }


    private void initMenu(Toolbar toolbar) {
        mImageView.setImageResource(R.drawable.icon_more);

        mImageView.setVisibility(View.VISIBLE);

        Toolbar.LayoutParams params = new Toolbar.LayoutParams(
                150,
                50,
                Gravity.RIGHT);
        //设置外边界
        params.setMargins(3, 3, 5, 3);
        toolbar.addView(mImageView, params);

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopwindow();
            }
        });
    }


    private void showPopwindow() {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindow_diary_detail, null);

        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
        final PopupWindow window = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        window.setBackgroundDrawable(dw);


        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
        window.showAtLocation(DiaryDetailActivity.this.findViewById(R.id.linear_layout2), Gravity.BOTTOM, 0, 100);

        Button btn3 = (Button) view.findViewById(R.id.popupwindow_btn3);//
        btn3.setText("删除");
        btn3.setTextColor(Color.RED);
        btn3.setOnClickListener(new View.OnClickListener() {//删除这条话题哎呀呀
            @Override
            public void onClick(View v) {
                diaryDataBaseOperate.deleteMessageByDiary(diaryID);
                diaryDataBaseOperate.deleteDiary(diaryID);
                DiaryListActivity.diaryAdapter.setData(diaryDataBaseOperate.findAll());
                DiaryListActivity.diaryAdapter.notifyDataSetChanged();
                window.dismiss();
                finish();
            }
        });

        Button cancelBtn = (Button) view.findViewById(R.id.topic_cancel_add);
        cancelBtn.setOnClickListener(new View.OnClickListener() {//取消
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });

    }

    private void updateUI(){
        textContent.setText(content);
        textRemarkNum.setText(String.valueOf(commentNum));
        textAddress.setText(address);
        textDate.setText(date);
        textWeather.setText(weather);
        textTag.setText(tag);

        weatherImagev.setImageResource(Constant.mImageViewResourceId[weatherImage]);
        //weatherImagev.setImageBitmap(bmp);
        //发的话题的图片
        imagesList=new ArrayList<>();


        if(bmp == null){
            ivMore.setVisibility(View.GONE);
            ivOne.setVisibility(View.GONE);
        }else{
            ivMore.setVisibility(View.GONE);
            ivOne.setVisibility(View.VISIBLE);

//            pictureList = new ArrayList<>();
//            pictureList.add(imagesList.get(0).getUrl());
//
//            ivOne.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(DiaryDetailActivity.this, ShowImageActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putStringArrayList("url", pictureList);
//                    bundle.putInt("position", 0);
//                    intent.putExtras(bundle);
//                    startActivity(intent);
//                }
//            });

            Image image = new Image(bmp, bmp.getWidth(), bmp.getHeight());
            handlerOneImage(image);
        }
        /*

        if (imagesList.isEmpty()) {
            ivMore.setVisibility(View.GONE);
            ivOne.setVisibility(View.GONE);
        } else if (imagesList.size() == 1) {
            ivMore.setVisibility(View.GONE);
            ivOne.setVisibility(View.VISIBLE);

            pictureList = new ArrayList<>();
            pictureList.add(imagesList.get(0).getUrl());

            ivOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DiaryDetailActivity.this, ShowImageActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("url", pictureList);
                    bundle.putInt("position", 0);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            handlerOneImage(imagesList.get(0));
        } else {
            ivMore.setVisibility(View.VISIBLE);
            ivOne.setVisibility(View.GONE);
            ivMore.setImagesData(imagesList);
        }*/

        //评论列表
        commentAdapter = new DiaryCommentAdapter(DiaryDetailActivity.this, diaryCommentsList);
        Log.i("DiaryCommentAdapter","start Comment");
        remarkListView.setAdapter(commentAdapter);

        progressWheel.setVisibility(View.GONE);
        relativeLayout.setVisibility(View.VISIBLE);
        commentLayout.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}

