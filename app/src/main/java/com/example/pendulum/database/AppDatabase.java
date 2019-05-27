package com.example.pendulum.database;

import androidx.room.*;

import com.example.pendulum.database.entities.Activity;
import com.example.pendulum.database.entities.Tile;
import com.example.pendulum.database.daos.ActivityDao;
import com.example.pendulum.database.daos.TileDao;

@Database(
    entities = {
        Tile.class,
        Activity.class,
    },
    version = 1,
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    public static final Long DASHBOARD_ID = 1L; // NOTE: room starts auto incrementing from 1
    public abstract TileDao getTileDao();
    public abstract ActivityDao getActivityDao();
}
