package com.example.pendulum.database.entities;

@Entity
public class Tile {
    @PrimaryKey
    @ColumnInfo(name = "tile_id")
    public int id;
}
