package com.shakeup.setofthree.MainMenu;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

/**
 * Created by Jayson on 3/2/2017.
 *
 * This class contains unit tests for the MainMenuPresenter class
 */

public class MainMenuPresenterTests {

    private MainMenuPresenter mMainMenuPresenter;

    @Mock
    private MainMenuContract.View mMainMenuView;


    @Before
    public void setUpMainMenuPresenter(){
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        mMainMenuPresenter = new MainMenuPresenter(mMainMenuView);
    }

    /**
     * Verify the presenter handles the StartSinglePlayerNormal UI Command
     */
    @Test
    public void clickSinglePlayerNormal_StartSinglePlayerNormal(){
        mMainMenuPresenter.startSinglePlayerNormal();
        verify(mMainMenuView).openSinglePlayerNormal();
    }

    @Test
    public void clickMultiPlayer_StartMultiPlayer(){
        mMainMenuPresenter.startMultiPlayer(2);
        verify(mMainMenuView).openMultiPlayer(2);
    }
}