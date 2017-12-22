package vn.edu.hust.set.tung.musicplayer.model.statepattern;

import vn.edu.hust.set.tung.musicplayer.model.manager.PlayManager;

public class ShuffleRepeat implements State {

    PlayManager mPlayManager;

    public ShuffleRepeat(PlayManager playManager) {
        mPlayManager = playManager;
    }

    @Override
    public void shuffer() {
        mPlayManager.setState(mPlayManager.getRepeatingState());
    }

    @Override
    public void repeat() {
        mPlayManager.setState(mPlayManager.getShufferingState());
    }
}
