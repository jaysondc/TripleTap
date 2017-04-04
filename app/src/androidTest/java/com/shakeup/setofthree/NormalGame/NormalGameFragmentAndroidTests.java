package com.shakeup.setofthree.NormalGame;

import android.content.Intent;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.shakeup.setofthree.R;
import com.shakeup.setofthree.SetGame.SetGameFragmentAndroidTests;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;

/**
 * Created by Jayson on 3/10/2017.
 *
 * Tests to be run on the Set Game Multiplayer UI
 */

public class NormalGameFragmentAndroidTests extends SetGameFragmentAndroidTests{


    // Specify we need to launch the NormalGameActivity before these tests
    @Rule
    public ActivityTestRule<NormalGameActivity> mGameActivityTestRule =
            new ActivityTestRule<>(
                    NormalGameActivity.class,
                    true /* Initial touch mode  */,
                    false /* Lazily launch activity */);

    @Before
    public void setUpTestEnvironment(){
        Intent startIntent = new Intent();
        mGameActivityTestRule.launchActivity(startIntent);

        // Grab references to all active objects for testing
        mGameActivity = (NormalGameActivity) mGameActivityTestRule.getActivity();
        mGameFragment =
                (NormalGameFragment) mGameActivity.getSupportFragmentManager()
                .findFragmentById(R.id.content_frame);
        mGamePresenter =
                (NormalGamePresenter) mGameFragment.getActionsListener();
        mSetGame = mGamePresenter.getSetGame();
    }

    // TESTS

    /**
     * Test that the game is displayed correctly.
     * The grid must contain at least 12 cards.
     */
    @Test
    @Override
    public void displayGameTest(){
        super.displayGameTest();
    }

    /**
     * Click a random set on the board
     */
    @Test
    @Override
    public void clickRandomSet(){
        // Click our player button first to enable the board


        super.clickRandomSet();
    }

    /**
     * Test finding multiple sets in succession. This test will never finish a game
     * as long as i < 23.
     */
    @Test
    @Override
    public void findMultipleSetsTest(){
        for (int i = 0; i < 23; i++){

            highlightSetTest();
            clickRandomSet();

        }
    }

    /**
     * Tests that the onGameOver method properly handles the GameOver UI state.
     */
    @Test
    @Override
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

