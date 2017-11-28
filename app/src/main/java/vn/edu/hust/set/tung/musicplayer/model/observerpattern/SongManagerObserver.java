package vn.edu.hust.set.tung.musicplayer.model.observerpattern;

import java.util.ArrayList;

import vn.edu.hust.set.tung.musicplayer.model.obj.Album;
import vn.edu.hust.set.tung.musicplayer.model.obj.Artist;
import vn.edu.hust.set.tung.musicplayer.model.obj.Song;

/**
 * Created by tungt on 11/23/17.
 */

public interface SongManagerObserver {
    public void updateListSong(ArrayList<Song> listSong);
    public void updateListAlbum(ArrayList<Album> listAlbum);
    public void updateListArtist(ArrayList<Artist> listArtist);
}
