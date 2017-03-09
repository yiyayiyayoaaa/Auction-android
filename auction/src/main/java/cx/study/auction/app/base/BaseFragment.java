package cx.study.auction.app.base;

import android.support.v4.app.Fragment;

import butterknife.ButterKnife;

/**
 *
 * Created by cheng.xiao on 2017/3/9.
 */

public  class BaseFragment extends Fragment{
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
