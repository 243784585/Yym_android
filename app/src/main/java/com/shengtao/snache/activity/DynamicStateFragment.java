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
 * Created by TT on 2015/12/16.
 * Description:他的个人中心-动态的fragment
 */
@SuppressLint("ValidFragment")
public class DynamicStateFragment extends BaseFragment {

    private View view;
    private PullToRefreshListView lvDynamicState;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.inflect_dynamicstate, null);
        initView(view);
        return view;
    }

    //获取对象
    public static DynamicStateFragment newInstance() {
        DynamicStateFragment df = new DynamicStateFragment();

        return df;
    }

    public void initView(View view){
        //获取动态的资源id
        lvDynamicState = (PullToRefreshListView) view.findViewById(R.id.lv_dynamic_state);
        inflater(view);
    }
    public void inflater(View view){
        lvDynamicState.setAdapter(new MsgWinningAdapter());
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
                convertView = View.inflate(getActivity(),R.layout.item_dynamicstate,null);
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
