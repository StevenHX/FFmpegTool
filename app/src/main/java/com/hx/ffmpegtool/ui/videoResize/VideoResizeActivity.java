package com.hx.ffmpegtool.ui.videoResize;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.dmcbig.mediapicker.PickerActivity;
import com.dmcbig.mediapicker.PickerConfig;
import com.dmcbig.mediapicker.entity.Media;
import com.hx.ffmpegtool.R;
import com.hx.ffmpegtool.app.CreateConstants;
import com.hx.ffmpegtool.app.ToolApp;
import com.hx.ffmpegtool.ffmpeg.FFmpegExecuteListener;
import com.hx.ffmpegtool.ffmpeg.FFmpegManer;
import com.hx.ffmpegtool.ui.videoConnect.VideoConnectActivity;
import com.hx.steven.activity.BaseActivity;
import com.hx.steven.component.WxActionBar;
import com.hx.steven.util.FileUtil;
import com.hx.steven.util.StatusBarUtils;
import com.hx.steven.util.ToastUtil;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoResizeActivity extends BaseActivity {

    @BindView(R.id.video_resize_bar)
    WxActionBar videoResizeBar;
    @BindView(R.id.video_resize_videopath)
    TextView videoResizeVideopath;
    @BindView(R.id.video_resize_videobtn)
    TextView videoResizeVideobtn;
    @BindView(R.id.video_resize_px_path)
    TextView videoResizePxPath;
    @BindView(R.id.video_resize_px_btn)
    TextView videoResizePxBtn;
    @BindView(R.id.video_resize_button)
    TextView videoResizeButton;
    ArrayList<Media> select = new ArrayList<>();//相册选中数据
    ArrayList<String> options1Items;//位置选项数据
    OptionsPickerView pvOptions;
    {
        setEnableMultiple(false);
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.statusBarColor);
        videoResizeBar.setOnHeadClickListener(() -> {
            finish();
        });
        initOritionPicker();
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_video_resize;
    }

    @OnClick({R.id.video_resize_videobtn, R.id.video_resize_px_btn, R.id.video_resize_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.video_resize_videobtn:
                Intent intent = new Intent(this, PickerActivity.class);
                intent.putExtra(PickerConfig.SELECT_MODE, PickerConfig.PICKER_VIDEO);//default image and video (Optional)
                long maxSize = 188743680L;//long long long
                intent.putExtra(PickerConfig.MAX_SELECT_SIZE, maxSize); //default 180MB (Optional)
                intent.putExtra(PickerConfig.MAX_SELECT_COUNT, 1);  //default 40 (Optional)
                intent.putExtra(PickerConfig.DEFAULT_SELECTED_LIST, new ArrayList<>()); // (Optional)
                this.startActivityForResult(intent, 200);
                break;
            case R.id.video_resize_px_btn:
                if (pvOptions != null)
                    pvOptions.show();
                break;
            case R.id.video_resize_button:
                resizeVideo();
                break;
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == PickerConfig.RESULT_CODE) {
            select = data.getParcelableArrayListExtra(PickerConfig.EXTRA_RESULT);
            for (Media media : select) {
                Log.i("media", media.path);
                Log.i("media", "s:" + media.size);
                videoResizeVideopath.setText(media.path);
                videoResizeVideopath.setEnabled(true);
            }
        }
    }

    public void initOritionPicker() {
        if (options1Items == null) {
            options1Items = new ArrayList<>();
            options1Items.add("2560:1440");
            options1Items.add("1920:1080");
            options1Items.add("1812:1080");
            options1Items.add("1280:800");
            options1Items.add("1280:720");
            options1Items.add("1196:720");
            options1Items.add("1184:720");
            options1Items.add("1024:768");
            options1Items.add("1024:600");
            options1Items.add("960:540");
            options1Items.add("854:480");
            options1Items.add("800:480");
            options1Items.add("480:320");
        }
        pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1);
                videoResizePxPath.setText(tx);
                videoResizePxPath.setEnabled(true);
            }
        }).build();
        pvOptions.setPicker(options1Items);
    }

    private void resizeVideo() {
        String outpath = CreateConstants.PHOTO_ALBUM_PATH + System.currentTimeMillis() + ".mp4";
        String videoPath  = videoResizeVideopath.getText().toString().trim();
        String resize = videoResizePxPath.getText().toString().trim();
        String cmd = "-i "+videoPath+" -vf scale="+resize+" "+outpath;
        showProgressDialog("视频转换中...");
        ToolApp.getInstance().mostExecutor.execute(() -> {
            FFmpegManer.getInstance().runFFmpegCommad(cmd, new FFmpegExecuteListener() {
                @Override
                public void FFmpegExcuteSuccess(String s) {
                    Logger.d("success");
                    dismissProgressDialog();
                    FileUtil.scanFile(getApplicationContext(), outpath);
                    runOnUiThread(()->{
                        ToastUtil.showToast(VideoResizeActivity.this, "转换成功！");
                    });
                }

                @Override
                public void FFmpegExcuteFail(String s) {
                    Logger.d("fail");
                    dismissProgressDialog();
                    runOnUiThread(()->{
                        ToastUtil.showToast(VideoResizeActivity.this, "准换失败！");
                    });
                }

                @Override
                public void FFmpegExcuteProgress(String s) {

                }
            });
        });
    }
}
