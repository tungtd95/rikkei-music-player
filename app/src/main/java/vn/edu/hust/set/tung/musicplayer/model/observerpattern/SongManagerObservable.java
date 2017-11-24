package vn.edu.hust.set.tung.musicplayer.model.observerpattern;

/**
 * Created by tungt on 11/23/17.
 */

public interface SongManagerObservable {
    public void register(SongManagerObserver observer);
    public void notifyResetSong();
    public void notifyListSongChanged();
    public void notifyListArtistChanged();
    public void notifyListAlbumChanged();
}
