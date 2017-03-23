package com.shakeup.setofthree.SetGame;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.shakeup.setofthree.FullScreenActivity;
import com.shakeup.setofthree.R;

/**
 * Created by Jayson on 3/2/2017.
 *
 * This doesn't do anything except set up the
 * fragment which will contain all the views seen by the user.
 */

public class GameActivity extends FullScreenActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        if (null == savedInstanceState) {
            //initFragment(GameFragment.newInstance());
        }
    }

    private void initFragment(Fragment fragment) {
        // Add the to the layout
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.content_frame, fragment);
        transaction.commit();
    }

}
