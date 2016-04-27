package com.shengtao.mine.activity;

import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.shengtao.R;
import com.shengtao.baseview.BaseActivity;
import com.shengtao.domain.Config;
import com.shengtao.foundation.AsyncHttpTask;
import com.shengtao.foundation.JsonHttpResponse;
import com.shengtao.mine.entity.Earning;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hxhuang on 2016/1/4 0004.
 * 积分收益明细
 */
public class IntegeralEarningDetailActivity extends BaseActivity {

    private ListView lv;
    private List<Earning> detailList;

    @Override
    protected int setLayout() {
        return R.layout.activity_integral_earning_detail;
    }

    @Override
    protected void initView() {
        lv = (ListView) findViewById(R.id.lv_integeral_earning_detail);
    }

    @Override
    protected String getAvtionTitle() {
        return "积分收益明细";
    }

    @Override
    public int getHeaderType() {
        return 1;
    }

    @Override
    protected void doBusiness() {
        AsyncHttpTask.get(Config.HTTP_MODULE_MINE + "user/signdetail", new JsonHttpResponse() {
            @Override
            protected void success(Header[] headers, JSONObject json) {
                JSONArray info = json.optJSONArray("info");
                detailList = new ArrayList<Earning>();
                if(info != null) {
                    for (int i = 0, len = info.length(); i < len ; i++) {
                        json = info.optJSONObject(i);
                        detailList.add(new Earning(json.optString("time"), json.optString("value")));
                    }
                    lv.setAdapter(new MyAdapter());
                }else{
                    //没有积分兑换记录
                }

            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return detailList.size();
        }

        @Override
        public Object getItem(int i) {
            return detailList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if(view == null){
                view = View.inflate(IntegeralEarningDetailActivity.this,R.layout.item_integeral_earning_detail,null);
                holder = new ViewHolder(view);

                view.setTag(holder);
            }else{
                holder = (ViewHolder) view.getTag();
            }

            Earning earning = (Earning) getItem(i);
            holder.tvTime.setText(earning.getTime());

            holder.tvValue.setText(Html.fromHtml("签到获得 <font color='#ff4447'>"+ earning.getValue()+"</font> 积分"));

            return view;
        }
    }

    private class ViewHolder{
        private TextView tvTime;    //时间
        private TextView tvValue;   //积分值

        public ViewHolder(View view){
            tvTime = (TextView) view.findViewById(R.id.tv_time);
            tvValue = (TextView) view.findViewById(R.id.tv_value);
        }
    }
}
