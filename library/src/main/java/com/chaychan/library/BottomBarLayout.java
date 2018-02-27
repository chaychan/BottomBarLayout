package com.chaychan.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ChayChan
 * @description: 底部页签根节点
 * @date 2017/6/23  11:02
 */
public class BottomBarLayout extends LinearLayout implements ViewPager.OnPageChangeListener {

    private static final String STATE_INSTANCE = "instance_state";
    private static final String STATE_ITEM = "state_item";


    private ViewPager mViewPager;
    private int mChildCount;//子条目个数
    private List<BottomBarItem> mItemViews = new ArrayList<>();
    private int mCurrentItem;//当前条目的索引
    private boolean mSmoothScroll;

    public BottomBarLayout(Context context) {
        this(context, null);
    }

    public BottomBarLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomBarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BottomBarLayout);
        mSmoothScroll = ta.getBoolean(R.styleable.BottomBarLayout_smoothScroll,false);
        ta.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    @Override
    public void setOrientation(int orientation) {
        super.setOrientation(orientation);
    }

    public void setViewPager(ViewPager viewPager) {
        this.mViewPager = viewPager;
        init();
    }

    private void init() {
        mChildCount = getChildCount();

        if (mViewPager != null) {
            if (mViewPager.getAdapter().getCount() != mChildCount) {
                throw new IllegalArgumentException("LinearLayout的子View数量必须和ViewPager条目数量一致");
            }
        }

        for (int i = 0; i < mChildCount; i++) {
            if (getChildAt(i) instanceof BottomBarItem) {
                BottomBarItem bottomBarItem = (BottomBarItem) getChildAt(i);
                mItemViews.add(bottomBarItem);
                //设置点击监听
                bottomBarItem.setOnClickListener(new MyOnClickListener(i));
            } else {
                throw new IllegalArgumentException("BottomBarLayout的子View必须是BottomBarItem");
            }
        }

        mItemViews.get(mCurrentItem).setStatus(true);//设置选中项

        if (mViewPager != null){
            mViewPager.setOnPageChangeListener(this);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        resetState();
        mItemViews.get(position).setStatus(true);
        if (onItemSelectedListener != null){
            onItemSelectedListener.onItemSelected(getBottomItem(position),mCurrentItem,position);
        }
        mCurrentItem = position;//记录当前位置
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class MyOnClickListener implements OnClickListener {

        private int currentIndex;

        public MyOnClickListener(int i) {
            this.currentIndex = i;
        }

        @Override
        public void onClick(View v) {
            //回调点击的位置
            if(mViewPager != null){
                //有设置viewPager
                if (currentIndex == mCurrentItem){
                    //如果还是同个页签，使用setCurrentItem不会回调OnPageSelecte(),所以在此处需要回调点击监听
                    if (onItemSelectedListener != null) {
                        onItemSelectedListener.onItemSelected(getBottomItem(currentIndex),mCurrentItem,currentIndex);
                    }
                }else{
                    mViewPager.setCurrentItem(currentIndex, mSmoothScroll);
                }
            }else{
                //没有设置viewPager
                if (onItemSelectedListener != null) {
                    onItemSelectedListener.onItemSelected(getBottomItem(currentIndex),mCurrentItem,currentIndex);
                }

                updateTabState(currentIndex);
            }
        }
    }

    private void updateTabState(int position){
        resetState();
        mCurrentItem = position;
        mItemViews.get(mCurrentItem).setStatus(true);
    }

    /**
     * 重置当前按钮的状态
     */
    private void resetState() {
        if (mCurrentItem < mItemViews.size()){
            mItemViews.get(mCurrentItem).setStatus(false);
        }
    }

    public void setCurrentItem(int currentItem) {
        if (mViewPager != null){
            mViewPager.setCurrentItem(currentItem,mSmoothScroll);
        }else{
            updateTabState(currentItem);
        }
    }

    /**
     * 设置未读数
     * @param position 底部标签的下标
     * @param unreadNum 未读数
     */
    public void setUnread(int position,int unreadNum){
        mItemViews.get(position).setUnreadNum(unreadNum);
    }

    /**
     * 设置提示消息
     * @param position 底部标签的下标
     * @param msg 未读数
     */
    public void setMsg(int position,String msg){
        mItemViews.get(position).setMsg(msg);
    }

    /**
     * 隐藏提示消息
     * @param position 底部标签的下标
     */
    public void hideMsg(int position){
        mItemViews.get(position).hideMsg();
    }

    /**
     * 显示提示的小红点
     * @param position 底部标签的下标
     */
    public void showNotify(int position){
        mItemViews.get(position).showNotify();
    }

    /**
     * 隐藏提示的小红点
     * @param position 底部标签的下标
     */
    public void hideNotify(int position){
        mItemViews.get(position).hideNotify();
    }

    public int getCurrentItem() {
        return mCurrentItem;
    }

    public void setSmoothScroll(boolean smoothScroll) {
        this.mSmoothScroll = smoothScroll;
    }

    public BottomBarItem getBottomItem(int position){
        return mItemViews.get(position);
    }

    /**
     * @return 当View被销毁的时候，保存数据
     */
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(STATE_INSTANCE, super.onSaveInstanceState());
        bundle.putInt(STATE_ITEM, mCurrentItem);
        return bundle;
    }

    /**
     * @param state 用于恢复数据使用
     */
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mCurrentItem = bundle.getInt(STATE_ITEM);
            //重置所有按钮状态
            resetState();
            //恢复点击的条目颜色
            mItemViews.get(mCurrentItem).setStatus(true);
            super.onRestoreInstanceState(bundle.getParcelable(STATE_INSTANCE));
        } else {
            super.onRestoreInstanceState(state);
        }
    }

    private OnItemSelectedListener onItemSelectedListener;

    public interface OnItemSelectedListener {
        void onItemSelected(BottomBarItem bottomBarItem, int previousPosition,int currentPosition);
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }
}
