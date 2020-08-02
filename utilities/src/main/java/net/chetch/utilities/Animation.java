package net.chetch.utilities;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

public class Animation {

    static public void flashBackground(final View view, int flashColor, int duration, int repeat){
        int bgColour = Color.TRANSPARENT;
        if(view.getBackground() != null){
            bgColour = ((ColorDrawable)view.getBackground()).getColor();
        }
        ValueAnimator colorAnimation = ValueAnimator.ofArgb(bgColour, flashColor, bgColour);
        colorAnimation.setDuration(duration);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.setBackgroundColor((int) valueAnimator.getAnimatedValue());
            }
        });
        if(repeat > 0)colorAnimation.setRepeatCount(repeat);
        colorAnimation.start();

    }
}
