package com.shakeup.setofthree.SetGame;

import android.content.Intent;
import android.support.annotation.StringRes;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shakeup.setgamelibrary.SetGame;
import com.shakeup.setofthree.MultiplayerGame.MultiplayerGameActivity;
import com.shakeup.setofthree.MultiplayerGame.MultiplayerGameFragment;
import com.shakeup.setofthree.MultiplayerGame.MultiplayerGamePresenter;
import com.shakeup.setofthree.R;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertThat;

/**
 * Created by Jayson on 3/10/2017.
 *
 * Tests to be run on the Set Game Multiplayer UI
 */

public class MultiplayerGameFragmentAndroidTests extends SetGameFragmentAndroidTests{


    // Specify we need to launch the MultiplayerGameActivity before these tests
    @Rule
    public ActivityTestRule<MultiplayerGameActivity> mGameActivityTestRule =
            new ActivityTestRule<>(
                    MultiplayerGameActivity.class,
                    true /* Initial touch mode  */,
                    false /* Lazily launch activity */);

    @Before
    public void setUpTestEnvironment(){
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

    // TESTS

    /**
     * Test that the game is displayed correctly.
     * The grid must contain at least 12 cards.
     */
    @Test
    public void displayGameTest(){
        super.displayGameTest();
    }

    /**
     * Click a random set on the board
     */
    @Test
    public void clickRandomSet(){
        // Click our player button first to enable the board
        onView(withId(R.id.button_player_one)).perform(clickExplicit);

        super.clickRandomSet();
    }

    /**
     * Test that 3 cards are highlighted to indicate a set is available
     */
    @Test
    public void highlightSetTest(){
        super.highlightSetTest();
    }

    /**
     * Test finding multiple sets in succession. This test will never finish a game
     * as long as i < 23.
     */
    @Test
    public void findMultipleSetsTest(){
        for (int i = 0; i < 23; i++){
            // Click our player button first to enable the board
            onView(withId(R.id.button_player_one)).perform(clickExplicit);
            highlightSetTest();
            clickRandomSet();

            checkSnackBarDisplayedByMessage(R.string.message_found_set);
        }
    }

    /**
     * Tests that the onGameOver method properly handles the GameOver UI state.
     */
    @Test
    public void testGameOverHandler(){
        while( !mSetGame.getIsGameOver() ){
            clickRandomSet();
        }

        // Assert that the gameover message was shown
        checkSnackBarDisplayedByMessage(R.string.message_game_over);
    }

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
}

