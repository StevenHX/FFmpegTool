package com.hx.ffmpegtool.ui.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;
public class VerticalVpAdapter extends FragmentPagerAdapter {
    private List<MainFragment> datas;

    public VerticalVpAdapter(FragmentManager fm,List<MainFragment> datas) {
        super(fm);
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Fragment getItem(int position) {
        return datas.get(position);
    }
}
