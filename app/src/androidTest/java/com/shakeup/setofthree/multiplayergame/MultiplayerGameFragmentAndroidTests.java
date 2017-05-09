package com.shakeup.setofthree.setgame;

import android.content.Intent;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.shakeup.setofthree.R;
import com.shakeup.setofthree.multiplayergame.MultiplayerGameActivity;
import com.shakeup.setofthree.multiplayergame.MultiplayerGameFragment;
import com.shakeup.setofthree.multiplayergame.MultiplayerGamePresenter;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Jayson on 3/10/2017.
 * <p>
 * Tests to be run on the Set Game Multiplayer UI
 */

public class MultiplayerGameFragmentAndroidTests extends SetGameFragmentAndroidTests {


    // Specify we need to launch the MultiplayerGameActivity before these tests
    @Rule
    public ActivityTestRule<MultiplayerGameActivity> mGameActivityTestRule =
            new ActivityTestRule<>(
                    MultiplayerGameActivity.class,
                    true /* Initial touch mode  */,
                    false /* Lazily launch activity */);
    /*
     * Create our own explicit click action that overrides the "90% of the view must be visible"
     * error. Our buttons are rotated and as a result the bounds of the view extend past the sides
     * of the screen, preventing us from clicking them.
     *
     * StackoverFlow answer was here: http://stackoverflow.com/a/37994771
     */
    ViewAction clickExplicit = new ViewAction() {
        @Override
        public Matcher<View> getConstraints() {
            return isEnabled(); // no constraints, they are checked above
        }

        @Override
        public String getDescription() {
            return "click plus button";
        }

        @Override
        public void perform(UiController uiController, View view) {
            view.performClick();
        }
    };

    // TESTS

    @Before
    public void setUpTestEnvironment() {
        Intent startIntent = new Intent();
        mGameActivityTestRule.launchActivity(startIntent);

        // Grab references to all active objects for testing
        mGameActivity = (MultiplayerGameActivity) mGameActivityTestRule.getActivity();
        mGameFragment =
                (MultiplayerGameFragment) mGameActivity.getSupportFragmentManager()
                        .findFragmentById(R.id.content_frame);
        mGamePresenter =
                (MultiplayerGamePresenter) mGameFragment.getActionsListener();
        mSetGame = mGamePresenter.getSetGame();
    }

    /**
     * Test that the game is displayed correctly.
     * The grid must contain at least 12 cards.
     */
    @Test
    @Override
    public void displayGameTest() {
        super.displayGameTest();
    }

    /**
     * Click a random set on the board
     */
    @Test
    @Override
    public void clickRandomSet() {
        // Click our player button first to enable the board
        onView(withId(R.id.button_player_one)).perform(clickExplicit);

        super.clickRandomSet();
    }

    /**
     * Test finding multiple sets in succession. This test will never finish a game
     * as long as i < 23.
     */
    @Test
    @Override
    public void findMultipleSetsTest() {
        for (int i = 0; i < 23; i++) {
            // Click our player button first to enable the board
            onView(withId(R.id.button_player_one))
                    .perform(clickExplicit);
            highlightSetTest();
            clickRandomSet();

//            onView(withId(R.id.button_player_one))
//                    .check(matches(withText(R.string.button_found_set)));
        }
    }

    /**
     * Tests that the showGameOver method properly handles the GameOver UI state.
     */
    @Test
    @Override
    public void testGameOverHandler() {
        while (!mSetGame.getIsGameOver()) {
            clickRandomSet();
        }

        // Assert that the gameover message was shown
        checkSnackBarDisplayedByMessage(R.string.message_game_over);
    }
}

