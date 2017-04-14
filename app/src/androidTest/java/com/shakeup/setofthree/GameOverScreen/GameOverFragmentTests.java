package com.shakeup.setofthree.GameOverScreen;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import com.shakeup.setofthree.NormalGame.NormalGameActivity;
import com.shakeup.setofthree.NormalGame.NormalGameFragment;
import com.shakeup.setofthree.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Created by Jayson on 4/14/2017.
 */
public class GameOverFragmentTests {

    NormalGameActivity mGameActivity;
    NormalGameFragment mGameFragment;
    GameOverFragment mGameOverFragment;
    GameOverPresenter mGameOverPresenter;

    // Specify we need to launch the NormalGameActivity before these tests
    @Rule
    public ActivityTestRule<NormalGameActivity> mGameActivityTestRule =
            new ActivityTestRule<>(
                    NormalGameActivity.class,
                    true /* Initial touch mode  */,
                    false /* Lazily launch activity */);
    @Before
    public void setUp() throws Exception {
        Intent startIntent = new Intent();
        mGameActivityTestRule.launchActivity(startIntent);

        // Grab references to all active objects for testing
        mGameActivity =
                (NormalGameActivity) mGameActivityTestRule.getActivity();
        mGameFragment =
                (NormalGameFragment) mGameActivity.getSupportFragmentManager()
                        .findFragmentById(R.id.content_frame);

        // Launch the GameOver fragment
        mGameFragment.showGameOver();

        Thread.sleep(1500);

        // Get the Game Over Fragment
        mGameOverFragment =
                (GameOverFragment) mGameActivity.getSupportFragmentManager()
                .findFragmentById(R.id.content_frame);

        // Get the Game Over Actions Listener
        mGameOverPresenter =
                (GameOverPresenter) mGameOverFragment.getActionsListener();
    }

    @After
    public void tearDown() throws Exception {
        mGameActivity.finish();
    }

    @Test
    public void restartGame() throws Exception {

    }

    @Test
    public void openLeaderboard() throws Exception {
        mGameOverFragment.openLeaderboard();
    }

    @Test
    public void openFoundSets() throws Exception {

    }

    @Test
    public void openMainMenu() throws Exception {

    }

}