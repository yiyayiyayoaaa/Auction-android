package cx.study.auction.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *  商品
 * Created by cheng.xiao on 2017/3/9.
 */

public class Commodity implements Serializable{
    public interface CommodityStatus{
        int WAIT_AUCTION = 1;
        int AUCTION = 2;
        int SUCCESS = 3;
        int UNSOLD = 4;
        int OTHER = 5;
        int OFF = 6;
    }
    private Integer id;
    private String commodityName;
    private Integer typeId;
    private String  typeName;
    private String description;
    private Integer customerId;
    private String customerName;
    private Double appraisedPrice;
    private Double reservePrice;
    private Double startingPrice;
    private Double bidIncrements;
    private Double hammerPrice;
    private Double biddingDeposit;
    private Integer status;
    private Date startTime;
    private Date endTime;
    private List<String> imageUrls;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Double getAppraisedPrice() {
        return appraisedPrice;
    }

    public void setAppraisedPrice(Double appraisedPrice) {
        this.appraisedPrice = appraisedPrice;
    }

    public Double getReservePrice() {
        return reservePrice;
    }

    public void setReservePrice(Double reservePrice) {
        this.reservePrice = reservePrice;
    }

    public Double getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(Double startingPrice) {
        this.startingPrice = startingPrice;
    }

    public Double getBidIncrements() {
        return bidIncrements;
    }

    public void setBidIncrements(Double bidIncrements) {
        this.bidIncrements = bidIncrements;
    }

    public Double getHammerPrice() {
        return hammerPrice;
    }

    public void setHammerPrice(Double hammerPrice) {
        this.hammerPrice = hammerPrice;
    }

    public Double getBiddingDeposit() {
        return biddingDeposit;
    }

    public void setBiddingDeposit(Double biddingDeposit) {
        this.biddingDeposit = biddingDeposit;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    @Override
    public String toString() {
        return "Commodity{" +
                "id=" + id +
                ", commodityName='" + commodityName + '\'' +
                ", typeId=" + typeId +
                ", typeName='" + typeName + '\'' +
                ", description='" + description + '\'' +
                ", customerId=" + customerId +
                ", customerName='" + customerName + '\'' +
                ", appraisedPrice=" + appraisedPrice +
                ", reservePrice=" + reservePrice +
                ", startingPrice=" + startingPrice +
                ", bidIncrements=" + bidIncrements +
                ", hammerPrice=" + hammerPrice +
                ", biddingDeposit=" + biddingDeposit +
                ", status=" + status +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", imageUrls=" + imageUrls +
                '}';
    }
}
