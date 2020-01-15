package com.ecs.sign.socket.scan;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ecs.sign.R;
import com.ecs.sign.model.room.info.SliderInfo;
import com.ecs.sign.socket.scan.ServerDeviceEntity;

import java.io.File;
import java.util.List;

/**
 * @author zw
 * @time 2019/1/14
 * @description
 */
public class ServerDeviceListAdapter extends BaseQuickAdapter<ServerDeviceEntity, BaseViewHolder> {

    public ServerDeviceListAdapter(@Nullable List<ServerDeviceEntity> data) {
        super(R.layout.item_socket_device, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ServerDeviceEntity deviceEntity) {
        int colorRes = deviceEntity.isCheck() ? Color.BLUE : Color.BLACK;
        helper.setTextColor(R.id.tv_device_name, colorRes)
                .setTextColor(R.id.tv_device_ip, colorRes)
                .setText(R.id.tv_device_name, deviceEntity.getDeviceName())
                .setText(R.id.tv_device_ip, deviceEntity.getDeviceIp())
                .addOnClickListener(R.id.rl_layout_item);
    }
}
