package vn.edu.hust.set.tung.musicplayer.model.observerpattern;

import vn.edu.hust.set.tung.musicplayer.model.obj.Song;

/**
 * Created by tungt on 11/28/17.
 */

public interface PlayManagerObserver {
    public void updateSong(Song song);
}
