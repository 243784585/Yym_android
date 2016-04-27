package com.shengtao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengtao.R;
import com.shengtao.application.ImageLoaderCfg;
import com.shengtao.application.UIApplication;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by TT on 15/10/1.
 */
public class UpLoadAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String, Object>> data;
    private LayoutInflater inflater;

    public UpLoadAdapter(Context context, ArrayList<HashMap<String, Object>> data) {

        this.data = data;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Img7niuHolder img7niuHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_progress_img, null);
            img7niuHolder = new Img7niuHolder(convertView);
            convertView.setTag(img7niuHolder);
        } else {
            img7niuHolder = (Img7niuHolder) convertView.getTag();
        }
        HashMap<String, Object> hashMap = data.get(position);
        String path = hashMap.get("url").toString();//获取图片的本地路径

        ImageLoader.getInstance().displayImage("file://" + path,img7niuHolder.iv_uploadimg, ImageLoaderCfg.options);

        String flag = hashMap.get("flag").toString();
        if("0".equals(flag)){
            img7niuHolder.rpb_uploadbar.setVisibility(View.VISIBLE);
        }else {
            img7niuHolder.rpb_uploadbar.setVisibility(View.GONE);
        }

        return convertView;
    }

    private static class Img7niuHolder {
        ImageView iv_uploadimg;
        ProgressBar rpb_uploadbar;

        public Img7niuHolder(View convertView) {
            this.iv_uploadimg = (ImageView) convertView.findViewById(R.id.iv_uploadimg);
            this.rpb_uploadbar = (ProgressBar) convertView.findViewById(R.id.rpb_uploadbar);
        }

    }
}
