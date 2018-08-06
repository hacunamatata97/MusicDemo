package com.badasspsycho.musicdemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class PlayerService extends Service implements IMediaPlayerManager{

    private final IBinder mIBinder = new PlayerService.MyBinder();
    private IMediaPlayerManager mMediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        initMediaPlayer();
    }

    private void initMediaPlayer() {
        mMediaPlayer = new MediaPlayerManager(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mIBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mMediaPlayer.release();
        return super.onUnbind(intent);
    }

    public long getDuration() {
        return mMediaPlayer.getDuration();
    }

    @Override
    public int getMediaState() {
        return mMediaPlayer.getMediaState();
    }

    @Override
    public long getCurrentDuration() {
        return mMediaPlayer.getCurrentDuration();
    }

    @Override
    public void playSong(int resourceId) {
        mMediaPlayer.playSong(resourceId);
    }

    @Override
    public void release() {
        mMediaPlayer.release();
    }

    @Override
    public void stop() {
        mMediaPlayer.stop();
    }

    @Override
    public void seekTo(int mils) {
        mMediaPlayer.seekTo(mils);
    }

    @Override
    public void changeMediaState() {
        mMediaPlayer.changeMediaState();
    }

    @Override
    public void addListener(OnMediaListener listener) {
        mMediaPlayer.addListener(listener);
    }

    @Override
    public boolean removeListener(OnMediaListener listener) {
        return mMediaPlayer.removeListener(listener);
    }

    @Override
    public void removeAllListener() {
        mMediaPlayer.removeAllListener();
    }

    public class MyBinder extends Binder {

        public PlayerService getService() {
            return PlayerService.this;
        }
    }
}
