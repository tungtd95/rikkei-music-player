package vn.edu.hust.set.tung.musicplayer.model.manager;

import java.util.ArrayList;

import vn.edu.hust.set.tung.musicplayer.model.observerpattern.PlayManagerObservable;
import vn.edu.hust.set.tung.musicplayer.model.observerpattern.PlayManagerObserver;
import vn.edu.hust.set.tung.musicplayer.model.obj.Song;
import vn.edu.hust.set.tung.musicplayer.model.statepattern.NormalState;
import vn.edu.hust.set.tung.musicplayer.model.statepattern.RepeatingState;
import vn.edu.hust.set.tung.musicplayer.model.statepattern.ShufferingState;
import vn.edu.hust.set.tung.musicplayer.model.statepattern.State;

/**
 * Created by tungt on 11/23/17.
 */

public class PlayManager implements State, PlayManagerObservable {

    private State state;

    private State normalState;
    private State shufferingState;
    private State repeatingState;

    private int indexCurrentSong;
    private ArrayList<Song> listSong;
    private ArrayList<PlayManagerObserver> listPlayManagerObserver;

    public PlayManager() {
        normalState = new NormalState(this);
        shufferingState = new ShufferingState(this);
        repeatingState = new RepeatingState(this);
        state = normalState;

        indexCurrentSong = 0;
        listSong = new ArrayList<>();
        listPlayManagerObserver = new ArrayList<>();
    }

    @Override
    public void shuffer() {
        state.shuffer();
    }

    @Override
    public void repeat() {
        state.repeat();
    }

    public void updateListSong(ArrayList<Song> listSong, int indexSong) {
        this.listSong = listSong;
        this.indexCurrentSong = indexSong;
        notifySongChanged();
    }

    @Override
    public void register(PlayManagerObserver observer) {
        listPlayManagerObserver.add(observer);
    }

    @Override
    public void notifySongChanged() {
        for (PlayManagerObserver observer: listPlayManagerObserver) {
            observer.updateSong(listSong.get(indexCurrentSong));
        }
    }

    public ArrayList<Song> getListSong() {
        return listSong;
    }

    public void setListSong(ArrayList<Song> listSong) {
        this.listSong = listSong;
    }
}
