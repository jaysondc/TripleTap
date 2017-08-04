package com.shakeup.setofthree.normalgame;

import android.content.Intent;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.shakeup.setgamelibrary.SetCard;
import com.shakeup.setgamelibrary.SetGame;
import com.shakeup.setofthree.R;
import com.shakeup.setofthree.adapters.SetGameRecyclerAdapter;
import com.shakeup.setofthree.setgame.SetGameFragmentAndroidTests;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Jayson on 3/10/2017.
 * <p>
 * Tests to be run on the Set Game Multiplayer UI
 */

public class NormalGameFragmentAndroidTests extends SetGameFragmentAndroidTests {


    // Specify we need to launch the NormalGameActivity before these tests
    @Rule
    public ActivityTestRule<NormalGameActivity> mGameActivityTestRule =
            new ActivityTestRule<>(
                    NormalGameActivity.class,
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
        mGameActivity = mGameActivityTestRule.getActivity();
        mGameFragment =
                (NormalGameFragment) mGameActivity.getSupportFragmentManager()
                        .findFragmentById(R.id.content_frame);
        mGamePresenter =
                (NormalGamePresenter) mGameFragment.getActionsListener();
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

            highlightSetTest();
            clickRandomSet();

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

    /**
     * Test edge case where the player completely clears the board
     */
    @Test
    public void testClearBoardGameOver() throws InterruptedException {

        // Get the deck and empty it
        SetGame.SetDeck deck = mSetGame.getSetDeck();
        while (!deck.isEmpty()) {
            deck.drawCard();
        }
        // Use the empty deck
        mSetGame.setSetDeck(deck);

        // Get a random set
        ArrayList<SetCard> someHand = mSetGame.getSetHand();
        SetGame.Triplet someSetLocation = mSetGame.getRandomSet();
        SetCard cardOne = someHand.get(someSetLocation.getFirst());
        SetCard cardTwo = someHand.get(someSetLocation.getSecond());
        SetCard cardThree = someHand.get(someSetLocation.getThird());

        // Empty the hand and populate it with a single set
        while (!someHand.isEmpty()) {
            someHand.remove(0);
        }
        someHand.add(cardOne);
        someHand.add(cardTwo);
        someHand.add(cardThree);

        // Put the new hand in our game
        mSetGame.setSetHand(someHand);
        mSetGame.analyzeSets();
        // Put the new hand in our board adapter
        SetGameRecyclerAdapter myAdapter =
                (SetGameRecyclerAdapter) mGameFragment.getRecyclerGridView().getAdapter();
        // Set the adapter to be empty temporarily to clear any old view holders
        myAdapter.setSethand(new ArrayList<SetCard>());
        // Click the refresh button
        onView(withId(R.id.button_debug_refresh))
                .perform(click());

        myAdapter.setSethand(someHand);
        onView(withId(R.id.button_debug_refresh))
                .perform(click());

        //Thread.sleep(20000);
        clickRandomSet();
    }

    /**
     * Tests uploading and reading scores locally
     */
    @Test
    public void testSaveLocalScore() {
        NormalGameFragment normalFragment = (NormalGameFragment) mGameFragment;
        normalFragment.saveLocalScore(1500000, true);

        normalFragment.showGameOver();

        // Bad practice but set a Sleep command so we an mess with the UI ourselves
        try {
            Thread.sleep(50000);
        } catch (Exception e) {

        }
    }

    /**
     * Test pause menu brings up PauseFragment dialog
     */
    @Test
    public void testPauseMenu() {
        onView(withId(R.id.button_pause))
                .perform(click());

        onView(withId(R.id.pause_layout))
                .check(matches(isDisplayed()));
    }
}

