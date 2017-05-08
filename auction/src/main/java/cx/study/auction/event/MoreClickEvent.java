package cx.study.auction.event;

import cx.study.auction.bean.CommodityType;

/**
 *
 * Created by cheng.xiao on 2017/4/24.
 */

public class MoreClickEvent {
    public CommodityType type;

    public MoreClickEvent(CommodityType type){
        this.type = type;
    }

}
