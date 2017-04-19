package cx.study.auction.model.rest.json2object;

import org.json.JSONObject;

import cx.study.auction.bean.UserAddress;
import cx.study.auction.model.rest.http.MCException;

/**
 *
 * Created by cheng.xiao on 2017/4/19.
 */

public class Json2UserAddress implements Json2Object<UserAddress>{
    @Override
    public UserAddress json2Object(JSONObject jsonObject) throws MCException {
        if (jsonObject == null){
            return null;
        }
        UserAddress userAddress = new UserAddress();
        userAddress.setId(jsonObject.optInt("id"));
        userAddress.setUserId(jsonObject.optInt("userId"));
        userAddress.setAddress(jsonObject.optString("address"));
        return userAddress;
    }
}
