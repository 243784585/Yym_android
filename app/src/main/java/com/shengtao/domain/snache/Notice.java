package com.shengtao.domain.snache;

import com.shengtao.foundation.BaseEnitity;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by TT on 2015/12/12.
 * explain:公告的实体类
 */
public class Notice extends BaseEnitity{
    private String summary;
    private String topic;
    private String time;
    private int img;
    private int havemessage;//有消息提醒

    public Notice(JSONObject jsonObject) {
        if ("0".equals(jsonObject.optString("code"))) {
            JSONObject info = jsonObject.optJSONObject("info");
            summary = info.optString("summary");
            topic = info.optString("topic");
            time = info.optString("time");
        }
    }

    public int getHavemessage() {
        return havemessage;
    }

    public void setHavemessage(int havemessage) {
        this.havemessage = havemessage;
    }

    public Notice(){}

    public String getSummary() {
        return summary;
    }

    public String getTopic() {
        return topic;
    }

    public String getTime() {
        return time;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
