package com.hx.ffmpegtool.ui.main;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hx.ffmpegtool.R;
import com.hx.ffmpegtool.ui.videoInfo.VideoInfoActivity;
import com.hx.ffmpegtool.ui.videoWaterMark.VideoWaterMarkActivity;
import com.hx.steven.adapter.CommonViewHolder;
public class MainFragmentAdapter extends RecyclerView.Adapter<CommonViewHolder> {

    private Context mContext;
    private ImageView itemIv;
    private TextView itemTv;
    private String[] titles = new String[]{
            "视频信息", "视频图片添加水印", "视频裁剪", "视频合并", "视频旋转", "输出指定分辨率视频"
    };
    private int[] imgs = new int[]{
            R.mipmap.ic_main_01, R.mipmap.ic_main_02, R.mipmap.ic_main_03, R.mipmap.ic_main_04, R.mipmap.ic_main_05, R.mipmap.ic_main_06
    };

    public MainFragmentAdapter(Context context) {
        mContext = context;
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_main_item, parent, false);
        return CommonViewHolder.create(view);
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position) {
        itemIv = holder.getView(R.id.main_item_iv);
        itemTv = holder.getView(R.id.main_item_tv);
        itemIv.setImageResource(imgs[position]);
        itemTv.setText(titles[position]);
        holder.itemView.setOnClickListener((v) -> {
            switch (position){
                case 0:
                    ((MainActivity)mContext).launch(mContext, VideoInfoActivity.class);
                    break;
                case 1:
                    ((MainActivity)mContext).launch(mContext, VideoWaterMarkActivity.class);
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
            }
        });
    }

    @Override
    public int getItemCount() {
        return 6;
    }
}
