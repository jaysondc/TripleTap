package com.shakeup.setofthree.gameoverscreen;

/**
 * Created by Jayson on 4/13/2017.
 * <p>
 * Contract between the View and Presenter for the game over screen
 */

public class GameOverContract {
    /**
     * Methods that need to be implemented by the Game Over View
     */
    interface View {

        void restartGame(String gameMode, String gameDifficulty);

        void openLeaderboard();

        void loadLocalLeaderboard(String gameMode, String gameDifficulty);

        void openFoundSets();

        void openMainMenu();

    }

    /**
     * Methods that need to be implemented by the Game Over Presenter
     */
    interface UserActionsListener {

        void onRestartClicked();

        void onLeaderboardClicked();

        void onViewSetsClicked();

        void onMainMenuClicked();

        void onCreateViewFinished(String mode, String difficulty);

    }
}
