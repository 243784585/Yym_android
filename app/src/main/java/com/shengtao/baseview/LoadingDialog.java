package com.shengtao.baseview;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.shengtao.R;


/**
 * @copyright：Copyright © 2015 - 2020. All Rights Reserved
 * @author：dane55
 * @email：dane8789@gmail.com
 * @date： 15/9/15 20:13
 * @package： com.motu.baseui
 * @classname： LoadingDialog.java
 * @description： 对LoadingDialog.java类的功能描述
 */
public class LoadingDialog extends Dialog {

    private TextView mTvMessage = null;

    public LoadingDialog(Context context) {
        super(context, R.style.AlertDialogStyle);
        initProgressDialog(context);
    }

    private void initProgressDialog(Context context) {
        setCanceledOnTouchOutside(false);

        View view = View.inflate(context, R.layout.widget_dialog_loading, null);
        setContentView(view);

        mTvMessage = (TextView) view.findViewById(R.id.tv_message);
        mTvMessage.setText("正在加载中...");


        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        params.alpha = 1.0f;
        params.width = -2;
        params.height = -2;
        window.setAttributes(params);
    }

    public void setMessage(String msg) {
        mTvMessage.setText(msg);
    }
}