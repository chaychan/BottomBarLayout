package com.chaychan.bottombarlayout;

/**
 * @author ChayChan
 * @description: viewPager demo
 * @date 2020/11/21  15:18
 */
public class ViewPagerDemoActivity extends BaseViewPagerActivity{

    @Override
    protected String[] getFragmentContents() {
        return new String[]{"首页", "视频", "微头条", "我的"};
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_view_pager_demo;
    }

    @Override
    public void initView() {
        super.initView();
        getSupportActionBar().setTitle(ViewPagerDemoActivity.class.getSimpleName());
    }
}
