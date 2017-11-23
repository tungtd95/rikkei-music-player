package vn.edu.hust.set.tung.musicplayer.model.manager;

import java.util.ArrayList;

import vn.edu.hust.set.tung.musicplayer.model.obj.Song;
import vn.edu.hust.set.tung.musicplayer.model.observerpattern.ResetSongObservable;
import vn.edu.hust.set.tung.musicplayer.model.observerpattern.ResetSongObserver;
import vn.edu.hust.set.tung.musicplayer.model.statepattern.NormalState;
import vn.edu.hust.set.tung.musicplayer.model.statepattern.RepeatingState;
import vn.edu.hust.set.tung.musicplayer.model.statepattern.ShufferingState;
import vn.edu.hust.set.tung.musicplayer.model.statepattern.State;

/**
 * Created by tungt on 11/23/17.
 */

public class PlayManager implements State, ResetSongObserver {

    private State state;

    private State normalState;
    private State shufferingState;
    private State repeatingState;

    private int indexCurrentSong;
    private ArrayList<Song> listSong;

    public PlayManager(ResetSongObservable observable) {
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
    public void resetSong(ArrayList<Song> listSong, int indexSong) {
        this.listSong = listSong;
        this.indexCurrentSong = indexSong;
    }
}
