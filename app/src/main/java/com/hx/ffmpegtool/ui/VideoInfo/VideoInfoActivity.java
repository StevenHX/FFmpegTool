package com.hx.ffmpegtool.ui.VideoInfo;

import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import com.dmcbig.mediapicker.PickerActivity;
import com.dmcbig.mediapicker.PickerConfig;
import com.dmcbig.mediapicker.entity.Media;
import com.hx.ffmpegtool.R;
import com.hx.ffmpegtool.ffmpeg.FFmpegExecuteListener;
import com.hx.ffmpegtool.ffmpeg.FFmpegManer;
import com.hx.steven.activity.BaseActivity;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoInfoActivity extends BaseActivity {
    @BindView(R.id.video_info_button)
    TextView mVideoInfoButton;
    @BindView(R.id.video_info_content)
    TextView mVideoInfoContent;
    ArrayList<Media> select;//相册选中数据
    private StringBuilder mStringBuilder;
    {
        setEnableMultiple(false);
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_video_info;
    }

    @OnClick(R.id.video_info_button)
    public void onViewClicked() {
        Intent intent = new Intent(this, PickerActivity.class);
        intent.putExtra(PickerConfig.SELECT_MODE, PickerConfig.PICKER_VIDEO);//default image and video (Optional)
        long maxSize = 188743680L;//long long long
        intent.putExtra(PickerConfig.MAX_SELECT_SIZE, maxSize); //default 180MB (Optional)
        intent.putExtra(PickerConfig.MAX_SELECT_COUNT, 1);  //default 40 (Optional)
        intent.putExtra(PickerConfig.DEFAULT_SELECTED_LIST, select); // (Optional)
        this.startActivityForResult(intent, 200);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == PickerConfig.RESULT_CODE) {
            select = data.getParcelableArrayListExtra(PickerConfig.EXTRA_RESULT);
            for (Media media : select) {
                Log.i("media", media.path);
                Log.i("media", "s:" + media.size);
                mStringBuilder = new StringBuilder();
                mStringBuilder.append("视频大小："+media.size/1024/1024+"M \n");
                FFmpegManer.getInstance().runFFmpegCommad("-i " + media.path, new FFmpegExecuteListener() {
                    @Override
                    public void FFmpegExcuteSuccess(String s) {

                    }

                    @Override
                    public void FFmpegExcuteFail(String s) {
                       getVideoTime(s);
                    }

                    @Override
                    public void FFmpegExcuteProgress(String s) {

                    }
                });
            }
        }
    }

    /**
     * 获取视频
     */
    public void getVideoTime(String errorstr) {
        //从视频信息中解析时长
        String regexDuration = "Duration: (.*?), start: (.*?), bitrate: (\\d*) kb\\/s";
        Pattern pattern = Pattern.compile(regexDuration);
        Matcher m = pattern.matcher(errorstr);
        if (m.find()) {
            int time = getTimelen(m.group(1));
            mStringBuilder.append("视频时长：" + time + "s ,\n 开始时间：" + m.group(2) + ", \n比特率：" + m.group(3) + "kb/s\n");
            System.out.println("视频时长：" + time + "s , 开始时间：" + m.group(2) + ", 比特率：" + m.group(3) + "kb/s");
        }

        String regexVideo = "Video: (.*?), (.*?), (.*?),(.*?),(.*?)[,\\s]";
        pattern = Pattern.compile(regexVideo);
        m = pattern.matcher(errorstr);
        if (m.find()) {
            mStringBuilder.append("编码格式：" + m.group(1) + ",\n 视频格式：" + m.group(2) +  m.group(3)+",\n 分辨率：" + m.group(4));
            System.out.println("编码格式：" + m.group(1) + ", 视频格式：" + m.group(2) + ", 分辨率：" + m.group(3) + "kb/s");
        }

        mVideoInfoContent.setText(mStringBuilder.toString());
    }

    // 格式:"00:00:10.68"
    private int getTimelen(String timelen) {
        int min = 0;
        String strs[] = timelen.split(":");
        if (strs[0].compareTo("0") > 0) {
            // 秒
            min += Integer.valueOf(strs[0]) * 60 * 60;
        }
        if (strs[1].compareTo("0") > 0) {
            min += Integer.valueOf(strs[1]) * 60;
        }
        if (strs[2].compareTo("0") > 0) {
            min += Math.round(Float.valueOf(strs[2]));
        }
        return min;
    }
}
