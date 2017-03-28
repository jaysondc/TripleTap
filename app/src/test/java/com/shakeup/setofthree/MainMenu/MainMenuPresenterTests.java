package com.shakeup.setofthree.MainMenu;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Created by Jayson on 3/2/2017.
 *
 * This class contains unit tests for the MainMenuPresenter class
 */

public class MainMenuPresenterTests {

    private MainMenuPresenter mMainMenuPresenter;

    @Mock
    private MainMenuContract.MainView mMainMenuView;


    @Before
    public void setUpMainMenuPresenter(){
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        mMainMenuPresenter = new MainMenuPresenter(mMainMenuView);
    }
}
