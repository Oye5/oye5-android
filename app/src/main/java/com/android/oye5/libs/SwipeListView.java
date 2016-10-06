package com.android.oye5.libs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

public class SwipeListView extends ListView {
    private Boolean mIsHorizontal;

    private View mPreItemView;

    private View mCurrentItemView;

    private float mFirstX;

    private float mFirstY;

    private int mRightViewWidth = 0;
    private int mLeftViewWidth = 0;
    private int mCenterViewWidth = 0;

    // private boolean mIsInAnimation = false;
    private final int mDuration = 100;

    private final int mDurationStep = 10;

    private int mIsShown;

    public SwipeListView(Context context) {
        super(context);
    }

    public SwipeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipeListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * return true, deliver to listView. return false, deliver to child. if
     * move, return true
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        float lastX = ev.getX();
        float lastY = ev.getY();
        if (canDelete){
        	return super.onInterceptTouchEvent(ev);
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsHorizontal = null;
                System.out.println("onInterceptTouchEvent----->ACTION_DOWN");
                mFirstX = lastX;
                mFirstY = lastY;
                int motionPosition = pointToPosition((int)mFirstX, (int)mFirstY);

                if (motionPosition >= 0) {
                	System.out.println("onInterceptTouchEvent----->motionPosition="+motionPosition);
                    View currentItemView = getChildAt(motionPosition - getFirstVisiblePosition());
                    mPreItemView = mCurrentItemView;
                    mCurrentItemView = currentItemView;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                float dx = lastX - mFirstX;
                float dy = lastY - mFirstY;

                if (Math.abs(dx) >= 5 && Math.abs(dy) >= 5) {
                	if (Math.abs(dx) > Math.abs(dy)){
                		mPreItemView = mCurrentItemView;
                		return false;
                	}
                    return true;
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                System.out.println("onInterceptTouchEvent----->ACTION_UP");
                if (mIsShown == 1 && (mPreItemView != mCurrentItemView || isHitCurItemLeft(lastX))) {
                    System.out.println("1---> hiddenRight");
                    /**
                     * 情况一：
                     * <p>
                     * 一个Item的右边布局已经显示，
                     * <p>
                     * 这时候点击任意一个item, 那么那个右边布局显示的item隐藏其右边布局
                     */
                    hiddenLeftRight(mPreItemView);
                }else if (mIsShown == -1 && (mPreItemView != mCurrentItemView || isHitCurItemRight(lastX))) {
                    System.out.println("1---> hiddenRight");
                    /**
                     * 情况一：
                     * <p>
                     * 一个Item的右边布局已经显示，
                     * <p>
                     * 这时候点击任意一个item, 那么那个右边布局显示的item隐藏其右边布局
                     */
                    hiddenLeftRight(mPreItemView);
                }
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    private boolean isHitCurItemLeft(float x) {
        return x < getWidth() - mRightViewWidth;
    }
    
    private boolean isHitCurItemRight(float x) {
        return x > mLeftViewWidth;
    }

    /**
     * @param dx
     * @param dy
     * @return judge if can judge scroll direction
     */
    private boolean judgeScrollDirection(float dx, float dy) {
        boolean canJudge = true;

        if (Math.abs(dx) > 30 && Math.abs(dx) > 2 * Math.abs(dy)) {
            mIsHorizontal = true;
            System.out.println("mIsHorizontal---->" + mIsHorizontal);
        } else if (Math.abs(dy) > 30 && Math.abs(dy) > 2 * Math.abs(dx)) {
            mIsHorizontal = false;
            System.out.println("mIsHorizontal---->" + mIsHorizontal);
        } else {
            canJudge = false;
        }

        return canJudge;
    }

    /**
     * return false, can't move any direction. return true, cant't move
     * vertical, can move horizontal. return super.onTouchEvent(ev), can move
     * both.
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        float lastX = ev.getX();
        float lastY = ev.getY();
        if (canDelete){
        	return super.onTouchEvent(ev);
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                System.out.println("---->ACTION_DOWN");
                break;

            case MotionEvent.ACTION_MOVE:
                float dx = lastX - mFirstX;
                float dy = lastY - mFirstY;

                // confirm is scroll direction
                if (mIsHorizontal == null) {
                    if (!judgeScrollDirection(dx, dy)) {
                    	System.out.println("onTouchEvent----->Move break");
                        break;
                    }
                }

                if (mIsHorizontal) {
                    if (mIsShown != 0 && mPreItemView != mCurrentItemView) {
                        System.out.println("2---> hiddenRight");
                        hiddenLeftRight(mPreItemView);
                        mPreItemView = mCurrentItemView;
                    }
                    
                    if (mIsShown == 1 && mPreItemView == mCurrentItemView) {
                        dx = mRightViewWidth + mLeftViewWidth - dx;
                        System.out.println("======dx 1 " + dx);
                    }else if (mIsShown == -1 && mPreItemView == mCurrentItemView) {
                        dx = -dx;
                        System.out.println("======dx -1 " + dx);
                    }else if (mPreItemView == mCurrentItemView){
                    	dx = mLeftViewWidth - dx;
                    	System.out.println("======dx 0 " + dx);
                    }else{
                    	dx = mLeftViewWidth - dx;
                    }
                    System.out.println("======dx " + dx);
                    // can't move beyond boundary
                    if (mCurrentItemView != null){
	                    if (dx > 0 && dx < mLeftViewWidth + mRightViewWidth) {
	                        mCurrentItemView.scrollTo((int)(dx), 0);
	                    }else if (dx > mLeftViewWidth + mRightViewWidth){
	                    	mCurrentItemView.scrollTo((int)(mLeftViewWidth + mRightViewWidth), 0);
	                    }else if (dx < 0){
	                    	mCurrentItemView.scrollTo(0, 0);
	                    }
                    }

                    return true;
                } else {
                    if (mIsShown != 0) {
                        System.out.println("3---> hiddenRight");
                        /**
                         * 情况三：
                         * <p>
                         * 一个Item的右边布局已经显示，
                         * <p>
                         * 这时候上下滚动ListView,那么那个右边布局显示的item隐藏其右边布局
                         */
                        hiddenLeftRight(mPreItemView);
                    }
                }

                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                System.out.println("============ACTION_UP");
                clearPressedState();
                

                if (mIsHorizontal != null && mIsHorizontal) {
                    if (mFirstX - lastX > mRightViewWidth / 8 && mIsShown != -1) {
                        showRight(mCurrentItemView);
                    } else if (lastX - mFirstX > mLeftViewWidth / 8 && mIsShown != 1){
                    	showLeft(mCurrentItemView);
                    } else {
                        System.out.println("5---> hiddenRight");
                        /**
                         * 情况五：
                         * <p>
                         * 向右滑动一个item,且滑动的距离超过了右边View的宽度的一半，隐藏之。
                         */
                        hiddenLeftRight(mCurrentItemView);
                    }

                    return true;
                }
                if (mIsShown != 0) {
                    System.out.println("4---> hiddenRight");
                    /**
                     * 情况四：
                     * <p>
                     * 一个Item的右边布局已经显示，
                     * <p>
                     * 这时候左右滑动当前一个item,那个右边布局显示的item隐藏其右边布局
                     */
                    hiddenLeftRight(mPreItemView);
                }
                break;
        }

        return super.onTouchEvent(ev);
    }

    private void clearPressedState() {
        // TODO current item is still has background, issue
    	if (mCurrentItemView != null){
    		mCurrentItemView.setPressed(false);
    	}
        setPressed(false);
        refreshDrawableState();
        // invalidate();
    }

    private void showRight(View view) {
        System.out.println("=========showRight");
        if (view != null){
	        Message msg = new MoveHandler().obtainMessage();
            msg.obj = view;
            msg.arg1 = view.getScrollX();
            msg.arg2 = mRightViewWidth + mLeftViewWidth;
            msg.sendToTarget();

            mIsShown = 1;
        }
    }
    
    private void showLeft(View view) {
    	if (view != null){
	        System.out.println("=========showLeft");
	
	        Message msg = new MoveHandler().obtainMessage();
	        msg.obj = view;
	        msg.arg1 = view.getScrollX();
	        msg.arg2 = 0;
	        msg.sendToTarget();
	        mIsShown = -1;
    	}
	    
    }
    public boolean canDelete = false;
    
    public void hiddenLeftRight(View view) {
    	System.out.println("=========hiddenRight");
    	if (view != null){
	        
	        if (mCurrentItemView == null) {
	            return;
	        }
	        Message msg = new MoveHandler().obtainMessage();//
	        msg.obj = view;
	        msg.arg1 = view.getScrollX();
	        msg.arg2 = mLeftViewWidth;
	
	        msg.sendToTarget();
	
	        mIsShown = 0;
    	}
    }

    public void hiddenLeftRight() {
        System.out.println("=========hiddenRight");
        if (mCurrentItemView != null) {
            hiddenLeftRight(mCurrentItemView);
        }
    }
    /**
     * show or hide right layout animation
     */
    @SuppressLint("HandlerLeak")
    class MoveHandler extends Handler {
        int stepX = 0;

        int fromX;

        int toX;

        View view;

        private boolean mIsInAnimation = false;

        private void animatioOver() {
            mIsInAnimation = false;
            stepX = 0;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (stepX == 0) {
                if (mIsInAnimation) {
                    return;
                }
                mIsInAnimation = true;
                view = (View)msg.obj;
                fromX = msg.arg1;
                toX = msg.arg2;
                stepX = (int)((toX - fromX) * mDurationStep * 1.0 / mDuration);
                if (stepX < 0 && stepX > -1) {
                    stepX = -1;
                } else if (stepX > 0 && stepX < 1) {
                    stepX = 1;
                }
                if (Math.abs(toX - fromX) < 10) {
                    view.scrollTo(toX, 0);
                    animatioOver();
                    return;
                }
            }

            fromX += stepX;
            boolean isLastStep = (stepX > 0 && fromX > toX) || (stepX < 0 && fromX < toX);
            if (isLastStep) {
                fromX = toX;
            }

            view.scrollTo(fromX, 0);
            invalidate();

            if (!isLastStep) {
                this.sendEmptyMessageDelayed(0, mDurationStep);
            } else {
                animatioOver();
            }
        }
    }

    public int getRightViewWidth() {
        return mRightViewWidth;
    }

    public void setRightViewWidth(int mRightViewWidth) {
        this.mRightViewWidth = mRightViewWidth;
    }
    
    public int getLeftViewWidth() {
        return mLeftViewWidth;
    }

    public void setLeftViewWidth(int mLeftViewWidth) {
        this.mLeftViewWidth = mLeftViewWidth;
    }
    
    public void setDisplayWidth(int displayWidth, int leftWidth, int rightWidth){
    	mLeftViewWidth = leftWidth;
    	mRightViewWidth = rightWidth;
    	mCenterViewWidth = displayWidth;
    }
}
