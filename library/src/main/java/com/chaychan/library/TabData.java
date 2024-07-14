package com.chaychan.library;

import android.graphics.drawable.Drawable;

/**
 * @author chay
 * @description:
 * @date 2024/7/14 16:58
 */
public class TabData {
    private String title;

    private int normalIconResId;
    private Drawable normalIcon;

    private int selectedIconResId;
    private Drawable selectedIcon;

    private String lottieJson;

    private int iconWidth;
    private int iconHeight;

    public TabData(String title, Drawable normalIcon, Drawable selectedIcon){
        this.title = title;
        this.normalIcon = normalIcon;
        this.selectedIcon = selectedIcon;
    }

    public TabData(String title, int normalIconResId, int selectedIconResId){
        this.title = title;
        this.normalIconResId = normalIconResId;
        this.selectedIconResId = selectedIconResId;
    }

    public TabData(String title, String lottieJson){
        this.title = title;
        this.lottieJson = lottieJson;
    }

    public String getLottieJson() {
        return lottieJson;
    }

    public void setLottieJson(String lottieJson) {
        this.lottieJson = lottieJson;
    }

    public int getIconWidth() {
        return iconWidth;
    }

    public void setIconWidth(int iconWidth) {
        this.iconWidth = iconWidth;
    }

    public int getIconHeight() {
        return iconHeight;
    }

    public void setIconHeight(int iconHeight) {
        this.iconHeight = iconHeight;
    }

    public int getNormalIconResId() {
        return normalIconResId;
    }

    public void setNormalIconResId(int normalIconResId) {
        this.normalIconResId = normalIconResId;
    }

    public int getSelectedIconResId() {
        return selectedIconResId;
    }

    public void setSelectedIconResId(int selectedIconResId) {
        this.selectedIconResId = selectedIconResId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Drawable getNormalIcon() {
        return normalIcon;
    }

    public void setNormalIcon(Drawable normalIcon) {
        this.normalIcon = normalIcon;
    }

    public Drawable getSelectedIcon() {
        return selectedIcon;
    }

    public void setSelectedIcon(Drawable selectedIcon) {
        this.selectedIcon = selectedIcon;
    }
}
