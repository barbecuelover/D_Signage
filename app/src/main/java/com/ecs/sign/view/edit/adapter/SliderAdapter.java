package com.ecs.sign.view.edit.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ecs.sign.R;
import com.ecs.sign.model.room.info.SliderInfo;

import java.io.File;
import java.util.List;

/**
 * @author zw
 * @time 2019/11/29
 * @description
 */
public class SliderAdapter  extends BaseQuickAdapter<SliderInfo, BaseViewHolder> {

    public SliderAdapter(@Nullable List<SliderInfo> data) {
        super(R.layout.item_slider_list,data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SliderInfo sliderInfo) {
//
//        if (sliderInfo.getPreviewBitmap()!=null){
//            helper.setImageBitmap(R.id.img_slider_preview,sliderInfo.getPreviewBitmap());
//        }
//        else
        if (sliderInfo.getPreviewUrl()!=null){
            File file = new File(sliderInfo.getPreviewUrl());
            if (file.exists()){
                Glide.with(mContext).load(sliderInfo.getPreviewUrl())
                        .skipMemoryCache(true) // 不使用内存缓存
                        .diskCacheStrategy(DiskCacheStrategy.NONE) // 不使用磁盘缓存
                        .into( (ImageView) helper.getView(R.id.img_slider_preview));
            }
        }
        if (sliderInfo.isSelected()){
            helper.setVisible(R.id.img_slider_selected_status,true);
        }else {
            helper.setVisible(R.id.img_slider_selected_status,false);
        }
        if (sliderInfo.isCurrent()){
            helper.setBackgroundRes(R.id.fl_edit_content,R.drawable.shape_view_selected_color);
        }else {
            helper.setBackgroundRes(R.id.fl_edit_content,R.drawable.shape_view_nocolor);
        }

    }
}
