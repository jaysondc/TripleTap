package com.shakeup.setofthree.normalgame;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.shakeup.setofthree.R;
import com.shakeup.setofthree.common.contentprovider.ScoreColumns;
import com.shakeup.setofthree.common.contentprovider.ScoreProvider;
import com.shakeup.setofthree.common.customviews.FImageButton;
import com.shakeup.setofthree.gameoverscreen.GameOverFragment;
import com.shakeup.setofthree.common.interfaces.GoogleApiClientCallback;
import com.shakeup.setofthree.pausemenu.PauseContract;
import com.shakeup.setofthree.pausemenu.PauseFragment;
import com.shakeup.setofthree.setgame.GameFragment;

import java.util.Locale;

import info.hoang8f.widget.FButton;

/**
 * Created by Jayson on 4/4/2017.
 * <p>
 * This Fragment handles the UI for the Time Attack game mode
 */

public class NormalGameFragment
        extends GameFragment
        implements NormalGameContract.View {

    public final String LOG_TAG = this.getClass().getSimpleName();

    // Reference to our presenter
    NormalGameContract.UserActionsListener mNormalActionsListener;

    // UI Views
    Chronometer mGameTimerView;
    TextView mDeckRemainingView;
    Button mDebugRefreshView;
    FImageButton mPauseButton;
    FButton mHintButton;

    long mElapsedMillis = 0; // Maintain timer progress between lifecycle changes
    boolean mIsPaused = false; // Maintain whether or not we're already paused
    boolean mIsGameOver = false; // Maintain whether we finished the current game

    // Default constructor
    public NormalGameFragment() {
    }

    public static NormalGameFragment newInstance() {
        return new NormalGameFragment();
    }

    /*
     * Each game mode needs to set up its own onCreateView and assign the following
     * member variables of the superclass
     * mActionsListener a reference to our presenter
     * mRecyclerGridView a reference to the RecyclerView for the game
     * As well as restore the previous state if applicable
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Call our superclass to handle basic game state restoration
        super.onCreateView(inflater, container, savedInstanceState);

        View root;

        root = inflater.inflate(
                R.layout.fragment_game_normal, container, false);

        // Load game mode specific data from a saved state
        if (savedInstanceState != null) {
            mElapsedMillis = savedInstanceState
                    .getLong(getString(R.string.bundle_key_elapsed_millis));
        }

        // Instance the presenter our fragment uses and grab a reference
        mNormalActionsListener = new NormalGamePresenter(this);
        // Have the superclass use the NormalGamePresenter as its GamePresenter
        mActionsListener = mNormalActionsListener;

        // Set up the RecyclerView and assign it to the superclass
        mRecyclerGridView = root.findViewById(R.id.game_recycler_grid);

        // Grab references to our views
        mGameTimerView =
                root.findViewById(R.id.game_timer);
        mDeckRemainingView =
                root.findViewById(R.id.deck_remaining);
        mPauseButton =
                root.findViewById(R.id.button_pause);
        mHintButton =
                root.findViewById(R.id.button_hint);

        // Hook up click listeners
        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNormalActionsListener.onPauseClicked();
            }
        });
        mHintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mIsPaused) {
                    mNormalActionsListener.onHintClicked();
                }
            }
        });

        // Initialize a game
        mNormalActionsListener.initGame(mExistingGame, mElapsedMillis);

        return root;
    }

    /*
     * Save our game state to be restored on rotation or fragment recreation
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Stop the timer and save the elapsed time
        long elapsedMills = this.getTimerElapsedTime();

        // Bundle objects
        outState.putLong(getString(R.string.bundle_key_elapsed_millis), elapsedMills);
    }

    @Override
    public void onPause() {
        // Pause the game if we aren't already
        if (!mIsPaused && !mIsGameOver) {
            mNormalActionsListener.onPauseClicked();
        }
        super.onPause();
    }

    @Override
    public void onResume() {

        // Workaround to capture 'back' button presses from the fragment
        // since we want 'back' to pause the game
        // https://stackoverflow.com/a/29166971/7009268
        if(getView() == null){
            return;
        }
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP
                        && keyCode == KeyEvent.KEYCODE_BACK){
                    if (!mIsPaused) pauseGame();
                    return true;
                }
                return false;
            }
        });

        super.onResume();
    }

    /**
     * Cleanup timer and resources
     */
    @Override
    public void onStop() {
        stopTimer();

        super.onStop();
    }

    @Override
    public void onSetSuccess() {
        // Nothing to do here, handled by superclass
    }

    @Override
    public void onSetFailure() {
        // Nothing to do here, handled by superclass
    }

    @Override
    public void showGameOver() {
        mIsGameOver = true;

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
        transaction.disallowAddToBackStack();
        transaction.commit();
    }

    /**
     * Start the chronograph
     */
    @Override
    public void startTimer(long timerElapsedMills) {
        mGameTimerView.setBase(SystemClock.elapsedRealtime() - timerElapsedMills);
        mGameTimerView.start();
    }

    /**
     * Get the score
     *
     * @return The time in mills
     */
    @Override
    public long getTimerElapsedTime() {
        // Send the score to the presenter
        long elapsedMillis = SystemClock.elapsedRealtime() - mGameTimerView.getBase();
        return elapsedMillis;
    }

    /**
     * Stop the timer if it's currently active
     */
    @Override
    public void stopTimer() {
        if (mGameTimerView != null) {
            mGameTimerView.stop();
        }
    }

    /**
     * Update the display with the number of draws remaining in the deck
     *
     * @param deckRemaining Number of draws left in the deck
     */
    @Override
    public void updateDeckRemaining(int deckRemaining) {
        mDeckRemainingView.setText(String.format(Locale.getDefault(), "%d", deckRemaining));
    }

    /**
     * Uploads the score to the GoogleGamesApi. Unlocks any relevant achievements
     *
     * @param score Score to be uploaded
     */
    @Override
    public void uploadScore(long score) {
        // Get the GoogleApiClient from our parent activity
        GoogleApiClientCallback myActivity = (GoogleApiClientCallback) getActivity();
        GoogleApiClient myClient = myActivity.getGoogleApiClient();

        // Submit our score
        if (!myClient.isConnected()) {
            // Let the user know they aren't signed in but their high score will be saved
            // and uploaded when once they sign in
        } else {
            // Submit score to leaderboard
            Games.Leaderboards.submitScore(
                    myClient,
                    getString(R.string.leaderboard_classic_mode),
                    score);
            // Set flag to know we've uploaded this score.
            mNormalActionsListener.onScoreUploaded(true);

            // Increment number of Classic games played
            Games.Achievements.increment(
                    myClient,
                    getString(R.string.achievement_patience),
                    1
            );

            // Check for high score achievements
            if (score <= 900000) { // 15 minutes
                Games.Achievements.unlock(
                        myClient,
                        getString(R.string.achievement_youre_getting_the_hang_of_this)
                );
            }
            if (score <= 600000) { // 10 minutes
                Games.Achievements.unlock(
                        myClient,
                        getString(R.string.achievement_youre_pretty_good_at_this)
                );
            }
            if (score <= 300000) { // 5 minutes
                Games.Achievements.unlock(
                        myClient,
                        getString(R.string.achievement_youre_crazy)
                );
            }
        }
    }

    @Override
    public void saveLocalScore(long score, boolean uploaded) {
        ContentValues values = new ContentValues();
        values.put(ScoreColumns.MODE, getString(R.string.value_mode_normal));
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
     * Pauses the game and opens the PauseFragment as a dialog for result.
     */
    @Override
    public void pauseGame() {
        if (!mIsPaused) {
            // Stop the timer and store elapsed time
            stopTimer();
            mElapsedMillis = getTimerElapsedTime();
            mIsPaused = true;

            // Set up PauseFragment
            android.support.v4.app.DialogFragment pauseFragment = new PauseFragment();
            pauseFragment.setCancelable(false);
            pauseFragment.setTargetFragment(this, 1);
            pauseFragment.setStyle(STYLE_NORMAL, R.style.PauseDialogStyle);

            // Show fragment
            pauseFragment.show(getFragmentManager(), "dialog");
        }
    }

    /*
     * Retrieve the results from the pause pop up menu and react accordingly.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == PauseContract.RESULT_RESUME) {
            mNormalActionsListener.onPauseResultResume();
        } else if (resultCode == PauseContract.RESULT_RESTART) {
            mNormalActionsListener.onPauseResultRestart();
        } else if (resultCode == PauseContract.RESULT_MAIN_MENU) {
            mNormalActionsListener.onPauseResultMainMenu();
        } else {
            Log.d(LOG_TAG, "The pause menu returned an unexpected code.");
        }

    }

    /**
     * Un-pause the current game. This is called when leaving resuming from the pause
     * menu and coming back from being minimized.
     */
    @Override
    public void resumeGame() {
        startTimer(mElapsedMillis);
        mIsPaused = false;
    }

    /**
     * Start a new game. Ends the current game.
     */
    @Override
    public void restartGame() {
        // Swap in the Single Player Menu Fragment
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_frame, NormalGameFragment.newInstance());

        transaction.commit();
    }

    /**
     * Clear the task stack and open the main menu
     */
    @Override
    public void openMainMenu() {
        getActivity().finishAfterTransition();
    }

    /**
     * Update the hint button to show the available hints remaining. If there are 0 hints
     * remaining, disable the button
     * @param hintsRemaining Number of hints remaining
     */
    @Override
    public void updateHintButton(int hintsRemaining) {
        StringBuilder sb = new StringBuilder(getString(R.string.button_hint));
        sb.append(" (" + hintsRemaining + ")");

        mHintButton.setText(sb.toString());
        if (hintsRemaining <= 0) {
            mHintButton.setEnabled(false);
            mHintButton.setButtonColor(ContextCompat.getColor(getContext(), R.color.fbutton_color_silver));
            mHintButton.setShadowColor(ContextCompat.getColor(getContext(), R.color.fbutton_color_concrete));
        }
    }
}
