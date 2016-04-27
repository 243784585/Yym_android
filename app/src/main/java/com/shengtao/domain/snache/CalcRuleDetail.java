package com.shengtao.domain.snache;

import com.shengtao.foundation.BaseEnitity;

import org.json.JSONObject;

/**
 * @package com.shengtao.domain
 * Created by TT on 2015/12/24.
 * Description:计算规则
 */
public class CalcRuleDetail extends BaseEnitity {


    private String join_time;
    private String client_name;
    private String joinvalue;

    public CalcRuleDetail(JSONObject jsonObject) {
        try {
            join_time = jsonObject.optString("join_time");
            client_name = jsonObject.optString("client_name");
            joinvalue =  "→" +jsonObject.optString("joinvalue");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getJoin_time() {
        return join_time;
    }

    public void setJoin_time(String join_time) {
        this.join_time = join_time;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getJoinvalue() {
        return joinvalue;
    }
}
