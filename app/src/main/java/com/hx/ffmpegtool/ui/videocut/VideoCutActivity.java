package com.hx.ffmpegtool.ui.videocut;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.dmcbig.mediapicker.PickerActivity;
import com.dmcbig.mediapicker.PickerConfig;
import com.dmcbig.mediapicker.entity.Media;
import com.hx.ffmpegtool.R;
import com.hx.ffmpegtool.ui.videocut.view.TrimVideoListener;
import com.hx.ffmpegtool.ui.videocut.view.VideoTrimmerView;
import com.hx.steven.activity.BaseActivity;
import com.hx.steven.util.FileUtil;
import com.hx.steven.util.ToastUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoCutActivity extends BaseActivity {

    @BindView(R.id.VideoTrimmerView)
    VideoTrimmerView mVideoTrimmerView;
    ArrayList<Media> select = new ArrayList<>();//相册选中数据

    {
        setEnableMultiple(false);
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        jumpToAlbum();
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_video_cut;
    }

    private void jumpToAlbum() {
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
            if(select.size()==0){
                finish();
            }
            for (Media media : select) {
                Log.i("media", media.path);
                Log.i("media", "s:" + media.size);
                initVideoTrimmerView(media.path);
            }
        }
    }

    private void initVideoTrimmerView(String path) {
        mVideoTrimmerView.initVideoByURI(Uri.parse(path));
        mVideoTrimmerView.setOnTrimVideoListener(new TrimVideoListener() {
            @Override
            public void onStartTrim() {
                showProgressDialog("正在裁剪视频");
            }

            @Override
            public void onFinishTrim(String url) {
                dismissProgressDialog();
                FileUtil.scanFile(getApplicationContext(),mVideoTrimmerView.getFinalPath());
                runOnUiThread(()->{
                    ToastUtil.showToast(VideoCutActivity.this, "裁剪视频完成");
                });
            }

            @Override
            public void onCancel() {
                mVideoTrimmerView.onDestroy();
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoTrimmerView.onVideoPause();
        mVideoTrimmerView.setRestoreState(true);
    }
}
