package com.shengtao.mine.activity;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.shengtao.R;
import com.shengtao.baseview.BaseActivity;
import com.shengtao.domain.Config;
import com.shengtao.foundation.AsyncHttpTask;
import com.shengtao.foundation.JsonHttpResponse;
import com.shengtao.mine.entity.ExchangeRecord;
import com.shengtao.utils.LargeListView;
import com.shengtao.utils.ToastUtil;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hxhuang on 2016/1/4 0004.
 */
public class ExchangeRmbActivity extends BaseActivity implements View.OnClickListener{

    private LargeListView lvExchangeRecord;
    private List<ExchangeRecord> recordList;
    private RadioButton radios[] = new RadioButton[6];
    private RadioButton curRadio;

    @Override
    protected int setLayout() {
        return R.layout.activity_want_exchange;
    }

    @Override
    protected void initView() {
        lvExchangeRecord = (LargeListView) findViewById(R.id.lv_exchange_record);
        getViewAndSetOnClick(R.id.btn_now_exchange,this);

        radios[0] = (RadioButton) getViewAndSetOnClick(R.id.rb_1,this);
        radios[1] = (RadioButton) getViewAndSetOnClick(R.id.rb_3,this);
        radios[2] = (RadioButton) getViewAndSetOnClick(R.id.rb_5,this);
        radios[3] = (RadioButton) getViewAndSetOnClick(R.id.rb_10,this);
        radios[4] = (RadioButton) getViewAndSetOnClick(R.id.rb_30,this);
        radios[5] = (RadioButton) getViewAndSetOnClick(R.id.rb_50,this);

        Intent intent = getIntent();
        refresh(intent.getStringExtra("integrate"),intent.getStringExtra("integrateAll"),intent.getStringExtra("integrateToday"));
    }

    private void refresh(String integrate,String integrateAll,String integrateToday){
        ((TextView) findViewById(R.id.tv_integral)).setText(integrate);//积分金额
        ((TextView) findViewById(R.id.tv_integral_all)).setText("累计收益（积分）: " + integrateAll);//累计收益(积分)
        ((TextView) findViewById(R.id.tv_integral_today)).setText("今日收益（积分）: " + integrateToday);//今天收益(积分)
    }

    @Override
    protected String getAvtionTitle() {
        return "我要兑换";
    }

    @Override
    public int getHeaderType() {
        return 1;
    }

    @Override
    protected void doBusiness() {
        AsyncHttpTask.get(Config.HTTP_MODULE_MINE + "user/exchangedetail", new JsonHttpResponse() {
            @Override
            protected void success(Header[] headers, JSONObject json) {
                JSONArray info = json.optJSONArray("info");
                if(info != null) {
                    recordList = new ArrayList<ExchangeRecord>();
                    for(int i = 0,len = info.length(); i < len ; i++) {
                        json = info.optJSONObject(i);
                        recordList.add(new ExchangeRecord(json.optString("time"),json.optString("rmb_num"),json.optString("status")));
                    }
                    lvExchangeRecord.setAdapter(new MyAdapter());
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rb_1:
                selectThis(0);
                break;
            case R.id.rb_3:
                selectThis(1);
                break;
            case R.id.rb_5:
                selectThis(2);
                break;
            case R.id.rb_10:
                selectThis(3);
                break;
            case R.id.rb_30:
                selectThis(4);
                break;
            case R.id.rb_50:
                selectThis(5);
                break;
            case R.id.btn_now_exchange:
                //立即兑换
                if(curRadio != null) {
                    AsyncHttpTask.post(Config.HTTP_MODULE_MINE + "user/exchangeRMB", new RequestParams("rmb", curRadio.getText().toString().trim()), new JsonHttpResponse() {
                        @Override
                        protected void success(Header[] headers, JSONObject json) {
                            ToastUtil.makeText(ExchangeRmbActivity.this,"兑换成功");
                            JSONObject info = json.optJSONObject("info");
                            refresh(info.optString("integrate"),info.optString("integrateAll"),info.optString("integrateToday"));
                            doBusiness();
                        }
                    });
                }else{
                    ToastUtil.makeText(this,"请选择要兑换的抢币金额");
                }
                break;
        }
    }

    private void selectThis(int position){
        for(int i=0,len=radios.length;i < len; i++){
            if(i!=position){
                radios[i].setChecked(false);
            }else{
                radios[i].setChecked(true);
                curRadio = radios[i];
            }
        }
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return recordList.size();
        }

        @Override
        public Object getItem(int i) {
            return recordList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if(view == null){
                view = View.inflate(ExchangeRmbActivity.this,R.layout.item_exchange_record,null);
                holder = new ViewHolder(view);

                view.setTag(holder);
            }else{
                holder = (ViewHolder) view.getTag();
            }

            ExchangeRecord record = (ExchangeRecord) getItem(i);
            holder.tvTime.setText(record.getTime());
            holder.tvAmount.setText(record.getAmount());
            holder.tvState.setText(record.getState());
            return view;
        }
    }

    class ViewHolder{
        private TextView tvTime;
        private TextView tvAmount;
        private TextView tvState;

        public ViewHolder(View view){
            tvTime = (TextView) view.findViewById(R.id.tv_time);
            tvAmount = (TextView) view.findViewById(R.id.tv_amount);
            tvState = (TextView) view.findViewById(R.id.tv_state);
        }
    }
}
