package com.shengtao.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.shengtao.R;
import com.shengtao.discover.activity.OneCaptureActivity;
import com.shengtao.discover.activity.ShareOrderActivity;
import com.shengtao.utils.VisitorMode;


/**
 * 发现
 */

public class DiscoverFragment extends Fragment implements OnClickListener {

    private LinearLayout share, capture;

    private View travelMainView;
    private RelativeLayout app_top_bar;

    private int count = 1;

    static DiscoverFragment newInstance(String s) {
        DiscoverFragment newFragment = new DiscoverFragment();
        Bundle bundle = new Bundle();
        bundle.putString("hello", s);
        newFragment.setArguments(bundle);

        //bundle还可以在每个标签里传送数据


        return newFragment;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        travelMainView = inflater.inflate(R.layout.find_item, container, false);
        share = (LinearLayout) travelMainView.findViewById(R.id.ll_iteam1);
        capture = (LinearLayout) travelMainView.findViewById(R.id.ll_iteam2);
        app_top_bar = (RelativeLayout) travelMainView.findViewById(R.id.app_top_bar);
        app_top_bar.setBackgroundColor(getResources().getColor(R.color.yym_head_space));//标题栏的背景颜色

        //点击事件
        doItem();
        return travelMainView;
    }

    private void doItem() {
        share.setOnClickListener(this);
        capture.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.ll_iteam1:
                if(VisitorMode.isVistor(getActivity())){//用来判断用户是否登录
                    return;
                }
                intent = new Intent(getActivity(), ShareOrderActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_iteam2:
                if(VisitorMode.isVistor(getActivity())){
                    return;
                }
                intent = new Intent(getActivity(), OneCaptureActivity.class);
                startActivity(intent);
                break;
        }

    }
}

