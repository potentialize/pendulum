package com.example.pendulum.addTileActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.pendulum.database.AppDatabase;
import com.example.pendulum.database.entities.Tile;
import com.example.pendulum.paneActivity.PaneActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.pendulum.R;
import com.google.android.material.textfield.TextInputLayout;

public class AddTileActivity extends AppCompatActivity {

    // ACCEPTED INTENT DATA
    public static final String EXTRA_PANE_ID = "com.example.pendulum.AddTileActivity.EXTRA_PANE_ID";

    protected AddTileActivityViewModel mViewModel;

    private long mPaneId;

    private TextInputLayout inputTileName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // intent
        Intent intent = getIntent();
        mPaneId = intent.getLongExtra(EXTRA_PANE_ID, AppDatabase.DASHBOARD_ID);

        // view model
        mViewModel = ViewModelProviders.of(this).get(AddTileActivityViewModel.class);

        // form fields
        inputTileName = findViewById(R.id.input_tile_name);
    }

    public void addSampleData(View v) {
        Tile tile;

        tile = new Tile();
        tile.parentId = mPaneId;
        tile.name = "Study";
        mViewModel.insertTile(tile);

        tile = new Tile();
        tile.parentId = mPaneId;
        tile.name = "Eat";
        mViewModel.insertTile(tile);

        tile = new Tile();
        tile.parentId = mPaneId;
        tile.name = "Sleep";
        mViewModel.insertTile(tile);

        tile = new Tile();
        tile.parentId = mPaneId;
        tile.name = "Groceries";
        mViewModel.insertTile(tile);

        openPaneActivity();
    }

    private String getTileNameInput() {
        return inputTileName.getEditText().getText().toString().trim();
    }

    // true if data is accepted
    private boolean isTileNameValid() {
        if (getTileNameInput().isEmpty()) {
            inputTileName.setError(getResources().getString(R.string.input_tile_name_is_empty_error));
            return false;
        } else {
            inputTileName.setError(null);
            inputTileName.setErrorEnabled(false);
            return true;
        }
    }

    // true if form was successfully processed, false otherwise
    private boolean processForm() {
        if (
                isTileNameValid()
        ) {
            // add tile to db
            Tile tile = new Tile();
            tile.parentId = mPaneId;
            tile.name = getTileNameInput();
            mViewModel.insertTile(tile);

            // success
            return true;
        } else {
            // form has validation errors
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_tile_activity, menu);
        return true;
    }

    public void openPaneActivity() {
        Intent intent = new Intent(this, PaneActivity.class);
        intent.putExtra(PaneActivity.EXTRA_IS_TILE_ADDED, true);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_submit:
                if (processForm()) {
                    // form successfully handled, back to parent pane
                    openPaneActivity();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
