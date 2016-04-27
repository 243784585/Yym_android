package com.shengtao.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hxhuang on 2015/12/25 0025.
 */
public class CommonUtil {

    /**
     * 获取焦点
     * @param view 需要获取焦点的view
     * desc:方法内必须三个一起调用才行。
     */
    public static void getFocus(View view){
        view.requestFocus();
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
    }

    /** 判断字符串是否为空 */
    public static boolean isEmpty(String target){
        return target == null || target.length() == 0 || "null".equals(target);
    }

    /**弹出软键盘(延时100毫秒,等待页面加载完成)*/
    public static void showSoftInput(final EditText editText){
        new Timer().schedule(new TimerTask()
                       {

                           public void run()
                           {
                               InputMethodManager inputManager =
                                       (InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                               inputManager.showSoftInput(editText, 0);
                           }

                       },
                100);
    }
    /**弹出软键盘(无需等待页面加载,可立即弹出)*/
    public static void showSoft(EditText editText){
        InputMethodManager inputManager =
                (InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
    }

    /**隐藏软键盘*/
    public static void hideSoft(View view){
        ((InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(),InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

}
