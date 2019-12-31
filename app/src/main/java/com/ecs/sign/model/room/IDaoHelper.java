package com.ecs.sign.model.room;

import com.ecs.sign.base.common.CallBack;
import com.ecs.sign.model.room.dao.TemplateDao;
import com.ecs.sign.model.room.info.TemplateInfo;

import java.util.List;

import io.reactivex.Single;

/**
 * @author zw
 * @time 2019/11/26
 * @description
 */
public interface IDaoHelper{

    void updateTemplate(TemplateInfo info, CallBack callBack);

    void insertTemplate(TemplateInfo info, CallBack callBack);

    void deleteTemplate(TemplateInfo info, CallBack callBack);

    void renameTemplate(TemplateInfo info, CallBack callBack);

    void getAllTemplates(TemplateCallBack templateCallBack);

    public interface TemplateCallBack {
        void onResult(List<TemplateInfo> templateInfoList);

        void onError(String errorMsg);
    }

}
