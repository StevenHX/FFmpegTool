package com.hx.ffmpegtool.ui.videocut;

import com.hx.ffmpegtool.R;
import com.hx.steven.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoCutActivity extends BaseActivity {

    {
        setEnableMultiple(false);
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);

    }

    @Override
    protected int getContentId() {
        return R.layout.activity_video_cut;
    }
}
