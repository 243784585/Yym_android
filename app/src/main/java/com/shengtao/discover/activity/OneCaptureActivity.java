package com.shengtao.discover.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shengtao.R;
import com.shengtao.adapter.ViewPagerAdapter;
import com.shengtao.baseview.BaseSingleFragmentActivity;
import com.shengtao.discover.activity.fragment.OneCaptureFragment;
import com.shengtao.discover.activity.fragment.PastResultFragment;
import com.shengtao.discover.activity.fragment.WinnerShareFragment;
import com.shengtao.domain.Config;
import com.shengtao.domain.discover.GoodsDetail;
import com.shengtao.domain.discover.PastResult;
import com.shengtao.domain.discover.WinerShare;
import com.shengtao.domain.discover.WinerShareDetail;
import com.shengtao.domain.discover.ZeroGoods;
import com.shengtao.foundation.AsyncHttpTask;
import com.shengtao.foundation.SubmitType;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by Scoot on 2015/12/23.
 * 0元夺宝主页面
 */
public class OneCaptureActivity extends BaseSingleFragmentActivity {

    private ViewPager vp_one_capture;
    private RadioButton rb_capture, rb_result, rb_winer_order;
    private ArrayList<Fragment> mFragments;
    RequestParams requestParams;
    private String url;
    private String info;

    private int mSelectIndex = -1;
    private RadioGroup rg_status_ded;
    private ViewPagerAdapter mBrowseHistoryAdapter;
    private ArrayList<GoodsDetail> goodsDetail;
    private ArrayList<GoodsDetail> pastGoods;
    private ArrayList<WinerShareDetail> winerShareDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected String getAvtionTitle() {
        return "0元夺宝";
    }

    @Override
    protected int getLayoutResouceId() {
        return R.layout.one_capture;
    }

    @Override
    protected String getUri() {
        return Config.HTTP_URL_HEADED + "Zerogoods/AllZeroGoods";
    }

    @Override
    protected SubmitType getSubmitType() {
        return SubmitType.POST;
    }

    @Override
    protected RequestParams getRequestParams() {
        requestParams = new RequestParams();
        requestParams.put("pageNum", 1);
        return requestParams;
    }

    @Override
    public int getHeaderType() {
        return 1;
    }

    @Override
    protected void showUIData(Object obj) {
        String result = obj.toString();
        if (TextUtils.isEmpty(result))
            return;
        JSONObject json = null;
        try {
            json = new JSONObject(result);
            ZeroGoods zeroGoods = new ZeroGoods(json);
            info= zeroGoods.getInfo();
            goodsDetail = zeroGoods.getGoodsDetails();

            url = Config.HTTP_URL_HEADED + "Zerogoods/AllIsPrizeZeroGoods";//往期揭晓
            AsyncHttpTask.post(url, requestParams, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] LydbHeaders, byte[] bytes) {
                    String data = null;
                    try {
                        data = new String(bytes, super.getCharset());
//                        LogUtil.d(data);
                        parseTypeResult(data);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int i, Header[] motuHeaders, byte[] bytes, Throwable throwable) {
                    //网络请求错误
                    return;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    //往期揭晓的二次解析
    private void parseTypeResult(String data) {
        if (TextUtils.isEmpty(data))
            return;
        JSONObject json = null;
        try {
            json = new JSONObject(data);
            PastResult pastResult = new PastResult(json);
            pastGoods = pastResult.getGoodsDetails();
//            System.out.println(pastGoods.get(0).getOpen_time()+"获取时间哈哈哈哈");

            url = Config.HTTP_URL_HEADED + "Zerogoods/AllShareZeroGoods";//获奖晒单
            AsyncHttpTask.post(url, requestParams, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] LydbHeaders, byte[] bytes) {
                    String data = null;
                    try {
                        data = new String(bytes, super.getCharset());
//                        LogUtil.d(data);
                        parseWinnerData(data);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int i, Header[] motuHeaders, byte[] bytes, Throwable throwable) {
                    //网络请求错误
                    return;
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //获奖晒单的二次解析
    private void parseWinnerData(String data) {
        if (TextUtils.isEmpty(data))
            return;
        JSONObject json = null;
        try {
            json = new JSONObject(data);
            WinerShare winerShare =new WinerShare(json);
            winerShareDetail =winerShare.getWinerShareDetail();
            initViewPager();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void initView() {
        vp_one_capture = (ViewPager) findViewById(R.id.vp_one_capture);
        rg_status_ded = (RadioGroup) findViewById(R.id.rg_status_ded);
        rb_capture = (RadioButton) findViewById(R.id.rb_capture);//零元夺宝
        rb_result = (RadioButton) findViewById(R.id.rb_past_result);//往期揭晓
        rb_winer_order = (RadioButton) findViewById(R.id.rb_winer_order);//获奖晒单
    }

    private void initViewPager() {
        mFragments = new ArrayList<Fragment>();
        Fragment mOneCaptureFragment = OneCaptureFragment.newInstance();
        Fragment mPastResultFragment = PastResultFragment.newInstance();
        Fragment mWinnerShareFragment = WinnerShareFragment.newInstance();

        //传递数据
        Bundle bundle1 = new Bundle();
        bundle1.putSerializable("goodsDetail", goodsDetail);
        bundle1.putSerializable("info",info);
        mOneCaptureFragment.setArguments(bundle1);

        Bundle bundle2 = new Bundle();
        bundle2.putSerializable("pastGoods", pastGoods);
        mPastResultFragment.setArguments(bundle2);


        Bundle bundle3 = new Bundle();
        bundle3.putSerializable("winerShareDetail", winerShareDetail);
        mWinnerShareFragment.setArguments(bundle3);

        mFragments.add(mOneCaptureFragment);
        mFragments.add(mPastResultFragment);
        mFragments.add(mWinnerShareFragment);

        vp_one_capture.setOffscreenPageLimit(mFragments.size());
        mBrowseHistoryAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mFragments);
        vp_one_capture.setAdapter(mBrowseHistoryAdapter);

        vp_one_capture.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                switchTab(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

        rg_status_ded.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_capture:
                        switchTab(0);
                        break;
                    case R.id.rb_past_result:
                        switchTab(1);
                        break;
                    case R.id.rb_winer_order:
                        switchTab(2);
                        break;
                }
            }
        });
        switchTab(vp_one_capture.getCurrentItem());
    }

    public void switchTab(int index) {
        if (mSelectIndex == index)
            return;
        mSelectIndex = index;
        rg_status_ded.check(rg_status_ded.getChildAt(index).getId());
        vp_one_capture.setCurrentItem(index, true);
        mBrowseHistoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {

    }


}