package com.shakeup.setofthree.tutorial;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.shakeup.setofthree.R;

/**
 * Created by Jayson Dela Cruz on 8/5/2017.
 *
 * TutorialActivity displays a pager view of TutorialSlides that teach the user
 * how to play Set of Three
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

        // Hide Skip/Done button.
        showSkipButton(true);
        setProgressButtonEnabled(true);

    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // End the current activity. TODO Change this to launch training mode.
        this.finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}
