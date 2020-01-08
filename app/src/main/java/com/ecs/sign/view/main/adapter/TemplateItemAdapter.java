package com.ecs.sign.view.main.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ecs.sign.R;
import com.ecs.sign.model.room.info.SliderInfo;

import java.util.List;

public class TemplateItemAdapter extends BaseQuickAdapter <SliderInfo,BaseViewHolder>{

    public TemplateItemAdapter( @Nullable List<SliderInfo> sliderInfoList) {
        super(R.layout.recyclerview_home_template_item,sliderInfoList);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SliderInfo sliderInfo) {
        Glide.with(mContext).load(sliderInfo.getPreviewUrl())
                .skipMemoryCache(true) // 不使用内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE) // 不使用磁盘缓存
                .placeholder(R.mipmap.grid).into((ImageView) helper.getView(R.id.img_template_slider_preview));
    }


}
