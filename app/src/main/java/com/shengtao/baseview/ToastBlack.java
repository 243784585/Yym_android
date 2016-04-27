package com.shengtao.baseview;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.shengtao.R;
import com.shengtao.foundation.IToastBlock;
import com.shengtao.utils.ScreenUtil;

/**
 * @copyright：Copyright © 2015 - 2020. All Rights Reserved
 * @author：dane55
 * @email：dane8789@gmail.com
 * @date： 15/9/16 12:08
 * @package： com.motu.baseui
 * @classname： ToastBlack.java
 * @description： 对ToastBlack.java类的功能描述
 */
public class ToastBlack extends Toast implements IToastBlock {

    private static long sToastTime = 0;
    private static String sToastContent = "";

    public ToastBlack(Context context) {
        super(context);
    }

    @Override
    public void showText(Context context, String text, boolean isLong) {
        long curTime = System.currentTimeMillis();
        if (curTime - sToastTime < 2 * 1000 && sToastContent.equals(text)) {
            sToastTime = System.currentTimeMillis();
            return;
        }
        sToastTime = System.currentTimeMillis();
        sToastContent = text;

        Toast result = new Toast(context);
        // 获取LayoutInflater对象
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // 由layout文件创建一个View对象
        View layout = inflater.inflate(R.layout.widget_toast_black, null);

        TextView textView = (TextView) layout.findViewById(R.id.tv_title);

        textView.setText(text);

        result.setView(layout);
        result.setGravity(Gravity.BOTTOM, 0, ScreenUtil.dip2px(20));
        if (isLong)
            result.setDuration(Toast.LENGTH_LONG);
        else
            result.setDuration(Toast.LENGTH_SHORT);

        result.show();
    }

}
