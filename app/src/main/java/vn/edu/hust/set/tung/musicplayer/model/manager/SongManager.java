package vn.edu.hust.set.tung.musicplayer.model.manager;

import java.util.ArrayList;

import vn.edu.hust.set.tung.musicplayer.model.obj.Album;
import vn.edu.hust.set.tung.musicplayer.model.obj.Artist;
import vn.edu.hust.set.tung.musicplayer.model.obj.Song;
import vn.edu.hust.set.tung.musicplayer.model.observerpattern.SongManagerObservable;
import vn.edu.hust.set.tung.musicplayer.model.observerpattern.SongManagerObserver;

/**
 * Created by tungt on 11/23/17.
 */

public class SongManager implements SongManagerObservable {
    private ArrayList<Song> listSong;
    private ArrayList<Album> listAlbum;
    private ArrayList<Artist> listArtist;

    private ArrayList<SongManagerObserver> listSongManagerObserver;

    public SongManager() {
        listSongManagerObserver = new ArrayList<>();
    }

    @Override
    public void register(SongManagerObserver observer) {
        listSongManagerObserver.add(observer);
    }

    @Override
    public void notifyListSongChanged() {
        for (SongManagerObserver observer : listSongManagerObserver) {
            observer.updateListSong(listSong);
        }
    }

    @Override
    public void notifyListArtistChanged() {
        for (SongManagerObserver observer : listSongManagerObserver) {
            observer.updateListArtist(listArtist);
        }
    }

    @Override
    public void notifyListAlbumChanged() {
        for (SongManagerObserver observer : listSongManagerObserver) {
            observer.updateListAlbum(listAlbum);
        }
    }

    public ArrayList<Song> getListSong() {
        return listSong;
    }

    public void setListSong(ArrayList<Song> listSong) {
        this.listSong = listSong;
        notifyListSongChanged();
    }

    public ArrayList<Album> getListAlbum() {
        return listAlbum;
    }

    public void setListAlbum(ArrayList<Album> listAlbum) {
        this.listAlbum = listAlbum;
        notifyListAlbumChanged();
    }

    public ArrayList<Artist> getListArtist() {
        return listArtist;
    }

    public void setListArtist(ArrayList<Artist> listArtist) {
        this.listArtist = listArtist;
        notifyListArtistChanged();
    }
}
