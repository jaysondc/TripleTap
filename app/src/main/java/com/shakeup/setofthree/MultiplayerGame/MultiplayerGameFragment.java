package com.shakeup.setofthree.MultiplayerGame;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shakeup.setofthree.R;
import com.shakeup.setofthree.SetGame.GameFragment;
import com.shakeup.setofthree.SetGame.GamePresenter;

/**
 * Created by Jayson on 3/20/2017.
 */

public class MultiplayerGameFragment extends GameFragment {

    // Default constructor
    public MultiplayerGameFragment(){
    }

    public static MultiplayerGameFragment newInstance(){
        return new MultiplayerGameFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Get the number of desired players as an extra.
        int numPlayers = getActivity().getIntent()
                .getIntExtra(getString(R.string.extra_num_players), 2);

        View root;

        switch( numPlayers ){
            case 2:
                root = inflater.inflate(R.layout.fragment_game_multiplayer_two, container, false);
                break;
            case 3:
                root = inflater.inflate(R.layout.fragment_game_multiplayer_three, container, false);
                break;
            case 4:
                root = inflater.inflate(R.layout.fragment_game_multiplayer_four, container, false);
                break;
            default: // Default to 2 players if somehow it isn't specified
                root = inflater.inflate(R.layout.fragment_game_multiplayer_two, container, false);
                break;
        }

        // Instance the presenter our fragment uses
        setActionsListener(new GamePresenter(this));

        // Grab handlers for UI elements
        setRecyclerGridView((RecyclerView) root.findViewById(R.id.game_recycler_grid));

        // Initialize a game
        getActionsListener().initGame();

        return root;
    }
}
