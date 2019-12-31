package com.ecs.sign.view.edit.bean;

public class Widget {

    private String name ;
    private int typeId;
    private int drawableRes;

    public Widget(String name, int typeId, int drawableRes) {
        this.name = name;
        this.typeId = typeId;
        this.drawableRes = drawableRes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDrawableRes() {
        return drawableRes;
    }

    public void setDrawableRes(int drawableRes) {
        this.drawableRes = drawableRes;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }
}
