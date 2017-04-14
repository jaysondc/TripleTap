package com.shakeup.setofthree.NormalGame;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.shakeup.setofthree.ContentProvider.ScoreColumns;
import com.shakeup.setofthree.ContentProvider.ScoreProvider;
import com.shakeup.setofthree.GameOverScreen.GameOverFragment;
import com.shakeup.setofthree.Interfaces.GoogleApiClientCallback;
import com.shakeup.setofthree.R;
import com.shakeup.setofthree.SetGame.GameFragment;

/**
 * Created by Jayson on 4/4/2017.
 *
 * This Fragment handles the UI for the Time Attack game mode
 */


public class NormalGameFragment
        extends GameFragment
        implements NormalGameContract.View {

    public final String LOG_TAG = this.getClass().getSimpleName();

    // Reference to our presenter
    NormalGameContract.UserActionsListener mNormalActionsListener;

    Chronometer mGameTimerView;
    TextView mDeckRemainingView;
    Button mDebugRefreshView;

    // Default constructor
    public NormalGameFragment(){
    }

    public static NormalGameFragment newInstance(){
        return new NormalGameFragment();
    }

    /*
     * Each game mode needs to set up its own onCreateView and assign the following
     * member variables of the superclass
     * mActionsListener a reference to our presenter
     * mRecyclerGridView a reference to the RecyclerView for the game
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root;

        root = inflater.inflate(
                R.layout.fragment_game_normal, container, false);

        // Instance the presenter our fragment uses and grab a reference
        mNormalActionsListener = new NormalGamePresenter(this);
        // Have the superclass use the MultiplayerGamePresenter as its GamePresenter
        mActionsListener = mNormalActionsListener;

        // Set up the RecyclerView and assign it to the superclass
        mRecyclerGridView = (RecyclerView) root.findViewById(R.id.game_recycler_grid);

        // Grab references to our views
        mGameTimerView =
                (Chronometer) root.findViewById(R.id.game_timer);
        mDeckRemainingView =
                (TextView) root.findViewById(R.id.deck_remaining);
        mDebugRefreshView =
                (Button) root.findViewById(R.id.button_debug_refresh);

        mDebugRefreshView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshBoard();
            }
        });

        // If we are in debug mode, show the refresh button
        if(getResources().getBoolean(R.bool.is_debug)){
            mDebugRefreshView.setVisibility(View.VISIBLE);
        }

        // Initialize a game
        mNormalActionsListener.initGame();

        return root;
    }

    @Override
    public void onSetSuccess() {
//        // Do stuff in response to successful SET claim
//        Snackbar.make(getView(), getString(R.string.message_found_set), Snackbar.LENGTH_LONG)
//                .show();


    }

    @Override
    public void onSetFailure() {
//        // Do stuff in response to a failed set claim
//        Snackbar.make(getView(), getString(R.string.message_not_set), Snackbar.LENGTH_LONG)
//                .show();

    }


    @Override
    public void showGameOver() {
        // Swap in the Game Over Fragment
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        GameOverFragment gameOverFragment = GameOverFragment.newInstance();

        // Add args so the game over fragment knows what game mode we're in
        Bundle args = new Bundle();
        args.putString(
                getString(R.string.extra_game_mode),
                getString(R.string.value_mode_normal));
        args.putString(
                getString(R.string.extra_difficulty),
                getString(R.string.value_difficulty_normal));
        gameOverFragment.setArguments(args);

        transaction.replace(R.id.content_frame, gameOverFragment);
        transaction.commit();
    }

    /**
     * Start the chronograph
     */
    @Override
    public void startTimer() {
        mGameTimerView.setBase(SystemClock.elapsedRealtime());
        mGameTimerView.start();
    }

    /**
     * Get the score
     * @return The time in mills
     */
    @Override
    public long getScore(){
        // Send the score to the presenter
        long elapsedMillis = SystemClock.elapsedRealtime() - mGameTimerView.getBase();
        return elapsedMillis;
    }

    @Override
    public void stopTimer() {
        mGameTimerView.stop();
    }

    @Override
    public void updateDeckRemaining(int deckRemaining) {
        mDeckRemainingView.setText(Integer.toString(deckRemaining));
    }

    @Override
    public void showLeaderBoard() {

    }

    /**
     * Uploads the score to the GoogleGamesApi. Unlocks any relevant achievements
     * @param score
     */
    @Override
    public void uploadScore(long score){
        // Get the GoogleApiClient from our parent activity
        GoogleApiClientCallback myActivity = (GoogleApiClientCallback) getActivity();
        GoogleApiClient myClient = myActivity.getGoogleApiClient();

        // Submit our score
        if(!myClient.isConnected()){
            // Let the user know they aren't signed in but their high score will be saved
            // and uploaded when once they sign in
        } else {
            // Submit score to leaderboard
            Games.Leaderboards.submitScore(
                    myClient,
                    getString(R.string.leaderboard_classic_mode),
                    score);

            // Increment number of Classic games played
            Games.Achievements.increment(
                    myClient,
                    getString(R.string.achievement_patience),
                    1
            );

            // Check for high score achievements
            if(score <= 900000){ // 15 minutes
                Games.Achievements.unlock(
                        myClient,
                        getString(R.string.achievement_youre_getting_the_hang_of_this)
                );
            }
            if(score <= 600000){ // 10 minutes
                Games.Achievements.unlock(
                        myClient,
                        getString(R.string.achievement_youre_pretty_good_at_this)
                );
            }
            if(score <= 300000){ // 5 minutes
                Games.Achievements.unlock(
                        myClient,
                        getString(R.string.achievement_youre_crazy)
                );
            }
        }
    }

    void saveLocalScore(long score){
        ContentValues values = new ContentValues();
        values.put(ScoreColumns.MODE, getString(R.string.value_mode_normal));
        values.put(ScoreColumns.DIFFICULTY, getString(R.string.value_difficulty_normal));
        values.put(ScoreColumns.SCORE, score);
        values.put(ScoreColumns.TIME, System.currentTimeMillis());
        values.put(ScoreColumns.UPLOADED, false); // This should represent
                    // whether or not we actually uploaded the score

        getContext().getContentResolver().insert(
                ScoreProvider.Scores.SCORES,
                values
        );

        Log.d(LOG_TAG, "Saved score locally!");
    }

    void readAllScores(){
        Cursor cursor = getContext().getContentResolver().query(
                ScoreProvider.Scores.SCORES,
                ScoreColumns._ALL,
                null,
                new String[]{},
                ScoreColumns._ID
        );


        System.out.println("Cursor has " + cursor.getCount() + " items.");

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            int id = cursor.getInt(cursor.getColumnIndex(ScoreColumns._ID));
            long score = cursor.getLong(cursor.getColumnIndex(ScoreColumns.SCORE));
            long time = cursor.getLong(cursor.getColumnIndex(ScoreColumns.TIME));
            String mode = cursor.getString(cursor.getColumnIndex(ScoreColumns.MODE));
            String difficulty = cursor.getString(cursor.getColumnIndex(ScoreColumns.DIFFICULTY));

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(id)
                    .append(" ")
                    .append(score)
                    .append(" ")
                    .append(time)
                    .append(" ")
                    .append(mode)
                    .append(" ")
                    .append(difficulty);

            Log.d(LOG_TAG, stringBuilder.toString());

            cursor.moveToNext();
        }
        cursor.close();
    }

}
