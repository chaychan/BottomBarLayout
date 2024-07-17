package com.chaychan.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

/**
 * @author ChayChan
 * @description: 底部页签根节点
 * @date 2017/6/23  11:02
 */
public class BottomBarLayout extends FrameLayout implements ViewPager.OnPageChangeListener {

    private boolean titleTextBold = false;//文字加粗
    private int titleTextSize = 12;//文字大小 默认为12sp
    private int titleNormalColor;    //描述文本的默认显示颜色
    private int titleSelectedColor;  //述文本的默认选中显示颜色
    private int marginTop = 0;//文字和图标的距离,默认0dp
    private int iconWidth;//图标的宽度
    private int iconHeight;//图标的高度
    private int itemPadding;//BottomBarItem的padding
    private int unreadTextSize = 10; //未读数默认字体大小10sp
    private int unreadNumThreshold = 99;//未读数阈值
    private int unreadTextColor;//未读数字体颜色
    private Drawable unreadTextBg;//未读数字体背景
    private int msgTextSize = 6; //消息默认字体大小6sp
    private int msgTextColor;//消息文字颜色
    private Drawable msgTextBg;//消息文字背景
    private Drawable notifyPointBg;//小红点背景

    private Drawable barBackground;
    private int barHeight = 45;// bar的高度

    private Drawable floatIcon; //凸起图标
    private boolean floatEnable; //是否中间图标凸起
    private int floatMarginBottom = 0;//凸起按钮底部间距
    private int floatIconWidth; //凸起图标的宽度
    private int floatIconHeight; //凸起图标的高度

    private ViewPager mViewPager;
    private List<BottomBarItem> mItemViews = new ArrayList<>();
    private int mCurrentItem;//当前条目的索引
    private boolean mSmoothScroll;

    //相同tab点击是否回调
    private boolean mSameTabClickCallBack;

    private ViewPager2 mViewPager2;

    private LinearLayout mLlTab;

    public BottomBarLayout(Context context) {
        this(context, null);
    }

    public BottomBarLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomBarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BottomBarLayout);
        initAttrs(ta, context);
        mLlTab = new LinearLayout(context);
        mLlTab.setOrientation(LinearLayout.HORIZONTAL);
        if (barBackground != null){
            mLlTab.setBackground(barBackground);
        }else{
            mLlTab.setBackgroundColor(UIUtils.getColor(context, R.color.tab_gb));
        }
        addView(mLlTab);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i("bottomBarLayout", "width: " + getMeasuredWidth() + " height: " + barHeight);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(getMeasuredWidth(), barHeight);
        params.gravity = Gravity.BOTTOM;
        mLlTab.setLayoutParams(params);
    }

    private void initAttrs(TypedArray ta, Context context) {
        mSmoothScroll = ta.getBoolean(R.styleable.BottomBarLayout_smoothScroll, mSmoothScroll);
        mSameTabClickCallBack = ta.getBoolean(R.styleable.BottomBarLayout_sameTabClickCallBack, mSameTabClickCallBack);
        barBackground = ta.getDrawable(R.styleable.BottomBarLayout_barBackground);
        barHeight = ta.getDimensionPixelSize(R.styleable.BottomBarLayout_barHeight, UIUtils.dip2Px(context, barHeight));
        floatEnable = ta.getBoolean(R.styleable.BottomBarLayout_floatEnable, floatEnable);
        floatIcon = ta.getDrawable(R.styleable.BottomBarLayout_floatIcon);
        floatMarginBottom = ta.getDimensionPixelSize(R.styleable.BottomBarLayout_floatMarginBottom, UIUtils.dip2Px(context, floatMarginBottom));
        floatIconWidth = ta.getDimensionPixelSize(R.styleable.BottomBarLayout_floatIconWidth, UIUtils.dip2Px(context, floatIconWidth));
        floatIconHeight = ta.getDimensionPixelSize(R.styleable.BottomBarLayout_floatIconHeight, UIUtils.dip2Px(context, floatIconHeight));


        titleTextBold = ta.getBoolean(R.styleable.BottomBarLayout_itemTextBold, titleTextBold);
        titleTextSize = ta.getDimensionPixelSize(R.styleable.BottomBarLayout_itemTextSize, UIUtils.sp2px(context, titleTextSize));

        titleNormalColor = ta.getColor(R.styleable.BottomBarLayout_textColorNormal, UIUtils.getColor(context, R.color.bbl_999999));
        titleSelectedColor = ta.getColor(R.styleable.BottomBarLayout_textColorSelected, UIUtils.getColor(context, R.color.bbl_ff0000));

        marginTop = ta.getDimensionPixelSize(R.styleable.BottomBarLayout_itemMarginTop, UIUtils.dip2Px(context, marginTop));

        iconWidth = ta.getDimensionPixelSize(R.styleable.BottomBarLayout_iconWidth, 0);
        iconHeight = ta.getDimensionPixelSize(R.styleable.BottomBarLayout_iconHeight, 0);
        itemPadding = ta.getDimensionPixelSize(R.styleable.BottomBarLayout_itemPadding, 0);

        unreadTextSize = ta.getDimensionPixelSize(R.styleable.BottomBarLayout_unreadTextSize, UIUtils.sp2px(context, unreadTextSize));
        unreadTextColor = ta.getColor(R.styleable.BottomBarLayout_unreadTextColor, UIUtils.getColor(context, R.color.white));
        unreadTextBg = ta.getDrawable(R.styleable.BottomBarLayout_unreadTextBg);

        msgTextSize = ta.getDimensionPixelSize(R.styleable.BottomBarLayout_msgTextSize, UIUtils.sp2px(context, msgTextSize));
        msgTextColor = ta.getColor(R.styleable.BottomBarLayout_msgTextColor, UIUtils.getColor(context, R.color.white));
        msgTextBg = ta.getDrawable(R.styleable.BottomBarLayout_msgTextBg);

        notifyPointBg = ta.getDrawable(R.styleable.BottomBarLayout_notifyPointBg);

        unreadNumThreshold = ta.getInteger(R.styleable.BottomBarLayout_unreadThreshold, unreadNumThreshold);
    }

    public void setViewPager(ViewPager viewPager) {
        this.mViewPager = viewPager;

        if (mViewPager != null) {
            mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

    public void setViewPager2(androidx.viewpager2.widget.ViewPager2 viewPager2) {
        this.mViewPager2 = viewPager2;

        if (mViewPager2 != null) {
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

    private BottomBarItem createBottomBarItem(Drawable normalIcon, Drawable selectedIcon, String title, int iconWidth, int iconHeight, String lottieJson){
        return new BottomBarItem.Builder(getContext())
                .titleTextBold(titleTextBold)
                .titleTextSize(titleTextSize)
                .titleNormalColor(titleNormalColor)
                .iconHeight(iconHeight)
                .iconWidth(iconWidth)
                .marginTop(marginTop)
                .itemPadding(itemPadding)
                .titleSelectedColor(titleSelectedColor)
                .lottieJson(lottieJson)
                .unreadNumThreshold(unreadNumThreshold)
                .unreadTextBg(unreadTextBg)
                .unreadTextSize(unreadTextSize)
                .unreadTextColor(unreadTextColor)
                .msgTextBg(msgTextBg)
                .msgTextColor(msgTextColor)
                .msgTextSize(msgTextSize)
                .notifyPointBg(notifyPointBg)
                .create(normalIcon, selectedIcon, title);
    }

    public void setData(List<TabData> tabData){
        if (tabData == null || tabData.size() == 0){
            throw new IllegalArgumentException("tabData is null");
        }
        mItemViews.clear();
        mLlTab.removeAllViews();

        //添加tab
        for (int i = 0; i < tabData.size(); i++) {
            TabData itemData = tabData.get(i);
            Drawable normalIcon = !TextUtils.isEmpty(itemData.getLottieJson()) ? null : itemData.getNormalIcon() != null ? itemData.getNormalIcon() : getContext().getResources().getDrawable(itemData.getNormalIconResId());
            Drawable selectedIcon = !TextUtils.isEmpty(itemData.getLottieJson()) ? null : itemData.getSelectedIcon() != null ? itemData.getSelectedIcon() : getContext().getResources().getDrawable(itemData.getSelectedIconResId());
            int iconWidth = itemData.getIconWidth() == 0 ? this.iconWidth : itemData.getIconWidth();
            int iconHeight = itemData.getIconHeight() == 0 ? this.iconHeight : itemData.getIconHeight();
            BottomBarItem item = createBottomBarItem(normalIcon, selectedIcon, itemData.getTitle(), iconWidth, iconHeight, itemData.getLottieJson());
            addItem(item);
        }

        //如果开启凸起 且是 其他tab总数是偶数
        if (floatEnable && tabData.size() % 2 == 0){
            BottomBarItem item = createBottomBarItem(floatIcon, floatIcon, "", floatIconWidth, floatIconHeight, "");
            addItem(item, (tabData.size() + 1) / 2, true);
        }

        mItemViews.get(0).refreshTab(true);
    }

    public void addItem(BottomBarItem item){
        addItem(item, -1, false);
    }

    public void addItem(BottomBarItem item, int index, boolean isFloatItem) {
        if (index == -1){
            mItemViews.add(item);
        }else{
            mItemViews.add(index, item);
        }

        int position = index != -1 ? index : mItemViews.size() - 1;
        Log.e("bottomBarLayout", "position: " + position);

        View view = item;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT);
        layoutParams.weight = 1;
        layoutParams.gravity = Gravity.CENTER;
        view.setLayoutParams(layoutParams);

        if (isFloatItem){
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(floatIconWidth, floatIconHeight);
            params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
            params.bottomMargin = floatMarginBottom;
            addView(item, params);
            view = new View(getContext());
        }

        mLlTab.addView(view, position, layoutParams);

        //tab添加点击事件
        for (int i = 0; i < mItemViews.size(); i++) {
            mItemViews.get(i).setOnClickListener(new MyOnClickListener(i));
        }
    }

    public void removeItem(int position) {
        if (position >= 0 && position < mItemViews.size()) {
            BottomBarItem item = mItemViews.get(position);
            if (mItemViews.contains(item)) {
                resetState();
                mLlTab.removeViewAt(position);
            }
            mItemViews.remove(item);

            //tab添加点击事件
            for (int i = 0; i < mItemViews.size(); i++) {
                mItemViews.get(i).setOnClickListener(new MyOnClickListener(i));
            }
        }
    }

    private void handlePageSelected(int position){
        //滑动时判断是否需要拦截跳转
        if (mOnPageChangeInterceptor != null
                && mOnPageChangeInterceptor.onIntercepted(position)){
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
            if (mOnPageChangeInterceptor != null
                    && mOnPageChangeInterceptor.onIntercepted(currentIndex)){
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

    private OnPageChangeInterceptor mOnPageChangeInterceptor;

    public void setOnPageChangeInterceptor(OnPageChangeInterceptor onPageChangedInterceptor) {
        mOnPageChangeInterceptor = onPageChangedInterceptor;
    }

    public interface OnPageChangeInterceptor {
        boolean onIntercepted(int position);
    }
}
