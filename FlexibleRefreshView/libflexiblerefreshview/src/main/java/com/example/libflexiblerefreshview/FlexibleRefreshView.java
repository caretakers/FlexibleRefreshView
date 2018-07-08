package com.example.libflexiblerefreshview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

/**
 * Created by Anymo on 2018/6/19.
 */

public class FlexibleRefreshView extends View {

    private static final String TAG = "PullRefreshView";
    private Paint mPaint;
    private Path mPath;
    private float downY;
    private float moveY;
    private int mScreenWidth;
    private int mScreenHeight;
    private int startX;
    private int startY;
    private int endX;
    /**
     * 控制点
     */
    private float mAuxiliaryX;
    private float mAuxiliaryY;
    /**
     * 圆心（x, y）
     */
    private float mCircleX;
    private float mCircleY;
    private Paint mOvalPaint;
    private Path mOvalPath;
    private float moffset;
    private Paint mCirclePaint;
    /**
     * 画圆环，当前进度
     */
    private int progress = 0;
    private int progress2 = 0;
    //圆的半径
    private int radius;
    //header最大高度
    private int maxHeaderView;
    /**
     * 最大进度
     */
    private static final int maxProgress = 100;
    //headerView的高度
    private int mHeaderViewHeight = 0;
    //圆环半径
    private int radiusRound;
    //圆环的起始角度
    private float startAngle;
    private boolean flag = true;
    //画正反圆环
    private boolean animationStartFlag = false;
    //是否开始刷新
    private boolean isRefreash;

    private ValueAnimator mAnimator;
    private ValueAnimator mBackAnimator;
    private ValueAnimator mCircleRiseAnimator;

    public FlexibleRefreshView(Context context, int startAngle, int radiusRound, int radius, int maxHeaderView) {
        this(context, null);
        this.startAngle = startAngle;
        this.radiusRound = radiusRound;
        this.radius = radius;
        this.maxHeaderView = maxHeaderView;
    }

    public FlexibleRefreshView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlexibleRefreshView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.LTGRAY);
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mOvalPath = new Path();
        mOvalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOvalPaint.setColor(Color.WHITE);
        mOvalPaint.setStrokeWidth(1);
        mOvalPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(Color.WHITE);
        mCirclePaint.setStrokeWidth(3);
        mCirclePaint.setStyle(Paint.Style.STROKE);

    }

    /**
     * 画线
     * （mAuxiliaryX，mAuxiliaryY）控制点随着屏幕上手指的滑动而变化
     * (endX, startY)结束点
     */
    @SuppressLint("LongLogTag")
    private void drawLine() {
        if (startY > maxHeaderView) {
            startY = maxHeaderView;
        }
        mPath.moveTo(startX, startY);
        mPath.quadTo(mAuxiliaryX, mAuxiliaryY, endX, startY);
    }

    //画圆
    private void drawOval() {
        mOvalPath.addCircle(mCircleX, mCircleY, radius, Path.Direction.CW);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mScreenWidth = w;
        mScreenHeight = h;
        mAuxiliaryX = w / 2;
        mAuxiliaryY = mHeaderViewHeight;
        mCircleX = mAuxiliaryX;
        mCircleY = mAuxiliaryY - radiusRound - 15;
        startY = 0;
        endX = w;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        mOvalPath.reset();

        Log.i("PullRefreshView", "onDraw: " + mHeaderViewHeight);
        ///headerview的高度还不到变形的高度
        if (mHeaderViewHeight <= maxHeaderView) {
            drawHeader(canvas);
            return;
        }
        //贝塞尔线
        drawLine();
        if (animationStartFlag) {
            //画圆
            drawOval();
        }
        mPath.lineTo(mScreenWidth, 0);
        mPath.lineTo(0, 0);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
        canvas.drawPath(mOvalPath, mOvalPaint);

        //画圆环
        drawAround(canvas);

    }

    private void drawHeader(Canvas canvas) {
        mPath.moveTo(startX, mHeaderViewHeight);
        mPath.lineTo(mScreenWidth, mHeaderViewHeight);
        mPath.lineTo(mScreenWidth, 0);
        mPath.lineTo(0, 0);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
    }

    private void drawAround(Canvas canvas) {
        Log.i(TAG, "drawAround: " + (animationStartFlag) + ", " + flag);
        if (!flag) {
            if (animationStartFlag) {
                if (progress <= 100) {
                    Log.i("progress", "onDraw: " + progress);
                    startAngle = -90;
                    float precent = 1.0f * progress / maxProgress;//当前完成百分比
                    canvas.drawArc(new RectF(mCircleX - radiusRound, mCircleY - radiusRound, mCircleX + radiusRound, mCircleY + radiusRound), startAngle - 270 * precent, -(0 + precent * 360), false, mCirclePaint);
                    progress++;
                    if (progress == 100) {
                        progress2 = 0;
                    }
                } else {
                    startAngle = 0;
                    float precent = 1.0f * progress2 / maxProgress;//当前完成百分比
                    canvas.drawArc(new RectF(mCircleX - radiusRound, mCircleY - radiusRound, mCircleX + radiusRound, mCircleY + radiusRound), startAngle - 90 * precent, 360 - 360 * precent, false, mCirclePaint);
                    progress2++;
                    if (progress2 == 100) {
                        progress = 0;
                    }
                }
                invalidate();
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "onTouchEvent: ");
        if (isRefreash) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                moveY = event.getY();
                moffset = moveY - downY;
                downY = moveY;
                mHeaderViewHeight += moffset;
                if (mHeaderViewHeight < 0) {
                    mHeaderViewHeight = 0;
                } else if (mHeaderViewHeight > mScreenHeight / 2) {//曲线下拉最大距离
                    mHeaderViewHeight = mScreenHeight / 2;
                }
                mAuxiliaryY = mHeaderViewHeight;
                if (startY <= maxHeaderView) {
                    startY = mHeaderViewHeight;
                } else {
                    startY = maxHeaderView;
                }
                setViewTranslationY();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                Log.i(TAG, "onTouchEvent: " + mHeaderViewHeight);
                //只是点击不操作
                if (mHeaderViewHeight <= 0) {
                    return super.onTouchEvent(event);
                }
                Log.i(TAG, "onTouchEvent2: " + mHeaderViewHeight);
                controlAnimation();
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    private void controlAnimation() {
        //动画header上升
        mBackAnimator = ValueAnimator.ofFloat(0, mHeaderViewHeight >= maxHeaderView ? maxHeaderView : mHeaderViewHeight);
        mBackAnimator.setInterpolator(new LinearInterpolator());
        mBackAnimator.setDuration(500);
        mBackAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedValue = (float) valueAnimator.getAnimatedValue();
                Log.i("pullRefreshView", "onAnimationUpdate: " + animatedValue);
                float v = startY - animatedValue;
                mHeaderViewHeight = (int) v;
                setViewTranslationY();
                invalidate();
            }
        });
        mBackAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                isRefreash = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                downY = 0;
                moveY = 0;
                moffset = 0;
                animationStartFlag = false;
                progress = 0;
                progress2 = 0;
                mAuxiliaryY = 0;
                mCircleY = mAuxiliaryY - radiusRound - 15;
                flag = true;
                isRefreash = false;
            }
        });

        //动画header弹一弹
        mAnimator = ValueAnimator.ofFloat(mHeaderViewHeight, maxHeaderView);
        mAnimator.setInterpolator(new ScaleInterpolator(0.4f));
        mAnimator.setDuration(3000);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                isRefreash = true;
                mAuxiliaryY = (float) valueAnimator.getAnimatedValue();
                view.setTranslationY(mAuxiliaryY);
                Log.i(TAG, "onAnimationUpdate1: ");
                invalidate();
            }
        });
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                animationStartFlag = true;
                isRefreash = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                flag = false;
                //动画执行完开始刷新
                if (startAnimationListener != null) {
                    startAnimationListener.startAnimation();
                }
            }
        });

        //圆的上升动画
        mCircleRiseAnimator = ValueAnimator.ofFloat(mHeaderViewHeight, maxHeaderView);
        mCircleRiseAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mCircleRiseAnimator.setDuration(500);
        mCircleRiseAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                isRefreash = true;
                animationStartFlag = true;
                mCircleY = (float) valueAnimator.getAnimatedValue() - radiusRound - 15;
                Log.i(TAG, "onAnimationUpdate2: ");
                invalidate();
            }
        });
        mCircleRiseAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                flag = false;
            }
        });

        //大于header最大就执行刷新动画
        if (mHeaderViewHeight >= maxHeaderView) {
            mAnimator.start();
            mCircleRiseAnimator.start();
        } else {
            mBackAnimator.start();
        }
    }

    private void setViewTranslationY() {
        view.setTranslationY(mHeaderViewHeight);
    }

    private View view;

    public void setListView(View view) {
        this.view = view;
    }

    private OnStartAnimationListener startAnimationListener;

    public interface OnStartAnimationListener {
        void startAnimation();
    }

    public void setOnStartAnimationListener(OnStartAnimationListener listener) {
        this.startAnimationListener = listener;
    }

    public void stopAnimation() {
        if (isRefreash && mBackAnimator != null) {
            mBackAnimator.start();
            mAnimator.cancel();
        }
    }
}
