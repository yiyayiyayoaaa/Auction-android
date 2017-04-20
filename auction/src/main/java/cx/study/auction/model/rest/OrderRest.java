package cx.study.auction.model.rest;

import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

import cx.study.auction.bean.Order;
import cx.study.auction.contants.HttpRest;
import cx.study.auction.model.rest.http.HttpClient;
import cx.study.auction.model.rest.http.HttpResult;
import cx.study.auction.model.rest.http.MCException;
import cx.study.auction.model.rest.json2object.Json2Order;

/**
 *
 * Created by cheng.xiao on 2017/4/20.
 */

public class OrderRest extends AbstractRest{

    public List<Order> getOrderList(int userId,int status) throws MCException {
        Map<String, Object> jsonObj = Maps.newHashMap();
        jsonObj.put("userId", userId);
        jsonObj.put("status", status);
        HttpResult response = HttpClient.doJSONPost(HttpRest.ORDER_LIST,jsonObj);
        return getListFromData(response.string(),new Json2Order());
    }
}
