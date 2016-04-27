package com.shengtao.domain.snache;

import com.shengtao.foundation.BaseEnitity;

/**
 * @package com.shengtao.domain.snache
 * Created by TT on 2016/1/18.
 * Description:广告
 */
public class Ad extends BaseEnitity{

    private String goods_headurl;
    private String id;

    public Ad() {
    }

    public String getGoods_headurl() {
        return goods_headurl;
    }

    public void setGoods_headurl(String goods_headurl) {
        this.goods_headurl = goods_headurl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
