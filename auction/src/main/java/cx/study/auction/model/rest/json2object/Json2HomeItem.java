package cx.study.auction.model.rest.json2object;

import org.json.JSONObject;

import cx.study.auction.bean.Commodity;
import cx.study.auction.bean.HomeContentItem;
import cx.study.auction.bean.HomeItem;
import cx.study.auction.bean.HomeTitleItem;

/**
 *
 * Created by cheng.xiao on 2017/3/13.
 */

public class Json2HomeItem implements Json2Object<HomeItem>{
    @Override
    public HomeItem json2Object(JSONObject jsonObject) throws Exception {
        if (jsonObject == null){
            return null;
        }
        //HomeItem homeItem = null;
        try{
            int type = jsonObject.optInt("type");
            switch (type){

                case HomeItem.TITLE:
                    HomeItem<String> homeTitleItem = new HomeTitleItem();
                    homeTitleItem.setType(type);
                    homeTitleItem.setObj(jsonObject.getString("obj"));
                    return homeTitleItem;
                case HomeItem.CONTENT:
                    HomeItem<Commodity> homeContentItem = new HomeContentItem();
                    homeContentItem.setType(type);
                    JSONObject object = jsonObject.optJSONObject("obj");
                    Commodity commodity = new Json2Commodity().json2Object(object);
                    homeContentItem.setObj(commodity);
                    return homeContentItem;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
