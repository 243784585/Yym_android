package com.shengtao.snache.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.RequestParams;
import com.shengtao.R;
import com.shengtao.adapter.snache.PublishedAdapter;
import com.shengtao.baseview.BaseListActivity;
import com.shengtao.domain.Config;
import com.shengtao.domain.snache.Goods;
import com.shengtao.domain.snache.PublishedGood;
import com.shengtao.foundation.BaseEnitity;
import com.shengtao.foundation.SubmitType;
import com.shengtao.utils.ToastUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @package com.baixi.snache.activity
 * Created by TT on 2015/12/15.
 * Description:往期揭晓
 */
public class PublishedActivity extends BaseListActivity {


    private PullToRefreshListView lvPublished;

    private PublishedAdapter mPublishAdapter;
    private ArrayList<PublishedGood> pList = null;
    private Goods goods;
    private String singleGoodsId;
    private PublishedGood item;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        initView();
    }

    @Override
    protected void initViewData() {
        singleGoodsId = getIntent().getStringExtra("singleGoodsId");
        super.initViewData();
    }

    protected void initView() {
        lvPublished = (PullToRefreshListView) findViewById(R.id.lv_published);
    }

    @Override
    protected int getPullToRefreshListViewResouceId() {
        return R.id.lv_published;
    }

    @Override
    protected int getLayoutResouceId() {
        return R.layout.activity_publish;
    }

    @Override
    protected String getAvtionTitle() {
        return "往期揭晓";
    }

    @Override
    protected void setAdapter() {
        if (mPublishAdapter == null) {
            // 在这里填充adapter
            mPublishAdapter = new PublishedAdapter(this,pList);
            super.prlv_details.setAdapter(mPublishAdapter);
        }

        if (pList.size() == 0){
            showEmptyView("暂无数据，请重新选择");
        }
        mPublishAdapter.notifyDataSetChanged();
    }

    @Override
    protected <T extends BaseEnitity> List<T> getDataList() {
        return null;
    }

    @Override
    protected RequestParams getRequestParam() {
        RequestParams params = new RequestParams();
        params.put("singleGoodsId", singleGoodsId);
        params.put("pageNum", mCurPageIndex);
        return params;
    }

    @Override
    protected RequestParams getRefreshRequestParam() {
        RequestParams params = new RequestParams();
        params.put("singleGoodsId", singleGoodsId);
        params.put("pageNum", mCurPageIndex);
        return params;
    }

    @Override
    protected void parseResult(String result, boolean isRefresh) {
        if (TextUtils.isEmpty(result)) {
            showEmptyView("暂时没有商品哦哦...");
            return;
        }
        //集合
        List<PublishedGood> results = new ArrayList<>();
        if(pList==null){
            //存储集合
            pList = new ArrayList();
        }
        try {
            JSONObject json = new JSONObject(result);
            goods = new Goods(json);
            results = goods.getPublishedGoodList();
            if (isRefresh) {
                pList.clear();
            }
            if (results.size() > 0) {
                pList.addAll(results);
            } else {
                ToastUtil.loadMoreTips(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getUri() {
        return  Config.HTTP_URL_HEAD + "goods/GoodsOpenAlreadyList";
    }

    @Override
    protected SubmitType getSubmitType() {
        return SubmitType.POST;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        item = (PublishedGood) mPublishAdapter.getItem(position-1);
        Intent intent = new Intent(this,PrizeDetailsActivity.class);
        intent.putExtra("singleGoodsId",item.getId());
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        return false;
    }


}
