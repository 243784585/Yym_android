package com.shengtao.discover.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengtao.R;
import com.shengtao.application.ImageLoaderCfg;
import com.shengtao.discover.activity.WinnerDetailActivity;
import com.shengtao.domain.Config;
import com.shengtao.domain.discover.GoodsDetail;
import com.shengtao.domain.discover.PastResult;
import com.shengtao.foundation.AsyncHttpTask;
import com.shengtao.foundation.BaseFragment;
import com.shengtao.foundation.IToastBlock;
import com.shengtao.utils.LogUtil;
import com.shengtao.utils.ToastUtil;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by Scoot on 2015/12/23.
 * 发现——往期揭晓
 */
public class PastResultFragment extends BaseFragment {
    private View view;
    private PullToRefreshListView lvDynamicState;
    private ArrayList<GoodsDetail> pastGoods;
    private PastResultAdapter pastResultAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.inflect_dynamicstate, null);
        initView(view);
        return view;
    }

    //获取对象
    public static PastResultFragment newInstance() {
        PastResultFragment pf = new PastResultFragment();

        return pf;
    }

    public void initView(View view) {
        //获取动态的资源id
        lvDynamicState = (PullToRefreshListView) view.findViewById(R.id.lv_dynamic_state);
        Bundle bundle = getArguments();
        pastGoods = (ArrayList<GoodsDetail>) bundle.getSerializable("pastGoods");
        initRefresh();
        inflater(view);
    }

    public void inflater(View view) {
        pastResultAdapter = new PastResultAdapter();
        lvDynamicState.setAdapter(pastResultAdapter);
    }


    @Override
    public IToastBlock getToastBlock() {
        return null;
    }

    /**
     * 初始化上下拉刷新操作
     */
    public void initRefresh(){
        lvDynamicState.setMode(PullToRefreshBase.Mode.BOTH);
        lvDynamicState.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            int currentPage = 1;
            RequestParams params = new RequestParams();
            String url = Config.HTTP_URL_HEAD + "Zerogoods/AllIsPrizeZeroGoods";

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                currentPage = 1;
                params.put("pageNum", currentPage);
                AsyncHttpTask.post(url, params, new AsyncHttpResponseHandler() {
                    //加载成功
                    @Override
                    public void onSuccess(int statusCode, Header[] motuHeaders, byte[] bytes) {
                        try {
                            pastGoods.clear();
                            String data = new String(bytes, super.getCharset());
                            LogUtil.d(data);
                            showUIData(data);
                            lvDynamicState.onRefreshComplete();
                            pastResultAdapter.notifyDataSetChanged();
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
                            showUIData(data);
                            lvDynamicState.onRefreshComplete();
                            pastResultAdapter.notifyDataSetChanged();
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
            PastResult pastResult = new PastResult(json);
            results = pastResult.getGoodsDetails();

            if(results.size()>0){
                pastGoods.addAll(results);
            }else{
                ToastUtil.showTextToast("没有更多了");
            }
            pastResultAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    class PastResultAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return pastGoods == null ? 0 : pastGoods.size();
        }

        @Override
        public Object getItem(int position) {
            return pastGoods.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.find_capture_iteam, null);
            }

            ViewHolder holder = ViewHolder.getHolder(convertView);
            final GoodsDetail info = pastGoods.get(position);
            holder.tv_peoples.setText(info.getCurrent_join_num());
            holder.tv_all_count.setText(info.getZgoods_rmb());
            holder.tv_prize_title.setText(info.getZgoods_name());
            ImageLoader.getInstance().displayImage(info.getZgoods_headurl(), holder.prize_img, ImageLoaderCfg.options);

            holder.tv_times.setText("已结束");
            holder.tv_times.setTextColor(getResources().getColor(R.color.register_user_agreement_color));
            holder.btn_capture.setText("查看详情");
            holder.btn_capture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), WinnerDetailActivity.class);
                    intent.putExtra("pastGoods",info.getZgoodsId());
                    startActivity(intent);
                }
            });
            return convertView;
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
