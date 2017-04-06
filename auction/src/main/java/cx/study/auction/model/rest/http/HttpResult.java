package cx.study.auction.model.rest.http;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cx.study.auction.util.IOUtils;

/**
 * User: LiangLong
 * Date: 2016-11-08
 * Time: 09:34
 * Note: com.microcardio.wxdoctor.model
 * http请求通用应答
 */

public final class HttpResult {

	private final JSONObject src;

	public final int code;
    public final String msg;
    public final Object obj;

    public HttpResult(JSONObject result) {
	    src = result;
        code = result.optInt("code");
        msg = result.optString("msg", "");
        obj = result.opt("obj");
    }

    public String string() {
	    if (obj == null) {
		    return "";
	    }
	    String str = String.valueOf(obj);
        return str.equals("null") ? "" : str;
    }

    /**
     * @return 防止崩溃发生, 开发时应该确定数据结构
     */
    public JSONObject object() {
        if (obj instanceof JSONObject) {
            return (JSONObject) obj;
        } else {
            return new JSONObject();
        }
    }

    /**
     * @return 防止崩溃发生, 开发时应该确定数据结构
     */
    public JSONArray array() {
        if (obj instanceof JSONArray) {
            return (JSONArray) obj;
        } else {
            return new JSONArray();
        }
    }

	@Override
	public String toString() {
		return src == null ? super.toString() : src.toString();
	}

	/**
	 * 结果转换器
	 */
	public static class Parser implements IParser<HttpResult> {

	    @Override
	    public HttpResult parse(MCHttpResponse body) throws IOException {
		    String r = IOUtils.stream2String(body.contentStream());
		    try {
			    return new HttpResult(new JSONObject(r));
		    }
		    catch (JSONException e) {
			    throw new IOException(String.format("result %s isn't a JSON", r));
		    }
	    }

    }

    public static class HttpResponseHandler extends AbstractHttpResponseHandler<HttpResult>{

        @Override
        public HttpResult parse(MCHttpResponse body) throws IOException {
            String r = IOUtils.stream2String(body.contentStream());
            try {
                return new HttpResult(new JSONObject(r));
            }
            catch (JSONException e) {
                throw new IOException(String.format("result %s isn't a JSON", r));
            }
        }
    }

}
