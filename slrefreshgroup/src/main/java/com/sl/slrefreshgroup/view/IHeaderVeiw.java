package com.sl.slrefreshgroup.view;

/**
 * Created by dell on 2017/12/15.
 */

public interface IHeaderVeiw {
    void startAnimate();
    void stopAnimate();

   void onpull(int scrollY);

    void onReleaseGesture();

    void onFinishRefresh();
}
