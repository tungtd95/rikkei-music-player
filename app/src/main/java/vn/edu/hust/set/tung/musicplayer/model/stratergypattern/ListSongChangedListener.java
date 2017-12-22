package vn.edu.hust.set.tung.musicplayer.model.stratergypattern;

import java.util.ArrayList;

import vn.edu.hust.set.tung.musicplayer.model.obj.Song;

public interface ListSongChangedListener {
    void updateListSong(ArrayList<Song> listSong, int index);
}
