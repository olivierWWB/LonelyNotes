package com.homework.wtw.activity;

/**
 * Created by ts on 17/3/7.
 */

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import com.homework.wtw.adapter.DiaryAdapter;
import com.homework.wtw.database.DiaryDataBaseOperate;
import com.homework.wtw.diary.DiaryApplication;
import com.homework.wtw.diary.R;

import com.homework.wtw.model.Diary;
import com.homework.wtw.model.DiaryMessage;
import com.homework.wtw.util.Constant;
import com.homework.wtw.view.ProgressWheel;
import com.squareup.leakcanary.RefWatcher;


/**
 * Created by ts on 16/5/18.
 */
public class DiaryListActivity extends BaseActivity implements AbsListView.OnScrollListener{

    private String TAG = "DiaryListActivity";
    private DiaryDataBaseOperate diaryDataBaseOperate;

    public ProgressWheel progressWheel;
    private SwipeRefreshLayout swipeRefreshLayout;

    private ListView mListView;
    public static DiaryAdapter diaryAdapter;

    private LinearLayout container;

    private View loadMoreView; //加载更多页面

    private int lastItemIndex;
    public int dataCount, count;
    private int load_num = 10;
    public int jsonCount;

    private ProgressBar progressBar;
    private TextView textBar;
    private TextView textview_tipc;

    private Toolbar toolbar;
    private boolean isRefresh = false;

    private int indexItem = 0;

    private long mLastTime;
    private long mCurTime;

    private ImageView mImageView, mImageView2;
    private TextView mAddTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        diaryDataBaseOperate = new DiaryDataBaseOperate(DiaryApplication.diarySQLiteOpenHelper.getWritableDatabase());

//        container = (LinearLayout) findViewById(R.id.container);
//        initSystemBar(container);
//
//        toolbar = initMainToolBar();
        toolbar = initMainToolBar2("");
        mImageView = new ImageView(this);//右上角！！！！！！！
        mImageView.setVisibility(View.VISIBLE);

        mImageView2 = new ImageView(this);//左上角！！！！！！！
        mImageView2.setVisibility(View.VISIBLE);
        initMenu(toolbar);

        //双击toolbar回到列表顶端
// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLastTime = mCurTime;
                mCurTime = System.currentTimeMillis();
                if(mCurTime - mLastTime < 1000){
                    mListView.smoothScrollToPosition(0);
                }
            }
        });
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!



        progressWheel = (ProgressWheel) findViewById(R.id.progress_wheel);
        textview_tipc = (TextView) findViewById(R.id.textview_tipc);

        mListView = (ListView) findViewById(R.id.listview_topics);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == diaryAdapter.getCount()) {
                    Log.i("ResultTopic", "position=" + i);
                } else {
                    Intent intent = new Intent(DiaryListActivity.this, DiaryDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("fromwhere", 0);//从列表页点item跳过去的
                    bundle.putInt("diaryId", Constant.diariesList.get(i).getDiary_id());
                    bundle.putString("tag", Constant.diariesList.get(i).getTag());
                    bundle.putString("content", Constant.diariesList.get(i).getContent());
                    bundle.putString("pictures", Constant.diariesList.get(i).getPicture());//图片
                    bundle.putInt("commentNum", Constant.diariesList.get(i).getDiaryMessagesList().size());
                    bundle.putString("date", Constant.diariesList.get(i).getDate());
                    bundle.putString("day", Constant.diariesList.get(i).getDay());
                    bundle.putString("address", Constant.diariesList.get(i).getAddress());
                    bundle.putString("whether", Constant.diariesList.get(i).getWhether());
                    bundle.putParcelableArrayList("commentList", Constant.diariesList.get(i).getDiaryMessagesList());

                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        getDiaries();
//        diaryAdapter = new DiaryAdapter(DiaryListActivity.this, Constant.diariesList);
//        mListView.setAdapter(diaryAdapter);

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
                        Constant.diariesList = new ArrayList<>();
                        indexItem = 0;
                        jsonCount = 0;
                        dataCount = load_num;
                        isRefresh = true;

//                        getDiaries();
                        swipeRefreshLayout.setRefreshing(false);

//                        mListView.removeFooterView(loadMoreView);
                    }
                }, 3000);
            }
        });

        jsonCount = 0;
        dataCount = load_num;

//        Constant.diariesList = new ArrayList<>();


        //上拉加载更多
//        loadMoreView = this.getLayoutInflater().inflate(R.layout.item_pull_loadmore, null);
//        progressBar = (ProgressBar) loadMoreView.findViewById(R.id.progressBar2);
//        textBar = (TextView) loadMoreView.findViewById(R.id.loadmore_text);
//
//        textBar.setText("上拉加载更多");
//        progressBar.setVisibility(View.GONE);

//        mListView.addFooterView(loadMoreView);//一行加载更多的ProcessBar提示View
//        mListView.setFooterDividersEnabled(false);

        mListView.setOnScrollListener(this);

    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = DiaryApplication.getRefWatcher(this);
        refWatcher.watch(this);
    }

    private void loadMoreData() { //加载更多数据
//        if(flag == 0){
//            getTopics(load_num, dataCount / load_num + 1);
//        }else if(flag == 1){
//            getTopics2(load_num, dataCount / load_num + 1);
//        }
//        getDiaries();

        Log.i(TAG, "getTopics(" + load_num + "," + (dataCount / load_num + 1) + ")");

        dataCount += load_num;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //获取最后一项的行数
        lastItemIndex = firstVisibleItem + visibleItemCount - 1;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //判断下拉停止且满足下拉条件时，显示加在更多的view并更新数据
        Log.i(TAG, "lastItemIndex: " + lastItemIndex + "/dataCount: " + dataCount + "/jsonCount: " + jsonCount);

        if (lastItemIndex == jsonCount && jsonCount <= count && scrollState == SCROLL_STATE_IDLE) {
            loadMoreView.setVisibility(view.VISIBLE);
            textBar.setText("数据加载中...");
            progressBar.setVisibility(View.VISIBLE);

            mHandler.sendEmptyMessage(0);
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                loadMoreData();

                if (lastItemIndex == count) {
//                    Toast.makeText(getActivity(), "没有更多啦～", Toast.LENGTH_LONG).show();
                    loadMoreView.setVisibility(View.GONE);
                }

            }
        }
    };


    public void getDiaries(){
//        for(int i=0; i<5; i++){//5条日记数据
//            ArrayList<DiaryMessage> diaryMessages = new ArrayList<>();
//            for (int j=0; j<2; j++){//两条评论
//                DiaryMessage diaryMessage = new DiaryMessage(j+1,"2017-03-06 23:23:23","messageContent", 1);
//                diaryMessages.add(diaryMessage);
//            }
//
//            Diary diary = new Diary();
//            Constant.diariesList.add(diary);
//        }

//        diaryAdapter = null;
        Constant.diariesList = diaryDataBaseOperate.findAll();
        diaryAdapter = new DiaryAdapter(DiaryListActivity.this, Constant.diariesList);
        mListView.setAdapter(diaryAdapter);

        progressWheel.setVisibility(View.GONE);
        mListView.setVisibility(View.VISIBLE);

    }

    private void initMenu(Toolbar toolbar) {
        mImageView.setImageResource(R.drawable.icon_add);
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
                Intent intent = new Intent(DiaryListActivity.this, DiaryPublishActivity.class);
                startActivity(intent);
            }
        });

        mImageView2.setImageResource(R.drawable.ic_setting);
        mImageView2.setVisibility(View.VISIBLE);

        Toolbar.LayoutParams params2 = new Toolbar.LayoutParams(
                80,
                60,
                Gravity.LEFT);
        //设置外边界
        params.setMargins(3, 3, 5, 3);
        toolbar.addView(mImageView2, params2);

        mImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DiaryListActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

    }
}

