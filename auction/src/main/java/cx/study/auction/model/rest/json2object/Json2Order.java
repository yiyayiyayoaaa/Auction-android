package cx.study.auction.model.rest.json2object;

import android.text.TextUtils;

import org.json.JSONObject;

import java.util.Date;

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
        order.setCommodityId(jsonObject.optJSONObject("auction").optInt("id"));
        order.setCommodityName(jsonObject.optJSONObject("auction").optString("name"));
        order.setDescription(jsonObject.optJSONObject("auction").optString("description"));

        order.setUrl(jsonObject.optString("url"));

        order.setUserId(jsonObject.optJSONObject("user").optInt("userId"));

        order.setAddress(jsonObject.optString("address"));

        order.setPrice(jsonObject.optDouble("price"));

        order.setStatus(jsonObject.optInt("status"));

        order.setOrderNum(jsonObject.optString("orderNum"));

        long payTime = jsonObject.optLong("payTime");

        if (payTime > 0){
            order.setPayTime(new Date(payTime));
        }
        order.setStartTime(new Date(jsonObject.optLong("createTime")));
//        order.setUpdateTime(DateUtil.getDateByString(jsonObject.optString("updateTime")));
        long endTime = jsonObject.optLong("endTime");
        if (endTime > 0){
            order.setEndTime(new Date(endTime));
        }
        return order;
    }
}
