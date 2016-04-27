package com.shengtao.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by hxhuang on 2016/1/19 0019.
 */
public class AnimUtil {

    /**抖动动画*/
    public static void jitter(View view){
        AnimatorSet as = new AnimatorSet();
        as.playSequentially(
                //ObjectAnimator.ofFloat(llQq,"translationY",0,10).setDuration(1),//上移
                ObjectAnimator.ofFloat(view, "translationX", 0, -20).setDuration(5),//左移
                //ObjectAnimator.ofFloat(llQq,"translationY",0,-10).setDuration(1),//下移
                ObjectAnimator.ofFloat(view,"translationX",0,20).setDuration(25),//右移
                ObjectAnimator.ofFloat(view,"translationX",0,-10).setDuration(40),//右移
                ObjectAnimator.ofFloat(view,"translationX",0,10).setDuration(50)//右移
        );
        as.start();
    }
}
