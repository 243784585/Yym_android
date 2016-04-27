package com.shengtao.domain.mine;

import com.shengtao.foundation.BaseEnitity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @package com.shengtao.domain.mine
 * Created by TT on 2015/12/24.
 * Description:
 */
public class PersonDState extends BaseEnitity {
    private int code;
    private String error;
    private String attributes;
    private ArrayList<PersonDStateInfo> personDStateList = new ArrayList<>();
    private  JSONArray jsonArray;
    private  String head_url;
    private  String name;
    private  String userid;

    public PersonDState(JSONObject jsonObject) {
        if("0".equals(jsonObject.optString("code"))) {
            try {
                JSONObject info = jsonObject.optJSONObject("info");
                if(info != null) {
                    head_url = info.optString("head_url");
                    name = info.optString("name");
                    userid = info.optString("userid");
                    error = jsonObject.optString("error");
                    attributes = jsonObject.optString("attributes");
                    jsonArray = info.optJSONArray("list");
                }else{
                    jsonArray = jsonObject.optJSONArray("info");
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.optJSONObject(i);
                    personDStateList.add(new PersonDStateInfo(json));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public JSONArray getJsonArray() {
        return jsonArray;
    }

    public void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    public String getHead_url() {
        return head_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserid() {
        return userid;
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

    public ArrayList<PersonDStateInfo> getPersonDStateList() {
        return personDStateList;
    }

}
