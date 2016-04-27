package com.shengtao.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hxhuang on 2016/1/12 0012.
 */
public abstract class BezierAnimation {

    private FrameLayout animContainer;  //动画容器
    private View animTarget;            //执行动画的目标
    private View v;
    private PointF startP, endP, baseP;
    private List<Animator> animators = new ArrayList<Animator>();

    /**
     * 贝塞尔曲线动画
     * @param container 动画容器
     * @param target 执行动画的目标
     * @param view 事件源对象
     */
    public BezierAnimation(FrameLayout container, View target,View view){
        animContainer = container;
        animTarget = target;
        v = view;
    }

    class BezierEvaluator implements TypeEvaluator<PointF>
    {
        @Override
        public PointF evaluate(float fraction, PointF startValue,
                               PointF endValue)
        {
            if (fraction < 0 || fraction > 1) {
                throw new IllegalArgumentException("t must between 0 and 1");
            }
            PointF point1 = new PointF((startValue.x + endValue.x) / 2,0);

            float oneMinusT = 1.0f - fraction;
            PointF point = new PointF();
            point.x =oneMinusT * oneMinusT * startValue.x
                    + 2 * fraction * oneMinusT * point1.x
                    + fraction * fraction * endValue.x;//oneMinusT * startValue.x+fraction * endValue.x;
            point.y =oneMinusT * startValue.y+fraction * endValue.y;
            /*point.x = oneMinusT * oneMinusT * startValue.x
                    + 2 * fraction * oneMinusT * point1.x
                    + fraction * fraction * endValue.x;
            point.y = oneMinusT * oneMinusT * startValue.y
                    + 2 * fraction * oneMinusT * point1.y
                    + fraction * fraction * endValue.y;*/
            return point;
        }
    }


    public void showAnimation(){
        int[] location = new int[2];
        if (endP == null)
        {
            animContainer.getLocationOnScreen(location);
            baseP = new PointF(location[0],location[1]);
            endP = new PointF();
            int endLocation[] = new int[2];
            getEndView().getLocationOnScreen(endLocation);
            endP.x = endLocation[0];
            endP.y = endLocation[1]-45;
        }

        v.getLocationOnScreen(location);
        startP = new PointF(location[0]-baseP.x,location[1]-baseP.y);

        ValueAnimator valueAnimator = ValueAnimator.ofObject(new BezierEvaluator(), startP, endP);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                PointF pointF = (PointF) animation.getAnimatedValue();
                animTarget.setX(pointF.x);
                animTarget.setY(pointF.y);
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            public void onAnimationStart(Animator animation) {
                animContainer.addView(animTarget);
            }

            public void onAnimationEnd(Animator animation) {
                animContainer.removeView(animTarget);
                animTarget.destroyDrawingCache();
            }

            public void onAnimationCancel(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }
        });

        AnimatorSet animatorSet   = new AnimatorSet();
        animators.add(valueAnimator.setDuration(700));
        animators.addAll(getAnimators());
        animatorSet.playSequentially(
                animators
        );
        /*int len = animators.size()-1;
        AnimatorSet.Builder play = animatorSet.play(animators.get(len--));
        while(len != -1){
            play=play.before(animators.get(len--));
        }*/
        animatorSet.start();
    }

    protected abstract List<Animator> getAnimators();
    protected abstract View getEndView();
}
