package com.hx.ffmpegtool.ui.main;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hx.ffmpegtool.R;
import com.hx.steven.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainFragment extends BaseFragment {
    @BindView(R.id.main_fg_rv)
    RecyclerView mMainFgRv;
    Unbinder unbinder;
    private MainFragmentAdapter mMainFragmentAdapter;

    {
        setEnableMultiple(false);
    }

    @Override
    protected int getContentId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initView(View view) {
        unbinder = ButterKnife.bind(this, view);
        initRecycleView();
    }

    private void initRecycleView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 2);
        mMainFgRv.setLayoutManager(gridLayoutManager);
        mMainFragmentAdapter = new MainFragmentAdapter(activity);
        mMainFgRv.setAdapter(mMainFragmentAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
