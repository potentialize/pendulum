package com.example.pendulum.timerActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.pendulum.R;
import com.example.pendulum.database.AppDatabase;

import java.time.Duration;

public class TimerActivity extends AppCompatActivity {

    private static final String TAG = "TimerActivity";

    // ACCEPTED INTENT DATA
    public static final String EXTRA_TILE_ID = "com.example.pendulum.TimerActivity.EXTRA_TILE_ID";
    public static final String EXTRA_ACTIVITY_ID = "com.example.pendulum.TimerActivity.EXTRA_ACTIVITY_ID";

    private Toolbar mToolbar;
    protected TimerActivityViewModel mViewModel;
    private TextView mTimerOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        // toolbar
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTimerOutput = findViewById(R.id.timer_output);

        // intent
        Intent intent = getIntent();

        // view model
        mViewModel = ViewModelProviders.of(this).get(TimerActivityViewModel.class);
        Long tileId = intent.getLongExtra(EXTRA_TILE_ID, 0L);
        Long actId = intent.getLongExtra(EXTRA_ACTIVITY_ID, 0L);
        mViewModel.bootstrap(tileId, actId);
        mViewModel.getTimer().observe(this, (Long timer) -> {
            mTimerOutput.setText(timer.toString());
        });
    }

    @Override
    public void onBackPressed() {
        mViewModel.closeActivity();
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        mViewModel.closeActivity();
        return super.onSupportNavigateUp();
    }
}
