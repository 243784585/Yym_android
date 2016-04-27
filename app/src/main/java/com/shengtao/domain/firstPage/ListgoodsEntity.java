package com.shengtao.domain.firstPage;

import com.shengtao.foundation.BaseEnitity;

import org.json.JSONObject;

/**
 * @package com.shengtao.domain.firstPage
 * Created by TT on 2016/2/1.
 * Description:首页热门商品
 */
public class ListgoodsEntity extends BaseEnitity{
        private String goodsId;
        private String goods_10;
        private String goods_name;
        private String goods_headurl;
        private String current_join_num;
        private String all_join_num;

    public ListgoodsEntity(JSONObject jsonObject) {
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

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoods_10() {
        return goods_10;
    }

    public void setGoods_10(String goods_10) {
        this.goods_10 = goods_10;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_headurl() {
        return goods_headurl;
    }

    public void setGoods_headurl(String goods_headurl) {
        this.goods_headurl = goods_headurl;
    }

    public String getCurrent_join_num() {
        return current_join_num;
    }

    public void setCurrent_join_num(String current_join_num) {
        this.current_join_num = current_join_num;
    }

    public String getAll_join_num() {
        return all_join_num;
    }

    public void setAll_join_num(String all_join_num) {
        this.all_join_num = all_join_num;
    }
}
