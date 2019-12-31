package com.ecs.sign.view.edit.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ecs.sign.R;
import com.ecs.sign.view.edit.bean.Widget;

import java.util.List;


public class WidgetAttrAdapter extends BaseQuickAdapter <Widget,BaseViewHolder>{

    public WidgetAttrAdapter( @Nullable List<Widget> data) {
        super(R.layout.item_widget_attr,data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Widget widget) {
        helper.setText(R.id.widget_attr_name, widget.getName())
                .setImageResource(R.id.widget_attr_icon,widget.getDrawableRes());


    }
}
