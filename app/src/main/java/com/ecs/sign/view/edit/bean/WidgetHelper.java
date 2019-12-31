package com.ecs.sign.view.edit.bean;

import com.ecs.sign.R;
import com.ecs.sign.base.common.Constant;

import java.util.ArrayList;
import java.util.List;

/**

 * @author zw
 * @time 2019/11/27
 * @description
 */
public class WidgetHelper {


    private Widget viewDuplicate;
    private Widget viewDelete;

    private Widget textText;
    private Widget textColor;
    private Widget textSize;
    private Widget textAlign;
    private Widget textTypeface;

    private Widget imageImage;
    private Widget imageAlign;

    private Widget videoVideo;
    private Widget videoMute;

    private Widget typeText;
    private Widget typeBackground;
    private Widget typeImage;
    private Widget typeVideo;
    private Widget typeMusic;
    private Widget typeSound;
    private Widget setTime;
    private Widget sliderDuplicate;
    private Widget sliderDelete;

    List<Widget> widgetList = new ArrayList<>();
    List<Widget> typeList = new ArrayList<>();


    public WidgetHelper() {
        initWidgets();
    }

    private void initWidgets() {
        viewDuplicate = new Widget("Duplicate",Constant.ID_VIEW_DUPLICATE, R.mipmap.function_duplicate_icon);
        viewDelete = new Widget("Delete",Constant.ID_VIEW_DELETE, R.mipmap.delete_btn);
        //slider
        setTime =  new Widget("SetTime",Constant.ID_SLIDER_SET_TIME, R.mipmap.function_align_icon);
        sliderDuplicate = new Widget("Duplicate",Constant.ID_SLIDER_DUPLICATE, R.mipmap.function_duplicate_icon);
        sliderDelete = new Widget("Delete",Constant.ID_SLIDER_DELETE, R.mipmap.delete_btn);

        //Text 
        textText = new Widget("Text",Constant.ID_TEXT_TEXT, R.mipmap.text_btn);
        textColor = new Widget("Color",Constant.ID_TEXT_COLOR, R.mipmap.function_color_icon);
        textSize = new Widget("Size",Constant.ID_TEXT_SIZE, R.mipmap.function_font_icon);
        textAlign = new Widget("Align",Constant.ID_TEXT_ALIGN, R.mipmap.function_align_icon);
        textTypeface = new Widget("Typeface",Constant.ID_TEXT_TYPEFACE, R.mipmap.function_background_color_icon);
        //Image
        imageImage = new Widget("Image",Constant.ID_IMG_IMG, R.mipmap.function_background_color_icon);
        imageAlign = new Widget("Align",Constant.ID_IMG_ALIGN, R.mipmap.function_align_icon);
        //Video
        videoVideo = new Widget("Video",Constant.ID_VIDEO_VIDEO, R.mipmap.function_background_color_icon);
        videoMute = new Widget("Mute",Constant.ID_VIDEO_MUTE, R.mipmap.music_btn);

        //Type
        typeText = new Widget("Text",Constant.ID_TYPE_TEXT, R.mipmap.text_btn);
        typeBackground = new Widget("Wallpaper",Constant.ID_TYPE_BACKGROUND, R.mipmap.function_background_color_icon);
        typeImage = new Widget("Image",Constant.ID_TYPE_IMAGE, R.mipmap.record_btn);
        typeVideo = new Widget("Video",Constant.ID_TYPE_VIDEO, R.mipmap.media_btn);
        typeMusic = new Widget("Music",Constant.ID_TYPE_MUSIC, R.mipmap.music_btn);
        typeSound = new Widget("Sound",Constant.ID_TYPE_SOUND, R.mipmap.sound_btn);
    }


    public int getWidgetAttrID(int position){
        Widget widget =widgetList.get(position);
        return widget.getTypeId();
    }

    
    //TODO 应该使用 工厂模式
    public  List<Widget> getWidgetList(String viewType){
        widgetList.clear();
        switch (viewType){
            case Constant.VIEW_SLIDER:
                toSlider();
                break;
            case Constant.VIEW_TEXT:
                toTextView();
                break;
            case Constant.VIEW_IMAGE:
                toImageView();
                break;
            case Constant.VIEW_VIDEO:
                toVideoView();
                break;
            case Constant.VIEW_TYPE:
                toViewType();
                return typeList;
        }
    
        return widgetList;
    }


    private void toViewType(){
        typeList.clear();
        typeList.add(typeText);
        typeList.add(typeBackground);
        typeList.add(typeImage);
        typeList.add(typeVideo);
        typeList.add(typeMusic);
        typeList.add(typeSound);
    }

    private void toSlider() {
        widgetList.clear();
        widgetList.add(setTime);
        widgetList.add(sliderDuplicate);
        widgetList.add(sliderDelete);
    }

    private void toTextView(){

        widgetList.add(textText);
        widgetList.add(textColor);
        widgetList.add(textSize);
        widgetList.add(textAlign);
        widgetList.add(textTypeface);
        widgetList.add(viewDuplicate);
        widgetList.add(viewDelete);
    }

    private void toImageView(){

        widgetList.add(imageImage);
        widgetList.add(imageAlign);
        widgetList.add(viewDuplicate);
        widgetList.add(viewDelete);

    }

    private void toVideoView(){
        widgetList.add(videoVideo);
        widgetList.add(videoMute);
        widgetList.add(viewDuplicate);
        widgetList.add(viewDelete);
    }

    private void initVideoAttr() {

        Widget video = new Widget("Video",Constant.ID_VIDEO_VIDEO, R.mipmap.function_background_color_icon);
        Widget mute = new Widget("Mute",Constant.ID_VIDEO_MUTE, R.mipmap.music_btn);
        Widget duplicate = new Widget("Duplicate",Constant.ID_VIEW_DUPLICATE, R.mipmap.function_duplicate_icon);
        Widget delete = new Widget("Delete",Constant.ID_VIEW_DELETE, R.mipmap.delete_btn);

        widgetList.add(video);
        widgetList.add(mute);
        widgetList.add(duplicate);
        widgetList.add(delete);

    }

    private void initImageAttr() {
        Widget typeface = new Widget("Image",Constant.ID_IMG_IMG, R.mipmap.function_background_color_icon);
        Widget align = new Widget("Align",Constant.ID_IMG_ALIGN, R.mipmap.function_align_icon);
        Widget duplicate = new Widget("Duplicate",Constant.ID_VIEW_DUPLICATE, R.mipmap.function_duplicate_icon);
        Widget delete = new Widget("Delete",Constant.ID_VIEW_DELETE, R.mipmap.delete_btn);

        widgetList.add(align);
        widgetList.add(typeface);
        widgetList.add(duplicate);
        widgetList.add(delete);
    }

    private void initTextAttr(){
        Widget text = new Widget("Text",Constant.ID_TEXT_TEXT, R.mipmap.text_btn);
        Widget color = new Widget("Color",Constant.ID_TEXT_COLOR, R.mipmap.function_color_icon);
        Widget size = new Widget("Size",Constant.ID_TEXT_SIZE, R.mipmap.function_font_icon);
        Widget align = new Widget("Align",Constant.ID_TEXT_ALIGN, R.mipmap.function_align_icon);
        Widget typeface = new Widget("Typeface",Constant.ID_TEXT_TYPEFACE, R.mipmap.function_background_color_icon);
        Widget duplicate = new Widget("Duplicate",Constant.ID_VIEW_DUPLICATE, R.mipmap.function_duplicate_icon);
        Widget delete = new Widget("Delete",Constant.ID_VIEW_DELETE, R.mipmap.delete_btn);

        widgetList.add(text);
        widgetList.add(color);
        widgetList.add(size);
        widgetList.add(align);
        widgetList.add(typeface);
        widgetList.add(duplicate);
        widgetList.add(delete);
    }


    private void initViewType(){
        Widget textType = new Widget("Text",Constant.ID_TYPE_TEXT, R.mipmap.text_btn);
        Widget bgType = new Widget("BackGround",Constant.ID_TYPE_BACKGROUND, R.mipmap.function_background_color_icon);
        Widget imgType = new Widget("Image",Constant.ID_TYPE_IMAGE, R.mipmap.record_btn);
        Widget videoType = new Widget("Video",Constant.ID_TYPE_VIDEO, R.mipmap.media_btn);
        Widget musicType = new Widget("Music",Constant.ID_TYPE_MUSIC, R.mipmap.music_btn);
        Widget soundType = new Widget("Sound",Constant.ID_TYPE_SOUND, R.mipmap.sound_btn);

        widgetList.add(textType);
        widgetList.add(bgType);
        widgetList.add(imgType);
        widgetList.add(videoType);
        widgetList.add(musicType);
        widgetList.add(soundType);
    }


    public int getWidgetType(int position) {
        switch (position){
            case 0:
                return Constant.ID_TYPE_TEXT;
            case 1:
                return Constant.ID_TYPE_BACKGROUND;
            case 2:
                return Constant.ID_TYPE_IMAGE;
            case 3:
                return Constant.ID_TYPE_VIDEO;
            case 4:
                return Constant.ID_TYPE_MUSIC;
            case 5:
                return Constant.ID_TYPE_SOUND;
                default:
                    return  Constant.ID_TYPE_TEXT;
        }
    }
}
