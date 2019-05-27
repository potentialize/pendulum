package com.example.pendulum.addTileActivity;

import android.app.Application;
import android.os.AsyncTask;

import com.example.pendulum.database.AppDatabase;
import com.example.pendulum.database.DatabaseClient;
import com.example.pendulum.database.daos.TileDao;
import com.example.pendulum.database.entities.Tile;

import androidx.lifecycle.AndroidViewModel;

public class AddTileActivityViewModel extends AndroidViewModel {
    private TileDao mTileDao;

    AddTileActivityViewModel(Application application) {
        super(application);

        AppDatabase db = DatabaseClient.getInstance(application);
        mTileDao = db.getTileDao();
    }





    public void insertTile(Tile tile) {
        new InsertTileAsync(mTileDao).execute(tile);
    }
    private static class InsertTileAsync extends AsyncTask<Tile, Void, Long> {
        private TileDao mTileDao;

        InsertTileAsync(TileDao tileDao) {
            mTileDao = tileDao;
        }

        @Override
        protected Long doInBackground(final Tile... params) {
            Tile tile = params[0];
            tile.position = mTileDao.countPaneTiles(tile.parentId);
            return mTileDao.insert(tile);
        }
    }
}
