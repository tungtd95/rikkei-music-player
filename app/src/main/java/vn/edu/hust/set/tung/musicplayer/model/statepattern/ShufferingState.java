package vn.edu.hust.set.tung.musicplayer.model.statepattern;

import vn.edu.hust.set.tung.musicplayer.model.manager.PlayManager;

public class ShufferingState implements State {

    private PlayManager mPlayManager;

    public ShufferingState(PlayManager playManager) {
        this.mPlayManager = playManager;
    }

    @Override
    public void shuffer() {
        mPlayManager.setState(mPlayManager.getNormalState());
    }

    @Override
    public void repeat() {
        mPlayManager.setState(mPlayManager.getShuffleRepeatState());
    }
}
