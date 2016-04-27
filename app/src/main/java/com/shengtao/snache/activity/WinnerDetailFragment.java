package com.shengtao.snache.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.shengtao.R;
import com.shengtao.foundation.BaseFragment;
import com.shengtao.foundation.IToastBlock;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * @package com.baixi.snache.activity
 * Created by TT on 2015/12/17.
 * Description:中奖详情-fragment
 */
@SuppressLint("ValidFragment")
public class WinnerDetailFragment extends BaseFragment {

    private View view;
    private PullToRefreshListView lvWinDetail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.inflect_win_detail, null);
        initView(view);
        return view;
    }

    //获取对象
    public static WinnerDetailFragment newInstance() {
        WinnerDetailFragment df = new WinnerDetailFragment();

        return df;
    }

    public void initView(View view){
        //获取中奖详情的资源id
        lvWinDetail = (PullToRefreshListView) view.findViewById(R.id.lv_win_detail);
        inflater(view);
    }
    public void inflater(View view){
        lvWinDetail.setAdapter(new MsgWinningAdapter());
    }


    @Override
    public IToastBlock getToastBlock() {
        return null;
    }


    class MsgWinningAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 5;
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
                convertView = View.inflate(getActivity(),R.layout.item_win_detail,null);
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
