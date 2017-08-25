package com.shakeup.setofthree.setgame;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;

import com.shakeup.setgamelibrary.SetCard;
import com.shakeup.setgamelibrary.SetDeck;
import com.shakeup.setgamelibrary.SetGame;
import com.shakeup.setofthree.R;
import com.shakeup.setofthree.adapters.SetGameRecyclerAdapter;
import com.shakeup.setofthree.customviews.SetGameCardView;

import org.parceler.ParcelClass;
import org.parceler.ParcelClasses;
import org.parceler.Parcels;

import java.util.ArrayList;

/**
 * Created by Jayson on 3/2/2017.
 * <p>
 * Main fragment for playing a game of SET. May be subclassed to implmeent
 * variants of the main game
 */

@ParcelClasses({
        @ParcelClass(SetGame.class),
        @ParcelClass(SetCard.class),
        @ParcelClass(SetGame.Triplet.class),
        @ParcelClass(SetGame.SetTriplet.class),
        @ParcelClass(SetDeck.class)
})
public abstract class GameFragment extends AppCompatDialogFragment
        implements GameContract.View {

    // Listener for presenter to handle all user input
    protected GameContract.UserActionsListener mActionsListener;

    // GridView displaying the game board
    @javax.annotation.Resource
    protected RecyclerView mRecyclerGridView;

    // Adapter containing the current Set Hand displayed on the board
    protected SetGameRecyclerAdapter mSetGameRecyclerAdapter;

    // Holds the positions of a set we're currently trying to claim
    protected int[] mCheckedPositions = new int[3];
    protected int mCheckedCount;
    protected SetGame mExistingGame;

    private String LOG_TAG = this.getClass().getSimpleName();

    // Debug buttons
    private Button mDebugRefreshView, mDebugEndGameView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Transition transition = TransitionInflater.from(getContext()).inflateTransition(R.transition.game_enter_transition);
        getActivity().getWindow().setEnterTransition(transition);
        getActivity().getWindow().setReenterTransition(transition);
        getActivity().getWindow().setExitTransition(transition);
        getActivity().getWindow().setReturnTransition(transition);
    }

    /**
     * Run initial setup for creating a new game.
     * Any subclasses should override this method and set up
     * the root layout and presenter specific to their game mode.
     */
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Reload the game state if we've been rotated or restarted
        if (savedInstanceState != null) {
            mExistingGame = Parcels
                    .unwrap(savedInstanceState
                            .getParcelable(getString(R.string.bundle_key_game)));
            mCheckedPositions = savedInstanceState
                    .getIntArray(getString(R.string.bundle_key_checked_positions));
            mCheckedCount = savedInstanceState
                    .getInt(getString(R.string.bundle_key_checked_count));

            Log.d(LOG_TAG, "Restored the game from a previous state.");
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /*
     * Set the debug mode here after mActionsListener has been assigned by the subclass
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Let the presenter know whether or not we're in debug mode
        mActionsListener.setIsDebug(getResources().getBoolean(R.bool.is_debug));

        // Hook up and display our debug buttons if we're in debug mode
        mDebugRefreshView =
                view.findViewById(R.id.button_debug_refresh);
        mDebugEndGameView =
                view.findViewById(R.id.button_debug_end_game);

        mDebugRefreshView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshBoard();
            }
        });
        mDebugEndGameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGameOver();
            }
        });

        // If we are in debug mode, show the refresh button
        if (getResources().getBoolean(R.bool.is_debug)) {
            mDebugRefreshView.setVisibility(View.VISIBLE);
            mDebugEndGameView.setVisibility(View.VISIBLE);
        }
    }

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
     *
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


        // Use a ViewTreeObserver to only show highlights once the RecyclerView
        // is done drawing its layout.
        ViewTreeObserver vto = mRecyclerGridView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mRecyclerGridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                // If we are in debug mode, highlight a valid set
                if (getContext().getResources().getBoolean(R.bool.is_debug)) {
                    mActionsListener.highlightValidSet();
                }

                // Restore selected positions
                for (int i = 0; i < mCheckedCount; i++) {
                    selectCard(mCheckedPositions[i]);
                }
            }
        });

    }

    /**
     * Shows a failure animation for submitting an invalid set This will also deselect those cards
     * once the animation is complete
     */
    @Override
    public void showFailAnimation() {
        SetGameCardView cardView;
        for (int i : mCheckedPositions) {
            cardView = (SetGameCardView) mRecyclerGridView.getChildAt(i);
            cardView.animateFailedSet();
        }
    }

    /**
     * Shows a failure animation for submitting an invalid set This will also deselect those cards
     * once the animation is complete
     */
    @Override
    public void showSuccessAnimation(SetGameCardView.AnimationEndCallback callback) {
        SetGameCardView cardView;
        // Show successful animations on all 3 checked cards
        // Only send the callback to the 1st card.
        boolean callbackSent = false;
        for (int i : mCheckedPositions) {
            cardView = (SetGameCardView) mRecyclerGridView.getChildAt(i);
            if (!callbackSent) {
                cardView.animateSuccessfulSet(callback);
                callbackSent = true;
            } else {
                cardView.animateSuccessfulSet(null);
            }
        }
    }

    /**
     * Highlight the card at a specific index
     *
     * @param index Index of the card to highlight
     */
    @Override
    public void highlightCard(int index) {
        // Holds a reference to the card so we can highlight it
        SetGameCardView card =
                (SetGameCardView) mRecyclerGridView.getChildAt(index);
        card.setHighlighted(true);
    }

    /**
     * Select the card at a specific index
     *
     * @param index Index of the card to select
     */
    @Override
    public void selectCard(int index) {
        // Holds a reference to the card so we can select it
        SetGameCardView card =
                (SetGameCardView) mRecyclerGridView.getChildAt(index);
        card.setChecked(true, true);
    }

    /**
     * This is called by the presenter whenever a SET card is clicked by the user.
     * If 3 cards are selected those indices are sent to the presenter
     * who will check if they are a valid set.
     */
    @Override
    public void onSetCardClicked() {

        mCheckedCount = getCheckedItemCount();

        // If we have 3 items selected, check if they are a set
        if (mCheckedCount == 3) {
            SparseBooleanArray checkedItemPositions = getCheckedItemPositions();

            int positionIndex = 0;

            // Loop through SparseBooleanArray and grab the 3 positions that are checked
            for (int i = 0; i < checkedItemPositions.size(); i++) {
                if (checkedItemPositions.valueAt(i)) {
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
        }
    }

    /**
     * Gets the number of items currently selected in the RecyclerView
     *
     * @return Count of the items
     */
    public int getCheckedItemCount() {
        int checkedCount = 0;

        // Loop through all SetGameCardViews in the adapter and count how many are checked
        for (int i = 0; i < mRecyclerGridView.getChildCount(); i++) {
            SetGameCardView cardView = (SetGameCardView) mRecyclerGridView.getChildAt(i);
            if (cardView.isChecked()) {
                checkedCount++;
            }
        }
        return checkedCount;
    }

    /**
     * Gets all locations of checked items in the RecyclerGrid
     *
     * @return SparseBooleanArray containing key-value pairs of locations where checked == true
     */
    public SparseBooleanArray getCheckedItemPositions() {
        SparseBooleanArray checkedLocations = new SparseBooleanArray();

        // Loop through all SetGameCardViews in the adapter and add locations and values for
        // those that are checked
        for (int i = 0; i < mRecyclerGridView.getChildCount(); i++) {
            SetGameCardView cardView = (SetGameCardView) mRecyclerGridView.getChildAt(i);
            if (cardView.isChecked()) {
                checkedLocations.append(i, true);
            }
        }
        return checkedLocations;
    }

    /**
     * Clear the number of choices the player has made. The actual unchecking of cards happens
     * automatically after the success or failure animation is played.
     *
     * @param forceUnchecked specifies whether or not to forcibly uncheck each item without waiting
     *                       for animations to finish.
     */
    public void clearChoices(boolean forceUnchecked) {
        // Clear checked count
        mCheckedCount = 0;

        if (forceUnchecked) {
            for (int i = 0; i < mRecyclerGridView.getChildCount(); i++) {
                SetGameCardView cardView = (SetGameCardView) mRecyclerGridView.getChildAt(i);
                if (cardView.isChecked()) {
                    cardView.setChecked(false, true);
                }
            }
        }
    }

    /**
     * Un-highlight all views in the RecyclerGrid
     */
    public void clearHighlights() {
        // Loop through all SetGameCardViews in the adapter and mark them as Unchecked
        for (int i = 0; i < mRecyclerGridView.getChildCount(); i++) {
            SetGameCardView cardView = (SetGameCardView) mRecyclerGridView.getChildAt(i);
            if (cardView.isHighlighted()) {
                cardView.setHighlighted(false);
            }
        }
    }

    /**
     * The method refreshes the entire board display and should be used in the event
     * the board contents has been changed manually.
     */
    public void refreshBoard() {
        mSetGameRecyclerAdapter.notifyDataSetChanged();
    }

    /**
     * Updates the RecyclerView whenever the hand is updated. Updates member variables
     * with Set Locations and highlights sets if we are in debug mode.
     *
     * @param isOverflow State whether or not the hand is in overflow mode
     * @param deckSize   The number of cards remaining in the deck
     */
    @Override
    public void updateSetHand(boolean isOverflow, int deckSize) {
        mSetGameRecyclerAdapter.updateSetHand(
                mCheckedPositions[0],
                mCheckedPositions[1],
                mCheckedPositions[2],
                isOverflow,
                deckSize);

        // If we're in debug and there are sets available, clear highlights and show new highlights
        if (getContext().getResources().getBoolean(R.bool.is_debug)) {
            // Use a ViewTreeObserver to only show highlights once the RecyclerView
            // is done drawing its layout.
            ViewTreeObserver vto = mRecyclerGridView.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mRecyclerGridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    clearHighlights();
                    mActionsListener.highlightValidSet();
                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Get the SetGame
        SetGame game = mActionsListener.getSetGame();

        SparseBooleanArray checkedItemPositions = getCheckedItemPositions();
        int positionIndex = 0;
        // Loop through SparseBooleanArray and grab the positions that are checked
        for (int i = 0; i < checkedItemPositions.size(); i++) {
            if (checkedItemPositions.valueAt(i)) {
                mCheckedPositions[positionIndex] = checkedItemPositions.keyAt(i);
                positionIndex++;
            }
        }

        // Bundle objects
        outState.putParcelable(getString(R.string.bundle_key_game), Parcels.wrap(game));
        outState.putIntArray(getString(R.string.bundle_key_checked_positions), mCheckedPositions);
        outState.putInt(getString(R.string.bundle_key_checked_count), mCheckedCount);
    }

    /**
     * Public accessor to set the SetHand cards as Clickable
     *
     * @param isClickable The value to set the Clickable attribute
     */
    public void setGameClickable(boolean isClickable) {
        // Loop through all SetGameCardViews in the adapter set them as isClickble
        for (int i = 0; i < mRecyclerGridView.getChildCount(); i++) {
            mRecyclerGridView.getChildAt(i).setEnabled(isClickable);
        }
    }

    public boolean isClickable() {
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
