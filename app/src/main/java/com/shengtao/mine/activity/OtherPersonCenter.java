package com.shengtao.mine.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengtao.R;
import com.shengtao.adapter.snache.ViewPagerAdapter;
import com.shengtao.application.UIApplication;
import com.shengtao.baseview.BaseSingleFragmentActivity;
import com.shengtao.domain.Config;
import com.shengtao.domain.mine.PersonDState;
import com.shengtao.domain.mine.PersonDStateInfo;
import com.shengtao.domain.mine.PersonShow;
import com.shengtao.domain.mine.PersonShowInfo;
import com.shengtao.domain.mine.PersonWin;
import com.shengtao.domain.mine.PersonWinInfo;
import com.shengtao.foundation.AsyncHttpTask;
import com.shengtao.foundation.SubmitType;
import com.shengtao.main.MainActivity;
import com.shengtao.utils.BezierAnimation;
import com.shengtao.utils.CircleImageView;
import com.shengtao.utils.LogUtil;
import com.shengtao.utils.Session;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @package com.baixi.mine.activity
 * Created by TT on 2015/12/16.
 * Description:他人个人中心
 */
public class OtherPersonCenter extends BaseSingleFragmentActivity{

    private ViewPager vpOtherCenter;
    private CircleImageView ivImg;
    private TextView tvName;
    private RadioButton rbDynamicState;
    private RadioButton rbWinnerDetail;
    private RadioButton rbShowRecord;
    private ArrayList<Fragment> mFragments;

    /**
     * 当前选择的页面的序号
     */
    private int mSelectIndex = -1;
    private RadioGroup rgStatusDetail;
    private ViewPagerAdapter mBrowseHistoryAdapter;
    private RequestParams requestParams;
    private String headUrl;
    private String name;
    private String userId;
    private String url;
    private ArrayList<PersonDStateInfo> dList;
    private ArrayList<PersonWinInfo> wList;
    private ArrayList<PersonShowInfo> pList;
    private String id;
    private LinearLayout llShopcart;
    private TextView tvShopcart;
    public static Handler mHandler;
    private FrameLayout flAnimCotainer;//购物车动画容器
    private ImageView img_shopcart_ico;
    private RelativeLayout btn_shopcart_show;
    private ImageView ivBack;
    private LinearLayout back;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        initView();

        //处理选中最新揭晓和购物车
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        tvShopcart.setText(Session.GetInt("cartnum") + "");
                        if (Session.GetInt("cartnum") > 0) {
                            llShopcart.setVisibility(View.VISIBLE);
                        } else {
                            llShopcart.setVisibility(View.GONE);
                        }
                        if (msg.obj instanceof View)
                            new BezierAnimation(flAnimCotainer, getLayoutInflater().inflate(R.layout.cart_anim, flAnimCotainer, false), (View) msg.obj) {
                                @Override
                                protected List<Animator> getAnimators() {
                                    ArrayList<Animator> animators = new ArrayList<>();
                                    animators.add(ObjectAnimator.ofFloat(img_shopcart_ico, "scaleX", 1.0f, 1.2f).setDuration(300));
                                    animators.add(ObjectAnimator.ofFloat(img_shopcart_ico, "scaleX", 1.2f, 1.0f).setDuration(300));
                                    return animators;
                                }

                                @Override
                                protected View getEndView() {
                                    return llShopcart;
                                }
                            }.showAnimation();
                        break;
                    case 2:
                        //红点显示
                        tvShopcart.setText(Session.GetInt("cartnum") + "");
                        if (Session.GetInt("cartnum") > 0) {
                            llShopcart.setVisibility(View.VISIBLE);
                        } else {
                            llShopcart.setVisibility(View.INVISIBLE);
                        }
                        break;
                }
            }
        };
    }

    @Override
    protected void initData() {
        id = getIntent().getStringExtra("id");
        super.initData();
    }

    @Override
    protected String getAvtionTitle() {
        return "TA人个人中心";
    }

    @Override
    protected int getLayoutResouceId() {
        return R.layout.activity_other_persion_center;
    }

    @Override
    protected String getUri() {
        return Config.HTTP_URL_HEAD + "goods/otherJoinLog";
    }


    @Override
    protected SubmitType getSubmitType() {
        return SubmitType.POST;
    }


    @Override
    protected RequestParams getRequestParams() {
        requestParams = new RequestParams();
        requestParams.put("dbAppClientid", id);
        requestParams.put("pageNum", 1);
        return requestParams;
    }

    @Override
    protected void showUIData(Object obj) {
        //解析方法
        String result = obj.toString();
        LogUtil.e("log", result);
        if (TextUtils.isEmpty(result))
            return;
        JSONObject json = null;
        try {
            json = new JSONObject(result);
            PersonDState personDState = new PersonDState(json);
            dList = personDState.getPersonDStateList();//获取动态的数据集合
            headUrl = personDState.getHead_url();//用户头像
            name = personDState.getName();//用户名
            userId = personDState.getUserid();//用户id

            url = Config.HTTP_URL_HEAD + "goods/otherWinLog";//中奖纪录
            AsyncHttpTask.post(url, requestParams, new AsyncHttpResponseHandler() {
                //加载成功
                @Override
                public void onSuccess(int statusCode, Header[] LydbHeaders, byte[] bytes) {
                    try {
                        String data = new String(bytes, super.getCharset());
                        LogUtil.e("log",data);
                        parseData("win", data);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }

                //获取数据失败
                @Override
                public void onFailure(int statusCode, Header[] motuHeaders, byte[] bytes, Throwable throwable) {
                    // 网络请求出错
                    if(bytes != null)
                    LogUtil.e("otherPeronCenter",new String(bytes));
                    return;
                }
            });



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //继续解析中奖接口和晒单接口
    public void parseData(String type , Object data){
        try {
            String result = data.toString();
            if (TextUtils.isEmpty(result))
                return;
            JSONObject json = null;
            json = new JSONObject(result);
            //获取中奖详情的数据

            PersonWin personWin = new PersonWin(json);
            wList = personWin.getPersonWinList();
            url = Config.HTTP_URL_HEAD + "goods/otherShareLog";//晒单
            AsyncHttpTask.post(url, requestParams, new AsyncHttpResponseHandler() {
                //加载成功
                @Override
                public void onSuccess(int statusCode, Header[] LydbHeaders, byte[] bytes) {
                    try {
                        String data = new String(bytes, super.getCharset());
                        LogUtil.e("log", data);
                        parseData2("show", data);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                //获取数据失败
                @Override
                public void onFailure(int statusCode, Header[] motuHeaders, byte[] bytes, Throwable throwable) {
                    // 网络请求出错
                    return;
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //继续解析中奖接口和晒单接口
    public void parseData2(String type , Object data){
        try {
            String result = data.toString();
            if (TextUtils.isEmpty(result))
                return;
            JSONObject json = null;
            json = new JSONObject(result);
            //获取晒单页面的数据
            PersonShow personShow = new PersonShow(json);
            pList = personShow.getPersonShowList();
            initViewPager();
            //填充页面数据
            fillData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void initView() {
        vpOtherCenter = (ViewPager) findViewById(R.id.vp_other_center);//viewpager
        ivImg = (CircleImageView) findViewById(R.id.iv_img);//头像
        tvName = (TextView) findViewById(R.id.tv_name);//姓名
        rgStatusDetail = (RadioGroup) findViewById(R.id.rg_status_detail);//radiogroup
        rbDynamicState = (RadioButton) findViewById(R.id.rb_dynamic_state);//动态
        rbWinnerDetail = (RadioButton) findViewById(R.id.rb_winner_detail);//中奖详情
        rbShowRecord = (RadioButton) findViewById(R.id.rb_show_record);//晒单记录

        //购物车的小圆圈
        llShopcart = (LinearLayout) findViewById(R.id.layout_shopcart_num);
        btn_shopcart_show = (RelativeLayout) findViewById(R.id.btn_shopcart_show);//右上角
        back = (LinearLayout) findViewById(R.id.back);//左上角
        tvShopcart = (TextView) findViewById(R.id.tv_shopcart_num);
        img_shopcart_ico = (ImageView) findViewById(R.id.img_shopcart_ico);
        tvShopcart.setText(Session.GetInt("cartnum") + "");//进入页面就给购物车数量赋值
        flAnimCotainer = (FrameLayout) findViewById(R.id.fl_animcotainer);

        btn_shopcart_show.setOnClickListener(this);
        back.setOnClickListener(this);

        if(Session.GetInt("cartnum")>0){
            llShopcart.setVisibility(View.VISIBLE);
        }else{
            llShopcart.setVisibility(View.GONE);
        }
    }

    public void initViewPager() {
        mFragments = new ArrayList<Fragment>();

        Fragment mDynamicStateFragment = DynamicStateFragment.newInstance();//动态
        Fragment mWinnerDetailFragment = WinnerDetailFragment.newInstance();//中奖信息
        Fragment mShowRecordFragment = ShowRecordFragment.newInstance();//晒单记录

        Bundle bundle1 = new Bundle();
        bundle1.putSerializable("dList", dList);
        bundle1.putString("id", id);
        mDynamicStateFragment.setArguments(bundle1);

        Bundle bundle2 = new Bundle();
        bundle2.putSerializable("wList", wList);
        bundle2.putString("id", id);
        mWinnerDetailFragment.setArguments(bundle2);

        Bundle bundle3 = new Bundle();
        bundle3.putSerializable("pList", pList);
        bundle3.putString("id", id);
        mShowRecordFragment.setArguments(bundle3);

        mFragments.add(mDynamicStateFragment);
        mFragments.add(mWinnerDetailFragment);
        mFragments.add(mShowRecordFragment);

        vpOtherCenter.setOffscreenPageLimit(mFragments.size());

        mBrowseHistoryAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mFragments);
        vpOtherCenter.setAdapter(mBrowseHistoryAdapter);

        vpOtherCenter.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                switchTab(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

        rgStatusDetail.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_dynamic_state:
                        switchTab(0);
                        break;
                    case R.id.rb_winner_detail:
                        switchTab(1);
                        break;
                    case R.id.rb_show_record:
                        switchTab(2);
                        break;
                }
            }
        });
        switchTab(vpOtherCenter.getCurrentItem());
    }

    public void switchTab(int index) {
        if (mSelectIndex == index)
            return;
        mSelectIndex = index;
        rgStatusDetail.check(rgStatusDetail.getChildAt(index).getId());
        vpOtherCenter.setCurrentItem(index, true);
        mBrowseHistoryAdapter.notifyDataSetChanged();
    }

    //填充页面数据
    public void fillData(){
        tvName.setText(name);
        ImageLoader.getInstance().displayImage(headUrl,ivImg, UIApplication.getAvatar());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_shopcart_show:
                //购物车图标,跳转到购物车
                //跳入首页
                Intent intent= new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Session.SetBoolean("isback",true);
                startActivity(intent);
                break;
            case R.id.back:
                finish();
                break;
        }
    }
}
