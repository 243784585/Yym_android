package com.shengtao.mine.activity;

import android.graphics.Typeface;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.shengtao.R;
import com.shengtao.baseview.BaseActivity;
import com.shengtao.utils.Session;

import java.nio.charset.Charset;

/**
 * @package com.shengtao.mine.activity
 * Created by TT on 2016/1/16.
 * Description:关于我们
 */
public class AboveUsActivity extends BaseActivity{


    @Override
    protected int setLayout() {
        return R.layout.activity_aboveus;
    }

    @Override
    protected void initView() {
        findAndSetFont(R.id.tv_aboveus);
        findAndSetFont(R.id.tv_snache).setText(Html.fromHtml("<b>夺宝的产生</b>"));
        findAndSetFont(R.id.tv_snachesub);
    }

    private TextView findAndSetFont(int id){
        /*TextView viewById = (TextView) findViewById(id);
        viewById.setTypeface(Typeface.createFromAsset(getAssets(), "STHeiti-Light.ttc"));
        return viewById;*/
        return (TextView) findViewById(id);
    }

    @Override
    protected void doBusiness() {

    }

    @Override
    protected String getAvtionTitle() {
        return "关于我们";
    }

    @Override
    public int getHeaderType() {
        return 1;
    }

    @Override
    public void onClick(View view) {

    }
}
