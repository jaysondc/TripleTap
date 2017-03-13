package com.shakeup.setofthree.SetGame;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

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

    @Before
    public void setUpGamePresenter(){
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        mGamePresenter = new GamePresenter(mGameView);
    }

    // TESTS

    @Test
    public void initGameTest(){
        mGamePresenter.initGame();

        ArgumentCaptor argument = ArgumentCaptor.forClass(ArrayList.class);
        // intercept the created game and chekc that it's passed to mGameView
        verify(mGameView).displayGame((ArrayList)argument.capture());
    }

}
