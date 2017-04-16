package cx.study.auction.model.rest.json2object;

import org.json.JSONObject;

import cx.study.auction.bean.CommodityType;
import cx.study.auction.model.rest.http.MCException;

/**
 *
 * Created by chengxiao on 2017/4/16.
 */

public class Json2CommodityType implements Json2Object<CommodityType>{
    @Override
    public CommodityType json2Object(JSONObject jsonObject) throws MCException {
        if (jsonObject == null) {
            return null;
        }
        CommodityType commodityType = new CommodityType();
        commodityType.setId(jsonObject.optInt("id"));
        commodityType.setTypeName(jsonObject.optString("typeName"));
        return commodityType;
    }
}
