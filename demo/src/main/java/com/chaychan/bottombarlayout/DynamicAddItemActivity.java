package com.chaychan.bottombarlayout;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.chaychan.library.BottomBarItem;
import com.chaychan.library.BottomBarLayout;
import com.chaychan.library.UIUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

/**
 * @author ChayChan
 * @description: 动态添加条目
 * @date 2018/12/13  14:45
 */
public class DynamicAddItemActivity extends AppCompatActivity {

    private List<TabFragment> mFragmentList = new ArrayList<>();
    private BottomBarLayout mBottomBarLayout;

    private int[] mNormalIconIds = new int[]{
            R.mipmap.tab_home_normal, R.mipmap.tab_video_normal,
            R.mipmap.tab_micro_normal, R.mipmap.tab_me_normal
    };

    private int[] mSelectedIconIds = new int[]{
            R.mipmap.tab_home_selected, R.mipmap.tab_video_selected,
            R.mipmap.tab_micro_selected, R.mipmap.tab_me_selected
    };

    private int[] mTitleIds = new int[]{
            R.string.tab_home,
            R.string.tab_video,
            R.string.tab_micro,
            R.string.tab_me
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_add_item);

        initView();
        initData();
        initListener();
    }

    private void initView() {
        getSupportActionBar().setTitle(DynamicAddItemActivity.class.getSimpleName());
        mBottomBarLayout = findViewById(R.id.bbl);
    }

    private void initData() {
        for (int i = 0; i < mTitleIds.length; i++) {
            //创建item
            BottomBarItem item = createBottomBarItem(i);
            mBottomBarLayout.addItem(item);

            TabFragment homeFragment = createFragment(mTitleIds[i]);
            mFragmentList.add(homeFragment);
        }
        mBottomBarLayout.setCurrentItem(0);
    }

    @NonNull
    private TabFragment createFragment(int titleId) {
        TabFragment homeFragment = new TabFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TabFragment.CONTENT, getString(titleId));
        homeFragment.setArguments(bundle);
        return homeFragment;
    }

    private BottomBarItem createBottomBarItem(int i) {
        BottomBarItem item = new BottomBarItem.Builder(this)
                .titleTextBold(true)
                .titleTextSize(UIUtils.dip2Px(this, 8))
                .titleNormalColor(getResources().getColor(R.color.tab_normal_color))
                .titleSelectedColor(getResources().getColor(R.color.tab_selected_color))
                .marginTop(UIUtils.dip2Px(this, -5))
                //              .itemPadding(5)
                //              .unreadNumThreshold(99)
                //              .unreadTextColor(getResources().getColor(R.color.white))

                //还有很多属性，详情请查看Builder里面的方法
                //There are still many properties, please see the methods in the Builder for details.
                .create(mNormalIconIds[i], mSelectedIconIds[i], getString(mTitleIds[i]));
        return item;
    }

    private void initListener() {
        mBottomBarLayout.setOnItemSelectedListener((bottomBarItem, previousPosition, currentPosition) -> {
            Log.i("MainActivity", "position: " + currentPosition);

            changeFragment(currentPosition);
        });
    }

    private void changeFragment(int currentPosition) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_content, mFragmentList.get(currentPosition));
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dynamic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_add_item:
                int random = new Random().nextInt(3);
                //addFragment
                mFragmentList.add(createFragment(mTitleIds[random]));
                //addItem
                BottomBarItem bottomBarItem = createBottomBarItem(random);
                mBottomBarLayout.addItem(bottomBarItem);

                mBottomBarLayout.setCurrentItem(mFragmentList.size() - 1);
                break;
            case R.id.action_remove_item:
                //移除条目
                mBottomBarLayout.removeItem(0);

                if (mFragmentList.size() != 0) {
                    mFragmentList.remove(0);

                    if (mFragmentList.size() != 0) {
                        mBottomBarLayout.setCurrentItem(0);
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
