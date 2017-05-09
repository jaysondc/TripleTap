package com.shakeup.setofthree.normalgame;

import com.shakeup.setgamelibrary.SetGame;
import com.shakeup.setofthree.setgame.GameContract;

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

        void startTimer(long timerElapsedMills);

        void stopTimer();

        void updateDeckRemaining(int deckRemaining);

        void uploadScore(long score);

        void saveLocalScore(long score, boolean uploaded);

        long getTimerElapsedTime();

    }

    /**
     * Methods that need to be implemented by the Normal Game Presenter
     */
    interface UserActionsListener extends GameContract.UserActionsListener {

        void initGame(SetGame existingGame, long timerElapsedMills);

        void onScoreUploaded(boolean uploaded);

    }
}