package com.shakeup.setofthree.MainMenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shakeup.setofthree.R;
import com.shakeup.setofthree.TimeAttackGame.TimeAttackGameActivity;

/**
 * Created by Jayson on 3/29/2017.
 *
 * This fragment handles the UI for the Single Player buttons in the main menu
 */

public class MainMenuSinglePlayerFragment
        extends android.support.v4.app.Fragment
        implements MainMenuSinglePlayerContract.SinglePlayerView {

    // Presenter to handle all user actions
    MainMenuSinglePlayerContract.UserActionsListener mActionsListener;

    public final String LOG_TAG = this.getClass().getSimpleName();

    /**
     * Allow another class to construct us
     */
    public MainMenuSinglePlayerFragment() {
        // Requires empty public constructor
    }
    public static MainMenuSinglePlayerFragment newInstance() {
        return new MainMenuSinglePlayerFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActionsListener = new MainMenuSinglePlayerPresenter(this);

    }

    /**
     * Hook up click listeners and views when the main view is first created
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_single_player_menu, container, false);

        // Grab all button views
        AppCompatButton twoPlayerButton =
                (AppCompatButton) root.findViewById(R.id.button_normal);
        AppCompatButton threePlayerButton =
                (AppCompatButton) root.findViewById(R.id.button_time_attack);
        AppCompatButton backButton =
                (AppCompatButton) root.findViewById(R.id.button_back);

        // Set listeners to call methods in the presenter
        twoPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call presenter handler for a Normal game
                mActionsListener.onNormalClick();
            }
        });

        threePlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call presenter handler for a Time Attack game
                mActionsListener.onTimeAttackClick();
            }
        });

        // Call the presenter handler for clicking the back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsListener.onBackClick();
            }
        });

        return root;
    }

    /**
     * Launch a Single Player Normal game.
     */
    @Override
    public void openNormal() {
        Log.d(LOG_TAG, "Started a Single Player Normal game.");
//        Intent intent = new Intent(getContext(), SinglePlayerNormalGameActivity.class);
//        startActivity(intent);
    }

    /**
     * Launch a Single Player Time Attack game.
     */
    @Override
    public void openTimeAttack(long timeAttackLength) {
        Log.d(LOG_TAG, "Started a Single Player Time Attack game.");
        Intent intent = new Intent(getContext(), TimeAttackGameActivity.class);
        intent.putExtra(getString(R.string.extra_time_attack_length), timeAttackLength);
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
