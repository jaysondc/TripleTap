package com.shakeup.setofthree.TimeAttackGame;

import android.support.annotation.NonNull;

import com.shakeup.setofthree.SetGame.GameContract;
import com.shakeup.setofthree.SetGame.GamePresenter;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Jayson on 3/29/2017.
 *
 * Presenter for Time Attack game mode
 */

public class TimeAttackGamePresenter extends GamePresenter
        implements TimeAttackGameContract.UserActionsListener{

    private final String LOG_TAG = getClass().getSimpleName();

    private TimeAttackGameContract.View mTimeAttackGameView;

    int mPlayerScore = 0;

    // Supply a default constructor
    public TimeAttackGamePresenter(){
    }

    /**
     * Public constructor used to set up the presenter. Requires a reference to the calling View.
     * @param timeAttackGameView A reference to the calling View
     */
    public TimeAttackGamePresenter(
            @NonNull TimeAttackGameContract.View timeAttackGameView) {
        mTimeAttackGameView =
                checkNotNull(timeAttackGameView, "timeAttackGameView cannot be null!");

        // Set our the view reference in our superclass
        setGameView((GameContract.View) mTimeAttackGameView);
    }

    /**
     * Initialize the timer and scoreboard for a new game
     */
    @Override
    public void initGame() {
        super.initGame();

        // Set our deck to endless mode
        mSetGame.setEndlessMode(true);
        // Enable clicks
        mTimeAttackGameView.setGameClickable(true);
        // Start a time attack counter
        mTimeAttackGameView.startTimeAttackCountdown();
        // Reset the score
        mPlayerScore = 0;
        mTimeAttackGameView.updateScore(0);
    }

    @Override
    public void onFindSetSuccess() {
        mPlayerScore++;
        mTimeAttackGameView.updateScore(mPlayerScore);
    }

    @Override
    public void onFindSetFailure() {

    }

    @Override
    public void onTimeUp() {
        mTimeAttackGameView.showGameOver(mPlayerScore);
        mTimeAttackGameView.setGameClickable(false);
    }
}
