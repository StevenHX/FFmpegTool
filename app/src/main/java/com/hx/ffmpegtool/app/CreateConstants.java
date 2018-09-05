package com.hx.ffmpegtool.app;

import android.os.Environment;

import java.io.File;

public class CreateConstants {
    //相册路径
    public static String PHOTO_ALBUM_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "DCIM" + File.separator + "Camera" + File.separator;

}
