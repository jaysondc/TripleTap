package com.shakeup.setofthree.NormalGame;

import android.support.annotation.NonNull;

import com.shakeup.setofthree.SetGame.GameContract;
import com.shakeup.setofthree.SetGame.GamePresenter;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Jayson on 4/4/2017.
 *
 * Presenter for Normal game mode
 */

public class NormalGamePresenter extends GamePresenter
        implements NormalGameContract.UserActionsListener{

    private final String LOG_TAG = getClass().getSimpleName();

    private NormalGameContract.View mNormalGameView;

    int mPlayerScore = 0;
    boolean mScoreUploaded = false;

    // Supply a default constructor
    public NormalGamePresenter(){
    }

    /**
     * Public constructor used to set up the presenter. Requires a reference to the calling View.
     * @param normalGameView A reference to the calling View
     */
    public NormalGamePresenter(
            @NonNull NormalGameContract.View normalGameView) {
        mNormalGameView =
                checkNotNull(normalGameView, "normalGameView cannot be null!");

        // Set our the view reference in our superclass
        setGameView((GameContract.View) mNormalGameView);
    }

    /**
     * Initialize the timer and scoreboard for a new game
     */
    @Override
    public void initGame() {
        super.initGame();

        // Enable clicks
        mNormalGameView.setGameClickable(true);
        // Start a timer counting up
        mNormalGameView.startTimer();
        // Show the cards remaining in the deck
        mNormalGameView.updateDeckRemaining(mSetGame.getDeckSize() / 3);
        // Reset the score uploaded state
        mScoreUploaded = false;
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
        long finalScore = mNormalGameView.getScore();
        mNormalGameView.uploadScore(finalScore);
        mNormalGameView.saveLocalScore(finalScore, mScoreUploaded);
    }

    /**
     * Set our member flag to show whether or not the score was
     * successfully uploaded
     * @param uploaded Whether or not the score was uploaded
     */
    @Override
    public void onScoreUploaded(boolean uploaded) {
        mScoreUploaded = uploaded;
    }
}
