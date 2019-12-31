package com.ecs.sign.view.edit.view;

import android.app.Activity;

import com.ecs.sign.view.edit.window.ImageChooseWindow;
import com.ecs.sign.view.edit.window.TextInputWindow;
import com.ecs.sign.view.edit.window.VideoChooseWindow;
import com.ecs.sign.view.edit.window.attr.ColorPickerWindow;
import com.ecs.sign.view.edit.window.attr.TextAlignWindow;
import com.ecs.sign.view.edit.window.attr.TextSizeWindow;
import com.ecs.sign.view.edit.window.attr.TextTypefaceWindow;

/**
 * @author zw
 * @time 2019/12/5
 * @description
 */
public class EditWindowHelper {

    private Activity activity;

    private TextInputWindow textInputWindow;
    private ImageChooseWindow imageChooseWindow;
    private VideoChooseWindow videoChooseWindow;

    //--------------view attr------------//
    //TextView
    private TextSizeWindow textSizeWindow;
    private ColorPickerWindow  textColorPickerWindow;
    private TextAlignWindow textAlignWindow;
    private TextTypefaceWindow textTypefaceWindow;

    public EditWindowHelper(Activity activity) {
        this.activity = activity;
        init();
    }

    private void  init(){
        textInputWindow = new TextInputWindow(activity);
        imageChooseWindow = new ImageChooseWindow(activity);
        videoChooseWindow = new VideoChooseWindow(activity);

        textSizeWindow = new TextSizeWindow(activity);
        textColorPickerWindow = new ColorPickerWindow(activity);
        textAlignWindow =new TextAlignWindow(activity);
        textTypefaceWindow =new TextTypefaceWindow(activity);
    }

    public void popupTextWindow(String oldText,TextInputWindow.OnTextChangedListener  listener){
        textInputWindow.setText(oldText);
        textInputWindow.setOnTextChangedListener(listener);
        textInputWindow.show();
    }


    public void popupImageChooseWindow(ImageChooseWindow.OnLinkTypeSelectedListener listener){
        imageChooseWindow.setOnLinkTypeSelectedListener(listener);
        imageChooseWindow.show();
    }

    public void popupVideoChooseWindow(VideoChooseWindow.OnLinkSendListener linkSendListener){
        videoChooseWindow.setOnLinkSendListener(linkSendListener);
        videoChooseWindow.show();
    }

    public void chooseTextSize( TextSizeWindow.OnTextSizeChangedListener listener){
        textSizeWindow.setOnTextSizeChangedListener(listener);
        textSizeWindow.show();
    }

    public void chooseTextColor(int colorRes, ColorPickerWindow.OnColorChangedListener listener){
        textColorPickerWindow.setColor(colorRes);
        textColorPickerWindow.setOnColorChangedListener(listener);
        textColorPickerWindow.show();
    }

    public void chooseTextAlign(int align, TextAlignWindow.OnAlignChangedListener listener){
        textAlignWindow.setAlign(align);
        textAlignWindow.setOnAlignChangedListener(listener);
        textAlignWindow.show();
    }

    public void chooseTextTypeface(int typeface, TextTypefaceWindow.OnTypefaceChangedListener listener){
        textTypefaceWindow.setTypeface(typeface);
        textTypefaceWindow.setnTypefaceChangedListener(listener);
        textTypefaceWindow.show();
    }

}
