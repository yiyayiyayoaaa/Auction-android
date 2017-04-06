package cx.study.auction.model.rest.json2object;

import org.json.JSONObject;

import cx.study.auction.model.rest.http.MCException;

/**
 *
 * Created by cheng.xiao on 2017/3/13.
 */

public interface Json2Object<T> {
    T json2Object(JSONObject jsonObject) throws MCException;
}
