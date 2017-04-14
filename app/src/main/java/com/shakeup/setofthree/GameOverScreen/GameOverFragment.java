package com.shakeup.setofthree.GameOverScreen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.shakeup.setofthree.NormalGame.NormalGamePresenter;
import com.shakeup.setofthree.R;

/**
 * Created by Jayson on 4/4/2017.
 *
 * This Fragment handles the UI for the Time Attack game mode
 */


public class GameOverFragment 
        extends Fragment
        implements GameOverContract.View {

    public final String LOG_TAG = this.getClass().getSimpleName();

    // Reference to our presenter
    GameOverContract.UserActionsListener mGameOverActionsListener;

    Chronometer mGameTimerView;
    TextView mDeckRemainingView;
    Button mDebugRefreshView;
    RecyclerView mRecyclerLeaderboard;

    // Default constructor
    public GameOverFragment(){
    }

    public static GameOverFragment newInstance(){
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

        root = inflater.inflate(
                R.layout.fragment_game_over_single_player, container, false);

        // Instance the presenter our fragment uses and grab a reference
        mGameOverActionsListener = new GameOverPresenter(this);

        // Set up the RecyclerView and assign it to the superclass
        mRecyclerLeaderboard = 
                (RecyclerView) root.findViewById(R.id.recycler_game_over_leaderboard);

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
        mGameOverActionsListener.initGame();

        return root;
    }
    
    

}
