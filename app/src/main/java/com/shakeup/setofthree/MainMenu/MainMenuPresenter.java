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
     * Open the view handler to show the single player options
     */
    @Override
    public void onSinglePlayerClick() {
        mMainMenuView.openSinglePlayerOptions();
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
        mMainMenuView.openLeaderboard();
    }

    @Override
    public void onAchievementsClick() {
        mMainMenuView.openAchievements();
    }

    @Override
    public void onSettingsClick() {
        mMainMenuView.openSettings();
    }

    @Override
    public void onHowToPlayClick() {
        mMainMenuView.showHowToPlay();
    }

    @Override
    public void onExitGameClick() {
        // Open exit confirm dialog in the view
        mMainMenuView.exitGame();
    }
}
