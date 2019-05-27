package com.example.pendulum.database;

import android.content.Context;
import android.util.Log;

import com.example.pendulum.R;
import com.example.pendulum.database.daos.TileDao;
import com.example.pendulum.database.entities.*;

import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

// Singleton
public class DatabaseClient {
    private static final String dbName = "pendulum.db";
    private static volatile Context mContext;
    private static volatile AppDatabase db;

    // Singleton: private constructor prevents instantiating
    private DatabaseClient () {}

    public static synchronized AppDatabase getInstance(Context context) {
        if (db == null) {
            mContext = context;

            // Temp: delete database during debugging
//            context.deleteDatabase(dbName);

            // build database when getInstance is called for the first time
            db = Room.databaseBuilder(context, AppDatabase.class, dbName)
                    .addCallback(AppDatabaseCallback)
                    .build();
        }
        return db;
    }

    private static RoomDatabase.Callback AppDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            Executors.newSingleThreadScheduledExecutor().execute(DatabaseClient::initDb);
            super.onCreate(db);
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };

    private static void initDb() {
        AppDatabase db = getInstance(mContext);
        TileDao tileDao = db.getTileDao();

        // make sure dashboard tile is present
        Tile dashboardTile = new Tile();
        dashboardTile.name = mContext.getResources().getString(R.string.title_activity_pane);
        tileDao.insert(dashboardTile);
    }
}

// references:
// https://www.simplifiedcoding.net/android-room-database-example/ (DatabaseClient)
// https://stackoverflow.com/a/50105730
