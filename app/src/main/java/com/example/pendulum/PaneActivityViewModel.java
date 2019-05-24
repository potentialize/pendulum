package com.example.pendulum;

import android.app.Application;
import android.os.AsyncTask;

import com.example.pendulum.database.AppDatabase;
import com.example.pendulum.database.DatabaseClient;
import com.example.pendulum.database.daos.TileDao;
import com.example.pendulum.database.entities.Tile;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class PaneActivityViewModel extends AndroidViewModel {
    private LiveData<List<Tile>> mTiles;
    private TileDao mTileDao;

    public PaneActivityViewModel(Application application) {
        super(application);

        // load data
        AppDatabase db = DatabaseClient.getInstance(application);
        mTileDao = db.getTileDao();

        // TODO: get correct tiles
        mTiles = mTileDao.getAll();
    }

    public LiveData<List<Tile>> getTiles() {
        return mTiles;
    }





    public void insertTile(Tile tile) {
        new insertTileAsync(mTileDao).execute(tile);
    }
    private static class insertTileAsync extends AsyncTask<Tile, Void, Void> {
        private TileDao mTileDao;

        insertTileAsync(TileDao tileDao) {
            mTileDao = tileDao;
        }

        @Override
        protected Void doInBackground(final Tile... tiles) {
            mTileDao.insert(tiles);
            return null;
        }
    }





//    public void updateTilePosition(int id, int newPos) {
//        new updateTilePositionAsync(mTileDao).execute(id, newPos);
//    }
//    private static class updateTilePositionAsync extends AsyncTask<Integer, Void, Void> {
//        private TileDao mTileDao;
//
//        updateTilePositionAsync(TileDao tileDao) {
//            mTileDao = tileDao;
//        }
//
//        @Override
//        protected Void doInBackground(Integer... params) {
//            int id = params[0];
//            int newPos = params[1];
//            mTileDao.updatePosition(id, newPos);
//            return null;
//        }
//    }





    @SuppressWarnings("unchecked")
    public void persistTilePositions(List<Tile> tiles) {
        new persistTilePositionsAsync(mTileDao).execute(tiles);
    }
    private static class persistTilePositionsAsync extends AsyncTask<List<Tile>, Void, Void> {
        private TileDao mTileDao;

        persistTilePositionsAsync(TileDao tileDao) {
            mTileDao = tileDao;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected Void doInBackground(List<Tile>... lists) {
            List<Tile> tiles = lists[0];

            // TODO: Only update tiles that have changed position for efficiency
            for (int pos = 0; pos < tiles.size(); pos++) {
                Tile tile = tiles.get(pos);
                mTileDao.updatePosition(tile.id, pos);
            }

            return null;
        }
    }
}
