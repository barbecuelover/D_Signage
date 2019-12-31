package com.ecs.sign.model.room.info;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 1. 模板删除，对应的Slider 和 Slider对应的view 全部删除。
 *
 *
 */
@Entity (tableName = "template" )
public class TemplateInfo implements Serializable {

    /**
     * ID 为创建template时的时间戳。
     */
    @PrimaryKey()
    private long id;
    private String name;
    private double width;
    private double height;
    private String proportion ;
    private String description;
    private long modifiedTime;

    @Ignore
    private List<SliderInfo> sliderPageList =new ArrayList<>();

    public TemplateInfo() {
    }

    @Ignore
    public TemplateInfo(long id) {
        this.id = id;
    }

    public void addSlider(SliderInfo sliderInfo){
        for (SliderInfo info: sliderPageList){
            if (info.getId() == sliderInfo.getId())
                return;
        }
        sliderPageList.add(sliderInfo);
    }

    public void removeSlider(SliderInfo sliderInfo){
        if ( sliderPageList.contains(sliderInfo)){
            sliderPageList.remove(sliderInfo);
        }
    }

    public void addAllSliders(List<SliderInfo> sliderList){
        sliderPageList.clear();
        sliderPageList.addAll(sliderList);
    }

    public void removeAllSliders(){
        sliderPageList.clear();
    }

    public List<SliderInfo> getSliderInfoList() {
        return sliderPageList;
    }

    public String  toJsonString(){
        return new Gson().toJson(createMap());
    }

    private Map<String,Object> createMap(){
        Map<String ,Object> tempMap = new HashMap<>();
        tempMap.put("name", name);
        tempMap.put("id",id);
        tempMap.put("width", width);
        tempMap.put("height", height);
        tempMap.put("proportion",proportion);
        tempMap.put("description",description);
        tempMap.put("modifiedTime", modifiedTime);

        List<Map<String,Object>> list = new ArrayList<>();
        for (int i= 0;i< sliderPageList.size();i++){
            list.add( sliderPageList.get(i).createMap());
        }
        tempMap.put("sliders",list);
        return tempMap;
    }


    public long getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(long modifiedTime) {
        this.modifiedTime = modifiedTime;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getProportion() {
        return proportion;
    }

    public void setProportion(String proportion) {
        this.proportion = proportion;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
