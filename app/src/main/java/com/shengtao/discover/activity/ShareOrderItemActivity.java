package com.shengtao.discover.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengtao.R;
import com.shengtao.application.ImageLoaderCfg;
import com.shengtao.application.UIApplication;
import com.shengtao.baseview.BaseActivity;
import com.shengtao.domain.Config;
import com.shengtao.domain.discover.AllShareDetail;
import com.shengtao.domain.discover.WinerShareDetail;
import com.shengtao.mine.activity.OtherPersonCenter;
import com.shengtao.utils.CircleImageView;
import com.shengtao.utils.CommonUtil;
import com.shengtao.utils.DateTimeUtil;


/**
 * Created by Scoot on 2015/12/22.
 * <p/>
 * 晒单详情页面
 */
public class ShareOrderItemActivity extends BaseActivity {

    private Object list;
    private ImageView iv_head_image, img_1, img_2, img_3;
    private TextView user_account, tv_show_prize_title, tv_prize_title, tv_buy_count, tv_lucknum, tv_time, show_prize_body, show_prized_time;
    private LinearLayout layout_bingo_into_1, layout_bingo_into_2, layout_bingo_into_3;
    private Boolean isZero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        list = intent.getSerializableExtra("mList");
        isZero = intent.getBooleanExtra("isZero", false);
        fillData();
    }

    @Override
    protected int setLayout() {
        return R.layout.show_prize_detail;
    }

    @Override
    protected String getAvtionTitle() {
        return "晒单详情";
    }

    @Override
    public int getHeaderType() {
        return 1;
    }

    //初始化数据
    @Override
    protected void initView() {
        iv_head_image = (CircleImageView) findViewById(R.id.iv_head_image);
        img_1 = (ImageView) findViewById(R.id.img_1);
        img_2 = (ImageView) findViewById(R.id.img_2);
        img_3 = (ImageView) findViewById(R.id.img_3);
        img_1.setVisibility(View.VISIBLE);

        user_account = (TextView) findViewById(R.id.user_account);//昵称
        layout_bingo_into_1 = (LinearLayout) findViewById(R.id.layout_bingo_into_1);
        layout_bingo_into_2 = (LinearLayout) findViewById(R.id.layout_bingo_into_2);
        layout_bingo_into_3 = (LinearLayout) findViewById(R.id.layout_bingo_into_3);

        tv_show_prize_title = (TextView) findViewById(R.id.tv_show_prize_title);//晒单标题
        tv_prize_title = (TextView) findViewById(R.id.tv_prize_title);//获奖奖品
        show_prized_time = (TextView) findViewById(R.id.show_prized_time);

        tv_buy_count = (TextView) findViewById(R.id.tv_buy_count);//本期参与
        tv_lucknum = (TextView) findViewById(R.id.tv_lucknum);//幸运号码
        tv_time = (TextView) findViewById(R.id.tv_time);//揭晓时间
        show_prize_body = (TextView) findViewById(R.id.show_prize_body);//晒单正文

    }

    @Override
    protected void doBusiness() {
    }

    //填充数据
    private void fillData() {

        if (list != null) {
            if (!isZero) {
                final AllShareDetail list1 = (AllShareDetail) this.list;
                user_account.setText(list1.getClient_name());
                tv_show_prize_title.setText(list1.getShare_title());

                tv_prize_title.setText(list1.getGoods_name());
                tv_buy_count.setText(list1.getAll_join_num());
                tv_lucknum.setText(list1.getLucky_id());
                ImageLoader.getInstance().displayImage(list1.getHead_img(), iv_head_image, UIApplication.getAvatar());
                iv_head_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ShareOrderItemActivity.this, OtherPersonCenter.class);
                        intent.putExtra("id", list1.getUserId());
                        startActivity(intent);

                    }
                });

                String n = (Long.parseLong(list1.getShare_time()) / 1000) + "";
                show_prized_time.setText(DateTimeUtil.timestamp2Time(n));
                if (!CommonUtil.isEmpty(list1.getOpen_time())) {
                    String i = (Long.parseLong(list1.getOpen_time()) / 1000) + "";
                    tv_time.setText(DateTimeUtil.timestamp2Time(i));
                }

                ImageLoader.getInstance().displayImage(list1.getHead_img(), iv_head_image, ImageLoaderCfg.options);

                show_prize_body.setText(list1.getShare_content());
                String[] arry = list1.getGROUP_CONCAT().split(",");
                int j = arry.length;
                if (j > 0) {
                    switch (arry.length) {
                        case 1:
                            img_1.setVisibility(View.VISIBLE);
                            String s1 = arry[0];
                            if (s1.contains(Config.HTTP))
                                if (!s1.contains("imageView2")) {
                                    s1 = s1 + "?imageView2/2/w/640";
                                }
                            ImageLoader.getInstance().displayImage(s1, img_1);
                            break;
                        case 2:
                            img_1.setVisibility(View.VISIBLE);
                            img_2.setVisibility(View.VISIBLE);
                            String s2 = arry[0];
                            if (s2.contains(Config.HTTP))
                                if (!s2.contains("imageView2")) {
                                    s2 = s2 + "?imageView2/2/w/640";
                                }
                            String s3 = arry[1];
                            if (s3.contains(Config.HTTP))
                                if (!s3.contains("imageView2")) {
                                    s3 = s3 + "?imageView2/2/w/640";
                                }
                            ImageLoader.getInstance().displayImage(s2, img_1);
                            ImageLoader.getInstance().displayImage(s3, img_2);
                            break;
                        case 3:
                            img_1.setVisibility(View.VISIBLE);
                            img_2.setVisibility(View.VISIBLE);
                            img_3.setVisibility(View.VISIBLE);
                            String s4 = arry[0];
                            if (s4.contains(Config.HTTP))
                                if (!s4.contains("imageView2")) {
                                    s4 = s4 + "?imageView2/2/w/640";
                                }
                            String s5 = arry[1];
                            if (s5.contains(Config.HTTP))
                                if (!s5.contains("imageView2")) {
                                    s5 = s5 + "?imageView2/2/w/640";
                                }
                            String s6 = arry[2];
                            if (s6.contains(Config.HTTP))
                                if (!s6.contains("imageView2")) {
                                    s6 = s6 + "?imageView2/2/w/640";
                                }
                            ImageLoader.getInstance().displayImage(s4, img_1);
                            ImageLoader.getInstance().displayImage(s5, img_2);
                            ImageLoader.getInstance().displayImage(s6, img_3);
                            break;
                    }
                }


//            if(list.get(0).getShare_time()!=null && !"null".equals(list.get(0).getShare_time())) {
//                String j = (Long.parseLong(list.get(0).getShare_time()) / 1000) + "";
//                show_prized_time.setText(DateTimeUtil.timestamp2Time(j));
//            }


            } else {
                final WinerShareDetail list2 = (WinerShareDetail) this.list;
                user_account.setText(list2.getClient_name());
                tv_show_prize_title.setText(list2.getShare_title());

                tv_prize_title.setText(list2.getZgoods_name());
                ImageLoader.getInstance().displayImage(list2.getHead_img(), iv_head_image, UIApplication.getAvatar());

                String k = (Long.parseLong(list2.getShare_time()) / 1000) + "";
                show_prized_time.setText(DateTimeUtil.timestamp2Time(k));
                layout_bingo_into_1.setVisibility(View.GONE);
                layout_bingo_into_2.setVisibility(View.GONE);
                layout_bingo_into_3.setVisibility(View.GONE);

//                    if (!CommonUtil.isEmpty(list1.getOpen_time())) {
//                        String i = (Long.parseLong(list2.getOpen_time()) / 1000) + "";
//                        tv_time.setText(DateTimeUtil.timestamp2Time(i));
//                    }

                ImageLoader.getInstance().displayImage(list2.getHead_img(), iv_head_image, ImageLoaderCfg.options);

                show_prize_body.setText(list2.getShare_content());
                String[] arry1 = list2.getUrlGroup().split(",");
                int o = arry1.length;
                if (o > 0) {
                    switch (arry1.length) {
                        case 1:
                            img_1.setVisibility(View.VISIBLE);
                            String s1 = arry1[0];
                            if (s1.contains(Config.HTTP))
                                if (!s1.contains("imageView2")) {
                                    s1 = s1 + "?imageView2/2/w/640";
                                }
                            ImageLoader.getInstance().displayImage(s1, img_1);
                            break;
                        case 2:
                            img_1.setVisibility(View.VISIBLE);
                            img_2.setVisibility(View.VISIBLE);
                            String s2 = arry1[0];
                            if (s2.contains(Config.HTTP))
                                if (!s2.contains("imageView2")) {
                                    s2 = s2 + "?imageView2/2/w/640";
                                }
                            String s3 = arry1[1];
                            if (s3.contains(Config.HTTP))
                                if (!s3.contains("imageView2")) {
                                    s3 = s3 + "?imageView2/2/w/640";
                                }
                            ImageLoader.getInstance().displayImage(s2, img_1);
                            ImageLoader.getInstance().displayImage(s3, img_2);
                            break;
                        case 3:
                            img_1.setVisibility(View.VISIBLE);
                            img_2.setVisibility(View.VISIBLE);
                            img_3.setVisibility(View.VISIBLE);
                            String s4 = arry1[0];
                            if (s4.contains(Config.HTTP))
                                if (!s4.contains("imageView2")) {
                                    s4 = s4 + "?imageView2/2/w/640";
                                }
                            String s5 = arry1[1];
                            if (s5.contains(Config.HTTP))
                                if (!s5.contains("imageView2")) {
                                    s5 = s5 + "?imageView2/2/w/640";
                                }
                            String s6 = arry1[2];
                            if (s6.contains(Config.HTTP))
                                if (!s6.contains("imageView2")) {
                                    s6 = s6 + "?imageView2/2/w/640";
                                }
                            ImageLoader.getInstance().displayImage(s4, img_1);
                            ImageLoader.getInstance().displayImage(s5, img_2);
                            ImageLoader.getInstance().displayImage(s6, img_3);
                            break;
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View view) {

    }
}
