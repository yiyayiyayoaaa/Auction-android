package cx.study.auction.model.rest;

import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

import cx.study.auction.bean.BidRecord;
import cx.study.auction.bean.Commodity;
import cx.study.auction.contants.HttpRest;
import cx.study.auction.model.rest.http.HttpClient;
import cx.study.auction.model.rest.http.HttpResult;
import cx.study.auction.model.rest.http.MCException;
import cx.study.auction.model.rest.json2object.Json2BidRecord;
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
        return new Json2Commodity().json2Object(response.object());
    }

    public String postBidPrice(BidRecord bid) throws MCException {
        Map<String, Object> jsonObj = Maps.newHashMap();
        jsonObj.put("commodityId", bid.getCommodityId());
        jsonObj.put("userId",bid.getUserId());
        jsonObj.put("price",bid.getPrice());
        HttpResult response = HttpClient.doJSONPost(HttpRest.BID_PRICE_REST,jsonObj);
        return response.msg;
    }

    public List<BidRecord> getBidRecords(int id) throws MCException {
        Map<String, Object> jsonObj = Maps.newHashMap();
        jsonObj.put("commodityId", id);
        HttpResult response = HttpClient.doJSONPost(HttpRest.BID_RECORDS_REST,jsonObj);
        return getListFromData(response.string(),new Json2BidRecord());
    }
}
