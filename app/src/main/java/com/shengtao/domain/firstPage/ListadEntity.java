package com.shengtao.domain.firstPage;

import com.shengtao.foundation.BaseEnitity;

import org.json.JSONObject;

/**
 * @package com.shengtao.domain.firstPage
 * Created by TT on 2016/2/1.
 * Description:首页广告
 */
public class ListadEntity extends BaseEnitity {

    private String img_url;
    private String id;

    public ListadEntity(JSONObject jsonObject) {
        img_url = jsonObject.optString("img_url");
        id = jsonObject.optString("id");
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
