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
import android.widget.TextView;

import com.dd.processbutton.iml.SubmitProcessButton;
import com.shakeup.setgamelibrary.SetCard;
import com.shakeup.setofthree.R;
import com.shakeup.setofthree.SetGame.GameFragment;

import java.util.ArrayList;

import static com.shakeup.setofthree.MultiplayerGame.MultiplayerButtonView.BUTTON_TIMER_TICK_INTERVAL;

/**
 * Created by Jayson on 3/20/2017.
 *
 * This Fragment handles the UI for the Multiplayer variant of SetGame
 *
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


public class MultiplayerGameFragment
        extends GameFragment
        implements MultiplayerGameContract.View {

    public final String LOG_TAG = this.getClass().getSimpleName();

    // Array to hold SETS found by each player
    ArrayList<SetCard> foundSetsPlayerOne, foundSetsPlayerTwo,
            foundSetsPlayerThree, foundSetsPlayerFour;

    // Reference to our presenter
    MultiplayerGameContract.UserActionsListener mMultiplayerActionsListener;

    // Reference to player buttons
    MultiplayerButtonView playerOneButtonView,
            playerTwoButtonView,
            playerThreeButtonView,
            playerFourButtonView;

    TextView[] mPlayerScoreArray = new TextView[4];

    // Int represents the active player at any given time
    int mActivePlayer = 0;

    // Use state tracker to track the state of the game
    // 0 = Idle, Player buttons enabled, Board disabled
    // 1 = Waiting for set claim, Player buttons disabled, board enabled.
    //      On timer expire, move to 0
    // 2 = Failed set claim, Active player disabled on timer, others enabled, board disabled
    // 3 = Successful set claim, all buttons enabled, board disabled
    int mGameState = 0;

    // Reference to the timer we'll use to wait for the active player
    // to find a set
    FindSetCountdown mFindSetCountdownTimer;

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
            case 2:
                // Hide player 3 and 4
                root.findViewById(R.id.rotateLayoutButtonThree).setVisibility(View.GONE);
                root.findViewById(R.id.rotateLayoutScoreThree).setVisibility(View.GONE);
            case 3:
                // Hide player 4 only
                root.findViewById(R.id.rotateLayoutButtonFour).setVisibility(View.GONE);
                root.findViewById(R.id.rotateLayoutScoreFour).setVisibility(View.GONE);
                break;
        }

        root.invalidate();

        SubmitProcessButton playerOneButton =
                (SubmitProcessButton) root.findViewById(R.id.button_player_one);
        SubmitProcessButton playerTwoButton =
                (SubmitProcessButton) root.findViewById(R.id.button_player_two);
        SubmitProcessButton playerThreeButton =
                (SubmitProcessButton) root.findViewById(R.id.button_player_three);
        SubmitProcessButton playerFourButton =
                (SubmitProcessButton) root.findViewById(R.id.button_player_four);

        // Wrap the buttons in our MultiplayerButtonView class so we can easily animate them
        playerOneButtonView = new MultiplayerButtonView(playerOneButton, getContext());
        playerTwoButtonView = new MultiplayerButtonView(playerTwoButton, getContext());
        playerThreeButtonView = new MultiplayerButtonView(playerThreeButton, getContext());
        playerFourButtonView = new MultiplayerButtonView(playerFourButton, getContext());

        playerOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMultiplayerActionsListener.onPlayerButtonClick(1);
            }
        });
        playerTwoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMultiplayerActionsListener.onPlayerButtonClick(2);
            }
        });
        playerThreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMultiplayerActionsListener.onPlayerButtonClick(3);
            }
        });
        playerFourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMultiplayerActionsListener.onPlayerButtonClick(4);
            }
        });

        // Set up Score displays
        mPlayerScoreArray[0] = (TextView) root.findViewById(R.id.score_player_one);
        mPlayerScoreArray[1] = (TextView) root.findViewById(R.id.score_player_two);
        mPlayerScoreArray[2] = (TextView) root.findViewById(R.id.score_player_three);
        mPlayerScoreArray[3] = (TextView) root.findViewById(R.id.score_player_four);
        for(TextView view : mPlayerScoreArray){
            view.setText("Score: 0");
        }

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
//        // Do stuff in response to successful SET claim
//        Snackbar.make(getView(), getString(R.string.message_found_set), Snackbar.LENGTH_LONG)
//                .show();

        // Set the new game state and let the timers handle button states
        setGameState(3);

        // Disable game clicks
        setGameClickable(false);

        // Let the presenter know someone found a set
        mMultiplayerActionsListener.onPlayerSuccess(mActivePlayer);

        // Set the buttons to the success state
        for( int i = 1; i <= 4; i++ ){
            if( i == mActivePlayer ){
                getPlayerButton(i).activePlayerSuccess();
            } else {
                getPlayerButton(i).resetButton();
            }
        }
        // Cancel the local timer since the user took an action early.
        mFindSetCountdownTimer.cancel();
    }

    @Override
    public void onSetFailure() {
//        // Do stuff in response to a failed set claim
//        Snackbar.make(getView(), getString(R.string.message_not_set), Snackbar.LENGTH_LONG)
//                .show();

        // Set the new game state and let the timers handle button states
        setGameState(2);

        // Disable game clicks
        setGameClickable(false);

        // Set the buttons to the failure state
        for( int i = 1; i <= 4; i++ ){
            if( i == mActivePlayer ){
                getPlayerButton(i).activePlayerFailure();
            } else {
                getPlayerButton(i).resetButton();
            }
        }

        // Cancel the local timer since the user took an action early.
        mFindSetCountdownTimer.cancel();
    }

    @Override
    public void showGameOver() {
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
     */
    @Override
    public void startFindSetCountdown(){
        // Start the State 1 waiting states for every button
        for( int i = 1; i <= 4; i++ ){
            if( i == mActivePlayer ){
                getPlayerButton(i).activePlayerWait();
            } else {
                getPlayerButton(i).otherPlayerWait();
            }
        }

        // Start a timer to wait for these buttons to timeout
        // Start a shorter timer if we are in debug mode
        if( getContext().getResources().getBoolean(R.bool.is_debug) ){
            mFindSetCountdownTimer = new FindSetCountdown(
                    MultiplayerButtonView.BUTTON_FIND_SET_TIMER_DEBUG_LENGTH,
                    BUTTON_TIMER_TICK_INTERVAL
            );
        } else {
            mFindSetCountdownTimer = new FindSetCountdown(
                    MultiplayerButtonView.BUTTON_FIND_SET_TIMER_LENGTH,
                    BUTTON_TIMER_TICK_INTERVAL
            );
        }
    }


    /**
     * This sets up an error message for the player on a timer while locking their button
     */
    @Override
    public void onPlayerTimedOut() {
        // Handle state where the active player timed out
        for( int i = 1; i <= 4; i++ ){
            if( i == mActivePlayer ){
                getPlayerButton(i).activePlayerTimeout();
            } else {
                getPlayerButton(i).resetButton();
            }
        }
    }

    /**
     * Utility to get the button for a particular PlayerID
     * @param playerId PlayerID of the button we want
     * @return ActionProcessButton associated with that player
     */
    public MultiplayerButtonView getPlayerButton(int playerId){
        switch ( playerId ){
            case 1:
                return playerOneButtonView;
            case 2:
                return playerTwoButtonView;
            case 3:
                return playerThreeButtonView;
            case 4:
                return playerFourButtonView;
            default:
                return null;
        }
    }

    public void updatePlayerScore(int playerId, int playerScore){
        String scoreString = "Score: " + playerScore;
        mPlayerScoreArray[playerId-1].setText(scoreString);
    }



    /**
     * Timer class to handle starting the "Find a SET countdown". It will notify
     * the presenter if our buttons time out
     */
    public class FindSetCountdown extends CountDownTimer {

        SubmitProcessButton mPlayerButton;
        int mPlayerId;

        public FindSetCountdown(long startTime, long interval) {
            super(startTime, interval);

            start();
        }

        @Override
        public void onFinish() {
            mMultiplayerActionsListener.onPlayerButtonTimedOut();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // Do nothing
        }
    }
}
