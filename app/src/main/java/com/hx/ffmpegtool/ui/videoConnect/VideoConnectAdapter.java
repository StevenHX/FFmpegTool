package com.hx.ffmpegtool.ui.videoConnect;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.hx.ffmpegtool.R;
import com.hx.steven.adapter.CommonViewHolder;

import java.util.List;

/**
 * Created by Steven on 2018/9/8.
 */

public class VideoConnectAdapter extends RecyclerView.Adapter<CommonViewHolder>{

    private List<String> videoPaths;
    private Context context;
    private ImageView imageIv;
    private ImageView closeIv;
    private ItemClickListener itemClickListener;

    public VideoConnectAdapter(List<String> videoPaths, Context context,ItemClickListener itemClickListener) {
        this.videoPaths = videoPaths;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    public void update(){
        notifyDataSetChanged();
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.videoconnect_item, parent, false);
        return CommonViewHolder.create(view);
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position) {
        imageIv = holder.getView(R.id.video_connect_item_image);
        closeIv = holder.getView(R.id.video_connect_item_close);
        closeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.OnClick(position);
            }
        });

        Uri mediaUri = Uri.parse("file://" + videoPaths.get(position));
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)	//加载成功之前占位图
                .error(R.drawable.ic_launcher_background)	//加载错误之后的错误图
                //指定图片的缩放类型为fitCenter （等比例缩放图片，宽或者是高等于ImageView的宽或者是高。）
                .fitCenter()
                //指定图片的缩放类型为centerCrop （等比例缩放图片，直到图片的狂高都大于等于ImageView的宽度，然后截取中间的显示。）
                .centerCrop()
                .skipMemoryCache(true)	//跳过内存缓存
                .diskCacheStrategy(DiskCacheStrategy.ALL)	//缓存所有版本的图像
                .diskCacheStrategy(DiskCacheStrategy.NONE)	//跳过磁盘缓存
                .diskCacheStrategy(DiskCacheStrategy.DATA)	//只缓存原来分辨率的图片
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)	//只缓存最终的图片
                 ;
        Glide.with(context)
                .load(mediaUri)
                .apply(options)
                .into(imageIv);
    }

    @Override
    public int getItemCount() {
        return videoPaths==null?0:videoPaths.size();
    }
}

