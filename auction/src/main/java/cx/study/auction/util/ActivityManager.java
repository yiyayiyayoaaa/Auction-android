package cx.study.auction.util;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.common.collect.Lists;

import java.util.LinkedList;
import java.util.List;


public final class ActivityManager implements Application.ActivityLifecycleCallbacks {

	private static final String TAG = ActivityManager.class.getSimpleName();

	private static final ActivityManager INSTANCE = new ActivityManager();

	public static ActivityManager getInstance() {
		return INSTANCE;
	}

	private final LinkedList<Activity> activities = Lists.newLinkedList();

	private ActivityManager() {

	}

	public void finishAll() {
		List<Activity> activities = Lists.reverse((List<Activity>) this.activities.clone());
		for (Activity activity : activities) {
			if (activity != null && !activity.isFinishing()) {
				activity.finish();
			}
		}
	}

	/**
	 *
	 * @return  获取最后建立的activity,可能为空值,需要外部做一个判断
	 */
	@Nullable
	public Activity getTopActivity() {
		Activity top = !activities.isEmpty() ? activities.getLast() : null;
		if (top != null) {
//			Logger.t(TAG).d("App top activity is %s", top.getClass().getSimpleName());
		}
		return top;
	}

	@Override
	public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
		synchronized (activities) {
			activities.add(activity);
		}
	}

	@Override
	public void onActivityStarted(Activity activity) {

	}

	@Override
	public void onActivityResumed(Activity activity) {

	}

	@Override
	public void onActivityPaused(Activity activity) {

	}

	@Override
	public void onActivityStopped(Activity activity) {

	}

	@Override
	public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

	}

	@Override
	public void onActivityDestroyed(Activity activity) {
		synchronized (activities) {
			activities.remove(activity);
		}
	}

}
