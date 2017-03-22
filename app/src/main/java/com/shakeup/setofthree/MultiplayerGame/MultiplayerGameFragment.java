package com.shakeup.setofthree.MultiplayerGame;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;

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

    // The currently active player. 0 if nobody.
    private int mActivePlayer = 0;

    // Scorekeeping
    private int mScorePlayerOne = 0;
    private int mScorePlayerTwo = 0;
    private int mScorePlayerThree = 0;
    private int mScorePlayerFour = 0;


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

        switch( numPlayers ){
            case 2:
                root = inflater.inflate(
                        R.layout.fragment_game_multiplayer_two, container, false);
                break;
            case 3:
                root = inflater.inflate(
                        R.layout.fragment_game_multiplayer_three, container, false);
                break;
            case 4:
                root = inflater.inflate(
                        R.layout.fragment_game_multiplayer_four, container, false);
                break;
            default: // Default to 2 players if somehow it isn't specified
                root = inflater.inflate(
                        R.layout.fragment_game_multiplayer_two, container, false);
                break;
        }

        // Instance the presenter our fragment uses and grab a reference
        mMultiplayerActionsListener = new MultiplayerGamePresenter(this);
        // Have the superclass use the MultiplayerGamePresenter as its GamePresenter
        mActionsListener = mMultiplayerActionsListener;

        // Set up the RecyclerView and assign it to the superclass
        mRecyclerGridView = (RecyclerView) root.findViewById(R.id.game_recycler_grid);

        // Set up player buttons
        Button playerOneButton, playerTwoButton, playerThreeButton, playerFourButton;
        switch( numPlayers ){
            case 4:
                playerFourButton = (Button) root.findViewById(R.id.button_player_four);
                playerFourButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMultiplayerActionsListener.playerButtonClick(4);
                    }
                });
            case 3:
                playerThreeButton = (Button) root.findViewById(R.id.button_player_three);
                playerThreeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMultiplayerActionsListener.playerButtonClick(3);
                    }
                });
            case 2:
                playerTwoButton = (Button) root.findViewById(R.id.button_player_two);
                playerTwoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMultiplayerActionsListener.playerButtonClick(2);
                    }
                });
                playerOneButton = (Button) root.findViewById(R.id.button_player_one);
                playerOneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMultiplayerActionsListener.playerButtonClick(1);
                    }
                });
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


    /**
     * Handles the UI action for a player clicking his/her button
     * @param playerId ID of the player who clicked their button
     */
    @Override
    public void onPlayerButtonClick(int playerId) {
        Log.d(LOG_TAG, "Player " + playerId + " clicked their button.");

        mActivePlayer = playerId;

        // Start a 4 second timer with visual feedback for the player

        // Unlock the board to be clickable
        this.setGameClickable(true);
    }

    @Override
    public void onSetSuccess() {


        super.onSetSuccess();
    }
}
