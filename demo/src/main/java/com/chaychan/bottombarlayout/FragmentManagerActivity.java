package com.chaychan.bottombarlayout;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.chaychan.library.BottomBarItem;
import com.chaychan.library.BottomBarLayout;
import com.chaychan.library.TabData;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class FragmentManagerActivity extends AppCompatActivity {

    private List<TabFragment> mFragmentList = new ArrayList<>();
    private BottomBarLayout mBottomBarLayout;

    private List<TabData> getTabData() {
        List<TabData>  tabData = new ArrayList<>();
        tabData.add(new TabData("首页", R.mipmap.tab_home_normal, R.mipmap.tab_home_selected));
        tabData.add(new TabData("视频", R.mipmap.tab_video_normal, R.mipmap.tab_video_selected));
        tabData.add(new TabData("微头条", R.mipmap.tab_micro_normal, R.mipmap.tab_micro_selected));
        tabData.add(new TabData("我的", R.mipmap.tab_me_normal, R.mipmap.tab_me_selected));
        return tabData;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_manager);

        initView();
        initData();
        initListener();
    }

    private void initView() {
        getSupportActionBar().setTitle(FragmentManagerActivity.class.getSimpleName());
        mBottomBarLayout = findViewById(R.id.bbl);
    }

    private void initData() {

        TabFragment homeFragment = new TabFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString(TabFragment.CONTENT, "首页");
        homeFragment.setArguments(bundle1);
        mFragmentList.add(homeFragment);

        TabFragment videoFragment = new TabFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString(TabFragment.CONTENT, "视频");
        videoFragment.setArguments(bundle2);
        mFragmentList.add(videoFragment);

        TabFragment microFragment = new TabFragment();
        Bundle bundle3 = new Bundle();
        bundle3.putString(TabFragment.CONTENT, "微头条");
        microFragment.setArguments(bundle3);
        mFragmentList.add(microFragment);

        TabFragment meFragment = new TabFragment();
        Bundle bundle4 = new Bundle();
        bundle4.putString(TabFragment.CONTENT, "我的");
        meFragment.setArguments(bundle4);
        mFragmentList.add(meFragment);

        mBottomBarLayout.setData(getTabData());

        changeFragment(0); //默认显示第一页
    }

    private void initListener() {
        mBottomBarLayout.setOnItemSelectedListener((bottomBarItem, previousPosition, currentPosition) -> {
            Log.i("MainActivity", "position: " + currentPosition);
            changeFragment(currentPosition);
        });

        mBottomBarLayout.setUnread(0, 20);//设置第一个页签的未读数为20
        mBottomBarLayout.setUnread(1, 1001);//设置第二个页签的未读数
        mBottomBarLayout.showNotify(2);//设置第三个页签显示提示的小红点
        mBottomBarLayout.setMsg(3, "NEW");//设置第四个页签显示NEW提示文字
    }

    private void changeFragment(int currentPosition) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_content, mFragmentList.get(currentPosition));
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_demo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_clear_unread:
                mBottomBarLayout.setUnread(0, 0);
                mBottomBarLayout.setUnread(1, 0);
                break;
            case R.id.action_clear_notify:
                mBottomBarLayout.hideNotify(2);
                break;
            case R.id.action_clear_msg:
                mBottomBarLayout.hideMsg(3);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
