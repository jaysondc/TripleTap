package com.shakeup.setofthree.SetGame;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.shakeup.setgamelibrary.SetCard;
import com.shakeup.setgamelibrary.SetGame;
import com.shakeup.setofthree.CustomView.SetGameCardView;
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
    @javax.annotation.Resource
    private RecyclerView mRecyclerGridView;

    // Adapter containing the current Set Hand displayed on the board
    private SetGameRecyclerAdapter mSetGameRecyclerAdapter;

    // ArrayList holding the current valid locations of sets
    private ArrayList<SetGame.Triplet<Integer, Integer, Integer>> mSetLocations;

    // Holds the positions of a set we're currently trying to claim
    private int[] positions = new int[3];

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

        // Grab handlers for UI elements
        mRecyclerGridView = (RecyclerView) root.findViewById(R.id.game_recycler_grid);

        // Initialize a game
        mActionsListener.initGame();

        return root;
    }


    /**
     * Receives the generated Set Hand and displays it to the game grid
     * @param setHand ArrayList of SetCards to be displayed in the grid
     */
    @Override
    public void displayGame(ArrayList<SetCard> setHand) {
        // Initialize a new adapter with the Set Hand
        mSetGameRecyclerAdapter = new SetGameRecyclerAdapter(
                getContext(), mActionsListener, setHand);
        mSetGameRecyclerAdapter.setHasStableIds(true);

        // Display the board
        // RecyclerView requires a LayoutManager and RecyclerView.Adapter to work
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        mRecyclerGridView.setLayoutManager(gridLayoutManager);
        mRecyclerGridView.setAdapter(mSetGameRecyclerAdapter);

        // Get the locations of all available sets
        getSetLocations();

        // If we are in debug mode, highlight a valid set
        if( getContext().getResources().getBoolean(R.bool.is_debug) ){
            // Use a ViewTreeObserver to only show highlights once the RecyclerView
            // is done drawing its layout.
            ViewTreeObserver vto = mRecyclerGridView.getViewTreeObserver();
            vto.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mRecyclerGridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    highlightSet();
                }
            });
        }
    }

    /**
     * Highlight a random available set for testing
     */
    public void highlightSet(){

        // Get the location of a valid set and assign it to an array
        SetGame.Triplet<Integer, Integer, Integer> location = getRandomSet();
        int[] locationArray = new int[3];

        locationArray[0] = location.getFirst();
        locationArray[1] = location.getSecond();
        locationArray[2] = location.getThird();

        // Holds a reference to the card so we can highlight it
        SetGameCardView card;

        // Get the associated gridview children and highlight them
        for(int i = 0; i < 3; i++) {
            card = (SetGameCardView) mRecyclerGridView.getChildAt(locationArray[i]);
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
        if (getCheckedItemCount() == 3){
            SparseBooleanArray checkedItemPositions = getCheckedItemPositions();

            int positionIndex = 0;

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
                    "Submitted set at positions %d, %d, %d",
                    positions[0],
                    positions[1],
                    positions[2]));

            // Clear all selections from GridView
            clearChoices();
        }
    }

    /**
     * Gets the number of items currently selected in the RecyclerView
     * @return Count of the items
     */
    public int getCheckedItemCount(){
        int checkedCount = 0;

        // Loop through all SetGameCardViews in the adapter and count how many are checked
        for ( int i = 0; i < mRecyclerGridView.getChildCount(); i++ ){
            SetGameCardView cardView = (SetGameCardView) mRecyclerGridView.getChildAt(i);
            if ( cardView.isChecked() ){
                checkedCount++;
            }
        }
        return checkedCount;
    }

    /**
     * Gets all locations of checked items in the RecyclerGrid
     * @return SparseBooleanArray containing key-value pairs of locations where checked == true
     */
    public SparseBooleanArray getCheckedItemPositions(){
        SparseBooleanArray checkedLocations = new SparseBooleanArray();

        // Loop through all SetGameCardViews in the adapter and add locations and values for
        // those that are checked
        for ( int i = 0; i < mRecyclerGridView.getChildCount(); i++ ){
            SetGameCardView cardView = (SetGameCardView) mRecyclerGridView.getChildAt(i);
            if ( cardView.isChecked() ){
                checkedLocations.append(i, true);
            }
        }
        return checkedLocations;
    }

    /**
     * Uncheck all views in the RecyclerGrid
     */
    public void clearChoices(){
        // Loop through all SetGameCardViews in the adapter and mark them as Unchecked
        for ( int i = 0; i < mRecyclerGridView.getChildCount(); i++ ){
            SetGameCardView cardView = (SetGameCardView) mRecyclerGridView.getChildAt(i);
            if ( cardView.isChecked() ){
                cardView.setChecked(false);
            }
        }
    }

    /**
     * Un-highlight all views in the RecyclerGrid
     */
    public void clearHighlights(){
        // Loop through all SetGameCardViews in the adapter and mark them as Unchecked
        for ( int i = 0; i < mRecyclerGridView.getChildCount(); i++ ){
            SetGameCardView cardView = (SetGameCardView) mRecyclerGridView.getChildAt(i);
            if ( cardView.isHighlighted() ){
                cardView.setHighlighted(false);
            }
        }
    }

    /**
     * Updates the board and score in response to a successful set claim
     * @param newHand Updated state of the Set hand
     * @param isOverflow State whether or not the hand is in overflow mode
     * @param deckSize The number of cards remaining in the deck
     */
    @Override
    public void claimSetSuccess(
            boolean isOverflow,
            int deckSize) {
        // Do stuff in response to successful set claim
        Snackbar.make(getView(), getString(R.string.message_found_set), Snackbar.LENGTH_LONG)
                .show();

        mSetGameRecyclerAdapter.updateSetHand(
                positions[0],
                positions[1],
                positions[2],
                isOverflow,
                deckSize);

        getSetLocations();

        // If we're in debug and there are sets available, clear highlights and show new highlights
        if( getContext().getResources().getBoolean(R.bool.is_debug) &&
                mSetLocations.size() != 0){
            // Use a ViewTreeObserver to only show highlights once the RecyclerView
            // is done drawing its layout.
            ViewTreeObserver vto = mRecyclerGridView.getViewTreeObserver();
            vto.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mRecyclerGridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    clearHighlights();
                    highlightSet();
                }
            });
        }
    }

    @Override
    public void claimSetFailure() {
        // Do stuff in response to a failed set claim
        Snackbar.make(getView(), getString(R.string.message_not_set), Snackbar.LENGTH_LONG)
                .show();
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
