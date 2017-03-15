package com.shakeup.setofthree.SetGame;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.shakeup.setgamelibrary.SetCard;
import com.shakeup.setgamelibrary.SetGame;
import com.shakeup.setofthree.CustomView.SetGameCard;
import com.shakeup.setofthree.R;

import java.util.ArrayList;

/**
 * Created by Jayson on 3/2/2017.
 *
 * Main fragment for playing a game of SET. May be subclassed to implmeent
 * variants of the main game
 */


public class GameFragment extends Fragment
        implements GameContract.View, SetGameGridCallback {

    // Listener for presenter to handle all user input
    private GamePresenter mActionsListener;

    // GridView displaying the game board
    private GridView mGridView;

    // Adapter containing the current Set Hand displayed on the board
    private SetGameGridAdapter mGridAdapter;

    // ArrayList holding the current valid locations of sets
    private ArrayList<SetGame.Triplet<Integer, Integer, Integer>> mSetLocations;

    // Adapter to hold this fragment as a usable context
    private SetGameGridCallback mGameFragment;

    private String LOG_TAG = this.getClass().getSimpleName();

    /**
     * Allow another class to construct us
     */
    public GameFragment() {
        // Requires empty public constructor
    }
    public static GameFragment newInstance() {
        return new GameFragment();
    }

    // Run initial setup for creating a new game
    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_game, container, false);

        // Instance the presenter our fragment uses
        mActionsListener = new GamePresenter(this);

        // Keep an instance of this fragment to be used by subclasses
        mGameFragment = this;

        // Grab handlers for UI elements
        mGridView = (GridView) root.findViewById(R.id.game_grid);

        // Initialize a game
        mActionsListener.initGame();

        return root;
    }


    /**
     * Receives the generated Set Hand and displays it to the game grid
     * @param setCards ArrayList of SetCards to be displayed in the grid
     */
    @Override
    public void displayGame(ArrayList<SetCard> setCards) {
        // Initialize a new adapter with the Set Hand
        mGridAdapter = new SetGameGridAdapter(setCards);

        // Display the board
        mGridView.setAdapter(mGridAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SetGameCard card = (SetGameCard) view;
                card.toggle();

                // Let the presenter know we've been clicked
                mActionsListener.setCardClicked();
            }
        });

        // Get the locations of all available sets
        getSetLocations();

        // If we are in debug mode, highlight a valid set
        if( getContext().getResources().getBoolean(R.bool.is_debug) ){
            // Queue up a runnable since we can't highlight anything
            // before the GridView is set up
            mGridView.post(new Runnable() {
                @Override
                public void run() {
                    highlightSet();
                }
            });
        }
    }

    /**
     * Highlight a random available set for testing
     */
    public void highlightSet(){
        SetGame.Triplet<Integer, Integer, Integer> location = getRandomSet();
        int[] locationArray = new int[3];
        locationArray[0] = location.getFirst();
        locationArray[1] = location.getSecond();
        locationArray[2] = location.getThird();

        // Holds a reference to the card so we can highlight it
        SetGameCard card;

        for(int i = 0; i < 3; i++){
            card = (SetGameCard) mGridView.getChildAt(locationArray[i]);
            card.setHighlighted(true);
        }
    }

    /**
     * Get a random set location from the possible sets
     * @return Triplet of set indexes
     */
    public SetGame.Triplet<Integer, Integer, Integer> getRandomSet(){
        int index = (int) Math.floor(Math.random() * mSetLocations.size());

        return mSetLocations.get(index);
    }


    /**
     * This is called by the presenter whenever a SET card is clicked by the user.
     * If 3 cards are selected those indices are sent to the presenter
     * who will check if they are a valid set.
     */
    @Override
    public void onSetCardClicked() {

        // If we have 3 items selected, check if they are a set
        if (mGridView.getCheckedItemCount() == 3){
            SparseBooleanArray checkedItemPositions = mGridView.getCheckedItemPositions();

            int positionIndex = 0;
            int[] positions = new int[3];

            // Loop through SparseBooleanArray and grab the 3 positions that are checked
            for( int i = 0; i < checkedItemPositions.size() ; i++ ){
                if( checkedItemPositions.valueAt(i) ){
                    positions[positionIndex] = checkedItemPositions.keyAt(i);
                    positionIndex++;
                }
            }

            // Submit the set instances to the presenter
            mActionsListener.submitSet(
                    positions[0],
                    positions[1],
                    positions[2]);

            Log.d(LOG_TAG, String.format(
                    "Checked %d, %d, %d",
                    positions[0],
                    positions[1],
                    positions[2]));

            // Clear all selections from GridView
            mGridView.clearChoices();
        }
    }

    /**
     * Updates the board and score in response to a successful set claim
     */
    @Override
    public void claimSetSuccess(ArrayList<SetCard> newHand) {
        // Do stuff in response to successful set claim
        Snackbar.make(getView(), "You found a SET!", Snackbar.LENGTH_LONG).show();

        mGridAdapter = new SetGameGridAdapter(newHand);
    }

    @Override
    public void claimSetFailure() {
        // Do stuff in response to a failed set claim
        Snackbar.make(getView(), "That's not a SET!", Snackbar.LENGTH_LONG).show();
    }

    /**
     * Get the current locations of all sets and store them for later use
     */
    public void getSetLocations(){
        mSetLocations = mActionsListener.getSetLocations();
    }

    /**
     * Get the current locations of all sets for use in testing
     * @return
     */
    public ArrayList<SetGame.Triplet<Integer, Integer, Integer>> getSetLocationsForTest(){
        return mActionsListener.getSetLocations();
    }


}
