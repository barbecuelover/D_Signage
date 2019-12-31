package com.ecs.sign.view.main.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ecs.sign.R;
import com.ecs.sign.base.common.util.StringUtils;
import com.ecs.sign.model.room.info.TemplateInfo;

import java.util.List;


public class TemplateAdapter extends BaseQuickAdapter <TemplateInfo,BaseViewHolder>{

    public TemplateAdapter(@Nullable List<TemplateInfo> data) {
        super(R.layout.recyclerview_home_template,data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, TemplateInfo templateInfo) {
        helper.setText(R.id.template_create_time, StringUtils.timeToString(templateInfo.getId()))
                .setText(R.id.template_name,templateInfo.getName())
                .addOnClickListener(R.id.btn_template_more);

        RecyclerView recyclerView = helper.getView(R.id.recycler_slider_img_preview);
        //加载 slider 预览图片。
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext,RecyclerView.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        recyclerView.setLayoutManager(layoutManager);
        TemplateItemAdapter  itemAdapter = new TemplateItemAdapter(templateInfo.getSliderInfoList());
        recyclerView.setAdapter(itemAdapter);
    }
}
