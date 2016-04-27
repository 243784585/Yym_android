package com.shengtao.ShoppingCart.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iapppay.interfaces.callback.IPayResultCallback;
import com.iapppay.sdk.main.IAppPay;
import com.iapppay.sdk.main.IAppPayOrderUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shengtao.R;
import com.shengtao.baseview.BaseActivity;
import com.shengtao.domain.Config;
import com.shengtao.domain.shopping.ShoppingBuy;
import com.shengtao.domain.shopping.ShoppingBuyDetail;
import com.shengtao.domain.shopping.ShoppingCartGoodsDetail;
import com.shengtao.foundation.AsyncHttpTask;
import com.shengtao.utils.LogUtil;
import com.shengtao.utils.Session;
import com.shengtao.utils.ToastUtil;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Scoot on 2015/12/9.
 * <p/>
 * 提交订单
 */
public class ShoppingSubmitOrder extends BaseActivity implements View.OnClickListener {

    private LinearLayout layout_prize_list, layout_all_count;
    private Button btn_submit;
    private TextView title, tv_prize_title, tv_buy_count_tip, tv_all_count;
    private ImageView back_image, action_image, ico_all_count, ico_balance, ico_alipay, ico_weixin, ico_wangyin;
    private ImageView ivPay[] = new ImageView[3];
    private RelativeLayout app_top_bar;
    private boolean cick = true;
    private ArrayList<ShoppingCartGoodsDetail> resultdddd;
    private int num = 0;
    private String number, rmb;
    private ArrayList<ShoppingBuyDetail> sList;

    private RelativeLayout rl_detail;
    /**
     * 选中支付方式的索引,0:支付宝、1:微信、2:余额
     */
    private int curIndex;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resultdddd = (ArrayList<ShoppingCartGoodsDetail>) getIntent().getSerializableExtra("pListddddd");

        initView();
        doClick();
        showUIData();
    }

    @Override
    protected int setLayout() {
        return R.layout.shopping_cart_submit;
    }

    @Override
    public int getHeaderType() {
        return 1;
    }

    @Override
    protected String getAvtionTitle() {
        return "提交订单";
    }

//    @Override
//    public Integer getHeaderRight() {
//        return R.drawable.question;
//    }

    //初始化
    @Override
    public void initView() {
        rl_detail = (RelativeLayout) findViewById(R.id.rl_detail);

        layout_prize_list = (LinearLayout) findViewById(R.id.layout_prize_list);
        layout_all_count = (LinearLayout) findViewById(R.id.layout_all_count);
        btn_submit = (Button) findViewById(R.id.btn_submit);
//        title = (TextView) findViewById(R.id.title);//标题栏
        ico_all_count = (ImageView) findViewById(R.id.ico_all_count);//下拉
        ico_all_count.setBackgroundResource(R.mipmap.xialajiantou);
//        title.setText("提交订单");
        ico_balance = (ImageView) findViewById(R.id.ico_balance);//余额支付点击按钮
//        back_image = (ImageView) findViewById(R.id.back_image);
//        back_image.setVisibility(View.VISIBLE);
//        action_image = (ImageView) findViewById(R.id.action_image);
//        action_image.setVisibility(View.VISIBLE);
//        app_top_bar = (RelativeLayout) findViewById(R.id.app_top_bar);
//        app_top_bar.setBackgroundColor(getResources().getColor(R.color.yym_head_space));
        tv_all_count = (TextView) findViewById(R.id.tv_all_count);


        ivPay[0] = (ImageView) findViewById(R.id.ico_alipay);//支付宝按钮
        ivPay[1] = (ImageView) findViewById(R.id.ico_weixin);//微信
        ivPay[2] = (ImageView) findViewById(R.id.ico_balance);//余额


    }

    @Override
    protected void doBusiness() {

    }

    //点击事件的处理
    private void doClick() {
        btn_submit.setOnClickListener(this);
        layout_all_count.setOnClickListener(this);
        rl_detail.setOnClickListener(this);
//        back_image.setOnClickListener(this);

//        findViewById(R.id.title_icon).setOnClickListener(this);
        findViewById(R.id.cb_alipay).setOnClickListener(this);
        findViewById(R.id.cb_weixin).setOnClickListener(this);
        findViewById(R.id.cb_balance).setOnClickListener(this);
    }


    protected void showUIData() {

        for (int i = 0; i < resultdddd.size(); i++) {
            if (resultdddd.size() > 0) {
                View view = View.inflate(this, R.layout.shopping_cart_submit_item, null);
                tv_prize_title = (TextView) view.findViewById(R.id.tv_prize_title);//奖品名称
                tv_buy_count_tip = (TextView) view.findViewById(R.id.tv_buy_count_tip);//人次
                tv_prize_title.setText(resultdddd.get(i).getGoodsname());
                tv_buy_count_tip.setText(resultdddd.get(i).getNumber() + "人次");
                layout_prize_list.addView(view);

                int price = Integer.parseInt(resultdddd.get(i).getNumber());

                num += price;
            }
            tv_all_count.setText(Integer.toString(num) + "抢币");
        }
        //设置抢币余额、余额不足时禁用余额支付选项
        TextView tvBalance = (TextView) findViewById(R.id.tv_user_balance);
        String str = tvBalance.getText().toString().trim();
        if (Integer.parseInt(Session.GetString("rmb")) < num) {
            tvBalance.setTextColor(Color.parseColor("#cccccc"));
            findViewById(R.id.cb_balance).setEnabled(false);
            str = str.replace("0", Session.GetString("rmb"));
        } else {
            str = str.replace("0", "<font color='#FF4447'>" + Session.GetString("rmb") + "</font>");
            selectThis(2);
        }
        tvBalance.setText(Html.fromHtml(str));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_icon:
                LogUtil.e("haha","jeje");
                break;
            //确认提交按钮
            case R.id.btn_submit:
                switch (curIndex) {
                    case 0:
                        ToastUtil.makeText(this, "请选择支付方式");
                        break;
                    case 3:
                        //余额
                        shoppingBuy();
                        break;
                    case 2:
                        //微信
                    case 1:
                        //支付宝
                        if(!isNetworkAvailable()){
                            ToastUtil.makeText(this, "请检查网络环境");
                            return ;
                        }
                        getTransid();
                }
                break;
            //返回
            case R.id.back_image:
                finish();
                break;
            //奖品合计按钮
            case R.id.layout_all_count:
                if (cick) {
                    rl_detail.setVisibility(View.VISIBLE);
                    ico_all_count.setBackgroundResource(R.mipmap.xiangshang);
                    cick = false;
                } else {
                    rl_detail.setVisibility(View.GONE);
                    ico_all_count.setBackgroundResource(R.mipmap.xialajiantou);
                    cick = true;
                }
                break;
            case R.id.cb_alipay://选择支付宝支付
                selectThis(0);
                break;
            case R.id.cb_weixin://选择微信支付
                selectThis(1);
                break;
            case R.id.cb_balance://选择余额支付
                selectThis(2);
                break;
//            case R.id.header_right:
//                startActivity(new Intent(this, SnacheStatement.class));
//                break;
        }
    }

    private void selectThis(int position) {
        for (int i = 0, len = ivPay.length; i < len; i++) {
            if (i != position) {
                ivPay[i].setSelected(false);
            } else {
                ivPay[i].setSelected(true);
                curIndex = i + 1;
            }
        }
    }


    //抢币支付请求服务器
    private void shoppingBuy() {
        String url = Config.HTTP_URL_HEADED + "shoppingcart/buy";
        RequestParams requestParams = new RequestParams();
        requestParams.put("rmbNum", num);
        AsyncHttpTask.post(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                String result = response.toString();
                if (TextUtils.isEmpty(result))
                    return;
                if ("0".equals(response.optString("code"))) {
                    try {
                        JSONObject json = new JSONObject(result);
                        LogUtil.e("sList",result);

                        ShoppingBuy shoppingBuy = new ShoppingBuy(json);
                        number = shoppingBuy.getBuynumber();
                        rmb = shoppingBuy.getUserRMB();
                        Session.SetString("rmb", rmb);
                        sList = shoppingBuy.getShoppingBuyDetails();

                        //当购买成功向Fragment发送广播通知其刷新
                        Intent intent = new Intent();
                        intent.setAction("com.send");        //设置Action
                        sendBroadcast(intent);

                        balanceBuy();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if ("8".equals(response.optString("code"))) {
                    Toast.makeText(ShoppingSubmitOrder.this, "抢币不足", Toast.LENGTH_SHORT).show();
                } else if ("9".equals(response.optString("code"))) {
                    Toast.makeText(ShoppingSubmitOrder.this, "购物车空空如也", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private String cporderid;//订单号
    /**
     * 从后台获取transid
     */
    public void getTransid(){
        RequestParams reqParams = new RequestParams("cporderid",cporderid = new Date().getTime()+Session.GetString("mobile"));
        reqParams.put("price", num);//num*0.01
        AsyncHttpTask.post(Config.HTTP_URL_HEADED +"apppay/depositForAndriod", reqParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                String result = response.toString();
                if (TextUtils.isEmpty(result))
                    return;
                if ("0".equals(response.optString("code"))) {
                    IAppPay.startPay(ShoppingSubmitOrder.this, new StringBuilder("transid=").append(response.optString("info")).append("&appid=").append(Config.APP_ID_AIBEI)/*.append("&cporderid=").append(cporderid)*/.toString().trim(), mIPayResultCallback, curIndex == 1 ? 401 : 403);
                }
            }
        });
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) btn_submit.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        //获取可用的网络信息
        NetworkInfo networkinfo = manager.getActiveNetworkInfo();
        if (networkinfo != null && networkinfo.isConnected()) {
            //网络可用
            return true;
        }
        //网络不可用
        return false;
    }

    /**支付回调*/
    IPayResultCallback mIPayResultCallback  = new IPayResultCallback() {

        //@Override
        public void onPayResult(int resultCode, String signvalue, String resultInfo) {
            // TODO Auto-generated method stub

            switch (resultCode) {
                case IAppPay.PAY_SUCCESS:
                    boolean isPaySuccess = IAppPayOrderUtils.checkPayResult(signvalue, Config.PUBLIC_KEY_AIBEI);
                    if(isPaySuccess){
                        doRequest();
                    }
                    break;
                case IAppPay.PAY_ERROR:
                    ToastUtil.showTextToast("支付失败");
                    break;
                case IAppPay.PAY_CANCEL:
                    ToastUtil.showTextToast("支付取消");
                    break;
            }
        }

    };

    //支付成功跳转至支付结果页面
    private void balanceBuy() {
        Session.SetInt("curtnum",0);
        startActivity(new Intent(ShoppingSubmitOrder.this, ShoppingMainActivity.class).putExtra("sList", sList).putExtra("number", number));
        finish();
    }


    private void doRequest(){
        RequestParams params = new RequestParams("rmbNum", num);//1
        params.put("tradeNum",cporderid);
        AsyncHttpTask.post(Config.HTTP_MODULE_SHOPPING + "shoppingcart/buyOnline",params,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                if(0 == json.optInt("code")){
                    ShoppingBuy shoppingBuy = new ShoppingBuy(json);
                    number = shoppingBuy.getBuynumber();
                    rmb = shoppingBuy.getUserRMB();
                    Session.SetString("rmb", rmb);
                    rmb = null;
                    sList = shoppingBuy.getShoppingBuyDetails();

                    //当购买成功向Fragment发送广播通知其刷新
                    Intent intent = new Intent();
                    intent.setAction("com.send");        //设置Action
                    sendBroadcast(intent);

                    balanceBuy();
                }else if(8 == json.optInt("code")){
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    doRequest();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                LogUtil.e("HttpRequestError(" + statusCode + "):", responseString);
            }
        });
    }

}
