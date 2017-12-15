package com.sl.slrefreshgroup.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * Created by dell on 2017/12/15.
 */

public class SlRefreshGroup extends ViewGroup implements IRefreshView {

    private HeaderView mHeaderVeiw;
    private IFooterView mFooterView;
    private View mContentView;
    private float mLastY;
    private float mDownY;
    private float mMoveY;


    private Scroller mScroller;
    private boolean canScroll;

    private boolean isRefreshing=false;
    private boolean overLineOnced;

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    private boolean mCanPullDown=true;

    private OnRefreshListener onRefreshListener;

    public SlRefreshGroup(Context context) {
       super(context);
        init();
    }

    public SlRefreshGroup(Context context, AttributeSet attrs) {
      super(context,attrs);
        init();
    }

    public SlRefreshGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setmHeaderVeiw(HeaderView mHeaderVeiw) {
        if(this.mHeaderVeiw!=null){
            removeView(this.mHeaderVeiw);
        }
        this.mHeaderVeiw = mHeaderVeiw;
        LayoutParams layoutParams = new LayoutParams(-1, -2);
        addView( mHeaderVeiw,0,layoutParams);
    }

    private void init() {
        mScroller=new Scroller(getContext());
        mHeaderVeiw=new HeaderViewImp(getContext());
        LayoutParams layoutParams = new LayoutParams(-1, -2);
        addView( mHeaderVeiw,0,layoutParams);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            measureChild(getChildAt(i),widthMeasureSpec,heightMeasureSpec);
        }

    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if(childAt instanceof IHeaderVeiw){
                childAt.layout(left,-childAt.getMeasuredHeight(),right,0);
            }else{
                childAt.layout(left,top,right,bottom);
            }
        }

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        boolean isIntercept=false;
        int childCount = getChildCount();
        if(childCount>3){
            throw new IllegalStateException("只能包含一个内容view!");
        }

        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if(!(childAt instanceof  IHeaderVeiw)  && !(childAt instanceof IFooterView) ){
                mContentView=childAt;
            }
        }


        int action = ev.getAction();
        switch(action){
            case MotionEvent.ACTION_DOWN:
                 mDownY = ev.getY();
                mLastY=mDownY;
                break;
            case MotionEvent.ACTION_MOVE:

            
                 mMoveY = ev.getY();
                float dval = mMoveY - mLastY;
                if(dval>0&&mCanPullDown){
                    if(mContentView==null||!mContentView.canScrollVertically(-1)){
                        isIntercept=true;
                        break;
                    } 
                }
               
                mLastY=mMoveY;

                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                break;

        }
        return isIntercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch(action){
            case MotionEvent.ACTION_DOWN:
                mDownY = event.getY();
                mLastY=mDownY;

                break;
            case MotionEvent.ACTION_MOVE:
                canScroll=false;
                mMoveY = event.getY();
                float dval = mMoveY - mLastY;
                if(dval>0){
                    pullDown(dval);
                }
                mLastY=mMoveY;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                canScroll=true;

                    if(Math.abs(getScrollY())>=mHeaderVeiw.getHeight()){
                        mScroller.startScroll(0,getScrollY(),0,-(getScrollY()+mHeaderVeiw.getHeight()),1000);
                        if(!isRefreshing){
                            mHeaderVeiw.onReleaseGesture();
                        }
                        if(onRefreshListener!=null&&!isRefreshing){
                            onRefreshListener.onRefresh();
                            isRefreshing=true;
                        }

                    }else{
                        mScroller.startScroll(0,getScrollY(),0,-getScrollY(),1000);
                    }
                invalidate();
                break;

        }
        return true;
    }

    private  void pullDown(float dval) {
        mHeaderVeiw.startAnimate();

        if(!isRefreshing){
            mHeaderVeiw.onpull(getScrollY());
        }

        if(Math.abs(getScrollY())<mHeaderVeiw.getHeight()){
            scrollBy(0, (int)- (dval));
        }else{
            if(Math.abs(getScrollY())<mHeaderVeiw.getHeight()*2){
                scrollBy(0, (int)-(Math.abs(dval*0.5)) );
            }

        }
    }


    @Override
    public void setCanPullUp(boolean canPullDown) {
        this.mCanPullDown=canPullDown;
    }

    @Override
    public void finishRefresh( ) {
        mHeaderVeiw.stopAnimate();
        mHeaderVeiw.onFinishRefresh();
        isRefreshing=false;
        mScroller.startScroll(0,getScrollY(),0,-getScrollY());
        invalidate();
    }



    @Override
    public void computeScroll() {

        if (mScroller.computeScrollOffset()&&canScroll) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }


}
