package com.shakeup.setofthree.gameoverscreen;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Jayson on 4/4/2017.
 * <p>
 * Presenter for Normal game mode
 */

public class GameOverPresenter
        implements GameOverContract.UserActionsListener {

    private final String LOG_TAG = getClass().getSimpleName();

    private GameOverContract.View mGameOverView;

    // Supply a default constructor
    public GameOverPresenter() {
    }

    /**
     * Public constructor used to set up the presenter. Requires a reference to the calling View.
     *
     * @param gameOverView A reference to the calling View
     */
    public GameOverPresenter(
            @NonNull GameOverContract.View gameOverView) {
        mGameOverView =
                checkNotNull(gameOverView, "gameOverView cannot be null!");
    }

    /*
     * Loads the local leaderboard. Model logic would normally go here
     * but we need to use Loaders so all the logic is in the GameOverFragment
     */
    @Override
    public void onOnCreateViewFinished() {
        mGameOverView.loadLocalLeaderboard();
    }

    /*
     * Starts a new game
     */
    @Override
    public void onRestartClicked() {
        mGameOverView.restartGame();
    }

    /*
     * Opens the global leaderboard
     */
    @Override
    public void onLeaderboardClicked() {
        mGameOverView.openLeaderboard();
    }

    /*
     * Opens the view showing all found sets
     */
    @Override
    public void onViewSetsClicked() {
        mGameOverView.openFoundSets();
    }

    /*
     * Opens the main menu
     */
    @Override
    public void onMainMenuClicked() {
        mGameOverView.openMainMenu();
    }


}
