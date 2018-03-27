package io.kuban.teamscreen.manager;

import android.animation.Animator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

/**
 * Created by wangxuan on 17/11/7.
 */

public class AnimationManager {
    public static void animation(View view, Techniques technique, Animator.AnimatorListener animatorListener) {
        YoYo.with(technique)
                .duration(500)
                .repeat(1)
                .pivot(YoYo.CENTER_PIVOT, YoYo.CENTER_PIVOT)
                .interpolate(new AccelerateDecelerateInterpolator())
                .withListener(animatorListener)
                .playOn(view);
    }

    public static void animationOut(View view, Techniques technique) {
        YoYo.with(technique)
                .duration(500)
                .repeat(1)
                .pivot(YoYo.CENTER_PIVOT, YoYo.CENTER_PIVOT)
                .interpolate(new AccelerateDecelerateInterpolator())
                .playOn(view);
    }

    public static void animationIn(final View view, Techniques technique) {
        YoYo.with(technique)
                .duration(500)
                .repeat(1)
                .pivot(YoYo.CENTER_PIVOT, YoYo.CENTER_PIVOT)
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .interpolate(new AccelerateDecelerateInterpolator())
                .playOn(view);
    }
}
