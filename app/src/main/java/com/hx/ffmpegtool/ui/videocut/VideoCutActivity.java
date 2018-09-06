package com.hx.ffmpegtool.ui.videocut;

import com.hx.ffmpegtool.R;
import com.hx.steven.activity.BaseActivity;
import com.hx.steven.component.SeekRangeBar;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoCutActivity extends BaseActivity {
    @BindView(R.id.videoCut_sb)
    SeekRangeBar mVideoCutSb;

    {
        setEnableMultiple(false);
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        mVideoCutSb.setEditable(true);
        mVideoCutSb.setOnSeekBarChangeListener(new SeekRangeBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekRangeBar seekBar, double progressLow, double progressHigh) {
                Logger.d("LOGCAT"+"低：" + progressLow + "高：" + progressHigh);
            }
        });
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_video_cut;
    }
}
