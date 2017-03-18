package com.shakeup.setofthree.SetGame;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shakeup.setgamelibrary.SetCard;
import com.shakeup.setofthree.CustomView.SetGameCardView;
import com.shakeup.setofthree.R;

import java.util.ArrayList;

/**
 * Created by Jayson on 3/15/2017.
 *
 * RecyclerView Adapter for displaying an array of SetCards to a RecyclerView
 */

public class SetGameRecyclerAdapter
        extends RecyclerView.Adapter<SetGameRecyclerAdapter.SetCardViewHolder> {

    // This is a live reference to the SetHand object in the SetLibrary so it and the UI
    // can always be in sync
    private ArrayList<SetCard> mSetHand;
    private boolean mOverflowMode = false;
    private Context mContext;

    // Store a reference to the GamePresenter so we can let it know when items
    // are clicked
    private final GamePresenter mActionsListener;

    private final String LOG_TAG = getClass().getSimpleName();

    /**
     * Public constructor to create this RecyclerView Adapter. Takes the current
     * context and a SetHand object which holds a list of all the cards in our hand.
     * @param context The current context.
     * @param setHand An ArrayList of SetCard objects to be displayed.
     */
    public SetGameRecyclerAdapter(
            Context context,
            GamePresenter actionsListener,
            ArrayList<SetCard> setHand) {
        super();
        mSetHand = setHand;
        mContext = context;
        mActionsListener = actionsListener;

        // Handle edge case where we start the game in overflow mode
        if( mSetHand.size() > 12 ){
            mOverflowMode = true;
        }
    }

    // Method used to create a ViewHolder for each item in our list
    @Override
    public SetCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Create a new card from the setgame_card_main layout template
        final SetGameCardView cardView = (SetGameCardView) inflater.inflate(
                R.layout.setgame_card_main,
                parent,
                false);

        // Create a ViewHolder for the configured card
        return new SetCardViewHolder(cardView);
    }

    // TODO Remove these after set drawing is fixed
    int i = 0;
    int cardId = 0;

    @Override
    public void onBindViewHolder(SetCardViewHolder holder, int position) {
        // Do all the work setting values to the views based on their position

        // Change attributes to match the appropriate card from the set deck
        SetCard currentCard = mSetHand.get(position);

        int shape = currentCard.getShape().ordinal();
        int color = currentCard.getColor().ordinal();
        int count = currentCard.getCount().ordinal();
        int fill = currentCard.getFill().ordinal();

        holder.cardView.setAll(shape, color, count, fill);

        i++;
        i = i%3;

        // Set debug text
        holder.debugText.setText("" + shape + color + count + fill);
        cardId++;
    }

    @Override
    public int getItemCount() {
        return mSetHand.size();
    }

    @Override
    public long getItemId(int position) {
        // Get the ID generated by the SetCard
        return mSetHand.get(position).getId();
    }

    /**
     * Replace the the old cards with the newly drawn cards. The cards have to be updated
     * differently whether we are moving in and out of overflow mode, or at the end of the
     * game where the deck is empty.
     *
     * Unfortunately the UI SetHand needs to be updated identically to the
     * internal SetHand in SetGame.java, otherwise they may mismatch and sets
     * won't be detected properly.
     * @param one Index of the first new card
     * @param two Index of the second new card
     * @param three Index of the third new card
     * @param isNewHandOverflow Whether or not the incoming deck is in overflow
     * @param deckSize The number of cards remaining in the deck
     */
    public void updateSetHand(
            int one, int two, int three,
            boolean isNewHandOverflow, int deckSize){

        // TODO Change this to more granular refreshing once card draw issue is solved
        notifyDataSetChanged();

//        if( deckSize > 0 && !mOverflowMode  ){
//            // If we have cards in the deck and we aren't in overflow, update
//            // only the cards have have changed
//            notifyItemChanged(one);
//            notifyItemChanged(two);
//            notifyItemChanged(three);
//
//            // If we're moving into overflow mode, notify that cards have been added
//            if( isNewHandOverflow ){
//                notifyItemRangeInserted(11, 3);
//            }
//        } else {
//            // We are at an endgame or moving out of overflow state we need to reflow all
//            // cards past the lowest index of affected cards
//            int lowestIndex = Math.min(Math.min(one, two), three);
//
//            // Notify change occurred from lowest index to the end of the array
//            notifyItemRangeChanged(lowestIndex, mSetHand.size() - lowestIndex);
//            notifyItemRangeRemoved(mSetHand.size()-1, 3);
//        }
//
//        // Store whether or not we are in overflow for later use
//        mOverflowMode = isNewHandOverflow;
    }

    /**
     * ViewHolder implmentation for our SetCard views.
     */
    public class SetCardViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        SetGameCardView cardView;
        boolean isHighlighted;
        boolean isChecked;
        TextView debugText;

        /**
         * Populate data handlers using the given view.
         * @param itemView
         */
        public SetCardViewHolder(View itemView) {
            super(itemView);

            cardView = (SetGameCardView) itemView;
            isHighlighted = ((SetGameCardView) itemView).isHighlighted();
            isChecked = ((SetGameCardView) itemView).isChecked();

            // Use our own implementation of OnClickListener to handle clicks
            cardView.setOnClickListener(this);

            // If we are in debug, show the ID of the card
            debugText = new TextView(mContext);
            if( mContext.getResources().getBoolean(R.bool.is_debug) ){
                ((SetGameCardView) itemView).addView(debugText);
            }
        }

        /**
         * Cusom onClick handler to let cards know they've been clicked
         * @param v
         */
        @Override
        public void onClick(View v) {

            SetGameCardView card = (SetGameCardView) v;

            // Output clicked card to log
            Log.d(LOG_TAG, "Clicked " + card.toString());

            // Toggle checked state and store new state in the ViewHolder
            card.toggleChecked();
            isChecked = ((SetGameCardView) v).isChecked();

            // Notify the GamePresenter that we've been clicked
            mActionsListener.setCardClicked();
        }
    }
}
