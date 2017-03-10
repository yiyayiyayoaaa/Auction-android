package cx.study.auction.event;

/**
 *
 * Created by cheng.xiao on 2017/3/10.
 */

public class ViewPagerChangeEvent {
    public int currentItem;

    public ViewPagerChangeEvent(int currentItem){
        this.currentItem = currentItem;
    }
}
