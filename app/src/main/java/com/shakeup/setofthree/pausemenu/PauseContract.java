package com.shakeup.setofthree.pausemenu;

/**
 * Created by Jayson Dela Cruz on 8/4/2017.
 *
 * Contract between the PauseView and Presenter for the pause menu.
 */

public class PauseContract {

    public static final int RESULT_RESUME = 0;
    public static final int RESULT_RESTART = 1;
    public static final int RESULT_MAIN_MENU = 2;

    interface PauseView {

        void restartGame();

        void openMainMenu();

        void resumeGame();

        // The view also needs to override onActivityResult
        // to receive the result from the pause fragment

    }

    interface UserActionsListener {

        void onRestartClicked();

        void onMainMenuClicked();

        void onResumeClicked();

    }

}
