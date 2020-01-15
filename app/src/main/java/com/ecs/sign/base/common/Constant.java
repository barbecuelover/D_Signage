package com.ecs.sign.base.common;

public class Constant {


    public static  final  String TEMPLATE_INDEX= "template_index"; //（-1：new ）
    public static  final  String SLIDER_INDEX = "slider_index";



    public static final String VIEW_TYPE = "view_type";
    public static final String VIEW_SLIDER = "slider";
    public static final String VIEW_TEXT = "textTypeId";
    public static final String VIEW_IMAGE = "imageTypeId";
    public static final String VIEW_VIDEO = "mediaTypeId";

    public static String SOCKET_CLIENT = "client";
    public static String SOCKET_SERVER = "server";
    public static final int SOCKET_PORT = 8904;
    public static final int SERVER_PORT = 8999;

    public static final int ID_TYPE_TEXT = 1000;
    public static final int ID_TYPE_BACKGROUND = 1001;
    public static final int ID_TYPE_IMAGE = 1002;
    public static final int ID_TYPE_VIDEO = 1003;
    public static final int ID_TYPE_MUSIC = 1004;
    public static final int ID_TYPE_SOUND = 1005;

    public static final int ID_VIEW_DUPLICATE = 10;
    public static final int ID_VIEW_DELETE = 11;

    public static final int ID_SLIDER_SET_TIME = 20;
    public static final int ID_SLIDER_DUPLICATE = 21;
    public static final int ID_SLIDER_DELETE  = 22;
    public static final int ID_SLIDER_SET_BACKGROUND = 23;

    public static final int ID_TEXT_TEXT = 100;
    public static final int ID_TEXT_COLOR = 101;
    public static final int ID_TEXT_SIZE = 102;
    public static final int ID_TEXT_ALIGN = 103;
    public static final int ID_TEXT_TYPEFACE = 104;

    public static final int ID_IMG_IMG = 200;
    public static final int ID_IMG_ALIGN = 201;

    public static final int ID_VIDEO_VIDEO = 300;
    public static final int ID_VIDEO_MUTE = 301;


    public static final int REQUEST_CODE_NEW_IMAGE_VIEW_GALLERY = 100;
    public static final int REQUEST_CODE_NEW_IMAGE_VIEW_CAMERA = 101;
    public static final int REQUEST_CODE_NEW_VIDEO_VIEW_LOCAL = 200;

    public static final int REQUEST_CODE_SLIDER_BG_GALLERY = 300;
    public static final int REQUEST_CODE_SLIDER_BG_CAMERA = 301;
    public static final String FILE_PATH = "com.ecs.signage.fileprovider";
}
