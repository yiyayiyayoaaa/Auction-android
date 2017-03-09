package cx.study.auction.bean;

import java.util.List;

/**
 * 首页布局
 * Created by cheng.xiao on 2017/3/9.
 */

public class HomeItem {
    public static final int TITLE = 1;
    public static final int CONTENT = 2;
    private int type;
    private String typeName;
    private List<Commodity> commodities;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Commodity> getCommodities() {
        return commodities;
    }

    public void setCommodities(List<Commodity> commodities) {
        this.commodities = commodities;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
