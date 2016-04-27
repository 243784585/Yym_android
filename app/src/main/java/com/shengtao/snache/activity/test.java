package com.shengtao.snache.activity;

import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.shengtao.R;
import com.shengtao.baseview.BaseActivity;

/**
 * @package com.shengtao.snache.activity
 * Created by TT on 2015/12/27.
 * Description:
 */
public class test extends BaseActivity{

    private int recLen = 0;
    private TextView txtView;
    private Handler handler;
    private Runnable runnable;
    private MyCountDownTimer mc;

    @Override
    protected int setLayout() {
        return R.layout.test;
    }

    @Override
    protected void initView() {
//        txtView = (TextView)findViewById(R.id.tv_test);
//        mc = new CountDownTimerUtil(30000, 1);
//        mc.start();
    }



    @Override
    protected void doBusiness() {

    }

//    private void initUI(){
//        LayoutInflater.from(context).inflate(R.layout.layout_slideadshow, this, true);
//        for(int i=0;i
//        ImageView view =  new ImageView(context);
//        view.setScaleType(ScaleType.FIT_XY);
//        ImageLoader.getInstance().displayImage(
//                imageAd.get(i).imgUrl,
//                view, options);
//        view.setTag(imageAd.get(i).ClassId);
//        final String className=imageAd.get(i).className;
//        final int classId=imageAd.get(i).ClassId;
//        final String imgU=imageAd.get(i).imgUrl;
//        final int cStep=i;
//        view.setOnClickListener(new OnClickListener(){
//
//            @Override
//            public void onClick(View arg0) {
//                Intent intent=new Intent(context,PromotionActivity.class);
//                intent.putExtra("actid", classId);
//                intent.putExtra("ctitle", className);
//                intent.putExtra("cUrl", imgU);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
//
//            }});
//        imageViewsList.add(view);
//    }
//    dotViewsList.add(findViewById(R.id.v_dot1));
//    dotViewsList.add(findViewById(R.id.v_dot2));
//    dotViewsList.add(findViewById(R.id.v_dot3));
//
//    viewPager = (ViewPager) findViewById(R.id.viewPager);
//    viewPager.setFocusable(true);
//
//    viewPager.setAdapter(new MyPagerAdapter());
//    viewPager.setOnPageChangeListener(new MyPageChangeListener());
//    if(isAutoPlay){
//        startPlay();
//    }
//}

    @Override
    public void onClick(View view) {

    }

    class MyCountDownTimer extends CountDownTimer {
        /**
         *
         * @param millisInFuture
         *            表示以毫秒为单位 倒计时的总数
         *
         *            例如 millisInFuture=1000 表示1秒
         *
         * @param countDownInterval
         *            表示 间隔 多少微秒 调用一次 onTick 方法
         *
         *            例如: countDownInterval =1000 ; 表示每1000毫秒调用一次onTick()
         *
         */
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            txtView.setText("done");
        }

        @Override
        public void onTick(long millisUntilFinished) {
            txtView.setText("倒计时(" +millisUntilFinished/1000/60+"分" + millisUntilFinished/1000+"秒"+millisUntilFinished);
        }
    }




}
