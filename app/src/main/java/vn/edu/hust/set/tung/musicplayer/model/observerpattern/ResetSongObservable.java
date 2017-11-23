package vn.edu.hust.set.tung.musicplayer.model.observerpattern;

/**
 * Created by tungt on 11/23/17.
 */

public interface ResetSongObservable {
    public void register(ResetSongObserver observer);
    public void notifyResetSong();
}
