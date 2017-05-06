package cx.study.auction.contants;

/**
 *
 * Created by cheng.xiao on 2017/3/13.
 */

public interface HttpRest {
   // String SERVER_URL = "http://192.168.0.109:8080/";
    String SERVER_URL = "http://192.168.253.3:8080/auction/";
    String BASE_URL = SERVER_URL;
    String HOME_PAGE_REST = BASE_URL + "rest/homeInfo.do";

    String QUERY_COMMODITY_BY_ID_REST = BASE_URL + "CommodityRest/findCommodityById.do";
    String AUCTION_REST = BASE_URL + "/auction.do";
    String BID_PRICE_REST = BASE_URL + "CommodityRest/bid.do";
    String LOGIN_REST = BASE_URL + "UserRest/login.do";
    String REGISTER_REST = BASE_URL + "UserRest/register.do";
    String BID_RECORDS_REST = BASE_URL + "CommodityRest/bidRecords.do";

    /* Commodity */
  String COMMODITY = BASE_URL + "CommodityRest/";
    String COMMODITY_TYPE   = COMMODITY + "commodityType.do";
    String COMMODITIES = COMMODITY  + "commodities.do";
    String COMMODITY_AUCTION = COMMODITY  + "auction.do";
    /*订单*/
    String ORDER = BASE_URL + "OrderRest/";
    String ORDER_LIST = ORDER + "orderList.do";
    String ORDER_PAY = ORDER + "pay.do";
    String ORDER_CANCEL = ORDER + "cancel.do";
    String ORDER_FINISH = ORDER + "finish.do";
    String ORDER_INFO = ORDER + "orderInfo.do";
    /*用户*/
    String USER = BASE_URL + "UserRest/";
    String USER_RECHARGE = USER + "recharge.do";
    String USER_INFO = USER + "userInfo.do";

    /*地址*/
    String USER_ADDRESS = BASE_URL + "UserAddressRest/";
    String USER_ADDRESS_ADD = USER_ADDRESS + "add.do";
    String USER_ADDRESS_UPDATE = USER_ADDRESS + "update.do";
    String USER_ADDRESS_DELETE = USER_ADDRESS + "delete.do";
    String USER_ADDRESS_GET_ALL = USER_ADDRESS + "getAll.do";

    /*Deposit*/
    String DEPOSIT = BASE_URL + "DepositRest/";
    String DEPOSIT_PAY = DEPOSIT + "pay.do";
    String DEPOSIT_IS_PAY = DEPOSIT + "isPayDeposit.do";

    String HOME_REST = BASE_URL + "homeRest/";
    String HOME_INFO = HOME_REST + "homeInfo";
}
