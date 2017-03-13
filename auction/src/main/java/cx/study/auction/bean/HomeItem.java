package cx.study.auction.bean;

/**
 * 首页布局
 * Created by cheng.xiao on 2017/3/9.
 */

public class HomeItem<T> {
    public static final int TITLE = 0;
    public static final int CONTENT = 1;
    private int type;
    private T obj;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }


    @Override
    public String toString() {
        return "HomeItem{" +
                "type=" + type +
                ", obj=" + obj +
                '}';
    }
}
