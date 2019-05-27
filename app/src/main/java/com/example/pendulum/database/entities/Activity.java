package com.example.pendulum.database.entities;

import androidx.room.*;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
        tableName = "Activities",
        foreignKeys = @ForeignKey(
                entity = Tile.class,
                parentColumns = "id",
                childColumns = "tile_id",
                onDelete = CASCADE
        ),
        indices = {
                @Index(value = "tile_id"),
                @Index(value = "start_timestamp"),
                @Index(value = "end_timestamp")
        }
)
public class Activity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Long id;

    @ColumnInfo(name = "tile_id")
    public Long tileId;

    @ColumnInfo(name = "start_timestamp")
    public Long startTimestamp = null;

    @ColumnInfo(name = "end_timestamp")
    public Long endTimestamp = null;
}
