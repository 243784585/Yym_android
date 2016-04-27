package com.shengtao.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shengtao.R;


/**
 * 分享
 * Created by admin on 15/9/13.
 */
public class ShareDialog extends Dialog {

    private ImageView iv_close;
    private LinearLayout ll_wechat, ll_wechat_friend, ll_qq, ll_qzone, ll_sina;

    private Context mContext;
    private View.OnClickListener mListener;

    public ShareDialog(Context context, View.OnClickListener listener) {
        super(context, R.style.Alert_Dialog_Style);
        mListener = listener;
        mContext = context;
        init(context);
    }

    private void init(Context context) {
        setCanceledOnTouchOutside(false);

        View view = View.inflate(context,
                R.layout.widget_travel_share, null);
        setContentView(view);
        init(view);
    }

    private void init(View view) {
        iv_close = (ImageView) view.findViewById(R.id.iv_close);
        ll_wechat = (LinearLayout) view.findViewById(R.id.ll_wechat);
        ll_wechat_friend = (LinearLayout) view.findViewById(R.id.ll_wechat_friend);
        ll_qq = (LinearLayout) view.findViewById(R.id.ll_qq);
        ll_qzone = (LinearLayout) view.findViewById(R.id.ll_qzone);
        ll_sina = (LinearLayout) view.findViewById(R.id.ll_sina);

        iv_close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                dismiss();
                if (mListener != null)
                    mListener.onClick(arg0);

            }
        });
        ll_wechat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                dismiss();
                if (mListener != null)
                    mListener.onClick(arg0);

            }
        });
        ll_wechat_friend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                dismiss();
                if (mListener != null)
                    mListener.onClick(arg0);

            }
        });
        ll_qq.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                dismiss();
                if (mListener != null)
                    mListener.onClick(arg0);

            }
        });
        ll_qzone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                dismiss();
                if (mListener != null)
                    mListener.onClick(arg0);

            }
        });
        ll_sina.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                dismiss();
                if (mListener != null)
                    mListener.onClick(arg0);

            }
        });

        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.alpha = 1.0f;
        params.width = window.getWindowManager().getDefaultDisplay().getWidth();
        window.setAttributes(params);
    }


}
