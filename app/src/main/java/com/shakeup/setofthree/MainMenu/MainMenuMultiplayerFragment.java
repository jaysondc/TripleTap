package com.shakeup.setofthree.MainMenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shakeup.setofthree.MultiplayerGame.MultiplayerGameActivity;
import com.shakeup.setofthree.R;

/**
 * Created by Jayson on 3/2/2017.
 *
 * This fragment handles the UI for the multiplayer buttons in the main menu
 */

public class MainMenuMultiplayerFragment
        extends android.support.v4.app.Fragment
        implements MainMenuMultiplayerContract.MultiplayerView {

    // Presenter to handle all user actions
    MainMenuMultiplayerPresenter mActionsListener;

    public final String LOG_TAG = this.getClass().getSimpleName();

    /**
     * Allow another class to construct us
     */
    public MainMenuMultiplayerFragment() {
        // Requires empty public constructor
    }
    public static MainMenuMultiplayerFragment newInstance() {
        return new MainMenuMultiplayerFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActionsListener = new MainMenuMultiplayerPresenter(this);

    }

    /**
     * Hook up click listeners and views when the main view is first created
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_multiplayer_menu, container, false);

        // Grab all button views
        AppCompatButton twoPlayerButton =
                (AppCompatButton) root.findViewById(R.id.button_two_players);
        AppCompatButton threePlayerButton =
                (AppCompatButton) root.findViewById(R.id.button_three_players);
        AppCompatButton fourPlayerButton =
                (AppCompatButton) root.findViewById(R.id.button_four_players);
        AppCompatButton backButton =
                (AppCompatButton) root.findViewById(R.id.button_back);

        // Set listeners to call methods in the presenter
        twoPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call presenter handler for 2 player click
                mActionsListener.onMultiPlayerOptionClick(2);
            }
        });

        threePlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call presenter handler for 3 player click
                mActionsListener.onMultiPlayerOptionClick(3);
            }
        });

        fourPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call presenter handler for 4 player click
                mActionsListener.onMultiPlayerOptionClick(4);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsListener.onBackClick();
            }
        });

        return root;
    }

    /**
     * Launch multi player with specified number of players
     * @param numPlayers The number of players desired in a multiplayer game
     */
    @Override
    public void openMultiPlayer(int numPlayers) {
        Log.d(LOG_TAG, "Started a " + numPlayers + " player Multiplayer game.");
        Intent intent = new Intent(getContext(), MultiplayerGameActivity.class);
        intent.putExtra(getString(R.string.extra_num_players), numPlayers);
        startActivity(intent);
    }

    /**
     * Replicate pressing the back button to revert to the
     * previous fragment
     */
    @Override
    public void openPreviousFragment() {
        getActivity().onBackPressed();
    }
}
