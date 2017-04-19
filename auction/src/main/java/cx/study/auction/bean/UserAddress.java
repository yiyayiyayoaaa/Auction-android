package cx.study.auction.bean;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * Created by cheng.xiao on 2017/4/17.
 */
public class UserAddress implements Serializable{

    private int id;
    private int userId;
    private String address;
    private Date time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
