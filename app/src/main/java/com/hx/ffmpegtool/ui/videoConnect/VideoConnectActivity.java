package com.hx.ffmpegtool.ui.videoConnect;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hx.ffmpegtool.R;
import com.hx.steven.activity.BaseActivity;
import com.hx.steven.component.WxActionBar;
import com.hx.steven.util.StatusBarUtils;

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
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_video_connect;
    }

    @OnClick({R.id.video_connect_videobtn, R.id.video_connect_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.video_connect_videobtn:
                break;
            case R.id.video_connect_button:
                break;
        }
    }
}
