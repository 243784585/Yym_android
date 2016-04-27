package com.shengtao.domain.firstPage;

import com.shengtao.foundation.BaseEnitity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @package com.shengtao.domain
 * Created by TT on 2016/2/1.
 * Description:首页domain
 */
public class FirstPage extends BaseEnitity{

    /**
     * token : null
     * code : 0
     * info : {"listad":[{"img_url":"http://7xp1b8.com1.z0.glb.clouddn.com/40288808528617e501528631dc2c0007","id":"ff808081529adb8701529b0212950014"},{"img_url":"http://7xp1b8.com1.z0.glb.clouddn.com/40288808528617e50152862449cf0005","id":"ff808081529adb8701529b03a8020019"},{"img_url":"http://7xp1b8.com1.z0.glb.clouddn.com/40288808528617e50152861d94d00003","id":"ff808081529adb8701529b020e190013"}],"time":1454308164169,"listgoods":[{"goodsId":"ff808081529adb8701529b7e1338003a","goods_10":1,"goods_name":"100","goods_headurl":"http://7xp1b8.com1.z0.glb.clouddn.com/83286bc3-bbb3-46dd-bffc-933dc75e2bd1","current_join_num":0,"all_join_num":100},{"goodsId":"ff808081529adb8701529b081a500024","goods_10":1,"goods_name":"300","goods_headurl":"http://7xp1b8.com1.z0.glb.clouddn.com/c39fa97d-2dbe-40a4-9cda-f5c110e2220e","current_join_num":10,"all_join_num":100},{"goodsId":"ff808081529adb8701529b8397be003c","goods_10":0,"goods_name":"果时代蓝牙音箱手机低音炮便携小音响","goods_headurl":"http://7xp1b8.com1.z0.glb.clouddn.com/08238a16-7a1e-425c-b0c3-89e0a4f8b436","current_join_num":0,"all_join_num":80},{"goodsId":"ff808081529adb8701529b7b940f0039","goods_10":1,"goods_name":"200","goods_headurl":"http://7xp1b8.com1.z0.glb.clouddn.com/84803233-d72a-4b45-9df9-74f0283bd850","current_join_num":0,"all_join_num":60},{"goodsId":"ff808081529adb8701529b073c600022","goods_10":0,"goods_name":"极速达新款道奇跑车摩托车战斧跑车概念跑车超酷摩托","goods_headurl":"http://7xp1b8.com1.z0.glb.clouddn.com/6a02b628-f0e5-484e-bf22-adcecfbad9b3","current_join_num":0,"all_join_num":100},{"goodsId":"ff808081529adb8701529b03aaf8001a","goods_10":1,"goods_name":"川行者太阳能登山包背包太阳能持续电源户外移动充电","goods_headurl":"http://7xp1b8.com1.z0.glb.clouddn.com/d05ca333-cb0d-4393-813c-13428311acf6","current_join_num":0,"all_join_num":900},{"goodsId":"ff808081529adb8701529b020e190013","goods_10":0,"goods_name":"雷柏RapooV56混光机械段落感游戏键盘黑色","goods_headurl":"http://7xp1b8.com1.z0.glb.clouddn.com/0b380c80-a9c7-466d-90e2-7f12b8aeb5c9","current_join_num":40,"all_join_num":100},{"goodsId":"ff808081529adb8701529b03a8020019","goods_10":0,"goods_name":"魔兽世界WOW暗黑破坏神3凯恩之书钥匙链暗黑周边","goods_headurl":"http://7xp1b8.com1.z0.glb.clouddn.com/9d91708e-8925-4210-813f-a77d944cccbe","current_join_num":29,"all_join_num":100},{"goodsId":"ff808081529adb8701529b03abe0001b","goods_10":0,"goods_name":"魔兽世界WOW麻将魔兽世界纪念版麻将","goods_headurl":"http://7xp1b8.com1.z0.glb.clouddn.com/5ded9d7a-ece2-40ee-8c44-60c25d0fe334","current_join_num":0,"all_join_num":1000},{"goodsId":"ff808081529adb8701529b0212950014","goods_10":0,"goods_name":"键盘","goods_headurl":"http://7xp1b8.com1.z0.glb.clouddn.com/e9dcb4a7-ee81-46b7-9391-740505a97359","current_join_num":0,"all_join_num":123}],"listPrice":[{"id":"ff808081529adb8701529b095a510025","goods_headurl":"http://7xp1b8.com1.z0.glb.clouddn.com/08238a16-7a1e-425c-b0c3-89e0a4f8b436","goods_name":"果时代蓝牙音箱手机低音炮便携小音响","goods_rmb":80,"status":2,"client_name":"洛神℃","head_img":"http://7xp1b8.com1.z0.glb.clouddn.com/20160129695-4d1647cb4","client_join_num":80,"open_time":1454308038000},{"id":"ff808081529adb8701529b748f8d002b","goods_headurl":"http://7xp1b8.com1.z0.glb.clouddn.com/83286bc3-bbb3-46dd-bffc-933dc75e2bd1","goods_name":"100","goods_rmb":100,"status":2,"client_name":"洛神℃","head_img":"http://7xp1b8.com1.z0.glb.clouddn.com/20160129695-4d1647cb4","client_join_num":100,"open_time":1454307677000},{"id":"ff808081529adb8701529b095a9a0026","goods_headurl":"http://7xp1b8.com1.z0.glb.clouddn.com/84803233-d72a-4b45-9df9-74f0283bd850","goods_name":"200","goods_rmb":60,"status":2,"client_name":"洛神℃","head_img":"http://7xp1b8.com1.z0.glb.clouddn.com/20160129695-4d1647cb4","client_join_num":10,"open_time":1454307513000}]}
     * error : null
     * attributes : null
     */

    private Object token;
    private int code;
    /**
     * listad : [{"img_url":"http://7xp1b8.com1.z0.glb.clouddn.com/40288808528617e501528631dc2c0007","id":"ff808081529adb8701529b0212950014"},{"img_url":"http://7xp1b8.com1.z0.glb.clouddn.com/40288808528617e50152862449cf0005","id":"ff808081529adb8701529b03a8020019"},{"img_url":"http://7xp1b8.com1.z0.glb.clouddn.com/40288808528617e50152861d94d00003","id":"ff808081529adb8701529b020e190013"}]
     * time : 1454308164169
     * listgoods : [{"goodsId":"ff808081529adb8701529b7e1338003a","goods_10":1,"goods_name":"100","goods_headurl":"http://7xp1b8.com1.z0.glb.clouddn.com/83286bc3-bbb3-46dd-bffc-933dc75e2bd1","current_join_num":0,"all_join_num":100},{"goodsId":"ff808081529adb8701529b081a500024","goods_10":1,"goods_name":"300","goods_headurl":"http://7xp1b8.com1.z0.glb.clouddn.com/c39fa97d-2dbe-40a4-9cda-f5c110e2220e","current_join_num":10,"all_join_num":100},{"goodsId":"ff808081529adb8701529b8397be003c","goods_10":0,"goods_name":"果时代蓝牙音箱手机低音炮便携小音响","goods_headurl":"http://7xp1b8.com1.z0.glb.clouddn.com/08238a16-7a1e-425c-b0c3-89e0a4f8b436","current_join_num":0,"all_join_num":80},{"goodsId":"ff808081529adb8701529b7b940f0039","goods_10":1,"goods_name":"200","goods_headurl":"http://7xp1b8.com1.z0.glb.clouddn.com/84803233-d72a-4b45-9df9-74f0283bd850","current_join_num":0,"all_join_num":60},{"goodsId":"ff808081529adb8701529b073c600022","goods_10":0,"goods_name":"极速达新款道奇跑车摩托车战斧跑车概念跑车超酷摩托","goods_headurl":"http://7xp1b8.com1.z0.glb.clouddn.com/6a02b628-f0e5-484e-bf22-adcecfbad9b3","current_join_num":0,"all_join_num":100},{"goodsId":"ff808081529adb8701529b03aaf8001a","goods_10":1,"goods_name":"川行者太阳能登山包背包太阳能持续电源户外移动充电","goods_headurl":"http://7xp1b8.com1.z0.glb.clouddn.com/d05ca333-cb0d-4393-813c-13428311acf6","current_join_num":0,"all_join_num":900},{"goodsId":"ff808081529adb8701529b020e190013","goods_10":0,"goods_name":"雷柏RapooV56混光机械段落感游戏键盘黑色","goods_headurl":"http://7xp1b8.com1.z0.glb.clouddn.com/0b380c80-a9c7-466d-90e2-7f12b8aeb5c9","current_join_num":40,"all_join_num":100},{"goodsId":"ff808081529adb8701529b03a8020019","goods_10":0,"goods_name":"魔兽世界WOW暗黑破坏神3凯恩之书钥匙链暗黑周边","goods_headurl":"http://7xp1b8.com1.z0.glb.clouddn.com/9d91708e-8925-4210-813f-a77d944cccbe","current_join_num":29,"all_join_num":100},{"goodsId":"ff808081529adb8701529b03abe0001b","goods_10":0,"goods_name":"魔兽世界WOW麻将魔兽世界纪念版麻将","goods_headurl":"http://7xp1b8.com1.z0.glb.clouddn.com/5ded9d7a-ece2-40ee-8c44-60c25d0fe334","current_join_num":0,"all_join_num":1000},{"goodsId":"ff808081529adb8701529b0212950014","goods_10":0,"goods_name":"键盘","goods_headurl":"http://7xp1b8.com1.z0.glb.clouddn.com/e9dcb4a7-ee81-46b7-9391-740505a97359","current_join_num":0,"all_join_num":123}]
     * listPrice : [{"id":"ff808081529adb8701529b095a510025","goods_headurl":"http://7xp1b8.com1.z0.glb.clouddn.com/08238a16-7a1e-425c-b0c3-89e0a4f8b436","goods_name":"果时代蓝牙音箱手机低音炮便携小音响","goods_rmb":80,"status":2,"client_name":"洛神℃","head_img":"http://7xp1b8.com1.z0.glb.clouddn.com/20160129695-4d1647cb4","client_join_num":80,"open_time":1454308038000},{"id":"ff808081529adb8701529b748f8d002b","goods_headurl":"http://7xp1b8.com1.z0.glb.clouddn.com/83286bc3-bbb3-46dd-bffc-933dc75e2bd1","goods_name":"100","goods_rmb":100,"status":2,"client_name":"洛神℃","head_img":"http://7xp1b8.com1.z0.glb.clouddn.com/20160129695-4d1647cb4","client_join_num":100,"open_time":1454307677000},{"id":"ff808081529adb8701529b095a9a0026","goods_headurl":"http://7xp1b8.com1.z0.glb.clouddn.com/84803233-d72a-4b45-9df9-74f0283bd850","goods_name":"200","goods_rmb":60,"status":2,"client_name":"洛神℃","head_img":"http://7xp1b8.com1.z0.glb.clouddn.com/20160129695-4d1647cb4","client_join_num":10,"open_time":1454307513000}]
     */


    private String time;
    /**
     * img_url : http://7xp1b8.com1.z0.glb.clouddn.com/40288808528617e501528631dc2c0007
     * id : ff808081529adb8701529b0212950014
     */

    private List<ListadEntity> listadlist = new ArrayList<>();
    /**
     * goodsId : ff808081529adb8701529b7e1338003a
     * goods_10 : 1
     * goods_name : 100
     * goods_headurl : http://7xp1b8.com1.z0.glb.clouddn.com/83286bc3-bbb3-46dd-bffc-933dc75e2bd1
     * current_join_num : 0
     * all_join_num : 100
     */

    private List<ListgoodsEntity> listgoodslist = new ArrayList<>();
    /**
     * id : ff808081529adb8701529b095a510025
     * goods_headurl : http://7xp1b8.com1.z0.glb.clouddn.com/08238a16-7a1e-425c-b0c3-89e0a4f8b436
     * goods_name : 果时代蓝牙音箱手机低音炮便携小音响
     * goods_rmb : 80
     * status : 2
     * client_name : 洛神℃
     * head_img : http://7xp1b8.com1.z0.glb.clouddn.com/20160129695-4d1647cb4
     * client_join_num : 80
     * open_time : 1454308038000
     */

    private List<ListPriceEntity> listPricelist = new ArrayList<>();

    private Object error;
    private Object attributes;

    public void setToken(Object token) {
        this.token = token;
    }

    public void setCode(int code) {
        this.code = code;
    }


    public void setError(Object error) {
        this.error = error;
    }

    public void setAttributes(Object attributes) {
        this.attributes = attributes;
    }

    public Object getToken() {
        return token;
    }

    public int getCode() {
        return code;
    }

    public Object getError() {
        return error;
    }

    public Object getAttributes() {
        return attributes;
    }

    public FirstPage(JSONObject jsonObject) {
        JSONObject info = jsonObject.optJSONObject("info");

        time = info.optString("time");
        JSONArray listad = info.optJSONArray("listad");

        if (listad != null) {
            //如果解析为空
            for (int i = 0; i < listad.length(); i++) {
                JSONObject json = listad.optJSONObject(i);
                listadlist.add(new ListadEntity(json));
            }
        }
        JSONArray listgoods = info.optJSONArray("listgoods");

        if (listgoods != null) {
            //如果解析为空
            for (int i = 0; i < listgoods.length(); i++) {
                JSONObject json = listgoods.optJSONObject(i);
                listgoodslist.add(new ListgoodsEntity(json));
            }
        }
        JSONArray listPrice = info.optJSONArray("listPrice");

        if (listPrice != null) {
            //如果解析为空
            for (int i = 0; i < listPrice.length(); i++) {
                JSONObject json = listPrice.optJSONObject(i);
                listPricelist.add(new ListPriceEntity(json));
            }
        }
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setListad(List<ListadEntity> listad) {
        this.listadlist = listad;
    }

    public void setListgoods(List<ListgoodsEntity> listgoods) {
        this.listgoodslist = listgoods;
    }

    public void setListPrice(List<ListPriceEntity> listPrice) {
        this.listPricelist = listPrice;
    }

    public String getTime() {
        return time;
    }

    public List<ListadEntity> getListad() {
        return listadlist;
    }

    public List<ListgoodsEntity> getListgoods() {
        return listgoodslist;
    }

    public List<ListPriceEntity> getListPrice() {
        return listPricelist;
    }

}
