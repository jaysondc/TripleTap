package com.shakeup.setofthree.normalgame;

import com.shakeup.setgamelibrary.SetGame;
import com.shakeup.setofthree.setgame.GameContract;

/**
 * Created by Jayson on 4/4/2017.
 * <p>
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

        void pauseGame();

        void showHint();

        void updateHintButton(int hintsRemaining);

        void resumeGame();

        void restartGame();

        void openMainMenu();

    }

    /**
     * Methods that need to be implemented by the Normal Game Presenter
     */
    interface UserActionsListener extends GameContract.UserActionsListener {

        void initGame(SetGame existingGame, long timerElapsedMills);

        void onScoreUploaded(boolean uploaded);

        void onHintClicked();

        void onPauseClicked();

        void onPauseResultResume();

        void onPauseResultMainMenu();

        void onPauseResultRestart();

    }
}
