package com.shengtao.adapter.snache;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengtao.R;
import com.shengtao.application.ImageLoaderCfg;
import com.shengtao.application.ImageLoaderCfgCircle;
import com.shengtao.discover.activity.ShareOrderItemActivity;
import com.shengtao.domain.discover.AllShareDetail;
import com.shengtao.domain.snache.PublishedGood;
import com.shengtao.mine.activity.OtherPersonCenter;
import com.shengtao.snache.activity.AtyImagePagerActivity;
import com.shengtao.utils.CommonUtil;
import com.shengtao.utils.DateTimeUtil;
import com.shengtao.utils.MyGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @package com.shengtao.adapter
 * Created by TT on 2015/12/20.
 * Description:往期揭晓
 */
public class PublishedAdapter extends BaseAdapter {

    private ImageView ivIcon;

    private Context context;

    private List datas;

    private LayoutInflater inflater;
    public PublishedAdapter(Context context, List datas){

        this.context = context;
        this.datas = datas;
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
        Intent intent = new Intent(context, AtyImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(AtyImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(AtyImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        context.startActivity(intent);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Viewvher vh = null;
        if (convertView == null) {
            convertView = View.inflate(context,R.layout.item_published, null);
            vh = new Viewvher(convertView);
            convertView.setTag(vh);
        } else {
            vh = (Viewvher) convertView.getTag();
        }
        final PublishedGood item = (PublishedGood) getItem(position);
        //填充数据
        vh.tv_open_time.setText("第" + item.getGoods_current_num() + "期" + "  揭晓时间：" + DateTimeUtil.timestamp2Time(Long.parseLong(item.getOpen_time()) / 1000 + ""));
        ImageLoader.getInstance().displayImage(item.getHead_img(), vh.iv_icon, ImageLoaderCfgCircle.options);
        vh.tv_winner.setText(" " + item.getClient_name());
        vh.tv_id.setText(item.getIp_address() + item.getAddress_info());
        vh.tv_luck_nummber.setText(item.getLucky_id());
        vh.tv_join_nummber.setText(item.getClient_join_num() + "人次");

        if(!CommonUtil.isEmpty(item.getShare_title())){
            vh.tv_comment_title.setText(item.getShare_title());
        }else{
            vh.tv_comment_title.setVisibility(View.GONE);
        }
        if(!CommonUtil.isEmpty(item.getShare_time())){
            vh.tv_share_time.setText(DateTimeUtil.timestamp3Time(item.getShare_time()));
        }else{
            vh.tv_share_time.setText("此商品暂未晒单");
        }
        if(!CommonUtil.isEmpty(item.getShare_content())){
            vh.tv_share_content.setText(item.getShare_content());
        }else{
            vh.tv_share_content.setVisibility(View.GONE);
        }

        String allImgUrl = item.getAllImgUrl();
        String[] allImg = allImgUrl.split(",");
        final List<HashMap<String, String>> list = new ArrayList<>();
        if (!CommonUtil.isEmpty(allImg[0])) {
            for (int i = 0; i < allImg.length; i++) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("image", allImg[i]);//"http://pic.nipic.com/2007-11-09/2007119122519868_2.jpg"
                list.add(hashMap);
            }
        }
        if(list.size() == 1){
            vh.image_single.setVisibility(View.VISIBLE);
            vh.lv_img.setVisibility(View.GONE);
            String urlPath = list.get(0).get("image");
            ImageLoader.getInstance().displayImage(urlPath,vh.image_single, ImageLoaderCfg.options);
            vh.image_single.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String[] url = convertUrls(list);
                    imageBrower(0, url);
                }
            });
        }else {
            vh.image_single.setVisibility(View.GONE);
            vh.lv_img.setVisibility(View.VISIBLE);
            //GridView SimpleAdapter
            SimpleAdapter simpleAdapter = new SimpleAdapter(context, list, R.layout.item_gridview_image, new String[]{"image"}, new int[]{R.id.iv_image}) {
                @Override
                public void setViewImage(ImageView v, String value) {
                    super.setViewImage(v, value);
                    ImageLoader.getInstance().displayImage(value, v,ImageLoaderCfg.options);
                }
            };

            final String[] urls = convertUrls(list);
            vh.lv_img.setAdapter(simpleAdapter);
            //设置点击gradview空白处不消费事件
            vh.lv_img.setOnTouchInvalidPositionListener(new MyGridView.OnTouchInvalidPositionListener() {
                @Override
                public boolean onTouchInvalidPosition(int motionEvent) {
                    return false;
                }
            });
            vh.lv_img.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //弹出图片预览集
                    imageBrower(position, urls);
                }
            });
        }

        vh.ll_to_shareorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CommonUtil.isEmpty(item.getShare_title())){
                    return;
                }
                Intent intent = new Intent(context, ShareOrderItemActivity.class);
                AllShareDetail allShareDetail = new AllShareDetail();
                allShareDetail.setUserId(item.getClientid());
                allShareDetail.setClient_name(item.getClient_name());
                allShareDetail.setGoods_current_num(item.getGoods_current_num());
                allShareDetail.setGoods_name(item.getGoods_name());
                allShareDetail.setHead_img(item.getHead_img());
                allShareDetail.setLucky_id(item.getLucky_id());
                allShareDetail.setOpen_time(item.getOpen_time());
                allShareDetail.setShare_content(item.getShare_content());
                allShareDetail.setShare_time(item.getShare_time());
                allShareDetail.setShare_title(item.getShare_title());
                intent.putExtra("mList", allShareDetail);
                context.startActivity(intent);
            }
        });

        //点击头像进入他人个人中心
        vh.iv_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OtherPersonCenter.class);
                intent.putExtra("id",item.getClientid());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    private class Viewvher {
        private ImageView iv_icon;
        private TextView tv_winner;
        private TextView tv_id;
        private TextView tv_luck_nummber;//精华
        private TextView tv_join_nummber;
        private TextView tv_comment_title;
        private TextView tv_share_time;
        private TextView tv_share_content;
        private TextView tv_open_time;
        private ImageView image_single;
        private MyGridView lv_img;
        private LinearLayout ll_to_shareorder;

        public Viewvher(View view) {
            iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            tv_winner = (TextView) view.findViewById(R.id.tv_winner);
            tv_open_time = (TextView) view.findViewById(R.id.tv_open_time);
            tv_id = (TextView) view.findViewById(R.id.tv_id);
            tv_luck_nummber = (TextView) view.findViewById(R.id.tv_luck_nummber);
            tv_join_nummber = (TextView) view.findViewById(R.id.tv_join_nummber);
            tv_comment_title = (TextView) view.findViewById(R.id.tv_comment_title);
            tv_share_time = (TextView) view.findViewById(R.id.tv_share_time);
            tv_share_content = (TextView) view.findViewById(R.id.tv_share_content);
            image_single = (ImageView) view.findViewById(R.id.image_single);
            lv_img = (MyGridView) view.findViewById(R.id.lv_img);
            ll_to_shareorder = (LinearLayout) view.findViewById(R.id.ll_to_shareorder);
        }
    }
}
