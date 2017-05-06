package cx.study.auction.model.rest.json2object;

import com.google.common.collect.Lists;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import cx.study.auction.bean.Commodity;
import cx.study.auction.model.rest.http.MCException;
import cx.study.auction.util.DateUtil;

/**
 *
 * Created by cheng.xiao on 2017/3/13.
 */

public class Json2Commodity implements Json2Object<Commodity> {
    @Override
    public Commodity json2Object(JSONObject jsonObject) throws MCException {
        if (jsonObject == null){
            return null;
        }
        Commodity commodity = new Commodity();
        try{
            int id = jsonObject.optInt("id");
            commodity.setId(id);
            String name = jsonObject.optString("name");
            commodity.setCommodityName(name);
            int typeId = jsonObject.optJSONObject("type").optInt("id");
            commodity.setTypeId(typeId);
            String typeName = jsonObject.optJSONObject("type").optString("typeName");
            commodity.setTypeName(typeName);
            String description = jsonObject.optString("description");
            commodity.setDescription(description);
            int customerId = jsonObject.optJSONObject("customer").optInt("id");
            commodity.setCustomerId(customerId);
            String customerName = jsonObject.optJSONObject("customer").optString("name");
            commodity.setCustomerName(customerName);
            double startPrice = jsonObject.optDouble("startPrice");
            commodity.setStartingPrice(startPrice);
            double appraisedPrice = jsonObject.optDouble("appraisedPrice");
            commodity.setAppraisedPrice(appraisedPrice);
            double bidIncrements = jsonObject.optDouble("bidIncrements");
            commodity.setBidIncrements(bidIncrements);
            double hammerPrice = jsonObject.optDouble("hammerPrice");
            commodity.setHammerPrice(hammerPrice);
            double deposit = jsonObject.optDouble("deposit");
            commodity.setBiddingDeposit(deposit);
            int status = jsonObject.optInt("status");
            commodity.setStatus(status);
            long startTime = jsonObject.optLong("startTime");
            commodity.setStartTime(new Date(startTime));
            long endTime = jsonObject.optLong("endTime");
            commodity.setEndTime(new Date(endTime));
            JSONArray jsonArray = jsonObject.optJSONArray("imageUrls");
            List<String> urls = Lists.newArrayList();
            for (int i = 0; i < jsonArray.length(); i ++){
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                urls.add(jsonObj.optString("url"));
            }
            commodity.setImageUrls(urls);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return commodity;
    }
}
