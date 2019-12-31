package com.ecs.sign.model.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.ecs.sign.model.room.dao.SliderDao;
import com.ecs.sign.model.room.dao.TemplateDao;
import com.ecs.sign.model.room.dao.ViewDao;
import com.ecs.sign.model.room.info.SliderInfo;
import com.ecs.sign.model.room.info.TemplateInfo;
import com.ecs.sign.model.room.info.ViewInfo;


@Database(entities = {TemplateInfo.class, SliderInfo.class, ViewInfo.class},version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract TemplateDao templateDao();
    public abstract SliderDao sliderDao();
    public abstract ViewDao viewDao();

}
