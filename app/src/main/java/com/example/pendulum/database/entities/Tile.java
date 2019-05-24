package com.example.pendulum.database.entities;

import androidx.room.*;

@Entity(
        tableName = "tiles",
//        foreignKeys = @ForeignKey(
//                entity = Pane.class,
//                parentColumns = "id",
//                childColumns = "pane_id"
//        ),
        indices = {
                @Index(value = "parent_id")
        }
)
public class Tile {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "parent_id")
    public int parentId;

    @ColumnInfo(name = "position")
    public int position; // order in the pane
}
