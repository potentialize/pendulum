package com.example.pendulum.database.daos;

import com.example.pendulum.database.entities.Tile;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.*;

@Dao
public interface TileDao {
    @Query("SELECT * FROM tiles ORDER BY position ASC")
    LiveData<List<Tile>> getAll();

    @Query("SELECT * FROM tiles WHERE parent_id = :paneId ORDER BY position ASC")
    LiveData<List<Tile>> getPane(Long paneId);

    @Query("SELECT COUNT(id) FROM tiles WHERE parent_id = :paneId")
    int countPaneTiles(Long paneId);

    @Query("UPDATE tiles SET position = :newPosition WHERE id = :tileId")
    void updatePosition(Long tileId, int newPosition);

    // TODO: auto increment position
    @Insert
    Long insert(Tile tile);

    // NOTE: dashboard pane (id = 1) cannot be removed, or the app would crash
    @Query("DELETE FROM tiles WHERE id = :id AND NOT id = 1")
    void deleteById(Long id);

    // NOTE: dashboard pane (id = 1) cannot be removed, or the app would crash
    @Query("DELETE FROM tiles WHERE id in (:ids) AND NOT id = 1")
    void deleteByIds(List<Long> ids);

    // NOTE: dashboard pane (id = 1) cannot be removed, or the app would crash
    @Query("DELETE FROM tiles WHERE NOT id = 1")
    void deleteAll();
}
