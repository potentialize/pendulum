package com.example.pendulum.paneActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.pendulum.R;
import com.example.pendulum.addTileActivity.AddTileActivity;
import com.example.pendulum.database.AppDatabase;
import com.example.pendulum.database.entities.*;
import com.example.pendulum.timerActivity.TimerActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class PaneActivity extends AppCompatActivity {

    private static final String TAG = "PaneActivity";

    // ACCEPTED INTENT DATA
    public static final String EXTRA_PANE_ID = "com.example.pendulum.PaneActivity.EXTRA_PANE_ID";
    public static final String EXTRA_IS_TILE_ADDED = "com.example.pendulum.PaneActivity.EXTRA_IS_TILE_ADDED";

    private Toolbar mToolbar;
    protected PaneActivityViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private PaneActivityRecyclerAdapter mAdapter;

    private Long mPaneId;
    private String mPaneName;
    private int mSelectedTileCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pane);

        // intent
        Intent intent = getIntent();
        mPaneId = intent.getLongExtra(EXTRA_PANE_ID, AppDatabase.DASHBOARD_ID);

        // toolbar
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        // pane name
        mPaneName = getResources().getString(R.string.title_activity_pane);
        this.setTitle(mPaneName);

        // view model
        mViewModel = ViewModelProviders.of(this).get(PaneActivityViewModel.class);
        mViewModel.getPane(mPaneId).observe(this, (List<Tile> tiles) -> {
            mAdapter.setTiles(tiles);
        });
        mViewModel.getLastOpenTimer().observe(this, (Long openTimerId) -> {
            // open timer was found, go to timer activity
            openTimerActivity(mPaneId, openTimerId);
        });
        mViewModel.getSelectedTileIds().observe(this, (List<Long> ids) -> {
            mAdapter.setSelectedTileIds(ids);

            int newCount = ids.size();
            if (mSelectedTileCount != newCount) {
                mSelectedTileCount = newCount;

                // update title
                if (mSelectedTileCount == 0) {
                    this.setTitle(mPaneName);
                } else {
                    // TODO: use string resource
                    this.setTitle(newCount + " selected");
                }

                // update options menu
                invalidateOptionsMenu();
            }
        });

        // recycler
        initRecyclerView();
    }

    private void initRecyclerView() {
        // get recycler view
        mRecyclerView = findViewById(R.id.pane_recycler);

        // recycler view adapter
        mAdapter = new PaneActivityRecyclerAdapter(this);

        // item touch helper
        ItemTouchHelper.Callback callback = new ItemMoveCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView);

        // configure recycler view
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        // content does not change layout size (since recycler view is set to match its parent) -> improve performance, see: https://stackoverflow.com/a/33365341
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pane_activity, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // modify toolbar icons before they are shown

        menu.findItem(R.id.action_edit).setVisible(mSelectedTileCount == 1);
        menu.findItem(R.id.action_delete).setVisible(mSelectedTileCount > 0);

        return super.onPrepareOptionsMenu(menu);
    }

    public void openTimerActivity(Long tileId) {
        Intent intent = new Intent(this, TimerActivity.class);
        intent.putExtra(TimerActivity.EXTRA_TILE_ID, tileId);
        startActivity(intent);
    }

    public void openTimerActivity(Long tileId, Long actId) {
        Intent intent = new Intent(this, TimerActivity.class);
        intent.putExtra(TimerActivity.EXTRA_TILE_ID, tileId);
        intent.putExtra(TimerActivity.EXTRA_ACTIVITY_ID, actId);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                // delete selected tiles
                mViewModel.deleteSelectedTiles();
                return true;
            case R.id.action_edit:
                // edit selected tile
                // TODO
                return true;
            case R.id.action_add:
                Intent intent = new Intent(this, AddTileActivity.class);
                intent.putExtra(AddTileActivity.EXTRA_PANE_ID, mPaneId);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
