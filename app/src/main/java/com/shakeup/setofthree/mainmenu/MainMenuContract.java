package com.shakeup.setofthree.mainmenu;

/**
 * Created by Jayson on 3/2/2017.
 */

/**
 * This specifies the contract between the view and the presenter.
 */

public class MainMenuContract {

    /**
     * The view must implement the following methods to be used by the presenter
     */

    interface MainView {

        void openMultiplayerOptions();

        void openSinglePlayerOptions();

        void openLeaderboard();

        void openAchievements();

        void openSettings();

        void showHowToPlay();

    }

    /**
     * The presenter must implement the following methods to be used by the view
     */

    interface UserActionsListener {

        void onSinglePlayerClick();

        void onMultiplayerClick();

        void onLeaderBoardClick();

        void onAchievementsClick();

        void onHowToPlayClick();

        void onSettingsClick();

    }
}
