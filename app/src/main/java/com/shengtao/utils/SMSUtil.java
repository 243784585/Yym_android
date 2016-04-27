package com.shengtao.utils;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shengtao.domain.Config;
import com.shengtao.foundation.AsyncHttpTask;


import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by hxhuang on 2015/12/24 0024.
 * 短信验证码工具类
 */
public class SMSUtil {

    /**     +++++++第一步+++++++
     * 获取短信验证码
     * @param phone 收取验证码的手机号
     * @param tvCountdown 倒计时的view
     * return 倒计时对象,可以停止倒计时
     */
    public static CountDownTimer getVerificationCode(String phone,final TextView tvCountdown){
        AsyncHttpTask.post(Config.HTTP_URL_HEAD + "goods/text", new RequestParams("mobile",phone),new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if(0 == response.optInt("code")){
                    //获取验证码成功
                    SessionSms.SetString("verifyCode",response.optString("info"));
                    ToastUtil.showTextToast("验证码已经发送");
                }else if(8 == response.optInt("code")){
                    ToastUtil.showTextToast("验证码获取失败,请稍后重新获取...");
                }
            }
        });
        tvCountdown.setClickable(false);
        tvCountdown.setTextColor(Color.parseColor("#aaaaaa"));
        return new CountDownTimer(60000,1000){
            @Override
            public void onTick(long l) {
                tvCountdown.setText(l/1000 + "秒后重新获取");
            }

            @Override
            public void onFinish() {
                tvCountdown.setTextColor(Color.parseColor("#ff4447"));
                tvCountdown.setText("获取验证码");
                tvCountdown.setClickable(true);
            }
        }.start();//倒计时
    }

    /**    +++++++第二步+++++++
     * 提交用户输入的验证码
     * @param phone 收取验证码的手机号
     * @param verificationCode 用户输入的验证码
     * @param handler 验证码正确后发回调message
     */
    public static void submitVerificationCode(String phone,String verificationCode,Handler handler){
        if(SessionSms.GetString("verifyPhone").equals(phone)){
            if(verificationCode.equals(SessionSms.GetString("verifyCode"))){
                //验证码正确
                handler.sendEmptyMessage(0);
            }else{
                ToastUtil.showTextToast("验证码错误!!!");
            }
        }else{
            ToastUtil.showTextToast("验证码错误!!!");
        }
    }

    /**
     * 验证同一手机号获取验证码频率
     * @param phone 将获取验证码的手机号
     * @return 太频繁返回false,否则返回true
     */
    public static boolean doVerifyTime(String phone){
        boolean flag = false;

        String verifyPhone = SessionSms.GetString("verifyPhone", "");

        if("".equals(verifyPhone)){
            //当前无手机号
            SessionSms.SetString("verifyPhone",phone);
            SessionSms.SetString("curTime", Long.valueOf(System.currentTimeMillis()));
            flag = true;
        }else{
            //有手机号,判断是否是同一手机号
            if(verifyPhone.equals(phone)){
                //同一手机号
                long preTime = Long.parseLong(SessionSms.GetString("curTime"));
                if(System.currentTimeMillis() - preTime < 60000){
                    //小于1分钟
                    ToastUtil.showTextToast("请求验证码太频繁,请稍后再试");
                }else{
                    //大于1分钟
                    flag = true;
                    SessionSms.SetString("curTime",Long.valueOf(System.currentTimeMillis()));
                }

            }else{
                //不同手机号
                SessionSms.SetString("verifyPhone", phone);
                SessionSms.SetString("curTime", Long.valueOf(System.currentTimeMillis()));
                flag = true;
            }
        }
        return flag;
    }
}
