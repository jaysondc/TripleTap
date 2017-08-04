package com.shakeup.setofthree.pausemenu;

/**
 * Created by Jayson Dela Cruz on 8/4/2017.
 *
 * Contract between the View and Presenter for the pause menu
 */

public class PauseContract {

    public static int RESULT_RESUME = 0;
    public static int RESULT_RESTART = 1;
    public static int RESULT_MAIN_MENU = 2;

    interface View {

        void restartGame();

        void openMainMenu();

        void resumeGame();

    }

    interface UserActionsListener {

        void onRestartClicked();

        void onMainMenuClicked();

        void onResumeClicked();

    }

}
