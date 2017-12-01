package vn.edu.hust.set.tung.musicplayer.model.observerpattern;

import vn.edu.hust.set.tung.musicplayer.model.obj.Song;
import vn.edu.hust.set.tung.musicplayer.model.statepattern.State;

/**
 * Created by tungt on 11/28/17.
 */

public interface PlayManagerObserver {
    public void updateSong(Song song, int index);
    public void updatePlayingState(boolean isPlaying);
    public void updatePlayManagerState(State state);
    public void updatePlayingProgress(int progress);
    public void updateForProgressBar(int progress, int duration);
}
