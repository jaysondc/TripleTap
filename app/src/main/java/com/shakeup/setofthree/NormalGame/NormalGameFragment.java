package com.shakeup.setofthree.NormalGame;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
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

    @Override
    public void uploadScore(long score){
        // Get the GoogleApiClient from our parent activity
        NormalGameActivity myActivity = (NormalGameActivity) getActivity();
        GoogleApiClient myClient = myActivity.getApiClient();
        // Submit our score
        if(myClient.isConnected()){
            Games.Leaderboards.submitScore(
                    myClient,
                    getString(R.string.leaderboard_normal),
                    score);
        }
    }

}
