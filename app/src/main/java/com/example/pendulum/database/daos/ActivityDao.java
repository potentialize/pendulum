package com.example.pendulum.database.daos;

import com.example.pendulum.database.entities.Activity;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface ActivityDao {
    @Insert
    Long insert(Activity act);

    @Query("UPDATE activities SET end_timestamp = :ts WHERE id = :id")
    void setEndTimestamp(Long id, Long ts);

    @Query("SELECT id FROM activities WHERE end_timestamp IS NULL LIMIT 1")
    Long getLastOpenTimer();

    @Query("SELECT * FROM activities WHERE id = :id LIMIT 1")
    Activity getActivityById(Long id);
}
