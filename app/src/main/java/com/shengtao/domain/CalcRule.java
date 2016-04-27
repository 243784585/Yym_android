package com.shengtao.domain;

import com.shengtao.domain.snache.CalcRuleDetail;
import com.shengtao.foundation.BaseEnitity;
import com.shengtao.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @package com.shengtao.domain
 * Created by TT on 2015/12/24.
 * Description:计算规则
 */
public class CalcRule extends BaseEnitity {

    private int code;
    private String error;
    private String attributes;
    private String avalue;
    private String bvalue;
    private String joinLog;
    private String luckyId;
    private List<CalcRuleDetail> calcList = new ArrayList<>();

    public CalcRule(JSONObject jsonObject) {
        try {
            if ("0".equals(jsonObject.getString("code"))) {

                JSONObject json = jsonObject.optJSONObject("info");
                error = jsonObject.optString("error");
                attributes = jsonObject.optString("attributes");
                avalue = json.optString("Avalue");
                bvalue = json.optString("Bvalue");
                JSONArray joinLog = json.optJSONArray("joinLog");
                if (joinLog != null) {
                    //如果解析为空
                    for (int i = 0; i < joinLog.length(); i++) {
                        JSONObject jsonobj = joinLog.optJSONObject(i);
                        calcList.add(new CalcRuleDetail(jsonobj));
                    }
                }
                luckyId = json.optString("LuckyId");
            }else{
                ToastUtil.showTextToast(error);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<CalcRuleDetail> getCalcList() {
        return calcList;
    }

    public void setCalcList(List<CalcRuleDetail> calcList) {
        this.calcList = calcList;
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

    public String getAvalue() {
        return avalue;
    }

    public void setAvalue(String avalue) {
        this.avalue = avalue;
    }

    public String getBvalue() {
        return bvalue;
    }

    public void setBvalue(String bvalue) {
        this.bvalue = bvalue;
    }

    public String getJoinLog() {
        return joinLog;
    }

    public void setJoinLog(String joinLog) {
        this.joinLog = joinLog;
    }

    public String getLuckyId() {
        return luckyId;
    }

    public void setLuckyId(String luckyId) {
        this.luckyId = luckyId;
    }
}
