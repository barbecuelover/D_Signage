package com.ecs.sign.model.room.info;


import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;


import com.ecs.sign.base.common.util.DataKeeper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 1. Slider 删除 ，Slider 对应的view  全部删除。
 * 2. Slider 更新 ，Slider 对应的view  全部删除。 然后在View 表中 重新添加
 *
 */
@Entity(tableName = "slider", indices = {@Index("templateId")}, foreignKeys = @ForeignKey(entity = TemplateInfo.class, parentColumns = "id", childColumns = "templateId", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE))
public class SliderInfo implements Serializable {

    /**
     * ID 为 slider创建的时间戳
     */
    @PrimaryKey()
    private long id;

    private long templateId;
    private int playTime = 10 *1000; //s
    private String previewUrl;  //每页的预览图
    private String backgroundUrl;


    @Ignore
    private Bitmap previewBitmap;

    //长按slider导航页变成被选中状态，弹出底部slider可操作属性。 内部出现 蓝色斜杠区域。
    @Ignore
    private boolean selected;

    @Ignore
    private boolean current;

    public Bitmap getPreviewBitmap() {
        return previewBitmap;
    }

    public void setPreviewBitmap(Bitmap previewBitmap) {
        this.previewBitmap = previewBitmap;
    }

    //代表canvas 是否是 当前的 slider导航页 对应的。 外边框变蓝。
    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }



    public SliderInfo() {
    }


    @Ignore
    public SliderInfo(long id, long templateId) {
        this.id = id;
        this.templateId = templateId;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Ignore
    private List<ViewInfo> views = new ArrayList<>();

    public void  addView(ViewInfo viewInfo){
        if (!views.contains(viewInfo)){
            views.add(viewInfo);
        }
    }

    public void addAllViews(List<ViewInfo> viewInfoList){
        views.clear();
        views.addAll(viewInfoList);
    }

    public List<ViewInfo> getViews() {
        return views;
    }

    public void removeAllViews(){
        views.clear();
    }


    public Map<String,Object> createMap(){
        Map<String ,Object> viewMap = new HashMap<>();
        viewMap.put("sliderId",id);
        viewMap.put("playTime",playTime);
        //  /storage/emulated/0/signage/temp_1573805992443/slider_1573805992457/view_1573806016973.mp4
        viewMap.put("background", DataKeeper.fileRootPath+"temp_"+templateId+"/slider_"+id+"/background_"+id+".jpg");
        List<Map<String,Object>> list = new ArrayList<>();
        for (int i = 0; i< views.size(); i++){
            list.add(views.get(i).createMap());
        }
        viewMap.put("views",list);
        return viewMap;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(long templateId) {
        this.templateId = templateId;
    }

    public int getPlayTime() {
        return playTime;
    }

    public void setPlayTime(int playTime) {
        this.playTime = playTime;
    }

}
