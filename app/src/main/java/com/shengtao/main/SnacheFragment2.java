package com.shengtao.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengtao.R;
import com.shengtao.application.ImageLoaderCfg;
import com.shengtao.baseview.BaseSingleFragment;
import com.shengtao.baseview.HorizontalViewPager;
import com.shengtao.baseview.TipsLayoutNormal;
import com.shengtao.discover.activity.ShareOrderActivity;
import com.shengtao.domain.Config;
import com.shengtao.domain.firstPage.FirstPage;
import com.shengtao.domain.firstPage.ListPriceEntity;
import com.shengtao.domain.firstPage.ListadEntity;
import com.shengtao.domain.firstPage.ListgoodsEntity;
import com.shengtao.domain.snache.HotGoods;
import com.shengtao.foundation.AsyncHttpTask;
import com.shengtao.foundation.IToastBlock;
import com.shengtao.foundation.SubmitType;
import com.shengtao.mine.activity.RechargeActivity;
import com.shengtao.snache.activity.AllGoodsActivity;
import com.shengtao.snache.activity.CommonQuestionActivity;
import com.shengtao.snache.activity.MessageActivity;
import com.shengtao.snache.activity.PrizeDetailsActivity;
import com.shengtao.snache.activity.SearchActivity;
import com.shengtao.utils.CacheUtils;
import com.shengtao.utils.CircleImageView;
import com.shengtao.utils.CommonUtil;
import com.shengtao.utils.LogUtil;
import com.shengtao.utils.Session;
import com.shengtao.utils.ToastUtil;
import com.shengtao.utils.VisitorMode;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 夺宝
 */

public class SnacheFragment2 extends BaseSingleFragment implements View.OnClickListener {

    private View activeMain;
    private HorizontalViewPager vpSnache;
    private int[] mIconList = {};//R.drawable.banner,R.drawable.banner, R.drawable.banner


    private ArrayList<ListadEntity> sList;
    private GridView gvHotTre;
    private View headSnache;
    private PullToRefreshScrollView plScroll;
    private ImageView ivSearch;
    private ImageView ivMessage;

    private LinearLayout llAllPrize;
    private LinearLayout llChongZhi;
    private LinearLayout llShaidan;
    private LinearLayout llCommonQes;
    private TextView tv_show_all;

    private List<ListgoodsEntity> hList;

    private LinearLayout ll_win1;
    private LinearLayout ll_win2;
    private LinearLayout ll_win3;
    private LinearLayout ll_downtimer1;
    private LinearLayout ll_downtimer2;
    private LinearLayout ll_downtimer3;
    private ListPriceEntity newGood;
    private ListPriceEntity newGood1;
    private ListPriceEntity newGood2;
    private TextView tv_downtimer1;
    private TextView tv_downtimer2;
    private TextView tv_downtimer3;
    private ImageView iv_img1;
    private ImageView iv_img2;
    private ImageView iv_img3;
    private TextView tv_title1;
    private TextView tv_title2;
    private TextView tv_title3;
    private TextView tv_name1;
    private TextView tv_name2;
    private TextView tv_name3;
    private int i;
    private SnacheAdapter snacheAdapter;
    private SnacheGridAdapter snacheGridAdapter;
    private LinearLayout ll_hot1;
    private LinearLayout ll_hot2;
    private LinearLayout ll_hot3;
    private Animation animFadein;
    private LinearLayout llContainer;

    private CircleImageView iv_shopping;
    private CountDownTimer myCd[] = new CountDownTimer[3];

    private int mLastPointPos = 0;// 上一个选中圆点的位置

    private int currentPage = 1;

    private static long lastClickTime;
    private List<ListPriceEntity> results;
    private String time;


    static SnacheFragment2 newInstance(String s) {
        SnacheFragment2 newFragment = new SnacheFragment2();
        Bundle bundle = new Bundle();
        bundle.putString("hello", s);
        newFragment.setArguments(bundle);
        //bundle还可以在每个标签里传送数据
        return newFragment;
    }

    //处理轮播器
    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            int currentItem = vpSnache.getCurrentItem();
            currentItem++;
            vpSnache.setCurrentItem(currentItem);// 切换到下一条广告
            sendEmptyMessageDelayed(0, 7000);// 继续延时3秒发消息,形成无限循环
        }

        ;
    };


    //处理动画
    Handler handler1 = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            Object o[] = (Object[]) msg.obj;
            ((removeNumber) o[1]).reset();
            String url = Config.HTTP_URL_HEAD + "goods/AddShoppingCart";
            RequestParams reqParams = new RequestParams();
            //1判断是否点击的是十元商品
            reqParams.put("id", ((ListgoodsEntity) o[0]).getGoodsId());
            if ("1".equals(((ListgoodsEntity) o[0]).getGoods_10())) {
                reqParams.put("number", msg.what * 10);
                LogUtil.e("log1", msg.what + "");
            } else {
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
                    } else if ("4".equals(response.optString("code"))) {
                        ToastUtil.showTextToast(response.optString("error"));
                    } else {
                        ToastUtil.showTextToast(response.optString("error"));
                    }
                }
            });
//            RequestParams reqParams = new RequestParams();
//            //1判断是否点击的是十元商品
//            if ("1".equals(hotGood.getGoods_10())) {
//                reqParams.put("id", hotGood.getGoodsId());
//                reqParams.put("number", number * 10);
//            } else {
//                reqParams.put("id", hotGood.getGoodsId());
//                reqParams.put("number", number);
//            }
//            AsyncHttpTask.post(url, reqParams, new JsonHttpResponseHandler() {
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                    String result = response.toString();
//                    if (TextUtils.isEmpty(result))
//                        return;
//                    if ("0".equals(response.optString("code"))) {
//                        //2加入成功
//                        //发送动画消息,增加数量并显示
//                        Session.SetInt("cartnum", Integer.parseInt(response.optString("info")));
//                        Message msg1 = MainActivity.mHandler2.obtainMessage();
//                        msg1.what = 2;
//                        MainActivity.mHandler2.sendMessage(msg1);
//                        number = 0;
//                    } else if ("4".equals(response.optString("code"))) {
//                        ToastUtil.showTextToast(response.optString("error"));
//                    } else {
//                        ToastUtil.showTextToast(response.optString("error"));
//                    }
//                }
//            });
        }
    };

    @SuppressLint("HandlerLeak")
    public Handler handler2 = new Handler() {
        public void handleMessage(Message msg) {
            //点击时将其的页码数变为1，防止上拉造成页码错乱
            currentPage = 1;
            sendEmptyBackgroundMessage(MSG_UI_GEt_DATA_LOADING);
        }

        ;
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activeMain = inflater.inflate(R.layout.fragment_snache, container, false);

        //头布局上的资源id
        ivSearch = (ImageView) activeMain.findViewById(R.id.iv_search);//搜索
        ivMessage = (ImageView) activeMain.findViewById(R.id.iv_message);//消息

        llShaidan = (LinearLayout) activeMain.findViewById(R.id.ll_shaidan);//晒单
        llChongZhi = (LinearLayout) activeMain.findViewById(R.id.ll_chongzhi);//充值
        llAllPrize = (LinearLayout) activeMain.findViewById(R.id.ll_quanbushangpin);//全部商品
        llCommonQes = (LinearLayout) activeMain.findViewById(R.id.ll_changjianwenti);//常见问题

        vpSnache = (HorizontalViewPager) activeMain.findViewById(R.id.vp_snache);
        gvHotTre = (GridView) activeMain.findViewById(R.id.gv_hot_treasure);
        tv_show_all = (TextView) activeMain.findViewById(R.id.tv_show_all);//点击显示全部
        plScroll = (PullToRefreshScrollView) activeMain.findViewById(R.id.pull_refresh_scrollview);

        //获取首页最新揭晓的资源id
        ll_win1 = (LinearLayout) activeMain.findViewById(R.id.ll_win1);
        ll_win2 = (LinearLayout) activeMain.findViewById(R.id.ll_win2);
        ll_win3 = (LinearLayout) activeMain.findViewById(R.id.ll_win3);

        ll_downtimer1 = (LinearLayout) activeMain.findViewById(R.id.ll_downtimer1);
        ll_downtimer2 = (LinearLayout) activeMain.findViewById(R.id.ll_downtimer2);
        ll_downtimer3 = (LinearLayout) activeMain.findViewById(R.id.ll_downtimer3);

        tv_downtimer1 = (TextView) activeMain.findViewById(R.id.tv_downtimer1);
        tv_downtimer2 = (TextView) activeMain.findViewById(R.id.tv_downtimer2);
        tv_downtimer3 = (TextView) activeMain.findViewById(R.id.tv_downtimer3);

        iv_img1 = (ImageView) activeMain.findViewById(R.id.iv_img1);
        iv_img2 = (ImageView) activeMain.findViewById(R.id.iv_img2);
        iv_img3 = (ImageView) activeMain.findViewById(R.id.iv_img3);

        tv_title1 = (TextView) activeMain.findViewById(R.id.tv_title1);
        tv_title2 = (TextView) activeMain.findViewById(R.id.tv_title2);
        tv_title3 = (TextView) activeMain.findViewById(R.id.tv_title3);

        tv_name1 = (TextView) activeMain.findViewById(R.id.tv_name1);
        tv_name2 = (TextView) activeMain.findViewById(R.id.tv_name2);
        tv_name3 = (TextView) activeMain.findViewById(R.id.tv_name3);

        ll_hot1 = (LinearLayout) activeMain.findViewById(R.id.ll_hot1);
        ll_hot2 = (LinearLayout) activeMain.findViewById(R.id.ll_hot2);
        ll_hot3 = (LinearLayout) activeMain.findViewById(R.id.ll_hot3);

        //信箱上面的红点
        iv_shopping = (CircleImageView) activeMain.findViewById(R.id.iv_shopping);
        if (Session.GetInt("iswinMessage") > 0 || Session.GetInt("issendMessage") > 0 || Session.GetBoolean("isSysMessage") == true) {
            iv_shopping.setVisibility(View.VISIBLE);
        } else {
            iv_shopping.setVisibility(View.GONE);
        }

        //设置广告数据
        sList = new ArrayList<>();
        //存放热门商品的集合
        hList = new ArrayList<>();

        results = new ArrayList<>();
        //设置点击事件
        doBusiness();
        snacheGridAdapter = new SnacheGridAdapter();
        // 读缓存
        String cache = CacheUtils.getCache(Config.HTTP_URL_HEAD + "goods/firstPage",
                getActivity());
        if (cache != null) {
            // 有缓存
            showUIData(cache);
        }

        this.mTlLoading = (TipsLayoutNormal) activeMain.findViewById(R.id.tl_loading);
        this.mTlLoading.show(TipsLayoutNormal.TIPS_STATUS_LOAD_ING);
        sendEmptyBackgroundMessage(MSG_UI_GEt_DATA_LOADING);

        //禁掉gridview的item自动获取焦点的能力
        gvHotTre.setFocusable(false);


        //初始化上拉下啦刷新
        plScroll.setMode(PullToRefreshBase.Mode.BOTH);
        plScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                currentPage = 1;
                RequestParams params = new RequestParams();
                params.put("pageNum", currentPage);
                AsyncHttpTask.post(Config.HTTP_URL_HEAD + "goods/firstPage", params, new AsyncHttpResponseHandler() {
                    //加载成功
                    @Override
                    public void onSuccess(int statusCode, Header[] motuHeaders, byte[] bytes) {
                        try {
                            String data = new String(bytes, super.getCharset());
                            LogUtil.d(data);
                            showUIData(data);
                            plScroll.onRefreshComplete();
                            snacheGridAdapter.notifyDataSetChanged();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }

                    //获取数据失败
                    @Override
                    public void onFailure(int statusCode, Header[] motuHeaders, byte[] bytes, Throwable throwable) {
                        plScroll.onRefreshComplete();
                        return;
                    }
                });
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                ++currentPage;
                RequestParams params = new RequestParams();
                params.put("pageNum", currentPage);
                AsyncHttpTask.post(Config.HTTP_URL_HEAD + "goods/SearchByHot", params, new AsyncHttpResponseHandler() {
                    //加载成功
                    @Override
                    public void onSuccess(int statusCode, Header[] motuHeaders, byte[] bytes) {
                        try {
                            String data = new String(bytes, super.getCharset());
                            LogUtil.d(data);
                            parseTypeResult(data);
                            plScroll.onRefreshComplete();
                            snacheGridAdapter.notifyDataSetChanged();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }

                    //获取数据失败
                    @Override
                    public void onFailure(int statusCode, Header[] motuHeaders, byte[] bytes, Throwable throwable) {
                        plScroll.onRefreshComplete();
                        return;
                    }
                });
            }
        });

        gvHotTre.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListgoodsEntity hotGood = snacheGridAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), PrizeDetailsActivity.class);
                //不仅传商品单表id，还需要获奖进度的几个信息给奖品详情页面
                intent.putExtra("singleGoodsId", hotGood.getGoodsId());
                //设置进入奖品详情页面的入口是否为热门宝物
//                Session.SetBoolean("isHotGood",true);
//                intent.putExtra("tv_all_join_num",hotGood.getAll_join_num());
//                intent.putExtra("tv_current_join_num",hotGood.getCurrent_join_num());
//                Float i = Float.parseFloat(hotGood.getCurrent_join_num()) / Float.parseFloat(hotGood.getAll_join_num());
//                String progress = Math.round(i * 100 ) + "";
//                intent.putExtra("pro_buy_count",progress);
                startActivity(intent);
            }
        });
        return activeMain;
    }


    public void initGridview() {
        gvHotTre.setAdapter(snacheGridAdapter);
        if (results.size() > 2) {
            newGood2 = results.get(2);
            String status = newGood2.getStatus();
            ImageLoader.getInstance().displayImage(newGood2.getGoods_headurl(), iv_img3, ImageLoaderCfg.options);
            tv_title3.setText(newGood2.getGoods_name());
            //商品的状态0表示正在参与中，1表示正在开奖中，>=2表示已经开奖
            if ("1".equals(status)) {
                ll_win3.setVisibility(View.GONE);
                ll_downtimer3.setVisibility(View.VISIBLE);
                if (myCd[2] != null) myCd[2].cancel();//取消读缓存的倒计时
                Long n = Long.parseLong(newGood2.getOpen_time()) - Long.parseLong(time);
                myCd[2] = new MyCountDownTimer(n, 10, newGood2.getId(), tv_downtimer3, ll_win3, ll_downtimer3, tv_name3).start();//Long.parseLong(newGood2.getOpen_time())
            } else {
                ll_win3.setVisibility(View.VISIBLE);
                ll_downtimer3.setVisibility(View.GONE);
                tv_name3.setText(newGood2.getClient_name());
            }
        }
        if (results.size() > 1) {
            newGood1 = results.get(1);
            String status = newGood1.getStatus();
            ImageLoader.getInstance().displayImage(newGood1.getGoods_headurl(), iv_img2, ImageLoaderCfg.options);
            tv_title2.setText(newGood1.getGoods_name());

            if ("1".equals(status)) {
                ll_win2.setVisibility(View.GONE);
                ll_downtimer2.setVisibility(View.VISIBLE);
                if (newGood1.getOpen_time() != null) {
                    if (myCd[1] != null) myCd[1].cancel();//取消读缓存的倒计时
                    Long n = Long.parseLong(newGood1.getOpen_time()) - Long.parseLong(time);
                    myCd[1] = new MyCountDownTimer(n, 10, newGood1.getId(), tv_downtimer2, ll_win2, ll_downtimer2, tv_name2).start();//Long.parseLong(newGood2.getOpen_time())
                }
            } else {
                ll_win2.setVisibility(View.VISIBLE);
                ll_downtimer2.setVisibility(View.GONE);
                tv_name2.setText(newGood1.getClient_name());
            }
        }
        if (results.size() > 0) {
            newGood = results.get(0);
            String status = newGood.getStatus();
            ImageLoader.getInstance().displayImage(newGood.getGoods_headurl(), iv_img1, ImageLoaderCfg.options);
            tv_title1.setText(newGood.getGoods_name());
            if ("1".equals(status)) {
                ll_win1.setVisibility(View.GONE);
                ll_downtimer1.setVisibility(View.VISIBLE);
                if (myCd[0] != null) myCd[0].cancel();//取消读缓存的倒计时
                Long n = Long.parseLong(newGood.getOpen_time()) - Long.parseLong(time);
                myCd[0] = new MyCountDownTimer(n, 10, newGood.getId(), tv_downtimer1, ll_win1, ll_downtimer1, tv_name1).start();//Long.parseLong(newGood2.getOpen_time())
            } else {
                ll_win1.setVisibility(View.VISIBLE);
                ll_downtimer1.setVisibility(View.GONE);
                tv_name1.setText(newGood.getClient_name());
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.iv_search:
                intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_message:
//                Session.SetBoolean("isMessage",false);
//                iv_shopping.setVisibility(View.GONE);
                intent = new Intent(getActivity(), MessageActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_shaidan:
                intent = new Intent(getActivity(), ShareOrderActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_chongzhi:
                if (VisitorMode.isVistor(getActivity())) {
                    return;
                }
                intent = new Intent(getActivity(), RechargeActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_quanbushangpin:
                intent = new Intent(getActivity(), AllGoodsActivity.class);
                intent.putExtra("title", "全部商品");
                intent.putExtra("isten", "2");
                LogUtil.e("single", "2");
                startActivity(intent);
                break;
            case R.id.ll_changjianwenti:
                intent = new Intent(getActivity(), CommonQuestionActivity.class);
                startActivity(intent);
                break;

            //热门商品123
            case R.id.ll_hot1:
                if(newGood!=null){
                    intent = new Intent(getActivity(), PrizeDetailsActivity.class);
                    intent.putExtra("singleGoodsId", newGood.getId());
//                intent.putExtra("banner",newGood.getGoods_headurl());
                    startActivity(intent);
                }
                break;
            case R.id.ll_hot2:
                if(newGood1!=null) {
                    intent = new Intent(getActivity(), PrizeDetailsActivity.class);
                    intent.putExtra("singleGoodsId", newGood1.getId());
//                intent.putExtra("banner",newGood1.getGoods_headurl());
                    startActivity(intent);
                }
                break;
            case R.id.ll_hot3:
                if(newGood2!=null) {
                    intent = new Intent(getActivity(), PrizeDetailsActivity.class);
                    intent.putExtra("singleGoodsId", newGood2.getId());
//                intent.putExtra("banner",newGood2.getGoods_headurl());
                    startActivity(intent);
                }
                break;
            //显示全部
            case R.id.tv_show_all:
                Message msg1 = MainActivity.mHandler.obtainMessage();
                msg1.what = 1;
                MainActivity.mHandler.sendMessage(msg1);
                break;
        }
    }

    protected void doBusiness() {
        ivSearch.setOnClickListener(this);
        ivMessage.setOnClickListener(this);
        llShaidan.setOnClickListener(this);
        llChongZhi.setOnClickListener(this);
        llAllPrize.setOnClickListener(this);
        llCommonQes.setOnClickListener(this);
        tv_show_all.setOnClickListener(this);

        ll_hot1.setOnClickListener(this);
        ll_hot2.setOnClickListener(this);
        ll_hot3.setOnClickListener(this);
    }

    @Override
    protected RequestParams getRequestParam() {
        RequestParams params = new RequestParams();
        params.put("pageNum", currentPage);
        return params;
    }

    @Override
    protected SubmitType getSubmitType() {
        return SubmitType.POST;
    }

    @Override
    protected String getUri() {
        return Config.HTTP_URL_HEAD + "goods/firstPage";
    }

    //两种解析方式
    //进行全局的解析
    @Override
    protected void showUIData(Object obj) {

        //刷新右上角的小红点
        if (Session.GetInt("iswinMessage") > 0 || Session.GetInt("issendMessage") > 0 || Session.GetBoolean("isSysMessage") == true) {
            iv_shopping.setVisibility(View.VISIBLE);
        } else {
            iv_shopping.setVisibility(View.GONE);
        }

        sList.clear();
        hList.clear();
        results.clear();
        String result = obj.toString();
        if (TextUtils.isEmpty(result))
            return;
        JSONObject json = null;
        List<ListadEntity> adlist = null;
        List<ListgoodsEntity> goodslist = null;
        List<ListPriceEntity> pricelist = null;
        try {
            json = new JSONObject(result);
            // 写广告页数据的缓存
            CacheUtils.setCache(Config.HTTP_URL_HEAD + "goods/firstPage",
                    result, getActivity());
            FirstPage firstPage = new FirstPage(json);
            time = firstPage.getTime();
            LogUtil.e("time_first", time);
            adlist = firstPage.getListad();//获取的数据集合
            goodslist = firstPage.getListgoods();//获取的数据集合
            pricelist = firstPage.getListPrice();//获取的数据集合
            sList.addAll(adlist);
            hList.addAll(goodslist);
            results.addAll(pricelist);
            //初始化轮播图
            if (sList != null && sList.size() != 0) {
                initViewPager();
                initGridview();
            }
            currentPage = 1;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void parseTypeResult(Object obj) {
        //解析方法
        String result = obj.toString();
        if (TextUtils.isEmpty(result))
            return;
        JSONObject json = null;
        List<ListgoodsEntity> hotlist = null;
        try {
            json = new JSONObject(result);
            HotGoods hotGoods = new HotGoods(json);
            hotlist = hotGoods.getHotGoodList();
            if (hotlist.size() > 0) {
                hList.addAll(hotlist);
            } else {
                ToastUtil.showTextToast("没有更多了哦~");
                currentPage--;
            }
            snacheGridAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //初始化轮播图
    public void initViewPager() {
        if (snacheAdapter == null) {
            snacheAdapter = new SnacheAdapter();
            vpSnache.setAdapter(snacheAdapter);

//            vpSnache.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(getActivity(),PrizeDetailsActivity.class);
//                    startActivity(intent);
//                }
//            });

            vpSnache.setCurrentItem(sList.size() * 1000);// 设置item的位置


            // 设置页面滑动监听
            vpSnache.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                // 某一页被选中
                @Override
                public void onPageSelected(int position) {
                    int pos = position % sList.size();

                    llContainer.getChildAt(pos).setEnabled(true);// 被选中的圆点设置为红色
                    llContainer.getChildAt(mLastPointPos).setEnabled(false);// 上一次被选中的圆点设置为灰色

                    mLastPointPos = pos;// 给上一个圆点位置赋值
                }

                // 页面滑动事件
                @Override
                public void onPageScrolled(int position, float positionOffset,
                                           int positionOffsetPixels) {

                }

                // 页面滑动状态发生变化
                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            if (llContainer == null) {
                // 初始化小圆点
                llContainer = (LinearLayout) activeMain.findViewById(R.id.ll_container);

                for (int i = 0; i < sList.size(); i++) {
                    ImageView view = new ImageView(getActivity());
                    // view.setImageResource(R.drawable.shape_circle_default);
                    view.setImageResource(R.drawable.shape_circle_selector);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    if (i != 0) {
                        params.leftMargin = 10;// 从第二个圆点开始设置左边距
                        view.setEnabled(false);// 从第二个圆点开始都设置为不可用
                    }

                    view.setLayoutParams(params);// 给ImageView设置布局参数

                    llContainer.addView(view);
                }
                handler.removeCallbacksAndMessages(null);
                handler.sendEmptyMessageDelayed(0, 3000);// 延时3秒发消息,切换广告条
            }

            // 设置触摸事件
            vpSnache.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {

//                    switch (event.getAction()) {
                    switch (MotionEventCompat.getActionMasked(event)) {
                        case MotionEvent.ACTION_DOWN:
                            // 停止广告播放
                            handler.removeCallbacksAndMessages(null);// 清除handler中的所有消息
                            break;
                        case MotionEvent.ACTION_UP:
                            // 继续播放广告
//                            handler.removeCallbacksAndMessages(null);
//                            handler.sendEmptyMessageDelayed(0, 3000);// 继续延时3秒发消息,形成无限循环
                            break;
                        case MotionEvent.ACTION_CANCEL:// 当按下后,进行上下移动,导致无法响应ACTION_UP,
                            // 此时会响应ACTION_CANCEL, 此处也播放广告条
//                            handler.sendEmptyMessageDelayed(0, 3000);
                            break;
                        default:
                            break;
                    }
                    return false;// 要返回false,让ViewPager可以处理默认滑动效果的事件
                }
            });
        }

        snacheAdapter.notifyDataSetChanged();
    }

    @Override
    public IToastBlock getToastBlock() {
        return null;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        return false;
    }

    //广告轮播适配器
    class SnacheAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            //在此处获取图片url
            ImageView view = new ImageView(getActivity());
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            if (sList.size() != 0) {

                if (!sList.get(position % sList.size()).getImg_url().contains("imageView2")) {
                    sList.get(position % sList.size()).setImg_url(sList.get(position % sList.size()).getImg_url() + "?imageView2/2/w/640");
                }
                ImageLoader.getInstance().displayImage(sList.get(position % sList.size()).getImg_url(), view, ImageLoaderCfg.options);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), PrizeDetailsActivity.class);
                        intent.putExtra("singleGoodsId", sList.get(position % sList.size()).getId());
                        intent.putExtra("banner", sList.get(position % sList.size()).getImg_url());
                        startActivity(intent);
                    }
                });
            }

            container.addView(view);// 将元素布局添加给view的容器
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            container.removeView((View) object);
        }
    }

    //热门宝物适配器
    class SnacheGridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return hList.size();
        }

        @Override
        public ListgoodsEntity getItem(int position) {
            return hList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {

                convertView = View.inflate(getActivity(), R.layout.item_mian_hotgood, null);
                holder = new ViewHolder(convertView);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final ListgoodsEntity hotGood = getItem(position);

            holder.tv_title.setText(hotGood.getGoods_name());
            ImageLoader.getInstance().displayImage(hotGood.getGoods_headurl(), holder.ivImg, ImageLoaderCfg.options);

            Float i = Float.parseFloat(hotGood.getCurrent_join_num()) / Float.parseFloat(hotGood.getAll_join_num());
            String progress = "0";
            if (i != 0) {
                progress = Math.round((i *= 100) > 99 ? 99 : i < 1 ? 1 : i) + "";
            }
            holder.tv_max_buy_count.setText(progress + "%");

            //判断十元商品的图像是否显示
            if ("1".equals(hotGood.getGoods_10())) {
                holder.ico_characteristic.setVisibility(View.VISIBLE);
            } else {
                holder.ico_characteristic.setVisibility(View.GONE);
            }

            holder.pro_buy_count.setProgress(Integer.parseInt(progress));

            //加入清单按钮
            holder.tv_join.setOnClickListener(new removeNumber() {

                @Override
                public void reset() {
                    number = 0;
                }

                private int number = 0;

                @Override
                public void onClick(final View view) {
                    if (VisitorMode.isVistor(getActivity())) {//用来判断用户是否登录
                        return;
                    }
                    if (CommonUtil.isEmpty(Session.GetString("city_id"))) {
                        //未填写收货地址,不允许加入清单
                        VisitorMode.alertToAddress(getActivity());
                        return;
                    }
//                    第一时间发送消息进行动画请求
//                    第二储存点击事件到session中
//                    第三当手指离开按钮2秒，提交到服务器，并且清空session
                    //初始化响应数字
                    ++number;
                    //发送动画消息
                    Message msg1 = MainActivity.mHandler2.obtainMessage();
                    msg1.what = 1;
                    msg1.obj = view;
                    MainActivity.mHandler2.sendMessage(msg1);
                    //1进行添加到购物车的请求
                    handler1.removeCallbacksAndMessages(null);
                    Message msg = new Message();
                    msg.what = number;
                    msg.obj = new Object[]{hotGood, this};
                    handler1.sendMessageDelayed(msg, 500);
                }
            });
            return convertView;
        }
    }

    interface removeNumber extends View.OnClickListener {
        void reset();
    }

    public class ViewHolder {
        private ImageView ivImg;
        private TextView tv_title;
        private TextView tv_max_buy_count;
        private ProgressBar pro_buy_count;
        private TextView tv_join;
        private ImageView ico_characteristic;

        public ViewHolder(View view) {
            ivImg = (ImageView) view.findViewById(R.id.iv_img);
            ico_characteristic = (ImageView) view.findViewById(R.id.ico_characteristic);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_max_buy_count = (TextView) view.findViewById(R.id.tv_max_buy_count);
            pro_buy_count = (ProgressBar) view.findViewById(R.id.pro_buy_count);
            tv_join = (TextView) view.findViewById(R.id.tv_join);
        }
    }

    class MyCountDownTimer extends CountDownTimer {
        /**
         * @param millisInFuture
         * 表示以毫秒为单位 倒计时的总数
         * <p/>
         * 例如 millisInFuture=1000 表示1秒
         * @param countDownInterval
         * 表示 间隔 多少微秒 调用一次 onTick 方法
         * <p/>
         * 例如: countDownInterval =1000 ; 表示每1000毫秒调用一次onTick()
         */
        private int k = 0;

        private String id;
        private TextView view;
        private TextView tvview;
        private LinearLayout llvieww;
        private LinearLayout llviewd;

        public MyCountDownTimer(long millisInFuture, long countDownInterval, String id, TextView view, LinearLayout llvieww, LinearLayout llviewd, TextView tvview) {
            super(millisInFuture, countDownInterval);
            this.id = id;
            this.view = view;
            this.tvview = tvview;
            this.llvieww = llvieww;
            this.llviewd = llviewd;
            LogUtil.e("thread", 1 + "");
        }

        @Override
        public void onFinish() {
            cancel();
            this.view.setText("揭晓中");
            doRequest();
        }

        private void doRequest() {
            AsyncHttpTask.post(Config.HTTP_URL_HEAD + "goods/newSingleOpenGoods", new RequestParams("id", id), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    String result = response.toString();
                    if (TextUtils.isEmpty(result))
                        return;
                    if ("0".equals(response.optString("code"))) {
                        MyCountDownTimer.this.llvieww.setVisibility(View.VISIBLE);
                        MyCountDownTimer.this.llviewd.setVisibility(View.GONE);

                        JSONObject info = response.optJSONObject("info");
                        JSONArray list = info.optJSONArray("list");
                        String name = list.optJSONObject(0).optString("client_name");
                        MyCountDownTimer.this.tvview.setText(name);
                    } else {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        doRequest();
                    }
                }
            });
        }

        @Override
        public void onTick(long millisUntilFinished) {
            //变为分秒毫秒
            int fen = (int) (millisUntilFinished / 1000 / 60);
            int haomiao = (int) millisUntilFinished % 100;
            int miao = (int) ((millisUntilFinished - (fen * 60 * 1000)) / 1000);
            this.view.setText(((fen >= 10) ? fen + "分" : "0" + fen + "分") + ((miao >= 10) ? miao + "秒" : "0" + miao + "秒") + ((haomiao >= 10) ? haomiao : "0" + haomiao));

            /*if(k == 0){
                LogUtil.e("thread",Thread.currentThread().getName());
                k++;
            }*/
        }
    }

    //对点击清单频率进行限制
    public synchronized static boolean isFastClick() {

        long time = System.currentTimeMillis();
        if (time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
