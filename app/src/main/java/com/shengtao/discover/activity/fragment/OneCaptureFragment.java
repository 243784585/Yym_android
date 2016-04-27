package com.shengtao.discover.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengtao.R;
import com.shengtao.application.ImageLoaderCfg;
import com.shengtao.discover.activity.PartOneCaptureActivity;
import com.shengtao.domain.Config;
import com.shengtao.domain.discover.GoodsDetail;
import com.shengtao.domain.discover.ZeroGoods;
import com.shengtao.foundation.AsyncHttpTask;
import com.shengtao.foundation.BaseFragment;
import com.shengtao.foundation.IToastBlock;
import com.shengtao.utils.CommonUtil;
import com.shengtao.utils.LogUtil;
import com.shengtao.utils.Session;
import com.shengtao.utils.ToastUtil;
import com.shengtao.utils.VisitorMode;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by Scoot on 2015/12/23.
 * 发现——0元夺宝
 */
public class OneCaptureFragment extends BaseFragment {

    private View view;
    private PullToRefreshListView lvDynamicState;
    private ArrayList<GoodsDetail> goodsDetail;
    private String infoTime;
    private OneCaptureAdapter adapter;
    private SparseArray<MyCountDownTimer> countDownTimer = new SparseArray<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.inflect_dynamicstate, null);
        initView(view);
        return view;
    }

    //获取对象
    public static OneCaptureFragment newInstance() {
        OneCaptureFragment of = new OneCaptureFragment();
        return of;
    }

    public void initView(View view) {
        //获取动态的资源id
        lvDynamicState = (PullToRefreshListView) view.findViewById(R.id.lv_dynamic_state);
        Bundle bundle = getArguments();
        goodsDetail = (ArrayList<GoodsDetail>) bundle.getSerializable("goodsDetail");
        infoTime = (String) bundle.getSerializable("info");
        initRefresh();
        inflater();
    }

    public void inflater() {
        adapter = new OneCaptureAdapter();
        for(int i=0,len=goodsDetail.size();i<len;i++){
            if(Long.parseLong(goodsDetail.get(i).getOpen_time()) - Long.parseLong(infoTime) > 0){
                countDownTimer.put(i, new MyCountDownTimer(Long.parseLong(goodsDetail.get(i).getOpen_time()) - Long.parseLong(infoTime), 1));
            }
        }
        lvDynamicState.setAdapter(adapter);
    }


    @Override
    public IToastBlock getToastBlock() {
        return null;
    }

    class OneCaptureAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return goodsDetail == null ? 0 : goodsDetail.size();
        }


        @Override
        public GoodsDetail getItem(int position) {
            return goodsDetail.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.find_capture_iteam, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final ViewHolder holder = viewHolder;
            final GoodsDetail info = goodsDetail.get(position);
//            System.out.println(info.getOpen_time() + "-------------------------------------------------");
            holder.tv_peoples.setText(info.getCurrent_join_num());
//            holder.tv_times.setText(info.getOpen_time());
            holder.tv_all_count.setText(info.getZgoods_rmb());
            holder.tv_prize_title.setText(info.getZgoods_name());
            ImageLoader.getInstance().displayImage(info.getZgoods_headurl(), holder.prize_img, ImageLoaderCfg.options);
            holder.btn_capture.setOnClickListener(new View.OnClickListener() {

                private int num;

                @Override
                public void onClick(View v) {
                    if(CommonUtil.isEmpty(Session.GetString("city_id"))){
                        //未填写收货地址,不允许加入清单
                        VisitorMode.alertToAddress(getActivity());
                        return;
                    }
                    //点击之后立即显示已夺宝的样式
                    holder.btn_capture.setText("已夺宝");
                    if (Integer.parseInt(info.getIsGet()) == 0) {
                        //如果夺宝成功那么当前页面参与夺宝的人数加一
                        info.setCurrent_join_num((Integer.parseInt(info.getCurrent_join_num()) + 1)+"");
                        num = Integer.parseInt(info.getCurrent_join_num());
                        holder.tv_peoples.setText(num+"");
                        Toast.makeText(getActivity(), "0元夺宝成功", Toast.LENGTH_SHORT).show();
                        info.setIsGet("1");
                    } else {
                        num = Integer.parseInt(info.getCurrent_join_num());
                    }
                    Intent intent = new Intent(getActivity(), PartOneCaptureActivity.class);
                    intent.putExtra("goodsDetail", info.getZgoodsId());
                    intent.putExtra("num", num);
                    startActivity(intent);

                }
            });
            if (Integer.parseInt(info.getIsGet()) == 1) {
                holder.btn_capture.setText("已夺宝");
            }else{
                holder.btn_capture.setText("0元夺宝");
            }
//            if(mc2!=null){
//                mc2.cancel();
//            }

            for(int i=0,len=countDownTimer.size();i<len;i++){
                if(viewHolder.tv_times.equals(countDownTimer.valueAt(i).tv_times)){
                    countDownTimer.valueAt(i).tv_times = null;
                }
            }

            if(countDownTimer.get(position) != null) {
                countDownTimer.get(position).tv_times = viewHolder.tv_times;
            }
            LogUtil.e("TestData",info.getZgoods_name());
            return convertView;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEnabled(int position) {
            return false;
        }
    }

    /**
     * 初始化上下拉刷新操作
     */
    public void initRefresh() {
        lvDynamicState.setMode(PullToRefreshBase.Mode.BOTH);
        lvDynamicState.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            int currentPage = 1;
            RequestParams params = new RequestParams();
            String url = Config.HTTP_URL_HEAD + "Zerogoods/AllZeroGoods";

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                currentPage = 1;
                params.put("pageNum", currentPage);
                AsyncHttpTask.post(url, params, new AsyncHttpResponseHandler() {
                    //加载成功
                    @Override
                    public void onSuccess(int statusCode, Header[] motuHeaders, byte[] bytes) {
                        try {
                            goodsDetail.clear();
                            lvDynamicState.onRefreshComplete();
                            for(int i = 0 , len=countDownTimer.size();i<len;i++){
                                countDownTimer.valueAt(i).cancel();
                            }
                            countDownTimer.clear();
                            String data = new String(bytes, super.getCharset());
                            showUIData(data);
                            for(int i=0,len=goodsDetail.size();i<len;i++){
                                if(Long.parseLong(goodsDetail.get(i).getOpen_time()) - Long.parseLong(infoTime) > 0){
                                    countDownTimer.put(i, new MyCountDownTimer(Long.parseLong(goodsDetail.get(i).getOpen_time()) - Long.parseLong(infoTime), 1));
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }

                    //获取数据失败
                    @Override
                    public void onFailure(int statusCode, Header[] motuHeaders, byte[] bytes, Throwable throwable) {
                        lvDynamicState.onRefreshComplete();
                        return;
                    }
                });
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                ++currentPage;
                params.put("pageNum", currentPage);
                AsyncHttpTask.post(url, params, new AsyncHttpResponseHandler() {
                    //加载成功
                    @Override
                    public void onSuccess(int statusCode, Header[] motuHeaders, byte[] bytes) {
                        try {
                            String data = new String(bytes, super.getCharset());
                            LogUtil.d(data);
                            int i = goodsDetail.size();
                            showUIData(data);
                            for(int len=goodsDetail.size();i<len;i++){
                                if(Long.parseLong(goodsDetail.get(i).getOpen_time()) - Long.parseLong(infoTime) > 0){
                                    countDownTimer.put(i, new MyCountDownTimer(Long.parseLong(goodsDetail.get(i).getOpen_time()) - Long.parseLong(infoTime), 1));
                                }
                            }
                            lvDynamicState.onRefreshComplete();
                            adapter.notifyDataSetChanged();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }

                    //获取数据失败
                    @Override
                    public void onFailure(int statusCode, Header[] motuHeaders, byte[] bytes, Throwable throwable) {
                        lvDynamicState.onRefreshComplete();
                        return;
                    }
                });
            }
        });
    }

    protected void showUIData(Object obj) {
        //解析方法
        String result = obj.toString();
        LogUtil.e("log", result);
        if (TextUtils.isEmpty(result))
            return;
        JSONObject json = null;
        ArrayList<GoodsDetail> results;
        try {
            json = new JSONObject(result);
            ZeroGoods zeroGoods = new ZeroGoods(json);
            results = zeroGoods.getGoodsDetails();
            infoTime = zeroGoods.getInfo();

            if (results.size() > 0) {
                goodsDetail.addAll(results);
            } else {
                ToastUtil.showTextToast("没有更多了");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    static class ViewHolder {
        TextView tv_peoples, tv_times, tv_prize_title, tv_all_count, btn_capture;
        ImageView prize_img;

        public ViewHolder(View convertView) {
            tv_times = (TextView) convertView.findViewById(R.id.tv_times);
            tv_peoples = (TextView) convertView.findViewById(R.id.tv_peoples);
            prize_img = (ImageView) convertView.findViewById(R.id.prize_img);
            tv_prize_title = (TextView) convertView.findViewById(R.id.tv_prize_title);
            tv_all_count = (TextView) convertView.findViewById(R.id.tv_all_count);
            btn_capture = (TextView) convertView.findViewById(R.id.btn_capture);
        }

    }

    class MyCountDownTimer extends CountDownTimer {
        /**
         * @param millisInFuture    表示以毫秒为单位 倒计时的总数
         * <p/>
         * 例如 millisInFuture=1000 表示1秒
         * @param countDownInterval 表示 间隔 多少微秒 调用一次 onTick 方法
         * <p/>
         * 例如: countDownInterval =1000 ; 表示每1000毫秒调用一次onTick()
         */
        public TextView tv_times;
        public int position;

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            start();
        }


        @Override
        public void onTick(long millisUntilFinished) {
            if (tv_times != null) {
                int tian = (int) (millisUntilFinished / (1000 * 60 * 60 * 24));
                int shi = (int) ((millisUntilFinished % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
                int fen = (int) ((millisUntilFinished % (1000 * 60 * 60)) / (1000 * 60));
                int miao = (int) ((millisUntilFinished % (1000 * 60)) / 1000);
                tv_times.setText("还剩" + tian + "天" + shi + "时" + fen + "分" + miao + "秒");
            }
        }

        @Override
        public void onFinish() {
//            goodsDetail.remove(position);
            if(tv_times !=null) {
                tv_times.setText("已结束");
            }
            adapter.notifyDataSetChanged();
        }
    }

}
