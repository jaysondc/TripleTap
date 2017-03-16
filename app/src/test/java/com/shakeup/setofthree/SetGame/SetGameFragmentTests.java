package com.shakeup.setofthree.SetGame;

import com.shakeup.setgamelibrary.SetGame;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by Jayson on 3/16/2017.
 *
 * Contains all the unit tests for the GameFragment class
 */

@RunWith(MockitoJUnitRunner.class)
public class SetGameFragmentTests {

    private GameFragment mGameView;

    // Boilerplate mockito setup
    @Mock
    private GameContract.UserActionsListener  mGamePresenter;

    @Mock
    private SetGame mSetGame;

    @Before
    public void setUpGameFragment(){
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Create instance of presenter to test
        mGameView = new GameFragment();

        // Create set game to use in tests
        mSetGame = Mockito.mock(SetGame.class);

        // Make presenter use our mock game
        mGamePresenter.setSetGame(mSetGame);
    }

}
