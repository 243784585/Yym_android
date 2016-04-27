package com.shengtao.domain.mine;

import com.shengtao.domain.snache.OpenTimeComparator;
import com.shengtao.foundation.BaseEnitity;

import org.json.JSONObject;

/**
 * @package com.shengtao.domain.mine
 * Created by TT on 2016/3/15.
 * Description:我的优惠劵
 */
public class MyCoupon extends BaseEnitity implements OpenTimeComparator {
    /**
     * time : 1457063477000
     * coupon_value : 4
     * coupon_url : www.hupu.com
     * business_name : 星罗之家
     * isnew : 0
     * id : 59
     * ctime : 1457785158000
     */

    public String time;
    public int coupon_value;
    public String coupon_url;
    public String business_name;
    public int isnew;
    public int id;
    public long ctime;
    public boolean isZero = false;

    public MyCoupon(JSONObject jsonObject) {
        time = jsonObject.optString("time");
        coupon_value = jsonObject.optInt("coupon_value");
        coupon_url = jsonObject.optString("coupon_url");
        business_name = jsonObject.optString("business_name");
        isnew = jsonObject.optInt("isnew");
        id = jsonObject.optInt("id");
        ctime = jsonObject.optLong("ctime");
    }


    @Override
    public String getOpen_time() {
        return ctime+"";
    }
}
