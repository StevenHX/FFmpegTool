package com.hx.ffmpegtool.ui.videoWaterMark;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dmcbig.mediapicker.PickerActivity;
import com.dmcbig.mediapicker.PickerConfig;
import com.dmcbig.mediapicker.entity.Media;
import com.hx.ffmpegtool.R;
import com.hx.ffmpegtool.app.CreateConstants;
import com.hx.ffmpegtool.app.ToolApp;
import com.hx.ffmpegtool.ffmpeg.FFmpegExecuteListener;
import com.hx.ffmpegtool.ffmpeg.FFmpegManer;
import com.hx.steven.activity.BaseActivity;
import com.hx.steven.component.WxActionBar;
import com.hx.steven.util.BitmapUtil;
import com.hx.steven.util.FileUtil;
import com.hx.steven.util.Mp4Util;
import com.hx.steven.util.StatusBarUtils;
import com.hx.steven.util.ToastUtil;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoWaterMarkActivity extends BaseActivity {

    @BindView(R.id.video_mark_bar)
    WxActionBar mVideoMarkBar;
    @BindView(R.id.video_mark_videopath)
    TextView mVideoMarkVideopath;
    @BindView(R.id.video_mark_videobtn)
    TextView mVideoMarkVideobtn;
    @BindView(R.id.video_mark_waterpath)
    TextView mVideoMarkWaterpath;
    @BindView(R.id.video_mark_waterbtn)
    TextView mVideoMarkWaterbtn;
    @BindView(R.id.video_mark_orition)
    TextView mVideoMarkOrition;
    @BindView(R.id.video_mark_orition_btn)
    TextView mVideoMarkOritionBtn;
    @BindView(R.id.video_mark_button)
    TextView mVideoMarkButton;
    ArrayList<Media> select = new ArrayList<>();//相册选中数据
    String outpath;

    {
        setEnableMultiple(false);
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.statusBarColor);
        mVideoMarkBar.setOnHeadClickListener(() -> {
            finish();
        });
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_video_water_mark;
    }

    @OnClick({R.id.video_mark_videobtn, R.id.video_mark_waterbtn, R.id.video_mark_orition_btn, R.id.video_mark_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.video_mark_videobtn:
                jumpToPickerActivity(PickerConfig.PICKER_VIDEO);
                break;
            case R.id.video_mark_waterbtn:
                jumpToPickerActivity(PickerConfig.PICKER_IMAGE);
                break;
            case R.id.video_mark_orition_btn:

                break;
            case R.id.video_mark_button:
                if (checkParams()) {
                    addWaterMark();
                }
                break;
        }
    }

    private void jumpToPickerActivity(int type) {
        if (type == PickerConfig.PICKER_VIDEO) {
            Intent intent = new Intent(this, PickerActivity.class);
            intent.putExtra(PickerConfig.SELECT_MODE, PickerConfig.PICKER_VIDEO);//default image and video (Optional)
            long maxSize = 188743680L;//long long long
            intent.putExtra(PickerConfig.MAX_SELECT_SIZE, maxSize); //default 180MB (Optional)
            intent.putExtra(PickerConfig.MAX_SELECT_COUNT, 1);  //default 40 (Optional)
            intent.putExtra(PickerConfig.DEFAULT_SELECTED_LIST, new ArrayList<>()); // (Optional)
            this.startActivityForResult(intent, 200);
        } else if (type == PickerConfig.PICKER_IMAGE) {
            Intent intent = new Intent(this, PickerActivity.class);
            intent.putExtra(PickerConfig.SELECT_MODE, PickerConfig.PICKER_IMAGE);//default image and video (Optional)
            long maxSize = 188743680L;//long long long
            intent.putExtra(PickerConfig.MAX_SELECT_SIZE, maxSize); //default 180MB (Optional)
            intent.putExtra(PickerConfig.MAX_SELECT_COUNT, 1);  //default 40 (Optional)
            intent.putExtra(PickerConfig.DEFAULT_SELECTED_LIST, new ArrayList<>()); // (Optional)
            this.startActivityForResult(intent, 201);
        }
    }

    private boolean checkParams() {
        outpath = CreateConstants.PHOTO_ALBUM_PATH + System.currentTimeMillis() + ".mp4";
        String videoPath = mVideoMarkVideopath.getText().toString().trim();
        String waterPath = mVideoMarkWaterpath.getText().toString().trim();
        String oritation = mVideoMarkOrition.getText().toString().trim();
        if (!TextUtils.isEmpty(videoPath) && !TextUtils.isEmpty(waterPath) && !TextUtils.isEmpty(oritation)) {
            return true;
        } else if (TextUtils.isEmpty(videoPath)) {
            ToastUtil.showToast(this, "视频路径为空！");
        } else if (TextUtils.isEmpty(waterPath)) {
            ToastUtil.showToast(this, "水印图片路径为空！");
        } else if (TextUtils.isEmpty(oritation)) {
            ToastUtil.showToast(this, "位置选择为空！");
        }
        return false;
    }

    private void addWaterMark() {
        String cmd = getFFmpegCmd();
        showProgressDialog("添加中...");
        ToolApp.getInstance().mostExecutor.execute(() -> {
            FFmpegManer.getInstance().runFFmpegCommad(cmd, new FFmpegExecuteListener() {
                @Override
                public void FFmpegExcuteSuccess(String s) {
                    Logger.d("success");
                    dismissProgressDialog();
                    FileUtil.scanFile(getApplicationContext(), outpath);
                    ToastUtil.showToast(VideoWaterMarkActivity.this, "添加成功！");
                }

                @Override
                public void FFmpegExcuteFail(String s) {
                    Logger.d("fail");
                    dismissProgressDialog();
                    ToastUtil.showToast(VideoWaterMarkActivity.this, "添加失败！");
                }

                @Override
                public void FFmpegExcuteProgress(String s) {

                }
            });
        });
    }

    private String getFFmpegCmd() {
        StringBuilder stringBuilder = new StringBuilder();
        String videoPath = mVideoMarkVideopath.getText().toString().trim();
        String waterPath = mVideoMarkWaterpath.getText().toString().trim();
        String oritation = mVideoMarkOrition.getText().toString().trim();
        float[] wh = getWaterPicWH(videoPath, waterPath);
        stringBuilder.append("-i ").append(videoPath).append(" -i ").append(waterPath)
                .append(" -filter_complex ")
                .append("[0:v]scale=trunc(iw/2)*2:trunc(ih/2)*2[crop];")
                .append("[1:v]scale=").append(wh[0]).append(":").append(wh[1]).append("[s];")
                .append("[crop][s]overlay=");
        if (TextUtils.equals(oritation, "left_bottom")) {
            stringBuilder.append("0:main_h-overlay_h");
        } else if (TextUtils.equals(oritation, "left_top")) {

        } else if (TextUtils.equals(oritation, "right_top")) {

        } else if (TextUtils.equals(oritation, "right_bottom")) {

        }
        stringBuilder.append(" -preset ultrafast -c:a copy ").append(outpath);
        Logger.d("cmd=" + stringBuilder.toString());
        return stringBuilder.toString();
    }

    private float[] getWaterPicWH(String videoPath, String waterPath) {
        float videoWidth = Mp4Util.getVideoWidthHeight(videoPath)[0];
        float videoHeight = Mp4Util.getVideoWidthHeight(videoPath)[1];
        float waterWidth;
        float waterHeight;
        float waterRatio = BitmapUtil.getBitmapRatio(waterPath);
        if (videoWidth > videoHeight) {
            waterWidth = videoHeight / 4;
        } else {
            waterWidth = videoWidth / 4;
        }
        waterHeight = waterWidth * waterRatio;
        return new float[]{waterWidth, waterHeight};
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        select.clear();
        if (requestCode == 200 && resultCode == PickerConfig.RESULT_CODE) {
            select = data.getParcelableArrayListExtra(PickerConfig.EXTRA_RESULT);
            for (Media media : select) {
                Log.i("media", media.path);
                Log.i("media", "s:" + media.size);
                mVideoMarkVideopath.setText(media.path);
                mVideoMarkVideopath.setEnabled(true);
            }
        } else if (requestCode == 201 && resultCode == PickerConfig.RESULT_CODE) {
            select = data.getParcelableArrayListExtra(PickerConfig.EXTRA_RESULT);
            for (Media media : select) {
                Log.i("media", media.path);
                Log.i("media", "s:" + media.size);
                mVideoMarkWaterpath.setText(media.path);
                mVideoMarkWaterpath.setEnabled(true);
            }
        }
    }
}
