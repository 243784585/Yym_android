package com.shengtao.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import com.shengtao.baseview.IScrollViewListener;

/**
 * Created by yuchenghao on 15/9/8.
 */
public class LargeListView extends ListView {

    IScrollViewListener scrollViewListener;
    public LargeListView(Context context) {
        super(context);
    }

    public LargeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//    @Override
//    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
//
//        if(scrollViewListener != null){
//            scrollViewListener.onScrollChanged(l, t, oldl, oldt);
//        }
//
//    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
