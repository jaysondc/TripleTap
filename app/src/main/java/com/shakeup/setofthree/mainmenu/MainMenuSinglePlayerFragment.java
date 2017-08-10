package com.shakeup.setofthree.mainmenu;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shakeup.setofthree.R;
import com.shakeup.setofthree.normalgame.NormalGameActivity;
import com.shakeup.setofthree.practicegame.PracticeGameActivity;
import com.shakeup.setofthree.timeattackgame.TimeAttackGameActivity;

import info.hoang8f.widget.FButton;

/**
 * Created by Jayson on 3/29/2017.
 * <p>
 * This fragment handles the UI for the Single Player buttons in the main menu
 */

public class MainMenuSinglePlayerFragment
        extends android.support.v4.app.Fragment
        implements MainMenuSinglePlayerContract.SinglePlayerView {

    public final String LOG_TAG = this.getClass().getSimpleName();
    // Presenter to handle all user actions
    MainMenuSinglePlayerContract.UserActionsListener mActionsListener;

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
        FButton normalButton =
                root.findViewById(R.id.button_normal);
        FButton timeAttackButton =
                root.findViewById(R.id.button_time_attack);
        FButton practiceButton =
                root.findViewById(R.id.button_practice);
        FButton backButton =
                root.findViewById(R.id.button_back);

        // Set listeners to call methods in the presenter
        normalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call presenter handler for a Normal game
                mActionsListener.onNormalClick();
            }
        });
        timeAttackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call presenter handler for a Time Attack game
                mActionsListener.onTimeAttackClick();
            }
        });
        practiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call presenter handler for a Time Attack game
                mActionsListener.onPracticeClick();
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
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle();
        Intent intent = new Intent(getContext(), NormalGameActivity.class);
        startActivity(intent, bundle);
    }

    /**
     * Launch a Single Player Time Attack game.
     */
    @Override
    public void openTimeAttack(long timeAttackLength) {
        Log.d(LOG_TAG, "Started a Single Player Time Attack game.");
        Intent intent = new Intent(getContext(), TimeAttackGameActivity.class);
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle();
        intent.putExtra(getString(R.string.extra_time_attack_length), timeAttackLength);
        startActivity(intent, bundle);
    }

    /**
     * Launch a Single player Practice game.
     */
    @Override
    public void openPractice() {
        Log.d(LOG_TAG, "Started a Single Player Practice game.");
        Intent intent = new Intent(getContext(), PracticeGameActivity.class);
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle();
        startActivity(intent, bundle);
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
