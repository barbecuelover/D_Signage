package com.ecs.sign.model.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import com.ecs.sign.model.room.info.ViewInfo;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface ViewDao {
    /**
     * 获取数据库中所有的view
     * @return
     */
    @Query("SELECT*FROM views")
    Single<List<ViewInfo>> getAllViews();

    /**
     * 插入一个页中的所有View
     * @param infoList
     * @return
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long[]> insertViewList(List<ViewInfo> infoList);

}
