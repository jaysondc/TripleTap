package com.shakeup.setofthree.MainMenu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shakeup.setofthree.R;

/**
 * Created by Jayson on 3/2/2017.
 */

public class MainMenuFragment extends android.support.v4.app.Fragment implements MainMenuContract.View {

    public MainMenuFragment() {
        // Requires empty public constructor
    }

    public static MainMenuFragment newInstance() {
        return new MainMenuFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main_menu, container, false);

        return root;
    }

    @Override
    public void showSinglePlayerOptions() {

    }

    @Override
    public void showMultiPlayerOptions() {

    }
}
