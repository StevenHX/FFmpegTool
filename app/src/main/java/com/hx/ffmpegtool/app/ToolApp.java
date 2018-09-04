package com.hx.ffmpegtool.app;

import android.os.Handler;

import com.hx.ffmpegtool.ffmpeg.FFmpegManer;
import com.hx.steven.app.BaseApplication;


/**
 * Created by huangxiao on 2018/9/3.
 */

public class ToolApp extends BaseApplication {
    private Handler mHandler;
    private static ToolApp instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mHandler = new Handler(getMainLooper());
        //初始化ffmpeg
        FFmpegManer.getInstance().loadFFMpegBinary(this);
    }

    public Handler getHandler(){
        if(mHandler!=null){
            return mHandler;
        }else{
            return new Handler(getMainLooper());
        }
    }
    public static ToolApp getInstance() {
        return instance;
    }

}
