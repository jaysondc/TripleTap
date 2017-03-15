package com.shakeup.setofthree.SetGame;

import com.shakeup.setgamelibrary.SetGame;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;

/**
 * Created by Jayson on 3/10/2017.
 *
 * Contains all the unit tests for the GamePresenter class
 */

public class SetGamePresenterTests {

    private GamePresenter mGamePresenter;


    // Boilerplate mockito setup
    @Mock
    private GameContract.View  mGameView;

    @Mock
    private SetGame mSetGame;

    @Before
    public void setUpGamePresenter(){
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Create instance of presenter to test
        mGamePresenter = new GamePresenter(mGameView);

        // Create set game to use in tests
        mSetGame = Mockito.mock(SetGame.class);

        // Make presenter use our mock game
        mGamePresenter.setSetGame(mSetGame);
    }

    // TESTS

    @Test
    public void initGameTest(){
        mGamePresenter.initGame();

        ArgumentCaptor argument = ArgumentCaptor.forClass(ArrayList.class);
        // intercept the created game and chekc that it's passed to mGameView
        verify(mGameView).displayGame((ArrayList) argument.capture());
    }

    @Test
    public void submitSetTest(){
        List<SetGame.Triplet<Integer, Integer, Integer>> setLocations =
                mSetGame.getLocationOfSets();

        // Specify the indexes of a valid set
        int first = 0;
        int second = 1;
        int third = 2;

        // Specify a mocked return for valid set
        Mockito.doReturn(true).when(mSetGame).claimSet(
                first,
                second,
                third
        );

        // Specify a mocked return for invalid set
        Mockito.doReturn(false).when(mSetGame).claimSet(
                third,
                second,
                first
        );

        // Submit the SET expecting true
        mGamePresenter.submitSet(
                first,
                second,
                third
        );

        // Verify the success method was called
        ArgumentCaptor argument = ArgumentCaptor.forClass(ArrayList.class);
        verify(mGameView).claimSetSuccess((ArrayList) argument.capture());


        // Submit the set expectinng false
        mGamePresenter.submitSet(
                third,
                second,
                first
        );

        // Verify the failure method was called
        verify(mGameView).claimSetFailure();

    }

}
