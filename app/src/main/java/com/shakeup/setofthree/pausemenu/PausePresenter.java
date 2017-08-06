package com.shakeup.setofthree.pausemenu;

import android.support.annotation.NonNull;

import static android.support.v4.util.Preconditions.checkNotNull;

/**
 * Created by Jayson Dela Cruz on 8/4/2017.
 *
 * Presenter to handle logic of the Pause screen
 */

public class PausePresenter implements PauseContract.UserActionsListener {

    PauseContract.PauseView mPauseView;

    public PausePresenter(
            @NonNull PauseContract.PauseView pauseView) {
        mPauseView =
                checkNotNull(pauseView, "pauseView cannot be null!");
    }

    @Override
    public void onRestartClicked() {
        mPauseView.restartGame();
    }

    @Override
    public void onMainMenuClicked() {
        mPauseView.openMainMenu();
    }

    @Override
    public void onResumeClicked() {
        mPauseView.resumeGame();
    }

}
