package com.shakeup.setofthree.timeattackgame;

import com.shakeup.setgamelibrary.SetGame;
import com.shakeup.setofthree.setgame.GameContract;

/**
 * Created by Jayson on 3/29/2017.
 * <p>
 * Contract between the View and Presenter for the Time Attack game mode
 */

public class TimeAttackGameContract extends GameContract {
    /**
     * Methods that need to be implemented by the Time Attack Game View
     */
    interface View extends GameContract.View {

        void startTimeAttackCountdown(long millisRemaining);

        void updateScore(long playerScore);

        void uploadScore(long score);

        void saveLocalScore(long score, boolean uploaded);

        void showHint();

        void updateHintButton(int hintsRemaining);

        void pauseGame();

        void resumeGame();

        void restartGame();

        void openMainMenu();

    }

    /**
     * Methods that need to be implemented by the Time Attack Game Presenter
     */
    interface UserActionsListener extends GameContract.UserActionsListener {

        void initGame(SetGame game, long timeRemaining, long playerScore);

        void onFindSetSuccess();

        void onFindSetFailure();

        void onTimeUp();

        long getPlayerScore();

        void onScoreUploaded(boolean uploaded);

        void onPauseClicked();

        void onHintClicked();

        void onPauseResultResume();

        void onPauseResultMainMenu();

        void onPauseResultRestart();

    }
}
