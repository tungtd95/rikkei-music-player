package vn.edu.hust.set.tung.musicplayer.model.observerpattern;

public interface SongManagerObservable {
    void register(SongManagerObserver observer);

    void notifyListSongChanged();

    void notifyListArtistChanged();

    void notifyListAlbumChanged();
}
