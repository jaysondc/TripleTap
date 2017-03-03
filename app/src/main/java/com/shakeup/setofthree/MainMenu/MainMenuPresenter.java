package com.shakeup.setofthree.MainMenu;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Jayson on 3/2/2017.
 */

public class MainMenuPresenter implements MainMenuContract.UserActionsListener {

    private final MainMenuContract.View mMainMenuView;

    /**
     * Public constructor used to set up the presenter. Requires a reference to the calling View.
     * @param mainMenuView A reference to the calling View
     */
    public MainMenuPresenter(
            @NonNull MainMenuContract.View mainMenuView) {
        mMainMenuView = checkNotNull(mainMenuView, "mainMenu cannot be null!");
    }

    /**
     * Open a single player game in normal mode
     */
    @Override
    public void startSinglePlayerNormal() {
        mMainMenuView.openSinglePlayerNormal();
    }

    @Override
    public void startSinglePlayerTimeAttack() {

    }

    @Override
    public void startMultiPlayer(int numPlayers) {

    }

    @Override
    public void openLeaderBoard() {

    }

    @Override
    public void openSettings() {

    }

    @Override
    public void openHowToPlay() {

    }

    @Override
    public void exitGame() {
        // Open exit confirm dialog in the view
    }
}
