package com.shengtao.mine.activity;

import android.annotation.SuppressLint;
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
import com.shengtao.application.ImageLoaderCfg;
import com.shengtao.domain.Config;
import com.shengtao.domain.mine.PersonWin;
import com.shengtao.domain.mine.PersonWinInfo;
import com.shengtao.foundation.AsyncHttpTask;
import com.shengtao.foundation.BaseFragment;
import com.shengtao.foundation.IToastBlock;
import com.shengtao.snache.activity.PrizeDetailsActivity;
import com.shengtao.utils.DateTimeUtil;
import com.shengtao.utils.LogUtil;
import com.shengtao.utils.ToastUtil;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * @package com.baixi.mine.activity
 * Created by TT on 2015/12/17.
 * Description:中奖详情-fragment
 */
@SuppressLint("ValidFragment")
public class WinnerDetailFragment extends BaseFragment {

    private View view;
    private PullToRefreshListView lvWinDetail;
    private ArrayList<PersonWinInfo> wList;
    private MsgWinningAdapter msgWinningAdapter;
    private String id;
    private boolean isSnach = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.inflect_win_detail, null);
        initView(view);
        return view;
    }

    //获取对象
    public static WinnerDetailFragment newInstance() {
        WinnerDetailFragment df = new WinnerDetailFragment();

        return df;
    }

    public void initView(View view){
        Bundle bundle = getArguments();
        wList = (ArrayList<PersonWinInfo>) bundle.getSerializable("wList");
        id = bundle.getString("id");
        isSnach = bundle.getBoolean("snach",false);
        //获取中奖详情的资源id
        lvWinDetail = (PullToRefreshListView) view.findViewById(R.id.lv_win_detail);
        lvWinDetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                PersonWinInfo item = (PersonWinInfo) msgWinningAdapter.getItem(position-1);
                Intent intent = new Intent(getActivity(),PrizeDetailsActivity.class);
                intent.putExtra("singleGoodsId",item.getId());
                intent.putExtra("banner",item.getGoods_headurl());
                startActivity(intent);
            }
        });
        initRefresh();
        inflater(view);
    }
    public void inflater(View view){
        msgWinningAdapter = new MsgWinningAdapter();
        lvWinDetail.setAdapter(msgWinningAdapter);
    }


    /**
     * 初始化上下拉刷新操作
     */
    public void initRefresh(){
        lvWinDetail.setMode(PullToRefreshBase.Mode.BOTH);
        lvWinDetail.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            int currentPage = 1;
            RequestParams params = new RequestParams();
            String url = !isSnach?Config.HTTP_URL_HEAD + "goods/otherWinLog":Config.HTTP_URL_HEAD + "user/myWinLog";

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                currentPage = 1;
                if (!isSnach) {
                    params.put("dbAppClientid", id);
                }
                params.put("pageNum", currentPage);
                AsyncHttpTask.post(url, params, new AsyncHttpResponseHandler() {
                    //加载成功
                    @Override
                    public void onSuccess(int statusCode, Header[] motuHeaders, byte[] bytes) {
                        try {
                            wList.clear();
                            String data = new String(bytes, super.getCharset());
                            LogUtil.d(data);
                            showUIData(data);
                            lvWinDetail.onRefreshComplete();
                            msgWinningAdapter.notifyDataSetChanged();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                    //获取数据失败
                    @Override
                    public void onFailure(int statusCode, Header[] motuHeaders, byte[] bytes, Throwable throwable) {
                        lvWinDetail.onRefreshComplete();
                        return;
                    }
                });
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                ++currentPage;
                if (!isSnach) {
                    params.put("dbAppClientid", id);
                }
                params.put("pageNum", currentPage);
                AsyncHttpTask.post(url, params, new AsyncHttpResponseHandler() {
                    //加载成功
                    @Override
                    public void onSuccess(int statusCode, Header[] motuHeaders, byte[] bytes) {
                        try {
                            String data = new String(bytes, super.getCharset());
                            LogUtil.d(data);
                            showUIData(data);
                            lvWinDetail.onRefreshComplete();
                            msgWinningAdapter.notifyDataSetChanged();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }

                    //获取数据失败
                    @Override
                    public void onFailure(int statusCode, Header[] motuHeaders, byte[] bytes, Throwable throwable) {
                        lvWinDetail.onRefreshComplete();
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
        ArrayList<PersonWinInfo> results;
        try {
            json = new JSONObject(result);
            PersonWin personWin = new PersonWin(json);
            results = personWin.getPersonWinList();

            if(results.size()>0){
                wList.addAll(results);
            }else{
                ToastUtil.showTextToast("没有更多了");
            }
            msgWinningAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IToastBlock getToastBlock() {
        return null;
    }


    class MsgWinningAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return wList.size();
        }

        @Override
        public Object getItem(int position) {
            return wList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if(convertView==null){
                convertView = View.inflate(getActivity(),R.layout.item_win_detail,null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            PersonWinInfo personWinInfo = (PersonWinInfo) getItem(position);
            ImageLoader.getInstance().displayImage(personWinInfo.getGoods_headurl(), viewHolder.iv_head_url, ImageLoaderCfg.options);
            viewHolder.tv_goods_name.setText(personWinInfo.getGoods_name());
            viewHolder.tv_client_join_num.setText(personWinInfo.getClient_join_num());
            viewHolder.all_join_num.setText(personWinInfo.getAll_join_num());
            viewHolder.tv_lucky_id.setText(personWinInfo.getLucky_id());
            if(personWinInfo.getOpen_time()!=null){
                String i = (Long.parseLong(personWinInfo.getOpen_time())/1000)+"";
                viewHolder.tv_open_time.setText(DateTimeUtil.timestamp2Time(i));
            }
            return convertView;
        }
    }

    public class ViewHolder{
        private ImageView iv_head_url;
        private TextView tv_goods_name;
        private TextView tv_client_join_num;
        private TextView all_join_num;
        private TextView tv_lucky_id;
        private TextView tv_open_time;

        public ViewHolder(View view){
            iv_head_url = (ImageView) view.findViewById(R.id.iv_head_url);
            tv_goods_name = (TextView) view.findViewById(R.id.tv_goods_name);
            tv_client_join_num = (TextView) view.findViewById(R.id.tv_client_join_num);
            all_join_num = (TextView) view.findViewById(R.id.all_join_num);
            tv_lucky_id = (TextView) view.findViewById(R.id.tv_lucky_id);
            tv_open_time = (TextView) view.findViewById(R.id.tv_open_time);
        }
    }

}
