package com.shengtao.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shengtao.R;

import java.util.HashMap;

/**
 * Created by Y on 2015/5/15.
 */
public class LYDBHeader extends LinearLayout{
    private View view;
    public LinearLayout headerLeft;
    public LinearLayout headerRight;
    private LinearLayout headerCenter;
    private Activity activity;
    private Context context;

    public LYDBHeader(Context context) {
        super(context);
        this.context = context;
        initLayoutInflater();
    }

    public LYDBHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initLayoutInflater();
    }

    private void initLayoutInflater() {
        LayoutInflater lInflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = lInflater.inflate(R.layout.header, this);
        initView();
    }

    private void initView() {
        headerLeft = (LinearLayout)view.findViewById(R.id.header_left);
        headerRight = (LinearLayout)view.findViewById(R.id.header_right);
        headerCenter = (LinearLayout)view.findViewById(R.id.header_center);
    }

    public void setHeaderView(final Activity activity,HashMap<String, Object> header) {
        this.activity = activity;
        if (header.get("left") != null) {
            if(header.get("left") instanceof String) {
                ImageView imageView = new ImageView(context);
                imageView.setImageResource(R.drawable.fanhui);
                LayoutParams layoutParams = new LayoutParams(181, 55);

                layoutParams.setMargins(22, 0, 0, 0);
                imageView.setLayoutParams(layoutParams);
                imageView.setPadding(10, 0, 130, 0);
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.onBackPressed();
                    }
                });
                headerLeft.addView(imageView);
            }else if(header.get("left") instanceof View){
                headerLeft.addView((View) header.get("left"));
            }
        }
        if (header.get("center") != null) {

            if(header.get("center") instanceof String ) {
                TextView textView = new TextView(context);
                textView.setText(header.get("center").toString());
                textView.setTextColor(Color.parseColor("#ff4447"));//"#60646f"
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                headerCenter.addView(textView);
            }else if(header.get("center") instanceof View){
                headerCenter.addView((View)header.get("center"));
            }
        }
        if (header.get("right") != null) {

            if(header.get("right") instanceof Integer){
                ImageView imageView = new ImageView(context);
                imageView.setImageResource(Integer.valueOf(header.get("right").toString()));
                LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, 40);
                layoutParams.setMargins(0,0,20,0);
                imageView.setLayoutParams(layoutParams);
                headerRight.addView(imageView);
            }else if(header.get("right") instanceof String){
                TextView textView = new TextView(context);
                textView.setText(header.get("right").toString());
                textView.setTextColor(Color.parseColor("#60646f"));
                textView.setTextSize(16);
                LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 0, 20, 0);
                textView.setLayoutParams(layoutParams);
                textView.setGravity(Gravity.CENTER_VERTICAL);
                headerRight.addView(textView);
            }else if(header.get("right") instanceof View){
                headerRight.addView((View)header.get("right"));
            }
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            setTranslucentStatus(true);
//        }else{
////            StatusBarCompant
//        }
    }
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);

        setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }


    /**
     * 设置标题栏
     * //@param activity 所在的activity
     * //@param listener 标题栏右边设置用的监听器
     * //@param centerContext 标题栏中心内容
     * @param right 标题栏右边,可选
    public void setActionBar(Activity activity, OnClickListener listener, String centerContext, Object right) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("left", "left");
        hashMap.put("center", centerContext);

        //设置ActionBar右侧事件
        if (right != null) {
            hashMap.put("right", right);
            headerRight.setOnClickListener(listener);
        }
        setHeaderView(activity, hashMap);
    }*/

    public static void expandViewTouchDelegate(final View view, final int top,
                                               final int bottom, final int left, final int right) {

        ((View) view.getParent()).post(new Runnable() {
            @Override
            public void run() {
                Rect bounds = new Rect();
                view.setEnabled(true);
                view.getHitRect(bounds);

                bounds.top -= top;
                bounds.bottom += bottom;
                bounds.left -= left;
                bounds.right += right;

                TouchDelegate touchDelegate = new TouchDelegate(bounds, view);

                if (View.class.isInstance(view.getParent())) {
                    ((View) view.getParent()).setTouchDelegate(touchDelegate);
                }
            }
        });
    }
}
