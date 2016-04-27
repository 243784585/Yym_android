package com.shengtao.main;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shengtao.R;
import com.shengtao.adapter.snache.ViewPagerAdapter;
import com.shengtao.domain.Config;
import com.shengtao.foundation.AsyncHttpTask;
import com.shengtao.foundation.JsonHttpResponse;
import com.shengtao.login.activity.LoginActivity;
import com.shengtao.utils.BezierAnimation;
import com.shengtao.utils.CircleImageView;
import com.shengtao.utils.LogUtil;
import com.shengtao.utils.NoScrollViewPager;
import com.shengtao.utils.Session;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private NoScrollViewPager mViewPager;
    private ArrayList<Fragment> fragmentList;
    private int currIndex;//当前页卡编号
    private LinearLayout ll_main_snatch, ll_main_publish, ll_main_discover, ll_main_shopping, ll_main_mine;
    private ImageView tab_image_snatch, tab_image_publish, tab_image_discover, tab_image_shopping, tab_image_mine;
    private TextView tab_txt_snatch, tab_txt_publish, tab_txt_discover, tab_txt_shopping, tab_txt_mine;
    private int bmpW;//横线图片宽度
    private int offset;//图片移动的偏移量
    private DrawerLayout dl_search;
    private ActionBarDrawerToggle drawerbar;
    private RelativeLayout left_menu_layout;
    private ListView lv_main_left;
    private ArrayList<String> mList;
    public static Handler mHandler;

    //下线通知
    public static Handler mHandler1;

    //处理购物车动画效果
    public static Handler mHandler2;
    public static Handler mHandler3;
    private CircleImageView iv_shopping;
    private TextView tv_shopping;
    private FrameLayout flAnimCotainer;//贝塞尔曲线动画容器
    private Animation animFadein;
    private Animation out;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //改变状态栏背景
        // Set a toolbar to replace the action bar.
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitleTextColor(Color.WHITE);


//        getWindow().setStatusBarColor(Color.WHITE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }

        mViewPager = (NoScrollViewPager) findViewById(R.id.vp_main_content);
        mViewPager.setOffscreenPageLimit(5);
        this.initMainContent();
        this.initView();

        //处理程序退出
        mHandler1 = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                MainActivity.this.finish();
            }
        };

        //处理选中最新揭晓和购物车
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        setState(R.id.ll_main_publish);
                        //设置选中颜色
                        mViewPager.setCurrentItem(1,false);
                        break;
                    case 2:
                        setState(R.id.ll_main_shopping);
                        //设置选中颜色
                        mViewPager.setCurrentItem(3,false);
                        break;
                    case 3:
                        setState(R.id.ll_main_snatch);
                        //设置选中颜色
                        mViewPager.setCurrentItem(0,false);
                        break;
                }
            }
        };

        //用来处理添加购物车操作
        mHandler2 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        //红点显示
                        tv_shopping.setText(Session.GetInt("cartnum") + "");
                        if(Session.GetInt("cartnum")>0){
                            tv_shopping.setVisibility(View.VISIBLE);
                            iv_shopping.setVisibility(View.VISIBLE);
                        }else{
                            tv_shopping.setVisibility(View.INVISIBLE);
                            iv_shopping.setVisibility(View.INVISIBLE);
                        }
                        // 加载动画
//                        ObjectAnimator//
//                                .ofFloat(tab_image_shopping, "pivotX", 50.0 %, "pivotY", 50.0 %)//
//                                .setDuration(500)//
//                                .start();
//                        final Animator anim = AnimatorInflater.loadAnimator(getApplicationContext(), R.anim.shopcart_icon);
//                        anim.setTarget(tab_image_shopping);
                        if(msg.obj instanceof View)
                        new BezierAnimation(flAnimCotainer,getLayoutInflater().inflate(R.layout.cart_anim,flAnimCotainer,false),(View)msg.obj){
                            @Override
                            protected List<Animator> getAnimators() {
                                ArrayList<Animator> animators = new ArrayList<>();
                                animators.add(ObjectAnimator.ofFloat(tab_image_shopping,"scaleX",1.0f,1.2f).setDuration(300));
                                animators.add(ObjectAnimator.ofFloat(tab_image_shopping,"scaleX",1.2f,1.0f).setDuration(300));
                                return animators;
                            }

                            @Override
                            protected View getEndView() {
                                return iv_shopping;
                            }
                        }.showAnimation();
                        break;
                    case 2:
                        tv_shopping.setText(Session.GetInt("cartnum") + "");
                        if(Session.GetInt("cartnum")>0){
                            tv_shopping.setVisibility(View.VISIBLE);
                            iv_shopping.setVisibility(View.VISIBLE);
                        }else{
                            tv_shopping.setVisibility(View.GONE);
                            iv_shopping.setVisibility(View.GONE);
                        }
                        break;
                    default:
                        break;
                }
            }
        };
    }

    /**
     * 加载Fragment内容
     */
    private void initMainContent() {

        fragmentList = new ArrayList<Fragment>();
        Fragment snatchFragment = SnacheFragment2.newInstance("");
        Fragment discoverFragment = DiscoverFragment.newInstance("this is a discoverFragment");
        Fragment shoppingFragment = ShoppingFragment.newInstance("this is a shoppingFragment");
        Fragment mineFragment = MineFragment.newInstance("this is a mineFragment");
        Fragment publishFragment = PublishFragment.newInstance("this is a publishFragment");

        fragmentList.add(snatchFragment);
        fragmentList.add(publishFragment);
        fragmentList.add(discoverFragment);
        fragmentList.add(shoppingFragment);
        fragmentList.add(mineFragment);
        //给ViewPager设置适配器
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());//页面变化时的监听器
    }

    //初始化资源id
    private void initView() {
        //获取token
        LogUtil.e("token", Session.GetString("token"));

        ll_main_snatch = (LinearLayout) findViewById(R.id.ll_main_snatch);
        ll_main_publish = (LinearLayout) findViewById(R.id.ll_main_publish);
        ll_main_discover = (LinearLayout) findViewById(R.id.ll_main_discover);
        ll_main_shopping = (LinearLayout) findViewById(R.id.ll_main_shopping);
        ll_main_mine = (LinearLayout) findViewById(R.id.ll_main_mine);

        ll_main_snatch.setOnClickListener(new LinearLayoutClickListener(this.ll_main_snatch, 0));
        ll_main_publish.setOnClickListener(new LinearLayoutClickListener(this.ll_main_publish, 1));
        ll_main_discover.setOnClickListener(new LinearLayoutClickListener(this.ll_main_discover, 2));
        ll_main_shopping.setOnClickListener(new LinearLayoutClickListener(this.ll_main_shopping, 3));
        ll_main_mine.setOnClickListener(new LinearLayoutClickListener(this.ll_main_mine, 4));

        tab_image_snatch = (ImageView) findViewById(R.id.tab_image_snatch);
        tab_image_publish = (ImageView) findViewById(R.id.tab_image_publish);
        tab_image_discover = (ImageView) findViewById(R.id.tab_image_discover);
        tab_image_shopping = (ImageView) findViewById(R.id.tab_image_shopping);
        tab_image_mine = (ImageView) findViewById(R.id.tab_image_mine);

        tab_txt_snatch = (TextView) findViewById(R.id.tab_txt_snatch);
        tab_txt_publish = (TextView) findViewById(R.id.tab_txt_publish);
        tab_txt_discover = (TextView) findViewById(R.id.tab_txt_discover);
        tab_txt_shopping = (TextView) findViewById(R.id.tab_txt_shopping);
        tab_txt_mine = (TextView) findViewById(R.id.tab_txt_mine);

        //购物车数量
        iv_shopping = (CircleImageView) findViewById(R.id.iv_shopping);
        tv_shopping = (TextView) findViewById(R.id.tv_shopping);
        flAnimCotainer = (FrameLayout) findViewById(R.id.fl_animcotainer);
        setState(R.id.ll_main_snatch);
    }

    private class LinearLayoutClickListener implements View.OnClickListener {

        private int index = 0;
        private LinearLayout sender;

        public LinearLayoutClickListener(LinearLayout target, int i) {
            index = i;
            sender = target;
        }

        @Override
        public void onClick(View view) {
            if(view.equals(ll_main_mine)){
                String token = Session.GetString("token");
                //如果没有token,即没有登录
                if (token == null || "".equals(token)) {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    return;
                }
                //获取抢币数据
                AsyncHttpTask.get(Config.HTTP_MODULE_MINE + "user/rmbInfo", new JsonHttpResponse() {
                    @Override
                    protected void success(Header[] headers, JSONObject json) {
                        Session.SetString("rmb", json.optJSONObject("info").optString("rmb"));
                        for(Fragment fment : (List<Fragment>)MainActivity.this.getSupportFragmentManager().getFragments()){
                            if(fment instanceof MineFragment){
                                ((MineFragment) fment).mHandler.sendEmptyMessage(3);
                            }
                        }
                    }
                });
                //是否有未读的优惠券
                AsyncHttpTask.get(Config.HTTP_MODULE_MINE + "user/isNew", new JsonHttpResponse() {
                    @Override
                    protected void success(Header[] headers, JSONObject json) {
                        if ("0".equals(json.optString("code"))) {
                            Session.SetString("hasCoupon", json.optString("info"));
                            for (Fragment fment : (List<Fragment>) MainActivity.this.getSupportFragmentManager().getFragments()) {
                                if (fment instanceof MineFragment) {
                                    if(!"0".equals(json.optInt("info"))){
                                        ((MineFragment) fment).mHandler.sendEmptyMessage(9);
                                    }
                                }
                            }
                        }
                    }
                });
            }
            if(view.equals(ll_main_shopping)){
                sendMessageToFrame(1);
            }
            if(view.equals(ll_main_publish)){
                for (Fragment fment : (List<Fragment>) MainActivity.this.getSupportFragmentManager().getFragments()) {
                    if (fment instanceof PublishFragment) {
                        ((PublishFragment) fment).handler.sendEmptyMessage(0);
                    }
                }
            }
            if(view.equals(ll_main_snatch)){
                for (Fragment fment : (List<Fragment>) MainActivity.this.getSupportFragmentManager().getFragments()) {
                    if (fment instanceof SnacheFragment2) {
                        ((SnacheFragment2) fment).handler2.sendEmptyMessage(0);
                    }
                }
            }

            setState(view.getId());
            //设置选中颜色
            mViewPager.setCurrentItem(index, false);
        }
    }

    /**
     * 传递消息给子fragment
     * @param what   发送哪个handler消息
     */
    private void sendMessageToFrame(int what){
        ShoppingFragment fragment = null;
        List<Fragment> list = MainActivity.this.getSupportFragmentManager().getFragments();
        for(Fragment fment : list){
            if(fment instanceof ShoppingFragment){
                fragment = (ShoppingFragment) fment;
            }
        }
        Handler handler = fragment.getHandler();
        handler.sendEmptyMessage(what);
    }

    private void setState(int id) {
        switch (id) {
            case R.id.ll_main_snatch: //选择夺宝

                this.tab_image_snatch.setBackgroundResource(R.drawable.duobao_select);
                this.tab_image_publish.setBackgroundResource(R.drawable.selector_tab_image_main_publish);
                this.tab_image_discover.setBackgroundResource(R.drawable.selector_tab_image_main_discover);
                this.tab_image_shopping.setBackgroundResource(R.drawable.selector_tab_image_main_shopping);
                this.tab_image_mine.setBackgroundResource(R.drawable.selector_tab_image_main_mine);

                this.tab_txt_snatch.setTextColor(Color.parseColor("#ff5050"));
                this.tab_txt_publish.setTextColor(Color.parseColor("#5C5C5C"));
                this.tab_txt_discover.setTextColor(Color.parseColor("#5C5C5C"));
                this.tab_txt_shopping.setTextColor(Color.parseColor("#5C5C5C"));
                this.tab_txt_mine.setTextColor(Color.parseColor("#5C5C5C"));
                break;
            case R.id.ll_main_publish: //选择最新揭晓
                this.tab_image_snatch.setBackgroundResource(R.drawable.selector_tab_image_main_snatch);
                this.tab_image_publish.setBackgroundResource(R.drawable.zuixinjiexiao_select);
                this.tab_image_discover.setBackgroundResource(R.drawable.selector_tab_image_main_discover);
                this.tab_image_shopping.setBackgroundResource(R.drawable.selector_tab_image_main_shopping);
                this.tab_image_mine.setBackgroundResource(R.drawable.selector_tab_image_main_mine);

                this.tab_txt_snatch.setTextColor(Color.parseColor("#5C5C5C"));
                this.tab_txt_publish.setTextColor(Color.parseColor("#ff5050"));
                this.tab_txt_discover.setTextColor(Color.parseColor("#5C5C5C"));
                this.tab_txt_shopping.setTextColor(Color.parseColor("#5C5C5C"));
                this.tab_txt_mine.setTextColor(Color.parseColor("#5C5C5C"));
                break;
            case R.id.ll_main_discover: //选择发现
                this.tab_image_snatch.setBackgroundResource(R.drawable.selector_tab_image_main_snatch);
                this.tab_image_publish.setBackgroundResource(R.drawable.selector_tab_image_main_publish);
                this.tab_image_discover.setBackgroundResource(R.drawable.find__select);
                this.tab_image_shopping.setBackgroundResource(R.drawable.selector_tab_image_main_shopping);
                this.tab_image_mine.setBackgroundResource(R.drawable.selector_tab_image_main_mine);

                this.tab_txt_snatch.setTextColor(Color.parseColor("#5C5C5C"));
                this.tab_txt_publish.setTextColor(Color.parseColor("#5C5C5C"));
                this.tab_txt_discover.setTextColor(Color.parseColor("#ff5050"));
                this.tab_txt_shopping.setTextColor(Color.parseColor("#5C5C5C"));
                this.tab_txt_mine.setTextColor(Color.parseColor("#5C5C5C"));
                break;
            case R.id.ll_main_shopping: //选择购物车

                this.tab_image_snatch.setBackgroundResource(R.drawable.selector_tab_image_main_snatch);
                this.tab_image_publish.setBackgroundResource(R.drawable.selector_tab_image_main_publish);
                this.tab_image_discover.setBackgroundResource(R.drawable.selector_tab_image_main_discover);
                this.tab_image_shopping.setBackgroundResource(R.drawable.shoppingcart_select);
                this.tab_image_mine.setBackgroundResource(R.drawable.selector_tab_image_main_mine);

                this.tab_txt_snatch.setTextColor(Color.parseColor("#5C5C5C"));
                this.tab_txt_publish.setTextColor(Color.parseColor("#5C5C5C"));
                this.tab_txt_discover.setTextColor(Color.parseColor("#5C5C5C"));
                this.tab_txt_shopping.setTextColor(Color.parseColor("#ff5050"));
                this.tab_txt_mine.setTextColor(Color.parseColor("#5C5C5C"));
                break;

            case R.id.ll_main_mine: //选择我的

                this.tab_image_snatch.setBackgroundResource(R.drawable.selector_tab_image_main_snatch);
                this.tab_image_publish.setBackgroundResource(R.drawable.selector_tab_image_main_publish);
                this.tab_image_discover.setBackgroundResource(R.drawable.selector_tab_image_main_discover);
                this.tab_image_shopping.setBackgroundResource(R.drawable.selector_tab_image_main_shopping);
                this.tab_image_mine.setBackgroundResource(R.drawable.my_select);

                this.tab_txt_snatch.setTextColor(Color.parseColor("#5C5C5C"));
                this.tab_txt_publish.setTextColor(Color.parseColor("#5C5C5C"));
                this.tab_txt_discover.setTextColor(Color.parseColor("#5C5C5C"));
                this.tab_txt_shopping.setTextColor(Color.parseColor("#5C5C5C"));
                this.tab_txt_mine.setTextColor(Color.parseColor("#ff5050"));
                break;
        }
    }

    private float lastArg1 = 1;
    private boolean isFirst = true;

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        private int one = offset * 2 + bmpW;//两个相邻页面的偏移量

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = new TranslateAnimation(currIndex * one, arg0 * one, 0, 0);//平移动画
            currIndex = arg0;
            animation.setFillAfter(true);//动画终止时停留在最后一帧，不然会回到没有执行前的状态
            animation.setDuration(200);//动画持续时间0.2秒

            switch (arg0) {
                case 0:
                    setState(R.id.ll_main_snatch);
                    break;
                case 1:
                    setState(R.id.ll_main_publish);
                    break;
                case 2:
                    setState(R.id.ll_main_discover);
                    break;
                case 3:
                    setState(R.id.ll_main_shopping);
                    break;
                case 4:
                    setState(R.id.ll_main_mine);
                    break;
            }
        }
    }

    public class ViewHolder {
        private TextView tv_title;


        public ViewHolder(View view) {
            tv_title = (TextView) view.findViewById(R.id.item);

        }
    }

    /**
     * 双击退出程序
     */
    private long lastClickTime = 0;
    @Override
    public void onBackPressed() {
        if(currIndex != 0){
            mHandler.obtainMessage(3).sendToTarget();
            return;
        }
        if (lastClickTime<=0) {
            Toast.makeText(this, R.string.press_the_back_key_to_exit_the_application, Toast.LENGTH_SHORT).show();
            lastClickTime = System.currentTimeMillis();
        }else{
            long currentClickTime = System.currentTimeMillis();
            if (currentClickTime - lastClickTime < 1000) {
                finish();
            }else{
                Toast.makeText(this, R.string.again_press_the_back_key_to_exit_the_application, Toast.LENGTH_SHORT).show();
                lastClickTime = currentClickTime;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Session.GetBoolean("isback",false)){
            setState(R.id.ll_main_shopping);
            //设置选中颜色
            mViewPager.setCurrentItem(3, false);
            Session.SetBoolean("isback",false);
        }
        mHandler2.obtainMessage(2).sendToTarget();
    }
}

