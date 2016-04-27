package com.shengtao.snache.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.shengtao.R;
import com.shengtao.adapter.UpLoadAdapter;
import com.shengtao.baseview.BaseSingleFragmentActivity;
import com.shengtao.domain.Config;
import com.shengtao.foundation.Img7niuHttpUploadTask;
import com.shengtao.foundation.SubmitType;
import com.shengtao.nativeimg.NativeImageActivity;
import com.shengtao.nativeimg.SelectImageAdapter;
import com.shengtao.utils.CommonUtil;
import com.shengtao.utils.LargeGridView;
import com.shengtao.utils.LogUtil;
import com.shengtao.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * @package com.baixi.snache.activity
 * Created by TT on 2015/12/17.
 * Description:晒单页面
 */
public class ShowListActivity extends BaseSingleFragmentActivity implements View.OnClickListener {

    private LargeGridView noScrollgridview;
    private LargeGridView largeGridViewUpLoad;
    private TextView tvProgress;
    private EditText etDesc;
    private EditText etTitle;
    private SelectImageAdapter selectImageAdapter;
    private ArrayList<String> imageList = new ArrayList<>();

    private ArrayList<HashMap<String,Object>> data = new ArrayList<HashMap<String,Object>>();
    private ArrayList<String> img_list = new ArrayList<>();
    private static int count = 0;
    private LinkedList<String> ulist;
    private ProgressDialog pd;
    private boolean progressShow;
    private boolean isZero;
    RequestParams params = new RequestParams();
    private String singlegoodsid;
    private LinearLayout header_right;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected void initData() {
        super.initData();
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (count == data.size()) {//当图片全部上传完成后 与服务器交互
//				requestParams.put("type", "active");
//				requestParams.put("target_id", active_id);
//				requestParams.put("content", content);
//				requestParams.put("img_list",img_list);


                params.put(isZero?"singleZerogoodsid":"singlegoodsid", singlegoodsid);
                params.put("shareTitle", etTitle.getText().toString().trim());
                params.put("shareContent", etDesc.getText().toString().trim());

                for (int i = 0; i < img_list.size(); i++) {
                    params.add("img_url", img_list.get(i));
                }
                getHttpResponseList2(params);
                count = 0;
            }
        }
    };

    protected void initView() {
        singlegoodsid = getIntent().getStringExtra("singlegoodsid");
        isZero = getIntent().getBooleanExtra("isZero", false);

        noScrollgridview = (LargeGridView) findViewById(R.id.noScrollgridview);
        largeGridViewUpLoad = (LargeGridView) findViewById(R.id.largeGridViewUpLoad);
        tvProgress = (TextView) findViewById(R.id.tv_progress);//监听描述字数
        etDesc = (EditText) findViewById(R.id.et_desc);//描述
        etTitle = (EditText) findViewById(R.id.et_title);//标题
        header_right = (LinearLayout) findViewById(R.id.header_right);

        selectImageAdapter = new SelectImageAdapter(this, imageList, noScrollgridview);
        noScrollgridview.setAdapter(selectImageAdapter);

        noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), NativeImageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("select_photo", imageList);
                intent.putExtras(bundle);
                startActivityForResult(intent, 101);
                overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
            }
        });

        //监听详情的文本变化
        etDesc.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>100){
                    return;
                }
                temp = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>100){
                    s.clear();
                    return;
                }
                int number = 100 - s.length();
                tvProgress.setText("" + number + "/100");
                selectionStart = etDesc.getSelectionStart();
                selectionEnd = etDesc.getSelectionEnd();
                if(temp!=null){
                    if (temp.length() > 100) {
                        s.delete(selectionStart - 1, selectionEnd);
                        int tempSelection = selectionEnd;
                        etDesc.setText(s);
                        etDesc.setSelection(tempSelection);//设置光标在最后
                    }
                }
            }
        });

        header_right.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                if(!CommonUtil.isEmpty(etTitle.getText().toString().trim())&&!CommonUtil.isEmpty(etDesc.getText().toString().trim())){
                    if(imageList.size() > 0){
                        upLoad();
//                    progressShow = true;
//                    pd = new ProgressDialog(this);
//                    pd.setCanceledOnTouchOutside(false);
//                    pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
//
//                        @Override
//                        public void onCancel(DialogInterface dialog) {
//                            progressShow = false;
//                        }
//                    });
//                    pd.setMessage("晒单生成中~~");
//                    pd.show();
                    }else{
                        ToastUtil.showTextToast("至少上传一张图片哦~");
                    }
                }else{
                    ToastUtil.showTextToast("信息不能为空哦~");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.header_right:
//
//                break;
//        }
    }

    private synchronized void upLoad() {
        if (imageList.size() == 0) {
            Message msg = mHandler.obtainMessage();
            mHandler.sendMessage(msg);
            return;
        }

        for (int i = 0; i < imageList.size(); i++) {
            LinkedHashMap<String, Object> hashMap = new LinkedHashMap<>();
            hashMap.put("url", imageList.get(i));
            hashMap.put("flag", "0");//0显示
            data.add(hashMap);
        }

        final UpLoadAdapter upLoadAdapter = new UpLoadAdapter(this, data);
        if (imageList.size() > 0) {
            largeGridViewUpLoad.setAdapter(upLoadAdapter);
            noScrollgridview.setVisibility(View.GONE);
            largeGridViewUpLoad.setVisibility(View.VISIBLE);
        }

        for(int i=0;i<data.size();i++){
            img_list.add("");
        }
        for (int i = 0; i < data.size(); i++) {
            final HashMap<String, Object> hashMap = data.get(i);
            final int temp=i;
            String path = hashMap.get("url").toString();
            Img7niuHttpUploadTask img7niuHttpUploadTask = new Img7niuHttpUploadTask() {
                @Override
                protected void completeListener(String key) {

                    count++;
                    hashMap.put("flag", "1");
                    upLoadAdapter.notifyDataSetChanged();

                    //拼接图片url
                    String url = Config.HTTP + key;
                    url = url.replace("?", "%3F");
                    img_list.set(temp,url);

                    Message msg = mHandler.obtainMessage();
                    mHandler.sendMessage(msg);
                }
            };//

            img7niuHttpUploadTask.execute("http://112.126.75.199/xingluo/qiniu.do?uptoken",path);
            //保存所有的图片地址
//            ulist = new LinkedList<>();
//            ulist.add(path);
        }
    }

    // 拍照回掉函数
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 101:
                if (resultCode == RESULT_OK) {
                    imageList = data.getExtras().getStringArrayList("list");
                    selectImageAdapter = new SelectImageAdapter(this, imageList, noScrollgridview);
                    noScrollgridview.setAdapter(selectImageAdapter);
                }
                break;
        }
    }

    @Override
    protected String getAvtionTitle() {
        return "所有晒单";
    }

    @Override
    protected int getLayoutResouceId() {
        return R.layout.activity_show_list;
    }

    @Override
    public Object getHeaderRight() {
        return "提交";
    }

    @Override
    protected String getUri() {
        return isZero?Config.HTTP_URL_HEAD +"user/shareZeroOrder":Config.HTTP_URL_HEAD + "user/shareOrder";
    }

    @Override
    protected SubmitType getSubmitType() {
        return SubmitType.POST;
    }

    @Override
    protected RequestParams getRequestParams() {
        return params;
    }

    @Override
    public int getHeaderType() {
        return 1;
    }

    @Override
    public int getType() {
        return 1;
    }

    @Override
    protected void showUIData(Object obj) {
        String result = obj.toString();
        if (TextUtils.isEmpty(result))
            return;
        JSONObject json = null;
        try {
            json = new JSONObject(result);
            String code = json.optString("code");
            if ("0".equals(code)) {
                LogUtil.e("params",result.toString());
                //提示晒单成功
                ToastUtil.showTextToast("晒单成功喽");
                finish();
            }else{
                ToastUtil.showTextToast(json.optString("error"));
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public abstract class NoDoubleClickListener implements View.OnClickListener {

        public static final int MIN_CLICK_DELAY_TIME = 1000;
        private long lastClickTime = 0;

        @Override
        public void onClick(View v) {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                lastClickTime = currentTime;
                onNoDoubleClick(v);
            }else{
                ToastUtil.showTextToast("不要点的太快哦！");
            }
        }
        protected abstract void onNoDoubleClick(View view);
    }
}
