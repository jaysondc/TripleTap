package com.shakeup.setofthree.MainMenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shakeup.setofthree.R;
import com.shakeup.setofthree.SetGame.GameActivity;

/**
 * Created by Jayson on 3/2/2017.
 */

public class MainMenuFragment extends android.support.v4.app.Fragment implements MainMenuContract.View {

    // Presenter to handle all user actions
    MainMenuPresenter mActionsListener;

    /**
     * Allow another class to construct us
     */
    public MainMenuFragment() {
        // Requires empty public constructor
    }
    public static MainMenuFragment newInstance() {
        return new MainMenuFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActionsListener = new MainMenuPresenter(this);

    }

    /**
     * Hook up click listeners and views when the main view is first created
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main_menu, container, false);

        /**
         * Set up click listeners for each button
         */
        // Grab all button views
        AppCompatButton singlePlayerButton = (AppCompatButton) root.findViewById(R.id.button_single_player);
        AppCompatButton multiplayerButton = (AppCompatButton) root.findViewById(R.id.button_multi_player);
        AppCompatButton howToPlayButton = (AppCompatButton) root.findViewById(R.id.button_how_to_play);
        AppCompatButton leaderboardButton = (AppCompatButton) root.findViewById(R.id.button_leaderboard);
        AppCompatButton settingsButton = (AppCompatButton) root.findViewById(R.id.button_settings);
        AppCompatButton exitButton = (AppCompatButton) root.findViewById(R.id.button_exit);

        // Set listeners to call methods in the presenter
        singlePlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // This will eventually open different single player options but for
                // now just start a normal single player game
                mActionsListener.startSinglePlayerNormal();
            }
        });


        return root;
    }

    @Override
    public void openSinglePlayerNormal() {
        // Launch single player activity
        Intent intent = new Intent(getContext(), GameActivity.class);
        startActivity(intent);
    }

    @Override
    public void openMultiPlayer(int numPlayers) {
        // Launch multi player with specified number of players
    }

    @Override
    public void showSinglePlayerOptions() {

    }

    @Override
    public void showMultiPlayerOptions() {

    }
}
