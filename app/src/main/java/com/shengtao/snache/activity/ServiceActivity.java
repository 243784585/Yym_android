package com.shengtao.snache.activity;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shengtao.R;
import com.shengtao.baseview.BaseActivity;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * @package com.shengtao.snache.activity
 * Created by TT on 2015/12/18.
 * Description:客服反馈
 */
public class ServiceActivity extends BaseActivity {

    private PullToRefreshListView lvService;

    @Override
    protected int setLayout() {
        return R.layout.activity_service;
    }

    @Override
    protected void initView() {
        lvService = (PullToRefreshListView) findViewById(R.id.lv_service);
        lvService.setAdapter(new ServiceAdapter());
        lvService.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(ServiceActivity.this,ServiceDetailActivity.class));
            }
        });
    }

    @Override
    protected void doBusiness() {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public int getHeaderType() {
        return 1;
    }

    @Override
    protected String getAvtionTitle() {
        return "客服反馈";
    }

    class ServiceAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if(convertView==null){
                convertView = View.inflate(getApplicationContext(),R.layout.item_service,null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }

            return convertView;
        }
    }

    public class ViewHolder{
        private TextView tvTitle;
        private TextView tvDesc;

        public ViewHolder(View view){
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            tvDesc = (TextView) view.findViewById(R.id.tv_desc);
        }
    }

}
