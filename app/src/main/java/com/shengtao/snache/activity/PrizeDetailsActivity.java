package com.shengtao.snache.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengtao.R;
import com.shengtao.adapter.snache.UserAdapter;
import com.shengtao.application.ImageLoaderCfg;
import com.shengtao.application.UIApplication;
import com.shengtao.baseview.BaseSingleFragmentActivity;
import com.shengtao.domain.Config;
import com.shengtao.domain.snache.PrizeDetails;
import com.shengtao.domain.snache.PrizeDetails1;
import com.shengtao.domain.snache.PrizeDetailsJoinUser;
import com.shengtao.domain.snache.PrizeDetailsJoinUserInfo;
import com.shengtao.foundation.AsyncHttpTask;
import com.shengtao.foundation.JsonHttpResponse;
import com.shengtao.foundation.SubmitType;
import com.shengtao.main.MainActivity;
import com.shengtao.mine.activity.OtherPersonCenter;
import com.shengtao.utils.BezierAnimation;
import com.shengtao.utils.CommonUtil;
import com.shengtao.utils.DateTimeUtil;
import com.shengtao.utils.LogUtil;
import com.shengtao.utils.Session;
import com.shengtao.utils.ToastUtil;
import com.shengtao.utils.VisitorMode;
import com.shengtao.widget.IosDialog;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


/**
 * @package com.baixi.snache.activity
 * Created by TT on 2015/12/14.
 * Description:奖品详情
 */
public class PrizeDetailsActivity extends BaseSingleFragmentActivity implements View.OnClickListener {

    private int[] mIconList = {R.drawable.banner,
            R.drawable.banner, R.drawable.banner};
    private PullToRefreshListView lvUser;
    private View headView;
    private RelativeLayout rlPublished;
    private PrizeDetails prizeDetails;
    private PrizeDetails1 prizeDetails1;
    private String url;
    private List<PrizeDetailsJoinUserInfo> userList;
    private RelativeLayout rl_published;
    private RelativeLayout rl_calc_details;
    private TextView tv_currentNum;
    private TextView tv_published;
    private TextView tv_join_time;
    private UserAdapter userAdapter;
    private ViewPager vpPrize;
    private ArrayList<View> sList;
    private String singleGoodsId;
    private String status;
    private RelativeLayout rl_lucknum;
    private LinearLayout ll_win;
    private ImageView iv_win;
    private ImageView iv_Published;
    private ImageView iv_userimg;
    private TextView tv_name;
    private TextView tv_ip;
    private TextView tv_join_nummber;
    private TextView tv_open_time;
    private TextView tv_daojishi;
    private TextView tv_luck_nummber;
    private TextView tv_calc;
//    private TextView tv_calc1;
    private TextView tv_calc2;
    private RelativeLayout llBuy;
    private RelativeLayout rl_go;
    private FrameLayout fl_go;
    private Button btnBuy;
    private Button btnShopcart;
    private Button btnGo;
    private TextView tvNumber;
    private TextView tvShopcart;
    private LinearLayout llShopcart;
    private String banner;
    private ImageView ivBanner;
    private ImageView img_shopcart_ico;
    private Animation animFadein;
    private TextView tv_all_join_num;
    private TextView tv_current_join_num;
    private ProgressBar pro_buy_count;

    private Handler mHandler;
    private RelativeLayout btn_shopcart_show;
    private FrameLayout flAnimCotainer;//购物车动画容器
    private String upSingleGoodsId;
    private int page;
    private String tv_all_join_num1;
    private String tv_current_join_num1;
    private String pro_buy_count1;
    private LinearLayout ll_progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        this.initRefresh();//初始化刷新操作。

        //处理选中最新揭晓和购物车
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:

                        tvShopcart.setText(Session.GetInt("cartnum") + "");
                        // 加载动画
//                animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
//                        R.anim.shopcart_icon);
//                img_shopcart_ico.startAnimation(animFadein);
                        new BezierAnimation(flAnimCotainer, getLayoutInflater().inflate(R.layout.cart_anim, flAnimCotainer, false), btnShopcart) {
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
                        if(Session.GetInt("cartnum")>0){
                            llShopcart.setVisibility(View.VISIBLE);
                        }else{
                            llShopcart.setVisibility(View.INVISIBLE);
                        }
                        break;
                }


            }
        };
    }


    Handler handler1 = new Handler(){
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            Object o[] = (Object[]) msg.obj;
            ((removeNumber)o[1]).reset();
            String url = Config.HTTP_URL_HEAD + "goods/AddShoppingCart";
            RequestParams reqParams = new RequestParams();
            //1判断是否点击的是十元商品
            reqParams.put("id", ((PrizeDetails1) o[0]).getId());
            if ("1".equals(((PrizeDetails1) o[0]).getIstenGoods())) {
                reqParams.put("number", msg.what * 10);
                LogUtil.e("log1",msg.what + "");
            } else {
//                reqParams.put("id", ((SResult) o[0]).getId());
                reqParams.put("number", msg.what);
                LogUtil.e("log1", msg.what + "");
            }
            LogUtil.e("numbernow", reqParams.toString());
            AsyncHttpTask.post(url, reqParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    String result = response.toString();
                    if (TextUtils.isEmpty(result))
                        return;
                    if ("0".equals(response.optString("code"))) {
                        //2加入成功
                        //发送动画消息,增加数量并显示
                        Session.SetInt("cartnum", Integer.parseInt(response.optString("info")));
                        Message msg1 = MainActivity.mHandler2.obtainMessage();
                        msg1.what = 1;
                        MainActivity.mHandler2.sendMessage(msg1);
                        mHandler.obtainMessage(2).sendToTarget();//成功后更新下当前购物车数量，防止数量不一致的产生
//                        LogUtil.e("numbernow", (removeNumber)o[1] + "");
                    } else if ("4".equals(response.optString("code"))) {
                        ToastUtil.showTextToast(response.optString("error"));
                    } else {
                        ToastUtil.showTextToast(response.optString("error"));
                    }
                }
            });
        }
    };

    @Override
    protected void initData() {
        singleGoodsId = getIntent().getStringExtra("singleGoodsId");
//        if(Session.GetBoolean("isHotGood",false)){
//            tv_all_join_num1 = getIntent().getStringExtra("tv_all_join_num");
//            tv_current_join_num1 = getIntent().getStringExtra("tv_current_join_num");
//            pro_buy_count1 = getIntent().getStringExtra("pro_buy_count");
////            Session.SetBoolean("isHotGood",false);
//        }
//      banner = getIntent().getStringExtra("banner");
        super.initData();
    }

    protected void initView() {
        //获取头布局
        headView = getLayoutInflater().inflate(R.layout.head_prize_details, lvUser, false);
        lvUser = (PullToRefreshListView) findViewById(R.id.lv_user_join);

        //获取头布局里面的资源id
//        vpPrize = (ViewPager) headView.findViewById(R.id.vp_prize_detail);
        rlPublished = (RelativeLayout) headView.findViewById(R.id.rl_published);
        tv_published = (TextView) headView.findViewById(R.id.tv_published);
        tv_currentNum = (TextView) headView.findViewById(R.id.tv_currentNum);
//        tv_join_time = (TextView) headView.findViewById(R.id.tv_join_time);
        iv_userimg = (ImageView) headView.findViewById(R.id.iv_userimg);
        tv_name = (TextView) headView.findViewById(R.id.tv_name);
        tv_ip = (TextView) headView.findViewById(R.id.tv_ip);
        tv_join_nummber = (TextView) headView.findViewById(R.id.tv_join_nummber);
        tv_open_time = (TextView) headView.findViewById(R.id.tv_open_time);
        tv_daojishi = (TextView) headView.findViewById(R.id.tv_daojishi);
        tv_luck_nummber = (TextView) headView.findViewById(R.id.tv_luck_nummber);
        rl_calc_details = (RelativeLayout) headView.findViewById(R.id.rl_calc_details);
        ivBanner = (ImageView) headView.findViewById(R.id.iv_banner);
        ll_progress = (LinearLayout) headView.findViewById(R.id.ll_progress);

        tv_all_join_num = (TextView) headView.findViewById(R.id.tv_all_join_num);
        tv_current_join_num = (TextView) headView.findViewById(R.id.tv_current_join_num);
        pro_buy_count = (ProgressBar) headView.findViewById(R.id.pro_buy_count);

        //两个状态的显示
        llBuy = (RelativeLayout) findViewById(R.id.layout_buy_btn);
        rl_go = (RelativeLayout) findViewById(R.id.rl_go);
        fl_go = (FrameLayout) findViewById(R.id.fl_go);

        btnBuy = (Button) findViewById(R.id.btn_buy);//立即购买
        btnShopcart = (Button) findViewById(R.id.btn_shopcart);//加入购物车
        img_shopcart_ico = (ImageView) findViewById(R.id.img_shopcart_ico);
        btn_shopcart_show = (RelativeLayout) findViewById(R.id.btn_shopcart_show);

        btnGo = (Button) findViewById(R.id.btn_go);//去本期商品
        tvNumber = (TextView) findViewById(R.id.tv_number);//第多少期

        //购物车的小圆圈
        llShopcart = (LinearLayout) findViewById(R.id.layout_shopcart_num);
        tvShopcart = (TextView) findViewById(R.id.tv_shopcart_num);
        tvShopcart.setText(Session.GetInt("cartnum") + "");//进入页面就给购物车数量赋值
        flAnimCotainer = (FrameLayout) findViewById(R.id.fl_animcotainer);
        //三个计算详情
        tv_calc = (TextView) headView.findViewById(R.id.tv_calc);
//        tv_calc1 = (TextView) headView.findViewById(R.id.tv_calc1);
        tv_calc2 = (TextView) headView.findViewById(R.id.tv_calc2);


        //已开奖幸运号码
        rl_lucknum = (RelativeLayout) headView.findViewById(R.id.rl_lucknum);
        ll_win = (LinearLayout) headView.findViewById(R.id.ll_win);
        iv_win = (ImageView) headView.findViewById(R.id.iv_win);
        //进行中
        iv_Published = (ImageView) headView.findViewById(R.id.iv_published);

        //倒计时
        rl_calc_details = (RelativeLayout) headView.findViewById(R.id.rl_calc_details);
        doBusiness();


        lvUser.setMode(PullToRefreshBase.Mode.BOTH);
        lvUser.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            int curPage;

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                sendEmptyBackgroundMessage(MSG_UI_GEt_DATA_LOADING);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

                RequestParams params = getRequestParams();
                curPage = page;
                params.put("pageNum", ++page);
                LogUtil.e("hehe", params.toString());
                AsyncHttpTask.post(url, params, new JsonHttpResponse() {
                    @Override
                    protected void success(Header[] headers, JSONObject json) {
                        userList = new PrizeDetailsJoinUser(json).getPrizeDetailsJoinUserList();//参与人的信息
                        if (userList.size() > 0) {
                            LogUtil.e("hehe", userList.size() + "");
                            userAdapter.getDatas().addAll(userList);
                            userAdapter.notifyDataSetChanged();
                        } else {
                            ToastUtil.showTextToast("没有更多数据了...");
                            page = curPage;
                        }
                        lvUser.onRefreshComplete();
//                        userAdapter = new UserAdapter(this, userList);
                    }
                });

            }
        });
    }

    /**
     *初始化上下拉操作
     */
    public void initRefresh(){

    }

    protected void doBusiness() {
        rlPublished.setOnClickListener(this);
        rl_calc_details.setOnClickListener(this);
        tv_calc.setOnClickListener(this);
//        tv_calc1.setOnClickListener(this);
        tv_calc2.setOnClickListener(this);
        rl_lucknum.setOnClickListener(this);
        iv_userimg.setOnClickListener(this);
        rl_calc_details.setOnClickListener(this);
    }

    @Override
    public Integer getHeaderRight() {
        return R.drawable.question;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, CalcRuleActivity.class);
        intent.putExtra("singleGoodsId", singleGoodsId);
        switch (v.getId()) {
            case R.id.rl_published:
                Intent intent1 = new Intent(this, PublishedActivity.class);
                intent1.putExtra("singleGoodsId", singleGoodsId);
                startActivity(intent1);
                break;
//            case R.id.rl_calc_details:
//                LogUtil.e("hahahah","zoulerl");
//                startActivity(intent);
//                break;
            case R.id.tv_calc:
//                startActivity(intent);
                ToastUtil.showTextToast("开奖的商品才可以看哦！");
                break;
            /*case R.id.tv_calc1:
                intent.putExtra("singleGoodsId", upSingleGoodsId);
                startActivity(intent);
                break;*/
            case R.id.tv_calc2:
            case R.id.rl_lucknum:
                if("0".equals(prizeDetails1.getStatus())){
                    intent.putExtra("singleGoodsId", prizeDetails1.getGoodsid());
                }
                startActivity(intent);
                break;
            case R.id.header_right:
                startActivity(new Intent(this, SnacheStatement.class));
                break;
            case R.id.iv_userimg:
//                prizeDetails1.getId()
                Intent intent2 = new Intent(this, OtherPersonCenter.class);
                intent2.putExtra("id", prizeDetails1.getUpclientid());
                startActivity(intent2);
                break;
        }
    }

    @Override
    protected String getAvtionTitle() {
        return "奖品详情";
    }

    @Override
    protected int getLayoutResouceId() {
        return R.layout.activity_prize_detail;
    }

    @Override
    protected String getUri() {
        return Config.HTTP_URL_HEAD + "goods/GoodsDetails";
    }

    @Override
    protected SubmitType getSubmitType() {
        return SubmitType.POST;
    }

    @Override
    protected RequestParams getRequestParams() {
//        LogUtil.e("log",singleGoodsId);
        return new RequestParams("singleGoodsId", singleGoodsId);
    }

    @Override
    public int getHeaderType() {
        return 1;
    }

    @Override
    protected void showUIData(Object obj) {
        //解析方法
        String result = obj.toString();
        if (TextUtils.isEmpty(result))
            return;
        JSONObject json = null;
        try {
            json = new JSONObject(result);
            prizeDetails = new PrizeDetails(json);
            prizeDetails1 = prizeDetails.getPrizeDetails1();

            url = Config.HTTP_URL_HEAD + "goods/GoodsJoinIngo";
            RequestParams requestParams = getRequestParams();
            requestParams.put("pageNum", page = 1);
            AsyncHttpTask.post(url, requestParams, new AsyncHttpResponseHandler() {
                //加载成功
                @Override
                public void onSuccess(int statusCode, Header[] LydbHeaders, byte[] bytes) {
                    try {
                        String data = new String(bytes, super.getCharset());
                        LogUtil.d(data);
                        parseTypeResult(data);
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

    //二次解析该商品当期的参与人数
    public void parseTypeResult(Object obj) {
        //解析方法
        String result = obj.toString();
        if (TextUtils.isEmpty(result))
            return;
        JSONObject json = null;

        try {
            json = new JSONObject(result);
            PrizeDetailsJoinUser prizeDetailsJoinUser = new PrizeDetailsJoinUser(json);
            userList = prizeDetailsJoinUser.getPrizeDetailsJoinUserList();//参与人的信息
            //数据为空的时候给listview留一个空位
            if(userList.size() == 0){
                PrizeDetailsJoinUserInfo user =  new PrizeDetailsJoinUserInfo();
                user.setUserId("");
                user.setClient_name("");
                user.setHead_img("");
                user.setRmb_num("");
                user.setCreate_time("");
                user.setIp_address("");
                user.setAddress_info("");
                /**
                 * private  String userId;
                 private  String client_name;
                 private  String head_img;
                 private  String rmb_num;
                 private  String create_time;
                 private  String ip_address;
                 private  String address_info;

                 */
                userList.add(user);
            }
            if(userAdapter==null){
                userAdapter = new UserAdapter(this, userList);
                lvUser.setAdapter(userAdapter);
                //给GridView添加头布局
                AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
                headView.setLayoutParams(layoutParams);
                ListView lv = lvUser.getRefreshableView();
                lv.addHeaderView(headView);
                lvUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        if (position - 2 >= 0) {
                            PrizeDetailsJoinUserInfo item = (PrizeDetailsJoinUserInfo) userAdapter.getItem(position - 2);
                            //如果为空那么就不跳入下一个activity
                            if("".equals(item.getUserId())){
                                return;
                            }
                            Intent intent = new Intent(PrizeDetailsActivity.this, OtherPersonCenter.class);
                            intent.putExtra("id", item.getUserId());
                            startActivity(intent);
                            //finishAnimationActivity();
                        } else {
                            return;
                        }
                    }
                });
            }
            userAdapter.setDatas(userList);
            userAdapter.notifyDataSetChanged();
            //进行填充数据
            fillData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //填充数据
    public void fillData() {
//        rlPublished
        //设置假数据
        sList = new ArrayList<>();
        for (int i = 0; i < mIconList.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setImageResource(mIconList[i]);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            sList.add(iv);
        }

        //判断整个对象是否为空
        if (prizeDetails1 != null) {
            if (!CommonUtil.isEmpty(prizeDetails1.getGoodsHeadurl())) {
                if(!prizeDetails1.getGoodsHeadurl().contains("imageView2")){
                    prizeDetails1.setGoodsHeadurl(prizeDetails1.getGoodsHeadurl()+"?imageViw2/2/w/640");
                }
                LogUtil.e("slist",prizeDetails1.getGoodsHeadurl());
                ImageLoader.getInstance().displayImage(prizeDetails1.getGoodsHeadurl(), ivBanner, ImageLoaderCfg.options);
            }
            if (null != prizeDetails1.getStatus()) {
                status = prizeDetails1.getStatus();
            }
            //商品的状态0表示正在参与中，1表示正在开奖中，>=2表示已经开奖
            SpannableStringBuilder style = new SpannableStringBuilder("(第" + prizeDetails1.getCurrentNum() + "期) " + prizeDetails1.getGoodsName());
            //SpannableStringBuilder实现CharSequence接口
            style.setSpan(new ForegroundColorSpan(Color.GRAY), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_currentNum.setText(style);

            if (prizeDetails1.getOpen_time() != null) {
                String i = (Long.parseLong(prizeDetails1.getOpen_time()) / 1000) + "";
                tv_open_time.setText(DateTimeUtil.timestamp2Time(i));
//                tv_join_time.setText(DateTimeUtil.timestamp2Time(i));
            }

            if("0".equals(prizeDetails1.getStatus())){
                ll_progress.setVisibility(View.VISIBLE);
                tv_all_join_num.setText(Html.fromHtml("共需 <font color=green>" + prizeDetails1.getAllJoinNum() + "</font> 人次"));
                int i = Integer.parseInt(prizeDetails1.getAllJoinNum())-Integer.parseInt(prizeDetails1.getCurrentJoinNum());
                tv_current_join_num.setText(Html.fromHtml("剩余 <font color=red>"+i+"</font>"));
                Float k = Float.parseFloat(prizeDetails1.getCurrentJoinNum()) / Float.parseFloat(prizeDetails1.getAllJoinNum());
                String progress = "0";
                if(k != 0) {
                    progress = Math.round((k *= 100) > 99 ? 99 : k < 1 ? 1 : k) + "";
                }
                pro_buy_count.setProgress(Integer.parseInt(progress));
            }

            //判断状态
            if ("0".equals(status)) {
                //判断如果是第一期，那么就没有最新揭晓
                if ("1".equals(prizeDetails1.getCurrentNum()) || "null".equals(prizeDetails1.getCurrentNum())) {
                    iv_Published.setVisibility(View.GONE);//进行中
                    rl_calc_details.setVisibility(View.GONE);//倒计时
                    rl_lucknum.setVisibility(View.GONE);//已开奖幸运号码
                    ll_win.setVisibility(View.GONE);
                    iv_win.setVisibility(View.GONE);
                } else {
                    iv_Published.setVisibility(View.VISIBLE);//进行中
                    rl_calc_details.setVisibility(View.GONE);//倒计时
                    rl_lucknum.setVisibility(View.VISIBLE);//已开奖幸运号码
                    ll_win.setVisibility(View.VISIBLE);
                    iv_win.setVisibility(View.VISIBLE);
                }
                fl_go.setVisibility(View.VISIBLE);
                llBuy.setVisibility(View.VISIBLE);
                if (Session.GetInt("cartnum") > 0) {
                    llShopcart.setVisibility(View.VISIBLE);
                }

                btnBuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (VisitorMode.isVistor(PrizeDetailsActivity.this)) {
                            return;
                        }
                        if(CommonUtil.isEmpty(Session.GetString("city_id"))){
                            //未填写收货地址,不允许加入清单
                            VisitorMode.alertToAddress(PrizeDetailsActivity.this);
                            return;
                        }
                        //进入到购物车页面，该商品数量也要加1
                        Message msg = MainActivity.mHandler.obtainMessage();
                        msg.what = 2;
                        MainActivity.mHandler.sendMessage(msg);

                        //进行添加到购物车的请求
                        String url = Config.HTTP_URL_HEAD + "goods/AddShoppingCart";
                        RequestParams reqParams = new RequestParams();
                        reqParams.add("id", prizeDetails1.getId());
                        reqParams.add("number", "1".equals(prizeDetails1.getIstenGoods())?"10":"1");
                        AsyncHttpTask.post(url, reqParams, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                String result = response.toString();
                                if (TextUtils.isEmpty(result))
                                    return;
                                Message msg1 = MainActivity.mHandler2.obtainMessage();
                                if ("0".equals(response.optString("code"))) {
                                    //加入成功
                                    //发送动画消息,增加数量并显示
                                    Session.SetInt("cartnum", Integer.parseInt(response.optString("info")));
                                    msg1.what = 1;
                                    MainActivity.mHandler2.sendMessage(msg1);
                                } else if ("4".equals(response.optString("code"))) {
                                    msg1.what = 2;
                                    MainActivity.mHandler2.sendMessage(msg1);
                                } else {
                                    ToastUtil.showTextToast(response.optString("error"));
                                }
                                //跳入首页
                                Session.SetBoolean("isback", true);
                                Intent intent = new Intent(PrizeDetailsActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        });
                    }
                });

                btnShopcart.setOnClickListener(new removeNumber() {
                    @Override
                    public void reset() {
                        number = 0;
                    }
                    private int number = 0;
                    @Override
                    public void onClick(View view) {
                        if (VisitorMode.isVistor(PrizeDetailsActivity.this)) {
                            return;
                        }
                        if(CommonUtil.isEmpty(Session.GetString("city_id"))){
                            //未填写收货地址,不允许加入清单
                            VisitorMode.alertToAddress(PrizeDetailsActivity.this);
                            return;
                        }

                        ++number;
                        //发送动画消息
                        LogUtil.e("number",number+"");
                        mHandler.obtainMessage(1,view).sendToTarget();
                        handler1.removeCallbacksAndMessages(null);
                        Message msg = new Message();
                        msg.what = number;
                        msg.obj = new Object[]{prizeDetails1,this};
                        handler1.sendMessageDelayed(msg, 500);

                    }
                });

                btn_shopcart_show.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //进入到购物车页面，该商品数量也要加1
                        Message msg = MainActivity.mHandler.obtainMessage();
                        msg.what = 2;
                        MainActivity.mHandler.sendMessage(msg);
                        //跳入首页
                        Session.SetBoolean("isback", true);
                        Intent intent = new Intent(PrizeDetailsActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });

                ImageLoader.getInstance().displayImage(prizeDetails1.getHead_img(), iv_userimg, UIApplication.getAvatar());
                tv_name.setText(prizeDetails1.getClient_name());
                tv_ip.setText(prizeDetails1.getIp_address());
                if (prizeDetails1.getOpen_time() != null) {
                    String i = (Long.parseLong(prizeDetails1.getOpen_time()) / 1000) + "";
                    tv_open_time.setText(DateTimeUtil.timestamp2Time(i));
                }
                tv_join_nummber.setText(prizeDetails1.getClient_join_num() + " 人 次");
                tv_luck_nummber.setText("幸运号码:  " + prizeDetails1.getLucky_id());
                tv_published.setText("进行中");
                tv_published.setTextColor(getResources().getColor(R.color.yym_common_red));
                tv_published.setBackgroundResource(R.drawable.btn_red_radius_border);
                upSingleGoodsId = prizeDetails1.getGoodsid();
            } else if ("1".equals(status)) {
                iv_Published.setVisibility(View.GONE);//进行中
                rl_calc_details.setVisibility(View.VISIBLE);//倒计时
                rl_lucknum.setVisibility(View.GONE);//已开奖幸运号码
                ll_win.setVisibility(View.GONE);
                iv_win.setVisibility(View.GONE);

                rl_go.setVisibility(View.GONE);
                llBuy.setVisibility(View.GONE);
                fl_go.setVisibility(View.GONE);

                btnGo.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (VisitorMode.isVistor(PrizeDetailsActivity.this)) {
                            return;
                        }
                        //这里进行判断，如果是最后一期，那么给个提示
                        if (!prizeDetails1.getCurrentNum().equals(prizeDetails1.getAllNum())) {
                            Intent intent = new Intent(PrizeDetailsActivity.this, PrizeDetailsActivity.class);
                            intent.putExtra("singleGoodsId", prizeDetails1.getNewGoodsid());
                            intent.putExtra("banner", banner);
                            startActivity(intent);
                        } else {
                            showFinishDialog();
//                        ToastUtil.showTextToast("目前已是最后一期，请时刻关商品注最新动态哦");
                        }
                    }
                });
                tvNumber.setText("最新一期正在火热进行中...");//"第" + prizeDetails1.getNewGoodsCurrentNum() + "期正在火热进行中..."
                tv_published.setText("倒计时");
                tv_published.setTextColor(getResources().getColor(R.color.orange));
                tv_published.setBackgroundResource(R.drawable.btn_orange_radius_border);

                //一个时间是OPENTIME  一个是time


                new MyCountDownTimer(Long.parseLong(prizeDetails1.getOpenTime()) - prizeDetails1.getTime(), 1).start();//Long.parseLong(newGood2.getOpen_time())

            } else {
                iv_Published.setVisibility(View.GONE);//进行中
                rl_calc_details.setVisibility(View.GONE);//倒计时
                rl_lucknum.setVisibility(View.VISIBLE);//已开奖幸运号码
                ll_win.setVisibility(View.VISIBLE);
                iv_win.setVisibility(View.VISIBLE);

                fl_go.setVisibility(View.VISIBLE);
                rl_go.setVisibility(View.VISIBLE);
                llBuy.setVisibility(View.GONE);

                btnGo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (VisitorMode.isVistor(PrizeDetailsActivity.this)) {
                            return;
                        }
                        if (!prizeDetails1.getCurrentNum().equals(prizeDetails1.getAllNum())) {
                            Intent intent = new Intent(PrizeDetailsActivity.this, PrizeDetailsActivity.class);
                            intent.putExtra("singleGoodsId", prizeDetails1.getNewGoodsid());
                            intent.putExtra("banner", banner);
                            startActivity(intent);
                            finishAnimationActivity();
                        } else {
                            showFinishDialog();
                        }
                    }
                });

                tvNumber.setText("最新一期正在火热进行中...");

                tv_published.setText("已揭晓");
                tv_published.setTextColor(getResources().getColor(R.color.green));
                tv_published.setBackgroundResource(R.drawable.btn_green_radius_border);
                ImageLoader.getInstance().displayImage(prizeDetails1.getHead_img(), iv_userimg, UIApplication.getAvatar());
                tv_name.setText(prizeDetails1.getClient_name());
                tv_ip.setText(prizeDetails1.getIp_address());
                tv_join_nummber.setText(prizeDetails1.getClient_join_num() + " 人 次");
                if (!CommonUtil.isEmpty(prizeDetails1.getOpen_time())) {
                    String i = (Long.parseLong(prizeDetails1.getOpen_time()) / 1000) + "";
                    tv_open_time.setText(DateTimeUtil.timestamp2Time(i));
                }
                tv_luck_nummber.setText("幸运号码  " + prizeDetails1.getLucky_id());
            }

        }
        lvUser.onRefreshComplete();
    }

    interface removeNumber extends View.OnClickListener{
        void reset();
    }


    private void showFinishDialog() {

        IosDialog iosDialog = new IosDialog(PrizeDetailsActivity.this);

        iosDialog.setTitle("亲~不能前往了哦~目前已经是最后一期了！记得时刻关注本商品的最新动态哦！");
        iosDialog.setCancelable(true);

        iosDialog.show();

    }
    //viewpager的适配器
//    class SnacheAdapter extends PagerAdapter {
//
//        @Override
//        public int getCount() {
//            return sList.size();
//        }
//
//        @Override
//        public boolean isViewFromObject(View arg0, Object arg1) {
//            return arg0 == arg1;
//        }
//
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
////            ImageView view = new ImageView(getActivity());
//            //在此处获取图片url
////            ImageLoader.getInstance().displayImage("", view);
//            ImageView view = (ImageView) sList.get(position);
//            view.setScaleType(ImageView.ScaleType.FIT_XY);// 设置图片缩放类型, 填充父窗体类型
//            container.addView(view);
//            return view;
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            container.removeView((View) object);
//        }
//    }

    class MyCountDownTimer extends CountDownTimer {
        /**
         * @param millisInFuture    表示以毫秒为单位 倒计时的总数
         *                          <p/>
         *                          例如 millisInFuture=1000 表示1秒
         * @param countDownInterval 表示 间隔 多少微秒 调用一次 onTick 方法
         *                          <p/>
         *                          例如: countDownInterval =1000 ; 表示每1000毫秒调用一次onTick()
         */
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            tv_daojishi.setText("揭晓中");

            AsyncHttpTask.post(Config.HTTP_URL_HEAD + "goods/newSingleOpenGoods", new RequestParams("id", singleGoodsId), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    String result = response.toString();
                    if (TextUtils.isEmpty(result))
                        return;
                    if ("0".equals(response.optString("code"))) {
                        sendEmptyBackgroundMessage(MSG_UI_GEt_DATA_LOADING);
                        /*JSONObject info = response.optJSONObject("info");
                        JSONArray list = info.optJSONArray("list");
                        String name = list.optJSONObject(0).optString("client_name");
                        String client_join_num = list.optJSONObject(0).optString("client_join_num");
                        String open_time = list.optJSONObject(0).optString("open_time");

                        iv_Published.setVisibility(View.GONE);//进行中
                        rl_calc_details.setVisibility(View.GONE);//倒计时
                        rl_lucknum.setVisibility(View.VISIBLE);//已开奖幸运号码
                        ll_win.setVisibility(View.VISIBLE);
                        iv_win.setVisibility(View.VISIBLE);

                        tv_published.setText("已揭晓");
                        ImageLoader.getInstance().displayImage(prizeDetails1.getHead_img(), iv_userimg, ImageLoaderCfg.options);
                        tv_name.setText(name);
                        tv_ip.setText(prizeDetails1.getIp_address());
                        tv_join_nummber.setText(client_join_num + "次");
                        String i = (Long.parseLong(open_time) / 1000) + "";
                        tv_open_time.setText(DateTimeUtil.timestamp2Time(i));
//                        tv_luck_nummber.setText("幸运号码  " + prizeDetails1.getLucky_id());*/
                    } else {
                        MyCountDownTimer.this.onFinish();
                    }
                }
            });
        }

        @Override
        public void onTick(long millisUntilFinished) {
//            int fen = (int) (millisUntilFinished / 1000 / 60);
//            int haomiao = (int) millisUntilFinished % 1000;
//            long i = millisUntilFinished;
//            long j = fen*60*1000;
//            String miao = String.valueOf((millisUntilFinished - (fen * 60 * 1000)) / 1000);

//            tv_daojishi.setText(fen + "分" + miao + "秒" + haomiao);


            int fen = (int) (millisUntilFinished/1000/60);
            int haomiao = (int)millisUntilFinished % 100;
            int miao = (int) ((millisUntilFinished - (fen * 60 * 1000)) / 1000);
            tv_daojishi.setText(((fen>=10)?fen+"分":"0"+fen+"分") + ((miao>=10)?miao+"秒":"0"+miao+"秒")+((haomiao>=10)?haomiao:"0"+haomiao));

        }
    }
}
