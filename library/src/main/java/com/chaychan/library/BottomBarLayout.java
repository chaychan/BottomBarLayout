package com.chaychan.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

/**
 * @author ChayChan
 * @description: 底部页签根节点
 * @date 2017/6/23  11:02
 */
public class BottomBarLayout extends LinearLayout implements ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private List<BottomBarItem> mItemViews = new ArrayList<>();
    private int mCurrentItem;//当前条目的索引
    private boolean mSmoothScroll;

    //相同tab点击是否回调
    private boolean mSameTabClickCallBack;

    private ViewPager2 mViewPager2;

    public BottomBarLayout(Context context) {
        this(context, null);
    }

    public BottomBarLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomBarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BottomBarLayout);
        mSmoothScroll = ta.getBoolean(R.styleable.BottomBarLayout_smoothScroll, mSmoothScroll);
        mSameTabClickCallBack = ta.getBoolean(R.styleable.BottomBarLayout_sameTabClickCallBack, mSameTabClickCallBack);
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

        if (mViewPager != null) {
            PagerAdapter adapter = mViewPager.getAdapter();
            if (adapter != null && adapter.getCount() != getChildCount()) {
                throw new IllegalArgumentException("LinearLayout的子View数量必须和ViewPager条目数量一致");
            }
            mViewPager.setOnPageChangeListener(this);
        }
    }

    public void setViewPager2(androidx.viewpager2.widget.ViewPager2 viewPager2) {
        this.mViewPager2 = viewPager2;

        if (mViewPager2 != null) {
            RecyclerView.Adapter adapter = mViewPager2.getAdapter();
            if (adapter != null && adapter.getItemCount() != getChildCount()) {
                throw new IllegalArgumentException("LinearLayout的子View数量必须和ViewPager条目数量一致");
            }
            mViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }
                @Override
                public void onPageSelected(int position) {
                    handlePageSelected(position);
                }
                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
        }
    }

    private void init() {
        mItemViews.clear();
        int childCount = getChildCount();
        if (childCount == 0) {
            return;
        }

        for (int i = 0; i < childCount; i++) {
            if (getChildAt(i) instanceof BottomBarItem) {
                BottomBarItem bottomBarItem = (BottomBarItem) getChildAt(i);
                mItemViews.add(bottomBarItem);
                //设置点击监听
                bottomBarItem.setOnClickListener(new MyOnClickListener(i));
            } else {
                throw new IllegalArgumentException("BottomBarLayout的子View必须是BottomBarItem");
            }
        }

        if (mCurrentItem < mItemViews.size())
            mItemViews.get(mCurrentItem).refreshTab(true);
    }

    public void addItem(BottomBarItem item) {
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1;
        item.setLayoutParams(layoutParams);
        addView(item);
        init();
    }

    public void removeItem(int position) {
        if (position >= 0 && position < mItemViews.size()) {
            BottomBarItem item = mItemViews.get(position);
            if (mItemViews.contains(item)) {
                resetState();
                removeViewAt(position);
                init();
            }
        }
    }

    private void handlePageSelected(int position){
        if (onPageChangedInterceptor != null
                && onPageChangedInterceptor.onPageChangedIntercepted(position)){
            setCurrentItem(mCurrentItem);
            return;
        }
        resetState();
        mItemViews.get(position).refreshTab(true);
        int prePos = mCurrentItem;
        mCurrentItem = position;//记录当前位置
        if (onItemSelectedListener != null) {
            onItemSelectedListener.onItemSelected(getBottomItem(mCurrentItem), prePos, mCurrentItem);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        handlePageSelected(position);
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
            //点击时判断是否需要拦截跳转
            if (onPageChangedInterceptor != null
                    && onPageChangedInterceptor.onPageChangedIntercepted(currentIndex)){
                return;
            }
            if (currentIndex == mCurrentItem) {
                //如果还是同个页签，判断是否要回调
                if (onItemSelectedListener != null && mSameTabClickCallBack){
                    onItemSelectedListener.onItemSelected(getBottomItem(currentIndex), mCurrentItem, currentIndex);
                }
            }else{
                if (mViewPager != null || mViewPager2 != null) {
                    if (mViewPager != null){
                        mViewPager.setCurrentItem(currentIndex, mSmoothScroll);
                    }else {
                        mViewPager2.setCurrentItem(currentIndex, mSmoothScroll);
                    }
                    return;
                }
                if (onItemSelectedListener != null){
                    onItemSelectedListener.onItemSelected(getBottomItem(currentIndex), mCurrentItem, currentIndex);
                }
                updateTabState(currentIndex);
            }
        }
    }

    private void updateTabState(int position) {
        resetState();
        mCurrentItem = position;
        mItemViews.get(mCurrentItem).refreshTab(true);
    }

    /**
     * 重置当前按钮的状态
     */
    private void resetState() {
        if (mCurrentItem < mItemViews.size()) {
            if (mItemViews.get(mCurrentItem).isSelected()){
                mItemViews.get(mCurrentItem).refreshTab(false);
            }
        }
    }

    public void setCurrentItem(int currentItem) {
        if (mViewPager != null || mViewPager2 != null) {
            if (mViewPager != null){
                mViewPager.setCurrentItem(currentItem, mSmoothScroll);
            }else {
                mViewPager2.setCurrentItem(currentItem, mSmoothScroll);
            }
        } else {
            if (onItemSelectedListener != null) {
                onItemSelectedListener.onItemSelected(getBottomItem(currentItem), mCurrentItem, currentItem);
            }
            updateTabState(currentItem);
        }
    }

    /**
     * 设置未读数
     *
     * @param position  底部标签的下标
     * @param unreadNum 未读数
     */
    public void setUnread(int position, int unreadNum) {
        mItemViews.get(position).setUnreadNum(unreadNum);
    }

    /**
     * 设置提示消息
     *
     * @param position 底部标签的下标
     * @param msg      未读数
     */
    public void setMsg(int position, String msg) {
        mItemViews.get(position).setMsg(msg);
    }

    /**
     * 隐藏提示消息
     *
     * @param position 底部标签的下标
     */
    public void hideMsg(int position) {
        mItemViews.get(position).hideMsg();
    }

    /**
     * 显示提示的小红点
     *
     * @param position 底部标签的下标
     */
    public void showNotify(int position) {
        mItemViews.get(position).showNotify();
    }

    /**
     * 隐藏提示的小红点
     *
     * @param position 底部标签的下标
     */
    public void hideNotify(int position) {
        mItemViews.get(position).hideNotify();
    }

    public int getCurrentItem() {
        return mCurrentItem;
    }

    public void setSmoothScroll(boolean smoothScroll) {
        this.mSmoothScroll = smoothScroll;
    }

    public BottomBarItem getBottomItem(int position) {
        return mItemViews.get(position);
    }

    private OnItemSelectedListener onItemSelectedListener;

    public interface OnItemSelectedListener {
        void onItemSelected(BottomBarItem bottomBarItem, int previousPosition, int currentPosition);
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    private OnPageChangedInterceptor onPageChangedInterceptor;

    public void setOnPageChangedInterceptor(OnPageChangedInterceptor onPageChangedInterceptor) {
        this.onPageChangedInterceptor = onPageChangedInterceptor;
    }

    public interface OnPageChangedInterceptor {
        boolean onPageChangedIntercepted(int position);
    }
}
