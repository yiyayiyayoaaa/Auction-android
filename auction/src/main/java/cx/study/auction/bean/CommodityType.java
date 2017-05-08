package cx.study.auction.bean;

import java.io.Serializable;

/**
 *
 * Created by chengxiao on 2017/4/16.
 */

public class CommodityType implements Serializable {
    private int id;
    private String typeName;
    private boolean isSelect;
    public CommodityType() {
    }

    public CommodityType(int id, String typeName) {
        this.id = id;
        this.typeName = typeName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
