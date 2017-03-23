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
    private int mScorePlayerOne = 0;
    private int mScorePlayerTwo = 0;
    private int mScorePlayerThree = 0;
    private int mScorePlayerFour = 0;

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

        setGameView((GameContract.View) mMultiplayerGameView);
    }

    /**
     * Handles the click in the Presenter layer and passes it back to the appropriate
     * method in the View layer.
     * @param playerId ID of the player who clicked their button
     */
    @Override
    public void playerButtonClick(int playerId) {
        Log.d(LOG_TAG, "Player " + playerId + " clicked their button.");

        // Set the player as active
        mMultiplayerGameView.setActivePlayer(playerId);

        // Unlock the board
        mMultiplayerGameView.setGameClickable(true);

        // Change the game state to show we are waiting for a player action
        mMultiplayerGameView.setGameState(1);

        // Start the find a set timer for each button
        for( int i = 1; i <= 4; i++ ){
            mMultiplayerGameView.startFindSetCountdown(i);
        }
    }

    /**
     * This method is called when the player finds a successful set
     * @param playerId ID of the player
     */
    public void playerButtonSuccess(int playerId){
        // Display success button message
    }

    /**
     * This method is called when the player fails to find a set in time or claims
     * something that isn't a set
     * @param playerId ID of the player
     */
    public void playerButtonPunish(int playerId){
        // Lock the active player's button and display an error message
        mMultiplayerGameView.onPunishPlayer(playerId);
        mMultiplayerGameView.setGameClickable(false);
        mMultiplayerGameView.clearChoices();
    }


}
