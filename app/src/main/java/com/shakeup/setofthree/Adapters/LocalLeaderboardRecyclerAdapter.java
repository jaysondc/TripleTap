package com.shakeup.setofthree.Adapters;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shakeup.setofthree.ContentProvider.ScoreColumns;
import com.shakeup.setofthree.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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

    public LocalLeaderboardRecyclerAdapter(Cursor cursor) {
        super();

        // Get a reference to the cursor we want to use
        mScoreCursor = cursor;
    }

    // Method used to create a ViewHolder for each item in our list
    @Override
    public LocalScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

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

        int scoreColumnIndex = mScoreCursor.getColumnIndex(ScoreColumns.SCORE);
        int timeColumnIndex = mScoreCursor.getColumnIndex(ScoreColumns.TIME);

        long score = mScoreCursor.getLong(scoreColumnIndex);
        long time = mScoreCursor.getLong(timeColumnIndex);

        long scoreMinutes =
                TimeUnit.MILLISECONDS.toMinutes(score);
        long scoreSeconds =
                TimeUnit.MILLISECONDS.toSeconds(score)
                - TimeUnit.MINUTES.toSeconds(scoreMinutes);
        long scoreHundreths =
                (score
                - TimeUnit.MINUTES.toMillis(scoreMinutes)
                - TimeUnit.SECONDS.toMillis(scoreSeconds))
                /10;

        String scoreString = String.format(
                Locale.getDefault(),
                "%02d:%02d:%02d",
                scoreMinutes,
                scoreSeconds,
                scoreHundreths
        );

        String timeString = new SimpleDateFormat(
                "MM/dd/yyyy hh:mm a",
                Locale.getDefault())
                .format(new Date(time));

        // Set our position to display the current adapter position
        holder.mPosition.setText(Integer.toString(position + 1));
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
