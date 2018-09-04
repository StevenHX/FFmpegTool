package com.hx.ffmpegtool.ui.main;


import android.animation.ValueAnimator;
import android.widget.ImageView;

import com.hx.ffmpegtool.R;
import com.hx.ffmpegtool.app.ToolApp;

import java.util.Timer;
import java.util.TimerTask;

public class MainBgAssist {

    // ====== 单例 ======
    private static MainBgAssist instance;
    private int mCurrentIndex;
    private float currentValue;
    private Timer mTimer;
    private ValueAnimator mAnimation;
    private boolean isInit = true;
    private ImageView mBg1;
    private ImageView mBg2;
    private int[] mResBg = new int[]{R.mipmap.mainbg1, R.mipmap.mainbg2, R.mipmap.mainbg3, R.mipmap.mainbg4};

    private MainBgAssist() {
    }

    public static MainBgAssist getInstance() {
        if (instance == null)
            synchronized (MainBgAssist.class) {
                if (instance == null)
                    instance = new MainBgAssist();
            }
        return instance;
    }

    public void bindView(ImageView bg1, ImageView bg2) {
        this.mBg1 = bg1;
        this.mBg2 = bg2;
        currentValue = 1f; // 完成状态
        mCurrentIndex = 0;
        initView();
        startChange();
    }

    /**
     * 初始化View
     */
    private void initView() {
        mBg1.setImageResource(mResBg[mCurrentIndex]);
        mBg2.setAlpha(0f);
    }

    /**
     * 开始计时
     */
    public void startChange() {
        initTimer();
        resumeAnim();
    }

    /**
     * 初始化计时器
     */
    private void initTimer() {
        if (mTimer != null) {
            mTimer.cancel();
        }
        mTimer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                ToolApp.getInstance().getHandler().post(() -> {
                    changeBg(mCurrentIndex++ % mResBg.length);
                });
            }
        };
        mTimer.schedule(timerTask, isInit ? 4000 : 4000, 4000);
    }

    /**
     * 动画恢复
     */
    private void resumeAnim() {
        if (currentValue < 1.0f) {
            mAnimation = ValueAnimator.ofFloat(currentValue, 1.0f);
            mAnimation.setDuration((long) ((1.0f - currentValue) * 800));
            mAnimation.addUpdateListener(animation -> {
                float value = (float) animation.getAnimatedValue();
                currentValue = value;
                mBg2.setAlpha(value);
            });
            mAnimation.start();
        }
    }

    /**
     * 改变背景
     *
     * @param position
     */
    private void changeBg(int position) {
        isInit = false;
        int index1 = position;
        int index2 = (position + 1) % mResBg.length;
        mBg1.setImageResource(mResBg[index1]);
        mBg2.setImageResource(mResBg[index2]);
        mBg2.setAlpha(0f);
        mAnimation = ValueAnimator.ofFloat(0f, 1.0f);
        mAnimation.setDuration(800);
        mAnimation.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            currentValue = value;
            mBg2.setAlpha(value);
        });
        mAnimation.start();
    }


    /**
     * 清除所有
     */
    public void clearAll() {
        mBg1 = null;
        mBg2 = null;
        stopChange();
        if (mAnimation != null) {
            mAnimation.cancel();
            mAnimation = null;
        }
    }

    /**
     * 停止计时
     */
    public void stopChange() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mAnimation != null && mAnimation.isRunning()) {
            mAnimation.cancel();
        }
    }
}
