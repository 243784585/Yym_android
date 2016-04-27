package com.shengtao.mine.activity;

import android.view.View;
import android.webkit.WebView;

import com.loopj.android.http.RequestParams;
import com.shengtao.R;
import com.shengtao.baseview.BaseActivity;
import com.shengtao.domain.Config;
import com.shengtao.foundation.AsyncHttpTask;
import com.shengtao.foundation.JsonHttpResponse;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by hxhuang on 2016/1/4 0004.
 *
 * desc:抢币使用规则
 */
public class UseRuleActivity extends BaseActivity {

    private WebView wvContent;

    @Override
    protected int setLayout() {
        return R.layout.activity_rmb_use_rules;
    }

    @Override
    protected void initView() {
        wvContent = (WebView) findViewById(R.id.wv_content);
    }

    @Override
    protected void doBusiness() {
        AsyncHttpTask.post(Config.HTTP_URL_HEAD + "Rule/getRule", new RequestParams("id", 4), new JsonHttpResponse() {
            @Override
            protected void success(Header[] headers, JSONObject json) {
                wvContent.getSettings().setUseWideViewPort(false);
                wvContent.loadDataWithBaseURL(null,json.optString("info"),"text/html","utf-8",null);
            }
        });
    }

    @Override
    protected String getAvtionTitle() {
        return "抢币使用规则";
    }

    @Override
    public int getHeaderType() {
        return 1;
    }

    @Override
    public void onClick(View view) {

    }
}
