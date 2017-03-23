package com.shakeup.setofthree.MultiplayerGame;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.dd.processbutton.iml.SubmitProcessButton;
import com.shakeup.setgamelibrary.SetCard;
import com.shakeup.setofthree.R;
import com.shakeup.setofthree.SetGame.GameFragment;

import java.util.ArrayList;

/**
 * Created by Jayson on 3/20/2017.
 *
 * This Fragment handles the UI for the Multiplayer variant of SetGame
 */

public class MultiplayerGameFragment extends GameFragment implements MultiplayerGameContract.View {

    // Constants for Timers
    public final long BUTTON_FIND_SET_TIMER_LENGTH = 3000;
    public final long BUTTON_TIMER_TICK_INTERVAL = 15;
    public final long BUTTON_MESSAGE_LENGTH = 1000;

    public final String LOG_TAG = this.getClass().getSimpleName();

    // Array to hold SETS found by each player
    ArrayList<SetCard> foundSetsPlayerOne, foundSetsPlayerTwo,
            foundSetsPlayerThree, foundSetsPlayerFour;

    // Reference to our presenter
    MultiplayerGameContract.UserActionsListener mMultiplayerActionsListener;

    // Reference to player buttons
    SubmitProcessButton playerOneButton, playerTwoButton, playerThreeButton, playerFourButton;

    // Int represents the active player at any given time
    int mActivePlayer = 0;

    // Use state tracker to track the state of the game
    // 0 = Idle, Player buttons enabled, Board disabled
    // 1 = Waiting for set claim, Player buttons disabled, board enabled.
    //      On timer expire, move to 0
    // 2 = Failed set claim, Active player disabled on timer, others enabled, board disabled
    // 3 = Successful set claim, all buttons enabled, board disabled
    int mGameState = 0;

    // Default constructor
    public MultiplayerGameFragment(){
    }

    public static MultiplayerGameFragment newInstance(){
        return new MultiplayerGameFragment();
    }

    /*
     * Each game mode needs to set up its own onCreateView and assign the following
     * member variables of the superclass
     * mActionsListener
     * mRecyclerGridView
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Get the number of desired players as an extra.
        int numPlayers = getActivity().getIntent()
                .getIntExtra(getString(R.string.extra_num_players), 2);

        View root;

        root = inflater.inflate(
                R.layout.fragment_game_multiplayer, container, false);

        // Instance the presenter our fragment uses and grab a reference
        mMultiplayerActionsListener = new MultiplayerGamePresenter(this);
        // Have the superclass use the MultiplayerGamePresenter as its GamePresenter
        mActionsListener = mMultiplayerActionsListener;

        // Set up the RecyclerView and assign it to the superclass
        mRecyclerGridView = (RecyclerView) root.findViewById(R.id.game_recycler_grid);

        // Hide unused player spaces
        switch( numPlayers ){
            case 3:
                // Hide player 4
                root.findViewById(R.id.space_player_four).setVisibility(View.GONE);
            case 2:
                root.findViewById(R.id.space_player_four).setVisibility(View.GONE);
                root.findViewById(R.id.space_player_three).setVisibility(View.GONE);

        }

        playerOneButton = (SubmitProcessButton) root.findViewById(R.id.button_player_one);
        playerTwoButton = (SubmitProcessButton) root.findViewById(R.id.button_player_two);
        playerThreeButton = (SubmitProcessButton) root.findViewById(R.id.button_player_three);
        playerFourButton = (SubmitProcessButton) root.findViewById(R.id.button_player_four);

        playerThreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMultiplayerActionsListener.playerButtonClick(3);
            }
        });
        playerTwoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMultiplayerActionsListener.playerButtonClick(2);
            }
        });
        playerOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMultiplayerActionsListener.playerButtonClick(1);
            }
        });
        playerFourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMultiplayerActionsListener.playerButtonClick(4);
            }
        });

        // Initialize a game
        mMultiplayerActionsListener.initGame();

        // Wrap some initialization methods inside a OnGlobalLayoutListener
        // so they fire once the RecyclerView is populated
        ViewTreeObserver vto = mRecyclerGridView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mRecyclerGridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                // Set the board to be unclickable until a player presses their button
                setGameClickable(false);
            }
        });

        return root;
    }

    @Override
    public void onSetSuccess() {
        // Do stuff in response to successful SET claim
        Snackbar.make(getView(), getString(R.string.message_found_set), Snackbar.LENGTH_LONG)
                .show();
        // Set the new game state and let the timers handle button states
        setGameState(3);

        // Display success message timer
        DisplayButtonMessageCountdown timer =
                new DisplayButtonMessageCountdown(
                        mActivePlayer,
                        BUTTON_MESSAGE_LENGTH,
                        BUTTON_TIMER_TICK_INTERVAL);

    }

    @Override
    public void onSetFailure() {
        // Do stuff in response to a failed set claim
        Snackbar.make(getView(), getString(R.string.message_not_set), Snackbar.LENGTH_LONG)
                .show();

        // Set the new game state and let the timers handle button states
        setGameState(2);

        // Display failure message timer
        DisplayButtonMessageCountdown timer =
                new DisplayButtonMessageCountdown(
                        mActivePlayer,
                        BUTTON_MESSAGE_LENGTH,
                        BUTTON_TIMER_TICK_INTERVAL);
    }

    @Override
    public void onGameOver() {
        // Do stuff in response to a failed set claim
        Snackbar.make(getView(), getString(R.string.message_game_over), Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.message_restart), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mActionsListener.initGame();
                    }
                })
                .show();
    }

    @Override
    public void setActivePlayer(int playerId) {
        mActivePlayer = playerId;
    }

    @Override
    public void setGameState(int gameState){
        mGameState = gameState;
    }

    /**
     * Start a set countdown for a specified player
     * @param playerId
     */
    @Override
    public void startFindSetCountdown(int playerId){
        FindSetCountdown timer = new FindSetCountdown(
                playerId,
                BUTTON_FIND_SET_TIMER_LENGTH,
                BUTTON_TIMER_TICK_INTERVAL);
    }


    /**
     * This sets up an error message for the player on a timer while locking their button
     * @param playerId
     */
    @Override
    public void onPunishPlayer(int playerId) {
        DisplayButtonMessageCountdown timer =
                new DisplayButtonMessageCountdown(
                        playerId,
                        BUTTON_MESSAGE_LENGTH,
                        BUTTON_TIMER_TICK_INTERVAL);
    }

    /**
     * This utility sets the Clickable attribute for a specific
     * @param playerId
     */
    @Override
    public void setEnablePlayerButton(int playerId, boolean enable){
        getPlayerButton(playerId).setClickable(enable);
    }

    /**
     * Utility to get the button for a particular PlayerID
     * @param playerId PlayerID of the button we want
     * @return ActionProcessButton associated with that player
     */
    public SubmitProcessButton getPlayerButton(int playerId){
        switch ( playerId ){
            case 1:
                return playerOneButton;
            case 2:
                return playerTwoButton;
            case 3:
                return playerThreeButton;
            case 4:
                return playerFourButton;
            default:
                return playerOneButton;
        }
    }



    /*
     * These timers handle displaying messages on individual player buttons.
     * The message flow is represented by the following chart
     *
     *                Game Idle
     *                    |
     *          Player Clicks their Button
     *                    |
     *  Unlock the Board, Lock other players buttons,
     *     Display "Find a Set" or "Wait" messages <TIMER>
     *        |                         |
     * <Found a set>        <Not a set or Out of time>
     *        |                         |
     *  Unlock board and          Unlock the board, lock player's
     * Display success <Timer>   button with error message <TIMER>
     *           \                  /
     *       Unlock other players buttons
     *                   |
     *               Game Idle
     */

    /**
     * Timer class to handle starting the "Find a SET countdown"
     */
    public class FindSetCountdown extends CountDownTimer {

        long mStartTime;
        SubmitProcessButton mPlayerButton;
        int mPlayerId;

        public FindSetCountdown(int playerId, long startTime, long interval) {
            super(startTime, interval);

            // Save member references
            mStartTime = startTime;
            mPlayerButton = getPlayerButton(playerId);
            mPlayerId = playerId;

            // Set error messages based on whether we are the active player
            if( mPlayerId == mActivePlayer ){
                mPlayerButton.setErrorText(getString(R.string.button_not_a_set));
            } else {
                mPlayerButton.setErrorText(getString(R.string.button_wait));
                mPlayerButton.setProgress(-1);
            }

            // Disable clicks on this button
            setEnablePlayerButton(mPlayerId, false);

            start();
        }

        @Override
        public void onFinish() {
            if( mPlayerId == mActivePlayer ){
                // Let the presenter know our button ran out of time
                mMultiplayerActionsListener.playerButtonPunish(mPlayerId);
            } else {
                // Set other buttons back to normal
                mPlayerButton.setProgress(0);
                setEnablePlayerButton(mPlayerId, true);
            }
        }

        /**
         * Update the progress bar and handle changes to the GameState
         */
        @Override
        public void onTick(long millisUntilFinished) {

            switch( mGameState ){
                case 1: // Waiting for set claim
                    int progressPercent;

                    double start = (double) mStartTime;
                    double timeLeft = (double) millisUntilFinished;

                    progressPercent = (int) Math.floor((1 - (timeLeft / start)) * 100);

                    // Only display progress for the active player.
                    // Other players wait in error mode
                    if( mPlayerId == mActivePlayer ){
                        mPlayerButton.setProgress(progressPercent);
                    }
                    break;

                case 2: // Failed set claim
                    // Lock the board
                    setGameClickable(false);
                    // Stop the timer
                    cancel();
                    if( mPlayerId == mActivePlayer ){
                        mMultiplayerActionsListener.playerButtonPunish(mPlayerId);
                        // Button remains locked
                    } else {
                        // For non-active player buttons, just set normal state
                        mPlayerButton.setProgress(0);
                        setEnablePlayerButton(mPlayerId, true);
                    }
                    // Reset Game State
                    setGameState(0);
                    // Reset the active player
                    setActivePlayer(0);
                    break;

                case 3: // Successful set claim
                    // Lock the board
                    setGameClickable(false);
                    // Stop the timer
                    cancel();
                    if( mPlayerId == mActivePlayer ){
                        mPlayerButton.setProgress(100);
                        setEnablePlayerButton(mPlayerId, true);
                    } else {
                        // For non-active player buttons, just set normal state
                        mPlayerButton.setProgress(0);
                        setEnablePlayerButton(mPlayerId, true);
                    }
                    // Reset Game State
                    setGameState(0);
                    // Reset the active player
                    setActivePlayer(0);
                    break;
            }
        }
    }


    /**
     * Timer class to handle starting the "You found a set!"
     * or "Out of time!" or "Not a Set!" countdown
     */
    public class DisplayButtonMessageCountdown extends CountDownTimer {

        long mStartTime;
        SubmitProcessButton mPlayerButton;
        int mPlayerId;

        public DisplayButtonMessageCountdown(
                int playerId, long startTime, long interval) {
            super(startTime, interval);

            // Save member references
            mStartTime = startTime;
            mPlayerButton = getPlayerButton(playerId);
            mPlayerId = playerId;

            // Set the appropriate message depending on whether or not we timed out
            switch( mGameState ){
                case 1:
                    mPlayerButton.setErrorText(getString(R.string.button_out_of_time));
                    mPlayerButton.setProgress(-1);
                    setEnablePlayerButton(mPlayerId, false);
                    break;
                case 2:
                    mPlayerButton.setErrorText(getString(R.string.button_not_a_set));
                    mPlayerButton.setProgress(-1);
                    setEnablePlayerButton(mPlayerId, false);
                    break;
                case 3:
                    mPlayerButton.setCompleteText(getString(R.string.button_found_set));
                    mPlayerButton.setProgress(100);
                    setEnablePlayerButton(mPlayerId, true);
                    break;
            }
            start();
        }

        @Override
        public void onFinish() {
            // Set the button to normal and enable it
            mPlayerButton.setProgress(0);
            setEnablePlayerButton(mPlayerId, true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // Nothing to do here
        }
    }
}
