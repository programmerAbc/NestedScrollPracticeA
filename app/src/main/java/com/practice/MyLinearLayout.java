package com.practice;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import static android.content.ContentValues.TAG;


/**
 * Created by gaofeng on 2016-12-16.
 */

public class MyLinearLayout extends LinearLayout {
    View topView;
    View navView;
    ScrollView scrollView;
    int topViewHeight = 0;
    float lastY = 0;
    String[] ACTION_NAME={"ACTION_DOWN","ACTION_UP","ACTION_MOVE","ACTION_CANCEL"};
    GestureDetector gestureDetector;
    boolean shouldIntercepted=false;
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
        gestureDetector=new GestureDetector(getContext(),new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return super.onScroll(e1, e2, distanceX, distanceY);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return super.onFling(e1, e2, velocityX, velocityY);
            }

        });
        topView = findViewById(R.id.topView);
        navView = findViewById(R.id.navView);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e(TAG, "dispatchTouchEvent: " +ACTION_NAME[ev.getAction()]);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e(TAG, "onInterceptTouchEvent: "+ACTION_NAME[ev.getAction()] );
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "onInterceptTouchEvent: ACTION_MOVE" );
                float dy = ev.getY() - lastY;
                if (dy < 0 && getScrollY() < topViewHeight) {
                    return true;
                }
                if (dy > 0 && scrollView.getScrollY() == 0) {
                    return true;
                }

                break;
            case MotionEvent.ACTION_UP:

                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG, "onTouchEvent: "+ACTION_NAME[event.getAction()] );
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "onTouchEvent: ACTION_MOVE" );
                float dy = event.getY() - lastY;
                lastY = event.getY();
                if (dy < 0) {

                    if (getScrollY() < topViewHeight) {
                        int scrollDistance=(int)-dy;
                        scrollDistance=scrollDistance>(topViewHeight-getScrollY())?(topViewHeight-getScrollY()):scrollDistance;
                        scrollBy( 0, scrollDistance);
                    } else {
                        event.setAction(MotionEvent.ACTION_CANCEL);
                        dispatchTouchEvent(event);
                        MotionEvent motionEvent=MotionEvent.obtain(event);
                        motionEvent.setAction(MotionEvent.ACTION_DOWN);
                        dispatchTouchEvent(motionEvent);
                    }

                } else if (dy > 0) {
                    if(getScrollY()>0){
                        int scrollDistance=(int)dy;
                        scrollDistance=getScrollY()-scrollDistance>=0?scrollDistance:getScrollY();
                        scrollBy(0,-scrollDistance);
                    }else{
                        event.setAction(MotionEvent.ACTION_CANCEL);
                        dispatchTouchEvent(event);
                        MotionEvent motionEvent=MotionEvent.obtain(event);
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
