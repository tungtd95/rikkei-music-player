package vn.edu.hust.set.tung.musicplayer.model.observerpattern;

import java.util.ArrayList;

import vn.edu.hust.set.tung.musicplayer.model.obj.Album;
import vn.edu.hust.set.tung.musicplayer.model.obj.Artist;
import vn.edu.hust.set.tung.musicplayer.model.obj.Song;

public interface SongManagerObserver {
    void updateListSong(ArrayList<Song> listSong);

    void updateListAlbum(ArrayList<Album> listAlbum);

    void updateListArtist(ArrayList<Artist> listArtist);
}
