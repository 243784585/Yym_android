package com.shengtao.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Xml;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.shengtao.R;
import com.shengtao.baseview.BaseActivity;
import com.shengtao.mine.entity.City;
import com.shengtao.mine.entity.District;
import com.shengtao.mine.entity.Province;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hxhuang on 2015/12/22 0022.
 */
public class RegionActivity extends BaseActivity implements AdapterView.OnItemClickListener{

    private ListView lvRegion;

    private String province;
    private String city;
    private String district;

    private String curPosition;     //当前选择地区的定位
    private int selectProvince;     //选中的省的索引
    private int selectCity;         //选中城市的索引

    private List<Province> allRegion = new ArrayList<Province>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViewById(R.id.header_left).setOnClickListener(this);
    }

    protected void initView() {
        lvRegion = (ListView) findViewById(R.id.lv_region);
        initData();
        curPosition = "province";
        notifyChange();
        lvRegion.setOnItemClickListener(this);
    }

    @Override
    protected void doBusiness() {

    }

    protected void initData() {
        InputStream region = null;
        try {
            region = getAssets().open("province_data.xml");
            XmlPullParser xp = Xml.newPullParser();
            xp.setInput(region,"utf-8");
            int type = xp.getEventType();
            Province curProvince = null;
            City curCity = null;
            while(type != XmlPullParser.END_DOCUMENT){
                if(type == XmlPullParser.START_TAG) {
                    switch (xp.getName()) {
                        case "province":
                            curProvince = new Province(xp.getAttributeValue(null, "name"));
                            break;
                        case "city":
                            curCity = new City(xp.getAttributeValue(null, "name"));
                            curProvince.getCityList().add(curCity);
                            break;
                        case "district":
                            curCity.getDistrictList().add(new District(xp.getAttributeValue(null, "name")));
                            break;
                    }
                }else if(type == XmlPullParser.END_TAG && "province".equals(xp.getName())){
                    allRegion.add(curProvince);
                }

                type = xp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(region != null){
                try {
                    region.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected String getAvtionTitle() {
        return "选择地区";
    }

    @Override
    public int getHeaderType() {
        return 1;
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_region;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if("province".equals(curPosition)){
            curPosition = "city";
            selectProvince = i;
            province = allRegion.get(i).getProvice();
            notifyChange();
//            lvRegion.setAdapter(new myAdapter());
        }else if("city".equals(curPosition)){
            curPosition = "district";
            selectCity = i;
            city = allRegion.get(selectProvince).getCityList().get(i).getCity();
            notifyChange();
//            lvRegion.setAdapter(new myAdapter());
        }else{
            district = allRegion.get(selectProvince).getCityList().get(selectCity).getDistrictList().get(i).getDistrict();
            Intent intent = new Intent();
            intent.putExtra("region",province + " " + city + " " + district);
            setResult(120, intent);
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        if(R.id.header_left == view.getId()){
            onBackPressed();
        }
    }

    private void notifyChange(){
        lvRegion.setAdapter(new myAdapter());
    }

    @Override
    public void onBackPressed() {
        //返回按钮
        switch (curPosition){
            case "province":
                //省
                finish();
                break;
            case "city":
                //市
                curPosition = "province";
                notifyChange();
                break;
            case "district":
                //区
                curPosition = "city";
                notifyChange();
                break;
        }
    }

    class myAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return "province".equals(curPosition) ? allRegion.size() :
                    "city".equals(curPosition) ? allRegion.get(selectProvince).getCityList().size() :
                            allRegion.get(selectProvince).getCityList().get(selectCity).getDistrictList().size();
        }

        @Override
        public Object getItem(int i) {
            return "province".equals(curPosition) ? allRegion.get(i) :
                    "city".equals(curPosition) ? allRegion.get(selectProvince).getCityList().get(i) :
                            allRegion.get(selectProvince).getCityList().get(selectCity).getDistrictList().get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            TextView tv = new TextView(RegionActivity.this);


            tv.setText(
                    "province".equals(curPosition) ? allRegion.get(i).getProvice() :
                            "city".equals(curPosition) ? allRegion.get(selectProvince).getCityList().get(i).getCity() :
                                    allRegion.get(selectProvince).getCityList().get(selectCity).getDistrictList().get(i).getDistrict()
            );


            tv.setWidth(554);
            tv.setHeight(70);
            tv.setGravity(Gravity.CENTER_VERTICAL);
            return tv;
        }
    }
}
