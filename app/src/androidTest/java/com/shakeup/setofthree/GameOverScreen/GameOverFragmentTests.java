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

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

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
                    true /* Lazily launch activity */);
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

        // Wait for the fragment to switch before continuing.
        // Espresso doesn't automatically wait because fragment
        // transactions are asynchronous
        Thread.sleep(500);

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

    // Check that the game has successfully restarted
    @Test
    public void restartGame() throws Exception {
        mGameOverFragment.restartGame();

        Thread.sleep(500);

        onView(withId(R.id.game_recycler_grid))
                .check(matches(isDisplayed()));
    }

    // Test opening the leaderboard
    @Test
    public void openLeaderboard() throws Exception {
        mGameOverFragment.openLeaderboard();
        // No assertions here because the leaderboard UI is pre-built
    }

    @Test
    public void openFoundSets() throws Exception {

    }

    @Test
    public void openMainMenu() throws Exception {
        mGameOverFragment.openMainMenu();

        Thread.sleep(500);

        onView(withId(R.id.button_single_player))
                .check(matches(isDisplayed()));
    }

}