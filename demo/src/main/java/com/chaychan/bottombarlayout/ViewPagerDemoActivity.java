package com.chaychan.bottombarlayout;

import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.chaychan.library.BottomBarItem;
import com.chaychan.library.BottomBarLayout;

/**
 * @author ChayChan
 * @description: viewPager demo
 * @date 2020/11/21  15:18
 */
public class ViewPagerDemoActivity extends BaseViewPagerActivity{

    private RotateAnimation mRotateAnimation;
    private Handler mHandler = new Handler();

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

    @Override
    public void initListener() {
        super.initListener();
        mBottomBarLayout.setOnItemSelectedListener((bottomBarItem, previousPosition, currentPosition) -> {
            Log.i("ViewPagerDemoActivity", "position: " + currentPosition);
            if (currentPosition == 0) {
                //如果是第一个，即首页
                if (previousPosition == currentPosition) {
                    //如果是在原来位置上点击,更换首页图标并播放旋转动画
                    if (mRotateAnimation != null && !mRotateAnimation.hasEnded()){
                        //如果当前动画正在执行
                        return;
                    }

                    bottomBarItem.setSelectedIcon(R.mipmap.tab_loading);//更换成加载图标

                    //播放旋转动画
                    if (mRotateAnimation == null) {
                        mRotateAnimation = new RotateAnimation(0, 360,
                                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                                0.5f);
                        mRotateAnimation.setDuration(800);
                        mRotateAnimation.setRepeatCount(-1);
                    }
                    ImageView bottomImageView = bottomBarItem.getImageView();
                    bottomImageView.setAnimation(mRotateAnimation);
                    bottomImageView.startAnimation(mRotateAnimation);//播放旋转动画

                    //模拟数据刷新完毕
                    mHandler.postDelayed(() -> {
                        boolean tabNotChanged = mBottomBarLayout.getCurrentItem() == currentPosition; //是否还停留在当前页签
                        bottomBarItem.setSelectedIcon(R.mipmap.tab_home_selected);//更换成首页原来选中图标
                        cancelTabLoading(bottomBarItem);
                    }, 3000);
                    return;
                }
            }

            //如果点击了其他条目
            BottomBarItem bottomItem = mBottomBarLayout.getBottomItem(0);
            bottomItem.setSelectedIcon(R.mipmap.tab_home_selected);//更换为原来的图标

            cancelTabLoading(bottomItem);//停止旋转动画
        });
    }

    /**
     * 停止首页页签的旋转动画
     */
    private void cancelTabLoading(BottomBarItem bottomItem) {
        Animation animation = bottomItem.getImageView().getAnimation();
        if (animation != null) {
            animation.cancel();
        }
    }
}
