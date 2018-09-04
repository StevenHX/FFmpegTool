package com.hx.ffmpegtool.ui.main;

import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hx.ffmpegtool.R;
import com.hx.steven.activity.BaseActivity;
import com.hx.steven.component.VerticalViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    List<MainFragment> mMainFragments;//首页的fragments
    VerticalVpAdapter mVerticalVpAdapter;//首页adapter
    @BindView(R.id.main_vertical_vp)
    VerticalViewPager mMainVerticalVp;
    @BindView(R.id.main_root)
    RelativeLayout mMainRoot;
    @BindView(R.id.main_bg)
    ImageView mMainBg;
    @BindView(R.id.main_bg2)
    ImageView mMainBg2;

    {
        setEnableMultiple(false);
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        initFragments();
        MainBgAssist.getInstance().bindView(mMainBg, mMainBg2);
    }

    private void initFragments() {
        mMainFragments = new ArrayList<>();
        MainFragment mainFragmentOne = new MainFragment();
        mMainFragments.add(mainFragmentOne);
        mVerticalVpAdapter = new VerticalVpAdapter(getSupportFragmentManager(), mMainFragments);
        mMainVerticalVp.setAdapter(mVerticalVpAdapter);
        mMainVerticalVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_main;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainBgAssist.getInstance().clearAll();
    }
    @Override
    protected void onPause() {
        super.onPause();
        MainBgAssist.getInstance().stopChange();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainBgAssist.getInstance().startChange();
    }
}
