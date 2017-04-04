package com.shakeup.setofthree.NormalGame;

import com.shakeup.setofthree.SetGame.GameContract;

/**
 * Created by Jayson on 4/4/2017.
 *
 * Contract between the View and Presenter for the Normal game mode
 */

public class NormalGameContract extends GameContract {
    /**
     * Methods that need to be implemented by the Normal Game View
     */
    interface View extends GameContract.View {

        void startTimer();

        void stopTimer();

        void updateDeckRemaining(int deckRemaining);

        void showLeaderBoard();

    }

    /**
     * Methods that need to be implemented by the Normal Game Presenter
     */
    interface UserActionsListener extends GameContract.UserActionsListener {

    }
}
