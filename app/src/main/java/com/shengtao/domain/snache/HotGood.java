package com.shengtao.domain.snache;

import com.shengtao.foundation.BaseEnitity;

import org.json.JSONObject;

/**
 * @package com.shengtao.domain.snache
 * Created by TT on 2015/12/29.
 * Description:
 */
public class HotGood extends BaseEnitity {


    private final String goodsId;
//    private final String hjoinNum;
    private final String goods_10;
    private final String goods_name;
    private final String goods_headurl;
    private final String current_join_num;
    private final String all_join_num;

    public HotGood(JSONObject jsonObject) {
        goodsId = jsonObject.optString("goodsId");
//        hjoinNum = jsonObject.optString("24HjoinNum");
        goods_10 = jsonObject.optString("goods_10");
        goods_name = jsonObject.optString("goods_name");
        goods_headurl = jsonObject.optString("goods_headurl");
        current_join_num = jsonObject.optString("current_join_num");
        all_join_num = jsonObject.optString("all_join_num");
    }

    public String getGoodsId() {
        return goodsId;
    }

//    public String getHjoinNum() {
//        return hjoinNum;
//    }

    public String getGoods_10() {
        return goods_10;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public String getGoods_headurl() {
        return goods_headurl;
    }

    public String getCurrent_join_num() {
        return current_join_num;
    }

    public String getAll_join_num() {
        return all_join_num;
    }
}
