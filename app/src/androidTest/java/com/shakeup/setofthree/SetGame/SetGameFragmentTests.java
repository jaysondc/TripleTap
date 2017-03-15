package com.shakeup.setofthree.SetGame;

import android.content.Intent;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.GridView;

import com.shakeup.setgamelibrary.SetGame;
import com.shakeup.setofthree.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertThat;

/**
 * Created by Jayson on 3/10/2017.
 *
 * Tests to be run on the Set Game UI
 */

public class SetGameFragmentTests{

    // Refrences to the activity and fragment
    private GameFragment mGameFragment;
    private GameActivity mGameActivity;

    // List of possible sets for the current game
    private ArrayList<SetGame.Triplet<Integer, Integer, Integer>> mSetLocations;


    // Specify we need to launch the GameActivity before these tests
    @Rule
    public ActivityTestRule<GameActivity> mGameActivityTestRule =
            new ActivityTestRule<>(
                    GameActivity.class,
                    true /* Initial touch mode  */,
                    false /* Lazily launch activity */);

    @Before
    public void setUpTests(){
        Intent startIntent = new Intent();
        mGameActivityTestRule.launchActivity(startIntent);

        mGameActivity = mGameActivityTestRule.getActivity();
        mGameFragment = (GameFragment) mGameActivity.getSupportFragmentManager()
                .findFragmentById(R.id.content_frame);
    }

    // TESTS

    /**
     * Test that the game is displayed correctly.
     * The grid must contain at least 12 cards.
     */
    @Test
    public void displayGameTest(){
        onView(withId(R.id.game_grid))
                .check(new GridViewItemCountAssertion(12));
    }

    /**
     * Test finding a set of 3 cards
     */
    @Test
    public void findSetTest(){

        // Pick a random valid SET
        SetGame.Triplet location = mGameFragment.getRandomSet();

        int first = (int) location.getFirst();
        int second = (int) location.getSecond();
        int third = (int) location.getThird();

        onData(anything()).inAdapterView(withId(R.id.game_grid))
                .atPosition(first).perform(click());
        onData(anything()).inAdapterView(withId(R.id.game_grid))
                .atPosition(second).perform(click());
        onData(anything()).inAdapterView(withId(R.id.game_grid))
                .atPosition(third).perform(click());

        // Assert that valid set was found
    }

    /**
     * Get a random set location from the possible sets
     * @return Triplet of set indexes
     */
    public SetGame.Triplet<Integer, Integer, Integer> getRandomSet(){
        int index = (int) Math.floor(Math.random() * mSetLocations.size());

        return mSetLocations.get(index);
    }

    /**
     * Custom view assertion method that checks that a GridView's
     * item count is greater or equal to expectedCount
     */
    public class GridViewItemCountAssertion implements ViewAssertion {
        private final int expectedCount;

        public GridViewItemCountAssertion(int expectedCount) {
            this.expectedCount = expectedCount;
        }

        @Override
        public void check(View view, NoMatchingViewException noViewFoundException) {
            if (noViewFoundException != null) {
                throw noViewFoundException;
            }

            GridView gridView = (GridView) view;

            SetGameGridAdapter adapter = (SetGameGridAdapter) gridView.getAdapter();
            assertThat(adapter.getCount(), greaterThanOrEqualTo(expectedCount));
        }
    }
}
