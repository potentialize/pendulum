package com.example.pendulum.database;

import androidx.room.*;

import com.example.pendulum.database.entities.Tile;
import com.example.pendulum.database.daos.TileDao;

@Database(
    entities = {
        Tile.class,
    },
    version = 1,
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TileDao getTileDao();
}
