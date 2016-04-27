package com.shengtao.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengtao.R;
import com.shengtao.baseview.BaseActivity;
import com.shengtao.domain.Config;
import com.shengtao.foundation.AsyncHttpTask;
import com.shengtao.foundation.Img7niuHttpUploadTask;
import com.shengtao.nativeimg.SingleImageActivity;
import com.shengtao.utils.CircleImageView;
import com.shengtao.utils.Session;
import com.shengtao.utils.ToastUtil;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hxhuang on 2015/12/18 0018.
 */
public class PersonInformationActivity extends BaseActivity implements View.OnClickListener{

    private CircleImageView ivHead;
    private String  localImgPath;
    private ArrayList<HashMap<String, Object>> data = new ArrayList();
    private String i;

    @Override
    protected int setLayout() {
        return R.layout.activity_person_information;
    }

    private Handler mHandler = new Handler() {

        private String url;

        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 2:
                    url = msg.obj.toString();
                    RequestParams reqParams = new RequestParams();
                    reqParams.put("headImg", url);
                    AsyncHttpTask.post(Config.HTTP_URL_HEAD + "user/updateInfo", reqParams, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            try {
                                String code = response.getString("code");
                                if ("0".equals(code)) {
//                                    pd.dismiss();
                                    Session.SetString("headimg", url);
//                                    finish();
                                } else {
                                    ToastUtil.showTextToast(response.optString("error"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    break;
            }
        }
    };

    @Override
    protected void initView() {
        getViewAndSetOnClick(R.id.ll_receiver_address, this);
        getViewAndSetOnClick(R.id.ll_client_name,this);
        getViewAndSetOnClick(R.id.ll_client_qq,this);
        getViewAndSetOnClick(R.id.ll_head_img,this);
        getViewAndSetOnClick(R.id.ll_mobile, this);
        ivHead = (CircleImageView)
//                findViewById(R.id.iv_head_image_recharge);
        getViewAndSetOnClick(R.id.iv_head_image_recharge, this);
    }

    private void setTextString(int id,String content){
        ((TextView)findViewById(id)).setText(Session.GetString(content));
    }

    private void setImageString(int id,String content){
        ImageLoader.getInstance().displayImage(Session.GetString(content), ((ImageView) findViewById(id)));

    }


    @Override
    protected String getAvtionTitle() {
        return "个人信息";
    }

    @Override
    public int getHeaderType() {
        return 1;
    }

    @Override
    protected void doBusiness() {
        setTextString(R.id.tv_client_name, "client_name");
        setTextString(R.id.tv_mobile, "mobile");
        setTextString(R.id.tv_client_qq, "client_qq");
        setTextString(R.id.tv_popularize_id, "popularize_id");
        setImageString(R.id.iv_head_image_recharge, "headimg");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_client_name://昵称
                startActivity(new Intent(this,ChangeNickNameActivity.class));
                break;
            case R.id.ll_client_qq://QQ
                startActivity(new Intent(this,ChangeQQActivity.class));
                break;
            case R.id.ll_mobile://手机
                startActivity(new Intent(this,ChangeMobileActivity.class));
                break;
            case R.id.ll_head_img://头像
                Intent intent = new Intent(getApplicationContext(),
                        SingleImageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("select_photo", data);
                intent.putExtras(bundle);
                startActivityForResult(intent, 101);
                break;
            case R.id.ll_receiver_address:
                startActivity(new Intent(this,ReceiverAddressActivity.class));
                break;

//            case R.id.iv_head_image_recharge:
//                Intent intent1 = new Intent(getApplicationContext(),
//                        SingleImageActivity.class);
//                Bundle bundle1 = new Bundle();
//                bundle1.putSerializable("select_photo", data);
//                intent1.putExtras(bundle1);
//                startActivityForResult(intent1, 101);
//                break;
        }
    }



//        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 101:
                if (data != null) {
                    this.localImgPath = data.getStringExtra("path");
                    i = "file:///" + localImgPath;
                    Session.SetString("headimg", i);
//                    ImageLoader.getInstance().displayImage(i, ivHead);
                    upLoad(localImgPath);
//                  Picasso.with(getApplicationContext())
//                            .load("file:///" + localImgPath)
//                            .resize(200, 200)
//                            .into(mRoundImageView);
                }
                break;
        }
    }


    //上传数据到后台
    private void upLoad(String path) {
        //查询图片localPath
        Img7niuHttpUploadTask uploadTask = new Img7niuHttpUploadTask() {
            /**
             * 7牛上传成功的回调函数，看这个Img7niuHttpUploadTask类就明白了
             * @param key
             */
            @Override
            protected void completeListener(String key) {
                //完成上传 Key：7牛返回的图片地址相关
                //拼接图片url
                String url = (Config.HTTP + key).replace("?", "%3F");
                Message msg = mHandler.obtainMessage();
                msg.what = 2;
                msg.obj = url;
                mHandler.sendMessage(msg);

                //这一步是已经将图片url上传到7牛了？图片已经上传成功，并且已经将有效的可以访问的url地址返回了
            }
        };
        uploadTask.execute("http://112.126.75.199/xingluo/qiniu.do?uptoken", path);
        //获取参数信息
    }

    @Override
    protected void onResume() {
        super.onResume();
        doBusiness();
//        upLoad(i);
    }
}
