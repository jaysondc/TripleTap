package com.shakeup.setofthree.tutorial;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.shakeup.setofthree.R;
import com.shakeup.setofthree.practicegame.PracticeGameActivity;

/**
 * Created by Jayson Dela Cruz on 8/5/2017.
 *
 * TutorialActivity displays a pager view of TutorialSlides that teach the user
 * how to play Triple Tap
 */

public class TutorialActivity extends AppIntro {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Note here that we DO NOT use setContentView();

        // Add your slide fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
        addSlide(TutorialSlide.newInstance(R.layout.tutorial_slide_1));
        addSlide(TutorialSlide.newInstance(R.layout.tutorial_slide_2));
        addSlide(TutorialSlide.newInstance(R.layout.tutorial_slide_3));
        addSlide(TutorialSlide.newInstance(R.layout.tutorial_slide_4));
        addSlide(TutorialSlide.newInstance(R.layout.tutorial_slide_5));
        addSlide(TutorialSlide.newInstance(R.layout.tutorial_slide_6));
        addSlide(TutorialSlide.newInstance(R.layout.tutorial_slide_7));
        addSlide(TutorialSlide.newInstance(R.layout.tutorial_slide_8));
        addSlide(TutorialSlide.newInstance(R.layout.tutorial_slide_9));
        addSlide(TutorialSlide.newInstance(R.layout.tutorial_slide_10));
        addSlide(TutorialSlide.newInstance(R.layout.tutorial_slide_11));
        addSlide(TutorialSlide.newInstance(R.layout.tutorial_slide_12));
        addSlide(TutorialSlide.newInstance(R.layout.tutorial_slide_13));

        // Hide Skip/Done button.
        showSkipButton(true);
        setProgressButtonEnabled(true);

        // Set 'DONE' text
        setDoneText(getString(R.string.tutorial_done_text));

    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // End the current activity.
        this.finish();
    }

    /**
     * When the tutorial is finished, open Practice Mode
     * @param currentFragment
     */
    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // End the current activity.
        Intent intent = new Intent(this, PracticeGameActivity.class);
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivity(intent, bundle);

        this.finishAfterTransition();
    }
}
