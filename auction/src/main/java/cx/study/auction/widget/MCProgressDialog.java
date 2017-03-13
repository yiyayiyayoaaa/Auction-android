package cx.study.auction.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface.OnCancelListener;
import android.view.View;
import android.widget.TextView;

import cx.study.auction.R;


public class MCProgressDialog {

	public static Dialog show(final Activity context, String msg, boolean cancelAble, OnCancelListener onCancelListener) {
		if (context == null || context.isFinishing()) {
			return null;
		}
		Dialog dialog = new Dialog(context, R.style.MCLoadingDialogTheme);
		dialog.setOwnerActivity(context);
		dialog.setCancelable(cancelAble);
		dialog.setCanceledOnTouchOutside(cancelAble);
		if (onCancelListener != null) {
			dialog.setOnCancelListener(onCancelListener);
		}
		View view = View.inflate(context, R.layout.layout_dialog_loading, null);
		TextView tipTextView = (TextView) view.findViewById(R.id.tipTextView);// 提示文字
		tipTextView.setText(msg);// 设置加载信息
		dialog.setContentView(view);
		dialog.show();
		return dialog;
	}

}
