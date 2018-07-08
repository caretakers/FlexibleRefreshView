package com.example.libflexiblerefreshview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;

/**
 * Created by Anymo on 2018/7/2.
 */

public class PullRefreashLayout extends FrameLayout {
    private static final String TAG = "PullRefreashLayout";
    private Context context;
    private PullRefreshView pullRefreshView;
    private int firstVisiblePosition;
    private float downY;
    private View childView;
    private int startAngle;
    private int radiusRound;
    private int radius;
    private int maxHeaderView;

    public PullRefreashLayout(@NonNull Context context) {
        this(context, null);
    }

    public PullRefreashLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullRefreashLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setBackgroundColor(getResources().getColor(R.color.colorWhile));
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        Log.i(TAG, "init: ");
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PullRefreashLayout);
        startAngle = ta.getInt(R.styleable.PullRefreashLayout_StartAngle, -90);
        radiusRound = ta.getInt(R.styleable.PullRefreashLayout_RadiusRound, 60);
        radius = ta.getInt(R.styleable.PullRefreashLayout_Radius, 50);
        maxHeaderView = ta.getInt(R.styleable.PullRefreashLayout_MaxheaderView, 100);
        Log.i(TAG, "init: startAngle: " + startAngle + " radiusRound: " + radiusRound + " radius:" + radius + " maxHeaderView: " + maxHeaderView);
        ta.recycle();
        this.post(new Runnable() {

            @Override
            public void run() {
                childView = getChildAt(0);
                pullRefreshView = new PullRefreshView(context, startAngle, radiusRound, radius, maxHeaderView);
                pullRefreshView.setListView(childView);
                addView(pullRefreshView);
                startAnimation();
            }
        });

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i(TAG, "onInterceptTouchEvent: ");
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveY = ev.getY();
                float dy = moveY - downY;
                Log.i(TAG, "onInterceptTouchEvent3: " + canChildScrollUp() + (dy > 0));
                //判断是否滑到底部
                if (dy > 0 && !canChildScrollUp()) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "onTouchEvent: ");
        if (childView instanceof ListView) {
            firstVisiblePosition = ((ListView) childView).getFirstVisiblePosition();
            Log.i(TAG, "onInterceptTouchEvent2: " + firstVisiblePosition);
        }
        if (firstVisiblePosition == 0) {
            pullRefreshView.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    private boolean canChildScrollUp() {
        if (childView == null) {
            return false;
        }
        /**
         * 检测一个 View 在给定的方向（up or down）能否竖直滑动
         *
         * @param 调用该方法的 view
         * @param 负数表示检测上滑，正数表示下滑
         * 返回 true 表示能在指定的方向滑动，false 反之
         */
        return ViewCompat.canScrollVertically(childView, -1);
    }

    private void startAnimation(){
        pullRefreshView.setOnStartAnimationListener(new PullRefreshView.OnStartAnimationListener() {
            @Override
            public void startAnimation() {
                Log.i(TAG, "startAnimation: ");
                if (refreashingListener != null) {
                    refreashingListener.refreashing();
                }
            }
        });
    }
    
    private OnRefreashingListener refreashingListener;
    public interface OnRefreashingListener{
        void refreashing();
    }

    public void setOnRefreashingListener(OnRefreashingListener listener){
        this.refreashingListener = listener;
    }
    
    public void stopRefreash(){
        this.post(new Runnable() {
            @Override
            public void run() {
                pullRefreshView.stopAnimation();
            }
        });
    }
}
