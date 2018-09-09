package com.hx.ffmpegtool.app;

import android.os.Environment;

import java.io.File;

public class CreateConstants {
    //根目录
    public static final String ROOT_DIR;

    static {
        ROOT_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +
                "Android" + File.separator + "data" + File.separator + "com.hx.ffmpegtool"
                + File.separator + "Files";
    }


    //相册路径
    public static String PHOTO_ALBUM_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "DCIM" + File.separator + "Camera" + File.separator;
    public static String TEMP_PATH = ROOT_DIR + File.separator + "Temp" + File.separator;
}
