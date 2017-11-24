package vn.edu.hust.set.tung.musicplayer.model.manager;

import java.util.ArrayList;

import vn.edu.hust.set.tung.musicplayer.model.obj.Album;
import vn.edu.hust.set.tung.musicplayer.model.obj.Artist;
import vn.edu.hust.set.tung.musicplayer.model.obj.Song;
import vn.edu.hust.set.tung.musicplayer.model.observerpattern.SongManagerObservable;
import vn.edu.hust.set.tung.musicplayer.model.observerpattern.SongManagerObserver;
import vn.edu.hust.set.tung.musicplayer.model.statepattern.NormalState;
import vn.edu.hust.set.tung.musicplayer.model.statepattern.RepeatingState;
import vn.edu.hust.set.tung.musicplayer.model.statepattern.ShufferingState;
import vn.edu.hust.set.tung.musicplayer.model.statepattern.State;

/**
 * Created by tungt on 11/23/17.
 */

public class PlayManager implements State, SongManagerObserver {

    private State state;

    private State normalState;
    private State shufferingState;
    private State repeatingState;

    private int indexCurrentSong;
    private ArrayList<Song> listSong;

    public PlayManager(SongManagerObservable observable) {
        observable.register(this);

        normalState = new NormalState(this);
        shufferingState = new ShufferingState(this);
        repeatingState = new RepeatingState(this);
        state = normalState;

        indexCurrentSong = 0;
        listSong = new ArrayList<>();
    }

    @Override
    public void shuffer() {
        state.shuffer();
    }

    @Override
    public void repeat() {
        state.repeat();
    }

    @Override
    public void updateSong(ArrayList<Song> listSong, int indexSong) {
        this.listSong = listSong;
        this.indexCurrentSong = indexSong;
    }

    @Override
    public void updateListSong(ArrayList<Song> listSong) {

    }

    @Override
    public void updateListAlbum(ArrayList<Album> listAlbum) {

    }

    @Override
    public void updateListArtist(ArrayList<Artist> listArtist) {

    }
}
