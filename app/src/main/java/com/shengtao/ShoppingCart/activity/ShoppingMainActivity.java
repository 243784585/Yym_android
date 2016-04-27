package com.shengtao.ShoppingCart.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shengtao.R;
import com.shengtao.baseview.BaseActivity;
import com.shengtao.baseview.TipsLayoutNormal;
import com.shengtao.domain.Config;
import com.shengtao.domain.shopping.ShoppingBuyDetail;
import com.shengtao.foundation.AsyncHttpTask;
import com.shengtao.main.MainActivity;
import com.shengtao.utils.LargeGridView;
import com.shengtao.utils.ShareDialog;
import com.shengtao.utils.ShareUtils;
import com.shengtao.utils.ShareUtils1;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
/*
*购物车--支付结果
* */

public class ShoppingMainActivity extends BaseActivity implements View.OnClickListener {

    private TextView btn_buy, btn_buy_logs, tv_buy_num, tv_buy_count;
    private ArrayList<ShoppingBuyDetail> sList;
    private ListView lv_shopping;

    private TipsLayoutNormal loading;
    private UMSocialService mController;
    private String number;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sList = (ArrayList<ShoppingBuyDetail>) getIntent().getSerializableExtra("sList");
        number = (String) getIntent().getSerializableExtra("number");
        super.onCreate(savedInstanceState);
        initView();
        doBusiness();


    }

    @Override
    protected int setLayout() {
        return R.layout.shopping_pay_end;
    }

    @Override
    protected String getAvtionTitle() {
        return "支付结果";
    }

    @Override
    public int getHeaderType() {
        return 1;
    }

    //初始化数据
    protected void initView() {
        lv_shopping = (ListView) findViewById(R.id.lv_shopping);
        btn_buy = (TextView) findViewById(R.id.btn_buy);//继续夺宝
        btn_buy_logs = (TextView) findViewById(R.id.btn_buy_logs);//分享获得夺宝机会
        tv_buy_num = (TextView) findViewById(R.id.tv_buy_num);//多少件商品
        tv_buy_num.setText(Integer.toString(sList.size()));
        tv_buy_count = (TextView) findViewById(R.id.tv_buy_count);//多少人次
        tv_buy_count.setText(number);
        filtData();
    }

    private void filtData() {
        ShoppingResultAdapter adapter = new ShoppingResultAdapter();
        lv_shopping.setAdapter(adapter);
    }

    protected void doBusiness() {
        btn_buy.setOnClickListener(this);
        btn_buy_logs.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //继续购买
            case R.id.btn_buy:
                Intent intent = new Intent(ShoppingMainActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                Session.SetBoolean("isback", true);
                startActivity(intent);
                finish();
                break;
            //分享
            case R.id.btn_buy_logs:
                mController = UMServiceFactory.getUMSocialService("com.umeng.share");
                //分享配置
                ShareUtils.configPlatforms(ShoppingMainActivity.this, "http://120.25.145.15/oneDream.html","lala");
                ShareDialog shareDialog1 = new ShareDialog(ShoppingMainActivity.this, ShoppingMainActivity.this);
                shareDialog1.show();
                mController = UMServiceFactory.getUMSocialService("com.umeng.share");

                break;
            case R.id.ll_wechat:
                ShareUtils1.shareActive(this, SHARE_MEDIA.WEIXIN);
                break;
            case R.id.ll_wechat_friend:
                ShareUtils1.shareActive(this,  SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case R.id.ll_qq:
                ShareUtils.shareActive(this, SHARE_MEDIA.QQ, "");
                break;
            case R.id.ll_qzone:
                ShareUtils.shareActive(this, SHARE_MEDIA.QZONE,"");
                break;
            case R.id.ll_sina:
                ShareUtils.promtoteSinaActive(this);
                break;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    //分享成功请求服务器
    private void ShareSuccess(){
        String url = Config.HTTP_URL_HEADED + "Zerogoods/SharegetZeroCode";
        RequestParams reqParams = new RequestParams();
        reqParams.put("shopcartid", getIntent().getExtras().get("goodsDetail"));
        AsyncHttpTask.post(url, reqParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                String result = response.toString();
                if (TextUtils.isEmpty(result))
                    return;
                if ("0".equals(response.optString("code"))) {
                    //添加成功之后需要重新请求一次数据
                    System.out.println("添加成功&&&&&&&&&&&&&&&&&&&&&&&");
                    //getShopcarData();
                } else if ("7".equals(response.optString("code"))) {
                    System.out.println("没token");
                } else {
                    System.out.println("参数异常");
                }
            }
        });
    }

    class ShoppingResultAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return sList.size();
        }

        @Override
        public ShoppingBuyDetail getItem(int position) {
            return sList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = View.inflate(ShoppingMainActivity.this, R.layout.buy_success_tip_item, null);
            }
            final ViewHolder holder = ViewHolder.getHolder(convertView);
            final ShoppingBuyDetail info = sList.get(position);
            holder.tv_prize_title.setText("第" + info.getCurrentNum() + "期 " + info.getGoodsName());
            holder.tv_prize_buy_count.setText(info.getBuyNum());
            final String[] buyCode = info.getBuyCode().split(",");

            if (!("-1").equals(buyCode[0])) {
                holder.lgvLuckyNo.setAdapter(new ArrayAdapter<String>(ShoppingMainActivity.this, R.layout.arrayadapter_textview, R.id.tv, buyCode) {

                    @Override
                    public int getCount() {
                        return buyCode.length > 20 ? 20 : buyCode.length;
                    }
                });
                if (buyCode.length > 20) {
                    //打开UI线程,设置查看全部的点击事件
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            holder.tvLuckyNoMore.setVisibility(View.VISIBLE);
                            holder.tvLuckyNoMore.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //弹出对话框,显示所有夺宝号
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ShoppingMainActivity.this);
                                    final AlertDialog dialog = builder.create();
                                    View v = View.inflate(ShoppingMainActivity.this, R.layout.check_snach_no, null);
                                    v.findViewById(R.id.tv_onclick).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                                    ((TextView) v.findViewById(R.id.pop_title)).setText(info.getGoodsName());//晒单标题
                                    ((TextView) v.findViewById(R.id.pop_number)).setText("参与" + info.getBuyNum() + "次，夺宝号码");//晒单参与次数
                                    ((GridView) v.findViewById(R.id.gv_snach_no)).setAdapter(new ArrayAdapter<String>(ShoppingMainActivity.this, R.layout.arrayadapter_textview, R.id.tv, buyCode));

                                    dialog.setView(v);
                                    dialog.show();
                                }
                            });

                        }
                    });
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            holder.tvLuckyNoMore.setVisibility(View.GONE);
                        }
                    });
                }
            }else{
                //否则就只显示失败样式，隐藏购买吗listview
                holder.tv_luck_failed.setText("\""+info.getGoodsName() + "\":购买失败,已退回抢币,请查收！");
                holder.tv_luck_failed.setVisibility(View.VISIBLE);
                holder.lgvLuckyNo.setVisibility(View.GONE);
            }
            return convertView;
        }
    }

    static class ViewHolder {
        TextView tv_prize_title, tv_prize_buy_count, tv_lucky_no, tvLuckyNoMore, tv_luck_failed;
        ImageView prize_img;
        LargeGridView lgvLuckyNo;

        public ViewHolder(View convertView) {
            tv_prize_buy_count = (TextView) convertView.findViewById(R.id.tv_prize_buy_count);//参与人次
            prize_img = (ImageView) convertView.findViewById(R.id.prize_img);
            tv_prize_title = (TextView) convertView.findViewById(R.id.tv_prize_title);//商品名称
            tv_lucky_no = (TextView) convertView.findViewById(R.id.tv_lucky_no);//幸运号码
            lgvLuckyNo = (LargeGridView) convertView.findViewById(R.id.lgv_lucky_no);
            tvLuckyNoMore = (TextView) convertView.findViewById(R.id.btn_lucynum_more);
            tv_luck_failed = (TextView) convertView.findViewById(R.id.tv_luck_failed);
        }

        public static ViewHolder getHolder(View convertView) {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            if (holder == null) {
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }
            return holder;
        }
    }
}
