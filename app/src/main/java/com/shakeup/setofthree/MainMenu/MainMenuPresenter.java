package com.shakeup.setofthree.MainMenu;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Jayson on 3/2/2017.
 */

public class MainMenuPresenter implements MainMenuContract.UserActionsListener {

    private final MainMenuContract.MainView mMainMenuView;

    /**
     * Public constructor used to set up the presenter. Requires a reference to the calling MainView.
     * @param mainMenuView A reference to the calling MainView
     */
    public MainMenuPresenter(
            @NonNull MainMenuContract.MainView mainMenuView) {
        mMainMenuView = checkNotNull(mainMenuView, "mainMenu cannot be null!");
    }

    /**
     * Open a single player game in normal mode
     */
    @Override
    public void onSinglePlayerNormalClick() {
        mMainMenuView.openSinglePlayerOptions();
    }

    @Override
    public void onSinglePlayerTimeAttackClick() {

    }

    @Override
    public void onSinglePlayerClick() {

    }

    /**
     * Open a multi-player game with the specified number of players
     * @param numPlayers
     */
    @Override
    public void onMultiPlayerOptionClick(int numPlayers) {
        mMainMenuView.openMultiplayerOptions();
    }

    /**
     * Open start the view handler to show the multiplayer options
     */
    @Override
    public void onMultiplayerClick() {
        mMainMenuView.openMultiplayerOptions();
    }

    @Override
    public void onLeaderBoardClick() {

    }

    @Override
    public void onSettingsClick() {

    }

    @Override
    public void ohHowToPlayClick() {

    }

    @Override
    public void onExitGameClick() {
        // Open exit confirm dialog in the view
    }
}
