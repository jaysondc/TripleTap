package com.shakeup.setofthree.TimeAttackGame;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
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
    long mTimeAttackTickLength = 1000;

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
        View root;

        root = inflater.inflate(
                R.layout.fragment_game_time_attack, container, false);

        // Instance the presenter our fragment uses and grab a reference
        mTimeAttackActionsListener = new TimeAttackGamePresenter(this);
        // Have the superclass use the MultiplayerGamePresenter as its GamePresenter
        mActionsListener = mTimeAttackActionsListener;

        // Set up the RecyclerView and assign it to the superclass
        mRecyclerGridView = (RecyclerView) root.findViewById(R.id.game_recycler_grid);

        // Get the desired time attack length from the intent extras
        mTimeAttackLength = getActivity().getIntent().getLongExtra(
                getString(R.string.extra_time_attack_length),
                60000
        );

        // Grab references to our views
        mGameTimer =
                (TextView) root.findViewById(R.id.game_timer);
        mGameScore =
                (TextView) root.findViewById(R.id.game_score);

        // Initialize a game
        mTimeAttackActionsListener.initGame();

        return root;
    }

    @Override
    public void onStop() {
        super.onStop();
        mTimeAttackCountdown.cancel();
    }

    @Override
    public void onSetSuccess() {
//        // Do stuff in response to successful SET claim
//        Snackbar.make(getView(), getString(R.string.message_found_set), Snackbar.LENGTH_LONG)
//                .show();

        // Let the presenter know someone found a set
        mTimeAttackActionsListener.onFindSetSuccess();

    }

    @Override
    public void onSetFailure() {
//        // Do stuff in response to a failed set claim
//        Snackbar.make(getView(), getString(R.string.message_not_set), Snackbar.LENGTH_LONG)
//                .show();

        mTimeAttackActionsListener.onFindSetFailure();

    }

    @Override
    public void onGameOver() {
        // Do stuff when the game is over
        long playerScore = mTimeAttackActionsListener.getPlayerScore();

        Snackbar.make(
                getView(),
                getString(R.string.message_game_over) + " You found " + playerScore + " SETs!",
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.message_restart), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mTimeAttackActionsListener.initGame();
                    }
                })
                .show();
    }


    @Override
    public void startTimeAttackCountdown() {
        mTimeAttackCountdown = new TimeAttackCountdown(
                mTimeAttackLength,
                mTimeAttackTickLength);
    }

    @Override
    public void updateScore(long playerScore) {
        mGameScore.setText(Long.toString(playerScore));
    }

    @Override
    public void uploadScore(long score) {
        // Get the GoogleApiClient from our parent activity
        TimeAttackGameActivity myActivity = (TimeAttackGameActivity) getActivity();
        GoogleApiClient myClient = myActivity.getApiClient();
        // Submit our score
        if(myClient.isConnected()){
            Games.Leaderboards.submitScore(
                    myClient,
                    getString(R.string.leaderboard_time_attack),
                    score);
        }
    }

    @Override
    public void showLeaderBoard() {

    }

    /**
     * Timer class to handle starting the time attack countdown. It will notify
     * the presenter if our time runs out
     */
    public class TimeAttackCountdown extends CountDownTimer {

        public TimeAttackCountdown(long startTime, long interval) {
            super(startTime, interval);

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
        }
    }
}
