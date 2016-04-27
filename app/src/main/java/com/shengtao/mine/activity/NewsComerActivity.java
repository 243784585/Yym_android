package com.shengtao.mine.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import com.shengtao.R;
import com.shengtao.baseview.BaseActivity;
import com.shengtao.main.MainActivity;

/**
 * @package com.shengtao.mine.activity
 * Created by TT on 2016/1/16.
 * Description:新手帮助页面
 */
public class NewsComerActivity extends BaseActivity {

//    private TextView tvNewsComer;
//    private int[] image = new int[]{R.drawable.newuser_helpone,R.drawable.newuser_helptwo,R.drawable.newuser_helpthree,R.drawable.newuser_helpfour};
//    private LinearLayout llUserHelp;
    private Button btnSnachNow;

    @Override
    protected int setLayout() {
        return R.layout.activity_newscomer;
    }

    @Override
    protected void initView() {
//        tvNewsComer = (TextView) findViewById(R.id.tv_newscomer);
//        Typeface face = Typeface.createFromAsset(getAssets(), "STHeiti-Light.ttc");
//        tvNewsComer.setTypeface(face);
//        llUserHelp = (LinearLayout) findViewById(R.id.ll_user_help);
        btnSnachNow = (Button) findViewById(R.id.btn_snache_now);
    }

    @Override
    protected String getAvtionTitle() {
        return "新手帮助";
    }

    @Override
    public int getHeaderType() {
        return 1;
    }

    @Override
    protected void doBusiness() {
        btnSnachNow.setOnClickListener(this);
        /*ImageView imageView;
        for(int i=0,len=image.length;i < len ; i++){
            imageView = new ImageView(this);
            imageView.setBackgroundResource(image[i]);
//            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            if(i<3) {
                imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 520));
            }else{
                imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,840));
            }
            llUserHelp.addView(imageView);
        }*/

    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_snache_now){
            startActivity(new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        }
    }
}
