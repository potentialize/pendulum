package com.example.pendulum.database;

import android.content.Context;
import androidx.room.Room;

// Singleton
public class DatabaseClient {
    private static final String dbName = "pendulum.db";
    private static volatile AppDatabase db;

    // Singleton: private constructor prevents instantiating
    private DatabaseClient () {}

    public static synchronized AppDatabase getInstance(Context context) {
        if (db == null) {
            // Temp: delete database during debugging
//            context.deleteDatabase(dbName);

            // build database when getInstance is called for the first time
            db = Room.databaseBuilder(context, AppDatabase.class, dbName)
//                    .allowMainThreadQueries() // Temp
                    .build();
        }
        return db;
    }
}

// references:
// https://www.simplifiedcoding.net/android-room-database-example/ (DatabaseClient)
// https://stackoverflow.com/a/50105730
