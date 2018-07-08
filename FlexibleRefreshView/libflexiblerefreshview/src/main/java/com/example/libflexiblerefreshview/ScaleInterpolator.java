package com.example.libflexiblerefreshview;

import android.view.animation.Interpolator;

/**
 * Created by Anymo on 2018/7/4.
 */

class ScaleInterpolator implements Interpolator {
    //弹性因数
    private float factor;

    public ScaleInterpolator(float factor) {
        this.factor = factor;
    }

    @Override
    public float getInterpolation(float input) {

        return (float) (Math.pow(2, -9 * input) * Math.sin((input - factor / 4) * (2 * Math.PI) / factor) + 1);
    }


}