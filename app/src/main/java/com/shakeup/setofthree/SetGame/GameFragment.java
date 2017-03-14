package com.shakeup.setofthree.SetGame;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.shakeup.setgamelibrary.SetCard;
import com.shakeup.setofthree.CustomView.SetGameCard;
import com.shakeup.setofthree.R;

import java.util.ArrayList;

/**
 * Created by Jayson on 3/2/2017.
 */


public class GameFragment extends Fragment implements GameContract.View {

    // Listener for presenter to handle all user input
    private GamePresenter mActionsListener;

    // GridView displaying the game board
    private GridView mGridView;

    // Adapter containing the current Set Hand displayed on the board
    private SetGameGridAdapter mGridAdapter;

    /**
     * Allow another class to construct us
     */
    public GameFragment() {
        // Requires empty public constructor
    }
    public static GameFragment newInstance() {
        return new GameFragment();
    }

    // Run initial setup for creating a new game
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_game, container, false);

        // Instance the presenter our fragment uses
        mActionsListener = new GamePresenter(this);

        // Grab handlers for UI elements
        mGridView = (GridView) root.findViewById(R.id.game_grid);

        // Initialize a game
        mActionsListener.initGame();

        return root;
    }


    /**
     * Receives the generated Set Hand and displays it to the game grid
     * @param setCards ArrayList of SetCards to be displayed in the grid
     */
    @Override
    public void displayGame(ArrayList<SetCard> setCards) {
        // Initialize a new adapter with the Set Hand
        mGridAdapter = new SetGameGridAdapter(setCards);

        // Display the board
        mGridView.setAdapter(mGridAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SetGameCard card = (SetGameCard) view;
                card.toggle();
            }
        });
    }

}
