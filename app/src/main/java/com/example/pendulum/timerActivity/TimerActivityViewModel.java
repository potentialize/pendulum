package com.example.pendulum.timerActivity;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.example.pendulum.database.AppDatabase;
import com.example.pendulum.database.DatabaseClient;
import com.example.pendulum.database.daos.ActivityDao;
import com.example.pendulum.database.daos.TileDao;
import com.example.pendulum.database.entities.Activity;

import java.util.Timer;
import java.util.TimerTask;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class TimerActivityViewModel extends AndroidViewModel {

    private TileDao mTileDao;
    private ActivityDao mActivityDao;
    private Timer mSysTimer;
    private MutableLiveData<Long> mTimer = new MutableLiveData<>();
    private Long mActivityId = 0L;
    private Long mTileId = 0L;





    public TimerActivityViewModel(Application application) {
        super(application);

        AppDatabase db = DatabaseClient.getInstance(application);
        mTileDao = db.getTileDao();
        mActivityDao = db.getActivityDao();
    }





    public LiveData<Long> getTimer() {
        return mTimer;
    }

    public void setTileId(Long id) {
        mTileId = id;
    }

    public void setActivityId(Long id) {
        mActivityId = id;
    }


    public void increment() {
        mTimer.setValue(System.currentTimeMillis());
    }



    public void bootstrap(Long tileId, Long actId) {
        setTileId(tileId);
        setActivityId(actId);

        // TODO handle missing data better
        // NOTE: only execute once!
        if (mTileId != 0L && mActivityId == 0L) {
            // tile given, but not activity => create new activity
            new CreateActivityAsync(this, mActivityDao).execute(mTileId);
        } else if (mActivityId != 0L) {
            // activity given => load (do not care about tile in this case)
            new LoadActivityAsync(this, mActivityDao).execute(mActivityId);
        }

//        // timer every sec
//        mSysTimer = new Timer();
//        mSysTimer.schedule(new TimerTask(this) {
//            private TimerActivityViewModel ctx;
//
//            @Override
//            TimerTask(TimerActivityViewModel ctx) {
//                super();
//                this.ctx = ctx;
//            }
//            @Override
//            public void run() {
////                Log.d("", "run: timer");
//                ctx.mTimer.setValue(System.currentTimeMillis());
//
//            }
//        }, 0L, 1000L);
    }
    private static class CreateActivityAsync extends AsyncTask<Long, Void, Long> {
        private TimerActivityViewModel mContext;
        private ActivityDao mActivityDao;

        CreateActivityAsync(TimerActivityViewModel context, ActivityDao activityDao) {
            mContext = context;
            mActivityDao = activityDao;
        }

        @Override
        protected Long doInBackground(Long... params) {
            Long mTileId = params[0];

            Activity act = new Activity();
            act.tileId = mTileId;
            act.startTimestamp = System.currentTimeMillis();
            return mActivityDao.insert(act);
        }

        @Override
        protected void onPostExecute(Long newId) {
            mContext.setActivityId(newId);
            super.onPostExecute(newId);
        }
    }
    private static class LoadActivityAsync extends AsyncTask<Long, Void, Activity> {
        private TimerActivityViewModel mContext;
        private ActivityDao mActivityDao;

        LoadActivityAsync(TimerActivityViewModel context, ActivityDao activityDao) {
            mContext = context;
            mActivityDao = activityDao;
        }

        @Override
        protected Activity doInBackground(Long... params) {
            Long actId = params[0];

            return mActivityDao.getActivityById(actId);
        }

        @Override
        protected void onPostExecute(Activity act) {
            mContext.setTileId(act.tileId);

            // set start value of timer
            Long diff = System.currentTimeMillis() - act.startTimestamp;
            mContext.mTimer.setValue(diff);

            super.onPostExecute(act);
        }
    }




    public void closeActivity() {
        if (mActivityId != null) {
            new CloseActivityAsync(mActivityDao).execute(mActivityId);
        }
    }
    private static class CloseActivityAsync extends AsyncTask<Long, Void, Void> {
        private ActivityDao mActivityDao;

        CloseActivityAsync(ActivityDao activityDao) {
            mActivityDao = activityDao;
        }

        @Override
        protected Void doInBackground(Long... params) {
            Long actId = params[0];

            mActivityDao.setEndTimestamp(actId, System.currentTimeMillis());
            return null;
        }
    }




//    private Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//
//            mTimer.setValue();
//
//            handler.postDelayed(this, 1000);
//        }
//    };
}
