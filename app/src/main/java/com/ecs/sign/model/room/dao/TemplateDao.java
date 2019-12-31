package com.ecs.sign.model.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.ecs.sign.model.room.info.TemplateInfo;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface TemplateDao {

    @Query("SELECT*FROM template")
    Single<List<TemplateInfo>> getAllTemplates();

    @Insert
    Single<Long>  insertTemplate(TemplateInfo info);

    @Update()
    Single<Integer> updateTemplate(TemplateInfo info);

    @Delete
    Single<Integer> deleteTemplate(TemplateInfo info);

    @Update()
    Single<Integer> renameTemplate(TemplateInfo info);

}
