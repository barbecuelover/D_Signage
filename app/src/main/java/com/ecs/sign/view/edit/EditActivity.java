package com.ecs.sign.view.edit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.ecs.sign.R;
import com.ecs.sign.base.activity.BaseActivity;
import com.ecs.sign.base.common.CallBack;
import com.ecs.sign.base.common.Constant;
import com.ecs.sign.base.common.util.LogUtils;
import com.ecs.sign.model.DataManager;
import com.ecs.sign.model.room.info.TemplateInfo;
import com.ecs.sign.presenter.edit.EditContract;
import com.ecs.sign.presenter.edit.EditPresenter;
import com.ecs.sign.view.edit.adapter.SliderAdapter;
import com.ecs.sign.view.edit.adapter.WidgetAttrAdapter;
import com.ecs.sign.view.edit.adapter.WidgetTypeAdapter;
import com.ecs.sign.view.edit.bean.Widget;
import com.ecs.sign.view.edit.bean.WidgetHelper;
import com.ecs.sign.view.edit.fragment.CanvasFragment;
import com.ecs.sign.view.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.ecs.sign.base.common.Constant.TEMPLATE_INDEX;

public class EditActivity extends BaseActivity<EditContract.EditActivityPresenter> implements View.OnClickListener ,EditContract.EditView {

    private static final String TAG ="EditActivity";

    @BindView(R.id.btn_edit_page_setting)
    ImageButton btnSetting;
    @BindView(R.id.btn_edit_project_page_add)
    ImageButton btnAdd;
    @BindView(R.id.btn_edit_project_play)
    ImageButton btnPlay;
    @BindView(R.id.btn_edit_page_output)
    ImageButton btnExport;
    @BindView(R.id.recycler_edit_project_page)
    RecyclerView rycSlider;
    @BindView(R.id.recycler_edit_project_widget_attr)
    RecyclerView rycWidgetAttr;

    @BindView(R.id.container_widget_attr)
    LinearLayout widgetAttrContainer;

    @BindView(R.id.recycler_edit_project_widget_type)
    RecyclerView rycWidgetType;

    @BindView(R.id.edit_project_canvas_container)
    FrameLayout CanvasContainer;

//    NoScrollViewPager viewPager;

    int tempIndex;

    //用两个List防止数据混乱
    private List<Widget> widgetAttrList = new ArrayList<>();
    private List<Widget> widgetTypeList = new ArrayList<>();

    private WidgetTypeAdapter typeAdapter;
    private WidgetAttrAdapter attrAdapter;
    private SliderAdapter sliderAdapter;

    public TemplateInfo templateInfo;
    WidgetHelper widgetHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_edit;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new EditPresenter(DataManager.getInstance(getApplicationContext()));
    }

    @Override
    protected void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        rycSlider.setLayoutManager(linearLayoutManager);

        LinearLayoutManager widgetLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        rycWidgetAttr.setLayoutManager(widgetLayoutManager);

        LinearLayoutManager typeLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        rycWidgetType.setLayoutManager(typeLayoutManager);
    }


    @Override
    protected void initData() {
        widgetHelper = new WidgetHelper();
        attrAdapter = new WidgetAttrAdapter( widgetAttrList);
        rycWidgetAttr.setAdapter(attrAdapter);

        Intent intent = getIntent();
        tempIndex = intent.getIntExtra(TEMPLATE_INDEX, -1);
        //回调 initSliders
        mPresenter.getTemplate(tempIndex);


    }

    @Override
    protected void initEvent() {
        btnSetting.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnExport.setOnClickListener(this);
        //添加新view到canvas
        rycWidgetType.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                int type = widgetHelper.getWidgetType(position);
                mPresenter.addNewView2Canvas(type);
            }
        });
        //修改view 属性
        rycWidgetAttr.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                LogUtils.d(TAG,"onSimpleItemClick ="+position);
                int attrID = widgetHelper.getWidgetAttrID(position);
                LogUtils.d(TAG,"attrID ="+attrID);
                mPresenter.changeCurrentWidgetAttr(attrID);

            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_edit_project_page_add:
//                editMvpPresenter.addSliderPage();
                //新建页
                mPresenter.addNewSlider();
                break;
            case R.id.btn_edit_page_setting:
//                editMvpPresenter.showSlideSettingPopWindow(this,v);
                break;
            case R.id.btn_edit_project_play:
//                changeToPlayModel();
                break;
            case R.id.btn_edit_page_output:
//                ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                viewPager.setLayoutParams(layoutParams);
//                viewPager.requestLayout();
                break;
        }
    }



    /**
     * 每次进入Edit界面,只会执行一次。
     * 显示  slider 指示器
     * @param templateInfo
     */
    @Override
    public void initSliders(TemplateInfo templateInfo) {
        this.templateInfo = templateInfo;
        sliderAdapter = new SliderAdapter(templateInfo.getSliderInfoList());
        rycSlider.setAdapter(sliderAdapter);
        rycSlider.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                LogUtils.d(TAG,"onSimpleItemClick======slider position="+position);
                mPresenter.replaceCanvasAndResetStatus(position);

            }
        });
        rycSlider.addOnItemTouchListener(new OnItemLongClickListener() {
            @Override
            public void onSimpleItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                LogUtils.d(TAG,"onSimpleItemLongClick======slider position="+position);
                //长按 变成选中状态。 并弹出 slider 操作的属性。 // 复制，删除
                mPresenter.changeSelectSliderStatus(position);

            }
        });

    }

    /**
     * 显示控件属性列表
     */
    @Override
    public void showWidgetAttr(String  viewType) {
        LogUtils.d(TAG,"===========showWidgetAttr==============");
        if (rycWidgetType.getVisibility()!=View.GONE){
            rycWidgetType.setVisibility(View.GONE);
        }
        if (widgetAttrContainer.getVisibility()!=View.VISIBLE){
            widgetAttrContainer.setVisibility(View.VISIBLE);
        }
        this.widgetAttrList.clear();
        List<Widget> widgetList =widgetHelper.getWidgetList(viewType);
        this.widgetAttrList.addAll(widgetList);
        attrAdapter.notifyDataSetChanged();
    }

    /**
     * 显示 控件类型列表
     * @param first  是否初始化
     */
    @Override
    public void showWidgetType(boolean first) {
        /**
         * Type list 数据不会变，所以只有第一次需要 赋值。
         */
        if (first){
            LogUtils.d(TAG,"===========showWidgetType==============");
            List<Widget> widgetList =widgetHelper.getWidgetList(Constant.VIEW_TYPE);
            widgetTypeList.addAll(widgetList);
            typeAdapter = new WidgetTypeAdapter(widgetTypeList);
            rycWidgetType.setAdapter(typeAdapter);
        }

        if (rycWidgetType.getVisibility()!=View.VISIBLE){
            rycWidgetType.setVisibility(View.VISIBLE);
        }

        if (widgetAttrContainer.getVisibility()!=View.GONE){
            widgetAttrContainer.setVisibility(View.GONE);
        }

    }


    @Override
    public void changeToPlayModel(boolean play) {
        /**
         * 当处于播放模式时 ，要隐藏 底部Widget RecyclerView
         * 当处于暂停模式时 ，要显示。
         */
        if (play){
            if (rycWidgetAttr.getVisibility() != View.GONE) {
                rycWidgetAttr.setVisibility(View.GONE);
            }
        }else {
            if (rycWidgetAttr.getVisibility() != View.VISIBLE) {
                rycWidgetAttr.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void replaceCanvas(CanvasFragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.edit_project_canvas_container,fragment);
//        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void refreshSliderStatus() {
        sliderAdapter.notifyDataSetChanged();
    }

    public void resetCanvas() {
        mPresenter.resetStatus();
    }


    @Override
    public void onBackPressed() {

//        showLoading();
        mPresenter.updateTemplate( new CallBack() {
            @Override
            public void onSucceed() {
                LogUtils.d(TAG," updateTemplate db onSucceed");
//                hideLoading();
                finish();
            }

            @Override
            public void onFailed() {
                LogUtils.d(TAG," updateTemplate db onFailed");
//                hideLoading();
                finish();
            }
        });
    }



}
