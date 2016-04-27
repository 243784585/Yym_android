package com.shengtao.domain;

import com.shengtao.foundation.BaseEnitity;

/**
 * @package com.baixi.domain
 * Created by TT on 2015/12/19.
 * Description:分类浏览的对象
 */
public class SortSkim extends BaseEnitity {

    private String name;
    private int img;

    public SortSkim(String name, int img) {
        this.name = name;
        this.img = img;
    }

    public SortSkim() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
