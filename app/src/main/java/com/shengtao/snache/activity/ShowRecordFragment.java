package com.shengtao.snache.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.shengtao.R;
import com.shengtao.foundation.BaseFragment;
import com.shengtao.foundation.IToastBlock;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * @package com.baixi.snache.activity
 * Created by TT on 2015/12/17.
 * Description:晒单记录
 */
public class ShowRecordFragment extends BaseFragment {

    private View view;
    private GridView gvShow;
    private PullToRefreshListView lv_show_record;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.inflect_show_record,null);
        initView(view);
        return view;
    }

    //获取对象
    public static ShowRecordFragment newInstance() {
        ShowRecordFragment df = new ShowRecordFragment();

        return df;
    }

    public void initView(View view){
        //获取动态的资源id
        lv_show_record = (PullToRefreshListView) view.findViewById(R.id.lv_show_record);
        inflater(view);
    }
    public void inflater(View view){
        lv_show_record.setAdapter(new MsgWinningAdapter());
    }



    @Override
    public IToastBlock getToastBlock() {
        return null;
    }

    class MsgWinningAdapter extends BaseAdapter {

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
                convertView = View.inflate(getActivity(),R.layout.item_show_record,null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            return convertView;
        }
    }

    public class ViewHolder{

        public ViewHolder(View view){

        }
    }
}
