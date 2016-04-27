package com.shengtao.mine.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.shengtao.R;
import com.shengtao.baseview.BaseSingleFragmentActivity;
import com.shengtao.domain.Config;
import com.shengtao.foundation.SubmitType;
import com.shengtao.utils.CommonDialog;
import com.shengtao.utils.Session;
import com.shengtao.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by hxhuang on 2015/12/18 0018.
 * descript:收货地址
 */
public class ReceiverAddressActivity extends BaseSingleFragmentActivity implements View.OnClickListener{

    private LinearLayout llRegion;
    private TextView tvRegion;
    private EditText etShipName;
    private EditText etShipPhone;
    private EditText etShipAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    protected void initView() {
        llRegion = (LinearLayout) findViewById(R.id.ll_region);
        tvRegion = (TextView) findViewById(R.id.tv_region);
        etShipAddress = (EditText)findViewById(R.id.et_ship_address);
        etShipName = (EditText)findViewById(R.id.et_ship_name);
        etShipPhone = (EditText)findViewById(R.id.et_ship_phone);

        tvRegion.setText("null".equals(Session.GetString("city_id",""))? "":Session.GetString("city_id"));
        etShipAddress.setText("null".equals(Session.GetString("ship_address",""))? "":Session.GetString("ship_address"));
        etShipName.setText("null".equals(Session.GetString("ship_name",""))? "":Session.GetString("ship_name"));
        etShipPhone.setText("null".equals(Session.GetString("ship_phone","")) ? "":Session.GetString("ship_phone"));

        llRegion.setOnClickListener(this);
        findViewById(R.id.btn_ensure).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_region:
                //进入选择地区
                startActivityForResult(new Intent(this, RegionActivity.class), 110);

                break;
            case R.id.btn_ensure:
                if(verify()) {
                    if(getTextString(etShipPhone).length() == 11) {
                        getHttpResponseList2(getRequestParams());
                    }else{
                        ToastUtil.makeText(this,"请输入正确手机号");
                    }
                }else{
                    ToastUtil.makeText(this,"收货地址没有改动");

                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 110 && resultCode == 120) {
            tvRegion.setText(data.getExtras().getString("region"));
        }
    }

    @Override
    protected String getAvtionTitle() {
        return "添加收货地址";
    }

    @Override
    protected int getLayoutResouceId() {
        return R.layout.activity_add_receiver_address;
    }

    @Override
    public int getType() {
        return 1;
    }

    @Override
    public int getHeaderType() {
        return 1;
    }

    @Override
    protected String getUri() {
        return Config.HTTP_MODULE_MINE + "user/updateAddress";
    }

    @Override
    protected SubmitType getSubmitType() {
        return SubmitType.POST;
    }

    @Override
    protected RequestParams getRequestParams() {
        RequestParams params = new RequestParams();
        params.add("cityId", getTextString(tvRegion));
        params.add("shipAddress", getTextString(etShipAddress));
        params.add("shipName", getTextString(etShipName));
        params.add("shipPhone", getTextString(etShipPhone));
        return params;
    }

    /**
     * 获取文本内容
     * @param tv 要获取内容的目标
     * @return 要获取的文本内容
     */
    private String getTextString(TextView tv){
        return tv.getText().toString().trim();
    }

    @Override
    protected void showUIData(Object obj) {
        try {
            JSONObject jsonObject = new JSONObject(obj.toString());
            if("0".equals(jsonObject.optString("code"))){
                Session.SetString("city_id", getTextString(tvRegion));
                Session.SetString("ship_name", getTextString(etShipName));
                Session.SetString("ship_phone", getTextString(etShipPhone));
                Session.SetString("ship_address", getTextString(etShipAddress));
                Session.SetString("rmb", jsonObject.optString("info"));

                if("1".equals(jsonObject.optString("token"))){
                    new CommonDialog(this){
                        @Override
                        protected void afterConfirm() {
                            dismiss();
                            finishAnimationActivity();
                        }
                    }.setTitle("恭喜您获得1抢币!", R.color.black,16f, TypedValue.COMPLEX_UNIT_SP)
                            .setButtonStyle(R.color.dialog_btn_color, 15f, TypedValue.COMPLEX_UNIT_SP)
                            .setCancelable(false)
                            .show();
                    return;
                }

                ToastUtil.makeText(this, "保存成功");
                finishAnimationActivity();
            }else{
                ToastUtil.makeText(this, jsonObject.optString("error"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 如果收货地址没有任何变动,则不请求
     * @return
     */
    private boolean verify(){
        return(!Session.GetString("city_id").equals(getTextString(tvRegion)) |
        !Session.GetString("ship_address").equals(getTextString(etShipAddress)) |
        !Session.GetString("ship_phone").equals(getTextString(etShipPhone)) |
        !Session.GetString("ship_name").equals(getTextString(etShipName)) );
    }
}
