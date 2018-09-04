package com.hx.steven.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;

/**
 * 窗体操作工具
 * 界面背景颜色的修改，控件x,y坐标的获取，屏幕宽和高的获取
 * Created by user on 2017/11/14.
 */

public class WindowUtil {
    /**
     * 屏幕宽度
     */
    private int screenWidth;
    /**
     * 屏幕宽度
     */
    private int screenHeight;

    private WindowUtil() {
    }

    /**
     * 获得单例对象
     *既实现了线程安全，又避免了同步带来的性能影响
     * @return
     */
    public static WindowUtil getInstance() {
        return WindowUtilInstance.instance;
    }

    /**
     * 获取屏幕的宽
     *
     * @param context Context
     * @return 屏幕的宽
     */
    public int getScreenWidth(Activity context) {
        if (context == null) {
            return 0;
        }
        if (screenWidth != 0) {
            return screenWidth;
        }
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        return screenWidth;
    }

    /**
     * 获取屏幕的高
     *
     * @param context Context
     * @return 屏幕的高
     */
    public int getScreenHeight(Activity context) {
        if (context == null) {
            return 0;
        }
        if (screenHeight != 0) {
            return screenHeight;
        }
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenHeight = dm.heightPixels;
        return screenHeight;
    }

    /**
     * 获取控件的位置
     *
     * @param view 控件View
     * @return int[] x,y
     */
    public int[] getViewLocation(View view) {
        int[] location = new int[2]; //获取筛选按钮的x坐标
        view.getLocationOnScreen(location);
        return location;
    }

    public int getStateBarHeight(Context context) {
        Rect rect = new Rect();
        Activity activity = (Activity) context;
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int statusBarHeight = rect.top;
        return statusBarHeight;
    }

    private static final class WindowUtilInstance {
        private static final WindowUtil instance = new WindowUtil();
    }

    /**
     * 隐藏导航栏
     * @param window
     */
    public static void setHideVirtualKey(Window window) {
        //保持布局状态
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                //布局位于状态栏下方
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                //全屏
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                //隐藏导航栏
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        if (Build.VERSION.SDK_INT >= 19) {
            uiOptions |= 0x00001000;
        } else {
            uiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        }
        window.getDecorView().setSystemUiVisibility(uiOptions);
    }
}
