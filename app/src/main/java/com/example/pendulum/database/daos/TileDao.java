package com.example.pendulum.database.daos;

import com.example.pendulum.database.entities.Tile;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.*;

@Dao
public interface TileDao {
    @Query("SELECT * FROM tiles ORDER BY position ASC")
    LiveData<List<Tile>> getAll();

    @Query("SELECT * FROM tiles WHERE id IN (:tileIds)")
    List<Tile> loadAllByIds(int[] tileIds);

    @Query("UPDATE tiles SET position = :newPosition WHERE id = :tileId")
    void updatePosition(int tileId, int newPosition);

    @Insert
    void insert(Tile... tiles);

    @Delete
    void delete(Tile tile);
}
