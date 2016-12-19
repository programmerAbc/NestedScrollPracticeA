package com.practice;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;


/**
 * Created by gaofeng on 2016-12-16.
 */

public class MyLinearLayout extends LinearLayout {
    private static final String TAG = MyLinearLayout.class.getSimpleName();
    View topView;
    View searchBar;
    View scrollViewContainer;
    ImageButton qrcodeBtn;
    ScrollView scrollView;
    int driftHeight = 0;
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
        searchBar = findViewById(R.id.searchBar);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        scrollViewContainer=findViewById(R.id.scrollViewContainer);
        qrcodeBtn= (ImageButton) findViewById(R.id.qrcodeBtn);
        qrcodeBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                FrameLayout.LayoutParams searchBarLayoutParams= (FrameLayout.LayoutParams) searchBar.getLayoutParams();
                LinearLayout.LayoutParams scrollViewContainerLayoutParams= (LayoutParams) scrollViewContainer.getLayoutParams();
                scrollViewContainerLayoutParams.height=getMeasuredHeight() - searchBar.getMeasuredHeight()-searchBarLayoutParams.topMargin-searchBarLayoutParams.bottomMargin;
                scrollViewContainer.requestLayout();
            }
        });
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
                    if (dy < 0 && getScrollY() < driftHeight) {
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
        y=y> driftHeight ? driftHeight :y;
        float scrollFactor=(float)y/ driftHeight;
        //topView.setAlpha(1-scrollFactor);
       // topView.setTranslationX(-scrollFactor*topView.getMeasuredWidth());
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
                    if (getScrollY() < driftHeight) {
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
        Log.e(TAG, "onMeasure: ===START===");
        Log.e(TAG, "onMeasure: scrollViewContainer 1st measure:"+scrollViewContainer.getMeasuredHeight() );
        FrameLayout.LayoutParams searchBarLayoutParams= (FrameLayout.LayoutParams) searchBar.getLayoutParams();
        driftHeight = topView.getMeasuredHeight()-searchBar.getMeasuredHeight()-searchBarLayoutParams.topMargin-searchBarLayoutParams.bottomMargin;
        Log.e(TAG, "onMeasure: searchBar measured height"+searchBar.getMeasuredHeight() );
        LinearLayout.LayoutParams scrollViewContainerLayoutParams= (LayoutParams) scrollViewContainer.getLayoutParams();
        Log.e(TAG, "onMeasure: a="+getMeasuredHeight()+"b="+searchBar.getMeasuredHeight()+"c="+searchBarLayoutParams.topMargin+"d="+searchBarLayoutParams.bottomMargin);
        scrollViewContainerLayoutParams.height=getMeasuredHeight() - searchBar.getMeasuredHeight()-searchBarLayoutParams.topMargin-searchBarLayoutParams.bottomMargin;
        Log.e(TAG, "onMeasure: scrollViewContainer layoutparams height"+scrollViewContainerLayoutParams.height );
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e(TAG, "onMeasure: scrollViewContainer 2nd measure:"+scrollViewContainer.getMeasuredHeight() );
        Log.e(TAG, "onMeasure: ===END===");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.e(TAG, "onLayout: ");
        super.onLayout(changed, l, t, r, b);

    }


}
