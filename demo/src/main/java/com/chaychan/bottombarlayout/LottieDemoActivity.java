package com.chaychan.bottombarlayout;

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
    protected int getLayoutResId() {
        return R.layout.activity_lottie_demo;
    }

    @Override
    public void initView() {
        super.initView();
        getSupportActionBar().setTitle(LottieDemoActivity.class.getSimpleName());
    }
}
