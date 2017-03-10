package cx.study.auction.app.base;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import cx.study.auction.R;

/**
 *
 * Created by cheng.xiao on 2017/3/9.
 */

public class BaseActivity extends AppCompatActivity{
    protected ViewGroup contentLayout;
    private TextView tvTitle;
    private List<ActionItem> actionItems;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);
        initToolbar();
    }

    /**
     * 初始化toolbar,子类重写可以调整home键的样式,或者设置toolbar的popupTheme
     */
    protected void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.mcToolbar);
        tvTitle = (TextView) findViewById(R.id.mcToolbarTitle);
        contentLayout = (ViewGroup) findViewById(R.id.mcContentLayout);
        this.setSupportActionBar(toolbar);
    }

    @Override
    public final void setSupportActionBar(@Nullable Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            //actionBar.setHomeAsUpIndicator(R.drawable.ic_home_back);
        }
        if (tvTitle != null) {
            tvTitle.setText(getTitle());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (actionItems == null || actionItems.isEmpty()) {
            return super.onCreateOptionsMenu(menu);
        }
        int size = actionItems.size();
        for (int i = 0; i < size; i++) {
            final ActionItem action = actionItems.get(i);
            action.createMenuItem(menu, i).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    onActionClick(action);
                    return true;
                }
            });
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            this.onHomeClick();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onHomeClick() {
        finish();
    }

    /**
     * 右边的Action按钮点击事件,注意右边的按钮需要设置标题或者图标才能够显示出来
     */
    public void onActionClick(ActionItem item) {
        // override this method
    }

    /**
     * 添加一个单一的actionItem
     * @param itemId    唯一id
     * @param title     action标题
     * @param icon      action图标,如果有值的话会设置的标题会不显示
     */
    @MainThread
    public final void setSingleAction(int itemId, CharSequence title, Drawable icon) {
        ActionItem item = new ActionItem.Builder().itemId(itemId).title(title).icon(icon).build();
        this.setActions(Collections.singletonList(item));
    }

    /**
     * @param drawable 设置导航栏左侧图标
     */
    public final void setHomeIcon(Drawable drawable) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(drawable);
        }
    }

    /**
     * 隐藏actionbar
     */
    @MainThread
    protected final void setNoToolBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().hide();
        } else {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
    }

    @MainThread
    public final void setActions(List<ActionItem> items) {
        if (actionItems == null) {
            actionItems = new LinkedList<>();
        } else {
            actionItems.clear();
        }
        actionItems.addAll(items);
        this.supportInvalidateOptionsMenu();
    }


    /**
     * 重写设置标题的函数,如果标题是以"#"开头的话,使用自带的标题栏,隐藏自定义标题栏
     * @param title
     */
    @Override
    public final void setTitle(CharSequence title) {
        super.setTitle(title);
        String titleLeft = title.toString();
        boolean isLeftTitle = titleLeft.startsWith("#");
        if (isLeftTitle) {
            titleLeft = titleLeft.substring(1, titleLeft.length());
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
//			actionBar.setDisplayShowTitleEnabled(isLeftTitle);
//			actionBar.setTitle(titleLeft);
        }
        if (tvTitle != null) {
            tvTitle.setText(titleLeft);
            tvTitle.setVisibility(isLeftTitle ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public final void setContentView(@LayoutRes int layoutResID) {
        setContentView(View.inflate(this, layoutResID, null));
    }

    /**
     *
     * @return contentLayout的id,当界面是由单个fragment构成的时候用来放置fragment
     */
    protected final @IdRes
    int getContentLayoutId() {
        return contentLayout == null ? 0 : contentLayout.getId();
    }

    @Override
    public final void setContentView(View view) {
        int size = FrameLayout.LayoutParams.MATCH_PARENT;
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(size, size);
        lp.gravity = Gravity.CENTER;
        setContentView(view, lp);
    }

    @Override
    public final void setContentView(View view, ViewGroup.LayoutParams params) {
        contentLayout.addView(view, params);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 右侧菜单
     */
    public static class ActionItem {

        /**
         * @hide
         */
        private ActionItem(){}

        /**
         * 生成MenuItem
         * @param menu  menu
         * @param index 位置索引
         * @return  {@link MenuItem}
         */
        private MenuItem createMenuItem(Menu menu, int index) {
            MenuItem mi = menu.add(groupId, itemId, index, title).setIcon(icon);
            int actionEnum = MenuItem.SHOW_AS_ACTION_ALWAYS;
            if (index > 2) {
                actionEnum = MenuItem.SHOW_AS_ACTION_NEVER;
            }
            MenuItemCompat.setShowAsAction(mi, actionEnum);
            return mi;
        }

        private int groupId;
        private int itemId;
        private String title;
        private Drawable icon;

        public int getGroupId() {
            return groupId;
        }

        public int getItemId() {
            return itemId;
        }

        public String getTitle() {
            return title;
        }

        public Drawable getIcon() {
            return icon;
        }

        public static class Builder {

            private ActionItem item = new ActionItem();

            public Builder groupId(int groupId) {
                item.groupId = groupId;
                return this;
            }

            public Builder itemId(int itemId) {
                item.itemId = itemId;
                return this;
            }

            public Builder title(CharSequence title) {
                item.title = String.valueOf(title);
                return this;
            }

            public Builder icon(Drawable icon) {
                item.icon = icon;
                return this;
            }

            public ActionItem build() {
                return item;
            }

        }
    }
}
