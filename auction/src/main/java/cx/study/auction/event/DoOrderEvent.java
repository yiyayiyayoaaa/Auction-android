package cx.study.auction.event;

/**
 *
 * Created by cheng.xiao on 2017/4/21.
 */

public class DoOrderEvent {

    public interface Event{
        int PAY = 0;
        int RECEIVED = 1;
        int CANCEL = 2;
    }
    public int event;
    public int orderId;

    public DoOrderEvent() {
    }

    public DoOrderEvent(int event, int orderId){
        this.event = event;
        this.orderId = orderId;
    }
}
