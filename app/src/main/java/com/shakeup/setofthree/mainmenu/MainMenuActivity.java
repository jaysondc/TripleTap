package com.shakeup.setofthree.mainmenu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.google.android.gms.common.api.GoogleApiClient;
import com.shakeup.setofthree.FullScreenActivity;
import com.shakeup.setofthree.R;
import com.shakeup.setofthree.tutorial.TutorialActivity;

/**
 * This is the activity for the main menu. It doesn't do anything except set up the
 * fragment which will contain all the views seen by the user.
 */

public class MainMenuActivity
        extends FullScreenActivity
        implements View.OnClickListener,
        MainMenuFragment.googleApiClientCallback {

    // request codes we use when invoking an external activity
    private static final int RC_RESOLVE = 5000;
    private static final int RC_UNUSED = 5001;
    private static final int RC_SIGN_IN = 9001;
    private static final int MY_PERMISSIONS_REQUEST_INTERNET = 1;
    boolean mExplicitSignOut = false;
    boolean mInSignInFlow = false; // set to true when you're in the middle of the
    // sign in flow, to know you should not attempt
    // to connect in onStart()
    GoogleApiClient mGoogleApiClient;
    private String LOG_TAG = this.getClass().getSimpleName();
    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInFlow = true;
    private boolean mSignInClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupAnimations();

        setContentView(R.layout.activity_main_menu);

        // Show the user the tutorial if it's their first time launching the app
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted = prefs.getBoolean(getString(R.string.pref_previously_started), false);
        if (!previouslyStarted) {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean(getString(R.string.pref_previously_started), Boolean.TRUE);
            edit.apply();

            Intent intent = new Intent(this, TutorialActivity.class);
            startActivity(intent);
        }

        if (savedInstanceState == null) {
            initFragment(MainMenuFragment.newInstance());
        }

        // Set onClickListeners for the sign in and out buttons
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);


    }

    /**
     * Initialize the fragment showing the menu buttons
     *
     * @param mainMenuFragment Fragment to be displayed in our activity
     */
    private void initFragment(Fragment mainMenuFragment) {
        // Add the fragment to the layout
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.menu_frame, mainMenuFragment);
        transaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Enable auto-sign in on start
        if (!mInSignInFlow && !mExplicitSignOut) {
            // auto sign in
            beginUserInitiatedSignIn();
        }
    }


    /*
     * Handle clicks of the sign in/out buttons
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.sign_in_button) {
            // start the asynchronous sign in flow
            mSignInClicked = true;
            beginUserInitiatedSignIn();

        } else if (view.getId() == R.id.sign_out_button) {
            mExplicitSignOut = true;
            // sign out.
            mSignInClicked = false;
            if (isSignedIn()) {
                signOut();
            }

            // show sign-in button, hide the sign-out button
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_button).setVisibility(View.GONE);
        }
    }

    @Override
    public void onSignInFailed() {
        Log.d(LOG_TAG, "User sign-in failed.");
        // Put code here to display the sign-in button
        findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
        findViewById(R.id.sign_out_button).setVisibility(View.GONE);
    }

    @Override
    public void onSignInSucceeded() {
        Log.d(LOG_TAG, "User sign-in succeeded!");
        // show sign-out button, hide the sign-in button
        findViewById(R.id.sign_in_button).setVisibility(View.GONE);
        findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
    }

    /*
     * Callback methods used by fragments to obtain the API client
     */
    @Override
    public GoogleApiClient getGoogleApiClient() {
        return getApiClient();
    }

    /**
     * Create and set animations for activity transition
     */
    private void setupAnimations() {

        Transition activityTransition = android.transition.TransitionInflater
                .from(this)
                .inflateTransition(R.transition.main_menu_exit_transition);
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setEnterTransition(activityTransition);
        getWindow().setReturnTransition(activityTransition);
        getWindow().setExitTransition(activityTransition);
        getWindow().setReenterTransition(activityTransition);
    }
}
