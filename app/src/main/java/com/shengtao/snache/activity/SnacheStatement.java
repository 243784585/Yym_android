package com.shengtao.snache.activity;

import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.shengtao.R;
import com.shengtao.baseview.BaseActivity;

/**
 * @package com.baixi.snache.activity
 * Created by TT on 2015/12/18.
 * Description:夺宝声明
 */
public class SnacheStatement extends BaseActivity{
    TextView tv1;

    @Override
    protected int setLayout() {
        return R.layout.activity_snache_statement;
    }

    @Override
    protected void initView() {
        tv1 = (TextView) findViewById(R.id.tv1);
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
        textView.setText("夺宝奖品声明");
        return textView;
    }

    @Override
    protected void doBusiness() {
        //tv1.setText(Html.fromHtml("<span style='text-align:justify;'>"+tv1.getText()+"</span>"));
    }

    @Override
    public void onClick(View view) {

    }
}
