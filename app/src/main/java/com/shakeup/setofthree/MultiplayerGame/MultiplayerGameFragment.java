package com.shakeup.setofthree.MultiplayerGame;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
 */

public class MultiplayerGameFragment extends GameFragment implements MultiplayerGameContract.View {

    public final String LOG_TAG = this.getClass().getSimpleName();

    // Array to hold SETS found by each player
    ArrayList<SetCard> foundSetsPlayerOne, foundSetsPlayerTwo,
            foundSetsPlayerThree, foundSetsPlayerFour;

    // Reference to our presenter
    MultiplayerGamePresenter mMultiplayerActionsListener;

    // Reference to player buttons
    SubmitProcessButton playerOneButton, playerTwoButton, playerThreeButton, playerFourButton;

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

    /**
     * Handles the UI action for a player clicking his/her button
     * @param playerId ID of the player who clicked their button
     */
    @Override
    public void onPlayerButtonClick(int playerId) {

        // Start a 4 second timer with visual feedback for the player

        // Unlock the board to be clickable
        this.setGameClickable(true);
    }

    @Override
    public void startPlayerCountdown(int playerId){
        // Timer of 4 seconds, ticking every 0.1 seconds
        PlayerCountdown timer = new PlayerCountdown(playerId, 4000, 100);
    }

    @Override
    public void onSetSuccess() {
        super.onSetSuccess();
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


    /**
     * Timer class to handle setting and displaying the player in the player's button.
     */
    public class PlayerCountdown extends CountDownTimer {

        long mStartTime;
        SubmitProcessButton mPlayerButton;
        int mPlayerId;

        public PlayerCountdown(int playerId, long startTime, long interval) {
            super(startTime, interval);

            // Save member references
            mStartTime = startTime;
            mPlayerButton = getPlayerButton(playerId);
            mPlayerId = playerId;

            start();
        }

        @Override
        public void onFinish() {
            Log.d(LOG_TAG, "Timer for Player " + mPlayerId + " finished!");

            mPlayerButton.setProgress(0);
//            text.setText("Time's up!");
//            timeElapsedView.setText("Time Elapsed: " + String.valueOf(startTime));
        }


        @Override
        public void onTick(long millisUntilFinished) {
            int progressPercent;

            double start = (double) mStartTime;
            double timeLeft = (double) millisUntilFinished;

            progressPercent = (int) Math.floor((1 - (timeLeft / start)) * 100);

            mPlayerButton.setProgress(progressPercent);
//            text.setText("Time remain:" + millisUntilFinished);
//            timeElapsed = startTime - millisUntilFinished;
//            timeElapsedView.setText("Time Elapsed: " + String.valueOf(timeElapsed));
        }


    }

}
