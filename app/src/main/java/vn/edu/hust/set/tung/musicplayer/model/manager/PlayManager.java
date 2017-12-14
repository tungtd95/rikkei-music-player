package vn.edu.hust.set.tung.musicplayer.model.manager;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import vn.edu.hust.set.tung.musicplayer.model.observerpattern.PlayManagerObservable;
import vn.edu.hust.set.tung.musicplayer.model.observerpattern.PlayManagerObserver;
import vn.edu.hust.set.tung.musicplayer.model.obj.Song;
import vn.edu.hust.set.tung.musicplayer.model.statepattern.NormalState;
import vn.edu.hust.set.tung.musicplayer.model.statepattern.RepeatingState;
import vn.edu.hust.set.tung.musicplayer.model.statepattern.ShufferingState;
import vn.edu.hust.set.tung.musicplayer.model.statepattern.ShuffleRepeat;
import vn.edu.hust.set.tung.musicplayer.model.statepattern.State;

import static vn.edu.hust.set.tung.musicplayer.activity.MainActivity.TAG;

/**
 * Created by tungt on 11/23/17.
 */

public class PlayManager extends Service implements State, PlayManagerObservable {

    private State state;

    private State normalState;
    private State shufferingState;
    private State repeatingState;
    private State shuffleRepeatState;

    private int indexCurrentSong;
    private ArrayList<Song> listSong;
    private ArrayList<PlayManagerObserver> listPlayManagerObserver;
    private MediaPlayer mMediaPlayer;
    int playingProgress = 0;
    int playingForProgressBar = 0;

    public PlayManager() {
        normalState = new NormalState(this);
        shufferingState = new ShufferingState(this);
        repeatingState = new RepeatingState(this);
        shuffleRepeatState = new ShuffleRepeat(this);
        state = normalState;

        indexCurrentSong = 0;
        listSong = new ArrayList<>();
        listPlayManagerObserver = new ArrayList<>();

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                handleNextSong();
            }
        });

        new ProgressAsync().execute("");

    }

    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    public void setUpLastState(ArrayList<Song> listSong, int indexLast, int progressLast) {
        this.listSong = listSong;
        this.indexCurrentSong = indexLast;
        try {
            mMediaPlayer.setDataSource(listSong.get(indexCurrentSong).getUri());
            mMediaPlayer.prepare();
            mMediaPlayer.seekTo(progressLast);
            notifyPlayingProgressChanged(progressLast/1000);
            notifyPlayingForProgressBar(progressLast, mMediaPlayer.getDuration());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleSeek(int percent) {
        try {
            int seekTo = mMediaPlayer.getDuration()*percent/100;
            mMediaPlayer.seekTo(seekTo);
        }catch (Exception e) {}
    }

    public void handleShuffle() {
        state.shuffer();
        notifyPlayManagerStateChanged();
    }

    public void handleRepeatOne() {
        state.repeat();
        notifyPlayManagerStateChanged();
    }

    public void handlePlayingState() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        } else if (!mMediaPlayer.isPlaying()) {
            try {
                mMediaPlayer.start();
            } catch (Exception e) {
            }
        }
        notifyPlayingStateChanged();
    }

    public void handlePause() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            notifyPlayingStateChanged();
        }
    }

    public void handlePlay() {
        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.start();
                notifyPlayingStateChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void handleNextSong() {
        if (state instanceof NormalState) {
            if (indexCurrentSong == listSong.size() - 1) {
                indexCurrentSong = 0;
            } else {
                indexCurrentSong++;
            }
        } else if (state instanceof ShufferingState) {
            indexCurrentSong = new Random().nextInt(listSong.size());
        }
        startNewSong();
    }

    public void handlePreviousSong() {
        if (state instanceof NormalState) {
            if (indexCurrentSong == 0) {
                indexCurrentSong = listSong.size() - 1;
            } else {
                indexCurrentSong--;
            }
        } else if (state instanceof ShufferingState) {
            indexCurrentSong = new Random().nextInt(listSong.size());
        }
        startNewSong();
    }

    public void startNewSong() {
        if (listSong.size() > 0) {
            mMediaPlayer.reset();
            try {
                mMediaPlayer.setDataSource(listSong.get(indexCurrentSong).getUri());
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            notifySongChanged();
            notifyPlayingStateChanged();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    public class MyBinder extends Binder {
        public PlayManager getPlayManagerService() {
            return PlayManager.this;
        }
    }

    @Override
    public void shuffer() {
        state.shuffer();
    }

    @Override
    public void repeat() {
        state.repeat();
    }

    public void updateListSong(ArrayList<Song> listSong, int indexSong) {
        this.listSong = listSong;
        this.indexCurrentSong = indexSong;
        startNewSong();
    }

    public void updateSong(int indexSong) {
        this.indexCurrentSong = indexSong;
        startNewSong();
    }

    @Override
    public void register(PlayManagerObserver observer) {
        listPlayManagerObserver.add(observer);
    }

    @Override
    public void notifySongChanged() {
        for (PlayManagerObserver observer : listPlayManagerObserver) {
            observer.updateSong(listSong.get(indexCurrentSong), indexCurrentSong);
        }
    }

    @Override
    public void notifyPlayingStateChanged() {
        for (PlayManagerObserver observer : listPlayManagerObserver) {
            observer.updatePlayingState(mMediaPlayer.isPlaying());
        }
    }

    @Override
    public void notifyPlayManagerStateChanged() {
        for (PlayManagerObserver observer : listPlayManagerObserver) {
            observer.updatePlayManagerState(state);
        }
    }

    @Override
    public void notifyPlayingProgressChanged(int progress) {
        for (PlayManagerObserver observer : listPlayManagerObserver) {
            observer.updatePlayingProgress(progress);
        }
    }

    @Override
    public void notifyPlayingForProgressBar(int progress, int duration) {
        for (PlayManagerObserver observer : listPlayManagerObserver) {
            observer.updateForProgressBar(progress, duration);
        }
    }

    public ArrayList<Song> getListSong() {
        return listSong;
    }

    public void setListSong(ArrayList<Song> listSong) {
        this.listSong = listSong;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public State getNormalState() {
        return normalState;
    }

    public void setNormalState(State normalState) {
        this.normalState = normalState;
    }

    public State getShufferingState() {
        return shufferingState;
    }

    public void setShufferingState(State shufferingState) {
        this.shufferingState = shufferingState;
    }

    public State getRepeatingState() {
        return repeatingState;
    }

    public void setRepeatingState(State repeatingState) {
        this.repeatingState = repeatingState;
    }

    public State getShuffleRepeatState() {
        return shuffleRepeatState;
    }

    public void setShuffleRepeatState(State shuffleRepeatState) {
        this.shuffleRepeatState = shuffleRepeatState;
    }

    public class ProgressAsync extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... strings) {
            while (true) {
                if (mMediaPlayer.isPlaying()) {
                    int temp = playingProgress;
                    try {
                        temp = mMediaPlayer.getCurrentPosition() / 1000;
                    } catch (Exception e) {
                    }
                    if (temp != playingProgress) {
                        playingProgress = temp;
                        notifyPlayingProgressChanged(playingProgress);
                    }
                    int temp2 = playingForProgressBar;
                    try {
                        temp2 = mMediaPlayer.getCurrentPosition();
                    } catch (Exception e) {
                    }
                    if (temp2 != playingForProgressBar) {
                        playingForProgressBar = temp2;
                        notifyPlayingForProgressBar(
                                playingForProgressBar,
                                mMediaPlayer.getDuration()
                        );
                    }
                }
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                }
            }
        }
    }

    public int getIndexCurrentSong() {
        return indexCurrentSong;
    }

    public void setIndexCurrentSong(int indexCurrentSong) {
        this.indexCurrentSong = indexCurrentSong;
    }
}
