package com.shakeup.setofthree.multiplayergame;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.shakeup.setofthree.FullScreenActivity;
import com.shakeup.setofthree.R;

/**
 * Created by Jayson on 3/2/2017.
 *
 * This doesn't do anything except set up the correct multiplayer
 * fragment which will contain all the views seen by the user.
 */

public class MultiplayerGameActivity extends FullScreenActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        if (null == savedInstanceState) {
            initFragment(MultiplayerGameFragment.newInstance());
        }
    }

    private void initFragment(Fragment fragment) {
        // Add the fragment to the layout
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.content_frame, fragment);
        transaction.commit();
    }

    @Override
    public void onSignInFailed() {
        // Do nothing
    }

    @Override
    public void onSignInSucceeded() {
        // Do nothing
    }
}
