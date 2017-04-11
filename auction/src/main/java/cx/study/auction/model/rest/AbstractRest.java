package cx.study.auction.model.rest;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cx.study.auction.model.rest.http.MCException;
import cx.study.auction.model.rest.json2object.Json2Object;


public abstract class AbstractRest {

	/**
	 * 将json字符串转换为实体类列表
	 *
	 * @param data json字符串
	 * @param json2Object
	 * @param <T>
	 * @return
	 * @throws Exception
	 */
	@Nullable
	public static <T> List<T> getListFromData(String data, Json2Object<T> json2Object) throws MCException {
		if (TextUtils.isEmpty(data)) {
			return null;
		}
		try {
			List<T> result = new ArrayList<>();
			JSONArray array = new JSONArray(data);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.optJSONObject(i);
				if (object == null) {
					continue;
				}
				T t = json2Object.json2Object(object);
				if (t == null) {
					continue;
				}
				result.add(t);
			}
			return result;
		}
		catch (JSONException e) {
			e.printStackTrace();
			throw new MCException(e);
		}
	}
}
