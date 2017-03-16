package com.shakeup.setofthree.SetGame;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;

import com.shakeup.setofthree.CustomView.SetGameCardView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Jayson on 3/16/2017.
 *
 * Contains all the unit tests for the GameFragment class
 */

@RunWith(MockitoJUnitRunner.class)
public class SetGameFragmentTests {

    @Mock
    private GamePresenter  mActionsListener;

    @Mock
    private RecyclerView mRecyclerGridView;

    @Mock
    private SetGameCardView mSetGameCardViewChecked;
    @Mock
    private SetGameCardView mSetGameCardViewUnchecked;

    @Mock SparseBooleanArray checkedLocations;

    // Create instance of game fragment to test and inject mock RecyclerGridView
    @InjectMocks
    private GameFragment mGameView = new GameFragment();

    @Before
    public void setUpGameFragment(){
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Set up RecyclerView Mock
        // Checked locations are 2, 4, and 6.
        Mockito.doReturn(12).when(mRecyclerGridView).getChildCount();
        for( int i = 0; i < 12; i++ ){
            if( i == 2 || i == 4 || i == 6 ){
                Mockito.when(mRecyclerGridView.getChildAt(i)).thenReturn(mSetGameCardViewChecked);
            } else {
                Mockito.when(mRecyclerGridView.getChildAt(i)).thenReturn(mSetGameCardViewUnchecked);
            }
        }

        // Set up mSetGameCardView mocks to return their checked states
        Mockito.when(mSetGameCardViewChecked.isChecked()).thenReturn(true);
        Mockito.when(mSetGameCardViewUnchecked.isChecked()).thenReturn(false);

    }

    /**
     * Test that we properly we can properly count the number of checked items in RecyclerView
     */
    @Test
    public void recyclerViewCheckedItemsTest(){
        int checkedItemCount = mGameView.getCheckedItemCount();
        assertEquals(3, checkedItemCount);
    }

    /**
     * Test that we properly we can properly get the location of checked items in RecyclerView
     * This test doesn't work yet because I don't know how to mock SparseBooleanArray.
     */
//    @Test
//    public void recyclerViewCheckedItemsPositionsTest(){
//        SparseBooleanArray checkedItemLocations = mGameView.getCheckedItemPositions();
//
//        assertEquals(true, checkedItemLocations.get(2));
//        assertEquals(true, checkedItemLocations.get(4));
//        assertEquals(true, checkedItemLocations.get(6));
//    }

}
