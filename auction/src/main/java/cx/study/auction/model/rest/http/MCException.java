package cx.study.auction.model.rest.http;

import com.orhanobut.logger.Logger;

import org.json.JSONException;

import java.io.IOException;
import java.net.ConnectException;

public class MCException extends Exception {

	private static final long serialVersionUID = -1438496563715140162L;

	private static final String TAG = MCException.class.getSimpleName();

	private final int code;
	private final String message;

	public MCException(int code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public MCException(MCError type) {
		super();
		this.code = type.getCode();
		this.message = type.getInfo();
	}

	public MCException(Throwable src) {
		super();
		if (src instanceof MCException) {
			this.code = ((MCException) src).getCode();
			this.message = src.getMessage();
		}
		else if (src instanceof ConnectException){
			this.code = MCError.ERROR_TIMEOUT.getCode();
			this.message = MCError.ERROR_TIMEOUT.getInfo();
		}
		else if (src instanceof JSONException) {
			this.code = MCError.ERROR_JSON.getCode();
			this.message = MCError.ERROR_JSON.getInfo();
		}
		else if (src instanceof IOException) {
			this.code = MCError.IO_EXCEPTION.getCode();
			this.message = MCError.IO_EXCEPTION.getInfo();
		}
		else {
			this.code = MCError.UNKONWN.getCode();
			this.message = MCError.UNKONWN.getInfo();
			Logger.t(TAG).e(src, "unknown exception !");
		}
	}

	@Override
	public String getMessage() {
		return message;// 从配置文件里面拿
	}

	public int getCode() {
		return code;
	}

	public MCError getCustomError() {
		return MCError.valueOf(code);
	}
}
