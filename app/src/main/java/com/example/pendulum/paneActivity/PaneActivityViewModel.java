package com.example.pendulum.paneActivity;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.example.pendulum.database.AppDatabase;
import com.example.pendulum.database.DatabaseClient;
import com.example.pendulum.database.daos.TileDao;
import com.example.pendulum.database.entities.Tile;
import com.example.pendulum.helpers.MutableLiveDataArrayList;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class PaneActivityViewModel extends AndroidViewModel {

    private static final String TAG = "PaneActivityViewModel";

    private TileDao mTileDao;
    private MutableLiveDataArrayList<Long> mActiveTileIds = new MutableLiveDataArrayList<>();
    private MutableLiveDataArrayList<Long> mSelectedTileIds = new MutableLiveDataArrayList<>();





    public PaneActivityViewModel(Application application) {
        super(application);

        AppDatabase db = DatabaseClient.getInstance(application);
        mTileDao = db.getTileDao();
    }





    public LiveData<List<Tile>> getAllTiles() {
        return mTileDao.getAll();
    }

    public LiveData<List<Tile>> getPane(Long paneId) {
        return mTileDao.getPane(paneId);
    }





    public void deleteTile(Long id) {
        new DeleteTileAsync(mTileDao).execute(id);
    }
    private static class DeleteTileAsync extends AsyncTask<Long, Void, Void> {
        private TileDao mTileDao;

        DeleteTileAsync(TileDao tileDao) {
            mTileDao = tileDao;
        }

        @Override
        protected Void doInBackground(Long... params) {
            mTileDao.deleteById(params[0]);
            return null;
        }
    }





    @SuppressWarnings("unchecked")
    public void deleteTiles(List<Long> ids) {
        new DeleteTilesAsync(mTileDao).execute(ids);
    }
    private static class DeleteTilesAsync extends AsyncTask<List<Long>, Void, Void> {
        private TileDao mTileDao;

        DeleteTilesAsync(TileDao tileDao) {
            mTileDao = tileDao;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected Void doInBackground(List<Long>... params) {
            mTileDao.deleteByIds(params[0]);
            return null;
        }
    }

    public void deleteSelectedTiles() {
        List<Long> ids = mSelectedTileIds.getValue();
        deleteTiles(ids);
        mSelectedTileIds.clear();
    }





    public void deleteAllTiles() {
        new DeleteAllTilesAsync(mTileDao).execute();
    }
    private static class DeleteAllTilesAsync extends AsyncTask<Void, Void, Void> {
        private TileDao mTileDao;

        DeleteAllTilesAsync(TileDao tileDao) {
            mTileDao = tileDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mTileDao.deleteAll();
            return null;
        }
    }





    @SuppressWarnings("unchecked")
    public void persistTilePositions(List<Tile> tiles) {
        new PersistTilePositionsAsync(mTileDao).execute(tiles);
    }
    private static class PersistTilePositionsAsync extends AsyncTask<List<Tile>, Void, Void> {
        private TileDao mTileDao;

        PersistTilePositionsAsync(TileDao tileDao) {
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





    public boolean isTileActive(Long id) {
        return mActiveTileIds.contains(id);
    }

    public boolean isTileSelected(Long id) {
        return mSelectedTileIds.contains(id);
    }

    public void setTileActive(Long id, boolean set) {
        if (set) {
            if (!isTileActive(id)) {
                mActiveTileIds.add(id);
            }
        } else {
            mActiveTileIds.removeIf(cid -> cid.equals(id));
        }
    }

    public void setTileSelected(Long id, boolean set) {
        if (set) {
            if (!isTileSelected(id)) {
                mSelectedTileIds.add(id);
            }
        } else {
            mSelectedTileIds.removeIf(cid -> cid.equals(id));
        }
    }

    public MutableLiveDataArrayList<Long> getActiveTileIds() {
        // TODO: make immutable
        return mActiveTileIds;
    }

    public MutableLiveDataArrayList<Long> getSelectedTileIds() {
        // TODO: make immutable
        return mSelectedTileIds;
    }
}
