package com.hx.ffmpegtool.ui.videoConnect;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.hx.ffmpegtool.ui.videoWaterMark.VideoWaterMarkActivity;
import com.hx.steven.activity.BaseActivity;
import com.hx.ffmpegtool.ui.videocut.view.SingleCallback;
import com.hx.steven.component.WxActionBar;
import com.hx.steven.util.FileUtil;
import com.hx.steven.util.StatusBarUtils;
import com.hx.steven.util.ToastUtil;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoConnectActivity extends BaseActivity {
    @BindView(R.id.video_connect_videobtn)
    TextView mVideoConnectVideobtn;
    @BindView(R.id.video_connect_recyclerView)
    RecyclerView mVideoConnectRecyclerView;
    @BindView(R.id.video_connect_button)
    TextView mVideoConnectButton;
    @BindView(R.id.video_connect_bar)
    WxActionBar mVideoConnectBar;
    private VideoConnectAdapter mVideoConnectAdapter;
    ArrayList<Media> select = new ArrayList<>();//相册选中数据
    List<String> paths = new ArrayList<>();

    {
        setEnableMultiple(false);
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.statusBarColor);
        mVideoConnectBar.setOnHeadClickListener(() -> {
            finish();
        });

        initRecycleView();
    }

    private void initRecycleView() {
        GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        mVideoConnectRecyclerView.setLayoutManager(layoutManager);
        mVideoConnectAdapter = new VideoConnectAdapter(paths, this, new ItemClickListener() {
            @Override
            public void OnClick(Object o) {
                paths.remove((int)o);
                mVideoConnectAdapter.update();
            }
        });
        mVideoConnectRecyclerView.setAdapter(mVideoConnectAdapter);
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_video_connect;
    }

    @OnClick({R.id.video_connect_videobtn, R.id.video_connect_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.video_connect_videobtn:
                Intent intent = new Intent(this, PickerActivity.class);
                intent.putExtra(PickerConfig.SELECT_MODE, PickerConfig.PICKER_VIDEO);//default image and video (Optional)
                long maxSize = 188743680L;//long long long
                intent.putExtra(PickerConfig.MAX_SELECT_SIZE, maxSize); //default 180MB (Optional)
                intent.putExtra(PickerConfig.MAX_SELECT_COUNT, 5);  //default 40 (Optional)
                intent.putExtra(PickerConfig.DEFAULT_SELECTED_LIST,select); // (Optional)
                this.startActivityForResult(intent, 200);
                break;
            case R.id.video_connect_button:
                String strcontent = getFileListText();
                FileUtil.writeTxtToFile(strcontent, CreateConstants.TEMP_PATH,"filelist.txt");
                connectVideo();
                break;
        }
    }

    private void connectVideo() {
        String path = CreateConstants.TEMP_PATH+"filelist.txt";
        if(!FileUtil.fileExists(path)) return;
        String outpath = CreateConstants.PHOTO_ALBUM_PATH + System.currentTimeMillis() + ".mp4";
        String cmd = "-y -f concat -safe 0 -i "+path+" -c copy "+outpath;
        showProgressDialog("拼接视频中...");
        ToolApp.getInstance().mostExecutor.execute(() -> {
            FFmpegManer.getInstance().runFFmpegCommad(cmd, new FFmpegExecuteListener() {
                @Override
                public void FFmpegExcuteSuccess(String s) {
                    Logger.d("success");
                    dismissProgressDialog();
                    FileUtil.scanFile(getApplicationContext(), outpath);
                    runOnUiThread(()->{
                        ToastUtil.showToast(VideoConnectActivity.this, "拼接成功！");
                    });
                }

                @Override
                public void FFmpegExcuteFail(String s) {
                    Logger.d("fail");
                    dismissProgressDialog();
                    runOnUiThread(()->{
                        ToastUtil.showToast(VideoConnectActivity.this, "拼接失败！");
                    });
                }

                @Override
                public void FFmpegExcuteProgress(String s) {

                }
            });
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == PickerConfig.RESULT_CODE) {
            select = data.getParcelableArrayListExtra(PickerConfig.EXTRA_RESULT);
            paths.clear();
            for (Media media : select) {
                Log.i("media", media.path);
                Log.i("media", "s:" + media.size);
                if(isMp4(media.path)){
                    paths.add(media.path);
                }
                mVideoConnectAdapter.update();
            }
        }
    }

    private String getFileListText(){
        StringBuilder builder = new StringBuilder();
        for (String path:paths) {
            builder.append("file \'"+path+"\'\r\n");
        }
        return builder.toString();
    }

    private boolean isMp4(String path){
       String type =  path.substring(path.length()-4,path.length());
       return TextUtils.equals(type,".mp4");
    }
}
