package vn.edu.hust.set.tung.musicplayer.model.observerpattern;

public interface PlayManagerObservable {
    void register(PlayManagerObserver observer);
    void notifySongChanged();
    void notifyPlayingStateChanged();
    void notifyPlayManagerStateChanged();
    void notifyPlayingProgressChanged(int progress);
    void notifyPlayingForProgressBar(int progress, int duration);
}
