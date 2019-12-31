package com.ecs.sign.model.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.ecs.sign.model.room.info.SliderInfo;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface SliderDao {

    /**
     * 获取数据库中的全部页
     * @return
     */
    @Query("SELECT*FROM slider ")
    Single<List<SliderInfo>> getAllSliders();

    /**
     * 插入一个模板中所有的 页
     * @param info
     * @return
     */
    @Insert
    Single<Long[]> insertSliderList(List<SliderInfo> info);

}
