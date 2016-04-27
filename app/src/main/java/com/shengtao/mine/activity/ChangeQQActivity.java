package com.shengtao.mine.activity;

import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
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
import com.shengtao.utils.ContainsEmojiEditText;
import com.shengtao.utils.Session;
import com.shengtao.utils.ToastUtil;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by hxhuang on 2015/12/30 0030.
 */
public class ChangeQQActivity extends BaseActivity/* implements View.OnClickListener,TextWatcher*/{

    private ContainsEmojiEditText etQQ;
    private ImageView ivClear;
    private LinearLayout llQq;

    @Override
    protected int setLayout() {
        return R.layout.activity_change_qq;
    }

    @Override
    protected void initView() {
        etQQ = (ContainsEmojiEditText) findViewById(R.id.et_qq);
//        etQQ.addTextChangedListener(this);
        ivClear = (ImageView) getViewAndSetOnClick(R.id.iv_clear,this);
        llQq = (LinearLayout) findViewById(R.id.ll_qq);
        etQQ.setText(Session.GetString("client_qq"));
    }

    @Override
    protected String getAvtionTitle() {
        return "QQ";
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
        CommonUtil.showSoftInput(etQQ);
    }

    @Override
    protected void doBusiness() {}

    private String getUri(){
        return Config.HTTP_MODULE_MINE + "user/updateInfo";
    }

    private RequestParams getRequestParams(){
        return new RequestParams("clientQq",etQQ.getText().toString().trim());
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.iv_clear){
            etQQ.setText("");
        }else {
            if (etQQ.length() != 0) {
                if (etQQ.length() < 13) {
                    AsyncHttpTask.post(getUri(), getRequestParams(), new JsonHttpResponse() {
                        @Override
                        protected void success(Header[] headers, JSONObject json) {
                            Session.SetString("client_qq", etQQ.getText().toString().trim());
                            new CommonDialog(ChangeQQActivity.this){

                                @Override
                                protected void afterConfirm() {
                                    finish();
                                }
                            }.setTitle("更改成功",R.color.black,16f, TypedValue.COMPLEX_UNIT_SP)
                            .setButtonStyle(R.color.dialog_btn_color,15f,TypedValue.COMPLEX_UNIT_SP)
                            .show();
                        }
                    });
                } else {
                    ToastUtil.showTextToast("字符不可超过12个");
                }
            } else {
                ToastUtil.showTextToast("QQ号不能为空");
            }
        }
    }


   /* @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(editable.length() > 0){
            ivClear.setVisibility(View.VISIBLE);
            if(editable.length() == 13){
                etQQ.setText(editable.delete(etQQ.getSelectionStart() - 1, etQQ.getSelectionEnd()));
//                TranslateAnimation ta = new TranslateAnimation(0, 0, 0, 10);//上移
//                ta.setDuration(100);
//                ta.setFillAfter(true);
//                llQq.startAnimation(ta);
//                ta = new TranslateAnimation(0,-10,0,0);//左移
//                ta.setDuration(100);
//                ta.setFillAfter(true);
//                llQq.startAnimation(ta);
//                ta = new TranslateAnimation(0,0,0,-10);//下移
//                ta.setDuration(100);
//                ta.setFillAfter(true);
//                llQq.startAnimation(ta);
//                ta = new TranslateAnimation(0,10,0,0);//右移
//                ta.setDuration(100);
//                ta.setFillAfter(true);
//                llQq.startAnimation(ta);ta.setInterpolator(new Interpolator() {
//                    @Override
//                    public float getInterpolation(float v) {
//                        return 7;
//                    }
//                });
//                etQQ.setSelection(11);
//                TranslateAnimation ta = new TranslateAnimation(0, 0, 0, 10);//上移
//                ta.setDuration(100);
//                ta.setFillAfter(true);
//                llQq.startAnimation(ta);
//                ta = new TranslateAnimation(0,-10,0,0);//左移
//                ta.setDuration(100);
//                ta.setFillAfter(true);
//                llQq.startAnimation(ta);
//                ta = new TranslateAnimation(0,0,0,-10);//下移
//                ta.setDuration(100);
//                ta.setFillAfter(true);
//                llQq.startAnimation(ta);
//                ta = new TranslateAnimation(0,10,0,0);//右移
//                ta.setDuration(100);
//                ta.setFillAfter(true);
//                llQq.startAnimation(ta);ta.setInterpolator(new Interpolator() {
//                    @Override
//                    public float getInterpolation(float v) {
//                        return 7;
//                    }
//                });
                etQQ.setSelection(12);
                AnimUtil.jitter(llQq);
                ToastUtil.makeText(this,"字符不可超过12个");
            }
        }else{
            ivClear.setVisibility(View.INVISIBLE);
        }
    }*/
}
