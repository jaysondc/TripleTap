package com.shakeup.setofthree.TimeAttackGame;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import com.shakeup.setofthree.R;
import com.shakeup.setofthree.SetGame.SetGameFragmentAndroidTests;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Created by Jayson on 4/14/2017.
 */
public class TimeAttackGameFragmentTests extends SetGameFragmentAndroidTests{

    // Specify we need to launch the TimeAttackActivity before these tests
    @Rule
    public ActivityTestRule<TimeAttackGameActivity> mGameActivityTestRule =
            new ActivityTestRule<>(
                    TimeAttackGameActivity.class,
                    true /* Initial touch mode  */,
                    false /* Lazily launch activity */);

    @Before
    public void setUpTestEnvironment(){
        Intent startIntent = new Intent();
        mGameActivityTestRule.launchActivity(startIntent);

        // Grab references to all active objects for testing
        mGameActivity = mGameActivityTestRule.getActivity();
        mGameFragment =
                (TimeAttackGameFragment) mGameActivity.getSupportFragmentManager()
                        .findFragmentById(R.id.content_frame);
        mGamePresenter =
                (TimeAttackGamePresenter) mGameFragment.getActionsListener();
        mSetGame = mGamePresenter.getSetGame();
    }

    @After
    public void tearDown() throws Exception {
        mGameActivity.finish();
    }

    @Test
    public void testGameOverHandler(){
        mGameFragment.showGameOver();

        try{
            Thread.sleep(50000);
        } catch (Exception e){
            // do nothing
        }
    }

    @Test
    public void saveLocalScoreTest(){

    }

}