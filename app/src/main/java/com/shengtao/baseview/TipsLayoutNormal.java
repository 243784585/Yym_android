package com.shengtao.baseview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shengtao.R;
import com.shengtao.utils.StringUtil;

/**
 * @copyright：Copyright © 2015 - 2020. All Rights Reserved
 * @author：dane55
 * @email：dane8789@gmail.com
 * @date： 15/9/17 09:24
 * @package： com.motu.baseview
 * @classname： TipsLayoutNormal.java
 * @description： 对TipsLayoutNormal.java类的功能描述
 */
public class TipsLayoutNormal extends RelativeLayout implements ITipLayout {
    /**
     * 显示正在加载提示
     */
    public static final int TIPS_STATUS_LOAD_ING = 1;

    /**
     * 显示失败提示，加载失败
     */
    public static final int TIPS_STATUS_LOAD_FAILED = 2;

    /**
     * 显示自定义提示界面
     */
    public static final int TIPS_STATUS_TYPE_CUSTOM_VIEW = 3;

    /**
     * 显示失败提示，无网络
     */
    public static final int TIPS_STATUS_LOAD_FAILED_NET_ERROR = 4;

    /**
     * 无数据
     */
    public static final int TIPS_STATUS_LOAD_SUCCESS_NO_DATA = 5;

    /**
     * 加载成功
     */
    public static final int TIPS_STATUS_LOAD_SUCCESS = 6;

    /**
     * 异步弹出提示
     */
    private CustomDialog mLoadingView;
    private View mLoadFaileView;
    /**
     * 图片
     */
    private ImageView mFailedIv;
    /**
     * 提示值
     */
    private TextView mFailedTv;
    /**
     * 按钮
     */
    private Button mBtnEmpty;
    private RelativeLayout mLayoutLoadFaile;
    private Context mContext;
    public ITipsLayoutListener mITipsLayoutListener;
    private int mShowType = 0;

    public void setITipsLayoutListener(ITipsLayoutListener listener) {
        mITipsLayoutListener = listener;
    }

    public TipsLayoutNormal(Context context) {
        this(context, null);
        this.mContext = context;
    }

    public TipsLayoutNormal(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.mContext = context;
    }

    public TipsLayoutNormal(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        LayoutInflater inflater = LayoutInflater.from(context);

        mLoadingView = new CustomDialog(mContext);
        mLoadFaileView = inflater.inflate(R.layout.widget_tip_load_failed, null);

        LayoutParams faillp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        faillp.addRule(RelativeLayout.CENTER_IN_PARENT);

        mLayoutLoadFaile = (RelativeLayout) mLoadFaileView.findViewById(R.id.layout_load_faile);
        mLayoutLoadFaile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mITipsLayoutListener != null && mFailedTv.getVisibility() == View.VISIBLE && mShowType == TIPS_STATUS_LOAD_FAILED)
                    mITipsLayoutListener.onTipLayoutClick(R.id.layout_load_faile);
            }
        });

        mFailedIv = (ImageView) mLoadFaileView.findViewById(R.id.img_load_faile);
        mFailedTv = (TextView) mLoadFaileView.findViewById(R.id.tv_loading_wrong);
        mBtnEmpty = (Button) mLoadFaileView.findViewById(R.id.btn_empty);
        mBtnEmpty.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mITipsLayoutListener != null)
                    mITipsLayoutListener.onTipLayoutClick(R.id.btn_empty);
            }
        });

        addView(mLoadFaileView, faillp);
    }

    public void show(int showType) {
        show(showType, this.mContext.getResources().getString(R.string.common_loading_no_data));
    }

    public void show(int showType, String noData) {
        show(showType, noData, "");
    }

    public void show(int showType, String noData, String btnNodata) {
        this.mShowType = showType;
        //设置TipsLayout为显示状态
        setVisibility(View.VISIBLE);
        //设置TipsLayout的子View为隐藏状态
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setVisibility(View.GONE);
        }
        //
        switch (showType) {
            case TIPS_STATUS_LOAD_ING:
                // 加载
                this.mLoadingView.show();
                break;
            case TIPS_STATUS_LOAD_FAILED:
                // 加载失败
                this.mLoadFaileView.setVisibility(View.VISIBLE);
                this.mLoadingView.cancel();
                this.mFailedIv.setVisibility(View.VISIBLE);
                this.mFailedTv.setVisibility(View.VISIBLE);
                this.mBtnEmpty.setVisibility(View.GONE);
                this.mFailedTv.setText(mContext.getResources().getString(R.string.common_loading_click_refresh));
                break;
            case TIPS_STATUS_TYPE_CUSTOM_VIEW:
                // 显示自定义页面
                if (getChildCount() == 2) {
                    getChildAt(1).setVisibility(View.VISIBLE);
                }
                this.mLoadingView.cancel();
                break;
            case TIPS_STATUS_LOAD_FAILED_NET_ERROR:
                // 无网络加载失败
                this.mLoadFaileView.setVisibility(View.VISIBLE);
                this.mLoadingView.cancel();
                this.mFailedIv.setVisibility(View.VISIBLE);
                this.mFailedTv.setVisibility(View.VISIBLE);
                this.mBtnEmpty.setVisibility(View.GONE);
                this.mFailedTv.setText(this.mContext.getResources().getString(R.string.common_text_error_net));
                break;
            case TIPS_STATUS_LOAD_SUCCESS_NO_DATA:
                // 加载完成，但是没有数据
                this.mLoadFaileView.setVisibility(View.VISIBLE);
                this.mLoadingView.cancel();
                this.mFailedIv.setVisibility(View.VISIBLE);
                this.mFailedTv.setVisibility(View.VISIBLE);
                if (!StringUtil.isEmpty(btnNodata)) {
                    this.mBtnEmpty.setVisibility(View.VISIBLE);
                    this.mBtnEmpty.setText(btnNodata);
                }
                this.mFailedTv.setText(noData);
                break;
        }
    }

    /**
     * 隐藏提示界面
     */
    public void hide() {
        this.setVisibility(View.GONE);
        this.mLoadingView.cancel();
    }

    /**
     * 设置自定义提示View
     *
     * @param customTipsView
     */
    public void setCustomView(View customTipsView) {
        if (getChildCount() == 1) {
            LayoutParams rlp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            rlp.addRule(RelativeLayout.CENTER_IN_PARENT);

            addView(customTipsView, rlp);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }
}
