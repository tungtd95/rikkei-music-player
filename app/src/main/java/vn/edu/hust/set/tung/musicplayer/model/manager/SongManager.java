package vn.edu.hust.set.tung.musicplayer.model.manager;

import java.util.ArrayList;

import vn.edu.hust.set.tung.musicplayer.model.obj.Album;
import vn.edu.hust.set.tung.musicplayer.model.obj.Artist;
import vn.edu.hust.set.tung.musicplayer.model.obj.Song;
import vn.edu.hust.set.tung.musicplayer.model.observerpattern.ResetSongObservable;
import vn.edu.hust.set.tung.musicplayer.model.observerpattern.ResetSongObserver;

/**
 * Created by tungt on 11/23/17.
 */

public class SongManager implements ResetSongObservable {
    private ArrayList<Song> listSong;
    private ArrayList<Album> listAlbum;
    private ArrayList<Artist> listArtist;

    private ArrayList<ResetSongObserver> listResetSongObserver;

    public SongManager() {
        listResetSongObserver = new ArrayList<>();
    }

    @Override
    public void register(ResetSongObserver observer) {
        listResetSongObserver.add(observer);
    }

    @Override
    public void notifyResetSong() {
        // TODO: 11/23/17 notify observer when list song and index song are changed 
    }

    public ArrayList<Song> getListSong() {
        return listSong;
    }

    public void setListSong(ArrayList<Song> listSong) {
        this.listSong = listSong;
    }

    public ArrayList<Album> getListAlbum() {
        return listAlbum;
    }

    public void setListAlbum(ArrayList<Album> listAlbum) {
        this.listAlbum = listAlbum;
    }

    public ArrayList<Artist> getListArtist() {
        return listArtist;
    }

    public void setListArtist(ArrayList<Artist> listArtist) {
        this.listArtist = listArtist;
    }
}
