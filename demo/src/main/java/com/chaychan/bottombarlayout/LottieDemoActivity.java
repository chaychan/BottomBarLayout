package com.chaychan.bottombarlayout;

import com.chaychan.library.TabData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ChayChan
 * @description: viewPager demo
 * @date 2020/11/21  15:18
 */
public class LottieDemoActivity extends BaseViewPagerActivity{

    @Override
    protected String[] getFragmentContents() {
        return new String[]{"首页", "分类", "购物车", "我的"};
    }

    @Override
    protected List<TabData> getTabData() {
        List<TabData>  tabData = new ArrayList<>();
        tabData.add(new TabData(getFragmentContents()[0], "home.json"));
        tabData.add(new TabData(getFragmentContents()[1], "category.json"));
        tabData.add(new TabData(getFragmentContents()[2], "cart.json"));
        tabData.add(new TabData(getFragmentContents()[3],  "mine.json"));
        return tabData;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_lottie_demo;
    }

    @Override
    public void initView() {
        super.initView();
        getSupportActionBar().setTitle(LottieDemoActivity.class.getSimpleName());
    }
}
