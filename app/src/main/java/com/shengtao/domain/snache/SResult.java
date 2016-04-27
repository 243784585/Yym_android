package com.shengtao.domain.snache;

import com.shengtao.foundation.BaseEnitity;

import org.json.JSONObject;

/**
 * @package com.shengtao.domain.snache
 * Created by TT on 2015/12/27.
 * Description:
 */
public class SResult extends BaseEnitity {

    private  String goods_headurl;
    private  String goods_10;
    private  String goods_name;
    private  String goods_current_num;
    private  String all_join_num;
    private  String current_join_num;
    private  String id;

    public SResult(JSONObject jsonObject) {
        goods_headurl = jsonObject.optString("goods_headurl");
        goods_10 = jsonObject.optString("goods_10");
        goods_name = jsonObject.optString("goods_name");
        goods_current_num = jsonObject.optString("goods_current_num");
        all_join_num = jsonObject.optString("all_join_num");
        current_join_num = jsonObject.optString("current_join_num");
        id = jsonObject.optString("id");
    }

    public String getGoods_headurl() {
        return goods_headurl;
    }

    public String getGoods_10() {
        return goods_10;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public String getGoods_current_num() {
        return goods_current_num;
    }

    public String getAll_join_num() {
        return all_join_num;
    }

    public String getCurrent_join_num() {
        return current_join_num;
    }

    public String getId() {
        return id;
    }
}
