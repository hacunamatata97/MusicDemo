package com.badasspsycho.musicdemo.service;

import android.content.Context;
import android.media.MediaPlayer;
import java.util.ArrayList;
import java.util.List;

public class MediaPlayerManager implements IMediaPlayerManager {
    private MediaPlayer mMediaPlayer;
    private Context mContext;
    private @MediaState
    int mMediaState;
    private List<OnMediaListener> mListeners;

    public MediaPlayerManager(Context context) {
        mContext = context;
        mMediaState = MediaState.IDLE;
        mListeners = new ArrayList<>();
    }

    @Override
    public long getDuration() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getDuration();
        }
        return 0;
    }

    @Override
    public int getMediaState() {
        return mMediaState;
    }

    @Override
    public long getCurrentDuration() {
        return mMediaPlayer.getCurrentPosition();
    }

    @Override
    public void playSong(int resourceId) {
        mMediaPlayer = MediaPlayer.create(mContext, resourceId);
        mMediaPlayer.setVolume(100, 100);
        mMediaPlayer.start();
        mMediaState = MediaState.PLAYING;
        updateMediaStateChanged();
    }

    @Override
    public void release() {
        stop();
        mMediaPlayer.release();
        mMediaState = MediaState.STOPPED;
        updateMediaStateChanged();
    }

    @Override
    public void stop() {
        mMediaPlayer.stop();
        mMediaState = MediaState.STOPPED;
        updateMediaStateChanged();
    }

    @Override
    public void seekTo(int mils) {
        mMediaPlayer.seekTo(mils);
    }

    @Override
    public void changeMediaState() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            mMediaState = MediaState.PAUSED;
        } else {
            mMediaPlayer.start();
            mMediaState = MediaState.PLAYING;
        }
        updateMediaStateChanged();
    }

    @Override
    public void addListener(OnMediaListener listener) {
        mListeners.add(listener);
    }

    @Override
    public boolean removeListener(OnMediaListener listener) {
        return mListeners.remove(listener);
    }

    @Override
    public void removeAllListener() {
        if (mListeners != null && !mListeners.isEmpty()) {
            mListeners.clear();
        }
    }

    private void updateMediaStateChanged() {
        for (OnMediaListener listener : mListeners) {
            listener.onStateChanged(getMediaState());
        }
    }
}
