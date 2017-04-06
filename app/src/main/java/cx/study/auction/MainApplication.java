package cx.study.auction;


import android.app.Application;

import cx.study.auction.util.ActivityManager;

public class MainApplication extends Application {

	private static final String TAG = "MainApplication";
	private static MainApplication app;

	public static MainApplication getInstance() {
		return app;
	}


	@Override
	public void onCreate() {
		super.onCreate();
		app = this;
        registerActivityLifecycleCallbacks(ActivityManager.getInstance());
	}

    public void finishActivities() {
        ActivityManager.getInstance().finishAll();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

    @Override
    public void onTerminate() {
        unregisterActivityLifecycleCallbacks(ActivityManager.getInstance());
        super.onTerminate();
    }
}
