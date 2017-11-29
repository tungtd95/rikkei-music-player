package vn.edu.hust.set.tung.musicplayer.model.observerpattern;

/**
 * Created by tungt on 11/28/17.
 */

public interface PlayManagerObservable {
    public void register(PlayManagerObserver observer);
    public void notifySongChanged();
    public void notifyPlayingStateChanged();
    public void notifyPlayManagerStateChanged();
    public void notifyPlayingProgressChanged(int progress);
    public void notifyPlayingForProgressBar(int progress, int duration);
}
