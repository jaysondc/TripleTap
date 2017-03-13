package com.shakeup.setofthree.SetGame;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.shakeup.setgamelibrary.SetCard;

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
    public View getView(int position, View convertView, ViewGroup parent) {

        // Create attribute set to be applied to the new card

        TextView text = new TextView(parent.getContext());

        text.setText("TESTING");

        return text;
    }



    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
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
