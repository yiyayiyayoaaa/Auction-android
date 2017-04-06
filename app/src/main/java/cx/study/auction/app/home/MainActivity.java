package cx.study.auction.app.home;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import butterknife.Bind;
import cx.study.auction.R;
import cx.study.auction.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fragmentLayout)
    FrameLayout fragmentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(null);
    }

    @Override
    public void onActionClick(ActionItem item) {

    }

    @Override
    public void onHomeClick() {
    }
}
