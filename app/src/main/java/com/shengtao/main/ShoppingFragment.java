package com.shengtao.main;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengtao.R;
import com.shengtao.ShoppingCart.activity.AddAndSub;
import com.shengtao.ShoppingCart.activity.ShoppingSubmitOrder;
import com.shengtao.baseview.BaseListFragment1;
import com.shengtao.domain.Config;
import com.shengtao.domain.shopping.ShoppingCartGoods;
import com.shengtao.domain.shopping.ShoppingCartGoodsDetail;
import com.shengtao.foundation.AsyncHttpTask;
import com.shengtao.foundation.IToastBlock;
import com.shengtao.foundation.SubmitType;
import com.shengtao.snache.activity.PrizeDetailsActivity;
import com.shengtao.utils.CommonUtil;
import com.shengtao.utils.Session;
import com.shengtao.utils.StringUtil;
import com.shengtao.utils.ToastUtil;
import com.shengtao.utils.VisitorMode;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 购物车
 */


public class ShoppingFragment extends BaseListFragment1 implements View.OnClickListener {


    private View activeMain;
    private Button btn_go_to_buy;
    private TextView tv_shopping_submit, action_title, title, tv_prize_count, tv_money_count, tv_delete, tv_money_cot;
    private PullToRefreshListView pull_refresh_shoppcart;
    private LinearLayout prize_buy, prize_bu, layout_shoppcart_null;
    private ImageView cb_choice;
    private boolean ischecked;
    private boolean cick = true;
    private ArrayList<ShoppingCartGoods> lists;
    private ArrayList<ShoppingCartGoodsDetail> pList;
    private ShoppingCartAdpater mShoppingCartAdpater;
    private RelativeLayout layout_shoppcart_main, rl_sub;
    private NetworkInfo activeInfo;
    private LinearLayout prize_bue;
    private ArrayList<ShoppingCartGoodsDetail> selectList = new ArrayList<ShoppingCartGoodsDetail>();//被选中的列表
    private static long lastClickTime;
    private HashMap<String, Integer> countMap = new HashMap<String, Integer>();
    private int num;// editText中的数值
    private double returnValue;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            sendEmptyBackgroundMessage(MSG_BACK_GET_LIST);
        }
    };
    private InputMethodManager imm;



    static ShoppingFragment newInstance(String s) {
        ShoppingFragment newFragment = new ShoppingFragment();
        Bundle bundle = new Bundle();
        bundle.putString("hello", s);
        newFragment.setArguments(bundle);
        //bundle还可以在每个标签里传送数据
        return newFragment;
    }


    @Override
    public IToastBlock getToastBlock() {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 判断一下有没网络
        ConnectivityManager manager = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        activeInfo = manager.getActiveNetworkInfo();
        if (activeInfo == null) {
            sendEmptyBackgroundMessage(MSG_BACK_GET_LIST);
        }
        imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activeMain = inflater.inflate(R.layout.shopping, container, false);
        super.initViewData(activeMain);

        btn_go_to_buy = (Button) activeMain.findViewById(R.id.btn_go_to_buy);
        tv_shopping_submit = (TextView) activeMain.findViewById(R.id.tv_shopping_submit);
        tv_delete = (TextView) activeMain.findViewById(R.id.tv_delete);//删除按钮
        pull_refresh_shoppcart = (PullToRefreshListView) activeMain.findViewById(R.id.pull_refresh_shoppcart);
        title = (TextView) activeMain.findViewById(R.id.title);
        action_title = (TextView) activeMain.findViewById(R.id.action_title);
        prize_buy = (LinearLayout) activeMain.findViewById(R.id.prize_buy);
        prize_bu = (LinearLayout) activeMain.findViewById(R.id.prize_bu);

        layout_shoppcart_null = (LinearLayout) activeMain.findViewById(R.id.layout_shoppcart_null);//空页面
        layout_shoppcart_main = (RelativeLayout) activeMain.findViewById(R.id.layout_shoppcart_main);//有数据
        prize_buy.setVisibility(View.VISIBLE);
        cb_choice = (ImageView) activeMain.findViewById(R.id.cb_choice);//全选删除按钮
        tv_prize_count = (TextView) activeMain.findViewById(R.id.tv_prize_count);//共多少件商品

        tv_money_count = (TextView) activeMain.findViewById(R.id.tv_money_count);//总需多少抢币
        tv_money_cot = (TextView) activeMain.findViewById(R.id.tv_money_cot);//总需多少抢币

        title.setText("购物车");
        action_title.setVisibility(View.VISIBLE);
        prize_bue = (LinearLayout) activeMain.findViewById(R.id.prize_bue);

        //提交订单那一栏设置背景色
        rl_sub = (RelativeLayout) activeMain.findViewById(R.id.rl_sub);
//        rl_sub.setBackgroundColor(getResources().getColor(R.color.common_splitline1));
//        prize_bu.setBackgroundColor(getResources().getColor(R.color.common_splitline1));
        initView();
        doItemClick();
        return activeMain;
    }


    //点击事件
    public void doItemClick() {
        tv_shopping_submit.setOnClickListener(this);
        action_title.setOnClickListener(this);
        cb_choice.setOnClickListener(this);
        tv_delete.setOnClickListener(this);
        prize_bue.setOnClickListener(this);
    }

    // 广播接收者 - 接受支付成功发来的广播清空购物车
    private BroadcastReceiver myBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            pList.clear();
            sendEmptyBackgroundMessage(MSG_BACK_GET_LIST);
        }

    };

    //  代码中注册：
    private void initView() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.send");
        getActivity().registerReceiver(myBroadcastReceiver, intentFilter);

    }

    @Override
    public void onResume() {
        super.onResume();
        mHandler.sendEmptyMessage(1);
    }

    @Override
    protected int getPullToRefreshListViewResouceId() {
        return R.id.pull_refresh_shoppcart;
    }

    @Override
    protected void setAdapter() {
        if (mShoppingCartAdpater == null) {
            // 在这里填充adapter
            mShoppingCartAdpater = new ShoppingCartAdpater();
            super.prlv_details.setAdapter(mShoppingCartAdpater);
        }
        super.prlv_details.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        if (pList.size() == 0) {
            showEmptyView("");
            action_title.setVisibility(View.GONE);
        } else {
            action_title.setVisibility(View.VISIBLE);
        }
        mShoppingCartAdpater.notifyDataSetChanged();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected List getDataList() {
        return null;
    }

    @Override
    protected RequestParams getRequestParam() {
        /*RequestParams requestParams = new RequestParams();
        return requestParams;*/
        return null;
    }

    @Override
    protected RequestParams getRefreshRequestParam() {
        /*RequestParams requestParams = new RequestParams();
        return requestParams;*/
        return null;
    }

    @Override
    protected void parseRequestListResult(String result) {
        if (StringUtil.isEmpty(result)) {
            return;
        }
        if (pList == null) {
            //存储集合
            pList = new ArrayList();
        }
        pList.clear();
        List results;
        ShoppingCartGoods cartGoods;
        try {
            JSONObject json = new JSONObject(result);
            cartGoods = new ShoppingCartGoods(json);
            results = cartGoods.getShoppingCartGoodsDetails();
            if (results.size() > 0) {
                pList.addAll(results);
                //价格处理
                selectList.clear();
                tv_money_cot.setText(selectList.size() + "");
                cb_choice.setBackgroundResource(R.mipmap.mianfeiqiangbiweixuanzezhifufangshi);
                tv_prize_count.setText(Integer.toString(pList.size()));
                tv_money_count.setText(calculateRmb());//


            } else {
                Session.SetInt("cartnum", 0);
                //用于每次请求后更新小红点数字
                Message msg1 = MainActivity.mHandler2.obtainMessage();
                msg1.what = 2;
                MainActivity.mHandler2.sendMessage(msg1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void parseRefreshRequestListResult(String result) {
        //下拉刷新和第一次请求
        pList.clear();
        if (StringUtil.isEmpty(result)) {
            return;
        }
        if (pList == null) {
            //存储集合
            pList = new ArrayList();
        }
        List results;
        ShoppingCartGoods cartGoods;
        try {
            JSONObject json = new JSONObject(result);
            cartGoods = new ShoppingCartGoods(json);
            results = cartGoods.getShoppingCartGoodsDetails();
            pList.addAll(results);
            selectList.clear();
            tv_money_cot.setText(selectList.size() + "");
            cb_choice.setBackgroundResource(R.mipmap.mianfeiqiangbiweixuanzezhifufangshi);
            tv_prize_count.setText(Integer.toString(pList.size()));
            tv_money_count.setText(calculateRmb());//开始的时候是多少抢币

            if (pList.size() == 0) {
                Session.SetInt("cartnum", 0);
            }
            //用于每次请求后更新小红点数字
            Message msg1 = MainActivity.mHandler2.obtainMessage();
            msg1.what = 2;
            MainActivity.mHandler2.sendMessage(msg1);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getUri() {
        return Config.HTTP_URL_HEADED + "goods/GetShoppingCart";
    }

    @Override
    protected SubmitType getSubmitType() {
        return SubmitType.POST;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_shopping_submit:
                //在这里提交商品的数量
                if (pList != null && pList.size() > 0) {
                    Intent intentd = new Intent(getActivity(), ShoppingSubmitOrder.class);
                    intentd.putExtra("pListddddd", pList);
                    startActivity(intentd);
                } else {
                    ToastUtil.makeText(getActivity(), "购物车饿扁了,请先去夺宝吧");
                }
                break;
            //点击编辑按钮
            case R.id.action_title:
                if (VisitorMode.isVistor(getActivity())) {//用来判断用户是否登录
                    return;
                }
                if (cick) {
                    prize_bu.setVisibility(View.VISIBLE);
                    prize_buy.setVisibility(View.INVISIBLE);
                    action_title.setText("取消");
                    cick = false;
                    //点击取消将左边的点击标记全部取消
                    for (ShoppingCartGoodsDetail item : pList) {
                        item.setIschecked(false);
                    }
                    tv_money_cot.setText("0");
                    selectAll = false;
                    cb_choice.setBackgroundResource(R.mipmap.mianfeiqiangbiweixuanzezhifufangshi);
                    selectList.clear();//  没有选中的时候清楚调所有
                    mShoppingCartAdpater.notifyDataSetChanged();
                } else {
                    prize_bu.setVisibility(View.INVISIBLE);
                    prize_buy.setVisibility(View.VISIBLE);
                    action_title.setText("编辑");
                    cick = true;
                    mShoppingCartAdpater.notifyDataSetChanged();
                }

                break;
            case R.id.prize_bue://全选删除按钮

                if (selectAll) {//没有选中
                    for (ShoppingCartGoodsDetail item : pList) {
                        item.setIschecked(false);
                    }
                    cb_choice.setBackgroundResource(R.mipmap.mianfeiqiangbiweixuanzezhifufangshi);
                    selectList.clear();//  没有选中的时候清楚调所有

                    tv_money_cot.setText(selectList.size() + "");
                    selectAll = false;
                } else {//选中
                    for (ShoppingCartGoodsDetail item : pList) {
                        item.setIschecked(true);
                    }
                    cb_choice.setBackgroundResource(R.mipmap.mianfeiqingbixuanzezhifufangshi);
                    selectList.clear();//  清除调之前点击单个的时候加入的对象
                    selectList.addAll(pList);
                    tv_money_cot.setText(selectList.size() + "");
                    selectAll = true;
                }
                mShoppingCartAdpater.notifyDataSetChanged();
                break;
            case R.id.cb_choice://全选事件设置图片上
                if (selectAll) {//没有选中
                    for (ShoppingCartGoodsDetail item : pList) {
                        item.setIschecked(false);
                    }
                    cb_choice.setBackgroundResource(R.mipmap.mianfeiqiangbiweixuanzezhifufangshi);
                    selectList.clear();//  没有选中的时候清楚调所有

                    tv_money_cot.setText(selectList.size() + "");
                    selectAll = false;
                } else {//选中
                    for (ShoppingCartGoodsDetail item : pList) {
                        item.setIschecked(true);
                    }
                    cb_choice.setBackgroundResource(R.mipmap.mianfeiqingbixuanzezhifufangshi);
                    selectList.clear();//  清除调之前点击单个的时候加入的对象
                    selectList.addAll(pList);
                    tv_money_cot.setText(selectList.size() + "");
                    selectAll = true;
                }
                mShoppingCartAdpater.notifyDataSetChanged();
                break;
            case R.id.tv_delete://删除按钮
                if (selectList.size() != 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    final AlertDialog dialog = builder.create();
                    View view = View.inflate(getActivity(), R.layout.shopping_delete_pop, null);
                    TextView btn_shopping_ok = (TextView) view.findViewById(R.id.btn_shopping_ok);
                    btn_shopping_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for (ShoppingCartGoodsDetail itemselect : selectList) {
                                deleteShoppingCart(itemselect.getId(), false, 0);
                            }
                            dialog.dismiss();
                        }
                    });
                    TextView btn_shopping_remove = (TextView) view.findViewById(R.id.btn_shopping_remove);
                    btn_shopping_remove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.setView(view);
                    dialog.show();
                }
                break;
        }


    }

    //要取消动态广播注册，防止内存泄露
    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(myBroadcastReceiver);
    }

    private boolean selectAll = false;


//    —————————————Adapter————————————————————— //


    class ShoppingCartAdpater extends BaseAdapter {

        @Override
        public int getCount() {
            return pList.size();
        }

        @Override
        public ShoppingCartGoodsDetail getItem(int position) {
            return pList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.shopping_iteam, null);
            }
            final ShoppingViewHolder holder = ShoppingViewHolder.getHolder(convertView);
            final ShoppingCartGoodsDetail info = getItem(position);
            if ("0".equals(info.getIsten())) {
                holder.ico_characteristic.setVisibility(View.GONE);
            } else {
                holder.ico_characteristic.setVisibility(View.VISIBLE);
            }
            //通过点击取消和编辑来判断垃圾桶是否显示
            if (cick) {
                holder.iv_delOne.setVisibility(View.VISIBLE);
            } else {
                holder.iv_delOne.setVisibility(View.GONE);
            }

            holder.tv_prize_title.setText(info.getGoodsname());
            holder.tv_all_count.setText(info.getGoodsallnum());
            holder.et01.setText(info.getNumber());
            //剩余参与量
            final int subNumber = Integer.parseInt(info.getGoodsallnum()) - Integer.parseInt(info.getGoodscurrentnum());
            holder.tv_surplus_count.setText(Integer.toString(subNumber));
            ImageLoader.getInstance().displayImage(info.getHeadurl(), holder.prize_img);

            //加号的事件处理
            holder.iv_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String countAdd = holder.et01.getText().toString().trim();//获取到数量
                    //还需要判断是一元商品还是10元商品，也需要判断商品的数量不能大于该商品的库存，最后需要判断是否有网络

                    if (Integer.parseInt(countAdd) >= subNumber) {
                        holder.tv_buy_all.setVisibility(View.GONE);
                        holder.tv_buy_all_ok.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), "没有更多了哦", Toast.LENGTH_SHORT).show();
                        holder.et01.setText(subNumber + "");
                        return;
                    } else {
                        if (Integer.parseInt(info.getIsten()) == 1) {
                            int number = Integer.parseInt(countAdd) + 10;
                            if (!isFastClick()) {
                                addOrSub(info.getId(), number + "");
                            }

                            holder.tv_buy_all.setVisibility(View.VISIBLE);
                            holder.tv_buy_all_ok.setVisibility(View.GONE);
                            info.setNumber(number + "");
                            holder.et01.setText(number + "");
                            tv_money_count.setText(calculateRmb());//点击之后是多少抢币
                        } else {
                            int number = Integer.parseInt(countAdd) + 1;
                            if (!isFastClick()) {
                                addOrSub(info.getId(), number + "");
                            }
                            holder.tv_buy_all.setVisibility(View.VISIBLE);
                            holder.tv_buy_all_ok.setVisibility(View.GONE);
                            info.setNumber(number + "");
                            holder.et01.setText(number + "");
                            tv_money_count.setText(calculateRmb());//点击之后是多少抢币
                        }
                    }
                }
            });

            //减号事件处理
            holder.iv_sub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String countAdd = holder.et01.getText().toString().trim();//获取到数量
                    //还需要判断是一元商品还是10元商品，也需要判断商品的数量不能少于1，最后需要判断是否有网络
                    if (Integer.parseInt(info.getIsten()) == 1) {
                        int number = Integer.parseInt(countAdd) - 10;
                        if (number < 10) {//判断商品的数量不能少于1
                            holder.et01.setText("10");
                            Toast.makeText(getActivity(), "不能在少了哦", Toast.LENGTH_SHORT).show();
                            return;
                        } else {

                            if (!isFastClick()) {
                                addOrSub(info.getId(), number + "");
                            }
                            holder.tv_buy_all.setVisibility(View.VISIBLE);
                            holder.tv_buy_all_ok.setVisibility(View.GONE);
                            info.setNumber(number + "");
                            holder.et01.setText(number + "");
                            tv_money_count.setText(calculateRmb());//点击之后是多少抢币
                        }

                    } else {
                        int number = Integer.parseInt(countAdd) - 1;
                        if (number < 1) {//判断商品的数量不能少于1
                            holder.et01.setText("1");
                            Toast.makeText(getActivity(), "不能在少了哦", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            if (!isFastClick()) {
                                addOrSub(info.getId(), number + "");
                            }
                            holder.tv_buy_all.setVisibility(View.VISIBLE);
                            holder.tv_buy_all_ok.setVisibility(View.GONE);
                            info.setNumber(number + "");
                            holder.et01.setText(number + "");
                            tv_money_count.setText(calculateRmb());//点击之后是多少抢币
                        }
                    }
                }
            });



            //模仿京东购物车弹窗修改数量
            holder.et01.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                   final AlertDialog dialog = builder.create();
                                                   View view = View.inflate(getActivity(), R.layout.shopping_jia, null);
                                                   final AddAndSub addAndSubView = (AddAndSub) view.findViewById(R.id.ads);
                                                   final EditText dia_et01 = (EditText) view.findViewById(R.id.et01);//弹窗的editext值
                                                   final String enumber = holder.et01.getText().toString().trim();//获取到数量
                                                   addAndSubView.setNum(Integer.parseInt(enumber));
                                                   //判断是否是10元商品
                                                   if (Integer.parseInt(info.getIsten()) == 1) {
                                                       addAndSubView.isTen = true;
                                                   }

                                                   //进行回调监听输入框数字变化
                                                   addAndSubView.setOnNumChangeListener(new AddAndSub.OnNumChangeListener() {
                                                       @Override
                                                       public void onNumChange(View view, int num) {
                                                           if (num > subNumber) {
                                                               addAndSubView.setNum(subNumber);
                                                           } else {
                                                               if (Integer.parseInt(info.getIsten()) == 1) {
                                                                   if (num % 10 != 0) {
                                                                       int i = (int) Math.floor(num / 10);
                                                                       if (num - i * 10 > 5) {
                                                                           dia_et01.setText(((i + 1) * 10) + "");
                                                                       } else {
                                                                           dia_et01.setText((i * 10) + "");
                                                                       }
                                                                   }
                                                               }
                                                           }

                                                       }
                                                   });

                                                   TextView btn_ok = (TextView) view.findViewById(R.id.btn_ok);
                                                   btn_ok.setOnClickListener(new View.OnClickListener()

                                                                             {
                                                                                 @Override
                                                                                 public void onClick(View v) {
                                                                                     CommonUtil.hideSoft(holder.et01);
                                                                                     int numed = addAndSubView.getNum();
                                                                                     if (numed == 0) {
                                                                                         Toast.makeText(getActivity(), "请重新选择数量", Toast.LENGTH_SHORT).show();
                                                                                     } else {
                                                                                         addOrSub(info.getId(), numed + "");
                                                                                         holder.et01.setText(numed + "");
                                                                                         info.setNumber(numed + "");
                                                                                         tv_money_count.setText(calculateRmb());//点击之后是多少抢币

                                                                                         imm.hideSoftInputFromWindow(dia_et01.getWindowToken(), 0);
                                                                                         dialog.dismiss();

                                                                                     }
                                                                                 }
                                                                             }

                                                   );
                                                   TextView remove = (TextView) view.findViewById(R.id.btn_remove);
                                                   remove.setOnClickListener(new View.OnClickListener()

                                                                             {
                                                                                 @Override
                                                                                 public void onClick(View v) {
                                                                                     CommonUtil.hideSoft(holder.et01);
                                                                                     dialog.dismiss();
                                                                                 }
                                                                             }

                                                   );

                                                   dialog.setView(view);
                                                   dialog.show();

                                               }
                                           }

            );

            //输入框数字变化处理
            //EditeText输入事件暂时先禁用掉吧，能力不够无法实现
           /* holder.et01.addTextChangedListener(new TextWatcher() {
                String temp = "";
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    //在这里判断是否有网络存在不存在直接return;
                    temp = s.toString().trim();
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
                @Override
                public void afterTextChanged(Editable s) {
                    if(s.toString().trim().equals("") || s.toString().trim() == null ){
                        return;
                    }else{
                        if(temp.equals(s.toString().trim())){
                            return;
                        }else{
                           // addOrSub(info.getId(),s.toString().trim());
                        }
                    }
                }
            });*/

            //条目是否选中处理
            holder.rl_cart_item.setOnClickListener(new View.OnClickListener()

                                                   {
                                                       @Override
                                                       public void onClick(View v) {
                                                           if (!cick) {
                                                               if (info.ischecked()) {//选中
                                                                   holder.iv_choice.setBackgroundResource(R.mipmap.mianfeiqiangbiweixuanzezhifufangshi);
                                                                   holder.iv_choice.setVisibility(View.VISIBLE);
                                                                   info.setIschecked(false);
                                                                   if (selectList.contains(info)) {
                                                                       selectList.remove(info);
                                                                       if (selectList.size() == pList.size()) {
                                                                           selectAll = true;
                                                                           cb_choice.setBackgroundResource(R.mipmap.mianfeiqingbixuanzezhifufangshi);
                                                                       } else {
                                                                           selectAll = false;
                                                                           cb_choice.setBackgroundResource(R.mipmap.mianfeiqiangbiweixuanzezhifufangshi);
                                                                       }
                                                                   }
                                                                   tv_money_cot.setText(selectList.size() + "");
                                                               } else {//没有选中
                                                                   holder.iv_choice.setBackgroundResource(R.mipmap.mianfeiqingbixuanzezhifufangshi);
                                                                   holder.iv_choice.setVisibility(View.VISIBLE);
                                                                   info.setIschecked(true);
                                                                   if (!selectList.contains(info)) {
                                                                       selectList.add(info);
                                                                       if (selectList.size() == pList.size()) {
                                                                           selectAll = true;
                                                                           cb_choice.setBackgroundResource(R.mipmap.mianfeiqingbixuanzezhifufangshi);
                                                                       } else {
                                                                           selectAll = false;
                                                                           cb_choice.setBackgroundResource(R.mipmap.mianfeiqiangbiweixuanzezhifufangshi);
                                                                       }
                                                                   }
                                                                   tv_money_cot.setText(selectList.size() + "");
                                                               }
                                                           } else {
                                                               //非编辑状态下点击查看详情
                                                               Intent intent = new Intent(getActivity(), PrizeDetailsActivity.class);
                                                               intent.putExtra("singleGoodsId", pList.get(position).getGoodssingleid());
                                                               startActivity(intent);
                                                           }

                                                       }

                                                   }

            );
            holder.iv_choice.setOnClickListener(new View.OnClickListener()

                                                {
                                                    @Override
                                                    public void onClick(View v) {
                                                        if (!cick) {
                                                            if (info.ischecked()) {//选中
                                                                holder.iv_choice.setBackgroundResource(R.mipmap.mianfeiqiangbiweixuanzezhifufangshi);
                                                                holder.iv_choice.setVisibility(View.VISIBLE);
                                                                info.setIschecked(false);
                                                                if (selectList.contains(info)) {
                                                                    selectList.remove(info);
                                                                    if (selectList.size() == pList.size()) {
                                                                        selectAll = true;
                                                                        cb_choice.setBackgroundResource(R.mipmap.mianfeiqingbixuanzezhifufangshi);
                                                                    } else {
                                                                        selectAll = false;
                                                                        cb_choice.setBackgroundResource(R.mipmap.mianfeiqiangbiweixuanzezhifufangshi);
                                                                    }
                                                                }
                                                                tv_money_cot.setText(selectList.size() + "");
                                                            } else {//没有选中
                                                                holder.iv_choice.setBackgroundResource(R.mipmap.mianfeiqingbixuanzezhifufangshi);
                                                                holder.iv_choice.setVisibility(View.VISIBLE);
                                                                info.setIschecked(true);
                                                                if (!selectList.contains(info)) {
                                                                    selectList.add(info);
                                                                    if (selectList.size() == pList.size()) {
                                                                        selectAll = true;
                                                                        cb_choice.setBackgroundResource(R.mipmap.mianfeiqingbixuanzezhifufangshi);
                                                                    } else {
                                                                        selectAll = false;
                                                                        cb_choice.setBackgroundResource(R.mipmap.mianfeiqiangbiweixuanzezhifufangshi);
                                                                    }
                                                                }
                                                                tv_money_cot.setText(selectList.size() + "");
                                                            }
                                                        }

                                                    }

                                                }

            );

            //单选垃圾筐删除事件
            holder.iv_delOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    final AlertDialog dialog = builder.create();
                    View view2 = View.inflate(getActivity(), R.layout.shopping_delete_pop, null);
                    TextView btn_shopping_ok = (TextView) view2.findViewById(R.id.btn_shopping_ok);
                    btn_shopping_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteShoppingCart(getItem(position).getId(), true, position);
                            mShoppingCartAdpater.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    });
                    TextView btn_shopping_remove = (TextView) view2.findViewById(R.id.btn_shopping_remove);
                    btn_shopping_remove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.setView(view2);
                    dialog.show();
                }
            });

            if (info.ischecked())

            {//选中
                holder.iv_choice.setBackgroundResource(R.mipmap.mianfeiqingbixuanzezhifufangshi);
                holder.iv_choice.setVisibility(View.VISIBLE);
            } else

            {//没有选中
                holder.iv_choice.setBackgroundResource(R.mipmap.mianfeiqiangbiweixuanzezhifufangshi);
                holder.iv_choice.setVisibility(View.VISIBLE);
            }

            if (!cick)

            {
                holder.iv_choice.setVisibility(View.VISIBLE);
            } else

            {
                holder.iv_choice.setVisibility(View.INVISIBLE);
            }

            return convertView;
        }


    }

    /**
     * 计算购物车总共需要的抢币
     *
     * @return
     */
    private String calculateRmb() {
        int total = 0;
        for (ShoppingCartGoodsDetail item : pList) {
            String n = item.getNumber().trim();
            if (!("".equals(n) || n == null)) {
                total += Integer.parseInt(n);
            }
        }
        return total + "";
    }

    private void doItem() {


    }

    /**
     * 删除购物车请求服务器
     *
     * @param id
     */
    private void deleteShoppingCart(final String id, final Boolean isOne, final int position) {
        String url = Config.HTTP_URL_HEADED + "goods/DeleteShoppingCart";
        RequestParams reqParams = new RequestParams();
        reqParams.add("id", id);
        AsyncHttpTask.post(url, reqParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                String result = response.toString();
                if (TextUtils.isEmpty(result))
                    return;
                if ("0".equals(response.optString("code"))) {
                    //服务器端删除成功之后需要把本地的也删除掉
                    if (false == isOne) {
                        for (int i = 0; i < pList.size(); i++) {
                            if (pList.get(i).getId().equals(id)) {
                                pList.remove(pList.get(i));
                            }
                        }

                        for (int i = 0; i < selectList.size(); i++) {
                            if (selectList.get(i).getId().equals(id)) {
                                selectList.remove(selectList.get(i));
                            }
                        }
                        tv_money_cot.setText("0");
                        tv_prize_count.setText(Integer.toString(pList.size()));
                        tv_money_count.setText(calculateRmb());//开始的时候是多少抢币
                    } else {
                        pList.remove(position);
                        tv_prize_count.setText(Integer.toString(pList.size()));
                        tv_money_count.setText(calculateRmb());//开始的时候是多少抢币
                    }
                    //设置剩余 购物车数量
                    Session.SetInt("cartnum", Integer.parseInt(response.optString("info")));
                    Message msg1 = MainActivity.mHandler2.obtainMessage();
                    msg1.what = 1;
                    MainActivity.mHandler2.sendMessage(msg1);

                    if (Integer.parseInt(response.optString("info")) == 0) {
                        showEmptyView("");
                    }

                    //全部删除后，要恢复编辑状态
                    if (pList.size() == 0) {
                        cick = true;
                        action_title.setVisibility(View.GONE);
                        action_title.setText("编辑");
                        prize_buy.setVisibility(View.VISIBLE);
                        prize_bu.setVisibility(View.INVISIBLE);
                    }
                    //刷新列表
                    mShoppingCartAdpater.notifyDataSetChanged();

                } else if ("7".equals(response.optString("code"))) {
                    System.out.println("没token");
                } else {
                    System.out.println("参数异常");
                }
            }
        });
    }
    /**
     * 请求购物车中的数据
     */
//    private void getShopcarData(){
//        String url = Config.HTTP_URL_HEADED + "goods/GetShoppingCart";
//        AsyncHttpTask.post(url, null, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                String result = response.toString();
//                if (TextUtils.isEmpty(result))
//                    return;
//                if ("0".equals(response.optString("code"))) {
//                    try {
//                        JSONObject json = new JSONObject(result);
//                        ShoppingCartGoods cartGoods = new ShoppingCartGoods(json);
//                        ArrayList<ShoppingCartGoodsDetail> results = cartGoods.getShoppingCartGoodsDetails();
//                        pList.clear();//清除原来的数据保持本地数据和服务器端的数据的同步性
//                        pList.addAll(results);
//                        mShoppingCartAdpater.notifyDataSetChanged();
//                        Session.SetInt("cartnum", 0);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } else if ("7".equals(response.optString("code"))) {
//                    System.out.println("没token");
//                } else {
//                    System.out.println("参数异常");
//                }
//            }
//        });
//    }

    /**
     * 加号减号的处理
     */
    private void addOrSub(String shopcartid, String number) {
        String url = Config.HTTP_URL_HEADED + "goods/AddShoppingnumber";
        RequestParams reqParams = new RequestParams();
        reqParams.add("shopcartid", shopcartid);
        reqParams.add("number", number);
        AsyncHttpTask.post(url, reqParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                String result = response.toString();
                if (TextUtils.isEmpty(result))
                    return;
                if ("0".equals(response.optString("code"))) {
                    //添加成功之后需要重新请求一次数据
                    System.out.println("添加成功&&&&&&&&&&&&&&&&&&&&&&&");
//                    getShopcarData();
                } else if ("7".equals(response.optString("code"))) {
                    System.out.println("没token");
                } else {
                    System.out.println("参数异常");
                }
            }
        });
    }

    static class ShoppingViewHolder {
        ImageView iv_choice, prize_img, iv_sub, iv_add, ico_characteristic, iv_delOne;
        TextView tv_prize_title, tv_all_count, tv_surplus_count, tv_buy_all, tv_buy_all_ok;
        EditText et01;
        LinearLayout rl_cart_item;

        public ShoppingViewHolder(View convertView) {
            iv_choice = (ImageView) convertView.findViewById(R.id.iv_choice);//按钮
            prize_img = (ImageView) convertView.findViewById(R.id.prize_img);//商品图片
            iv_sub = (ImageView) convertView.findViewById(R.id.iv_sub);//左按钮
            iv_add = (ImageView) convertView.findViewById(R.id.iv_add);//右按钮
            tv_prize_title = (TextView) convertView.findViewById(R.id.tv_prize_title);//商品名称
            tv_all_count = (TextView) convertView.findViewById(R.id.tv_all_count);//总需人数
            tv_surplus_count = (TextView) convertView.findViewById(R.id.tv_surplus_count);//剩余人数
            tv_buy_all = (TextView) convertView.findViewById(R.id.tv_buy_all);//最新一期
            tv_buy_all_ok = (TextView) convertView.findViewById(R.id.tv_buy_all_ok);//自动调整
            et01 = (EditText) convertView.findViewById(R.id.et01);//输入框
            rl_cart_item = (LinearLayout) convertView.findViewById(R.id.rl_cart_item);
            ico_characteristic = (ImageView) convertView.findViewById(R.id.ico_characteristic);//十元商品展示
            iv_delOne = (ImageView) convertView.findViewById(R.id.iv_delOne);//编辑状态下的删除按钮
        }

        public static ShoppingViewHolder getHolder(View converView) {
            ShoppingViewHolder holder = (ShoppingViewHolder) converView.getTag();
            if (holder == null) {
                holder = new ShoppingViewHolder(converView);
                converView.setTag(holder);
            }
            return holder;
        }
    }

    public Handler getHandler() {

        return mHandler;
    }

    //对加减频率就行限制
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}

