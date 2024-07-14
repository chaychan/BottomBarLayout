package com.chaychan.bottombarlayout;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.chaychan.library.BottomBarLayout;
import com.chaychan.library.TabData;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

public abstract class BaseViewPagerActivity extends AppCompatActivity {

    private ViewPager mVpContent;
    protected BottomBarLayout mBottomBarLayout;

    private List<TabFragment> mFragmentList = new ArrayList<>();

    protected abstract String[] getFragmentContents();

    protected abstract List<TabData> getTabData();

    protected abstract int getLayoutResId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        initView();
        initData();
        initListener();
    }

    public void initView() {
        mVpContent = findViewById(R.id.vp_content);
        mBottomBarLayout = findViewById(R.id.bbl);
    }

    private void initData() {
        for (String tabContent : getFragmentContents()) {
            TabFragment fragment = new TabFragment();
            Bundle bundle = new Bundle();
            bundle.putString(TabFragment.CONTENT, tabContent);
            fragment.setArguments(bundle);
            mFragmentList.add(fragment);
        }
        mBottomBarLayout.setData(getTabData());
    }

    public void initListener() {
        mVpContent.setAdapter(new MyAdapter(getSupportFragmentManager()));
        mBottomBarLayout.setViewPager(mVpContent);

        mBottomBarLayout.setUnread(0, 20);//设置第一个页签的未读数为20
        mBottomBarLayout.setUnread(1, 1001);//设置第二个页签的未读数
        mBottomBarLayout.showNotify(2);//设置第三个页签显示提示的小红点
        mBottomBarLayout.setMsg(3, "NEW");//设置第四个页签显示NEW提示文字
    }

    class MyAdapter extends FragmentStatePagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
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
                mBottomBarLayout.hideNotify(2);
                break;
            case R.id.action_clear_msg:
                mBottomBarLayout.hideMsg(3);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
