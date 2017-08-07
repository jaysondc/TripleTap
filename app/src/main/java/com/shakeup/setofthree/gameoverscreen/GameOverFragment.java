package com.shakeup.setofthree.gameoverscreen;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameUtils;
import com.shakeup.setofthree.R;
import com.shakeup.setofthree.adapters.LocalLeaderboardRecyclerAdapter;
import com.shakeup.setofthree.contentprovider.ScoreColumns;
import com.shakeup.setofthree.contentprovider.ScoreProvider;
import com.shakeup.setofthree.customviews.FImageButton;
import com.shakeup.setofthree.interfaces.GoogleApiClientCallback;
import com.shakeup.setofthree.mainmenu.MainMenuActivity;
import com.shakeup.setofthree.normalgame.NormalGameFragment;
import com.shakeup.setofthree.timeattackgame.TimeAttackGameFragment;

import info.hoang8f.widget.FButton;

/**
 * Created by Jayson on 4/4/2017.
 * <p>
 * This Fragment handles the UI for the Time Attack game mode
 */


public class GameOverFragment
        extends Fragment
        implements GameOverContract.View, LoaderManager.LoaderCallbacks<Cursor> {

    // request codes we use when invoking an external activity
    private static final int RC_RESOLVE = 5000;
    private static final int RC_UNUSED = 5001;
    private static final int RC_SIGN_IN = 9001;
    private static final int SCORE_LOADER_ID = 0;
    public final String LOG_TAG = this.getClass().getSimpleName();
    // Reference to our presenter
    GameOverContract.UserActionsListener mGameOverActionsListener;

    RecyclerView mRecyclerLeaderboard;
    FButton mRestartButton;
    FImageButton mLeaderboardButton, mMainMenuButton;
    Parcelable mRecyclerState = null; // Holds the previous scroll position if it's saved

    // Default constructor
    public GameOverFragment() {
    }

    public static GameOverFragment newInstance() {
        return new GameOverFragment();
    }

    /*
     * Set up all links and load the high score view
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root;

        // Restore our recycler view state
        if (savedInstanceState != null) {
            mRecyclerState = savedInstanceState
                    .getParcelable(getString(R.string.bundle_key_recycler_state));
        }

        root = inflater.inflate(
                R.layout.fragment_game_over, container, false);

        // Get arguments from the fragment that called us
        Bundle arguments = this.getArguments();

        // Instance the presenter our fragment uses and grab a reference
        mGameOverActionsListener = new GameOverPresenter(this);

        // Set up the RecyclerView and assign it to the superclass
        mRecyclerLeaderboard =
                root.findViewById(R.id.recycler_game_over_leaderboard);

        // Grab references to our views
        mRestartButton =
                root.findViewById(R.id.button_restart);
        mLeaderboardButton =
                root.findViewById(R.id.button_leaderboard);
        mMainMenuButton =
                root.findViewById(R.id.button_main_menu);

        // Set click listeners for each button
        mRestartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGameOverActionsListener.onRestartClicked();
            }
        });
        mLeaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGameOverActionsListener.onLeaderboardClicked();
            }
        });
        mMainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGameOverActionsListener.onMainMenuClicked();
            }
        });

        String gameMode = arguments.getString(getString(R.string.extra_game_mode));
        String gameDifficulty = arguments.getString(getString(R.string.extra_difficulty));

        // Let the presenter know we've finished setup and are ready to load
        // the leaderboard
        mGameOverActionsListener.onCreateViewFinished(gameMode, gameDifficulty);

        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save and store the recycler view state
        LinearLayoutManager llm = (LinearLayoutManager) mRecyclerLeaderboard.getLayoutManager();
        Parcelable layoutState = llm.onSaveInstanceState();

        outState.putParcelable(getString(R.string.bundle_key_recycler_state), layoutState);
    }

    @Override
    public void loadLocalLeaderboard(String mode, String difficulty) {

        Bundle args = new Bundle();
        args.putString(getString(R.string.extra_game_mode), mode);
        args.putString(getString(R.string.extra_difficulty), difficulty);

        // Create a loader for the local scoreboard
        getLoaderManager().initLoader(SCORE_LOADER_ID, args, this);
    }

    @Override
    public void restartGame(String gameMode, String gameDifficulty) {
        // Swap in the Single Player Menu Fragment
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (gameMode.equals(getString(R.string.value_mode_normal))) {
            transaction.replace(R.id.content_frame, NormalGameFragment.newInstance());
        } else if (gameMode.equals(getString(R.string.value_mode_time_attack))) {
            transaction.replace(R.id.content_frame, TimeAttackGameFragment.newInstance());
        }
        transaction.commit();
    }

    /*
     * Checks if we're connected to the GoogleAPIClient and opens the global leaderboard
     */
    @Override
    public void openLeaderboard() {
        Log.d(LOG_TAG, "The user opened the leaderboard");

        GoogleApiClientCallback myActivity = (GoogleApiClientCallback) getActivity();
        GoogleApiClient myClient = myActivity.getGoogleApiClient();

        if (myClient.isConnected()) {
            startActivityForResult(
                    Games.Leaderboards.getAllLeaderboardsIntent(myClient),
                    RC_UNUSED);
        } else {
            BaseGameUtils.makeSimpleDialog(
                    getActivity(),
                    getString(R.string.leaderboards_not_available))
                    .show();
        }
    }

    @Override
    public void openFoundSets() {
        // Does nothing for now
        Log.d(LOG_TAG, "The user opened the 'Found Sets' screen");
    }

    // Get the previous Main Menu activity and bring it to the front
    @Override
    public void openMainMenu() {
        Intent intent = new Intent(getContext(), MainMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getActivity().finish();
    }

    public GameOverContract.UserActionsListener getActionsListener() {
        return mGameOverActionsListener;
    }

    /*
     * Cursor Loader and callbacks for the local scoreboard
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Set up selection string
        String selection =
                ScoreColumns.MODE
                        + "=? AND "
                        + ScoreColumns.DIFFICULTY
                        + "=?";

        // Get mode and difficulty from args
        String gameMode = args.getString(getString(R.string.extra_game_mode));
        String gameDifficulty = args.getString(getString(R.string.extra_difficulty));

        // Set up selection args
        String[] selectionArgs = new String[2];
        selectionArgs[0] = gameMode;
        selectionArgs[1] = gameDifficulty;

        // Set up sortOrder string depending on our mode
        String sortOrder = "";
        if (gameMode.equals(getString(R.string.value_mode_normal))) {
            sortOrder = ScoreColumns.SCORE + " ASC, "
                    + ScoreColumns.TIME + " DESC";
                    //+ " LIMIT 5";
        } else if (gameMode.equals(getString(R.string.value_mode_time_attack))) {
            sortOrder = ScoreColumns.SCORE + " DESC, "
                    + ScoreColumns.TIME + " DESC";
                    //+ " LIMIT 5";
        }

        // Create the cursorLoader for our scores
        return new android.support.v4.content.CursorLoader(
                getContext(),
                ScoreProvider.Scores.SCORES,
                ScoreColumns._ALL,
                selection,
                selectionArgs,
                sortOrder
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Now we have a cursor for the local scores.
        Log.d(LOG_TAG, "Data loaded!");

        // Create our adapter
        LocalLeaderboardRecyclerAdapter adapter =
                new LocalLeaderboardRecyclerAdapter(data);
        // Create our layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        // Get the ID of the latest record
        int latestRecordId = adapter.getLatestRecordId();
        adapter.highlightRecord(latestRecordId);

        // Set up the RecyclerView
        mRecyclerLeaderboard.setLayoutManager(layoutManager);
        mRecyclerLeaderboard.setAdapter(adapter);

        if (mRecyclerState != null) {
            // Scroll to our previous position
            layoutManager.onRestoreInstanceState(mRecyclerState);
        } else {
            // Scroll to the most recent score
            mRecyclerLeaderboard.scrollToPosition(adapter.getLatestRecordId());
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        // The loader resets and the data is invalid. Stop displaying the cursor
        mRecyclerLeaderboard.setAdapter(null);
    }
}
