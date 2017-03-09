package cx.study.auction.app.base;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
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
    private TextView tvTitle;
    private TextView tvSubTitle;
    private List<ActionItem> actionItems;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        initToolbar();
    }

    /**
     * 初始化toolbar,子类重写可以调整home键的样式,或者设置toolbar的popupTheme
     */
    protected void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.mcToolbar);
        tvTitle = (TextView) findViewById(R.id.mcToolbarTitle);
        tvSubTitle = (TextView) findViewById(R.id.mcSubToolbarTitle);
        this.setSupportActionBar(toolbar);
    }

    @Override
    public final void setSupportActionBar(@Nullable Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
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
