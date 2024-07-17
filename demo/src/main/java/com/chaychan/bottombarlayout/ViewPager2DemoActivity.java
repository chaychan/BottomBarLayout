package com.chaychan.bottombarlayout;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.chaychan.library.BottomBarLayout;
import com.chaychan.library.TabData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author ChayChan
 * @description: viewPager2 demo
 * @date 2024/07/10  14:58
 */
public class ViewPager2DemoActivity extends AppCompatActivity {

    private ViewPager2 mVpContent;
    protected BottomBarLayout mBottomBarLayout;

    private List<TabFragment> mFragmentList = new ArrayList<>();

    private String[] mTitle = new String[]{"首页", "视频",  "微头条", "我的"};

    private List<TabData> tabData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager2_demo);

        initView();
        initData();
        initListener();
    }

    public void initView() {
        mVpContent = findViewById(R.id.vp_content);
        mBottomBarLayout = findViewById(R.id.bbl);
    }

    private void initData() {
        mFragmentList.add(createFragment(mTitle[0]));
        mFragmentList.add(createFragment(mTitle[1]));
        mFragmentList.add(createFragment("中间add")); //中间凸起占位的fragment 可以是空白的 然后在拦截处理
        mFragmentList.add(createFragment(mTitle[2]));
        mFragmentList.add(createFragment(mTitle[3]));

        tabData.add(new TabData(mTitle[0], R.mipmap.tab_home_normal, R.mipmap.tab_home_selected));
        tabData.add(new TabData(mTitle[1], R.mipmap.tab_video_normal, R.mipmap.tab_video_selected));
        tabData.add(new TabData(mTitle[2], R.mipmap.tab_micro_normal, R.mipmap.tab_micro_selected));
        tabData.add(new TabData(mTitle[3], R.mipmap.tab_me_normal, R.mipmap.tab_me_selected));
        mBottomBarLayout.setData(tabData);
    }

    public TabFragment createFragment(String content){
        TabFragment fragment = new TabFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TabFragment.CONTENT, content);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void initListener() {
        mVpContent.setAdapter(new MyAdapter(this));
        mVpContent.setUserInputEnabled(false);
        mBottomBarLayout.setViewPager2(mVpContent);

        mBottomBarLayout.setUnread(0, 20);//设置第一个页签的未读数为20
        mBottomBarLayout.setUnread(1, 1001);//设置第二个页签的未读数
        mBottomBarLayout.showNotify(3);//设置第三个页签显示提示的小红点
        mBottomBarLayout.setMsg(4, "NEW");//设置第四个页签显示NEW提示文字

        mBottomBarLayout.setOnPageChangedIntercepagetor(position -> {
            if(position == 2){
                //中间凸起图标的位置
                Toast.makeText(ViewPager2DemoActivity.this, "可以跳转别的页面，比如发布页", Toast.LENGTH_SHORT).show();
                return true;
            }
            boolean isLogin = false; //Simulate no login
            if (position == 4 && !isLogin){
                //no login intercept  to other tab or to LoginActivity
                Toast.makeText(ViewPager2DemoActivity.this, "Test intercept, Login first please", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
        mBottomBarLayout.setOnItemSelectedListener((bottomBarItem, previousPosition, currentPosition) -> {
            Log.i("ViewPager2DemoActivity", "position: " + currentPosition + " pre: " + previousPosition);
        });
    }

    class MyAdapter extends FragmentStateAdapter {

        public MyAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getItemCount() {
            return mFragmentList.size();
        }
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
                mBottomBarLayout.hideNotify(3);
                break;
            case R.id.action_clear_msg:
                mBottomBarLayout.hideMsg(4);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
