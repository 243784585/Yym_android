package com.shengtao.discover.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.shengtao.discover.activity.ShareOrderItemActivity;
import com.shengtao.domain.Config;
import com.shengtao.domain.discover.WinerShare;
import com.shengtao.domain.discover.WinerShareDetail;
import com.shengtao.foundation.AsyncHttpTask;
import com.shengtao.foundation.BaseFragment;
import com.shengtao.foundation.IToastBlock;
import com.shengtao.utils.DateTimeUtil;
import com.shengtao.utils.LogUtil;
import com.shengtao.utils.ToastUtil;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by Scoot on 2015/12/23.
 * <p/>
 * 0元晒单
 */
public class WinnerShareFragment extends BaseFragment {
    private View view;
    private PullToRefreshListView lvDynamicState;
    private ArrayList<WinerShareDetail> winerShareDetail;
    private WinerShareAdapter winerShareAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.inflect_dynamicstate, null);
        initView(view);
        return view;
    }

    //获取对象
    public static WinnerShareFragment newInstance() {
        WinnerShareFragment wf = new WinnerShareFragment();

        return wf;
    }

    public void initView(View view) {
        //获取动态的资源id
        lvDynamicState = (PullToRefreshListView) view.findViewById(R.id.lv_dynamic_state);
        Bundle bundle = getArguments();
        winerShareDetail = (ArrayList<WinerShareDetail>) bundle.getSerializable("winerShareDetail");
        initRefresh();
        inflater(view);
    }

    public void inflater(View view) {
        winerShareAdapter = new WinerShareAdapter();
        lvDynamicState.setAdapter(winerShareAdapter);

        lvDynamicState.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                WinerShareDetail winerShareDetail = WinnerShareFragment.this.winerShareDetail.get(i-1);
                Intent intent = new Intent(getActivity(), ShareOrderItemActivity.class);
                intent.putExtra("mList", winerShareDetail);
                intent.putExtra("isZero", true);
                startActivity(intent);
            }
        });
    }


    @Override
    public IToastBlock getToastBlock() {
        return null;
    }

    /**
     * 初始化上下拉刷新操作
     */
    public void initRefresh() {
        lvDynamicState.setMode(PullToRefreshBase.Mode.BOTH);
        lvDynamicState.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            int currentPage = 1;
            RequestParams params = new RequestParams();
            String url = Config.HTTP_URL_HEAD + "Zerogoods/AllShareZeroGoods";

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                currentPage = 1;
                params.put("pageNum", currentPage);
                AsyncHttpTask.post(url, params, new AsyncHttpResponseHandler() {
                    //加载成功
                    @Override
                    public void onSuccess(int statusCode, Header[] motuHeaders, byte[] bytes) {
                        try {
                            winerShareDetail.clear();
                            String data = new String(bytes, super.getCharset());
                            LogUtil.d(data);
                            showUIData(data);
                            lvDynamicState.onRefreshComplete();
                            winerShareAdapter.notifyDataSetChanged();
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
                            winerShareAdapter.notifyDataSetChanged();
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
        ArrayList<WinerShareDetail> results;
        try {
            json = new JSONObject(result);
            WinerShare winerShare = new WinerShare(json);
            results = winerShare.getWinerShareDetail();

            if (results.size() > 0) {
                winerShareDetail.addAll(results);
            } else {
                ToastUtil.showTextToast("没有更多了");
            }
            winerShareAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class WinerShareAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return winerShareDetail == null ? 0 : winerShareDetail.size();
        }

        @Override
        public Object getItem(int position) {
            return winerShareDetail.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.show_prize_item, null);
            }

            ViewHolder holder = ViewHolder.getHolder(convertView);
            final WinerShareDetail info = winerShareDetail.get(position);
            holder.userNick.setText(info.getClient_name());//用户昵称
            holder.tv_show_prized_title.setText(info.getShare_title());//晒奖标题
            holder.show_prized_body.setText(info.getShare_content());//晒奖内容
            holder.tv_show_prized_detail.setText(info.getZgoods_name());//商品的名字
            String n = (Long.parseLong(info.getShare_time()) / 1000) + "";
            holder.showPrizedTime.setText(DateTimeUtil.timestamp2Time(n));
            ImageLoader.getInstance().displayImage(info.getHead_img(), holder.ivUserHeader);//设置用户头像
//            holder.ivUserHeader.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(getActivity(), OtherPersonCenter.class);
//                    intent.putExtra("id", info.getShareid());
//                    startActivity(intent);
//                }
//            });
            holder.image1.setVisibility(View.GONE);
            holder.image2.setVisibility(View.GONE);
            holder.image3.setVisibility(View.GONE);
            //晒单图片处理
            if (info.getUrlGroup() != null) {
                String[] arry = info.getUrlGroup().split(",");
                switch (arry.length) {
                    case 3:
                        holder.image3.setVisibility(View.VISIBLE);
                        String s3 = arry[2];
                        ImageLoader.getInstance().displayImage(s3, holder.image3);
                    case 2:
                        holder.image2.setVisibility(View.VISIBLE);
                        String s2 = arry[1];
                        ImageLoader.getInstance().displayImage(s2, holder.image2);
                    case 1:
                        holder.image1.setVisibility(View.VISIBLE);
                        String s1 = arry[0];
                        ImageLoader.getInstance().displayImage(s1, holder.image1);
                }
            }
            return convertView;


        }
    }

    static class ViewHolder {
        ImageView ivUserHeader, image1, image3, image2;
        TextView userNick, showPrizedTime, tv_show_prized_title, tv_show_prized_detail, show_prized_body;

        public ViewHolder(View convertView) {
            ivUserHeader = (ImageView) convertView.findViewById(R.id.iv_user_head);
            userNick = (TextView) convertView.findViewById(R.id.user_nick);
            showPrizedTime = (TextView) convertView.findViewById(R.id.show_prized_time);
            tv_show_prized_title = (TextView) convertView.findViewById(R.id.tv_show_prized_title);
            tv_show_prized_detail = (TextView) convertView.findViewById(R.id.tv_show_prized_detail);
            show_prized_body = (TextView) convertView.findViewById(R.id.show_prized_body);
            image1 = (ImageView) convertView.findViewById(R.id.img_1);
            image2 = (ImageView) convertView.findViewById(R.id.img_2);
            image3 = (ImageView) convertView.findViewById(R.id.img_3);
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
