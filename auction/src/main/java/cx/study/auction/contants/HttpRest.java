package cx.study.auction.contants;

/**
 *
 * Created by cheng.xiao on 2017/3/13.
 */

public interface HttpRest {
    String SERVER_URL = "http://192.168.0.107:8080/";
    String BASE_URL = SERVER_URL;
    String HOME_PAGE_REST = BASE_URL + "rest/homeInfo.do";

    String QUERY_COMMODITY_BY_ID_REST = BASE_URL + "CommodityRest/findCommodityById.do";
    String AUCTION_REST = BASE_URL + "/auction.do";
    String LOGIN_REST = BASE_URL + "UserRest/login.do";
    String REGISTER_REST = BASE_URL + "UserRest/register.do";
}