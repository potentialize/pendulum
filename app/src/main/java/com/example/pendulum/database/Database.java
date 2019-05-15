package com.example.pendulum.database;

import androidx.room.RoomDatabase;

import com.example.pendulum.database.daos.TileDao;

public abstract class Database extends RoomDatabase {
    public abstract TileDao TileDao();
}
