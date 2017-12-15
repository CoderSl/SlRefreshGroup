package com.sl.slrefreshgroup.view;

/**
 * Created by dell on 2017/12/15.
 */

public interface IRefreshView {
    void setCanPullUp(boolean canPullDown);
    void  finishRefresh( );
    void setOnRefreshListener(OnRefreshListener onRefreshListener);

     interface  OnRefreshListener{
         void onRefresh();
    }
}
