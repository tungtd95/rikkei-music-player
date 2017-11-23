package vn.edu.hust.set.tung.musicplayer.model.observerpattern;

import java.util.ArrayList;

import vn.edu.hust.set.tung.musicplayer.model.obj.Song;

/**
 * Created by tungt on 11/23/17.
 */

public interface ResetSongObserver {
    public void resetSong(ArrayList<Song> listSong, int indexSong);
}
