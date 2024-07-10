package com.chaychan.bottombarlayout;

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

import java.util.ArrayList;
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

    private String[] getFragmentContents() {
        return new String[]{"首页", "视频", "微头条", "我的"};
    }

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
        for (String tabContent : getFragmentContents()) {
            TabFragment fragment = new TabFragment();
            Bundle bundle = new Bundle();
            bundle.putString(TabFragment.CONTENT, tabContent);
            fragment.setArguments(bundle);
            mFragmentList.add(fragment);
        }
    }

    public void initListener() {
        mVpContent.setAdapter(new MyAdapter(this));
        mBottomBarLayout.setViewPager2(mVpContent);

        mBottomBarLayout.setUnread(0, 20);//设置第一个页签的未读数为20
        mBottomBarLayout.setUnread(1, 1001);//设置第二个页签的未读数
        mBottomBarLayout.showNotify(2);//设置第三个页签显示提示的小红点
        mBottomBarLayout.setMsg(3, "NEW");//设置第四个页签显示NEW提示文字

        mBottomBarLayout.setOnItemClickInterceptor(position -> {
            boolean isLogin = false;
            if (position == 3 && !isLogin){
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
                mBottomBarLayout.hideNotify(2);
                break;
            case R.id.action_clear_msg:
                mBottomBarLayout.hideMsg(3);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
