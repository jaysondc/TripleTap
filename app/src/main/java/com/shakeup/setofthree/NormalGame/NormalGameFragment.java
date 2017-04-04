package com.shakeup.setofthree.NormalGame;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    // Length for our Time Attack mode
    long mTimeAttackLength;
    // Timer updates every second
    long mTimeAttackTickLength = 1000;

    TextView mGameTimer, mDeckRemaining;

    // Reference to the timer for the game
    TimeAttackCountdown mTimeAttackCountdown;


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

        // Get the desired time attack length from the intent extras
        mTimeAttackLength = getActivity().getIntent().getLongExtra(
                getString(R.string.extra_time_attack_length),
                60000
        );

        // Grab references to our views
        mGameTimer =
                (TextView) root.findViewById(R.id.game_timer);
        mDeckRemaining =
                (TextView) root.findViewById(R.id.deck_remaining);

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
    public void onGameOver() {
        // Do stuff when the game is over
        Snackbar.make(
                getView(),
                getString(R.string.message_game_over),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.message_restart), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mActionsListener.initGame();
                    }
                })
                .show();
    }

    @Override
    public void startTimer() {
        mTimeAttackCountdown = new TimeAttackCountdown(
                mTimeAttackLength,
                mTimeAttackTickLength);
    }

    @Override
    public void updateDeckRemaining(int deckRemaining) {
        mDeckRemaining.setText(Integer.toString(deckRemaining));
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
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // Update the time display in our timer view

            int secsUntilFinished = (int) millisUntilFinished / 1000;
            mGameTimer.setText(Integer.toString(secsUntilFinished));
        }
    }
}
