package com.shakeup.setofthree.multiplayergame;

import android.content.Context;
import android.os.CountDownTimer;

import com.dd.processbutton.iml.SubmitProcessButton;
import com.shakeup.setofthree.R;

/**
 * Created by Jayson on 3/23/2017.
 * <p>
 * This is a wrapper for the SubmitProcessButton allowing states to be set
 * more easily
 */

public class MultiplayerButtonView {

    // Constants for Timers
    public static final long BUTTON_FIND_SET_TIMER_LENGTH = 3000;
    public static final long BUTTON_FIND_SET_TIMER_DEBUG_LENGTH = 1000;
    public static final long BUTTON_TIMER_TICK_INTERVAL = 15;
    public static final long BUTTON_MESSAGE_LENGTH = 2000;

    private SubmitProcessButton mButton;
    private CountDownTimer mTimer;
    private Context mContext;

    public MultiplayerButtonView(SubmitProcessButton button, Context context) {
        mButton = button;
        mContext = context;
    }


    public void activePlayerWait() {
        if (mTimer != null) {
            mTimer.cancel();
        }

        // Set a shorter timer if we're in debug mode
        if (mContext.getResources().getBoolean(R.bool.is_debug)) {
            mTimer = new ButtonWaitingCountdownTimer(
                    BUTTON_FIND_SET_TIMER_DEBUG_LENGTH,
                    BUTTON_TIMER_TICK_INTERVAL,
                    true,
                    mContext.getResources().getString(R.string.button_claim_set)
            );
        } else {
            mTimer = new ButtonWaitingCountdownTimer(
                    BUTTON_FIND_SET_TIMER_LENGTH,
                    BUTTON_TIMER_TICK_INTERVAL,
                    true,
                    mContext.getResources().getString(R.string.button_claim_set)
            );
        }
    }

    public void otherPlayerWait() {
        if (mTimer != null) {
            mTimer.cancel();
        }

        // Set a shorter timer if we're in debug mode
        if (mContext.getResources().getBoolean(R.bool.is_debug)) {
            mTimer = new ButtonWaitingCountdownTimer(
                    BUTTON_FIND_SET_TIMER_DEBUG_LENGTH,
                    BUTTON_TIMER_TICK_INTERVAL,
                    true,
                    mContext.getResources().getString(R.string.button_claim_set)
            );
        } else {
            mTimer = new ButtonWaitingCountdownTimer(
                    BUTTON_FIND_SET_TIMER_LENGTH,
                    BUTTON_TIMER_TICK_INTERVAL,
                    false,
                    mContext.getResources().getString(R.string.button_wait)
            );
        }
    }

    /**
     * Displays a short "You found a SET" message on the button
     */
    public void activePlayerSuccess() {
        if (mTimer != null) {
            mTimer.cancel();
        }
        mTimer = new ButtonMessageCountdownTimer(
                BUTTON_MESSAGE_LENGTH,
                BUTTON_TIMER_TICK_INTERVAL,
                100,
                mContext.getResources().getString(R.string.button_found_set)
        );
    }

    /**
     * Displays a short "Time is up!" message on the button
     */
    public void activePlayerTimeout() {
        if (mTimer != null) {
            mTimer.cancel();
        }
        mTimer = new ButtonMessageCountdownTimer(
                BUTTON_MESSAGE_LENGTH,
                BUTTON_TIMER_TICK_INTERVAL,
                -1,
                mContext.getResources().getString(R.string.button_out_of_time)
        );
    }

    /**
     * Displays a short "That's not a SET" message on the button
     */
    public void activePlayerFailure() {
        if (mTimer != null) {
            mTimer.cancel();
        }
        mTimer = new ButtonMessageCountdownTimer(
                BUTTON_MESSAGE_LENGTH,
                BUTTON_TIMER_TICK_INTERVAL,
                -1,
                mContext.getResources().getString(R.string.button_not_a_set)
        );
    }

    /**
     * Turns the button back to normal
     */
    public void resetButton() {
        if (mTimer != null) {
            mTimer.cancel();
        }
        mButton.setClickable(true);
        mButton.setProgress(0);
    }

    /**
     * This is a custom timer for the SubmitProcessButton that allows
     * you to set a progress state and message to display
     */
    public class ButtonMessageCountdownTimer extends CountDownTimer {

        public ButtonMessageCountdownTimer(
                long startTime,
                long countDownInterval,
                int progress,
                String message) {
            super(startTime, countDownInterval);

            // Set the message differently depending on if its a Complete
            // or Error message
            if (progress == 100) {
                mButton.setClickable(true);
                mButton.setCompleteText(message);
            } else {
                mButton.setClickable(false);
                mButton.setErrorText(message);
            }

            // Set the progress state
            mButton.setProgress(progress);

            start();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // Do nothing
        }

        @Override
        public void onFinish() {
            // Reset the button state
            mButton.setProgress(0);
            mButton.setClickable(true);
        }
    }

    /**
     * This is a custom timer for setting the button to "Tap a set!" and "Waiting for player"
     * messages and progress bar
     */
    public class ButtonWaitingCountdownTimer extends CountDownTimer {

        long mStartTime;
        boolean mIsActivePlayer;

        public ButtonWaitingCountdownTimer(
                long startTime,
                long countDownInterval,
                boolean isActivePlayer,
                String message) {
            super(startTime, countDownInterval);

            // Save a reference to the timer length
            mStartTime = startTime;
            mIsActivePlayer = isActivePlayer;

            // Set the message differently depending on if we're the active player
            if (isActivePlayer) {
                mButton.setLoadingText(message);
            } else {
                mButton.setErrorText(message);
                mButton.setProgress(-1);
            }
            // Set the button as unclickable
            mButton.setClickable(false);

            start();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            int progressPercent;

            double start = (double) mStartTime;
            double timeLeft = (double) millisUntilFinished;

            progressPercent = (int) Math.floor((1 - (timeLeft / start)) * 100);

            // Only display progress for the active player.
            // Only display progress animations if we aren't in debug mode
            // Other players wait in error mode
            if (mIsActivePlayer &&
                    !mContext.getResources().getBoolean(R.bool.is_debug)) {
                mButton.setProgress(progressPercent);
            }
        }

        @Override
        public void onFinish() {
            // No nothing as this state should get reset from the outside
            // by the time the timer finishes
        }
    }
}
