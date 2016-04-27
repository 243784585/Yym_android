package com.shengtao.mine.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.shengtao.R;
import com.shengtao.baseview.BaseSingleFragmentActivity;
import com.shengtao.domain.Config;
import com.shengtao.domain.mine.MyCoupon;
import com.shengtao.domain.snache.OpenTimeComparator;
import com.shengtao.foundation.AsyncHttpTask;
import com.shengtao.foundation.JsonHttpResponse;
import com.shengtao.foundation.SubmitType;
import com.shengtao.utils.DateTimeUtil;
import com.shengtao.utils.LargeListView;
import com.shengtao.utils.Session;
import com.shengtao.utils.ToastUtil;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * Created by hxhuang on 2015/12/18 0018.
 * desc:我的优惠劵
 */
public class MyCouponActivity extends BaseSingleFragmentActivity {

    private String time;
    private LargeListView lvMyCoupon;
    private ArrayList<MyCoupon> mList = new ArrayList<>();

    private CouponAdapter couponAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    public void initView() {
        lvMyCoupon = (LargeListView) findViewById(R.id.lv_my_coupon);
        lvMyCoupon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MyCoupon item = couponAdapter.getItem(i);
                String uri = item.coupon_url.contains("http") ? item.coupon_url : "http://" + item.coupon_url;
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(uri)
                        ));
            }
        });
    }

    @Override
    protected String getAvtionTitle() {
        return "我的优惠券";
    }

    @Override
    protected int getLayoutResouceId() {
        return R.layout.activity_my_coupon;
    }

    @Override
    protected String getUri() {
        return Config.HTTP_MODULE_MINE + "user/mycoupon";
    }

    @Override
    protected SubmitType getSubmitType() {
        return SubmitType.GET;
    }

    @Override
    public int getHeaderType() {
        return 1;
    }

    @Override
    protected RequestParams getRequestParams() {
        return null;
    }

    @Override
    protected void showUIData(Object obj) {
        try {
            JSONObject response = new JSONObject(obj.toString());
            if ("0".equals(response.optString("code"))) {

                //清除最新的优惠劵
                clearNewCoupon();
                JSONArray info = response.optJSONArray("info");
                JSONArray attributes = response.optJSONArray("attributes");
                {
                    if (info != null && info.length() > 0) {
                        MyCoupon mc;
                        for (int i = 0, len = info.length(); i < len; i++) {
                            mc = new MyCoupon(info.optJSONObject(i));
                            mc.isZero = true;
                            mList.add(mc);
                        }

                    }

                    if (attributes != null && attributes.length() > 0) {

                        for (int i = 0, len = attributes.length(); i < len; i++) {
                            mList.add(new MyCoupon(attributes.optJSONObject(i)));
                        }
                    }
                }

                //冒泡排序
                Collections.sort(mList, new Comparator() {
                    @Override
                    public int compare(Object t1, Object t2) {
                        return (int) (Long.parseLong(((OpenTimeComparator) t2).getOpen_time()) - Long.parseLong(((OpenTimeComparator) t1).getOpen_time()));
                    }
                });
                if (couponAdapter == null) {
                    couponAdapter = new CouponAdapter();
                    lvMyCoupon.setAdapter(couponAdapter);
                } else {
                    couponAdapter.notifyDataSetChanged();
                }

            } else {
                ToastUtil.makeText(MyCouponActivity.this, response.optString("error"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {

    }

    /**
     * 清除最新优惠劵
     */
    public void clearNewCoupon() {
        AsyncHttpTask.get(Config.HTTP_URL_HEAD + "user/clearNew", new JsonHttpResponse() {
            @Override
            protected void success(Header[] headers, JSONObject json) {
                Session.SetString("hasCoupon", "0");
            }
        });
    }

    public class CouponAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public MyCoupon getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(MyCouponActivity.this, R.layout.coupon, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            MyCoupon item = getItem(position);
            if (item.isZero == true) {
                holder.tvSource.setVisibility(View.VISIBLE);
            } else {
                holder.tvSource.setVisibility(View.INVISIBLE);
            }
            holder.tvAmount.setText(item.coupon_value + "");
            holder.tvStore.setText(item.business_name);
            holder.tvTime.setText(DateTimeUtil.timestamp4Time(item.getOpen_time(), "1"));

            return convertView;
        }

        protected class ViewHolder {
            private TextView tvSource;
            private TextView tvAmount;
            private TextView tvStore;
            private TextView tvTime;

            public ViewHolder(View view) {
                tvSource = (TextView) view.findViewById(R.id.tv_source);
                tvAmount = (TextView) view.findViewById(R.id.tv_amount);
                tvStore = (TextView) view.findViewById(R.id.tv_store);
                tvTime = (TextView) view.findViewById(R.id.tv_time);
            }
        }
    }

}
