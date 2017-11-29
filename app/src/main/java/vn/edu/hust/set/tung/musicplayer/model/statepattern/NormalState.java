package vn.edu.hust.set.tung.musicplayer.model.statepattern;

import vn.edu.hust.set.tung.musicplayer.model.manager.PlayManager;

/**
 * Created by tungt on 11/23/17.
 */

public class NormalState implements State {

    private PlayManager mPlayManager;

    public NormalState(PlayManager playManager) {
        this.mPlayManager = playManager;
    }

    @Override
    public void shuffer() {
        mPlayManager.setState(mPlayManager.getShufferingState());
    }

    @Override
    public void repeat() {
        mPlayManager.setState(mPlayManager.getRepeatingState());
    }
}
