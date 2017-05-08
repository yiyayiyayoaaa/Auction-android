package cx.study.auction.model.rest.json2object;

import org.json.JSONObject;

import cx.study.auction.bean.User;
import cx.study.auction.model.rest.http.MCException;

/**
 *
 * Created by cheng.xiao on 2017/4/6.
 */

public class Json2User implements Json2Object<User>{
    @Override
    public User json2Object(JSONObject jsonObject) throws MCException {
        if (jsonObject == null){
            return null;
        }
        User user = new User();
        user.setId(jsonObject.optInt("id"));
        user.setUsername(jsonObject.optString("username"));
        user.setPassword(jsonObject.optString("password"));
        user.setGender(jsonObject.optInt("gender"));
        user.setNickname(jsonObject.optString("nickname"));
        user.setAccount(jsonObject.optDouble("account"));
        return user;
    }
}
