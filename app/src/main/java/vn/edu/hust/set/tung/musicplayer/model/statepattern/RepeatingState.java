package vn.edu.hust.set.tung.musicplayer.model.statepattern;

import vn.edu.hust.set.tung.musicplayer.model.manager.PlayManager;

/**
 * Created by tungt on 11/23/17.
 */

public class RepeatingState implements State {

    private PlayManager mPlayManager;

    public RepeatingState(PlayManager playManager) {
        mPlayManager = playManager;
    }

    @Override
    public void shuffer() {

    }

    @Override
    public void repeat() {

    }
}
