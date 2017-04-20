package cx.study.auction.model.rest;

import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

import cx.study.auction.bean.BidRecord;
import cx.study.auction.bean.Commodity;
import cx.study.auction.bean.CommodityType;
import cx.study.auction.contants.HttpRest;
import cx.study.auction.model.rest.http.HttpClient;
import cx.study.auction.model.rest.http.HttpResult;
import cx.study.auction.model.rest.http.MCException;
import cx.study.auction.model.rest.json2object.Json2BidRecord;
import cx.study.auction.model.rest.json2object.Json2Commodity;
import cx.study.auction.model.rest.json2object.Json2CommodityType;

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

    public List<CommodityType> getCommodityType() throws MCException {
        Map<String, Object> jsonObj = Maps.newHashMap();
        jsonObj.put("id",1);
        HttpResult response = HttpClient.doJSONPost(HttpRest.COMMODITY_TYPE,jsonObj);
        return getListFromData(response.string(),new Json2CommodityType());
    }

    /**
     *
     * @return
     * @throws MCException
     */
    public List<Commodity> getCommodities(int id) throws MCException {
        Map<String, Object> jsonObj = Maps.newHashMap();
        jsonObj.put("id",id);
        HttpResult response = HttpClient.doJSONPost(HttpRest.COMMODITIES,jsonObj);
        return getListFromData(response.string(),new Json2Commodity());
    }

    public boolean getPayDepositRecord(int userId,int commodityId) throws MCException {
        Map<String, Object> jsonObj = Maps.newHashMap();
        jsonObj.put("userId",userId);
        jsonObj.put("commodityId",commodityId);
        HttpResult response = HttpClient.doJSONPost(HttpRest.DEPOSIT_IS_PAY,jsonObj);
        return response.code == 0;
    }

    public int payDeposit(int userId,int commodityId) throws MCException {
        Map<String, Object> jsonObj = Maps.newHashMap();
        jsonObj.put("userId",userId);
        jsonObj.put("commodityId",commodityId);
        HttpResult response = HttpClient.doJSONPost(HttpRest.DEPOSIT_PAY,jsonObj);
        return response.code;
    }
}
