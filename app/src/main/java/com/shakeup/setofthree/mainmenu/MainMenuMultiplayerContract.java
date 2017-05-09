package com.shakeup.setofthree.mainmenu;

/**
 * Created by Jayson on 3/2/2017.
 */

/**
 * This specifies the contract between the view and the presenter.
 */

public class MainMenuMultiplayerContract {

    interface MultiplayerView {

        void openMultiPlayer(int numPlayers);

        void openPreviousFragment();

    }

    interface UserActionsListener {

        void onMultiPlayerOptionClick(int numPlayers);

        void onBackClick();

    }
}
