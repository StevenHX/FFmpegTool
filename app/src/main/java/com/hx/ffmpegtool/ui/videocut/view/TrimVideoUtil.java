package com.hx.ffmpegtool.ui.videocut.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.hx.ffmpegtool.app.ToolApp;
import com.hx.steven.callback.SingleCallback;
import com.hx.steven.util.TimeUtil;
import com.hx.steven.util.UnitConverter;
import com.hx.steven.util.WindowUtil;

/**
 * Author：J.Chou
 * Date：  2016.08.01 2:23 PM
 * Email： who_know_me@163.com
 * Describe:
 */
public class TrimVideoUtil {

  private static final String TAG = TrimVideoUtil.class.getSimpleName();
  public static final long MIN_SHOOT_DURATION = 3000L;// 最小剪辑时间3s
  public static final int VIDEO_MAX_TIME = 30;// 10秒
  public static final long MAX_SHOOT_DURATION = VIDEO_MAX_TIME * 1000L;//视频最多剪切多长时间10s
  public static final int MAX_COUNT_RANGE = 10;  //seekBar的区域内一共有多少张图片
  private static final int SCREEN_WIDTH_FULL = WindowUtil.getScreenWidth(ToolApp.getInstance());
  public static final int RECYCLER_VIEW_PADDING = UnitConverter.dpToPx(35);
  public static final int VIDEO_FRAMES_WIDTH = SCREEN_WIDTH_FULL - RECYCLER_VIEW_PADDING * 2;
  private static final int THUMB_WIDTH = (SCREEN_WIDTH_FULL - RECYCLER_VIEW_PADDING * 2) / VIDEO_MAX_TIME;
  private static final int THUMB_HEIGHT = UnitConverter.dpToPx(50);

  public static void trim(Context context, String inputFile, String outputFile, long startMs, long endMs, final TrimVideoListener callback) {
//    final String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
//    final String outputName = "trimmedVideo_" + timeStamp + ".mp4";
//    outputFile = outputFile + outputName;

    String start = TimeUtil.convertSecondsToTime(startMs / 1000);
    String duration = TimeUtil.convertSecondsToTime((endMs - startMs) / 1000);
    //String start = String.valueOf(startMs);
    //String duration = String.valueOf(endMs - startMs);

    /** 裁剪视频ffmpeg指令说明：
     * ffmpeg -ss START -t DURATION -i INPUT -codec copy -avoid_negative_ts 1 OUTPUT
     -ss 开始时间，如： 00:00:20，表示从20秒开始；
     -t 时长，如： 00:00:10，表示截取10秒长的视频；
     -i 输入，后面是空格，紧跟着就是输入视频文件；
     -codec copy -avoid_negative_ts 1 表示所要使用的视频和音频的编码格式，这里指定为copy表示原样拷贝；
     INPUT，输入视频文件；
     OUTPUT，输出视频文件
     */
    //String cmd = "-ss " + start + " -t " + duration + " -accurate_seek" + " -i " + inputFile + " -codec copy -avoid_negative_ts 1 " + outputFile;

    //https://superuser.com/questions/138331/using-ffmpeg-to-cut-up-video
    String cmd = "-ss " + start + " -i " + inputFile + " -ss " + start + " -t " + duration + " -c copy " + outputFile;
    String[] command = cmd.split(" ");
    try {
      final String tempOutFile = outputFile;
      FFmpeg.getInstance(context).execute(command, new ExecuteBinaryResponseHandler() {

        @Override
        public void onSuccess(String s) {
          callback.onFinishTrim(tempOutFile);
        }

        @Override
        public void onStart() {
          callback.onStartTrim();
        }
      });
    } catch (FFmpegCommandAlreadyRunningException e) {
      e.printStackTrace();
    }
  }

  public static void backgroundShootVideoThumb(final Context context, final Uri videoUri, final int totalThumbsCount, final long startPosition,
                                               final long endPosition, final SingleCallback<Bitmap, Integer> callback) {
   ToolApp.getInstance().mostExecutor.execute(()->{
     try {
       MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
       mediaMetadataRetriever.setDataSource(context, videoUri);
       // Retrieve media data use microsecond
       long interval = (endPosition - startPosition) / (totalThumbsCount - 1);
       for (long i = 0; i < totalThumbsCount; ++i) {
         long frameTime = startPosition + interval * i;
         Bitmap bitmap = mediaMetadataRetriever.getFrameAtTime(frameTime * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
         try {
           bitmap = Bitmap.createScaledBitmap(bitmap, THUMB_WIDTH, THUMB_HEIGHT, false);
         } catch (final Throwable t) {
           t.printStackTrace();
         }
         callback.onSingleCallback(bitmap, (int) interval);
       }
       mediaMetadataRetriever.release();
     } catch (final Throwable e) {
       Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
     }
   });
  }


}
