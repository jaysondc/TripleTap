package com.shakeup.setofthree.MultiplayerGame;

import android.support.annotation.NonNull;
import android.util.Log;

import com.shakeup.setofthree.SetGame.GameContract;
import com.shakeup.setofthree.SetGame.GamePresenter;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Jayson on 3/20/2017.
 */

public class MultiplayerGamePresenter extends GamePresenter
        implements MultiplayerGameContract.UserActionsListener{

    private final String LOG_TAG = getClass().getSimpleName();

    private MultiplayerGameContract.View mMultiplayerGameView;

    // Scorekeeping
    private int[] mScoreArray = new int[4];

    // Supply a default constructor
    public MultiplayerGamePresenter(){
    }

    /**
     * Public constructor used to set up the presenter. Requires a reference to the calling View.
     * @param multiplayerGameView A reference to the calling View
     */
    public MultiplayerGamePresenter(
            @NonNull MultiplayerGameContract.View multiplayerGameView) {
        mMultiplayerGameView =
                checkNotNull(multiplayerGameView, "multiplayerGameView cannot be null!");

        // Initialize Score Array
        mScoreArray[0] = 0;
        mScoreArray[1] = 0;
        mScoreArray[2] = 0;
        mScoreArray[3] = 0;

        setGameView((GameContract.View) mMultiplayerGameView);
    }

    /*
     * Override the init method to reset the score when we start a new game
     */
    @Override
    public void initGame() {
        super.initGame();

        // Initialize Score Array
        mScoreArray[0] = 0;
        mScoreArray[1] = 0;
        mScoreArray[2] = 0;
        mScoreArray[3] = 0;
        mMultiplayerGameView.updatePlayerScore(1, 0);
        mMultiplayerGameView.updatePlayerScore(2, 0);
        mMultiplayerGameView.updatePlayerScore(3, 0);
        mMultiplayerGameView.updatePlayerScore(4, 0);
    }

    /**
     * Handles the click in the Presenter layer and passes it back to the appropriate
     * method in the View layer.
     * @param playerId ID of the player who clicked their button
     */
    @Override
    public void onPlayerButtonClick(int playerId) {
        Log.d(LOG_TAG, "Player " + playerId + " clicked their button.");

        // Set the player as active
        mMultiplayerGameView.setActivePlayer(playerId);

        // Unlock the board
        mMultiplayerGameView.setGameClickable(true);

        // Change the game state to show we are waiting for a player action
        mMultiplayerGameView.setGameState(1);

        // Start the find a set timer for each button
        mMultiplayerGameView.startFindSetCountdown();
    }

    /**
     * This method is called when the player finds a successful set
     * @param playerId ID of the player
     */
    public void onPlayerSuccess(int playerId){
        mScoreArray[playerId-1]++;
        mMultiplayerGameView.updatePlayerScore(playerId, mScoreArray[playerId-1]);
    }

    /**
     * This method is called when the player fails to find a set in time
     */
    public void onPlayerButtonTimedOut(){
        // Set all buttons to their timeout state
        mMultiplayerGameView.onPlayerTimedOut();

        // Disable the board again
        mMultiplayerGameView.setGameClickable(false);

        // Clear any cards that were clicked
        mMultiplayerGameView.clearChoices();

        // Set the game state back to idle
        mMultiplayerGameView.setGameState(0);
    }


}
