package com.practice;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;


/**
 * Created by gaofeng on 2016-12-16.
 */

public class MyLinearLayout extends LinearLayout {
    private static final String TAG = MyLinearLayout.class.getSimpleName();
    View topView;
    View buttonBar;
    View searchBar;
    View scrollViewContainer;
    ImageButton qrcodeBtn;
    ImageButton personBtn;
    ImageButton checkDownloadBtn;
    ImageButton checkWifiBtn;
    ImageView topIv;
    ScrollView scrollView;
    int driftHeight = -1;
    float lastY = 0;
    boolean shouldInterceptActionMove = false;
    int searchBarMeasuredWidth = -1;
    int searchBarTopMargin = -1;
    int searchBarBottomMargin = -1;
    Scroller scroller;

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
        scroller = new Scroller(getContext());
        topView = findViewById(R.id.topView);
        searchBar = findViewById(R.id.searchBar);
        buttonBar=findViewById(R.id.buttonBar);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        scrollViewContainer = findViewById(R.id.scrollViewContainer);
        topIv = (ImageView) findViewById(R.id.topIv);
        personBtn = (ImageButton) findViewById(R.id.personBtn);
        checkDownloadBtn = (ImageButton) findViewById(R.id.checkDownloadBtn);
        qrcodeBtn = (ImageButton) findViewById(R.id.qrcodeBtn);
        checkWifiBtn= (ImageButton) findViewById(R.id.checkWifiBtn);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e(TAG, "dispatchTouchEvent: " + ev.getAction());
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                scroller.abortAnimation();
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
                shouldInterceptActionMove = false;
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e(TAG, "onInterceptTouchEvent: " + ev.getAction());
        switch (ev.getAction()) {
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
        y = y < 0 ? 0 : y;
        y = y > driftHeight ? driftHeight : y;
        float scrollFactor = (float) y / driftHeight;
        buttonBar.setTranslationY(y);
        checkWifiBtn.setAlpha(1-scrollFactor);
        topIv.setAlpha(1 - scrollFactor);
        topIv.setScaleY(1+scrollFactor);
        topIv.setScaleX(1+scrollFactor);
        Log.e(TAG, "scrollTo: a= " + personBtn.getMeasuredWidth() + " b= " + checkDownloadBtn.getMeasuredWidth() + " c= " + scrollFactor);
        Log.e(TAG, "scrollTo: width=" + (searchBarMeasuredWidth - (int) ((personBtn.getMeasuredWidth() + ((RelativeLayout.LayoutParams) personBtn.getLayoutParams()).rightMargin + checkDownloadBtn.getMeasuredWidth() + ((RelativeLayout.LayoutParams) checkDownloadBtn.getLayoutParams()).leftMargin) * scrollFactor)));
        searchBar.getLayoutParams().width = searchBarMeasuredWidth - (int) ((personBtn.getMeasuredWidth() + ((RelativeLayout.LayoutParams) personBtn.getLayoutParams()).rightMargin + checkDownloadBtn.getMeasuredWidth() + ((RelativeLayout.LayoutParams) checkDownloadBtn.getLayoutParams()).leftMargin) * scrollFactor);
        ((FrameLayout.LayoutParams) searchBar.getLayoutParams()).topMargin = searchBarTopMargin - (int) (searchBarTopMargin / 2 * scrollFactor);
        ((FrameLayout.LayoutParams) searchBar.getLayoutParams()).bottomMargin = searchBarBottomMargin - (int) (searchBarBottomMargin / 2 * scrollFactor);
        searchBar.requestLayout();
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
                if (getScrollY() > 0 && getScrollY() < driftHeight) {
                    if (getScrollY() > driftHeight / 2) {
                        scroller.startScroll(0, getScrollY(), 0, driftHeight - getScrollY(), 300);
                        invalidate();
                    } else {
                        scroller.startScroll(0, getScrollY(), 0, -getScrollY(), 300);
                        invalidate();
                    }
                }


                break;
        }


        return true;
    }

    @Override
    public void computeScroll() {
        if(scroller.computeScrollOffset()){
            scrollTo(0,scroller.getCurrY());
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e(TAG, "onMeasure: ===START===");
        if (searchBarMeasuredWidth == -1) {
            searchBarMeasuredWidth = searchBar.getMeasuredWidth();
        }
        Log.e(TAG, "onMeasure: scrollViewContainer 1st measure:" + scrollViewContainer.getMeasuredHeight());
        FrameLayout.LayoutParams searchBarLayoutParams = (FrameLayout.LayoutParams) searchBar.getLayoutParams();
        if (driftHeight < 0) {
            driftHeight = topView.getMeasuredHeight() - searchBar.getMeasuredHeight() - searchBarLayoutParams.topMargin / 2 - searchBarLayoutParams.bottomMargin / 2;
        }
        if (searchBarTopMargin < 0) {
            searchBarTopMargin = ((FrameLayout.LayoutParams) searchBar.getLayoutParams()).topMargin;
        }
        if (searchBarBottomMargin < 0) {
            searchBarBottomMargin = ((FrameLayout.LayoutParams) searchBar.getLayoutParams()).bottomMargin;
        }
        Log.e(TAG, "onMeasure: searchBar measured height" + searchBar.getMeasuredHeight());
        LinearLayout.LayoutParams scrollViewContainerLayoutParams = (LayoutParams) scrollViewContainer.getLayoutParams();
        Log.e(TAG, "onMeasure: a=" + getMeasuredHeight() + "b=" + searchBar.getMeasuredHeight() + "c=" + searchBarLayoutParams.topMargin + "d=" + searchBarLayoutParams.bottomMargin);
        scrollViewContainerLayoutParams.height = getMeasuredHeight() - searchBar.getMeasuredHeight() - searchBarLayoutParams.topMargin - searchBarLayoutParams.bottomMargin;
        Log.e(TAG, "onMeasure: scrollViewContainer layoutparams height" + scrollViewContainerLayoutParams.height);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e(TAG, "onMeasure: scrollViewContainer 2nd measure:" + scrollViewContainer.getMeasuredHeight());
        Log.e(TAG, "onMeasure: ===END===");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.e(TAG, "onLayout: ");
        super.onLayout(changed, l, t, r, b);

    }


}
