package com.shakeup.setofthree.GameOverScreen;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.shakeup.setofthree.ContentProvider.ScoreColumns;
import com.shakeup.setofthree.ContentProvider.ScoreProvider;
import com.shakeup.setofthree.R;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Jayson on 4/4/2017.
 *
 * Presenter for Normal game mode
 */

public class GameOverPresenter
        implements GameOverContract.UserActionsListener{

    private final String LOG_TAG = getClass().getSimpleName();

    private GameOverContract.View mGameOverView;

    // Supply a default constructor
    public GameOverPresenter(){
    }

    /**
     * Public constructor used to set up the presenter. Requires a reference to the calling View.
     * @param gameOverView A reference to the calling View
     */
    public GameOverPresenter(
            @NonNull GameOverContract.View gameOverView) {
        mGameOverView =
                checkNotNull(gameOverView, "gameOverView cannot be null!");
    }

    /*
     * Gets the cursor for the appropriate high scores and passes
     * it back to the view
     */
    @Override
    public void onViewCreated(Context context, String mode, String difficulty) {

        // Set up selection string
        String selection = ScoreColumns.MODE + "=?";

        // Set up selection args
        String[] selectionArgs = new String[1];
        selectionArgs[0] = context.getString(R.string.value_mode_normal);


        Cursor cursor = context.getContentResolver().query(
                ScoreProvider.Scores.SCORES,
                ScoreColumns._ALL,
                selection,
                selectionArgs,
                ScoreColumns.SCORE + " DESC"
        );

        cursor.close();
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
