package com.shengtao.mine.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.shengtao.R;
import com.shengtao.adapter.snache.ViewPagerAdapter;
import com.shengtao.baseview.BaseSingleFragmentActivity;
import com.shengtao.domain.Config;
import com.shengtao.domain.mine.PersonDState;
import com.shengtao.domain.mine.PersonDStateInfo;
import com.shengtao.domain.mine.PersonShow;
import com.shengtao.domain.mine.PersonShowInfo;
import com.shengtao.domain.mine.PersonWin;
import com.shengtao.domain.mine.PersonWinInfo;
import com.shengtao.foundation.AsyncHttpTask;
import com.shengtao.foundation.JsonHttpResponse;
import com.shengtao.foundation.SubmitType;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by hxhuang on 2015/12/16 0016.
 */
public class MyTracksActivity extends BaseSingleFragmentActivity{

    private ViewPager vpMyTracks;
    private RadioButton rbSnatchRecord; //夺宝记录
    private RadioButton rbWinnerRecord; //中奖记录
    private RadioButton rbMySunList;    //我的晒单

    private ArrayList<PersonDStateInfo> snatchList;
    private ArrayList<PersonWinInfo> winList;
    private ArrayList<PersonShowInfo> mySunList;
    private ArrayList<Fragment> mFragments;

    private int mSelectIndex = -1;
    private RadioGroup rgStatusDetail;
    private ViewPagerAdapter mBrowseHistoryAdapter;
    private TextView title;
    private RelativeLayout right;
    private LinearLayout back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    protected void initView(){
        findViewById(R.id.ll_header_info).setVisibility(View.GONE);
        rbSnatchRecord = (RadioButton) findViewById(R.id.rb_dynamic_state);
        rbWinnerRecord = (RadioButton) findViewById(R.id.rb_winner_detail);
        rbMySunList = (RadioButton) findViewById(R.id.rb_show_record);
        rgStatusDetail = (RadioGroup) findViewById(R.id.rg_status_detail);
        vpMyTracks = (ViewPager) findViewById(R.id.vp_other_center);
        title = (TextView) findViewById(R.id.title);
        right = (RelativeLayout) findViewById(R.id.btn_shopcart_show);
        back = (LinearLayout) findViewById(R.id.back);
        title.setText("我的足迹");
        right.setVisibility(View.GONE);
        back.setOnClickListener(this);


        rbSnatchRecord.setText("夺宝记录");
        rbWinnerRecord.setText("中奖记录");
        rbMySunList.setText("我的晒单");
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.back:
            finishAnimationActivity();
            break;
        }
    }

    @Override
    protected String getAvtionTitle() {
        return "我的足迹";
    }

//    @Override
//    public int getHeaderType() {
//        return 1;
//    }

    @Override
    protected int getLayoutResouceId() {
        return R.layout.activity_other_persion_center;
    }

    @Override
    protected String getUri() {
        return Config.HTTP_MODULE_MINE + "user/myJoinLog";
    }

    @Override
    protected SubmitType getSubmitType() {
        return SubmitType.POST;
    }

    @Override
    protected RequestParams getRequestParams() {
        return new RequestParams("pageNum",1);
    }

    @Override
    protected void showUIData(Object obj) {
        JSONObject json = null;
        try {
            json = new JSONObject(obj.toString());
            snatchList = new PersonDState(json).getPersonDStateList();


            //中奖记录
            AsyncHttpTask.post(Config.HTTP_MODULE_MINE + "user/myWinLog", getRequestParams(), new JsonHttpResponse() {
                @Override
                protected void success(Header[] headers, JSONObject json) {
                    winList = new PersonWin(json).getPersonWinList();
                    //我的晒单
                    AsyncHttpTask.post(Config.HTTP_MODULE_MINE + "user/myShareLog", getRequestParams(), new JsonHttpResponse() {
                        @Override
                        protected void success(Header[] headers, JSONObject json) {
                            mySunList = new PersonShow(json).getPersonShowList();
                            initViewPager();
                        }
                    });
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void initViewPager(){
        mFragments = new ArrayList<Fragment>();

        //夺宝记录
        Bundle dBundle = new Bundle();
        dBundle.putSerializable("dList", snatchList);
        Fragment dynamicStateFragment = DynamicStateFragment.newInstance();
        dBundle.putBoolean("snach",true);
        dynamicStateFragment.setArguments(dBundle);

        //中奖记录
        Bundle wBundler = new Bundle();
        wBundler.putSerializable("wList", winList);
        Fragment winnerDetailFragment = WinnerDetailFragment.newInstance();
        wBundler.putBoolean("snach",true);
        winnerDetailFragment.setArguments(wBundler);

        //我的晒单
        Bundle pBundler = new Bundle();
        pBundler.putSerializable("pList", mySunList);
        Fragment showRecordFragment = ShowRecordFragment.newInstance();
        pBundler.putBoolean("snach",true);
        showRecordFragment.setArguments(pBundler);

        mFragments.add(dynamicStateFragment);
        mFragments.add(winnerDetailFragment);
        mFragments.add(showRecordFragment);

        vpMyTracks.setOffscreenPageLimit(mFragments.size());

        mBrowseHistoryAdapter = new ViewPagerAdapter(getSupportFragmentManager(),mFragments);

        vpMyTracks.setAdapter(mBrowseHistoryAdapter);

        vpMyTracks.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switchTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        rgStatusDetail.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i){
                    case R.id.rb_dynamic_state:switchTab(0);break;
                    case R.id.rb_winner_detail:switchTab(1);break;
                    case R.id.rb_show_record:switchTab(2);break;
                }
            }
        });
        switchTab(vpMyTracks.getCurrentItem());
    }

    public void switchTab(int index) {
        if (mSelectIndex == index)
            return;
        mSelectIndex = index;
        rgStatusDetail.check(rgStatusDetail.getChildAt(index).getId());
        vpMyTracks.setCurrentItem(index, true);
        mBrowseHistoryAdapter.notifyDataSetChanged();
    }
}
