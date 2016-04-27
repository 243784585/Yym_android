package com.shengtao.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengtao.R;
import com.shengtao.chat.chatUI.activity.ChatActivity;
import com.shengtao.domain.Config;
import com.shengtao.foundation.AsyncHttpTask;
import com.shengtao.login.activity.LoginActivity;
import com.shengtao.mine.activity.AboveUsActivity;
import com.shengtao.mine.activity.FreeRobMoneyActivity;
import com.shengtao.mine.activity.MyCouponActivity;
import com.shengtao.mine.activity.MyTracksActivity;
import com.shengtao.mine.activity.NewsComerActivity;
import com.shengtao.mine.activity.PersonInformationActivity;
import com.shengtao.mine.activity.ReceiverAddressActivity;
import com.shengtao.mine.activity.RechargeActivity;
import com.shengtao.snache.activity.MessageActivity;
import com.shengtao.utils.CircleImageView;
import com.shengtao.utils.CommonDialog;
import com.shengtao.utils.MyDialog;
import com.shengtao.utils.Session;
import com.shengtao.utils.ToastUtil;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;


/**
 * 个人中心
 */
public class MineFragment extends Fragment implements View.OnClickListener {
    private View mainView;

    private CircleImageView ivHeadImage;  //头像
    private TextView tvUsername;    //用户名/昵称
    private TextView tvPopularizeId;//推广ID
    private TextView tvQb;          //抢币
    private TextView tvIntegral;    //积分
    private TextView tv_version;    //版本点击
    private LinearLayout civRedPoint;
    private TextView tv_num;


    private static final String savePath = "/sdcard/update/";

    private static final String saveFileName = savePath + "etripbon.apk";

    private boolean interceptFlag = false;
    private Dialog downloadDialog;
    private Boolean isUpadate;
    private int version_id;
    private String url;
    private String content;
    private String create_time;
    private MyDialog dialog;
    private int versionCode;

    /* 进度条与通知ui刷新的handler和msg常量 */
    private ProgressBar mProgress;

    private static final int DOWN_UPDATE = 1;

    private static final int DOWN_OVER = 2;

    private int progress;

    private Thread downLoadThread;

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    mProgress.setProgress(progress);
                    break;
                case DOWN_OVER:
                    downloadDialog.dismiss();
                    installApk();
                    break;
                case 3:
                    //更新抢币
                    tvQb.setText("抢币:  " + Session.GetString("rmb"));
                    break;
                default:
                    //优惠券红点
                    if(!"0".equals(Session.GetString("hasCoupon"))){
                        civRedPoint.setVisibility(View.VISIBLE);
                        tv_num.setText(Session.GetString("hasCoupon"));
                    }else{
                        civRedPoint.setVisibility(View.GONE);
                    }
                    //消息红点
                    if(Session.GetInt("iswinMessage")>0||Session.GetInt("issendMessage")>0||Session.GetBoolean("isSysMessage") == true){
                        civ_red_point_msg.setVisibility(View.VISIBLE);
                    }else{
                        civ_red_point_msg.setVisibility(View.GONE);
                    }
                    break;
            }
        }

        ;
    };
    private int version;
    private String id;
    private String error;
    private LinearLayout civ_red_point_msg;
    private TextView tv_num_msg;

    static MineFragment newInstance(String s) {
        MineFragment newFragment = new MineFragment();
        Bundle bundle = new Bundle();
        bundle.putString("hello", s);
        newFragment.setArguments(bundle);
        //bundle还可以在每个标签里传送数据
        return newFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        if(getActivity().getIntent()!=null){
//            type = getActivity().getIntent().getStringExtra("type");
//            content = getActivity().getIntent().getStringExtra("content");
//            LogUtil.e("类型是啥type",""+type+content);
//        }

        mainView = inflater.inflate(R.layout.fragment_mine, container, false);

        initView();

        initData();

        doBusiness();   //设置点击监听

        return mainView;
    }

    private void initData() {
        tvUsername.setText(Session.GetString("client_name"));
        tvPopularizeId.setText("推广ID:  " + Session.GetString("popularize_id"));
        tvQb.setText("抢币:  " + Session.GetString("rmb"));
        tvIntegral.setText("积分:  " + Session.GetString("integrate_all"));
        ImageLoader.getInstance().displayImage(Session.GetString("headimg"), ivHeadImage);
    }

    private void initView() {
        ivHeadImage = (CircleImageView) mainView.findViewById(R.id.iv_head_image);
        tvUsername = (TextView) mainView.findViewById(R.id.tv_username);
        tvPopularizeId = (TextView) mainView.findViewById(R.id.tv_popularize_id);
        tvQb = (TextView) mainView.findViewById(R.id.tv_qb);
        tvIntegral = (TextView) mainView.findViewById(R.id.tv_integral);
        tv_version = (TextView) mainView.findViewById(R.id.tv_version);
        civRedPoint = (LinearLayout) mainView.findViewById(R.id.civ_red_point);
        tv_num = (TextView) mainView.findViewById(R.id.tv_num);
        civ_red_point_msg = (LinearLayout) mainView.findViewById(R.id.civ_red_point_msg);
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
        mHandler.sendEmptyMessage(9);
    }

    private void doBusiness() {
        mainView.findViewById(R.id.ll_person_information).setOnClickListener(this);
        mainView.findViewById(R.id.ll_my_coupon).setOnClickListener(this);
        mainView.findViewById(R.id.ll_recharge).setOnClickListener(this);
        mainView.findViewById(R.id.ll_my_tracks).setOnClickListener(this);
        mainView.findViewById(R.id.ll_receiver_address).setOnClickListener(this);
        mainView.findViewById(R.id.ll_free_rob_money).setOnClickListener(this);
        mainView.findViewById(R.id.ll_message).setOnClickListener(this);
        mainView.findViewById(R.id.ll_customer_services).setOnClickListener(this);
        mainView.findViewById(R.id.ll_new_hand_helper).setOnClickListener(this);
        mainView.findViewById(R.id.ll_about_us).setOnClickListener(this);
        mainView.findViewById(R.id.header_right).setOnClickListener(this);
        mainView.findViewById(R.id.tv_version).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_person_information:
                //用户个人信息
                startActivity(new Intent(getActivity(), PersonInformationActivity.class));
                break;
            case R.id.ll_my_coupon:
                //我的优惠
                startActivity(new Intent(getActivity(), MyCouponActivity.class));
                break;
            case R.id.ll_recharge:
                startActivity(new Intent(getActivity(), RechargeActivity.class));
                //充值
                break;
            case R.id.ll_my_tracks:
                //我的足迹
                startActivity(new Intent(getActivity(), MyTracksActivity.class));
                break;
            case R.id.ll_receiver_address:
                //收货地址
                startActivity(new Intent(getActivity(), ReceiverAddressActivity.class));
                break;
            case R.id.ll_free_rob_money:
                //免费抢币
                startActivity(new Intent(getActivity(), FreeRobMoneyActivity.class));
                break;
            case R.id.ll_message:
                //消息
//                Session.SetBoolean("isMessage",false);
//                civ_red_point_msg.setVisibility(View.GONE);
                startActivity(new Intent(getActivity(), MessageActivity.class));
                break;
            case R.id.ll_customer_services:
                //客服,进入环信客服热线
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
                intent.putExtra("ID", Session.GetString("id"));
                intent.putExtra("userId", "13587654321");
                startActivity(intent);
                break;
            case R.id.ll_new_hand_helper:
                //新手帮助
                startActivity(new Intent(getActivity(), NewsComerActivity.class));
                break;
            case R.id.ll_about_us:
                //关于我们
                startActivity(new Intent(getActivity(), AboveUsActivity.class));
                break;
            case R.id.tv_version:
                //版本检测
                update();
                break;
            case R.id.header_right:
                //退出
                myDialog();
                break;
        }
    }

    private void update() {
//        String i = "general/check_version/version_id/" + getVersionCode();
        AsyncHttpTask.get(Config.HTTP_URL_HEAD + "Rule/getVersion", new RequestParams(), new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String data = new String(responseBody, "UTF-8");
                    JSONObject jsonObject = new JSONObject(data);
                    int code = jsonObject.optInt("code");

                    if (0 == code) {
                        JSONObject jsonInfo = jsonObject.getJSONObject("info");
                        version = jsonInfo.optInt("version");
                        url = jsonInfo.optString("url");
                        content = jsonInfo.optString("content");
                        id = jsonInfo.optString("id");
                        create_time = jsonInfo.optString("create_time");
                        error = jsonInfo.optString("error");
                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        hashMap.put("title", content);
                        hashMap.put("cancel", "稍后更新");
                        hashMap.put("submit", "下载更新");
                        new MyDialog(getActivity(), hashMap) {
                            @Override
                            public void dialogSubmit() {
                                //判断当前版本号是否小于服务器版本
                                if (getVersionCode() < version) {
                                    showDownloadDialog();
                                    dismiss();
                                } else{
                                    ToastUtil.makeText(getActivity(), "已经是最新版本了哦");
                                }
                            }
                        }.show();
                    } else {
                        ToastUtil.showTextToast(error);
                    }

//					version_id: "2"
//					version: "1.0.0"
//					url: "http://7xobkc.dl1.z0.glb.clouddn.com/etripbon.apk"
//					content: "这个好像是最新的版本，各种好啊啊啊啊啊"
//					create_time: "1448007899"

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                ToastUtil.showTextToast("更新失败");
            }
        });
    }

    /**
     * 下载新版本
     */
    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url1 = new URL(url);//http://www.a1166.com/etripbon/app-release.apk

                HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();

                File file = new File(savePath);
                if (!file.exists()) {
                    file.mkdir();
                }
                String apkFile = saveFileName;
                File ApkFile = new File(apkFile);
                FileOutputStream fos = new FileOutputStream(ApkFile);

                int count = 0;
                byte buf[] = new byte[1024];

                do {
                    int numread = is.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);
                    //更新进度
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if (numread <= 0) {
                        //下载完成通知安装
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    fos.write(buf, 0, numread);
                } while (!interceptFlag);

                fos.close();
                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };


    /**
     * 下载成功后安装
     */
    private void installApk() {
        File apkfile = new File(saveFileName);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        android.os.Process.killProcess(android.os.Process.myPid());
    }


    /**
     * 获取版本名称
     */
    private String getVersionName() {
        PackageManager pm = getActivity().getPackageManager();// 获取包管理器
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getActivity().getPackageName(), 0);// 根据包名获取应用版本信息
            // int versionCode = packageInfo.versionCode;//获取版本号
            String versionName = packageInfo.versionName;// 版本名
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // 找不到包名异常
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 获取版本号
     */
    private int getVersionCode() {
        try {
            PackageManager pm = getActivity().getPackageManager();// 获取包管理器
            PackageInfo packageInfo = null;// 根据包名获取应用版本信息
            packageInfo = pm.getPackageInfo(getActivity().getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    private void showDownloadDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//		builder.setTitle("软件版本更新");

        final LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.update_progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.progress);
        TextView textView = (TextView) v.findViewById(R.id.cancel);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadDialog.dismiss();
                interceptFlag = true;
            }
        });
        builder.setView(v);
//		builder.setNegativeButton("取消", new OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//				interceptFlag = true;
//			}
//		});
        downloadDialog = builder.create();
        downloadDialog.show();
        downloadApk();
    }

    private void downloadApk() {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }

    public void myDialog() {
        new CommonDialog(getActivity()) {
            @Override
            protected void afterConfirm() {
                Session.ClearSession();
                JPushInterface.setAlias(getActivity(), "", null);//取消极光推送的消息
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        }.setSubTitle("确定退出吗", R.color.black, 11f, TypedValue.COMPLEX_UNIT_SP)
                .setButtonStyle(R.color.dialog_btn_color, 15f, TypedValue.COMPLEX_UNIT_SP)
                .setCancle("取消")
                .setConfirm("确定")
                .show();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
