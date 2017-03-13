package cx.study.auction.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface.OnCancelListener;
import android.os.Looper;
import android.support.annotation.StringRes;

import cx.study.auction.widget.MCProgressDialog;


public class MCProgress {
	private static Dialog dialog;

	public static void show(String text, Activity activity) {
		show(text, activity, true, null);
	}

	public static void show(@StringRes int resID, Activity activity) {
		if (activity == null) {
			return;
		}
		show(activity.getString(resID), activity, true, null);
	}

	public static void show(String text, Activity activity, boolean cancelable) {
		if (activity == null) {
			return;
		}
		show(text, activity, cancelable, null);
	}

	public static void show(final String text, final Activity activity, final boolean cancelAble, final OnCancelListener onCancelListener) {
		if (activity == null) {
			return;
		}
		if (Looper.getMainLooper() == Looper.myLooper()) {
			dismiss();
			dialog = MCProgressDialog.show(activity, text, cancelAble, onCancelListener);
		}
		else {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					show(text, activity, cancelAble, onCancelListener);
				}
			});
		}
	}

	public static void dismiss() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
			dialog = null;
		}
	}

}
