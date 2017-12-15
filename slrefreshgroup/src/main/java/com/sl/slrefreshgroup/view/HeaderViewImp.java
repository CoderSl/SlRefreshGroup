package com.sl.slrefreshgroup.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


import com.sl.slrefreshgroup.R;

/**
 * Created by dell on 2017/12/15.
 */

public class HeaderViewImp extends HeaderView {

    private View headerView;
    private TextView contentTv;

    public HeaderViewImp(@NonNull Context context) {
        this(context,null);
    }

    public HeaderViewImp(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HeaderViewImp(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
         headerView = LayoutInflater.from(getContext()).inflate(R.layout.view_header, null);
         contentTv = headerView.findViewById(R.id.content_tv);
        addView(headerView);
    }

    @Override
    public void startAnimate() {

    }

    @Override
    public void stopAnimate() {

    }

    @Override
    public void onpull(int scrollY) {
        if(Math.abs(scrollY)>getHeight()){
            contentTv.setText("释放刷新");
        }else{
            contentTv.setText("拉动刷新");
        }

    }


    @Override
    public void onReleaseGesture() {
        contentTv.setText("刷新中...");
    }

    @Override
    public void onFinishRefresh() {
        contentTv.setText("成功");
    }
}
