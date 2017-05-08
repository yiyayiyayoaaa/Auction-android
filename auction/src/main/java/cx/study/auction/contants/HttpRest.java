package cx.study.auction.contants;

/**
 *
 * Created by cheng.xiao on 2017/3/13.
 */

public interface HttpRest {
   // String SERVER_URL = "http://192.168.0.109:8080/";
    String SERVER_URL = "http://192.168.92.2:8080/auction/";
    String BASE_URL = SERVER_URL;
    String HOME_PAGE_REST = BASE_URL + "rest/homeInfo";

    String QUERY_COMMODITY_BY_ID_REST = BASE_URL + "CommodityRest/findCommodityById";
    String AUCTION_REST = BASE_URL + "/auction";
    String BID_PRICE_REST = BASE_URL + "CommodityRest/bid";
    String LOGIN_REST = BASE_URL + "UserRest/login";
    String REGISTER_REST = BASE_URL + "UserRest/register";
    String BID_RECORDS_REST = BASE_URL + "CommodityRest/bidRecords";

    /* Commodity */
  String COMMODITY = BASE_URL + "CommodityRest/";
    String COMMODITY_TYPE   = COMMODITY + "commodityType";
    String COMMODITIES = COMMODITY  + "commodities";
    String COMMODITY_AUCTION = COMMODITY  + "auction";
    /*订单*/
    String ORDER = BASE_URL + "OrderRest/";
    String ORDER_LIST = ORDER + "orderList";
    String ORDER_PAY = ORDER + "pay";
    String ORDER_CANCEL = ORDER + "cancel";
    String ORDER_FINISH = ORDER + "finish";
    String ORDER_INFO = ORDER + "orderInfo";
    /*用户*/
    String USER = BASE_URL + "UserRest/";
    String USER_RECHARGE = USER + "recharge";
    String USER_INFO = USER + "userInfo";
    String USER_GENDER = USER + "updateGender";
    String USER_NICKNAME = USER + "updateNickname";

    /*地址*/
    String USER_ADDRESS = BASE_URL + "UserAddressRest/";
    String USER_ADDRESS_ADD = USER_ADDRESS + "add";
    String USER_ADDRESS_UPDATE = USER_ADDRESS + "update";
    String USER_ADDRESS_DELETE = USER_ADDRESS + "delete";
    String USER_ADDRESS_GET_ALL = USER_ADDRESS + "getAll";

    /*Deposit*/
    String DEPOSIT = BASE_URL + "DepositRest/";
    String DEPOSIT_PAY = DEPOSIT + "pay";
    String DEPOSIT_IS_PAY = DEPOSIT + "isPayDeposit";

    String HOME_REST = BASE_URL + "homeRest/";
    String HOME_INFO = HOME_REST + "homeInfo";
}
