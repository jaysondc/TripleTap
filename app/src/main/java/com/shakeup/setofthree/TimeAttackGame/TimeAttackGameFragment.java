package com.shakeup.setofthree.TimeAttackGame;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shakeup.setofthree.R;
import com.shakeup.setofthree.SetGame.GameFragment;

/**
 * Created by Jayson on 3/20/2017.
 *
 * This Fragment handles the UI for the Time Attack game mode
 */


public class TimeAttackGameFragment
        extends GameFragment
        implements TimeAttackGameContract.View {

    public final String LOG_TAG = this.getClass().getSimpleName();

    // Reference to our presenter
    TimeAttackGameContract.UserActionsListener mTimeAttackActionsListener;

    // Length for our Time Attack mode
    long mTimeAttackLength;


    // Default constructor
    public TimeAttackGameFragment(){
    }

    public static TimeAttackGameFragment newInstance(){
        return new TimeAttackGameFragment();
    }

    /*
     * Each game mode needs to set up its own onCreateView and assign the following
     * member variables of the superclass
     * mActionsListener a reference to our presenter
     * mRecyclerGridView a reference to the RecyclerView for the game
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Get the length of our time attack mode as an extra
        long mTimeAttackLength = getActivity().getIntent()
                .getLongExtra(getString(R.string.extra_time_attack_length), 60000);

        View root;

        root = inflater.inflate(
                R.layout.fragment_game_time_attack, container, false);

        // Instance the presenter our fragment uses and grab a reference
        mTimeAttackActionsListener = new TimeAttackGamePresenter(this);
        // Have the superclass use the MultiplayerGamePresenter as its GamePresenter
        mActionsListener = mTimeAttackActionsListener;

        // Set up the RecyclerView and assign it to the superclass
        mRecyclerGridView = (RecyclerView) root.findViewById(R.id.game_recycler_grid);

        // Grab references to our views
//        SubmitProcessButton playerOneButton =
//                (SubmitProcessButton) root.findViewById(R.id.button_player_one);
//        SubmitProcessButton playerTwoButton =
//                (SubmitProcessButton) root.findViewById(R.id.button_player_two);
//        SubmitProcessButton playerThreeButton =
//                (SubmitProcessButton) root.findViewById(R.id.button_player_three);
//        SubmitProcessButton playerFourButton =
//                (SubmitProcessButton) root.findViewById(R.id.button_player_four);

        // Wrap the buttons in our MultiplayerButtonView class so we can easily animate them
//        playerOneButtonView = new MultiplayerButtonView(playerOneButton, getContext());
//        playerTwoButtonView = new MultiplayerButtonView(playerTwoButton, getContext());
//        playerThreeButtonView = new MultiplayerButtonView(playerThreeButton, getContext());
//        playerFourButtonView = new MultiplayerButtonView(playerFourButton, getContext());

//        playerOneButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mTimeAttackActionsListener.onPlayerButtonClick(1);
//            }
//        });
//        playerTwoButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mTimeAttackActionsListener.onPlayerButtonClick(2);
//            }
//        });
//        playerThreeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mTimeAttackActionsListener.onPlayerButtonClick(3);
//            }
//        });
//        playerFourButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mTimeAttackActionsListener.onPlayerButtonClick(4);
//            }
//        });

        // Initialize a game
        mTimeAttackActionsListener.initGame();

        return root;
    }

    @Override
    public void onSetSuccess() {
//        // Do stuff in response to successful SET claim
//        Snackbar.make(getView(), getString(R.string.message_found_set), Snackbar.LENGTH_LONG)
//                .show();

        // Let the presenter know someone found a set
        mTimeAttackActionsListener.onFindSetSuccess();

    }

    @Override
    public void onSetFailure() {
//        // Do stuff in response to a failed set claim
//        Snackbar.make(getView(), getString(R.string.message_not_set), Snackbar.LENGTH_LONG)
//                .show();

        mTimeAttackActionsListener.onFindSetFailure();

    }

    @Override
    public void showGameOver() {
        // Do stuff when the game is over
        Snackbar.make(getView(), getString(R.string.message_game_over), Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.message_restart), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mActionsListener.initGame();
                    }
                })
                .show();
    }

    @Override
    public void startTimeAttackCountdown() {

    }

    @Override
    public void showLeaderBoard() {

    }

    /**
     * Timer class to handle starting the time attack countdown. It will notify
     * the presenter if our time runs out
     */
    public class FindSetCountdown extends CountDownTimer {

        public FindSetCountdown(long startTime, long interval) {
            super(startTime, interval);

            start();
        }

        @Override
        public void onFinish() {
            mTimeAttackActionsListener.onTimeUp();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // Update the time display in our timer view
            // TODO: Update timer display
        }
    }
}
