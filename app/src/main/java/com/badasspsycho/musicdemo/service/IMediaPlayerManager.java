package com.badasspsycho.musicdemo.service;

import android.support.annotation.IntDef;

public interface IMediaPlayerManager {
    long getDuration();

    @MediaState
    int getMediaState();

    long getCurrentDuration();

    void playSong(int resourceId);

    void release();

    void stop();

    void seekTo(int mils);

    void changeMediaState();

    void addListener(OnMediaListener listener);

    boolean removeListener(OnMediaListener listener);

    void removeAllListener();

    @IntDef({
            MediaState.IDLE, MediaState.PREPARING, MediaState.PLAYING, MediaState.PAUSED,
            MediaState.STOPPED
    })
    @interface MediaState {
        int IDLE = 0;
        int PREPARING = 1;
        int PLAYING = 2;
        int PAUSED = 3;
        int STOPPED = 4;
    }

    interface OnMediaListener {
        void onStateChanged(@MediaState int mediaState);
    }
}
