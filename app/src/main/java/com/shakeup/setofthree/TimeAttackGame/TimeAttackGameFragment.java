package com.shakeup.setofthree.TimeAttackGame;

import android.content.ContentValues;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.shakeup.setgamelibrary.SetGame;
import com.shakeup.setofthree.ContentProvider.ScoreColumns;
import com.shakeup.setofthree.ContentProvider.ScoreProvider;
import com.shakeup.setofthree.GameOverScreen.GameOverFragment;
import com.shakeup.setofthree.Interfaces.GoogleApiClientCallback;
import com.shakeup.setofthree.R;
import com.shakeup.setofthree.SetGame.GameFragment;

/**
 * Created by Jayson on 3/20/2017.
 *
 * This Fragment handles the UI for the Time Attack game mode
 */


public class TimeAttackGameFragment
        extends GameFragment
        implements TimeAttackGameContract.View {

    public final String LOG_TAG = this.getClass().getSimpleName();

    // Reference to our presenter
    TimeAttackGameContract.UserActionsListener mTimeAttackActionsListener;

    // Length for our Time Attack mode
    long mTimeAttackLength;
    // Timer updates every second
    long mTimeAttackTickLength = 100;

    TextView mGameTimer, mGameScore;

    // Reference to the timer for the game
    TimeAttackCountdown mTimeAttackCountdown;


    // Default constructor
    public TimeAttackGameFragment(){
    }

    public static TimeAttackGameFragment newInstance(){
        return new TimeAttackGameFragment();
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
        // Call our superclass to handle basic game state restoration
        super.onCreateView(inflater, container, savedInstanceState);

        View root;

        long playerScore = 0;

        root = inflater.inflate(
                R.layout.fragment_game_time_attack, container, false);

        // Load game mode specific data from a saved state
        if(savedInstanceState!=null){
            mTimeAttackLength = savedInstanceState
                    .getLong(getString(R.string.bundle_key_timer_length));
            playerScore = savedInstanceState
                    .getLong(getString(R.string.bundle_key_ta_score));

        } else {
            // Get the desired time attack length from the intent extras
            mTimeAttackLength = getActivity().getIntent().getLongExtra(
                    getString(R.string.extra_time_attack_length),
                    60000
            );
        }

        // Instance the presenter our fragment uses and grab a reference
        mTimeAttackActionsListener = new TimeAttackGamePresenter(this);
        // Have the superclass use the MultiplayerGamePresenter as its GamePresenter
        mActionsListener = mTimeAttackActionsListener;

        // Set up the RecyclerView and assign it to the superclass
        mRecyclerGridView = (RecyclerView) root.findViewById(R.id.game_recycler_grid);

        // Grab references to our views
        mGameTimer =
                (TextView) root.findViewById(R.id.game_timer);
        mGameScore =
                (TextView) root.findViewById(R.id.game_score);

        // Initialize a game
        mTimeAttackActionsListener.initGame(mExistingGame, mTimeAttackLength, playerScore);

        return root;
    }

    /*
     * Save our game state to be restored on rotation or fragment recreation
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Stop the timer
        mTimeAttackCountdown.cancel();

        // Get the SetGame
        SetGame game = mActionsListener.getSetGame();
        // Get the score
        long score = mTimeAttackActionsListener.getPlayerScore();

        // Bundle objects
        outState.putLong(getString(R.string.bundle_key_timer_length), mTimeAttackLength);
        outState.putLong(getString(R.string.bundle_key_ta_score), score);
    }

    @Override
    public void onPause() {
        super.onPause();
        mTimeAttackCountdown.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();
        startTimeAttackCountdown(mTimeAttackLength);
    }



    @Override
    public void onSetSuccess() {
        // Let the presenter know someone found a set
        mTimeAttackActionsListener.onFindSetSuccess();

    }

    @Override
    public void onSetFailure() {
        mTimeAttackActionsListener.onFindSetFailure();
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
                getString(R.string.value_mode_time_attack));
        args.putString(
                getString(R.string.extra_difficulty),
                getString(R.string.value_difficulty_normal));
        gameOverFragment.setArguments(args);

        transaction.replace(R.id.content_frame, gameOverFragment);
        transaction.disallowAddToBackStack();
        transaction.commit();
    }


    @Override
    public void startTimeAttackCountdown(long mMillisRemaining) {
        mTimeAttackCountdown = new TimeAttackCountdown(
                mMillisRemaining,
                mTimeAttackTickLength);
    }

    @Override
    public void updateScore(long playerScore) {
        mGameScore.setText(Long.toString(playerScore));
    }

    @Override
    public void uploadScore(long score) {
        // Get the GoogleApiClient from our parent activity
        GoogleApiClientCallback myActivity = (GoogleApiClientCallback) getActivity();
        GoogleApiClient myClient = myActivity.getGoogleApiClient();

        // Submit our score
        if(!myClient.isConnected()){
            // Let the user know they aren't signed in but their high score will be saved
            // and uploaded when once they sign in
        } else {
            Games.Leaderboards.submitScore(
                    myClient,
                    getString(R.string.leaderboard_time_attack),
                    score);

            // Increment number of Classic games played
            Games.Achievements.increment(
                    myClient,
                    getString(R.string.achievement_persistence),
                    1
            );

            // Check for high score achievements
            if(score >= 2){
                Games.Achievements.unlock(
                        myClient,
                        getString(R.string.achievement_speed_beginner)
                );
            }
            if(score >= 6){
                Games.Achievements.unlock(
                        myClient,
                        getString(R.string.achievement_speed_intermediate)
                );
            }
            if(score >= 10){
                Games.Achievements.unlock(
                        myClient,
                        getString(R.string.achievement_speed_master)
                );
            }
        }
    }

    @Override
    public void saveLocalScore(long score, boolean uploaded) {
        ContentValues values = new ContentValues();
        values.put(ScoreColumns.MODE, getString(R.string.value_mode_time_attack));
        values.put(ScoreColumns.DIFFICULTY, getString(R.string.value_difficulty_normal)); //TODO Change when easy mode exists
        values.put(ScoreColumns.SCORE, score);
        values.put(ScoreColumns.TIME, System.currentTimeMillis());
        values.put(ScoreColumns.UPLOADED, uploaded);

        getContext().getContentResolver().insert(
                ScoreProvider.Scores.SCORES,
                values
        );

        Log.d(LOG_TAG, "Saved score locally!");
    }

    /**
     * Timer class to handle starting the time attack countdown. It will notify
     * the presenter if our time runs out
     */
    public class TimeAttackCountdown extends CountDownTimer {

        public TimeAttackCountdown(long timerLength, long interval) {
            super(timerLength, interval);

            start();
        }

        @Override
        public void onFinish() {
            mGameTimer.setText(Integer.toString(0));
            mTimeAttackActionsListener.onTimeUp();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // Update the time display in our timer view
            int secsUntilFinished = (int) millisUntilFinished / 1000;
            mGameTimer.setText(Integer.toString(secsUntilFinished));

            // Save mills remaining so we can pause and start the timer later
            mTimeAttackLength = millisUntilFinished;
        }
    }
}
