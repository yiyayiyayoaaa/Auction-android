package cx.study.auction.model.rest.json2object;

import android.text.TextUtils;

import org.json.JSONObject;

import cx.study.auction.bean.Order;
import cx.study.auction.model.rest.http.MCException;
import cx.study.auction.util.DateUtil;

/**
 *
 * Created by cheng.xiao on 2017/4/20.
 */

public class Json2Order implements Json2Object<Order>{
    @Override
    public Order json2Object(JSONObject jsonObject) throws MCException {
        if (jsonObject == null) {
            return null;
        }
        Order order = new Order();
        order.setId(jsonObject.optInt("id"));
        order.setCommodityId(jsonObject.optInt("commodityId"));
        order.setCommodityName(jsonObject.optString("commodityName"));
        order.setUrl(jsonObject.optString("url"));
        order.setUserId(jsonObject.optInt("userId"));
        order.setAddress(jsonObject.optString("address"));
        order.setPrice(jsonObject.optDouble("price"));
        order.setStatus(jsonObject.optInt("status"));
        order.setOrderNum(jsonObject.optString("orderNum"));
        String payTime = jsonObject.optString("payTime");
        if (!TextUtils.isEmpty(payTime)){
            order.setPayTime(DateUtil.getDateByString(payTime));
        }
        order.setStartTime(DateUtil.getDateByString(jsonObject.optString("startTime")));
//        order.setUpdateTime(DateUtil.getDateByString(jsonObject.optString("updateTime")));
//        order.setEndTime(DateUtil.getDateByString(jsonObject.optString("endTime")));
        return order;
    }
}
