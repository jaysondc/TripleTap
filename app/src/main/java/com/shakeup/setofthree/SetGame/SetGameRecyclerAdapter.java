package com.shakeup.setofthree.SetGame;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    // Hold the current SetHand to be displayed to the GridView
    private ArrayList<SetCard> mSetHand;
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

    @Override
    public void onBindViewHolder(SetCardViewHolder holder, int position) {
        // Do all the work setting values to the views based on their position

        // Change attributes to match the appropriate card from the set deck
        SetCard currentCard = mSetHand.get(position);

        holder.cardView.setColor(currentCard.getColor().ordinal());
        holder.cardView.setShape(currentCard.getShape().ordinal());
        holder.cardView.setCount(currentCard.getCount().ordinal());
        holder.cardView.setFill(currentCard.getFill().ordinal());
    }

    @Override
    public int getItemCount() {
        return mSetHand.size();
    }

    /**
     * Replace the current hand with an updated one after cards are replaced
     * @param newHand
     */
    public void setSetHand(ArrayList<SetCard> newHand){
        mSetHand = newHand;
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
