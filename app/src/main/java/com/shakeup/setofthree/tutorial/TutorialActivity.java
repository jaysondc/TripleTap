package com.shakeup.setofthree.tutorial;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2Fragment;
import com.github.paolorotolo.appintro.AppIntroFragment;
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
        addSlide(AppIntro2Fragment.newInstance("Test Fragment", "Here is a description.", R.drawable.ic_set_icons_oval_solid, R.color.fbutton_color_emerald));

        // Hide Skip/Done button.
        showSkipButton(true);
        setProgressButtonEnabled(true);

    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
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
