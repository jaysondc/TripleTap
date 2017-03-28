package com.shakeup.setofthree.MainMenu;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Jayson on 3/2/2017.
 */

public class MainMenuMultiplayerPresenter
        implements MainMenuMultiplayerContract.UserActionsListener {

    private final MainMenuMultiplayerContract.MultiplayerView mMultiplayerView;

    /**
     * Public constructor used to set up the presenter. Requires a reference to the calling MainView.
     * @param mainMultiplayerView A reference to the calling MainView
     */
    public MainMenuMultiplayerPresenter(
            @NonNull MainMenuMultiplayerContract.MultiplayerView mainMultiplayerView) {
        mMultiplayerView = checkNotNull(mainMultiplayerView, "mainMultiplayerView cannot be null!");
    }

    /**
     * Open a multi-player game with the specified number of players
     * @param numPlayers Number of players desired in Multiplayer game
     */
    @Override
    public void onMultiPlayerOptionClick(int numPlayers) {
        mMultiplayerView.openMultiPlayer(numPlayers);
    }

    @Override
    public void onBackClick() {
        mMultiplayerView.openPreviousFragment();
    }
}
