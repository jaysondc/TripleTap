package com.shakeup.setofthree.pausemenu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

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

    @Override
    public void onStart() {
        super.onStart();

        // Allow the SystemUI to interact with this fragment again
        Window window = getDialog().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        // http://vardhan-justlikethat.blogspot.com/2014/06/android-immersive-mode-for-dialog.html
        // FLAG_NOT_FOCUSABLE prevents the dialog from breaking immersive mode, clear this flag
        // on onStart to interact with it normally.
        Window window = getDialog().getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        );
        window.addFlags(
                WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        // Set our presenter and layout
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
