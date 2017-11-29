package vn.edu.hust.set.tung.musicplayer.model.stratergypattern;

import java.util.ArrayList;

import vn.edu.hust.set.tung.musicplayer.model.obj.Song;

/**
 * Created by tungt on 11/28/17.
 */

public interface ListSongChangedListener {
    public void updateListSong(ArrayList<Song> listSong, int index);
}
