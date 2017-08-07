package com.shakeup.setofthree.normalgame;

import android.support.annotation.NonNull;

import com.shakeup.setgamelibrary.SetGame;
import com.shakeup.setofthree.setgame.GamePresenter;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Jayson on 4/4/2017.
 * <p>
 * Presenter for Normal game mode
 */

public class NormalGamePresenter extends GamePresenter
        implements NormalGameContract.UserActionsListener {

    // Hints given per game
    private final int HINTS_AVAILABLE = 3;

    private final String LOG_TAG = getClass().getSimpleName();
    boolean mScoreUploaded = false;
    private NormalGameContract.View mNormalGameView;
    private int mHintsAvailable = HINTS_AVAILABLE;

    // Supply a default constructor
    public NormalGamePresenter() {
    }

    /**
     * Public constructor used to set up the presenter. Requires a reference to the calling View.
     *
     * @param normalGameView A reference to the calling View
     */
    public NormalGamePresenter(
            @NonNull NormalGameContract.View normalGameView) {
        mNormalGameView =
                checkNotNull(normalGameView, "normalGameView cannot be null!");

        // Set our the view reference in our superclass
        setGameView(mNormalGameView);
    }

    /**
     * Initialize the timer and scoreboard for a new game
     */
    @Override
    public void initGame(SetGame existingGame, long timerElapsedMills) {
        super.initGame(existingGame);

        // Enable clicks
        mNormalGameView.setGameClickable(true);
        // Start a timer counting up
        mNormalGameView.startTimer(timerElapsedMills);
        // Show the cards remaining in the deck
        mNormalGameView.updateDeckRemaining(mSetGame.getDeckSize() / 3);
        // Reset the score uploaded state
        mScoreUploaded = false;
        // Show hints available
        mNormalGameView.updateHintButton(mHintsAvailable);
    }


    /*
     * Update the deck
     */
    @Override
    public void onSetSuccess() {
        mNormalGameView.updateDeckRemaining(mSetGame.getDeckSize() / 3);
        super.onSetSuccess();
    }

    /*
     * Show visual feedback that the set was invalid
     */
    @Override
    public void onSetFailure() {
        super.onSetFailure();
    }

    /*
     * Stop the timer and show the game over screen
     */
    @Override
    public void onGameOver() {
        super.onGameOver();
        mNormalGameView.stopTimer();

        // Upload the final score
        long finalScore = mNormalGameView.getTimerElapsedTime();
        if (!mIsDebug) {
            mNormalGameView.uploadScore(finalScore);
        }
        mNormalGameView.saveLocalScore(finalScore, mScoreUploaded);
    }

    /**
     * Set our member flag to show whether or not the score was
     * successfully uploaded
     *
     * @param uploaded Whether or not the score was uploaded
     */
    @Override
    public void onScoreUploaded(boolean uploaded) {
        mScoreUploaded = uploaded;
    }

    /**
     * Shows the user a hint if any are left to use.
     */
    @Override
    public void onHintClicked() {
        if (mHintsAvailable > 0) {
            if (showHint()) {
                mHintsAvailable--;
            }
        }
        mNormalGameView.updateHintButton(mHintsAvailable);
    }

    @Override
    public void onPauseClicked() {
        mNormalGameView.pauseGame();
    }

    @Override
    public void onPauseResultResume() {
        mNormalGameView.resumeGame();
    }

    @Override
    public void onPauseResultMainMenu() {
        mNormalGameView.openMainMenu();
    }

    @Override
    public void onPauseResultRestart() {
        mNormalGameView.restartGame();
    }

}
