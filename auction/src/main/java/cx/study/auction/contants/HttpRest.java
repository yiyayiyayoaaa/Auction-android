package cx.study.auction.contants;

/**
 *
 * Created by cheng.xiao on 2017/3/13.
 */

public interface HttpRest {
    String SERVER_URL = "http://192.168.0.109:8080/";
    String BASE_URL = SERVER_URL;
    String HOME_PAGE_REST = BASE_URL + "rest/homeInfo.do";

    String QUERY_COMMODITY_BY_ID_REST = BASE_URL + "CommodityRest/findCommodityById.do";
    String AUCTION_REST = BASE_URL + "/auction.do";
    String BID_PRICE_REST = BASE_URL + "CommodityRest/bid.do";
    String LOGIN_REST = BASE_URL + "UserRest/login.do";
    String REGISTER_REST = BASE_URL + "UserRest/register.do";
    String BID_RECORDS_REST = BASE_URL + "CommodityRest/bidRecords.do";
    String COMMODITY_TYPE   = BASE_URL + "CommodityRest/commodityType.do";
    String COMMODITIES = BASE_URL + "CommodityRest/commodities.do";

    /*地址*/
    String USER_ADDRESS = BASE_URL + "UserAddressRest/";
    String USER_ADDRESS_ADD = USER_ADDRESS + "add.do";
    String USER_ADDRESS_UPDATE = USER_ADDRESS + "update.do";
    String USER_ADDRESS_DELETE = USER_ADDRESS + "delete.do";
    String USER_ADDRESS_GET_ALL = USER_ADDRESS + "getAll.do";
}
