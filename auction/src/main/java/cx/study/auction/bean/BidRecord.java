package cx.study.auction.bean;

import java.util.Date;

/**
 * 出价纪录
 * Created by cheng.xiao on 2017/4/10.
 */
public class BidRecord {
    private int id;
    private int commodityId;
    private int userId;
    private double price;
    private Date bidTime;

    public BidRecord(){
        this.bidTime = new Date();
    }
    public BidRecord(int commodityId,int userId,double price){
        this.commodityId = commodityId;
        this.userId = userId;
        this.price = price;
        this.bidTime = new Date();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(int commodityId) {
        this.commodityId = commodityId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getBidTime() {
        return bidTime;
    }

    public void setBidTime(Date bidTime) {
        this.bidTime = bidTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
