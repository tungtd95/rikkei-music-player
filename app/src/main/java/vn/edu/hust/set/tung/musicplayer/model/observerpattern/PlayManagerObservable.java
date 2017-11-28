package vn.edu.hust.set.tung.musicplayer.model.observerpattern;

/**
 * Created by tungt on 11/28/17.
 */

public interface PlayManagerObservable {
    public void register(PlayManagerObserver observer);
    public void notifySongChanged();
}
