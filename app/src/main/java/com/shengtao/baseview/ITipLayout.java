package com.shengtao.baseview;

import android.view.MotionEvent;
import android.view.View;

/**
 * @copyright：Copyright © 2015 - 2020. All Rights Reserved
 * @author：dane55
 * @email：dane8789@gmail.com
 * @date： 15/9/17 09:34
 * @package： com.motu.baseview
 * @classname： ITipLayout.java
 * @description： 对ITipLayout.java类的功能描述，加载提示接口
 */
public interface ITipLayout {

    public void setITipsLayoutListener(ITipsLayoutListener listener);

    public void show(int showType);

    public void show(int showType, String noData);

    public void show(int showType, String noData, String btnNodata);

    public void hide();

    public void setCustomView(View customTipsView);

    public boolean onTouchEvent(MotionEvent event);
}
