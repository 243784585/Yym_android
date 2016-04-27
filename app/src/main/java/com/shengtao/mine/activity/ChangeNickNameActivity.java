package com.shengtao.mine.activity;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.shengtao.R;
import com.shengtao.baseview.BaseActivity;
import com.shengtao.domain.Config;
import com.shengtao.foundation.AsyncHttpTask;
import com.shengtao.foundation.JsonHttpResponse;
import com.shengtao.utils.CommonDialog;
import com.shengtao.utils.CommonUtil;
import com.shengtao.utils.Session;
import com.shengtao.utils.ToastUtil;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by hxhuang on 2015/12/31 0031.
 */
public class ChangeNickNameActivity extends BaseActivity implements View.OnClickListener{

    private EditText etClientName;
    private ImageView ivClear;
    @Override
    protected int setLayout() {
        return R.layout.activity_change_nickname;
    }

    @Override
    protected void initView() {
        etClientName = (EditText) findViewById(R.id.et_client_name);
        ivClear = (ImageView) getViewAndSetOnClick(R.id.iv_clear, this);
        etClientName.setText(Session.GetString("client_name"));
    }

    @Override
    protected String getAvtionTitle() {
        return "昵称";
    }

    @Override
    public int getHeaderType() {
        return 1;
    }

    @Override
    public Object getHeaderRight() {
        TextView textView = new TextView(this);
        textView.setText("保存");
        textView.setTextColor(Color.parseColor("#ff4447"));
        textView.setTextSize(18);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 20, 0);
        textView.setLayoutParams(layoutParams);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setOnClickListener(this);
        return textView;
    }

    @Override
    protected void onResume() {
        super.onResume();
        CommonUtil.showSoftInput(etClientName);
    }

    @Override
    protected void doBusiness() {
        etClientName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() > 0){
                    ivClear.setVisibility(View.VISIBLE);
                }else{
                    ivClear.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    private String getUri(){
        return Config.HTTP_MODULE_MINE + "user/updateInfo";
    }

    private RequestParams getRequestParams(){
        return new RequestParams("clientName", etClientName.getText().toString().trim());
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.iv_clear){
            etClientName.setText("");
        }else{
            if (etClientName.length() != 0) {
//                if (etClientName.length() < 13) {
                    AsyncHttpTask.post(getUri(), getRequestParams(), new JsonHttpResponse() {
                        @Override
                        protected void success(Header[] headers, JSONObject json) {
                            Session.SetString("client_name",etClientName.getText().toString().trim());
                            new CommonDialog(ChangeNickNameActivity.this){

                                @Override
                                protected void afterConfirm() {
                                    finish();
                                }
                            }.setTitle("更改成功",R.color.black,16f, TypedValue.COMPLEX_UNIT_SP)
                                    .setButtonStyle(R.color.dialog_btn_color,15f,TypedValue.COMPLEX_UNIT_SP)
                                    .show();
                        }
                    });
                /*} else {
                    ToastUtil.showTextToast("中英文字符不可超过12个");
                }*/
            } else {
                ToastUtil.showTextToast("昵称不能为空");
            }
        }

    }
}
