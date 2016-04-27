package com.shengtao.snache.activity;

import android.text.Html;
import android.util.TypedValue;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.shengtao.R;
import com.shengtao.baseview.BaseActivity;
import com.shengtao.domain.Config;
import com.shengtao.foundation.AsyncHttpTask;
import com.shengtao.foundation.JsonHttpResponse;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * @package com.baixi.snache.activity
 * Created by TT on 2015/12/15.
 * Description:夺宝规则
 */
public class SnacheRule extends BaseActivity {

    private WebView wvContent;

    @Override
    protected int setLayout() {
        return R.layout.activity_snache_rule;
    }

    @Override
    protected void initView() {
        wvContent = (WebView) findViewById(R.id.wv_content);
    }

    @Override
    public int getHeaderType() {
        return 1;
    }

    @Override
    protected Object getAvtionTitle() {
        TextView textView = new TextView(this);
        textView.setTextColor(getResources().getColor(R.color.yym_common_red));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        textView.setText("夺宝规则");
        return textView;
    }

    @Override
    protected void doBusiness() {
        AsyncHttpTask.post(Config.HTTP_URL_HEAD + "Rule/getRule", new RequestParams("id", 2), new JsonHttpResponse() {
            @Override
            protected void success(Header[] headers, JSONObject json) {
                wvContent.getSettings().setUseWideViewPort(false);
                wvContent.loadDataWithBaseURL(null,json.optString("info"),"text/html","utf-8",null);
            }
        });
    }

    @Override
    public void onClick(View view) {

    }
}
