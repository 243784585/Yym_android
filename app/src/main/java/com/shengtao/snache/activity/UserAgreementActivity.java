package com.shengtao.snache.activity;

import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.shengtao.R;
import com.shengtao.baseview.BaseActivity;

/**
 * @package com.shengtao.snache.activity
 * Created by TT on 2015/12/18.
 * Description:用户协议
 */
public class UserAgreementActivity extends BaseActivity{
    @Override
    protected int setLayout() {
        return R.layout.activity_user_agreement;
    }

    @Override
    protected void initView() {

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
        textView.setText("用户协议");
        return textView;
    }

    @Override
    protected void doBusiness() {

    }

    @Override
    public void onClick(View view) {

    }
}
