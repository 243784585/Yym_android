package com.shengtao.foundation;

import android.content.Context;

/**
 * @copyright：Copyright © 2015 - 2020. All Rights Reserved
 * @author：dane55
 * @email：dane8789@gmail.com
 * @date： 15/9/15 18:21
 * @package： com.foudation.framework.base
 * @classname： IToastBlock.java
 * @description： 对IToastBlack.java类的功能描述;
 */
public interface IToastBlock {
    /**
     * 显示一个Toast类型的消息
     *
     * @param context
     * @param text
     * @param isLong
     */
    void showText(Context context, String text, boolean isLong);
}
