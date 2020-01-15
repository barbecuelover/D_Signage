package com.ecs.sign.presenter.edit;

import android.os.Bundle;

import com.ecs.sign.base.common.CallBack;
import com.ecs.sign.base.common.Constant;
import com.ecs.sign.base.common.util.DataKeeper;
import com.ecs.sign.base.common.util.FileUtils;
import com.ecs.sign.base.common.util.LogUtils;
import com.ecs.sign.base.common.util.ZipUtils;
import com.ecs.sign.base.presenter.RxBasePresenter;
import com.ecs.sign.model.DataManager;
import com.ecs.sign.model.room.info.SliderInfo;
import com.ecs.sign.model.room.info.TemplateInfo;
import com.ecs.sign.socket.transport.SocketFileClientCallBack;
import com.ecs.sign.socket.transport.TransportFileHelper;
import com.ecs.sign.view.edit.fragment.CanvasFragment;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zw
 * @time 2019/11/27
 * @description
 */
public class EditPresenter extends RxBasePresenter<EditContract.EditView> implements EditContract.EditActivityPresenter {

    int currentCanvas = -1;
    TemplateInfo templateInfo;
    boolean isNew = false;

    public Map<SliderInfo, CanvasFragment> mapList = new HashMap<>();


    public EditPresenter(DataManager dataManager) {
        super(dataManager);

    }


    @Override
    public void changeBottomWidgetList(String viewType) {

    }

    /**
     * 获取当前的 Template ，无则是新建。 并显示第一个slider
     *
     * @param index
     */
    @Override
    public void getTemplate(int index) {
        if (index == -1) {
            //新建
            isNew = true;
            long tempId = System.currentTimeMillis();
            templateInfo = new TemplateInfo(tempId);
            templateInfo.setName("New Template");
            SliderInfo sliderInfo = new SliderInfo(tempId + 200, tempId);
            templateInfo.addSlider(sliderInfo);
            CanvasFragment canvas = new CanvasFragment();
            mapList.put(sliderInfo, canvas);
        } else {
            isNew = false;
            templateInfo = dataManager.getTemplate(index);
            LogUtils.d("Template ：slider size = " + templateInfo.getSliderInfoList().size());
            for (SliderInfo sliderInfo : templateInfo.getSliderInfoList()) {
                CanvasFragment canvasFragment = new CanvasFragment();
                mapList.put(sliderInfo, canvasFragment);
            }
        }

        //初始化 Slider
        view.initSliders(templateInfo);
        //初始化 widget type list
        view.showWidgetType(true);
        //显示第一个canvas
        replaceCanvasAndResetStatus(0);

    }

    /**
     * 创建新的 slider 导航页。 和canvas 。并显示
     */
    @Override
    public void addNewSlider() {
        //创建新的slider,并刷新列表
        SliderInfo sliderInfo = new SliderInfo(System.currentTimeMillis(), templateInfo.getId());
        templateInfo.addSlider(sliderInfo);
        CanvasFragment canvasFragment = new CanvasFragment();
        mapList.put(sliderInfo, canvasFragment);

        replaceCanvasAndResetStatus(templateInfo.getSliderInfoList().size() - 1);

    }

    /**
     * 替换 canvas 中的fragment 并重置界面所有状态
     *
     * @param position
     */
    public void replaceCanvasAndResetStatus(int position) {
        LogUtils.d("replaceCanvasAndResetStatus position=" + position);
        if (currentCanvas != position) {

            if (currentCanvas >= 0) {
                CanvasFragment oldCanvas = mapList.get(templateInfo.getSliderInfoList().get(currentCanvas));

                oldCanvas.saveCanvasInfo();
            }

            currentCanvas = position;
            CanvasFragment canvasFragment = mapList.get(templateInfo.getSliderInfoList().get(position));
            setCanvasIndex(canvasFragment);
            view.replaceCanvas(canvasFragment);

            resetStatus();
        }

    }

    @Override
    public void addNewView2Canvas(int viewType) {
        SliderInfo slider = templateInfo.getSliderInfoList().get(currentCanvas);
        CanvasFragment canvas = mapList.get(slider);
        if (canvas != null) {
            canvas.createNewView(viewType);
        }
    }

    /**
     * 改变被选中 slider的背景 ，显示 slider 可操作的 widget attr
     *
     * @param slider
     */
    @Override
    public void changeSelectSliderStatus(int slider) {
        List<SliderInfo> sliderInfoList = templateInfo.getSliderInfoList();
        currentCanvas = slider;
        resetSliderList();
        sliderInfoList.get(slider).setSelected(true);
        view.refreshSliderStatus();
        view.showWidgetAttr(Constant.VIEW_SLIDER);
    }

    /**
     * 重置当前template 所有状态。
     */
    @Override
    public void resetStatus() {
        resetSliderList();
        view.refreshSliderStatus();
        view.showWidgetType(false);
    }

    /**
     * 更改当前 view 或者 slider的属性， 或者 复制 删除 。
     *
     * @param attrID
     */
    @Override
    public void changeCurrentWidgetAttr(int attrID) {

        switch (attrID) {
            case Constant.ID_SLIDER_SET_TIME:

                break;

            case Constant.ID_SLIDER_DUPLICATE:
                duplicateSlider();
                break;

            case Constant.ID_SLIDER_DELETE:
                deleteSlider();
                break;

            default:
                //通知  当前canvasFragment ，对对应的view 做相应的操作。
                SliderInfo sliderInfo = templateInfo.getSliderInfoList().get(currentCanvas);
                CanvasFragment canvasFragment = mapList.get(sliderInfo);
                if (canvasFragment != null) {
                    canvasFragment.changeViewAttr(attrID);
                }
                break;

        }
    }


    @Override
    public void updateTemplate(CallBack callBack) {

        //先保存 当前CanvasFragment中的信息。
        SliderInfo currentSlider = templateInfo.getSliderInfoList().get(currentCanvas);
        CanvasFragment currentCanvas = mapList.get(currentSlider);
        if (currentCanvas != null) {
            currentCanvas.saveCanvasInfo();
        }

        //将url 修改， 图片另存为到 Template 所属目录。
        if (isNew) {
            dataManager.insertTemplate(templateInfo, new CallBack() {
                @Override
                public void onSucceed() {
                    isNew = false;
                    callBack.onSucceed();
                }

                @Override
                public void onFailed() {
                    callBack.onFailed();
                }
            });

        } else {
            dataManager.updateTemplate(templateInfo, callBack);
        }

    }

    /**
     * 导出模板 在播放端显示
     *
     * @param templateInfo
     */
    @Override
    public void transferTemplate(String ip, TemplateInfo templateInfo, SocketFileClientCallBack clientCallBack) {
        SliderInfo currentSlider = templateInfo.getSliderInfoList().get(currentCanvas);
        CanvasFragment currentCanvas = mapList.get(currentSlider);
        if (currentCanvas != null) {
            currentCanvas.saveCanvasInfo();
        }
        String tempPath = DataKeeper.getTemplatePath(templateInfo);
        FileUtils.writeStringToFile(templateInfo.toJsonString(), tempPath + "/temp.json");
        //需要打包的文件夾
        File resFile = new File(tempPath);
        //壓縮生成的文件
        File zipFile = new File(tempPath + ".zip");

        ZipUtils.zipFile(resFile, zipFile, new ZipUtils.ZipListener() {
            @Override
            public void zipProgress(int zipProgress) {
                LogUtils.e("zipProgress  :" + zipProgress);
            }
        });

        TransportFileHelper helper = new TransportFileHelper();
        helper.startTransport(ip, zipFile, clientCallBack);
    }


    /**
     * 复制 当前编辑页，并更新界面内容
     * //TODO
     */
    private void duplicateSlider() {
        SliderInfo currentSlider = templateInfo.getSliderInfoList().get(currentCanvas);


    }


    /**
     * 删除 一个编辑页。并更新当前 界面内容。
     */
    private void deleteSlider() {
        if (templateInfo.getSliderInfoList().size() == 1) {
            view.showToast("至少要保留一个编辑页");
        } else {
            SliderInfo sliderDel = templateInfo.getSliderInfoList().get(currentCanvas);
            templateInfo.getSliderInfoList().remove(sliderDel);
            mapList.remove(sliderDel);
            replaceCanvasAndResetStatus(0);
        }
    }


    /**
     * 删除slider的选中状态。重置当前current slider
     */
    private void resetSliderList() {
        List<SliderInfo> sliderInfoList = templateInfo.getSliderInfoList();
        for (int i = 0; i < sliderInfoList.size(); i++) {
            SliderInfo sliderInfo = sliderInfoList.get(i);
            if (currentCanvas == i) {
                sliderInfo.setCurrent(true);
            } else {
                sliderInfo.setCurrent(false);
            }
            sliderInfo.setSelected(false);
        }
    }

    /**
     * 为 canvas 设置 对应的 sliderInfo 在List<SliderInfo>的Index
     *
     * @param canvasFragment
     */
    private void setCanvasIndex(CanvasFragment canvasFragment) {
        Bundle args = new Bundle();
        args.putInt(Constant.SLIDER_INDEX, currentCanvas);
        canvasFragment.setArguments(args);
    }


}
