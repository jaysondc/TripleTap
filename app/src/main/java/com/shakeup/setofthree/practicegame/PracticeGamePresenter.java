package com.shakeup.setofthree.practicegame;

import android.support.annotation.NonNull;

import com.shakeup.setgamelibrary.SetGame;
import com.shakeup.setofthree.setgame.GamePresenter;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Jayson on 8/7/2017.
 * <p>
 * Presenter for Practice game mode
 */

public class PracticeGamePresenter extends GamePresenter
        implements PracticeGameContract.UserActionsListener {

    private final String LOG_TAG = getClass().getSimpleName();
    private PracticeGameContract.View mPracticeGameView;

    // Supply a default constructor
    public PracticeGamePresenter() {
    }

    /**
     * Public constructor used to set up the presenter. Requires a reference to the calling View.
     *
     * @param practiceGameView A reference to the calling View
     */
    public PracticeGamePresenter(
            @NonNull PracticeGameContract.View practiceGameView) {
        mPracticeGameView =
                checkNotNull(practiceGameView, "practiceGameView cannot be null!");

        // Set our the view reference in our superclass
        setGameView(mPracticeGameView);
    }

    /**
     * Set up a practice mode game
     */
    @Override
    public void initGame(SetGame existingGame) {
        super.initGame(existingGame);
        // Enable clicks
        mPracticeGameView.setGameClickable(true);
    }


    /*
     * Update the deck
     */
    @Override
    public void onSetSuccess() {
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
     * Nothing to do here because practice mode doesn't end
     */
    @Override
    public void onGameOver() {
        super.onGameOver();
    }

    @Override
    public void onPauseClicked() {
        mPracticeGameView.pauseGame();
    }

    @Override
    public void onPauseResultResume() {
        mPracticeGameView.resumeGame();
    }

    @Override
    public void onPauseResultMainMenu() {
        mPracticeGameView.openMainMenu();
    }

    @Override
    public void onPauseResultRestart() {
        mPracticeGameView.restartGame();
    }

}
