package cx.study.auction.model.rest;

import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

import cx.study.auction.bean.UserAddress;
import cx.study.auction.contants.HttpRest;
import cx.study.auction.model.rest.http.HttpClient;
import cx.study.auction.model.rest.http.HttpResult;
import cx.study.auction.model.rest.http.MCException;
import cx.study.auction.model.rest.json2object.Json2UserAddress;

/**
 *
 * Created by cheng.xiao on 2017/4/19.
 */

public class UserAddressRest extends AbstractRest{

    public List<UserAddress> getUserAddressList(int userId) throws MCException {
        Map<String, Object> jsonObj = Maps.newHashMap();
        jsonObj.put("id", userId);
        HttpResult response = HttpClient.doJSONPost(HttpRest.USER_ADDRESS_GET_ALL,jsonObj);
        return getListFromData(response.string(), new Json2UserAddress());
    }

    public void add(UserAddress address) throws MCException {
        Map<String, Object> jsonObj = Maps.newHashMap();
        jsonObj.put("userId",address.getUserId());
        jsonObj.put("address",address.getAddress());
        HttpClient.doJSONPost(HttpRest.USER_ADDRESS_ADD,jsonObj);
    }

    public void update(UserAddress address) throws MCException {
        Map<String, Object> jsonObj = Maps.newHashMap();
        jsonObj.put("id",address.getId());
        jsonObj.put("userId",address.getUserId());
        jsonObj.put("address",address.getAddress());
        HttpClient.doJSONPost(HttpRest.USER_ADDRESS_UPDATE,jsonObj);
    }

    public void delete(int id) throws MCException {
        Map<String, Object> jsonObj = Maps.newHashMap();
        jsonObj.put("id",id);
        HttpClient.doJSONPost(HttpRest.USER_ADDRESS_DELETE,jsonObj);
    }
}
