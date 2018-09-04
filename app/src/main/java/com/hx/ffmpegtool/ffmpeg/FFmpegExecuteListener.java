package com.hx.ffmpegtool.ffmpeg;

/**
 * ffmpeg执行命令接口
 * Created by huangxiao on 2018/7/6.
 */

public interface FFmpegExecuteListener {
    void FFmpegExcuteSuccess(String s);
    void FFmpegExcuteFail(String s);
    void FFmpegExcuteProgress(String s);
}
