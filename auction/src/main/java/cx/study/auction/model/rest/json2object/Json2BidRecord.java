package cx.study.auction.model.rest.json2object;

import org.json.JSONObject;

import java.util.Date;

import cx.study.auction.bean.BidRecord;
import cx.study.auction.model.rest.http.MCException;
import cx.study.auction.util.DateUtil;

/**
 *
 * Created by cheng.xiao on 2017/4/11.
 */

public class Json2BidRecord implements Json2Object<BidRecord>{
    @Override
    public BidRecord json2Object(JSONObject jsonObject) throws MCException {
        if (jsonObject == null) {
            return null;
        }
        BidRecord record = new BidRecord();
        record.setId(jsonObject.optInt("id"));
        record.setCommodityId(jsonObject.optJSONObject("auction").optInt("id"));
        record.setUserId(jsonObject.optJSONObject("user").optInt("id"));
        record.setBidTime(new Date(jsonObject.optLong("bidTime")));
        record.setPrice(jsonObject.optDouble("price"));
        return record;
    }
}
