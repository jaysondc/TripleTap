package com.shakeup.setofthree.timeattackgame;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.google.android.gms.common.api.GoogleApiClient;
import com.shakeup.setofthree.FullScreenActivity;
import com.shakeup.setofthree.R;
import com.shakeup.setofthree.common.interfaces.GoogleApiClientCallback;

/**
 * Created by Jayson on 3/29/2017.
 * <p>
 * This doesn't do anything except set up the correct TimeAttack
 * fragment which will contain all the views seen by the user.
 */

public class TimeAttackGameActivity
        extends FullScreenActivity
        implements GoogleApiClientCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        if (null == savedInstanceState) {
            initFragment(TimeAttackGameFragment.newInstance());
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
        // Let the user know we aren't signed in and high scores won't be saved (for now)
    }

    @Override
    public void onSignInSucceeded() {
        // Let the user know we're signed in and can save high scores
    }

    /*
     * Allow our fragments to get the API client
     */
    @Override
    public GoogleApiClient getGoogleApiClient() {
        return super.getApiClient();
    }
}
