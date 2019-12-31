package com.ecs.sign.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.ecs.sign.R;
import com.ecs.sign.base.activity.BaseActivity;
import com.ecs.sign.base.common.CallBack;
import com.ecs.sign.base.common.util.LogUtils;
import com.ecs.sign.model.DataManager;
import com.ecs.sign.model.room.info.TemplateInfo;
import com.ecs.sign.presenter.main.MainContract;
import com.ecs.sign.presenter.main.MainPresenter;
import com.ecs.sign.view.edit.EditActivity;
import com.ecs.sign.view.main.adapter.TemplateAdapter;
import com.ecs.sign.view.main.window.CommentWindow;
import com.ecs.sign.view.main.window.HomeWindowHelper;
import com.ecs.sign.view.main.window.ProjectInfoWindow;
import com.ecs.sign.view.main.window.RenameWindow;
import com.ecs.sign.view.main.window.TempMoreWindow;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.ecs.sign.base.common.Constant.TEMPLATE_INDEX;

public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.MainView, View.OnClickListener {

    @BindView(R.id.btn_info)
    ImageButton btnInfo;
    @BindView(R.id.btn_new_project)
    ImageButton btnNewTemp;
    @BindView(R.id.group_should_gone)
    Group placeHolder;
    @BindView(R.id.recycler_project_list)
    RecyclerView tempRecycler;
    private RxPermissions rxPermissions;
    private TemplateAdapter templateAdapter;
    private List<TemplateInfo> templateInfoList = new ArrayList<>();

    private HomeWindowHelper homeWindowHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new MainPresenter(DataManager.getInstance(getApplicationContext()));
    }

    @Override
    protected void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        tempRecycler.setLayoutManager(layoutManager);
        tempRecycler.setOverScrollMode(View.OVER_SCROLL_NEVER);
        templateAdapter = new TemplateAdapter(templateInfoList);
        tempRecycler.setAdapter(templateAdapter);
    }

    @Override
    protected void initData() {
        rxPermissions = new RxPermissions(this);
        //获取到template List 后会回调 showTemplateList
        homeWindowHelper = new HomeWindowHelper(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getTemplates();
    }

    @Override
    protected void initEvent() {
        btnInfo.setOnClickListener(this);
        btnNewTemp.setOnClickListener(this);
        templateAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                goEditActivity(position);
            }
        });
        tempRecycler.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                LogUtils.e("onSimpleItemChildClick position:" + position);
                TemplateInfo templateInfo = templateInfoList.get(position);
                if (view.getId() == R.id.btn_template_more) {
                    LogUtils.d("clicked template more info ");
                    homeWindowHelper.popupTemplateMoreWindow(new TempMoreWindow.OnButtonClicked() {
                        @Override
                        public void onRename() {
                            renameTemplate(templateInfo);
                        }

                        @Override
                        public void onDuplicate() {
                            showToast("onDuplicate");
                        }

                        @Override
                        public void onEvent() {
                            showToast("onEvent ");
                        }

                        @Override
                        public void onDelete() {
                            mPresenter.deleteTemplate(templateInfo);
                        }
                    });

                }
            }
        });
    }

    /**
     * 重命名 模板
     *
     * @param templateInfo
     */
    private void renameTemplate(TemplateInfo templateInfo) {
        homeWindowHelper.popupRenameWindow(new RenameWindow.OnNameChanged() {
            @Override
            public void onNameChanged(String name) {
                templateInfo.setName(name);
                mPresenter.renameTemplate(templateInfo);
            }
        });
    }


    @Override
    public void showPlaceHolder(boolean visible) {
        if (visible) {
            placeHolder.setVisibility(View.VISIBLE);
        } else {
            placeHolder.setVisibility(View.GONE);
        }
    }

    @Override
    public void showTemplateList(List<TemplateInfo> list) {
        templateInfoList.clear();
        templateInfoList.addAll(list);
        templateAdapter.notifyDataSetChanged();
    }

    @Override
    public void refreshTemplateList() {
        templateAdapter.notifyDataSetChanged();
    }

    @Override
    public void skipToEditActivity(int index) {
        Intent intent = new Intent(MainActivity.this, EditActivity.class);
        intent.putExtra(TEMPLATE_INDEX, index);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_new_project:
                mPresenter.requestPermissionsAndSkip(rxPermissions, new CallBack() {
                    @Override
                    public void onSucceed() {
                        goEditActivity(-1);
                    }

                    @Override
                    public void onFailed() {
                        showToast("Please check the permissions");
                    }
                });
                break;
            case R.id.btn_info:
                homeWindowHelper.popupProjectInfoWindow(new ProjectInfoWindow.OnButtonClicked() {
                    @Override
                    public void onComment() {
                        homeWindowHelper.popupCommentWindow(new CommentWindow.OnCommentSend() {
                            @Override
                            public void SendMsg(String msg) {
                                showToast("Comment Content :" + msg);
                            }
                        });
                    }

                    @Override
                    public void onSupport() {
                        showToast("onSupport");
                    }

                    @Override
                    public void onLicence() {
                        showToast("onLicence");
                    }
                });
                break;
        }


    }


    private void goEditActivity(int index) {


    }

}
