package com.chaychan.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * @author ChayChan
 * @description: 底部tab条目
 * @date 2017/6/23  9:14
 */

public class BottomBarItem extends LinearLayout {

    private Context context;
    private ImageView mImageView;
    private LottieAnimationView mLottieView;
    private TextView mTvUnread;
    private TextView mTvNotify;
    private TextView mTvMsg;
    private TextView mTextView;

    private Builder mBuilder;

    public BottomBarItem(Context context) {
        super(context);
    }

    public BottomBarItem(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomBarItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        checkValues();//检查值是否合法
        init();//初始化相关操作
    }

    /**
     * 检查传入的值是否完善
     */
    private void checkValues() {
        if (mBuilder == null){
            throw new IllegalStateException("Builder is null");
        }

        if (mBuilder.unreadTextBg == null) {
            mBuilder.unreadTextBg = getResources().getDrawable(R.drawable.shape_unread);
        }

        if (mBuilder.msgTextBg == null) {
            mBuilder.msgTextBg = getResources().getDrawable(R.drawable.shape_msg);
        }

        if (mBuilder.notifyPointBg == null) {
            mBuilder.notifyPointBg = getResources().getDrawable(R.drawable.shape_notify_point);
        }
    }

    private void init() {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);

        View view = initView();

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mImageView.getLayoutParams();
        if (mBuilder.iconWidth != 0 && mBuilder.iconHeight != 0) {
            //如果有设置图标的宽度和高度，则设置ImageView的宽高
            layoutParams.width = mBuilder.iconWidth;
            layoutParams.height = mBuilder.iconHeight;
        }

        if (!TextUtils.isEmpty(mBuilder.lottieJson)){
            mLottieView.setLayoutParams(layoutParams);
            mLottieView.setAnimation(mBuilder.lottieJson);
            mLottieView.setRepeatCount(0);
        }else{
            mImageView.setImageDrawable(mBuilder.normalIcon);
            mImageView.setLayoutParams(layoutParams);
        }

        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mBuilder.titleTextSize);//设置底部文字字体大小
        mTextView.getPaint().setFakeBoldText(mBuilder.titleTextBold);
        mTvUnread.setTextSize(TypedValue.COMPLEX_UNIT_PX, mBuilder.unreadTextSize);//设置未读数的字体大小
        mTvUnread.setTextColor(mBuilder.unreadTextColor);//设置未读数字体颜色
        mTvUnread.setBackground(mBuilder.unreadTextBg);//设置未读数背景

        mTvMsg.setTextSize(TypedValue.COMPLEX_UNIT_PX, mBuilder.msgTextSize);//设置提示文字的字体大小
        mTvMsg.setTextColor(mBuilder.msgTextColor);//设置提示文字的字体颜色
        mTvMsg.setBackground(mBuilder.msgTextBg);//设置提示文字的背景颜色

        mTvNotify.setBackground(mBuilder.notifyPointBg);//设置提示点的背景颜色

        mTextView.setTextColor(mBuilder.titleNormalColor);//设置底部文字字体颜色
        mTextView.setText(mBuilder.title);//设置标签文字

        LayoutParams textLayoutParams = (LayoutParams) mTextView.getLayoutParams();
        textLayoutParams.topMargin = mBuilder.marginTop;
        mTextView.setLayoutParams(textLayoutParams);

        addView(view);
    }

    @NonNull
    private View initView() {
        View view = View.inflate(context, R.layout.item_bottom_bar, null);
        if (mBuilder.itemPadding != 0) {
            //如果有设置item的padding
            view.setPadding(mBuilder.itemPadding, mBuilder.itemPadding, mBuilder.itemPadding, mBuilder.itemPadding);
        }
        mImageView = view.findViewById(R.id.iv_icon);
        mLottieView = view.findViewById(R.id.lottieView);
        mTvUnread = view.findViewById(R.id.tv_unred_num);
        mTvMsg = view.findViewById(R.id.tv_msg);
        mTvNotify = view.findViewById(R.id.tv_point);
        mTextView = view.findViewById(R.id.tv_text);

        mImageView.setVisibility(!TextUtils.isEmpty(mBuilder.lottieJson) ? GONE : VISIBLE);
        mLottieView.setVisibility(!TextUtils.isEmpty(mBuilder.lottieJson) ? VISIBLE : GONE);

        return view;
    }

    public String getTitle(){
        return mBuilder.title;
    }

    public ImageView getImageView() {
        return mImageView;
    }

    public TextView getTextView() {
        return mTextView;
    }

    public void setNormalIcon(Drawable normalIcon) {
        mBuilder.normalIcon = normalIcon;
        refreshTab();
    }

    public void setNormalIcon(int resId) {
        setNormalIcon(UIUtils.getDrawable(context, resId));
    }

    public void setSelectedIcon(Drawable selectedIcon) {
        mBuilder.selectedIcon = selectedIcon;
        refreshTab();
    }

    public void setSelectedIcon(int resId) {
        setSelectedIcon(UIUtils.getDrawable(context, resId));
    }

    public void refreshTab(boolean isSelected) {
        setSelected(isSelected);
        refreshTab();
    }

    public void refreshTab() {
        if (!TextUtils.isEmpty(mBuilder.lottieJson)){
            if (isSelected()){
                mLottieView.playAnimation();
            }else{
                //取消动画 进度设置为0
                mLottieView.cancelAnimation();
                mLottieView.setProgress(0);
            }
        }else{
            mImageView.setImageDrawable(isSelected() ? mBuilder.selectedIcon : mBuilder.normalIcon);
        }

        mTextView.setTextColor(isSelected() ? mBuilder.titleSelectedColor : mBuilder.titleNormalColor);
    }

    private void setTvVisible(TextView tv) {
        //都设置为不可见
        mTvUnread.setVisibility(GONE);
        mTvMsg.setVisibility(GONE);
        mTvNotify.setVisibility(GONE);

        tv.setVisibility(VISIBLE);//设置为可见
    }

    public int getUnreadNumThreshold() {
        return mBuilder.unreadNumThreshold;
    }

    public void setUnreadNumThreshold(int unreadNumThreshold) {
        mBuilder.unreadNumThreshold = unreadNumThreshold;
    }

    public void setUnreadNum(int unreadNum) {
        setTvVisible(mTvUnread);
        if (unreadNum <= 0) {
            mTvUnread.setVisibility(GONE);
        } else if (unreadNum <= mBuilder.unreadNumThreshold) {
            mTvUnread.setText(String.valueOf(unreadNum));
        } else {
            mTvUnread.setText(String.format(Locale.CHINA, "%d+", mBuilder.unreadNumThreshold));
        }
    }

    public void setMsg(String msg) {
        setTvVisible(mTvMsg);
        mTvMsg.setText(msg);
    }

    public void hideMsg() {
        mTvMsg.setVisibility(GONE);
    }

    public void showNotify() {
        setTvVisible(mTvNotify);
    }

    public void hideNotify() {
        mTvNotify.setVisibility(GONE);
    }

    public BottomBarItem create(Builder builder) {
        this.context = builder.context;
        mBuilder = builder;
        checkValues();
        init();
        return this;
    }

    public static final class Builder {
        private Context context;
        private Drawable normalIcon;//普通状态图标的资源id
        private Drawable selectedIcon;//选中状态图标的资源id
        private String title;//标题
        private boolean titleTextBold;//文字加粗
        private int titleTextSize;//字体大小
        private int titleNormalColor;    //描述文本的默认显示颜色
        private int titleSelectedColor;  //述文本的默认选中显示颜色
        private int marginTop;//文字和图标的距离
        private int iconWidth;//图标的宽度
        private int iconHeight;//图标的高度
        private int itemPadding;//BottomBarItem的padding
        private int unreadTextSize; //未读数字体大小
        private int unreadNumThreshold;//未读数阈值
        private int unreadTextColor;//未读数字体颜色
        private Drawable unreadTextBg;//未读数文字背景
        private int msgTextSize; //消息字体大小
        private int msgTextColor;//消息文字颜色
        private Drawable msgTextBg;//消息提醒背景颜色
        private Drawable notifyPointBg;//小红点背景颜色
        private String lottieJson; //lottie文件名

        public Builder(Context context) {
            this.context = context;
            titleTextBold = false;
            titleTextSize = UIUtils.sp2px(context, 12);
            titleNormalColor = getColor(R.color.bbl_999999);
            titleSelectedColor = getColor(R.color.bbl_ff0000);
            unreadTextSize = UIUtils.sp2px(context, 10);
            msgTextSize = UIUtils.sp2px(context, 6);
            unreadTextColor = getColor(R.color.white);
            unreadNumThreshold = 99;
            msgTextColor = getColor(R.color.white);
        }

        /**
         * Sets the default icon's resourceId
         */
        public Builder normalIcon(Drawable normalIcon) {
            this.normalIcon = normalIcon;
            return this;
        }

        /**
         * Sets the selected icon's resourceId
         */
        public Builder selectedIcon(Drawable selectedIcon) {
            this.selectedIcon = selectedIcon;
            return this;
        }

        /**
         * Sets the title's resourceId
         */
        public Builder title(int titleId) {
            this.title = context.getString(titleId);
            return this;
        }

        /**
         * Sets the title string
         */
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        /**
         * Sets the title's text bold
         */
        public Builder titleTextBold(boolean titleTextBold) {
            this.titleTextBold = titleTextBold;
            return this;
        }

        /**
         * Sets the title's text size
         */
        public Builder titleTextSize(int titleTextSize) {
            this.titleTextSize = titleTextSize;
            return this;
        }

        /**
         * Sets the title's normal color resourceId
         */
        public Builder titleNormalColor(int titleNormalColor) {
            this.titleNormalColor = titleNormalColor;
            return this;
        }

        /**
         * Sets the title's selected color resourceId
         */
        public Builder titleSelectedColor(int titleSelectedColor) {
            this.titleSelectedColor = titleSelectedColor;
            return this;
        }

        /**
         * Sets the item's margin top
         */
        public Builder marginTop(int marginTop) {
            this.marginTop = marginTop;
            return this;
        }

        /**
         * Sets icon's width
         */
        public Builder iconWidth(int iconWidth) {
            this.iconWidth = iconWidth;
            return this;
        }

        /**
         * Sets icon's height
         */
        public Builder iconHeight(int iconHeight) {
            this.iconHeight = iconHeight;
            return this;
        }


        /**
         * Sets padding for item
         */
        public Builder itemPadding(int itemPadding) {
            this.itemPadding = itemPadding;
            return this;
        }

        /**
         * Sets unread font size
         */
        public Builder unreadTextSize(int unreadTextSize) {
            this.unreadTextSize = unreadTextSize;
            return this;
        }

        /**
         * Sets the number of unread array thresholds greater than the threshold to be displayed as n + n as the set threshold
         */
        public Builder unreadNumThreshold(int unreadNumThreshold) {
            this.unreadNumThreshold = unreadNumThreshold;
            return this;
        }

        /**
         * Sets the message font size
         */
        public Builder msgTextSize(int msgTextSize) {
            this.msgTextSize = msgTextSize;
            return this;
        }

        /**
         * Sets the message font background
         */
        public Builder unreadTextBg(Drawable unreadTextBg) {
            this.unreadTextBg = unreadTextBg;
            return this;
        }

        /**
         * Sets unread font color
         */
        public Builder unreadTextColor(int unreadTextColor) {
            this.unreadTextColor = unreadTextColor;
            return this;
        }

        /**
         * Sets the message font color
         */
        public Builder msgTextColor(int msgTextColor) {
            this.msgTextColor =msgTextColor;
            return this;
        }

        /**
         * Sets the message font background
         */
        public Builder msgTextBg(Drawable msgTextBg) {
            this.msgTextBg = msgTextBg;
            return this;
        }

        /**
         * Set the message prompt point background
         */
        public Builder notifyPointBg(Drawable notifyPointBg) {
            this.notifyPointBg = notifyPointBg;
            return this;
        }

        /**
         * Set the name of lottie json file
         */
        public Builder lottieJson(String lottieJson) {
            this.lottieJson = lottieJson;
            return this;
        }

        /**
         * Create a BottomBarItem object
         */
        public BottomBarItem create(Drawable normalIcon, Drawable selectedIcon, String text) {
            this.normalIcon = normalIcon;
            this.selectedIcon = selectedIcon;
            title = text;

            BottomBarItem bottomBarItem = new BottomBarItem(context);
            return bottomBarItem.create(this);
        }

        public BottomBarItem create(int normalIconId, int selectedIconId, String text) {
            return create(UIUtils.getDrawable(context, normalIconId), UIUtils.getDrawable(context, selectedIconId), text);
        }

        private int getColor(int colorId) {
            return context.getResources().getColor(colorId);
        }

    }
}
