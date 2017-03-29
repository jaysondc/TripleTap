package com.shakeup.setofthree.MainMenu;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Jayson on 3/29/2017.
 *
 * This is the presenter that handles user input from the MainMenuSinglePlayerFragment
 */

public class MainMenuSinglePlayerPresenter
        implements MainMenuSinglePlayerContract.UserActionsListener {

    private final MainMenuSinglePlayerContract.SinglePlayerView mSinglePlayerView;

    /**
     * Public constructor used to set up the presenter. Requires a reference to the calling MainView.
     * @param mainSinglePlayerView A reference to the calling MainView
     */
    public MainMenuSinglePlayerPresenter(
            @NonNull MainMenuSinglePlayerContract.SinglePlayerView mainSinglePlayerView) {
        mSinglePlayerView = checkNotNull(mainSinglePlayerView, "mainSinglePlayerView cannot be null!");
    }

    /**
     * Open the UI handler for launching a Normal Game
     */
    @Override
    public void onNormalClick() {

    }

    /**
     * Open the UI handler for launching a Time Attack Game
     */
    @Override
    public void onTimeAttackClick() {

    }

    @Override
    public void onBackClick() {
        mSinglePlayerView.openPreviousFragment();
    }
}
