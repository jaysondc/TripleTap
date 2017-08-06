package com.shakeup.setofthree.mainmenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameUtils;
import com.shakeup.setofthree.R;
import com.shakeup.setofthree.customviews.FImageButton;
import com.shakeup.setofthree.tutorial.TutorialActivity;

import info.hoang8f.widget.FButton;

/**
 * Created by Jayson on 3/2/2017.
 */

public class MainMenuFragment
        extends android.support.v4.app.Fragment
        implements MainMenuContract.MainView {

    // request codes we use when invoking an external activity
    private static final int RC_RESOLVE = 5000;
    private static final int RC_UNUSED = 5001;
    private static final int RC_SIGN_IN = 9001;
    // Presenter to handle all user actions
    MainMenuContract.UserActionsListener mActionsListener;
    private String LOG_TAG = getClass().getSimpleName();

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
        FButton singlePlayerButton =
                root.findViewById(R.id.button_single_player);
        FButton multiplayerButton =
                root.findViewById(R.id.button_multi_player);
        FButton howToPlayButton =
                root.findViewById(R.id.button_how_to_play);
        FImageButton leaderboardButton =
                root.findViewById(R.id.button_leaderboard);
        FImageButton achievementsButton =
                root.findViewById(R.id.button_achievements);
        FImageButton settingsButton =
                root.findViewById(R.id.button_settings);

        // Set listeners to call methods in the presenter
        singlePlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show single player options
                mActionsListener.onSinglePlayerClick();
            }
        });

        multiplayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show multiplayer options
                mActionsListener.onMultiplayerClick();
            }
        });

        leaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the leaderboard
                mActionsListener.onLeaderBoardClick();
            }
        });

        achievementsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the achievements
                mActionsListener.onAchievementsClick();
            }
        });

        howToPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the how to play tutorial
                mActionsListener.onHowToPlayClick();
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the settings
                mActionsListener.onSettingsClick();
            }
        });

        return root;
    }

    /**
     * Swaps in the Single Player Options Fragment
     */
    @Override
    public void openSinglePlayerOptions() {
        // Swap in the Single Player Menu Fragment
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.menu_frame, MainMenuSinglePlayerFragment.newInstance());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Swaps in the Single Player Options Fragment
     */
    @Override
    public void openMultiplayerOptions() {
        // Swap in the Multiplayer Menu Fragment
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.menu_frame, MainMenuMultiplayerFragment.newInstance());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void openLeaderboard() {
        Log.d(LOG_TAG, "The user opened the leaderboard");

        googleApiClientCallback myActivity = (googleApiClientCallback) getActivity();
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
    public void openAchievements() {
        Log.d(LOG_TAG, "The user opened achievements");

        googleApiClientCallback myActivity = (googleApiClientCallback) getActivity();
        GoogleApiClient myClient = myActivity.getGoogleApiClient();

        if (myClient.isConnected()) {
            startActivityForResult(Games.Achievements.getAchievementsIntent(myClient),
                    RC_UNUSED);
        } else {
            BaseGameUtils.makeSimpleDialog(
                    getActivity(),
                    getString(R.string.achievements_not_available))
                    .show();
        }
    }

    @Override
    public void openSettings() {
        Toast.makeText(
                getContext(),
                "No settings yet!",
                Toast.LENGTH_LONG)
                .show();
    }


    @Override
    public void showHowToPlay() {
        Intent intent = new Intent(getContext(), TutorialActivity.class);
        startActivity(intent);
    }

    /*
     * Interface implemented by the parent activity to grant access to the GoogleApiClient
     */
    public interface googleApiClientCallback {

        GoogleApiClient getGoogleApiClient();

    }

}
