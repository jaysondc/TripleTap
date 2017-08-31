package com.shakeup.setofthree.setgame;

import android.support.annotation.StringRes;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shakeup.setgamelibrary.SetGame;
import com.shakeup.setofthree.FullScreenActivity;
import com.shakeup.setofthree.R;
import com.shakeup.setofthree.adapters.SetGameRecyclerAdapter;
import com.shakeup.setofthree.common.customviews.SetGameCardView;

import org.junit.Test;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo;
import static org.junit.Assert.assertThat;

/**
 * Created by Jayson on 3/10/2017.
 * <p>
 * Tests to be run on a generic set game. This is to be subclassed by
 * specific game modes to test game mode specific UI features.
 * <p>
 * Specific @Before methods which get the member references should be implemented
 * by any subclass.
 */

public abstract class SetGameFragmentAndroidTests {
    // References to the activity and fragment
    protected GameFragment mGameFragment;
    protected FullScreenActivity mGameActivity;
    protected GamePresenter mGamePresenter;
    protected SetGame mSetGame;
    // List of possible sets for the current game
    protected ArrayList<SetGame.Triplet> mSetLocations;

    // Subclasses need to set their own @Before methods and set these references
    public abstract void setUpTestEnvironment();

    // TESTS

    /**
     * Test that the game is displayed correctly.
     * The grid must contain at least 12 cards.
     */
    @Test
    public void displayGameTest() {
        onView(withId(R.id.game_recycler_grid))
                .check(new RecyclerViewItemCountAssertion(12));
    }

    /**
     * Click a random set on the board
     */
    @Test
    public void clickRandomSet() {

        // Pick a random valid SET
        SetGame.Triplet location = mSetGame.getRandomSet();
        if (location != null) {
            int first = (int) location.getFirst();
            int second = (int) location.getSecond();
            int third = (int) location.getThird();

            onView(withId(R.id.game_recycler_grid))
                    .perform(RecyclerViewActions
                            .actionOnItemAtPosition(first, click()));
            onView(withId(R.id.game_recycler_grid))
                    .perform(RecyclerViewActions
                            .actionOnItemAtPosition(second, click()));
            onView(withId(R.id.game_recycler_grid))
                    .perform(RecyclerViewActions
                            .actionOnItemAtPosition(third, click()));
        }

    }

    /**
     * Test that 3 cards are highlighted to indicate a set is available
     */
    @Test
    public void highlightSetTest() {
        // Get a reference to the grid
        RecyclerView mRecycleGridView =
                (RecyclerView) mGameActivity.findViewById(R.id.game_recycler_grid);

        int highlightedCellCount = 0;

        for (int i = 0; i < mRecycleGridView.getChildCount(); i++) {
            if (((SetGameCardView) mRecycleGridView.getChildAt(i)).isHighlighted())
                highlightedCellCount++;
        }
        assertEquals(3, highlightedCellCount);
    }

    /**
     * Test finding multiple sets in succession. This test will never finish a game
     * as long as i < 23.
     */
    @Test
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
    public void testGameOverHandler() {
        while (!mSetGame.getIsGameOver()) {
            clickRandomSet();
        }

        // Assert that the gameover message was shown
        checkSnackBarDisplayedByMessage(R.string.message_game_over);
    }


    /**
     * Get a random set location from the possible sets
     *
     * @return Triplet of set indexes
     */
    public SetGame.Triplet getRandomSet() {
        int index = (int) Math.floor(Math.random() * mSetLocations.size());

        return mSetLocations.get(index);
    }

    /**
     * Utility used to check snackbar with text is shown.
     * Gotten from top answer here:
     * http://stackoverflow.com/questions/35188183/snackbar-and-espresso-failing-sometimes
     *
     * @param message StringRes of the text that should be in the SnackBara
     */
    public void checkSnackBarDisplayedByMessage(@StringRes int message) {
        onView(withText(message))
                .check(matches(withEffectiveVisibility(
                        ViewMatchers.Visibility.VISIBLE
                )));
    }

    /**
     * Custom view assertion method that checks that a GridView's
     * item count is greater or equal to expectedCount
     */
    public class RecyclerViewItemCountAssertion implements ViewAssertion {
        private final int expectedCount;

        public RecyclerViewItemCountAssertion(int expectedCount) {
            this.expectedCount = expectedCount;
        }

        @Override
        public void check(View view, NoMatchingViewException noViewFoundException) {
            if (noViewFoundException != null) {
                throw noViewFoundException;
            }

            RecyclerView recyclerView = (RecyclerView) view;

            SetGameRecyclerAdapter adapter = (SetGameRecyclerAdapter) recyclerView.getAdapter();
            assertThat(adapter.getItemCount(), greaterThanOrEqualTo(expectedCount));
        }
    }
}
