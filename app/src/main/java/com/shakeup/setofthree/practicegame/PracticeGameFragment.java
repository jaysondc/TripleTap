package com.shakeup.setofthree.practicegame;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.shakeup.setofthree.R;
import com.shakeup.setofthree.common.customviews.FImageButton;
import com.shakeup.setofthree.pausemenu.PauseContract;
import com.shakeup.setofthree.pausemenu.PauseFragment;
import com.shakeup.setofthree.setgame.GameFragment;

import info.hoang8f.widget.FButton;

/**
 * Created by Jayson on 8/7/2017.
 * <p>
 * This Fragment handles the UI for the Time Attack game mode
 */

public class PracticeGameFragment
        extends GameFragment
        implements PracticeGameContract.View {

    public final String LOG_TAG = this.getClass().getSimpleName();

    // Reference to our presenter
    PracticeGameContract.UserActionsListener mPracticeActionsListener;

    // UI Views
    Button mDebugRefreshView;
    FImageButton mPauseButton;
    FButton mHintButton;

    long mElapsedMillis = 0; // Maintain timer progress between lifecycle changes
    boolean mIsPaused = false; // Maintain whether or not we're already paused
    boolean mIsGameOver = false; // Maintain whether we finished the current game

    // Default constructor
    public PracticeGameFragment() {
    }

    public static PracticeGameFragment newInstance() {
        return new PracticeGameFragment();
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
                R.layout.fragment_game_practice, container, false);

        // Load game mode specific data from a saved state
        if (savedInstanceState != null) {
            mElapsedMillis = savedInstanceState
                    .getLong(getString(R.string.bundle_key_elapsed_millis));
        }

        // Instance the presenter our fragment uses and grab a reference
        mPracticeActionsListener = new PracticeGamePresenter(this);
        // Have the superclass use the PracticeGamePresenter as its GamePresenter
        mActionsListener = mPracticeActionsListener;

        // Set up the RecyclerView and assign it to the superclass
        mRecyclerGridView = root.findViewById(R.id.game_recycler_grid);

        // Grab references to our views
        mPauseButton =
                root.findViewById(R.id.button_pause);
        mHintButton =
                root.findViewById(R.id.button_hint);

        // Hook up click listeners
        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPracticeActionsListener.onPauseClicked();
            }
        });
        mHintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPracticeActionsListener.onHintClicked();
            }
        });

        // Initialize a game
        mPracticeActionsListener.initGame(mExistingGame);

        return root;
    }

    /*
     * Save our game state to be restored on rotation or fragment recreation
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        // Pause the game if we aren't already
        if (!mIsPaused && !mIsGameOver) {
            mPracticeActionsListener.onPauseClicked();
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
     * Cleanup resources
     */
    @Override
    public void onStop() {
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
        // Nothing to do here, no game over in practice mode
    }

    /**
     * Pauses the game and opens the PauseFragment as a dialog for result.
     */
    @Override
    public void pauseGame() {

        mIsPaused = true;

        // Set up PauseFragment
        android.support.v4.app.DialogFragment pauseFragment = new PauseFragment();
        pauseFragment.setCancelable(false);
        pauseFragment.setTargetFragment(this, 1);
        pauseFragment.setStyle(STYLE_NORMAL, R.style.PauseDialogStyle);

        // Show fragment
        pauseFragment.show(getFragmentManager(), "dialog");
    }

    /*
     * Retrieve the results from the pause pop up menu and react accordingly.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == PauseContract.RESULT_RESUME) {
            mPracticeActionsListener.onPauseResultResume();
        } else if (resultCode == PauseContract.RESULT_RESTART) {
            mPracticeActionsListener.onPauseResultRestart();
        } else if (resultCode == PauseContract.RESULT_MAIN_MENU) {
            mPracticeActionsListener.onPauseResultMainMenu();
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
        transaction.replace(R.id.content_frame, PracticeGameFragment.newInstance());

        transaction.commit();
    }

    /**
     * Clear the task stack and open the main menu
     */
    @Override
    public void openMainMenu() {
        getActivity().finishAfterTransition();
    }
}
