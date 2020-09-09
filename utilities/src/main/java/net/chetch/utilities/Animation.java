package net.chetch.utilities;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.view.View;

public class Animation {

    //deprecated as of 09/09/2020 use 'flash' instead
    static public ValueAnimator flashBackground(final View view, int flashColor, int duration, int repeat){
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
        colorAnimation.setRepeatCount(repeat);
        colorAnimation.start();
        return colorAnimation;
    }

    //flashes a drawable ... use repeat = Animation.INIFITE for infinite repeat
    static public ValueAnimator flash(final Drawable drawable, int fromColour, int toColour, int duration, int repeat){
        ValueAnimator colorAnimation = ValueAnimator.ofArgb(fromColour, toColour, fromColour);
        colorAnimation.setDuration(duration);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int colour = (int) valueAnimator.getAnimatedValue();
                if (drawable instanceof ShapeDrawable) {
                    ((ShapeDrawable)drawable).getPaint().setColor(colour);
                } else if (drawable instanceof GradientDrawable) {
                    ((GradientDrawable)drawable).setColor(colour);
                } else if (drawable instanceof ColorDrawable) {
                    ((ColorDrawable)drawable).setColor(colour);
                }
            }
        });
        colorAnimation.setRepeatCount(repeat);
        colorAnimation.start();
        return colorAnimation;
    }
}
