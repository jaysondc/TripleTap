package com.shakeup.setofthree.timeattackgame;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.shakeup.setgamelibrary.SetGame;
import com.shakeup.setofthree.R;
import com.shakeup.setofthree.contentprovider.ScoreColumns;
import com.shakeup.setofthree.contentprovider.ScoreProvider;
import com.shakeup.setofthree.customviews.FImageButton;
import com.shakeup.setofthree.gameoverscreen.GameOverFragment;
import com.shakeup.setofthree.interfaces.GoogleApiClientCallback;
import com.shakeup.setofthree.mainmenu.MainMenuActivity;
import com.shakeup.setofthree.pausemenu.PauseContract;
import com.shakeup.setofthree.pausemenu.PauseFragment;
import com.shakeup.setofthree.setgame.GameFragment;

import java.util.Locale;

import info.hoang8f.widget.FButton;

/**
 * Created by Jayson on 3/20/2017.
 * <p>
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

    // UI Views
    TextView mGameTimer, mGameScore;
    FImageButton mPauseButton;
    FButton mHintButton;

    // Reference to the timer for the game
    TimeAttackCountdown mTimeAttackCountdown;

    // Game state
    boolean mIsPaused = false;
    boolean mIsGameOver = false;

    // Default constructor
    public TimeAttackGameFragment() {
    }

    public static TimeAttackGameFragment newInstance() {
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
        if (savedInstanceState != null) {
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
        mRecyclerGridView = root.findViewById(R.id.game_recycler_grid);

        // Grab references to our views
        mGameTimer =
                root.findViewById(R.id.game_timer);
        mGameScore =
                root.findViewById(R.id.game_score);
        mPauseButton =
                root.findViewById(R.id.button_pause);
        mHintButton =
                root.findViewById(R.id.button_hint);

        // Hook up click listeners
        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTimeAttackActionsListener.onPauseClicked();
            }
        });
        mHintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mIsPaused) {
                    mTimeAttackActionsListener.onHintClicked();
                }
            }
        });

        // Initialize a game
        mTimeAttackActionsListener.initGame(mExistingGame, mTimeAttackLength, playerScore);

        // Start the timer
        startTimeAttackCountdown(mTimeAttackLength);

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
        // Pause the game if we aren't already
        if (!mIsPaused && !mIsGameOver) {
            mTimeAttackActionsListener.onPauseClicked();
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
                        && keyCode == KeyEvent.KEYCODE_BACK) {
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
        mTimeAttackCountdown.cancel();
        mTimeAttackCountdown = null;

        super.onStop();
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

        mIsGameOver = true;

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

    /**
     * Update the display with the number sets found
     *
     * @param playerScore Current score
     */
    @Override
    public void updateScore(long playerScore) {
        mGameScore.setText(String.format(Locale.getDefault(), "%d", playerScore));
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
            if (score >= 2) {
                Games.Achievements.unlock(
                        myClient,
                        getString(R.string.achievement_speed_beginner)
                );
            }
            if (score >= 6) {
                Games.Achievements.unlock(
                        myClient,
                        getString(R.string.achievement_speed_intermediate)
                );
            }
            if (score >= 10) {
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
            mGameTimer.setText(String.format(Locale.getDefault(), "%d", 0));
            mTimeAttackActionsListener.onTimeUp();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // Update the time display in our timer view
            int secsUntilFinished = (int) millisUntilFinished / 1000;
            mGameTimer.setText(String.format(Locale.getDefault(), "%d", secsUntilFinished));

            // Save mills remaining so we can pause and start the timer later
            mTimeAttackLength = millisUntilFinished;
        }
    }

    /**
     * Pauses the game and opens the PauseFragment as a dialog for result.
     */
    @Override
    public void pauseGame() {
        if (!mIsPaused) {
            // Stop the timer, elapsed time is stored automatically
            mTimeAttackCountdown.cancel();

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
            mTimeAttackActionsListener.onPauseResultResume();
        } else if (resultCode == PauseContract.RESULT_RESTART) {
            mTimeAttackActionsListener.onPauseResultRestart();
        } else if (resultCode == PauseContract.RESULT_MAIN_MENU) {
            mTimeAttackActionsListener.onPauseResultMainMenu();
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
        startTimeAttackCountdown(mTimeAttackLength);
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
        transaction.replace(R.id.content_frame, TimeAttackGameFragment.newInstance());

        transaction.commit();
    }

    /**
     * Clear the task stack and open the main menu
     */
    @Override
    public void openMainMenu() {
        Intent intent = new Intent(getContext(), MainMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getActivity().finish();
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
