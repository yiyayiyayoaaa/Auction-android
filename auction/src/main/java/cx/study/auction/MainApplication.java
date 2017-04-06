package cx.study.auction;

import android.app.Application;

import cx.study.auction.util.ActivityManager;

/**
 *
 * Created by cheng.xiao on 2017/3/9.
 */

public class MainApplication extends Application{

    private static MainApplication app;

    public static MainApplication getInstance(){
        return app;
    }

    public static void finishAll(){
        ActivityManager.getInstance().finishAll();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        registerActivityLifecycleCallbacks(ActivityManager.getInstance());
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
