package com.example.pendulum.database.entities;

import androidx.room.*;

import static androidx.room.ForeignKey.CASCADE;

// NOTE: only have fields that are managed by database, other data is lost each time observer pushes change (?)
@Entity(
        tableName = "tiles",
        foreignKeys = @ForeignKey(
                entity = Tile.class,
                parentColumns = "id",
                childColumns = "parent_id",
                onDelete = CASCADE
        ),
        indices = {
                @Index(value = "parent_id")
        }
)
public class Tile {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Long id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "parent_id")
    public Long parentId = null;

    @ColumnInfo(name = "position")
    public int position; // order in the pane
}
