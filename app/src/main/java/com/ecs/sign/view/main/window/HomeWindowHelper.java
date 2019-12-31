package com.ecs.sign.view.main.window;

import android.app.Activity;

/**
 * @author zw
 * @time 2019/12/25
 * @description
 */
public class HomeWindowHelper {
    private TempMoreWindow tempMoreWindow;
    private ProjectInfoWindow projectInfoWindow;
    private CommentWindow commentWindow;
    private RenameWindow renameWindow;
    private Activity activity;


    public HomeWindowHelper(Activity activity) {
        this.activity = activity;
        init();
    }

    private void init() {
        tempMoreWindow = new TempMoreWindow(activity);
        projectInfoWindow = new ProjectInfoWindow(activity);
        commentWindow= new CommentWindow(activity);
        renameWindow = new RenameWindow(activity);
    }

    public void popupTemplateMoreWindow(TempMoreWindow.OnButtonClicked buttonClicked){
        tempMoreWindow.setOnButtonClicked(buttonClicked);
        tempMoreWindow.show();
    }

    public void popupProjectInfoWindow(ProjectInfoWindow.OnButtonClicked onButtonClicked){
        projectInfoWindow.setButtonClicked(onButtonClicked);
        projectInfoWindow.show();
    }

    public void popupRenameWindow(RenameWindow.OnNameChanged onNameChanged){
        renameWindow.setOnNameChanged(onNameChanged);
        renameWindow.show();
    }

    public void popupCommentWindow(CommentWindow.OnCommentSend commentSend){
        commentWindow.setOnCommentSend(commentSend);
        commentWindow.show();
    }
}
