package com.ecs.sign.model.room;

import android.content.Context;

import androidx.room.Room;

import com.ecs.sign.model.room.dao.SliderDao;
import com.ecs.sign.model.room.dao.TemplateDao;
import com.ecs.sign.model.room.dao.ViewDao;
import com.ecs.sign.model.room.info.SliderInfo;
import com.ecs.sign.model.room.info.TemplateInfo;
import com.ecs.sign.model.room.info.ViewInfo;

import java.util.List;

import io.reactivex.Single;


/**
 *
 * 在此类中 创建数据库和表。
 * 将 template slider view  三个表的操作 整合。
 *
 */
public class DaoHelper  implements TemplateDao,SliderDao,ViewDao{

    private TemplateDao templateDao;
    private SliderDao sliderDao;
    private ViewDao viewDao;

    private static DaoHelper instance;

    private DaoHelper(Context applicationContext) {
        AppDatabase database = Room.databaseBuilder(applicationContext,AppDatabase.class,"signage.db").build();
        templateDao = database.templateDao();
        sliderDao = database.sliderDao();
        viewDao = database.viewDao();
    }


    public static DaoHelper getInstance(Context applicationContext){
        if (instance == null){
            synchronized (DaoHelper.class){
                if (instance == null){
                    instance = new DaoHelper(applicationContext);
                }
            }
        }
        return instance;
    }

    @Override
    public Single<List<SliderInfo>> getAllSliders() {
        return sliderDao.getAllSliders();
    }

    @Override
    public Single<Long[]> insertSliderList(List<SliderInfo> info) {
        return sliderDao.insertSliderList(info);
    }

    @Override
    public Single<List<TemplateInfo>> getAllTemplates() {
        return templateDao.getAllTemplates();
    }

    @Override
    public Single<Long> insertTemplate(TemplateInfo info) {
        return templateDao.insertTemplate(info);
    }

    @Override
    public Single<Integer> updateTemplate(TemplateInfo info) {
        return templateDao.updateTemplate(info);
    }

    @Override
    public Single<Integer> deleteTemplate(TemplateInfo info) {
        return templateDao.deleteTemplate(info);
    }

    @Override
    public Single<Integer> renameTemplate(TemplateInfo info) {
        return templateDao.renameTemplate(info);
    }

    @Override
    public Single<List<ViewInfo>> getAllViews() {
        return viewDao.getAllViews();
    }

    @Override
    public Single<Long[]> insertViewList(List<ViewInfo> infoList) {
        return viewDao.insertViewList(infoList);
    }

}
