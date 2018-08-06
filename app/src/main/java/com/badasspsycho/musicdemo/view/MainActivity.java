package com.badasspsycho.musicdemo.view;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.badasspsycho.musicdemo.R;
import com.badasspsycho.musicdemo.Utilities;
import com.badasspsycho.musicdemo.service.IMediaPlayerManager;
import com.badasspsycho.musicdemo.service.PlayerService;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, SeekBar.OnSeekBarChangeListener,
        IMediaPlayerManager.OnMediaListener {

    private final String TAG = MainActivity.class.getSimpleName();

    private SeekBar mSeekBar;
    private TextView mCurrentTime;
    private TextView mTotalTime;
    private ImageView mButtonPrevious;
    private ImageView mButtonPlay;
    private ImageView mButtonNext;

    private PlayerService mService;
    private boolean mBound = false;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayerService.MyBinder binder = (PlayerService.MyBinder) service;
            mService = binder.getService();
            mService.addListener(MainActivity.this);
            mService.playSong(R.raw.shigatsu_wa_kimi_no_uso);
            updateDuration();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            long currentTime = mService.getCurrentDuration();
            updateCurrentDuration(currentTime);
            mHandler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, PlayerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mBound) {
            updateDuration();
        }
    }

    private void updateDuration() {
        mTotalTime.setText(Utilities.milliSecondsToTimer(mService.getDuration()));
        mSeekBar.setMax((int) mService.getDuration());
        mHandler.postDelayed(mRunnable, 0);
    }

    private void updateCurrentDuration(long currentDuration) {
        mSeekBar.setProgress((int) currentDuration);
        mCurrentTime.setText(Utilities.milliSecondsToTimer(currentDuration));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(mConnection);
        mBound = false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_previous:
                break;
            case R.id.iv_play_pause:
                mService.changeMediaState();
                break;
            case R.id.iv_next:
                break;
        }
    }

    private void initializeViews() {
        mSeekBar = findViewById(R.id.sb_progress);
        mCurrentTime = findViewById(R.id.tv_current_time);
        mTotalTime = findViewById(R.id.tv_total_time);
        mButtonPrevious = findViewById(R.id.iv_previous);
        mButtonPlay = findViewById(R.id.iv_play_pause);
        mButtonNext = findViewById(R.id.iv_next);
        mSeekBar.setOnSeekBarChangeListener(this);
        mButtonPlay.setOnClickListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int progress = seekBar.getProgress();
        mService.seekTo(progress);
    }

    @Override
    public void onStateChanged(int mediaState) {
        switch (mediaState) {
            case IMediaPlayerManager.MediaState.PLAYING:
                mButtonPlay.setImageResource(R.drawable.btn_pause_on_press);
                break;
            case IMediaPlayerManager.MediaState.IDLE:
            case IMediaPlayerManager.MediaState.PAUSED:
            case IMediaPlayerManager.MediaState.STOPPED:
                mButtonPlay.setImageResource(R.drawable.btn_play_on_press);
                break;
            case IMediaPlayerManager.MediaState.PREPARING:
                showProgressBar();
                break;
        }
    }

    private void showProgressBar() {
        // TODO: 8/6/2018
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Loading").setMessage("Music is loading, please wait!");
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
