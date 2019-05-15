package com.example.pendulum;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

// deps for recyclerView spacing, see: https://www.androidhive.info/2016/05/android-working-with-card-view-and-recycler-view/
import android.graphics.Rect;
import android.content.res.Resources;
import android.util.TypedValue;

import java.util.ArrayList;

public class PaneActivity extends AppCompatActivity {

    private static final String TAG = "PaneActivity";

    private ArrayList<String> mTileTexts = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pane);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // set title (default: dashboard)
        this.setTitle(getResources().getString(R.string.activity_pane_title));

        // recycler
        initTileData();
        initRecyclerView();

        // fab
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void initTileData() {
        mTileTexts.add("Study");
        mTileTexts.add("Eat");
        mTileTexts.add("Sleep");
        mTileTexts.add("Sport");
        mTileTexts.add("Game");
//        mTileTexts.add("Hello World!");
//        mTileTexts.add("Bruno");
//        mTileTexts.add("awesome");
//        mTileTexts.add("Very long text option to test what the layout does in extreme cases. Let's make it even longer and longer and longer and longer and longer...");
//        mTileTexts.add("Hello World!");
//        mTileTexts.add("Bruno");
//        mTileTexts.add("awesome");
//        mTileTexts.add("Very long text option to test what the layout does in extreme cases. Let's make it even longer and longer and longer and longer and longer...");
//        mTileTexts.add("Hello World!");
//        mTileTexts.add("Bruno");
//        mTileTexts.add("awesome");
//        mTileTexts.add("Very long text option to test what the layout does in extreme cases. Let's make it even longer and longer and longer and longer and longer...");
    }

    private void initRecyclerView() {
        int spanCount = 2;
        recyclerView = (RecyclerView) findViewById(R.id.pane_recycler);
        recyclerView.setAdapter(new PaneRecyclerAdapter(this, mTileTexts));
        recyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount));
        recyclerView.setHasFixedSize(true); // content does not change layout size -> improve performance
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

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacingX;
        private int spacingY;
        private int marginFix;

        public GridSpacingItemDecoration(int spanCount) {
            this.spanCount = spanCount;
            this.spacingX = (int) getResources().getDimension(R.dimen.tile_space_between_horizontal);
            this.spacingY = (int) getResources().getDimension(R.dimen.tile_space_between_vertical);
            this.marginFix = (int) getResources().getDimension(R.dimen.tile_margin_fix);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int itemCount = parent.getAdapter().getItemCount();
            int column = position % spanCount; // item column

            outRect.left = (column == 0) ? (spacingX - marginFix) : (spacingX / 2 - marginFix);
            outRect.right = (column == spanCount - 1) ? (spacingX - marginFix) : (spacingX / 2 - marginFix);

            outRect.top = (position < spanCount) ? (spacingY - marginFix) : (spacingY / 2 - marginFix);
            outRect.bottom = (itemCount - position <= (itemCount - 1) % spanCount + 1) ? (spacingY - marginFix) : (spacingY / 2 - marginFix);
        }
    }
}
