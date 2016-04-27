package com.shengtao.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shengtao.R;


/**
 * Created by Scoot on 2015/12/22.
 */
public class ShareOrderAdapter extends BaseAdapter {


    private Context context;

    public ShareOrderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.show_prize_item, null);
        }
        ViewHolder holder = ViewHolder.getHolder(convertView);

        //设置数据



        return convertView;
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
}
