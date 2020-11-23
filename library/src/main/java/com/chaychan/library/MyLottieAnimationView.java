package com.chaychan.library;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;

import com.airbnb.lottie.LottieAnimationView;

/**
 * @author ChayChan
 * @description: 去除LottieAnimationView的缓存
 * @date 2020/11/23  16:02
 */
class MyLottieAnimationView extends LottieAnimationView {

    public MyLottieAnimationView(Context context) {
        super(context);
    }

    public MyLottieAnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLottieAnimationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 重写此方法将LottieAnimationView的缓存去除
     * 解决因异常情况或旋转方向后页面重新加载
     * 导致lottie文件读取成最后一个tab文件的bug
     * @return
     */
    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable parcelable = super.onSaveInstanceState();
        parcelable = null;
        return null;
    }
}
