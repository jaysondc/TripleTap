package com.shakeup.setofthree.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shakeup.setofthree.ContentProvider.ScoreColumns;
import com.shakeup.setofthree.R;
import com.shakeup.setofthree.Utilities.Utilities;

/**
 * Created by Jayson on 4/14/2017.
 *
 * RecyclerView Adapter for displaying the local leaderboard
 */

public class LocalLeaderboardRecyclerAdapter
        extends RecyclerView.Adapter<LocalLeaderboardRecyclerAdapter.LocalScoreViewHolder> {

    private final String LOG_TAG = getClass().getSimpleName();

    // Score cursor this adapter will use
    Cursor mScoreCursor;
    Context mContext;

    public LocalLeaderboardRecyclerAdapter(Cursor cursor) {
        super();

        // Get a reference to the cursor we want to use
        mScoreCursor = cursor;
    }

    // Method used to create a ViewHolder for each item in our list
    @Override
    public LocalScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(mContext);

        View scoreView = inflater.inflate(
                R.layout.listitem_score,
                parent,
                false
        );

        // Create a ViewHolder for the configured card
        return new LocalScoreViewHolder(scoreView);
    }

    @Override
    public void onBindViewHolder(LocalScoreViewHolder holder, int position) {
        // Do all the work setting values to the views based on their position

        // Move the cursor to our position
        mScoreCursor.moveToPosition(position);

        // Get indexes of the desired columns
        int scoreColumnIndex = mScoreCursor.getColumnIndex(ScoreColumns.SCORE);
        int timeColumnIndex = mScoreCursor.getColumnIndex(ScoreColumns.TIME);
        int modeIndex = mScoreCursor.getColumnIndex(ScoreColumns.MODE);

        // Retrieve the score and time as a long
        long score = mScoreCursor.getLong(scoreColumnIndex);
        long time = mScoreCursor.getLong(timeColumnIndex);
        String mode = mScoreCursor.getString(modeIndex);

        String scoreString = "";
        // Convert to String format using utility methods
        if(mode.equals(mContext.getString(R.string.value_mode_normal))){
            // If the score is for normal mode, format to time
            scoreString = Utilities.scoreTimeToString(score);
        } else if(mode.equals(mContext.getString(R.string.value_mode_time_attack))){
            // If the score is for time attack, format to string
            scoreString = Utilities.longToString(score);
        }
        String timeString = Utilities.dateToString(time);

        // Set our position to display the current adapter position
        holder.mPosition.setText(Utilities.longToString(position + 1));
        holder.mScore.setText(scoreString);
        holder.mDate.setText(timeString);

    }

    @Override
    public int getItemCount() {
        return mScoreCursor.getCount();
    }

    @Override
    public long getItemId(int position) {
        // Use the time in milliseconds as an ID for each view
        return mScoreCursor.getLong(
                mScoreCursor.getColumnIndex(ScoreColumns.TIME)
        );
    }

    /**
     * ViewHolder for the Score listitem
     */
    public class LocalScoreViewHolder
            extends RecyclerView.ViewHolder {

        public TextView mPosition, mScore, mDate;

        /**
         * Populate data handlers using the given view.
         * @param itemView
         */
        public LocalScoreViewHolder(View itemView) {
            super(itemView);

            mPosition = (TextView) itemView.findViewById(R.id.text_position);
            mScore = (TextView) itemView.findViewById(R.id.text_score);
            mDate = (TextView) itemView.findViewById(R.id.text_date);
        }
    }
}
