package com.ecs.sign.presenter.edit;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ecs.sign.R;
import com.ecs.sign.base.activity.BaseActivity;
import com.ecs.sign.base.common.Constant;
import com.ecs.sign.base.presenter.RxBasePresenter;
import com.ecs.sign.model.DataManager;
import com.ecs.sign.model.room.info.SliderInfo;
import com.ecs.sign.model.room.info.ViewInfo;
import com.ecs.sign.view.edit.EditActivity;
import com.ecs.sign.view.edit.view.DragTouchListener;
import com.ecs.sign.view.edit.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zw
 * @time 2019/11/27
 * @description
 */
public class CanvasPresenter extends RxBasePresenter<CanvasContract.CanvasView> implements CanvasContract.CanvasFragmentPresenter{

    private EditActivity activity;
    private SliderInfo sliderInfo;
    private ViewHelper viewHelper;

    private View  currentView;
    private List<View> viewList;

    public CanvasPresenter(DataManager dataManager) {
        super(dataManager);

        viewList = new ArrayList<>();
    }


    @Override
    public void initSlider(EditActivity editActivity,int sliderIndex) {
        this.activity = editActivity;
        sliderInfo = editActivity.templateInfo.getSliderInfoList().get(sliderIndex);

        view.setCanvasBackground(sliderInfo.getBackgroundUrl());
        List<ViewInfo> viewInfoList =  sliderInfo.getViews();



    }


    private void changeSelectedViewStatus(EditActivity editActivity) {
        for (int i = 0; i < viewList.size(); i++) {
            View view = viewList.get(i);
            view.setBackground(new ColorDrawable(Color.TRANSPARENT));
        }
        if (currentView!=null) {
            currentView.setBackground(editActivity.getResources().getDrawable(R.drawable.shape_view_selected, null));
        }
    }

    @Override
    public void createNewView(int viewType) {
        switch (viewType){


        }
    }

    @Override
    public void createNewTextView(String text, BaseActivity activity) {
        TextView textView = new TextView(activity);

        ViewInfo viewInfoNew = new ViewInfo();

        viewInfoNew.setType(Constant.VIEW_TEXT);
        viewInfoNew.setTextText(text);
        viewInfoNew.setSliderId(sliderInfo.getId());

        textView.setTag(R.id.view_info_tag, viewInfoNew);
        textView.setPadding(2, 2, 2, 2);
        textView.setBackgroundColor(Color.TRANSPARENT);
        textView.setText(text);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(300, 120);



    }
}
