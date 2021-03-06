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

    public void pay(int id,String address) throws MCException {
        Map<String, Object> jsonObj = Maps.newHashMap();
        jsonObj.put("id", id);
        jsonObj.put("address", address);
        HttpClient.doJSONPost(HttpRest.ORDER_PAY,jsonObj);

    }

    public void cancel(int id) throws MCException {
        Map<String, Object> jsonObj = Maps.newHashMap();
        jsonObj.put("id", id);
        HttpClient.doJSONPost(HttpRest.ORDER_CANCEL,jsonObj);
    }

    public void finish(int id) throws MCException {
        Map<String, Object> jsonObj = Maps.newHashMap();
        jsonObj.put("id", id);
        HttpClient.doJSONPost(HttpRest.ORDER_FINISH,jsonObj);
    }

    public Order getOrderById(int id) throws MCException{
        Map<String, Object> jsonObj = Maps.newHashMap();
        jsonObj.put("id", id);
        HttpResult result = HttpClient.doJSONPost(HttpRest.ORDER_INFO, jsonObj);
        return new Json2Order().json2Object(result.object());
    }
}
