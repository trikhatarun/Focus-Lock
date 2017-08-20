package com.android.trikh.focusLock;

import android.os.Bundle;

import com.android.trikh.focusLock.ui.InputDateFragment;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

public class MainIntroActivity extends IntroActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addSlide(new SimpleSlide.Builder()
                .title(R.string.title_1)
                .description(R.string.description_1)
                .image(R.drawable.new_bitmap_image)
                .background(R.color.frag1_light)
                .backgroundDark(R.color.frag1_dark)
                .scrollable(false)
                .build());
        addSlide(new SimpleSlide.Builder()
                .image(R.drawable.screenshot2)
                .title(R.string.title_2)
                .background(R.color.frag2_light)
                .backgroundDark(R.color.frag2_dark)
                .scrollable(false)
                .description(R.string.description_2)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title(R.string.title_3)
                .description(R.string.description_3)
                .image(R.drawable.rsz_screenshot_20170808_180256)
                .background(R.color.frag3_light)
                .backgroundDark(R.color.frag3_dark)
                .scrollable(false)
                .build());
        addSlide(new FragmentSlide.Builder()
                .background(R.color.frag4_light)
                .backgroundDark(R.color.frag4_dark)
                .fragment(InputDateFragment.newInstance())
                .build());
    }

}
