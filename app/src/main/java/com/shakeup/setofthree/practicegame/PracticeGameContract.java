package com.shakeup.setofthree.practicegame;

import com.shakeup.setgamelibrary.SetGame;
import com.shakeup.setofthree.setgame.GameContract;

/**
 * Created by Jayson on 8/7/2017.
 * <p>
 * Contract between the View and Presenter for the Normal game mode
 */

public class PracticeGameContract extends GameContract {
    /**
     * Methods that need to be implemented by the Normal Game View
     */
    interface View extends GameContract.View {

        void pauseGame();

        void resumeGame();

        void restartGame();

        void openMainMenu();

    }

    /**
     * Methods that need to be implemented by the Normal Game Presenter
     */
    interface UserActionsListener extends GameContract.UserActionsListener {

        void initGame(SetGame existingGame);

        void onHintClicked();

        void onPauseClicked();

        void onPauseResultResume();

        void onPauseResultMainMenu();

        void onPauseResultRestart();

    }
}
