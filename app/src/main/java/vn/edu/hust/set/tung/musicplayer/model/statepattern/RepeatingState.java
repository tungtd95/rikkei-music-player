package vn.edu.hust.set.tung.musicplayer.model.statepattern;

import vn.edu.hust.set.tung.musicplayer.model.manager.PlayManager;

public class RepeatingState implements State {

    private PlayManager mPlayManager;

    public RepeatingState(PlayManager playManager) {
        mPlayManager = playManager;
    }

    @Override
    public void shuffer() {
        mPlayManager.setState(mPlayManager.getShuffleRepeatState());
    }

    @Override
    public void repeat() {
        mPlayManager.setState(mPlayManager.getNormalState());
    }
}
