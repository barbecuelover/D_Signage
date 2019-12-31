package com.ecs.sign.model.room.info;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * 1.能获取 对应Slider界面中所有view。
 * 2.能添加Slider中所有的View
 * 3.Slider更新时删除Slider对应的所有View 再添加新的View（重复1）。
 */
@Entity(tableName = "views",indices = {@Index("sliderId")},foreignKeys = @ForeignKey(entity = SliderInfo.class,parentColumns = "id",childColumns = "sliderId",onDelete =ForeignKey.CASCADE ))
public class ViewInfo implements Serializable {

    //common
    @PrimaryKey(autoGenerate = true)
    private  int vid;
    private long realId;
    private long sliderId;
    private  int left; //view.getLeft
    private  int top; //view.getTop
    private  int  width; // px
    private  int  height; // px
    private String type ;


    private String url;
    private boolean urlIsLocal = true;

    //TextView
    private String textText;
    private float textSize;
    private int textColor;
    private int textGravity;
    private int textTypeface; //加粗斜体


    //ImageView
    private int imgScaleType;//图片拉伸


    private boolean mediaIsLoop;


    public boolean isMediaIsLoop() {
        return mediaIsLoop;
    }

    public void setMediaIsLoop(boolean mediaIsLoop) {
        this.mediaIsLoop = mediaIsLoop;
    }

    public boolean isUrlIsLocal() {
        return urlIsLocal;
    }

    public void setUrlIsLocal(boolean urlIsLocal) {
        this.urlIsLocal = urlIsLocal;
    }

    @Ignore
    public ViewInfo() {
    }


    public ViewInfo(long realId,long sliderId, String type) {
        this.realId = realId;
        this.sliderId = sliderId;
        this.type = type;
    }

    /**
     * 生成Map
     * @return
     */
    public Map<String ,Object> createMap(){
        Map<String ,Object> viewMap = new HashMap<>();
        viewMap.put("realId",realId);
        viewMap.put("type",type);
        viewMap.put("left", left);
        viewMap.put("top", top);
        viewMap.put("width",width);
        viewMap.put("height",height);

        viewMap.put("url",url);
        viewMap.put("urlIsLocal",urlIsLocal);

        viewMap.put("textText",textText);
        viewMap.put("textSize",textSize);
        viewMap.put("textColor",textColor);
        viewMap.put("textGravity",textGravity);
        viewMap.put("textTypeface",textTypeface);

        viewMap.put("imgScaleType",imgScaleType);

        return  viewMap;
    }

    public long getRealId() {
        return realId;
    }

    public void setRealId(long realId) {
        this.realId = realId;
    }

    public String getTextText() {
        return textText;
    }

    public void setTextText(String textText) {
        this.textText = textText;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getTextGravity() {
        return textGravity;
    }

    public void setTextGravity(int textGravity) {
        this.textGravity = textGravity;
    }

    public int getTextTypeface() {
        return textTypeface;
    }

    public void setTextTypeface(int textTypeface) {
        this.textTypeface = textTypeface;
    }

    public int getImgScaleType() {
        return imgScaleType;
    }

    public void setImgScaleType(int imgScaleType) {
        this.imgScaleType = imgScaleType;
    }

    public long getSliderId() {
        return sliderId;
    }

    public void setSliderId(long sliderId) {
        this.sliderId = sliderId;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getVid() {
        return vid;
    }

    public void setVid(int vid) {
        this.vid = vid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLeft() {
        return left;
    }

    public int getTop() {
        return top;
    }


    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }



    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
