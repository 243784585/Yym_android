package com.shengtao.discover.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengtao.R;
import com.shengtao.application.ImageLoaderCfg;
import com.shengtao.application.UIApplication;
import com.shengtao.baseview.BaseListActivity;
import com.shengtao.domain.Config;
import com.shengtao.domain.discover.AllShare;
import com.shengtao.domain.discover.AllShareDetail;
import com.shengtao.foundation.BaseEnitity;
import com.shengtao.foundation.SubmitType;
import com.shengtao.snache.activity.AtyImagePagerActivity;
import com.shengtao.utils.DateTimeUtil;
import com.shengtao.utils.LYDBHeader;
import com.shengtao.utils.ToastUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Scoot on 2015/12/22.
 * <p/>
 * 奖品晒单
 */
public class ShareOrderActivity extends BaseListActivity {

    private WinnerOrderAdapter mWinnerOrderAdapter;
    RequestParams requestParams;

    private ArrayList<AllShareDetail> mList = null;
    private AllShare allShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

    }

    private void initView() {
        LYDBHeader lyb = (LYDBHeader) findViewById(R.id.header);
        lyb.setVisibility(View.VISIBLE);
    }

    @Override
    protected int getPullToRefreshListViewResouceId() {
        return R.id.lv_dynamic_state;
    }

    @Override
    protected int getLayoutResouceId() {
        return R.layout.inflect_dynamicstate;
    }

    @Override
    protected String getAvtionTitle() {
        return "晒单分享";
    }

    @Override
    public int getHeaderType() {
        return 1;
    }

    @Override
    protected void setAdapter() {
        if (mWinnerOrderAdapter == null) {
            mWinnerOrderAdapter = new WinnerOrderAdapter();
            prlv_details.setAdapter(mWinnerOrderAdapter);
        }
        mWinnerOrderAdapter.notifyDataSetChanged();
    }

    @Override
    protected <T extends BaseEnitity> List<T> getDataList() {
        return null;
    }

    @Override
    protected RequestParams getRequestParam() {
        requestParams = new RequestParams();
        requestParams.put("pageNum", mCurPageIndex);
        return requestParams;
    }

    @Override
    protected RequestParams getRefreshRequestParam() {
        requestParams = new RequestParams();
        requestParams.put("pageNum", mCurPageIndex);
        return requestParams;
    }

    @Override
    protected void parseResult(String result, boolean isRefresh) {
        if (TextUtils.isEmpty(result)) {
            return;
        }
        //集合
        List<AllShareDetail> results = new ArrayList<>();
        if (mList == null) {
            //存储集合
            mList = new ArrayList();
        }
        try {
            JSONObject json = new JSONObject(result);
            allShare = new AllShare(json);
            results = allShare.getAllShareDetail();
            if (isRefresh) {
                mList.clear();
            }
            if (results.size() > 0) {
                mList.addAll(results);
            } else {
                ToastUtil.loadMoreTips(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getUri() {
        return Config.HTTP_URL_HEADED + "goods/AllShareOrder";
    }

    @Override
    protected SubmitType getSubmitType() {
        return SubmitType.POST;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(ShareOrderActivity.this, ShareOrderItemActivity.class);
        intent.putExtra("mList", mList.get(position - 1));
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        return false;
    }

    class WinnerOrderAdapter extends BaseAdapter {



        @Override
        public int getCount() {
            return mList == null ? 0 : mList.size();
        }

        @Override
        public AllShareDetail getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = View.inflate(ShareOrderActivity.this, R.layout.show_prize_item, null);
            }

            ViewHolder holder = ViewHolder.getHolder(convertView);
            AllShareDetail info = getItem(position);
            holder.userNick.setText(info.getClient_name());
//            holder.showPrizedTime.setText(info.getShare_time());
            String n =(Long.parseLong(info.getShare_time())/1000)+"";
            holder.showPrizedTime.setText(DateTimeUtil.timestamp2Time(n));

            holder.tv_show_prized_title.setText(info.getShare_title());
            holder.tv_show_prized_detail.setText(info.getGoods_name());
            holder.show_prized_body.setText(info.getShare_content());
            ImageLoader.getInstance().displayImage(info.getHead_img(), holder.ivUserHeader,UIApplication.getAvatar());

            if (info.getGROUP_CONCAT() != null) {
                String[] arry = info.getGROUP_CONCAT().split(",");
                final List<HashMap<String, String>> list = new ArrayList<>();
                if (arry[0] != null) {
                    for (int i = 0; i < arry.length; i++) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("image", arry[i]);//"http://pic.nipic.com/2007-11-09/2007119122519868_2.jpg"
                        list.add(hashMap);
                    }
                }

                final String[] urls = convertUrls(list);
                if(arry.length > 0){
                    switch (arry.length) {
                        case 1:
                            holder.image1.setVisibility(View.VISIBLE);
                            holder.image2.setVisibility(View.GONE);
                            holder.image3.setVisibility(View.GONE);
                            String s1 = arry[0];
                            s1 = qiniuend(s1);
                            ImageLoader.getInstance().displayImage(s1, holder.image1,ImageLoaderCfg.options);
                            holder.image1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    imageBrower(0, urls);
                                }
                            });

                            break;
                        case 2:
                            holder.image1.setVisibility(View.VISIBLE);
                            holder.image2.setVisibility(View.VISIBLE);
                            holder.image3.setVisibility(View.GONE);
                            String s2 = arry[0];
                            s2 = qiniuend(s2);
                            String s3 = arry[1];
                            s3 = qiniuend(s3);
                            ImageLoader.getInstance().displayImage(s2, holder.image1,ImageLoaderCfg.options);
                            ImageLoader.getInstance().displayImage(s3, holder.image2,ImageLoaderCfg.options);
                            holder.image1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    imageBrower(0, urls);
                                }
                            });
                            holder.image2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    imageBrower(1, urls);
                                }
                            });
                            break;
                        case 3:
                            holder.image1.setVisibility(View.VISIBLE);
                            holder.image2.setVisibility(View.VISIBLE);
                            holder.image3.setVisibility(View.VISIBLE);
                            String s4 = arry[0];
                            s4 = qiniuend(s4);
                            String s5 = arry[1];
                            s5 = qiniuend(s5);
                            String s6 = arry[2];
                            s6 = qiniuend(s6);
                            ImageLoader.getInstance().displayImage(s4, holder.image1,ImageLoaderCfg.options);
                            ImageLoader.getInstance().displayImage(s5, holder.image2,ImageLoaderCfg.options);
                            ImageLoader.getInstance().displayImage(s6, holder.image3,ImageLoaderCfg.options);
                            holder.image1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    imageBrower(0, urls);

                                }
                            });
                            holder.image2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    imageBrower(1, urls);
                                }
                            });
                            holder.image3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    imageBrower(2, urls);
                                }
                            });
                            break;
                    }
                }
            }

            return convertView;
        }
    }

    private String[] convertUrls(List<HashMap<String, String>> urls) {
        String[] urlArray = new String[urls.size()];
        for (int i = 0; i < urls.size(); i++) {
            urlArray[i] = urls.get(i).get("image");
        }
        return urlArray;
    }

    /**
     * 图片浏览器
     *
     * @param position
     * @param urls
     */
    private void imageBrower(int position, String[] urls) {
        Intent intent = new Intent(ShareOrderActivity.this, AtyImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(AtyImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(AtyImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        ShareOrderActivity.this.startActivity(intent);
    }

    static class ViewHolder {
        ImageView ivUserHeader, image1, image3, image2;
        TextView userNick, showPrizedTime, tv_show_prized_title, tv_show_prized_detail, show_prized_body;


        public ViewHolder(View convertView) {
            ivUserHeader = (ImageView) convertView.findViewById(R.id.iv_user_head);
            userNick = (TextView) convertView.findViewById(R.id.user_nick);
            showPrizedTime = (TextView) convertView.findViewById(R.id.show_prized_time);
            tv_show_prized_title = (TextView) convertView.findViewById(R.id.tv_show_prized_title);
            tv_show_prized_detail = (TextView) convertView.findViewById(R.id.tv_show_prized_detail);
            show_prized_body = (TextView) convertView.findViewById(R.id.show_prized_body);
            image1 = (ImageView) convertView.findViewById(R.id.img_1);
            image2 = (ImageView) convertView.findViewById(R.id.img_2);
            image3 = (ImageView) convertView.findViewById(R.id.img_3);
        }

        public static ViewHolder getHolder(View convertView) {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            if (holder == null) {
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }
            return holder;
        }
    }

    /**
     * 返回带七牛小尾巴的图片url
     * * @param qiniu
     * @return
     */
    public String qiniuend(String qiniu) {
        if (qiniu.contains(Config.HTTP)) {
            if (!qiniu.contains("imageView2")) {
                qiniu = qiniu + "?imageView2/2/w/160";
            }
        }
        return qiniu;
    }
}
