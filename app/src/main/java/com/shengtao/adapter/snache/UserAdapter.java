package com.shengtao.adapter.snache;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengtao.R;
import com.shengtao.application.ImageLoaderCfgCircleHeadImg;
import com.shengtao.domain.snache.PrizeDetailsJoinUserInfo;

import java.util.List;

/**
 * @package com.shengtao.adapter.snache
 * Created by TT on 2015/12/23.
 * Description:
 */
public class UserAdapter extends BaseAdapter {

    private Context context;

    private List datas;

    public UserAdapter(Context context, List datas) {
        this.context = context;
        this.datas = datas;
    }

    public List getDatas() {
        return datas;
    }

    public void setDatas(List datas) {
        this.datas = datas;
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
            convertView = View.inflate(context, R.layout.item_prize_detail, null);
            vh = new Viewvher(convertView);
            convertView.setTag(vh);
        } else {
            vh = (Viewvher) convertView.getTag();
        }
        PrizeDetailsJoinUserInfo pInfo = (PrizeDetailsJoinUserInfo) getItem(position);
         if("".equals(pInfo.getHead_img())){
          vh.iv_icon.setVisibility(View.GONE);
         }
        if("".equals(pInfo.getIp_address())){
            vh.tv_ip.setVisibility(View.GONE);
        }
        if("".equals(pInfo.getRmb_num())){
            vh.tv_rmb_num.setVisibility(View.GONE);
        }
        if("".equals(pInfo.getClient_name())){
            vh.tv_username.setVisibility(View.GONE);
        }

        ImageLoader.getInstance().displayImage(pInfo.getHead_img(), vh.iv_icon, ImageLoaderCfgCircleHeadImg.options);
        vh.tv_username.setText(pInfo.getClient_name());
        if(pInfo.getIp_address()!=null){
            vh.tv_ip.setText("("+pInfo.getIp_address()+"  "+pInfo.getAddress_info()+")");
        }
        vh.tv_rmb_num.setText(Html.fromHtml("<font color=#999999>参与了</font>"+pInfo.getRmb_num()+"<font color=#999999>人次</font>"));
        vh.create_time.setText(pInfo.getCreate_time());

//        vh.iv_icon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//    }
//});
        return convertView;
    }

    private class Viewvher {
        private ImageView iv_icon;
        private TextView tv_username;
        private TextView tv_rmb_num;
        private TextView tv_ip;
        private TextView create_time;

        public Viewvher(View view) {
            iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            tv_username = (TextView) view.findViewById(R.id.tv_username);
            tv_rmb_num = (TextView) view.findViewById(R.id.tv_rmb_num);
            tv_ip = (TextView) view.findViewById(R.id.tv_ip);
            create_time = (TextView) view.findViewById(R.id.create_time);
        }
    }
}
