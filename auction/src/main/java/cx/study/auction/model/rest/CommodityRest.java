package cx.study.auction.model.rest;

import android.util.Log;

import com.google.common.collect.Maps;

import java.util.Map;

import cx.study.auction.bean.Commodity;
import cx.study.auction.contants.HttpRest;
import cx.study.auction.model.rest.http.HttpClient;
import cx.study.auction.model.rest.http.HttpResult;
import cx.study.auction.model.rest.http.MCException;
import cx.study.auction.model.rest.json2object.Json2Commodity;

/**
 *
 * Created by cheng.xiao on 2017/3/25.
 */

public class CommodityRest extends AbstractRest{

    public Commodity getCommodityById(int id) throws MCException {
        Map<String, Object> jsonObj = Maps.newHashMap();
        jsonObj.put("commodityId", id);
        HttpResult response = HttpClient.doJSONPost(HttpRest.QUERY_COMMODITY_BY_ID_REST,jsonObj);
        Log.e("d", "getCommodityById: " + response.string());
        return new Json2Commodity().json2Object(response.object());
    }
}
