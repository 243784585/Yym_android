package com.shengtao.domain.shopping;

import com.shengtao.foundation.BaseEnitity;

/**
 * Created by Scoot on 2016/1/2.
 *
 * 购物车item实体类封装
 */
public class ShoppingGoods extends BaseEnitity {
    private  String image;
    private String goodsTittle;
    private boolean ischecked ;//判读是否选中


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGoodsTittle() {
        return goodsTittle;
    }

    public void setGoodsTittle(String goodsTittle) {
        this.goodsTittle = goodsTittle;
    }

    public boolean ischecked() {
        return ischecked;
    }

    public void setIschecked(boolean ischecked) {
        this.ischecked = ischecked;
    }

    public boolean getIschecked() {
        return ischecked;
    }
}
