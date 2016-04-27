package com.shengtao.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shengtao.R;
import com.shengtao.mine.activity.OtherPersonCenter;

import java.util.List;

/**
 * @package com.baixi.adapter
 * Created by TT on 2015/12/20.
 * Description:往期揭晓
 */
public class PublishedAdapter extends BaseAdapter {

    private ImageView ivIcon;

    private Context context;

    private List datas;

    private LayoutInflater inflater;
    public PublishedAdapter(Context context,List datas){
        this.context = context;
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
            convertView = View.inflate(context,R.layout.item_published, null);
            vh = new Viewvher(convertView);
            convertView.setTag(vh);
        } else {
            vh = (Viewvher) convertView.getTag();
        }
        vh.iv_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, OtherPersonCenter.class));
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

        public Viewvher(View view) {
            iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            tv_winner = (TextView) view.findViewById(R.id.tv_winner);
            tv_id = (TextView) view.findViewById(R.id.tv_id);
            tv_luck_nummber = (TextView) view.findViewById(R.id.tv_luck_nummber);
            tv_join_nummber = (TextView) view.findViewById(R.id.tv_join_nummber);
            tv_comment_title = (TextView) view.findViewById(R.id.tv_comment_title);
            tv_share_time = (TextView) view.findViewById(R.id.tv_share_time);
            tv_share_content = (TextView) view.findViewById(R.id.tv_share_content);
        }
    }
}
