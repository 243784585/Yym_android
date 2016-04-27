package com.shengtao.snache.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengtao.R;
import com.shengtao.application.ImageLoaderCfg;
import com.shengtao.baseview.BaseListActivity;
import com.shengtao.domain.Config;
import com.shengtao.domain.snache.SResult;
import com.shengtao.domain.snache.SearchResult;
import com.shengtao.foundation.AsyncHttpTask;
import com.shengtao.foundation.BaseEnitity;
import com.shengtao.foundation.SubmitType;
import com.shengtao.main.MainActivity;
import com.shengtao.utils.BezierAnimation;
import com.shengtao.utils.CommonUtil;
import com.shengtao.utils.LogUtil;
import com.shengtao.utils.Session;
import com.shengtao.utils.ToastUtil;
import com.shengtao.utils.VisitorMode;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @package com.baixi.snache.activity
 * Created by TT on 2015/12/21.
 * Description:全部商品页面
 */
public class AllGoodsActivity extends BaseListActivity {

    private PullToRefreshListView lvSearchResult;
    private String title;
    private String url = "goods/SearchByName";
    private ArrayList<SResult> sList;
    private SearchResultAdapter mSearchAdapter;
    private String isten;
    private FrameLayout flContainer;
    private LinearLayout redPoint;
    private ImageView iv;//购物车图标
    private TextView tv_shopping;
    private static long lastClickTime;
    //进行动画操作
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //红点显示
                    tv_shopping.setText(Session.GetInt("cartnum") + "");
                    if(Session.GetInt("cartnum")>0){
                        redPoint.setVisibility(View.VISIBLE);
                    }else{
                        redPoint.setVisibility(View.INVISIBLE);
                    }
                    new BezierAnimation(flContainer, getLayoutInflater().inflate(R.layout.cart_anim, flContainer, false), (View) msg.obj) {

                    @Override
                    protected List<Animator> getAnimators() {
                        ArrayList<Animator> animators = new ArrayList<>();
                        animators.add(ObjectAnimator.ofFloat(iv, "scaleX", 1.0f, 1.2f).setDuration(300));
                        animators.add(ObjectAnimator.ofFloat(iv, "scaleX", 1.2f, 1.0f).setDuration(300));
                        return animators;
                    }

                    @Override
                    protected View getEndView() {
                        return redPoint;
                    }
                }.showAnimation();
                    break;

                case 2:
                    //红点显示
                    tv_shopping.setText(Session.GetInt("cartnum") + "");
                    if(Session.GetInt("cartnum")>0){
                        redPoint.setVisibility(View.VISIBLE);
                    }else{
                        redPoint.setVisibility(View.INVISIBLE);
                    }
                    break;
            }
        }
    };

    Handler handler1 = new Handler(){
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            Object o[] = (Object[]) msg.obj;
            ((removeNumber)o[1]).reset();
            String url = Config.HTTP_URL_HEAD + "goods/AddShoppingCart";
            RequestParams reqParams = new RequestParams();
            //1判断是否点击的是十元商品
            reqParams.put("id", ((SResult) o[0]).getId());
            if ("1".equals(((SResult) o[0]).getGoods_10())) {
                reqParams.put("number", msg.what * 10);
                LogUtil.e("log1",msg.what + "");
            } else {
//                reqParams.put("id", ((SResult) o[0]).getId());
                reqParams.put("number", msg.what);
                LogUtil.e("log1", msg.what + "");
            }
            LogUtil.e("numbernow",reqParams.toString());
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
                        handler.obtainMessage(2).sendToTarget();//成功后更新下当前购物车数量，防止数量不一致的产生
//                        LogUtil.e("numbernow", (removeNumber)o[1] + "");
                    } else if ("4".equals(response.optString("code"))) {
                        ToastUtil.showTextToast(response.optString("error"));
                    }
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        handler.obtainMessage(2).sendToTarget();
    }

    @Override
    protected void initViewData() {
        isten = getIntent().getStringExtra("isten");
        title = getIntent().getStringExtra("title");
        super.initViewData();
    }

    protected void initView() {
        flContainer = (FrameLayout) findViewById(R.id.fl_animcotainer);
        iv = (ImageView) findViewById(R.id.img_shopcart_ico);
        redPoint = (LinearLayout) findViewById(R.id.layout_shopcart_num);
        tv_shopping = (TextView) findViewById(R.id.tv_shopcart_num);
    }

    @Override
    protected int getPullToRefreshListViewResouceId() {
        return R.id.prlv_details;
    }

    @Override
    protected int getLayoutResouceId() {
        return R.layout.activity_search_result;
    }

    @Override
    protected String getAvtionTitle() {
        return title;
    }

    @Override
    protected Object getHeaderRight() {
        View inflate = getLayoutInflater().inflate(R.layout.shopcart_redpoint, null);
        return inflate;
    }

    @Override
    protected void setAdapter() {
        if (mSearchAdapter == null) {
            // 在这里填充adapter
            mSearchAdapter = new SearchResultAdapter();
            super.prlv_details.setAdapter(mSearchAdapter);
        }

        if (sList.size() == 0){
            showEmptyView("暂无数据，请重新选择");
        }
        mSearchAdapter.notifyDataSetChanged();
    }

    @Override
    protected <T extends BaseEnitity> List<T> getDataList() {
        return null;
    }

    @Override
    protected RequestParams getRequestParam() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("pageNum", mCurPageIndex);
        requestParams.put("type", title);
        requestParams.put("isTen", isten);
        LogUtil.e("pageNum",mCurPageIndex+"");
        return requestParams;
    }

    @Override
    protected RequestParams getRefreshRequestParam() {
        return getRequestParam();
    }

    @Override
    protected void parseResult(String result, boolean isRefresh) {
        if (TextUtils.isEmpty(result)) {
            showEmptyView("暂时没有商品哦哦...");
            return;
        }
        //集合
        List<SResult> results = new ArrayList<>();
        if(sList==null){
            //存储集合
            sList = new ArrayList();
        }
        try {
            JSONObject json = new JSONObject(result);
            SearchResult data = new SearchResult(json);
            results = data.getSearchList();
            if (isRefresh) {
                sList.clear();
            }
            if (results.size() > 0) {
                sList.addAll(results);
            } else {
                ToastUtil.loadMoreTips(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getUri() {
        return Config.HTTP_URL_HEAD + "goods/SearchByType";
    }

    @Override
    protected SubmitType getSubmitType() {
        return SubmitType.POST;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.header_right:
                //购物车图标,跳转到购物车
                //跳入首页
                Intent intent= new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Session.SetBoolean("isback", true);
                startActivity(intent);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        SResult item = mSearchAdapter.getItem(i-1);
        Intent intent = new Intent(AllGoodsActivity.this,PrizeDetailsActivity.class);
        intent.putExtra("singleGoodsId",item.getId());
        intent.putExtra("banner",item.getGoods_headurl());
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        return false;
    }

    class SearchResultAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return sList.size();
        }

        @Override
        public SResult getItem(int position) {
            return sList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(), R.layout.item_search_result, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final SResult sResult = getItem(position);
            ImageLoader.getInstance().displayImage(sResult.getGoods_headurl(), viewHolder.prize_img, ImageLoaderCfg.options);

            SpannableStringBuilder style=new SpannableStringBuilder("(第" + sResult.getGoods_current_num() + "期)    " + sResult.getGoods_name());
            //SpannableStringBuilder实现CharSequence接口
            style.setSpan(new ForegroundColorSpan(Color.GRAY), 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE );
            viewHolder.tv_prize_title.setText(style);

//            viewHolder.tv_prize_title.setText("(第" + sResult.getCurrent_join_num() + "期)" + sResult.getGoods_name());
            viewHolder.tv_buy_count.setText(sResult.getCurrent_join_num());

            //剩余的数量
            Integer i = Integer.parseInt(sResult.getAll_join_num())-Integer.parseInt(sResult.getCurrent_join_num());
            viewHolder.tv_surplus_buy_count.setText(i.toString());

            //进度
            Float j = (Float.valueOf(sResult.getCurrent_join_num()))/Float.valueOf(sResult.getAll_join_num())*100;
            viewHolder.pro_buy_count.setProgress(j > 99 ? 99 : j < 1 && j!=0? 1 : j.intValue());

            //判断是否是10元商品
            if("1".equals(sResult.getGoods_10())){
                viewHolder.ico_characteristic.setVisibility(View.VISIBLE);
            }else{
                viewHolder.ico_characteristic.setVisibility(View.GONE);
            }
            viewHolder.tv_join_manifest.setOnClickListener(new removeNumber() {

                @Override
                public void reset() {
                    number = 0;
                }

                private int number = 0;

                @Override
                public void onClick(View view) {
                    if(VisitorMode.isVistor(AllGoodsActivity.this)){//用来判断用户是否登录
                        return;
                    }
                    if(CommonUtil.isEmpty(Session.GetString("city_id"))){
                        //未填写收货地址,不允许加入清单
                        VisitorMode.alertToAddress(AllGoodsActivity.this);
                        return;
                    }
                    //加入购物车
                    //进行添加到购物车的请求
                    //第一时间发送消息进行动画请求
//                    第二储存点击事件到session中
//                    第三当手指离开按钮2秒，提交到服务器，并且清空session
                    //初始化响应数字
//                    if (isFastClick()) {
//                        ++number;
//                        LogUtil.e("number+",number+"");
//                        handler1.removeCallbacksAndMessages(null);

//                    }else{
//                        //1进行添加到购物车的请求
//                        number+=1;
//                        LogUtil.e("number",number+"");
//                        //发送动画消息
//                        handler.obtainMessage(1,view).sendToTarget();
////                        Message msg1 = MainActivity.mHandler2.obtainMessage();
////                        msg1.what = 1;
////                        msg1.obj = view;
////                        MainActivity.mHandler2.sendMessage(msg1);
//
//                        handler1.obtainMessage(number,sResult).sendToTarget();
//                    }
                    ++number;
                    //发送动画消息
                    LogUtil.e("number",number+"");
                    handler.obtainMessage(1,view).sendToTarget();
                    handler1.removeCallbacksAndMessages(null);
                    Message msg = new Message();
                    msg.what = number;
                    msg.obj = new Object[]{sResult,this};
                    handler1.sendMessageDelayed(msg,500);

//                    String url = Config.HTTP_URL_HEAD + "goods/AddShoppingCart";
//                    RequestParams reqParams = new RequestParams();
//                    reqParams.add("id", sResult.getId());
//                    reqParams.add("number", "1");
//                    AsyncHttpTask.post(url, reqParams, new JsonHttpResponseHandler() {
//                        @Override
//                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                            String result = response.toString();
//                            if (TextUtils.isEmpty(result))
//                                return;
//                            if ("0".equals(response.optString("code"))) {
//                                //加入成功
//                                //发送动画消息,增加数量并显示
//                                Session.SetInt("cartnum", Integer.parseInt(response.optString("info")));
//                                Message msg1 = MainActivity.mHandler2.obtainMessage();
//                                msg1.what = 1;
//                                MainActivity.mHandler2.sendMessage(msg1);
//                            } else if ("4".equals(response.optString("code"))) {
//                                Message msg1 = MainActivity.mHandler2.obtainMessage();
//                                msg1.what = 2;
//                                MainActivity.mHandler2.sendMessage(msg1);
//                            }else{
//                                ToastUtil.showTextToast(response.optString("error"));
//                            }
//                        }
//                    });
                }
            });
            viewHolder.prize_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AllGoodsActivity.this, PrizeDetailsActivity.class);
                    intent.putExtra("singleGoodsId",sResult.getId());
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }

    interface removeNumber extends View.OnClickListener{
        void reset();
    }

    public class ViewHolder {
        private ImageView prize_img;
        private TextView tv_prize_title;
        private ProgressBar pro_buy_count;
        private TextView tv_buy_count;
        private TextView tv_surplus_buy_count;
        private ImageView ico_characteristic;
        private TextView tv_join_manifest;

        public ViewHolder(View view) {
            prize_img = (ImageView) view.findViewById(R.id.prize_img);
            tv_prize_title = (TextView) view.findViewById(R.id.tv_prize_title);
            pro_buy_count = (ProgressBar) view.findViewById(R.id.pro_buy_count);
            tv_buy_count = (TextView) view.findViewById(R.id.tv_buy_count);
            tv_surplus_buy_count = (TextView) view.findViewById(R.id.tv_surplus_buy_count);
            ico_characteristic = (ImageView) view.findViewById(R.id.ico_characteristic);
            tv_join_manifest = (TextView) view.findViewById(R.id.tv_join_manifest);
        }
    }


    //对点击清单频率进行限制
    public synchronized static boolean isFastClick() {

        long time = System.currentTimeMillis();
        if (time - lastClickTime < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
