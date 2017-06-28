package com.chaychan.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



/**
 * @author ChayChan
 * @description: 底部tab条目
 * @date 2017/6/23  9:14
 */

public class BottomBarItem extends LinearLayout {

    private Context mContext;
    private int mIconNormalResourceId;//普通状态图标的资源id
    private int mIconSelectedResourceId;//选中状态图标的资源id
    private String mText;//文本
    private int mTextSize = 12;//文字大小 默认为12sp
    private int mTextColorNormal = 0xFF999999;    //描述文本的默认显示颜色
    private int mTextColorSelected = 0xFF46C01B;  //述文本的默认选中显示颜色
    private int mMarginTop = 0;//文字和图标的距离,默认0dp
    private boolean mOpenTouchBg = false;// 是否开启触摸背景，默认关闭
    private Drawable mTouchDrawable;//触摸时的背景

    private TextView mTextView;
    private ImageView mImageView;



    public BottomBarItem(Context context) {
        this(context, null);
    }

    public BottomBarItem(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomBarItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BottomBarItem);

        mIconNormalResourceId = ta.getResourceId(R.styleable.BottomBarItem_iconNormal, -1);
        mIconSelectedResourceId = ta.getResourceId(R.styleable.BottomBarItem_iconSelected, -1);

        mText = ta.getString(R.styleable.BottomBarItem_itemText);
        mTextSize = ta.getDimensionPixelSize(R.styleable.BottomBarItem_itemTextSize, UIUtils.sp2px(mContext,mTextSize));

        mTextColorNormal = ta.getColor(R.styleable.BottomBarItem_textColorNormal, mTextColorNormal);
        mTextColorSelected = ta.getColor(R.styleable.BottomBarItem_textColorSelected, mTextColorSelected);

        mMarginTop = ta.getDimensionPixelSize(R.styleable.BottomBarItem_itemMarginTop, UIUtils.dip2Px(mContext, mMarginTop));

        mOpenTouchBg = ta.getBoolean(R.styleable.BottomBarItem_openTouchBg, mOpenTouchBg);
        mTouchDrawable = ta.getDrawable(R.styleable.BottomBarItem_touchDrawable);

        ta.recycle();

        checkValues();
        init();
    }

    /**
     * 检查传入的值是否完善
     */
    private void checkValues() {
        if (mIconNormalResourceId == -1) {
            throw new IllegalStateException("您还没有设置默认状态下的图标，请指定iconNormal的图标");
        }

        if (mIconSelectedResourceId == -1) {
            throw new IllegalStateException("您还没有设置选中状态下的图标，请指定iconSelected的图标");
        }

        if (mOpenTouchBg && mTouchDrawable == null){
            //如果有开启触摸背景效果但是没有传对应的drawable
            throw new IllegalStateException("开启了触摸效果，但是没有指定touchDrawable");
        }
    }

    private void init() {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);

        View view = View.inflate(mContext, R.layout.item_bottom_bar, null);
        mImageView = (ImageView) view.findViewById(R.id.iv_icon);
        mTextView = (TextView) view.findViewById(R.id.tv_text);

        mImageView.setImageResource(mIconNormalResourceId);
        mTextView.getPaint().setTextSize(mTextSize);
        mTextView.setText(mText);
        mTextView.setTextColor(mTextColorNormal);

        LayoutParams layoutParams = (LayoutParams) mTextView.getLayoutParams();
        layoutParams.topMargin = mMarginTop;
        mTextView.setLayoutParams(layoutParams);

        if (mOpenTouchBg){
            //如果有开启触摸背景
            setBackground(mTouchDrawable);
        }

        addView(view);
    }

    public ImageView getImageView() {
        return mImageView;
    }

    public TextView getTextView() {
        return mTextView;
    }

    public void setIconNormalResourceId(int mIconNormalResourceId) {
        this.mIconNormalResourceId = mIconNormalResourceId;
    }

    public void setIconSelectedResourceId(int mIconSelectedResourceId) {
        this.mIconSelectedResourceId = mIconSelectedResourceId;
    }

    public void setStatus(boolean isSelected){
        mImageView.setImageResource(isSelected?mIconSelectedResourceId:mIconNormalResourceId);
        mTextView.setTextColor(isSelected?mTextColorSelected:mTextColorNormal);
    }
}
