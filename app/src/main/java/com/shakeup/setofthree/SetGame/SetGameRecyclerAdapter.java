package com.shakeup.setofthree.SetGame;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shakeup.setgamelibrary.SetCard;
import com.shakeup.setofthree.CustomView.SetGameCard;
import com.shakeup.setofthree.R;

import java.util.ArrayList;

/**
 * Created by Jayson on 3/15/2017.
 */

public class SetGameRecyclerAdapter
        extends RecyclerView.Adapter<SetGameRecyclerAdapter.SetCardViewHolder> {

    // Hold the current SetHand to be displayed to the GridView
    private ArrayList<SetCard> mSetHand;
    private Context mContext;

    private final String LOG_TAG = getClass().getSimpleName();

    /**
     * Public constructor to create this RecyclerView Adapter. Takes the current
     * context and a SetHand object which holds a list of all the cards in our hand.
     * @param context The current context.
     * @param setHand An ArrayList of SetCard objects to be displayed.
     */
    public SetGameRecyclerAdapter(Context context, ArrayList<SetCard> setHand) {
        super();
        mSetHand = setHand;
        mContext = context;
    }

    // Method used to create a ViewHolder for each item in our list
    @Override
    public SetCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Create a new card from the setgame_card_main layout template
        final SetGameCard cardView = (SetGameCard) inflater.inflate(
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
     * ViewHolder implmentation for our SetCard views.
     */
    public class SetCardViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        SetGameCard cardView;

        /**
         * Populate data handlers using the given view. In our case there
         * aren't any since the Card is just an image.
         * @param itemView
         */
        public SetCardViewHolder(View itemView) {
            super(itemView);

            cardView = (SetGameCard) itemView;
        }

        @Override
        public void onClick(View v) {
            // Do things when we are clicked.
        }
    }
}
