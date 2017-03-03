package com.shakeup.setofthree.MainMenu;

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

    interface View {

        void showSinglePlayerOptions();

        void showMultiPlayerOptions();

        void openSinglePlayerNormal();

    }

    /**
     * The presenter must implement the following methods to be used by the view
     */

    interface UserActionsListener {

        void startSinglePlayerNormal();

        void startSinglePlayerTimeAttack();

        void startMultiPlayer(int numPlayers);

        void openLeaderBoard();

        void openHowToPlay();

        void openSettings();

        void exitGame();

    }
}
