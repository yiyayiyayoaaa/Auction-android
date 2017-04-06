package cx.study.auction.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.support.annotation.Nullable;

import cx.study.auction.MainApplication;

public class NetUtils {
	public static final int NETWORN_NONE = 0;
	public static final int NETWORN_WIFI = 1;
	public static final int NETWORN_MOBILE = 2;

	private static ConnectivityManager conMgr;

	//监听网络状态使用
	public static int getNetworkState(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netWorkInfo;
		// Wifi
		netWorkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		netWorkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (netWorkInfo != null) {
			State state = netWorkInfo.getState();
			if (state == State.CONNECTED || state == State.CONNECTING) {
				return NETWORN_WIFI;
			}
		}
		// 3G
		netWorkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (netWorkInfo != null) {
			State state = netWorkInfo.getState();
			if (state == State.CONNECTED || state == State.CONNECTING) {
				return NETWORN_MOBILE;
			}
		}
		return NETWORN_NONE;
	}

	public static boolean isNetworkAvailable() {
		return isNetworkAvailable(MainApplication.getInstance());
	}

	public static boolean isNetworkAvailable(@Nullable Context appContext) {
		if (appContext == null) {
			appContext = MainApplication.getInstance();
		}
		if (conMgr == null) {
			conMgr = (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		}
		NetworkInfo info = conMgr.getActiveNetworkInfo();
		return (info != null && info.getState() == NetworkInfo.State.CONNECTED);
	}

	/**
	 * make true current connect service is wifi
	 *
	 * @param mContext
	 * @return
	 */
	public static boolean isWifi(Context mContext) {
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

}
