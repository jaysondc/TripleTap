package com.shakeup.setofthree.SetGame;

import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.shakeup.setgamelibrary.SetCard;
import com.shakeup.setofthree.CustomView.SetGameCard;
import com.shakeup.setofthree.R;

import java.util.ArrayList;

/**
 * Created by Jayson on 3/13/2017.
 *
 * List Adapter implementation to display create views for Set Cards on a
 * GridView
 */

public class SetGameGridAdapter implements ListAdapter {
    ArrayList<SetCard> mSetHand;


    // Public constructor takes a ArrayList of Set Cards
    public SetGameGridAdapter(ArrayList<SetCard> setHand){
        mSetHand = setHand;
    }


    // Creates a Set Card View and returns it to the GridView.
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if(convertView == null){
            // Create a new card from the setgame_card_main layout template
            final SetGameCard cardView = (SetGameCard) inflater.inflate(
                    R.layout.setgame_card_main,
                    parent,
                    false);

            // Change attributes to match the Set Deck
            SetCard currentCard = mSetHand.get(position);

            cardView.setColor(currentCard.getColor().ordinal());
            cardView.setShape(currentCard.getShape().ordinal());
            cardView.setCount(currentCard.getCount().ordinal());
            cardView.setFill(currentCard.getFill().ordinal());

            return cardView;
        } else {
            // recycle convertView argument
            return convertView;
        }

    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return mSetHand.size();
    }

    @Override
    public Object getItem(int position) {
        return mSetHand.get(position);
    }

    @Override
    public long getItemId(int position) {
        // Our cards don't have IDs so we don't need this.
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return mSetHand.isEmpty();
    }

}
