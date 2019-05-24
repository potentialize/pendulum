package com.example.pendulum;

import android.os.Bundle;

import com.example.pendulum.database.entities.*;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class PaneActivity extends AppCompatActivity {

    private static final String TAG = "PaneActivity";
    private FloatingActionButton mFab;
    private Toolbar mToolbar;
    protected PaneActivityViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private PaneActivityRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pane);

        // set title (default: dashboard)
        this.setTitle(getResources().getString(R.string.activity_pane_title));

        // toolbar
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        // view model
        mViewModel = ViewModelProviders.of(this).get(PaneActivityViewModel.class);
        mViewModel.getTiles().observe(this, new Observer<List<Tile>>() {
            @Override
            public void onChanged(List<Tile> tiles) {
                // update recycler view adapter with new data (tiles)
                mAdapter.setTiles(tiles);
            }
        });

        // recycler
        initRecyclerView();

        // fab
        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TEMP: add new tile
                Tile tile = new Tile();
                tile.parentId = 0; // Temp
                tile.name = "Just another tile";
                tile.position = mViewModel.getTiles().getValue().size();
                mViewModel.insertTile(tile);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
