package cx.study.auction.base;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.anthonycr.grant.PermissionsManager;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import cx.study.auction.R;

/**
 * User: LiangLong
 * Date: 2016-09-26
 * Time: 19:34
 * Note: com.microcardio.app
 */

public class LibBaseActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    private static final String TAG    = "LibActivity";

    protected ViewGroup contentLayout;
    private TextView tvTitle;
	private TextView tvSubTitle;
    private ProgressBar progressBar;
    private List<ActionItem> actionItems;

    /**
     * 左返回箭头点击事件,默认关闭当前Activity,子类可以重写此函数实现自己的逻辑
     */
    public void onHomeClick() {
        finish();
    }

    /**
     * 右边的Action按钮点击事件,注意右边的按钮需要设置标题或者图标才能够显示出来
     */
    public void onActionClick(ActionItem item) {
        // override this method
    }

    public boolean isToolbarProgressShown() {
        return progressBar != null && progressBar.getVisibility() == View.VISIBLE;
    }

    /**
     *
     * @param show 显示隐藏顶部的progressBar
     */
    @MainThread
    public final void setToolbarProgressVisibility(boolean show) {
        if (progressBar != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    /**
     *
     * @param items 设置导航栏右侧的action按钮信息,
     *              事件回调在{@link LibBaseActivity#onActionClick(ActionItem)}
     *              根据ItemId来获取,默认显示两个按钮,多的按钮会通过overflow菜单显示
     */
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
     *
     * @return contentLayout的id,当界面是由单个fragment构成的时候用来放置fragment
     */
    protected final @IdRes int getContentLayoutId() {
        return contentLayout == null ? 0 : contentLayout.getId();
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

	/**
	 * 设置当前界面保持屏幕常亮
	 */
	@MainThread
    protected final void keepScreenOn() {
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /**
     * 设置全屏
     */
    @MainThread
    protected final void setFullScreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_lib_base);
        this.initToolbar();
    }

    /**
     * 初始化toolbar,子类重写可以调整home键的样式,或者设置toolbar的popupTheme
     */
    protected void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.mcToolbar);
        tvTitle = (TextView) findViewById(R.id.mcToolbarTitle);
        tvSubTitle = (TextView) findViewById(R.id.mcSubToolbarTitle);
        progressBar = (ProgressBar) findViewById(R.id.mcToolbarProgress);
        contentLayout = (ViewGroup) findViewById(R.id.mcContentLayout);
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
            actionBar.setHomeAsUpIndicator(R.drawable.ic_home_back);
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
			actionBar.setDisplayShowTitleEnabled(isLeftTitle);
			actionBar.setTitle(titleLeft);
		}
		if (tvTitle != null) {
			tvTitle.setText(titleLeft);
			tvTitle.setVisibility(isLeftTitle ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * 设置副标题,居中的副标题
	 * @param title
	 */
	public final void setSubTitle(CharSequence title) {
		if (tvSubTitle != null) {
			tvSubTitle.setVisibility(View.VISIBLE);
			tvSubTitle.setText(title);
		}
	}

    @Override
    public final void setTitleColor(int textColor) {
        super.setTitleColor(textColor);
        if (tvTitle != null) {
            tvTitle.setTextColor(textColor);
        }
    }

    @Override
    public final void setContentView(@LayoutRes int layoutResID) {
        setContentView(View.inflate(this, layoutResID, null));
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
        ((ViewGroup)findViewById(R.id.mcContentLayout)).addView(view, params);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
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
