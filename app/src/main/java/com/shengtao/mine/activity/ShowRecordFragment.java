package com.shengtao.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengtao.R;
import com.shengtao.discover.activity.ShareOrderItemActivity;
import com.shengtao.domain.Config;
import com.shengtao.domain.discover.AllShareDetail;
import com.shengtao.domain.mine.PersonShow;
import com.shengtao.domain.mine.PersonShowInfo;
import com.shengtao.foundation.AsyncHttpTask;
import com.shengtao.foundation.BaseFragment;
import com.shengtao.foundation.IToastBlock;
import com.shengtao.snache.activity.AtyImagePagerActivity;
import com.shengtao.utils.DateTimeUtil;
import com.shengtao.utils.LogUtil;
import com.shengtao.utils.MyGridView;
import com.shengtao.utils.ToastUtil;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @package com.shengtao.mine.activity
 * Created by TT on 2015/12/17.
 * Description:晒单记录
 */
public class ShowRecordFragment extends BaseFragment {

    private View view;
    private GridView gvShow;
    private PullToRefreshListView lv_show_record;
    private ArrayList<PersonShowInfo> pList;
    private MsgWinningAdapter msgWinningAdapter;
    private String id;
    private boolean isSnach = false;

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
        Bundle bundle = getArguments();
        pList = (ArrayList<PersonShowInfo>) bundle.getSerializable("pList");
        id = bundle.getString("id");
        isSnach = bundle.getBoolean("snach",false);
        //获取动态的资源id
        lv_show_record = (PullToRefreshListView) view.findViewById(R.id.lv_show_record);
        initRefresh();
        inflater(view);
    }
    public void inflater(View view){
        msgWinningAdapter = new MsgWinningAdapter();
        lv_show_record.setAdapter(msgWinningAdapter);
        lv_show_record.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                PersonShowInfo item = msgWinningAdapter.getItem(position - 1);
                Intent intent = new Intent(getActivity(), ShareOrderItemActivity.class);
                AllShareDetail sharedetial = new AllShareDetail();
                sharedetial.setShare_title(item.getShare_title());
                sharedetial.setShare_time(item.getShare_time());
                sharedetial.setShare_content(item.getShare_content());
                sharedetial.setOpen_time(item.getOpen_time());
                sharedetial.setAll_join_num(item.getAll_join_num());
                sharedetial.setClient_name(item.getClient_name());
                sharedetial.setGoods_current_num(item.getGoods_current_num());
                sharedetial.setGoods_name(item.getGoods_name());
                sharedetial.setHead_img(item.getHead_img());
                sharedetial.setLucky_id(item.getLucky_id());
                sharedetial.setUserId(item.getId());
                sharedetial.setGROUP_CONCAT(item.getAllImgUrl());
                intent.putExtra("mList", sharedetial);
                startActivity(intent);
            }
        });
    }

    /**
     * 初始化上下拉刷新操作
     */
    public void initRefresh(){
        lv_show_record.setMode(PullToRefreshBase.Mode.BOTH);
        lv_show_record.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            int currentPage = 1;
            RequestParams params = new RequestParams();
            String url = !isSnach?Config.HTTP_URL_HEAD + "goods/otherShareLog":Config.HTTP_URL_HEAD + "user/myShareLog";

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                currentPage = 1;
                if (!isSnach) {
                    params.put("dbAppClientid", id);
                }
                params.put("pageNum", currentPage);
                AsyncHttpTask.post(url, params, new AsyncHttpResponseHandler() {
                    //加载成功
                    @Override
                    public void onSuccess(int statusCode, Header[] motuHeaders, byte[] bytes) {
                        try {
                            pList.clear();
                            String data = new String(bytes, super.getCharset());
                            LogUtil.d(data);
                            showUIData(data);
                            lv_show_record.onRefreshComplete();
                            msgWinningAdapter.notifyDataSetChanged();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }

                    //获取数据失败
                    @Override
                    public void onFailure(int statusCode, Header[] motuHeaders, byte[] bytes, Throwable throwable) {
                        lv_show_record.onRefreshComplete();
                        return;
                    }
                });
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                ++currentPage;
                if (!isSnach) {
                    params.put("dbAppClientid", id);
                }
                params.put("pageNum", currentPage);
                AsyncHttpTask.post(url, params, new AsyncHttpResponseHandler() {
                    //加载成功
                    @Override
                    public void onSuccess(int statusCode, Header[] motuHeaders, byte[] bytes) {
                        try {
                            String data = new String(bytes, super.getCharset());
                            LogUtil.d(data);
                            showUIData(data);
                            lv_show_record.onRefreshComplete();
                            msgWinningAdapter.notifyDataSetChanged();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }

                    //获取数据失败
                    @Override
                    public void onFailure(int statusCode, Header[] motuHeaders, byte[] bytes, Throwable throwable) {
                        lv_show_record.onRefreshComplete();
                        return;
                    }
                });
            }
        });
    }

    protected void showUIData(Object obj) {
        //解析方法
        String result = obj.toString();
        LogUtil.e("log", result);
        if (TextUtils.isEmpty(result))
            return;
        JSONObject json = null;
        ArrayList<PersonShowInfo> results;
        try {
            json = new JSONObject(result);
            PersonShow personShow = new PersonShow(json);
//                pList = new ArrayList<>();
            results = personShow.getPersonShowList();

            if(results.size()>0){
                pList.addAll(results);
            }else{
                ToastUtil.showTextToast("没有更多了");
            }
            msgWinningAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String[] convertUrls(List<HashMap<String, String>> urls) {
        String[] urlArray = new String[urls.size()];
        for (int i = 0; i < urls.size(); i++) {
            urlArray[i] = urls.get(i).get("image");
        }
        return urlArray;
    }

    /**
     * 图片浏览器
     *
     * @param position
     * @param urls
     */
    private void imageBrower(int position, String[] urls) {
        Intent intent = new Intent(getActivity(), AtyImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(AtyImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(AtyImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        getActivity().startActivity(intent);
    }

    @Override
    public IToastBlock getToastBlock() {
        return null;
    }

    class MsgWinningAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return pList.size();
        }

        @Override
        public PersonShowInfo getItem(int position) {
            return pList.get(position);
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
            PersonShowInfo personShowInfo =  getItem(position);
            viewHolder.tv_goods_name.setText(personShowInfo.getGoods_name());
            viewHolder.tv_share_title.setText(personShowInfo.getShare_title());
            viewHolder.tv_share_content.setText(personShowInfo.getShare_content());
            viewHolder.show_prized_time.setText(DateTimeUtil.timestamp2Time(Long.parseLong(personShowInfo.getShare_time())/1000+""));

            String allImgUrl = personShowInfo.getAllImgUrl();
            String[] allImg = allImgUrl.split(",");
            final List<HashMap<String, String>> list = new ArrayList<>();
            if (allImg[0] != null) {
                for (int i = 0; i < allImg.length; i++) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("image", allImg[i]);//"http://pic.nipic.com/2007-11-09/2007119122519868_2.jpg"
                    list.add(hashMap);
                }
            }
            if(list.size() == 1){
                viewHolder.image_single.setVisibility(View.VISIBLE);
                viewHolder.gv_show.setVisibility(View.GONE);
                String urlPath = list.get(0).get("image");
                if(!urlPath.contains("imageView2")){
                    urlPath = urlPath + "?imageViw2/2/w/320";
                }
                ImageLoader.getInstance().displayImage(urlPath,viewHolder.image_single);
                viewHolder.image_single.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String[] url = convertUrls(list);
                        imageBrower(0, url);
                    }
                });
            }else {
                viewHolder.image_single.setVisibility(View.GONE);
                viewHolder.gv_show.setVisibility(View.VISIBLE);
                //GridView SimpleAdapter
                SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), list, R.layout.item_gridview_image, new String[]{"image"}, new int[]{R.id.iv_image}) {
                    @Override
                    public void setViewImage(ImageView v, String value) {
                        super.setViewImage(v, value);
                        if(!value.contains("imageView2")){
                            value = value + "?imageViw2/2/w/200";
                        }
                        ImageLoader.getInstance().displayImage(value, v);
                    }
                };

                final String[] urls = convertUrls(list);
                viewHolder.gv_show.setAdapter(simpleAdapter);
                //设置点击gradview空白处不消费事件
                viewHolder.gv_show.setOnTouchInvalidPositionListener(new MyGridView.OnTouchInvalidPositionListener() {
                    @Override
                    public boolean onTouchInvalidPosition(int motionEvent) {
                        return false;
                    }
                });
                viewHolder.gv_show.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //弹出图片预览集
                        imageBrower(position, urls);
                    }
                });
            }

            return convertView;
        }
    }

    public class ViewHolder{
        private TextView tv_share_title;
        private TextView tv_goods_name;
        private TextView tv_share_content;
        private MyGridView gv_show;
        private ImageView image_single;
        private TextView show_prized_time;


        public ViewHolder(View view){
            tv_share_title = (TextView) view.findViewById(R.id.tv_share_title);
            tv_goods_name = (TextView) view.findViewById(R.id.tv_goods_name);
            tv_share_content = (TextView) view.findViewById(R.id.tv_share_content);
            gv_show = (MyGridView) view.findViewById(R.id.gv_show);
            image_single = (ImageView) view.findViewById(R.id.image_single);
            show_prized_time = (TextView) view.findViewById(R.id.show_prized_time);
        }
    }
}
