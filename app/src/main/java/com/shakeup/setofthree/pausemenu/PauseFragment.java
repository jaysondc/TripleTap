package com.shakeup.setofthree.pausemenu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shakeup.setofthree.R;
import com.shakeup.setofthree.customviews.FImageButton;

import info.hoang8f.widget.FButton;

/**
 * Created by Jayson Dela Cruz on 8/4/2017.
 * <p>
 * This fragment handles the UI actions for the Pause menu
 */

public class PauseFragment extends android.support.v4.app.DialogFragment implements PauseContract.View {

    FButton mResume, mNewGame;
    FImageButton mMainMenu;
    PauseContract.UserActionsListener mPresenter;

    public static PauseFragment newInstance() {
        return new PauseFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        mPresenter = new PausePresenter(this);

        View root = inflater.inflate(R.layout.fragment_pause, container, false);

        // Grab button references
        mResume = root.findViewById(R.id.button_resume);
        mNewGame = root.findViewById(R.id.button_restart);
        mMainMenu = root.findViewById(R.id.button_main_menu);

        // Assign click listeners
        mResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onResumeClicked();
            }
        });
        mNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onRestartClicked();
            }
        });
        mMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onMainMenuClicked();
            }
        });

        return root;
    }

    /*
     * Send the Restart command back to the calling fragment
     */
    @Override
    public void restartGame() {
        getTargetFragment().onActivityResult(
                getTargetRequestCode(),
                PauseContract.RESULT_RESTART,
                getActivity().getIntent());
        dismiss();
    }

    /*
     * Send the MainMenu command back to the calling fragment
     */
    @Override
    public void openMainMenu() {
        getTargetFragment().onActivityResult(
                getTargetRequestCode(),
                PauseContract.RESULT_MAIN_MENU,
                getActivity().getIntent());
        dismiss();
    }

    /*
     * Send the Resume command back to the calling fragment
     */
    @Override
    public void resumeGame() {
        getTargetFragment().onActivityResult(
                getTargetRequestCode(),
                PauseContract.RESULT_RESUME,
                getActivity().getIntent());
        dismiss();
    }

    @Override
    public void onResume() {

        // Set the size of our dialog manually
        int width = getResources().getDimensionPixelSize(R.dimen.pause_dialog_width);
        int height = getResources().getDimensionPixelSize(R.dimen.pause_dialog_height);
        getDialog().getWindow().setLayout(width, height);

        super.onResume();
    }
}
