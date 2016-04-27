package com.shengtao.snache.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shengtao.R;
import com.shengtao.baseview.BaseActivity;
import com.shengtao.domain.SortSkim;
import com.shengtao.utils.LogUtil;

import java.util.ArrayList;

/**
 * @package com.baixi.snache.activity
 * Created by TT on 2015/12/19.
 * Description:搜索界面
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener {

    private GridView gvSearch;
    private ArrayList<SortSkim> sList;
    private int[] mImgList = {
            R.drawable.quanbushangpin, R.drawable.shiyuanzhuanqu, R.drawable.fuzhuang, R.drawable.xiebao
            , R.drawable.peishi, R.drawable.yundong, R.drawable.zhubao, R.drawable.shuma
            , R.drawable.jiadian, R.drawable.meizhuang, R.drawable.muying, R.drawable.jiaju
            , R.drawable.shipin, R.drawable.baihuo, R.drawable.qiche, R.drawable.wenyu
            , R.drawable.bendi, R.drawable.xuni};

    private String[] mNameList = {
            "全部商品", "十元专区", "服装", "鞋包",
            "配饰", "运动", "珠宝", "数码",
            "家电", "美妆", "母婴", "家居",
            "食品", "百货", "汽车", "文娱",
            "本地", "虚拟"
    };
    private ImageView ivBack;
    private Button btnSearch;
    private EditText et_search;
    private String searchName;
    private SearchAdapter searchAdapter;
    private Bundle b;
    private InputMethodManager imm;


    @Override
    protected int setLayout() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {
        //隐藏键盘
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);


        gvSearch = (GridView) findViewById(R.id.gv_search);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        btnSearch = (Button) findViewById(R.id.btn_search);
        et_search = (EditText) findViewById(R.id.et_search);

        //创建文字和图片的对象，并放到集合中
        sList = new ArrayList<>();
        for (int i = 0; i < mImgList.length; i++) {
            SortSkim ss = new SortSkim();
            ss.setName(mNameList[i]);
            ss.setImg(mImgList[i]);
            sList.add(ss);
        }
        searchAdapter = new SearchAdapter();
        gvSearch.setAdapter(searchAdapter);
        gvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    SortSkim item = searchAdapter.getItem(position);
                    Intent intent = new Intent(SearchActivity.this, AllGoodsActivity.class);
                    switch (position) {
                        case 0:
                            intent.putExtra("isten","2");
                            LogUtil.e("single", "2");
                            intent.putExtra("title",item.getName());
                            startActivity(intent);
                            break;
                        case 1:
                            intent.putExtra("isten","1");
                            LogUtil.e("single", "1");
                            intent.putExtra("title",item.getName());
                            startActivity(intent);
                            break;
                        default:
                            intent.putExtra("isten","0");
                            LogUtil.e("single", "2");
                            intent.putExtra("title",item.getName());
                            startActivity(intent);
                            break;
                    }
                }
            }
        );

        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int keyCode, KeyEvent keyEvent) {

                if (keyCode == EditorInfo.IME_ACTION_SEARCH) {//修改回车键功能
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(
                                    SearchActivity.this
                                            .getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    //跳转到搜索结果界面
                    Intent intent = new Intent(SearchActivity.this,
                            SearchResultActivity.class);
                    intent.putExtra("url", "goods/SearchByName");
                    intent.putExtra("name", "".equals(et_search.getText().toString()) ? "" : et_search.getText().toString());
                    startActivity(intent);
                }
                return false;
            }
        });
    }

    @Override
    protected void doBusiness() {
        ivBack.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_search:
//                searchName = et_search.getText().toString().trim();
//                Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
//                intent.putExtra("url", "goods/SearchByName");
//                intent.putExtra("name", searchName);
//                startActivity(intent);
                et_search.setText("");
                imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
                break;
        }
    }

    class SearchAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return sList.size();
        }

        @Override
        public SortSkim getItem(int position) {
            return sList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(), R.layout.item_search, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final SortSkim ss = getItem(position);
            viewHolder.ivImg.setImageResource(ss.getImg());
            viewHolder.tvName.setText(ss.getName());
            return convertView;
        }
    }

    public class ViewHolder {
        private ImageView ivImg;
        private TextView tvName;
        private LinearLayout ll_img;

        public ViewHolder(View view) {
            ivImg = (ImageView) view.findViewById(R.id.iv_img);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            ll_img = (LinearLayout) view.findViewById(R.id.ll_img);
        }
    }
}
