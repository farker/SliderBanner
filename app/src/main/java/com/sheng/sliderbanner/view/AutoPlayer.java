package com.sheng.sliderbanner.view;

import android.os.Handler;
import android.os.Looper;

public class AutoPlayer {
    private static final int DEFAULT_INTERVAL = 3000;
    private AutoPlayer.PlayDirection mDirection;
    private AutoPlayer.PlayRecycleMode mPlayRecycleMode;
    private int mTimeInterval;
    private AutoPlayer.Playable mPlayable;
    private Runnable mTimerTask;
    private boolean mSkipNext;
    private int mTotal;
    private boolean mPlaying;
    private boolean mPaused;

    public AutoPlayer(AutoPlayer.Playable playable) {
        this.mDirection = AutoPlayer.PlayDirection.to_right;
        this.mPlayRecycleMode = AutoPlayer.PlayRecycleMode.repeat_from_start;
        this.mTimeInterval = DEFAULT_INTERVAL;
        this.mSkipNext = false;
        this.mPlaying = false;
        this.mPaused = false;
        this.mPlayable = playable;
    }

    public void play(int start, AutoPlayer.PlayDirection direction) {
        if (!this.mPlaying) {
            this.mTotal = this.mPlayable.getTotal();
            if (this.mTotal > 1) {
                this.mPlaying = true;
                this.playTo(start);
                final Handler handler = new Handler(Looper.myLooper());
                this.mTimerTask = new Runnable() {
                    public void run() {
                        if (!AutoPlayer.this.mPaused) {
                            AutoPlayer.this.playNextFrame();
                        }

                        if (AutoPlayer.this.mPlaying) {
                            handler.postDelayed(AutoPlayer.this.mTimerTask, (long) AutoPlayer.this.mTimeInterval);
                        }
                    }
                };
                handler.postDelayed(this.mTimerTask, (long) this.mTimeInterval);
            }
        }
    }

    public void play(int start) {
        this.play(start, AutoPlayer.PlayDirection.to_right);
    }

    public void play() {
        this.play(0, AutoPlayer.PlayDirection.to_right);
    }

    public void stop() {
        if (this.mPlaying) {
            this.mPlaying = false;
        }
    }

    public void pause() {
        this.mPaused = true;
    }

    public void resume() {
        this.mPaused = false;
    }

    public void skipNext() {
        this.mSkipNext = true;
    }

    public AutoPlayer setTimeInterval(int timeInterval) {
        this.mTimeInterval = timeInterval;
        return this;
    }

    public AutoPlayer setPlayRecycleMode(AutoPlayer.PlayRecycleMode playRecycleMode) {
        this.mPlayRecycleMode = playRecycleMode;
        return this;
    }

    private void playNextFrame() {
        if (this.mSkipNext) {
            this.mSkipNext = false;
        } else {
            int current = this.mPlayable.getCurrent();
            if (this.mDirection == AutoPlayer.PlayDirection.to_right) {
                if (current == this.mTotal - 1) {
                    if (this.mPlayRecycleMode == AutoPlayer.PlayRecycleMode.play_back) {
                        this.mDirection = AutoPlayer.PlayDirection.to_left;
                        this.playNextFrame();
                    } else {
                        this.playTo(0);
                    }
                } else {
                    this.playNext();
                }
            } else if (current == 0) {
                if (this.mPlayRecycleMode == AutoPlayer.PlayRecycleMode.play_back) {
                    this.mDirection = AutoPlayer.PlayDirection.to_right;
                    this.playNextFrame();
                } else {
                    this.playTo(this.mTotal - 1);
                }
            } else {
                this.playPrevious();
            }
        }
    }

    private void playTo(int to) {
        this.mPlayable.playTo(to);
    }

    private void playNext() {
        this.mPlayable.playNext();
    }

    private void playPrevious() {
        this.mPlayable.playPrevious();
    }

    public static enum PlayRecycleMode {
        repeat_from_start,
        play_back;

        private PlayRecycleMode() {
        }
    }

    public static enum PlayDirection {
        to_left,
        to_right;

        private PlayDirection() {
        }
    }

    public interface Playable {
        void playTo(int var1);

        void playNext();

        void playPrevious();

        int getTotal();

        int getCurrent();
    }
}
