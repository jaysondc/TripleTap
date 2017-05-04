package com.shakeup.setofthree.SetGame;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
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
import com.shakeup.setofthree.Adapters.SetGameRecyclerAdapter;
import com.shakeup.setofthree.CustomViews.SetGameCardView;
import com.shakeup.setofthree.R;

import org.parceler.ParcelClass;
import org.parceler.ParcelClasses;

import java.util.ArrayList;

/**
 * Created by Jayson on 3/2/2017.
 *
 * Main fragment for playing a game of SET. May be subclassed to implmeent
 * variants of the main game
 */

@ParcelClasses({
        @ParcelClass(SetGame.class),
        @ParcelClass(SetCard.class),
        @ParcelClass(SetGame.Triplet.class),
        @ParcelClass(SetGame.SetTriplet.class),
        @ParcelClass(SetGame.SetDeck.class)
})
public abstract class GameFragment extends AppCompatDialogFragment
        implements GameContract.View{

    // Listener for presenter to handle all user input
    protected GameContract.UserActionsListener mActionsListener;

    // GridView displaying the game board
    @javax.annotation.Resource
    protected RecyclerView mRecyclerGridView;

    // Adapter containing the current Set Hand displayed on the board
    protected SetGameRecyclerAdapter mSetGameRecyclerAdapter;

    // Holds the positions of a set we're currently trying to claim
    private int[] mCheckedPositions = new int[3];

    private String LOG_TAG = this.getClass().getSimpleName();

    /**
     * Run initial setup for creating a new game.
     * Any subclasses should override this method and set up
     * the root layout and presenter specific to their game mode.
     */
    public abstract View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState);

    /**
     * Updates the board in response to a successful set claim. Override this to handle
     * UI elements in specific game modes
     */
    public abstract void onSetSuccess();

    /**
     * Handle actions that happen if we claimed an invalid set. Override this to handle
     * UI elements differently in different game modes.
     */
    public abstract void onSetFailure();

    /**
     * Handle UI actions that happen when the game is over
     */
    public abstract void showGameOver();


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

        // If we are in debug mode, highlight a valid set
        if( getContext().getResources().getBoolean(R.bool.is_debug) ){
            // Use a ViewTreeObserver to only show highlights once the RecyclerView
            // is done drawing its layout.
            ViewTreeObserver vto = mRecyclerGridView.getViewTreeObserver();
            vto.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mRecyclerGridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    mActionsListener.highlightValidSet();
                }
            });
        }
    }

    /**
     * Highlight the card at a specific index
     * @param index Index of the card to highlight
     */
    public void highlightCard(int index){
        // Holds a reference to the card so we can highlight it
        SetGameCardView card =
                (SetGameCardView) mRecyclerGridView.getChildAt(index);
        card.setHighlighted(true);
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
                    mCheckedPositions[positionIndex] = checkedItemPositions.keyAt(i);
                    positionIndex++;
                }
            }

            // Submit the set instances to the presenter
            mActionsListener.onSubmitSet(
                    mCheckedPositions[0],
                    mCheckedPositions[1],
                    mCheckedPositions[2]);

            Log.d(LOG_TAG, String.format(
                    "Submitted set at positions %d, %d, %d",
                    mCheckedPositions[0],
                    mCheckedPositions[1],
                    mCheckedPositions[2]));

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
     * The method refreshes the entire board display and should be used in the event
     * the board contents has been changed manually.
     */
    public void refreshBoard(){
        mSetGameRecyclerAdapter.notifyDataSetChanged();
    }

    /**
     * Updates the RecyclerView whenever the hand is updated. Updates member variables
     * with Set Locations and highlights sets if we are in debug mode.
     * @param isOverflow State whether or not the hand is in overflow mode
     * @param deckSize The number of cards remaining in the deck
     */
    @Override
    public void updateSetHand(boolean isOverflow, int deckSize){
        mSetGameRecyclerAdapter.updateSetHand(
                mCheckedPositions[0],
                mCheckedPositions[1],
                mCheckedPositions[2],
                isOverflow,
                deckSize);

        // If we're in debug and there are sets available, clear highlights and show new highlights
        if( getContext().getResources().getBoolean(R.bool.is_debug)){
            // Use a ViewTreeObserver to only show highlights once the RecyclerView
            // is done drawing its layout.
            ViewTreeObserver vto = mRecyclerGridView.getViewTreeObserver();
            vto.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mRecyclerGridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    clearHighlights();
                    mActionsListener.highlightValidSet();
                }
            });
        }
    }


    /**
     * Public accessor to set the SetHand cards as Clickable
     * @param isClickable The value to set the Clickable attribute
     */
    public void setGameClickable(boolean isClickable){
        // Loop through all SetGameCardViews in the adapter set them as isClickble
        for ( int i = 0; i < mRecyclerGridView.getChildCount(); i++ ){
            mRecyclerGridView.getChildAt(i).setEnabled(isClickable);
        }
    }

    public boolean isClickable(){
        return mRecyclerGridView.getChildAt(0).isClickable();
    }

    public GameContract.UserActionsListener getActionsListener() {
        return mActionsListener;
    }

    public void setActionsListener(GameContract.UserActionsListener mActionsListener) {
        this.mActionsListener = mActionsListener;
    }

    public RecyclerView getRecyclerGridView() {
        return mRecyclerGridView;
    }

    public void setRecyclerGridView(RecyclerView mRecyclerGridView) {
        this.mRecyclerGridView = mRecyclerGridView;
    }
}
