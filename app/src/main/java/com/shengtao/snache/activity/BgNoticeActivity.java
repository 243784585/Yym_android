package com.shengtao.snache.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.shengtao.R;
import com.shengtao.baseview.BaseActivity;
import com.shengtao.domain.Config;
import com.shengtao.foundation.AsyncHttpTask;
import com.shengtao.foundation.JsonHttpResponse;
import com.shengtao.utils.LYDBHeader;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * @package com.shengtao.snache.activity
 * Created by TT on 2015/12/18.
 * Description:用户协议
 */
public class BgNoticeActivity extends Activity{

    private String title;
    private WebView wvContent;
    private LYDBHeader myHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getIntent().getStringExtra("title");
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(myHeader = new LYDBHeader(this));
        myHeader.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, this.getResources().getDisplayMetrics())));
        wvContent = new WebView(this);
        wvContent.setLayoutParams(layoutParams);
        linearLayout.addView(wvContent);
        AsyncHttpTask.post(Config.HTTP_URL_HEAD + "Rule/getRule", new RequestParams("id", 5), new JsonHttpResponse() {
            @Override
            protected void success(Header[] headers, JSONObject json) {
                wvContent.getSettings().setUseWideViewPort(false);
                wvContent.loadDataWithBaseURL(null, json.optString("info"), "text/html", "utf-8", null);
            }
        });
        setContentView(linearLayout);
        initView();
    }

    protected void initView() {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("left", "left");
        TextView textView = new TextView(this);
        textView.setTextColor(getResources().getColor(R.color.yym_common_red));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        textView.setText(title);
        hashMap.put("center", textView);
        myHeader.setHeaderView(this, hashMap);
    }

}
