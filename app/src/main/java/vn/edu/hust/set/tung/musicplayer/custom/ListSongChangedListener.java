package vn.edu.hust.set.tung.musicplayer.custom;

import java.util.ArrayList;

import vn.edu.hust.set.tung.musicplayer.model.obj.Song;

/**
 * Created by tungt on 11/28/17.
 */

public interface ListSongChangedListener {
    public void updateSong(ArrayList<Song> listSong, int index);
}
