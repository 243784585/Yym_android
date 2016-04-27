package com.shengtao.utils;

import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;

import com.shengtao.R;
import com.shengtao.login.activity.LoginActivity;
import com.shengtao.mine.activity.ReceiverAddressActivity;

/**
 * @package com.shengtao.utils
 * Created by TT on 2016/1/15.
 * Description:游客模式
 */
public class VisitorMode {

    /**
     * 用来判断用户是否登录
     */
    public static Boolean isVistor(Context context){
        if("".equals(Session.GetString("token"))){
            ToastUtil.showTextToast("快来完成圆梦之旅吧...");
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
            return true;
        }else{
            return false;
        }
    }

    public static void alertToAddress(Context context){
        new CommonDialog(context) {
            @Override
            protected void afterConfirm() {
                context.startActivity(new Intent(context, ReceiverAddressActivity.class));
                dismiss();
            }
        }.setTitle("请先设置收货地址", R.color.black, 16f, TypedValue.COMPLEX_UNIT_SP)
                .setSubTitle("首次设置收货地址可获得1抢币", R.color.black, 11f, TypedValue.COMPLEX_UNIT_SP)
                .setButtonStyle(R.color.dialog_btn_color, 15f, TypedValue.COMPLEX_UNIT_SP)
                .setCancle("取消")
                .setConfirm("去设置")
                .show();
    }
}
