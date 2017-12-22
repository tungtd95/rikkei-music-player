package vn.edu.hust.set.tung.musicplayer.model.observerpattern;

import vn.edu.hust.set.tung.musicplayer.model.obj.Song;
import vn.edu.hust.set.tung.musicplayer.model.statepattern.State;

public interface PlayManagerObserver {
    void updateSong(Song song, int index);
    void updatePlayingState(boolean isPlaying);
    void updatePlayManagerState(State state);
    void updatePlayingProgress(int progress);
    void updateForProgressBar(int progress, int duration);
}
