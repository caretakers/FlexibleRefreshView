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

public class FlexibleRefreshLayout extends FrameLayout {
    private static final String TAG = "PullRefreshLayout";
    private Context context;
    private FlexibleRefreshView flexibleRefreshView;
    private int firstVisiblePosition;
    private float downY;
    private View childView;
    private int startAngle;
    private int radiusRound;
    private int radius;
    private int maxHeaderView;

    public FlexibleRefreshLayout(@NonNull Context context) {
        this(context, null);
    }

    public FlexibleRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlexibleRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setBackgroundColor(getResources().getColor(R.color.colorWhile));
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        Log.i(TAG, "init: ");
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FlexibleRefreshLayout);
        startAngle = ta.getInt(R.styleable.FlexibleRefreshLayout_StartAngle, -90);
        radiusRound = ta.getInt(R.styleable.FlexibleRefreshLayout_RadiusRound, 60);
        radius = ta.getInt(R.styleable.FlexibleRefreshLayout_Radius, 50);
        maxHeaderView = ta.getInt(R.styleable.FlexibleRefreshLayout_MaxheaderView, 100);
        Log.i(TAG, "init: startAngle: " + startAngle + " radiusRound: " + radiusRound + " radius:" + radius + " maxHeaderView: " + maxHeaderView);
        ta.recycle();
        this.post(new Runnable() {

            @Override
            public void run() {
                int childCount = getChildCount();
                if (childCount == 0) {
                    try {
                        throw new Exception("must has a child");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                        return;
                    }
                }else if (childCount > 1){
                    try {
                        throw new Exception("Just only one child");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                        return;
                    }
                }
                childView = getChildAt(0);
                flexibleRefreshView = new FlexibleRefreshView(context, startAngle, radiusRound, radius, maxHeaderView);
                flexibleRefreshView.setListView(childView);
                addView(flexibleRefreshView);
                startAnimation();
            }
        });

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i(TAG, "onInterceptTouchEvent: ");
        if (flexibleRefreshView == null) return super.onInterceptTouchEvent(ev);;
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
        if (flexibleRefreshView == null) {
            return super.onTouchEvent(event);
        }
        if (childView instanceof ListView) {
            firstVisiblePosition = ((ListView) childView).getFirstVisiblePosition();
            Log.i(TAG, "onInterceptTouchEvent2: " + firstVisiblePosition);
        }
        if (firstVisiblePosition == 0) {
            flexibleRefreshView.onTouchEvent(event);
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
        if (flexibleRefreshView == null) return;
        flexibleRefreshView.setOnStartAnimationListener(new FlexibleRefreshView.OnStartAnimationListener() {
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
        if (flexibleRefreshView == null) return;
        this.post(new Runnable() {
            @Override
            public void run() {
                flexibleRefreshView.stopAnimation();
            }
        });
    }
}
