package com.shengtao.snache.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shengtao.R;
import com.shengtao.utils.HackyViewPager;
import com.shengtao.utils.LYDBHeader;

import java.util.HashMap;

/**
 * @copyright：Copyright © 2015 - 2020. All Rights Reserved
 * @author：dane55
 * @email：dane8789@gmail.com
 * @date： 15/9/13 11:02
 * @tilte：
 * @description：图片浏览器
 */
public class AtyImagePagerActivity extends FragmentActivity {
    private static final String STATE_POSITION = "STATE_POSITION";
    public static final String EXTRA_IMAGE_INDEX = "image_index";
    public static final String EXTRA_IMAGE_URLS = "image_urls";
    public static final String EXTRA_IMAGE_CONTEXT="image_context";

    private HackyViewPager mPager;
    private int pagerPosition;
    private TextView indicator;
    private TextView tv_desc;
    private RelativeLayout ll_desc_bg;
    private LYDBHeader LYDBHeader;
    private String ImageContext=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_image_detail_pager);
//        this.initHeader();
        pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
        String[] urls = getIntent().getStringArrayExtra(EXTRA_IMAGE_URLS);
        ImageContext=getIntent().getStringExtra(EXTRA_IMAGE_CONTEXT);

        mPager = (HackyViewPager) findViewById(R.id.pager);
        ImagePagerAdapter mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), urls);
        mPager.setAdapter(mAdapter);
        indicator = (TextView) findViewById(R.id.indicator);
        tv_desc = (TextView) findViewById(R.id.tv_desc);

        ll_desc_bg=(RelativeLayout) findViewById(R.id.ll_add);
        if(ImageContext==null||ImageContext.equals("")||ImageContext.equals("null")){
            ll_desc_bg.setVisibility(View.GONE);
            tv_desc.setVisibility(View.GONE);
        }else {
            ll_desc_bg.setVisibility(View.GONE);
            tv_desc.setVisibility(View.GONE);
            tv_desc.setText(ImageContext);
        }
        CharSequence text = getString(R.string.viewpager_indicator, 1, mPager
                .getAdapter().getCount());
        indicator.setText(text);
        // 更新下标
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
            @Override
            public void onPageSelected(int arg0) {
                CharSequence text = getString(R.string.viewpager_indicator,
                        arg0 + 1, mPager.getAdapter().getCount());
                indicator.setText(text);
                if(ImageContext==null){
                    ll_desc_bg.setVisibility(View.GONE);
                    tv_desc.setVisibility(View.GONE);
                }else {
                    ll_desc_bg.setVisibility(View.VISIBLE);
                    tv_desc.setVisibility(View.VISIBLE);
                    tv_desc.setText(ImageContext);
                }
            }

        });
        if (savedInstanceState != null) {
            pagerPosition = savedInstanceState.getInt(STATE_POSITION);
        }

        mPager.setCurrentItem(pagerPosition);
    }
    private void initHeader() {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("left", "left");
        hashMap.put("center", "相片");
        LYDBHeader = (LYDBHeader) findViewById(R.id.header);
        LYDBHeader.setHeaderView(this, hashMap);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_POSITION, mPager.getCurrentItem());
    }

    private class ImagePagerAdapter extends FragmentStatePagerAdapter {

        public String[] fileList;

        public ImagePagerAdapter(FragmentManager fm, String[] fileList) {
            super(fm);
            this.fileList = fileList;
        }

        @Override
        public int getCount() {
            return fileList == null ? 0 : fileList.length;
        }

        @Override
        public Fragment getItem(int position) {
            String url = fileList[position];
            return AtyImageDetailFragment.newInstance(url);
        }

    }
}
