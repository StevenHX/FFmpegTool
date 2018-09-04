package com.hx.ffmpegtool.ffmpeg;

import android.content.Context;
import android.util.Log;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

public class FFmpegManer {
    private static final String TAG = "FFmpegManer";
    private volatile static FFmpegManer instance;
    private FFmpeg ffmpeg;

    public static FFmpegManer getInstance() {
        if (instance == null) {
            synchronized (FFmpegManer.class) {
                if (instance == null) {
                    instance = new FFmpegManer();
                }
            }
        }
        return instance;
    }

    private FFmpegManer() {
    }

    /**
     * 初始化
     * @param context
     */
    public void loadFFMpegBinary(Context context) {
        if(ffmpeg == null){
            ffmpeg = FFmpeg.getInstance(context);
        }
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onFailure() {
                    Log.d(TAG,"loadBinary---onFailure");
                }

                @Override
                public void onSuccess() {
                    super.onSuccess();
                    Log.d(TAG,"loadBinary---onSuccess");
                }
            });
        } catch (FFmpegNotSupportedException e) {
        }
    }

    public void runFFmpegCommad(String cmd, final FFmpegExecuteListener listener){
        try {
            ffmpeg.execute(cmd.split(" "),new ExecuteBinaryResponseHandler(){
                @Override
                public void onFailure(String s) {
                    Log.d(TAG, "onFailure command : ffmpeg ="+s);
                    listener.FFmpegExcuteFail(s);
                }
                @Override
                public void onSuccess(String s) {
                    Log.d(TAG, "onSuccess command : ffmpeg ="+s);
                    listener.FFmpegExcuteSuccess(s);
                }
                @Override
                public void onProgress(String s) {
                    Log.d(TAG, "onProgress command : ffmpeg ="+s);
                    listener.FFmpegExcuteProgress(s);
                }
                @Override
                public void onStart() {
                    Log.d(TAG, "Started command : ffmpeg ");
                }
                @Override
                public void onFinish() {
                    Log.d(TAG, "Finished command : ffmpeg ");

                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.printStackTrace();
        }
    }

}
