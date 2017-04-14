package com.shakeup.setofthree.GameOverScreen;

import android.support.annotation.NonNull;

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





}
