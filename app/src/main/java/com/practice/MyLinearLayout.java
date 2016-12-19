package com.practice;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;


/**
 * Created by gaofeng on 2016-12-16.
 */

public class MyLinearLayout extends LinearLayout {
    private static final String TAG = MyLinearLayout.class.getSimpleName();
    View topView;
    View navView;
    ScrollView scrollView;
    int topViewHeight = 0;
    float lastY = 0;
    boolean shouldInterceptActionMove = false;
    String[] ACTION_NAME = {"ACTION_DOWN", "ACTION_UP", "ACTION_MOVE", "ACTION_CANCEL"};

    public MyLinearLayout(Context context) {
        super(context);
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        topView = findViewById(R.id.topView);
        navView = findViewById(R.id.navView);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e(TAG, "dispatchTouchEvent: " + ev.getAction());
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = ev.getY() - lastY;
                if (shouldInterceptActionMove == false) {
                    if (dy < 0 && getScrollY() < topViewHeight) {
                        shouldInterceptActionMove = true;
                    }
                    if (dy > 0 && scrollView.getScrollY() == 0) {
                        shouldInterceptActionMove = true;
                        ev.setAction(MotionEvent.ACTION_DOWN);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                shouldInterceptActionMove =false;
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e(TAG, "onInterceptTouchEvent: " + ev.getAction());
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastY = ev.getY();
                return false;
            case MotionEvent.ACTION_MOVE:
                return shouldInterceptActionMove;
            case MotionEvent.ACTION_UP:
                return false;
            default:
                return false;
        }
    }

    @Override
    public void scrollTo(int x, int y) {
        y=y<0?0:y;
        y=y>topViewHeight?topViewHeight:y;
        float scrollFactor=(float)y/topViewHeight;
        topView.setAlpha(1-scrollFactor);
        topView.setTranslationX(-scrollFactor*topView.getMeasuredWidth());
        super.scrollTo(x, y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG, "onTouchEvent: " + event.getAction());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "onTouchEvent: ACTION_MOVE");


                float dy = event.getY() - lastY;
                lastY = event.getY();
                if (dy < 0) {
                    if (getScrollY() < topViewHeight) {
                           scrollBy(0, (int) -dy);
                    } else {
                        event.setAction(MotionEvent.ACTION_UP);
                        dispatchTouchEvent(event);
                        MotionEvent motionEvent = MotionEvent.obtain(event);
                        motionEvent.setAction(MotionEvent.ACTION_DOWN);
                        dispatchTouchEvent(motionEvent);
                    }

                } else if (dy > 0) {
                    if (getScrollY() > 0) {
                        scrollBy(0, (int) -dy);
                    } else {
                        event.setAction(MotionEvent.ACTION_UP);
                        dispatchTouchEvent(event);
                        MotionEvent motionEvent = MotionEvent.obtain(event);
                        motionEvent.setAction(MotionEvent.ACTION_DOWN);
                        dispatchTouchEvent(motionEvent);
                    }
                }

                break;
            case MotionEvent.ACTION_UP:

                break;
        }


        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        topViewHeight = topView.getMeasuredHeight();
        scrollView.getLayoutParams().height = getMeasuredHeight() - navView.getMeasuredHeight();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.e(TAG, "onLayout: ");
        super.onLayout(changed, l, t, r, b);
    }


}
