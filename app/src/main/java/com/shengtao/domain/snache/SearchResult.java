package com.shengtao.domain.snache;

import com.shengtao.foundation.BaseEnitity;
import com.shengtao.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @package com.shengtao.domain.snache
 * Created by TT on 2015/12/27.
 * Description:搜素结果
 */
public class SearchResult extends BaseEnitity {

    private int code;
    private String error;
    private String attributes;
    private List<SResult> searchList = new ArrayList<>();

    public SearchResult(JSONObject jsonObject) {
        try {
            if ("0".equals(jsonObject.getString("code"))) {

                JSONArray jsonArray = jsonObject.optJSONArray("info");
                error = jsonObject.optString("error");
                attributes = jsonObject.optString("attributes");
                int j = jsonArray.length();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.optJSONObject(i);
                    searchList.add(new SResult(json));
                }
            }else{
                error = jsonObject.optString("error");
                ToastUtil.showTextToast(error);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public List<SResult> getSearchList() {
        return searchList;
    }

    public void setSearchList(List<SResult> searchList) {
        this.searchList = searchList;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
