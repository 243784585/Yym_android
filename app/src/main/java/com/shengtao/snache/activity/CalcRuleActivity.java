package com.shengtao.snache.activity;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.shengtao.R;
import com.shengtao.baseview.BaseSingleFragmentActivity;
import com.shengtao.domain.CalcRule;
import com.shengtao.domain.Config;
import com.shengtao.domain.snache.CalcRuleDetail;
import com.shengtao.foundation.SubmitType;
import com.shengtao.utils.LargeListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * @package com.shengtao.snache.activity
 * Created by TT on 2015/12/24.
 * Description:计算规则
 */
public class CalcRuleActivity extends BaseSingleFragmentActivity{

    private CalcRule calcRule;
    private TextView tvA;
    private TextView tvB;
    private TextView tvMore;
    private TextView tvLuckyId;
    private TextView tv_calc;
    private Boolean isVisible = true;
    private LargeListView lvCalc;
    private LinearLayout ll_calc;
    private String singleGoodsId;
    private List<CalcRuleDetail> calcList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected void initData() {
        singleGoodsId = getIntent().getStringExtra("singleGoodsId");
        super.initData();
    }

    protected void initView() {
        tvA = (TextView) findViewById(R.id.tv_a);
        tvB = (TextView) findViewById(R.id.tv_b);
        tvMore = (TextView) findViewById(R.id.tv_more);
        tvLuckyId = (TextView) findViewById(R.id.tv_LuckyId);
        tv_calc = (TextView) findViewById(R.id.tv_calc);

        lvCalc = (LargeListView) findViewById(R.id.lv_calc);
        ll_calc = (LinearLayout) findViewById(R.id.ll_calc);
        tvMore.setOnClickListener(this);
    }

    @Override
    protected String getAvtionTitle() {
        return "计算规则";
    }

    @Override
    protected int getLayoutResouceId() {
        return R.layout.activity_calcrule;
    }

    @Override
    protected String getUri() {
        return Config.HTTP_URL_HEAD + "goods/GoodsCountRule";
    }

    @Override
    protected SubmitType getSubmitType() {
        return SubmitType.POST;
    }

    @Override
    public int getHeaderType() {
        return 1;
    }

    @Override
    protected RequestParams getRequestParams() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("singleGoodsId",singleGoodsId);
        return requestParams;
    }

    @Override
    protected void showUIData(Object obj) {
        //解析方法
        String result = obj.toString();
        if (TextUtils.isEmpty(result))
            return;
        JSONObject json = null;
        //填充数据
        try {
            json = new JSONObject(result);
            calcRule = new CalcRule(json);
            calcList = calcRule.getCalcList();
            CalcAdapter calcAdapter = new CalcAdapter();
            lvCalc.setAdapter(calcAdapter);
            fillData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //填充数据
    public void fillData(){
        tvA.setText(Html.fromHtml("<font color=black>=</font>"+calcRule.getAvalue()));
        tvB.setText(Html.fromHtml("<font color=black>=</font>"+calcRule.getBvalue()));
        tvLuckyId.setText(Html.fromHtml("<font color=black>幸运号码</font>"+calcRule.getLuckyId()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_more:
                if(isVisible==true){
                    lvCalc.setVisibility(View.VISIBLE);
                    ll_calc.setVisibility(View.VISIBLE);
                    isVisible = false;
                    tvMore.setText("收起 ↑");
                }else{
                    lvCalc.setVisibility(View.GONE);
                    ll_calc.setVisibility(View.GONE);
                    tvMore.setText("展开 ↓");
                    isVisible = true;
                }
            break;
        }
    }


    class CalcAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return calcList.size();
        }

        @Override
        public CalcRuleDetail getItem(int position) {
            return calcList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if(viewHolder==null){
                convertView = View.inflate(CalcRuleActivity.this,R.layout.item_calcrule, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            CalcRuleDetail item = getItem(position);
            viewHolder.tv_time.setText(Html.fromHtml(item.getJoin_time()+"<font color='#ff4447'>"+item.getJoinvalue()+"</font>"));
            viewHolder.tv_join_phone.setText(item.getClient_name());

            return convertView;
        }
    }

    private class ViewHolder{
        private TextView tv_time;
        private TextView tv_join_phone;

        public ViewHolder(View view){
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_join_phone = (TextView) view.findViewById(R.id.tv_join_phone);
        }
    }

}
