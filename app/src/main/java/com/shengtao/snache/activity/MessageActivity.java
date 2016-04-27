package com.shengtao.snache.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.shengtao.R;
import com.shengtao.baseview.BaseMyActionFragmentActivity;
import com.shengtao.domain.Config;
import com.shengtao.domain.snache.Notice;
import com.shengtao.foundation.SubmitType;
import com.shengtao.utils.CommonUtil;
import com.shengtao.utils.DateTimeUtil;
import com.shengtao.utils.Session;
import com.shengtao.utils.ShareDialog;
import com.shengtao.utils.ShareUtils;
import com.shengtao.utils.ShareUtils1;
import com.shengtao.utils.VisitorMode;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TT on 2015/12/12.
 * explain:消息or公告
 */
public class MessageActivity extends BaseMyActionFragmentActivity implements View.OnClickListener {

    private ListView lvMessage;
    private RelativeLayout rlMessage;
    private ListView lvNotice;
    private RelativeLayout rlNotice;
    private TextView btnNotice;
    private TextView btnMessage;
    private ImageView ivBack;

    private Boolean isSelect = true;
    private MsgOrNoticeAdapter msgOrNoticeAdapter;
    private List<Notice> mList;
    private List<Notice> nList;

    private LayoutInflater inflater;
    private LinearLayout tvTitle;
    private MsgOrNoticeAdapter msgOrNoticeAdapter2;
    private MsgOrNoticeAdapter msgOrNoticeAdapter1;
    private UMSocialService mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        doBusiness();
    }

    protected void initView() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        btnNotice = (TextView) findViewById(R.id.btn_notice);
        btnMessage = (TextView) findViewById(R.id.btn_message);
        rlMessage = (RelativeLayout) findViewById(R.id.rl_message);//消息
        lvMessage = (ListView) findViewById(R.id.lv_message);//消息
        rlNotice = (RelativeLayout) findViewById(R.id.rl_notice);//公告
        lvNotice = (ListView) findViewById(R.id.lv_notice);//公告
        tvTitle = (LinearLayout) findViewById(R.id.title);


        lvNotice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent intent = new Intent(MessageActivity.this, BgNoticeActivity.class);
                        intent.putExtra("title", nList.get(0).getTopic());
                        Session.SetBoolean("isSysMessage", false);
                        startActivity(intent);
                        break;
                    case 1:
                        //夺宝声明界面
                        startActivity(new Intent(MessageActivity.this, SnacheStatement.class));
                        break;
                    case 2:
                        //夺宝规则
                        startActivity(new Intent(MessageActivity.this, SnacheRule.class));
                        break;
                    case 3:
                        //用户协议
                        startActivity(new Intent(MessageActivity.this, UserAgreementActivity.class));
                        break;
                    case 4:
                        //常见问题
                        startActivity(new Intent(MessageActivity.this, CommonQuestionActivity.class));
                        break;
                    case 5://分享
//                        startActivity(new Intent(MessageActivity.this, CommonQuestionActivity.class));
                        //分享配置
                        ShareUtils.configPlatforms(MessageActivity.this, "https://www.baidu.com", "lala");
                        ShareDialog shareDialog1 = new ShareDialog(MessageActivity.this, MessageActivity.this);
//                        shareDialog1.setCancelable(false);
                        shareDialog1.setCanceledOnTouchOutside(true);
                        shareDialog1.show();
                        mController = UMServiceFactory.getUMSocialService("com.umeng.share");

                        //wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID

                        //添加微信平台
//                        UMWXHandler wxHandler = new UMWXHandler(getApplicationContext(), Config.APP_ID_WECHAT, Config.APP_SECRET_WECHAT);
//                        wxHandler.addToSocialSDK();
//                        wxHandler.setTargetUrl("https://www.baidu.com");
//                        //添加微信朋友圈
//                        UMWXHandler wxCircleHandler = new UMWXHandler(getApplicationContext(), Config.APP_ID_WECHAT, Config.APP_SECRET_WECHAT);
//                        wxCircleHandler.setToCircle(true);
//                        wxCircleHandler.addToSocialSDK();
//
//                        // 设置分享内容
//                        mController.setShareContent("1圆梦一个只需要1元就可以完成梦想的购物天堂，http://www.baidu.com");
//                        mController.setShareMedia(new UMImage(getApplicationContext(), R.drawable.app_icon)); // 设置分享图片内容
//
//                        //QQ分享
//                        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(MessageActivity.this, Config.APP_ID_QQ, Config.APP_KEY_QQ);
//                        qqSsoHandler.addToSocialSDK();
//                        qqSsoHandler.setTargetUrl("");
//                        //空间
//                        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(MessageActivity.this, Config.APP_ID_QQ, Config.APP_KEY_QQ);
//                        qZoneSsoHandler.addToSocialSDK();
//
//                        //打开分享面板
//                        mController.openShare(MessageActivity.this, false);
                        break;
                }
            }
        });

        lvMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (VisitorMode.isVistor(MessageActivity.this)) {//用来判断用户是否登录
                    return;
                }
                switch (position) {
                    case 0:
                        Session.SetInt("iswinMessage", 0);
                        msgOrNoticeAdapter1.getItem(0).setHavemessage(0);
                        msgOrNoticeAdapter1.notifyDataSetChanged();
                        startActivity(new Intent(MessageActivity.this, MessageWinning.class));
                        break;
                    case 1:
                        Session.SetInt("issendMessage", 0);
                        msgOrNoticeAdapter1.getItem(1).setHavemessage(0);
                        msgOrNoticeAdapter1.notifyDataSetChanged();
                        startActivity(new Intent(MessageActivity.this, MessageSendGoodActivity.class));
                        break;
                }
            }
        });

        mList = new ArrayList<>();//消息集合
        nList = new ArrayList<>();//通告的集合
        //还要对公告集合添加一些数据
        addData();

        //设置公告数据
        if (msgOrNoticeAdapter2 == null) {
            // 在这里填充adapter
            msgOrNoticeAdapter2 = new MsgOrNoticeAdapter(1);
            lvNotice.setAdapter(msgOrNoticeAdapter2);
        }
        //设置消息数据
        if (msgOrNoticeAdapter1 == null) {
            // 在这里填充adapter
            msgOrNoticeAdapter1 = new MsgOrNoticeAdapter(2);
            lvMessage.setAdapter(msgOrNoticeAdapter1);
        }
    }

    protected void doBusiness() {
        ivBack.setOnClickListener(this);
        btnNotice.setOnClickListener(this);
        btnMessage.setOnClickListener(this);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        /**使用SSO授权必须添加如下代码 */
//        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
//        if (ssoHandler != null) {
//            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
//        }
//
//    }

    //添加固定数据
    protected void addData() {
        Notice notice1 = new Notice();
        notice1.setTopic("夺宝奖品声明");
        notice1.setSummary("请你在参与夺宝前，认真阅读以下规则");
        notice1.setImg(R.drawable.duobaojiangpinshengming);

        Notice notice2 = new Notice();
        notice2.setTopic("夺宝规则");
        notice2.setSummary("全民夺宝的夺宝规则，请您仔细阅读");
        notice2.setImg(R.drawable.duobaoguize);

        Notice notice3 = new Notice();
        notice3.setTopic("用户协议");
        notice3.setSummary("全民夺宝的用户协议，请您认真阅读");
        notice3.setImg(R.drawable.yonghuxieyi);

        Notice notice4 = new Notice();
        notice4.setTopic("常见问题");
        notice4.setSummary("全民夺宝的常见问题，在这里您可以找到问题对应...");
        notice4.setImg(R.drawable.changjianwenti1);

        Notice notice5 = new Notice();
        notice5.setTopic("邀请好友，返利赚不停");
        notice5.setSummary("赶紧叫上您的好伙伴一起参与...");
        notice5.setImg(R.drawable.yaoqinghaoyoufanlizhuanbuting);

        nList.add(notice1);
        nList.add(notice2);
        nList.add(notice3);
        nList.add(notice4);
        nList.add(notice5);

        Notice message1 = new Notice();
        message1.setTopic("中奖消息");
        message1.setSummary("请关注实时消息哦");
        message1.setHavemessage(Session.GetInt("iswinMessage"));
        message1.setImg(R.drawable.zhongjiangxiaoxi);

        Notice message2 = new Notice();
        message2.setTopic("发货消息");
        message2.setSummary("请关注实时消息哦");
        message2.setHavemessage(Session.GetInt("issendMessage"));
        message2.setImg(R.drawable.fahuoxiaoxi);

        mList.add(message1);
        mList.add(message2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_notice:
                //1按钮变为选中状态2消息按钮变为普通状态3listiview进行选择和隐藏
//                btnMessage.setBackgroundResource(R.color.transparent);
                btnMessage.setTextColor(getResources().getColor(R.color.red));
//                btnNotice.setBackgroundResource(R.color.common_red);
                btnNotice.setTextColor(getResources().getColor(R.color.white));
                tvTitle.setBackgroundResource(R.drawable.ico_message_title_1);
                rlNotice.setVisibility(View.VISIBLE);
                rlMessage.setVisibility(View.GONE);
                break;
            case R.id.btn_message:
//                btnMessage.setBackgroundResource(R.color.common_red);
                btnMessage.setTextColor(getResources().getColor(R.color.white));
//                btnNotice.setBackgroundResource(R.color.transparent);
                btnNotice.setTextColor(getResources().getColor(R.color.red));
                rlNotice.setVisibility(View.GONE);
                rlMessage.setVisibility(View.VISIBLE);
                tvTitle.setBackgroundResource(R.drawable.ico_message_title_2);
                break;
            case R.id.ll_wechat:
                ShareUtils1.shareActive(this, SHARE_MEDIA.WEIXIN);
                break;
            case R.id.ll_wechat_friend:
                ShareUtils1.shareActive(this, SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case R.id.ll_qq:
                ShareUtils.shareActive(this, SHARE_MEDIA.QQ, "");
                break;
            case R.id.ll_qzone:
                ShareUtils.shareActive(this, SHARE_MEDIA.QZONE, "");
                break;
            case R.id.ll_sina:
                ShareUtils.promtoteSinaActive(this);
                break;

        }
    }

    @Override
    protected RequestParams getRequestParam() {
        return null;
    }

    @Override
    protected int getLayoutResouceId() {
        return R.layout.activity_message;
    }

    @Override
    protected String getUri() {
        return Config.HTTP_URL_HEAD + "Rule/getSummary";
    }

    @Override
    protected SubmitType getSubmitType() {
        return SubmitType.POST;
    }

    @Override
    protected void showUIData(Object obj) {
        //解析方法
        String result = obj.toString();
        if (TextUtils.isEmpty(result))
            return;
        try {
            nList.add(0, new Notice(new JSONObject(result)));
            msgOrNoticeAdapter2.notifyDataSetChanged();
            msgOrNoticeAdapter1.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class MsgOrNoticeAdapter extends BaseAdapter {

        private int isSelect;// 标记是选择消息还是选择公告

        public MsgOrNoticeAdapter(int isSelect) {
            this.isSelect = isSelect;
        }

        @Override
        public int getCount() {
            if (isSelect == 1) {
                return nList.size();
            } else {
                return mList.size();
            }
        }

        @Override
        public Notice getItem(int position) {
            if (isSelect == 1) {
                return nList.get(position);
            } else {
                return mList.get(position);
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(), R.layout.message_list_item, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Notice notice = getItem(position);
            viewHolder.tv_title.setText(notice.getTopic());
            viewHolder.tv_desc.setText(notice.getSummary());
            if (isSelect == 2) {
                if (notice.getHavemessage() > 0) {
                    viewHolder.tv_number.setText(notice.getHavemessage() + "");
                    viewHolder.rl_havemsg.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.rl_havemsg.setVisibility(View.GONE);
                }
            }
            //专为系统公告设置的显示圆点
            if (isSelect == 1) {
                if (Session.GetBoolean("isSysMessage")) {
                    viewHolder.rl_havemsg.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.rl_havemsg.setVisibility(View.GONE);
                }
            }

            if (notice.getImg() != 0) {
                viewHolder.iv_img.setImageResource(notice.getImg());
            } else {
                viewHolder.iv_img.setImageResource(R.drawable.xtgg);
            }
            if (!CommonUtil.isEmpty(notice.getTime())) {
                viewHolder.tv_time.setVisibility(View.VISIBLE);
                String i = (Long.parseLong(notice.getTime()) / 1000) + "";
                viewHolder.tv_time.setText(DateTimeUtil.timestamp2Time(i));
            }
            return convertView;
        }
    }

    public class ViewHolder {
        private TextView tv_title;
        private TextView tv_desc;
        private TextView tv_time;
        private ImageView iv_img;
        private TextView tv_number;
        private RelativeLayout rl_havemsg;

        public ViewHolder(View view) {
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_desc = (TextView) view.findViewById(R.id.tv_desc);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            iv_img = (ImageView) view.findViewById(R.id.iv_img);
            rl_havemsg = (RelativeLayout) view.findViewById(R.id.rl_havemsg);//是否展示中奖消息和发货消息
            tv_number = (TextView) view.findViewById(R.id.tv_number);//是否展示中奖消息和发货消息
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        msgOrNoticeAdapter1.notifyDataSetChanged();
        msgOrNoticeAdapter2.notifyDataSetChanged();
    }
}
