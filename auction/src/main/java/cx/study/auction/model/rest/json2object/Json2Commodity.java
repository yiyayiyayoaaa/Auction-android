package cx.study.auction.model.rest.json2object;

import com.google.common.collect.Lists;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import cx.study.auction.bean.Commodity;

/**
 *
 * Created by cheng.xiao on 2017/3/13.
 */

public class Json2Commodity implements Json2Object<Commodity> {
    @Override
    public Commodity json2Object(JSONObject jsonObject) throws Exception {
        if (jsonObject == null){
            return null;
        }
        Commodity commodity = null;
        try{
            commodity = new Commodity();
            commodity.setId(jsonObject.optInt("id"));
            commodity.setCommodityName(jsonObject.optString("commodityName"));
            commodity.setTypeId(jsonObject.optInt("typeId"));
            commodity.setTypeName(jsonObject.optString("typeName"));
            commodity.setDescription(jsonObject.optString("description"));
            commodity.setCustomerId(jsonObject.optInt("customerId"));
            commodity.setCustomerName(jsonObject.optString("customerName"));
            commodity.setAppraisedPrice(jsonObject.optDouble("appraisedPrice"));
            commodity.setReservePrice(jsonObject.optDouble("reservePrice"));
            commodity.setStartingPrice(jsonObject.optDouble("startingPrice"));
            commodity.setBidIncrements(jsonObject.optDouble("bidIncrements"));
            commodity.setHammerPrice(jsonObject.optDouble("hammerPrice"));
            commodity.setBiddingDeposit(jsonObject.optDouble("biddingDeposit"));
            commodity.setStatus(jsonObject.optInt("status"));
            //commodity.setStartTime((Date) jsonObject.opt("startTime"));
            //new SimpleDateFormat().parse((String) jsonObject.opt("startTime"));
           // commodity.setEndTime((Date) jsonObject.opt("endTime"));
            JSONArray jsonArray = jsonObject.optJSONArray("imageUrls");
            List<String> urls = Lists.newArrayList();
            for (int i = 0; i < jsonArray.length(); i ++){
                String s = (String) jsonArray.get(i);
                urls.add(s);
            }
            commodity.setImageUrls(urls);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return commodity;
    }
}
