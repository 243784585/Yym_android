package com.shengtao.domain.snache;

import com.shengtao.domain.firstPage.ListgoodsEntity;
import com.shengtao.foundation.BaseEnitity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @package com.shengtao.domain.snache
 * Created by TT on 2015/12/29.
 * Description:热门商品
 */
public class HotGoods extends BaseEnitity {
    private int code;
    private String error;
    private String attributes;
    private List<ListgoodsEntity> hotGoodList = new ArrayList<>();

    public HotGoods(JSONObject jsonObject) {
        if ("0".equals(jsonObject.optString("code"))) {

            JSONArray jsonArray = jsonObject.optJSONArray("info");
            error = jsonObject.optString("error");
            attributes = jsonObject.optString("attributes");
            if (jsonArray != null) {
                //如果解析为空
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.optJSONObject(i);
                    hotGoodList.add(new ListgoodsEntity(json));
                }
            }
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public List<ListgoodsEntity> getHotGoodList() {
        return hotGoodList;
    }

    public void setHotGoodList(List<ListgoodsEntity> hotGoodList) {
        this.hotGoodList = hotGoodList;
    }
}
