package com.shengtao.baseview;

/**
 * Created by yuchenghao on 15/9/18.
 */
public interface IScrollViewListener {

    void onScrollChanged(ObservableScrollView scrollView, int x, int y,
                         int oldx, int oldy);
}
