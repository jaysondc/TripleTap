package com.shakeup.setofthree.NormalGame;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.shakeup.setofthree.FullScreenActivity;
import com.shakeup.setofthree.Interfaces.GoogleApiClientCallback;
import com.shakeup.setofthree.R;

/**
 * Created by Jayson on 3/29/2017.
 *
 * This doesn't do anything except set up the correct TimeAttack
 * fragment which will contain all the views seen by the user.
 */

public class NormalGameActivity
        extends FullScreenActivity
        implements GoogleApiClientCallback{

    final String LOG_TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        if (null == savedInstanceState) {
            initFragment(NormalGameFragment.newInstance());
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
        Log.d(LOG_TAG, "Google Play Games sign in failed!");
    }

    @Override
    public void onSignInSucceeded() {
        // Let the user know we're signed in and can save high scores
        Log.d(LOG_TAG, "Signed into Google Play Games!");
    }

    /*
     * Allow our fragments to get the API client
     */
    @Override
    public GoogleApiClient getGoogleApiClient() {
        return super.getApiClient();
    }
}
