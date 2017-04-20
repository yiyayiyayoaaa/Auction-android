package cx.study.auction.model.rest;

import com.google.common.collect.Maps;

import java.util.Map;

import cx.study.auction.bean.User;
import cx.study.auction.contants.HttpRest;
import cx.study.auction.model.rest.http.HttpClient;
import cx.study.auction.model.rest.http.HttpResult;
import cx.study.auction.model.rest.http.MCException;
import cx.study.auction.model.rest.json2object.Json2User;

/**
 *
 * Created by cheng.xiao on 2017/4/6.
 */

public class UserRest extends AbstractRest{

    public boolean register(String username,String password) throws MCException{
        Map<String, Object> jsonObj = Maps.newHashMap();
        jsonObj.put("username", username);
        jsonObj.put("password", password);
        HttpResult response = HttpClient.doJSONPost(HttpRest.REGISTER_REST,jsonObj);
        return response.code == 0;
    }

    public User login(String username,String password) throws MCException{
        Map<String, Object> jsonObj = Maps.newHashMap();
        jsonObj.put("username", username);
        jsonObj.put("password", password);
        HttpResult response = HttpClient.doJSONPost(HttpRest.LOGIN_REST,jsonObj);
        return new Json2User().json2Object(response.object());
    }

    public boolean recharge(int userId,double money) throws MCException {
        Map<String, Object> jsonObj = Maps.newHashMap();
        jsonObj.put("userId", userId);
        jsonObj.put("money", money);
        HttpResult response = HttpClient.doJSONPost(HttpRest.USER_RECHARGE,jsonObj);
        return response.code == 0;
    }

    public User getUserInfo(int id) throws MCException {
        Map<String, Object> jsonObj = Maps.newHashMap();
        jsonObj.put("id", id);
        HttpResult response = HttpClient.doJSONPost(HttpRest.USER_INFO,jsonObj);
        return new Json2User().json2Object(response.object());
    }
}
