package com.shakeup.setofthree.MainMenu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.shakeup.setofthree.FullScreenActivity;
import com.shakeup.setofthree.R;

/**
 * This is the activity for the main menu. It doesn't do anything except set up the
 * fragment which will contain all the views seen by the user.
 */

public class MainMenuActivity extends FullScreenActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        if (null == savedInstanceState) {
            initFragment(MainMenuFragment.newInstance());
        }
    }

    private void initFragment(Fragment mainMenuFragment) {
        // Add the NotesFragment to the layout
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.content_frame, mainMenuFragment);
        transaction.commit();
    }
}
