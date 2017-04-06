package cx.study.auction.model.rest.http;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.common.collect.Maps;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import cx.study.auction.BuildConfig;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpClient {

    private static final String TAG = HttpClient.class.getSimpleName();

    /**
     * @return 添加http请求的公共头
     */
    public static Map<String, String> getCommonHeaders() {
//        DoctorMediator doctorMediator = MediatorManager.getInstance().get(Module.DOCTOR);
//        String deviceId = DeviceUtils.getDeviceId(MainApplication.getAppContext());

        Map<String, String> headers = Maps.newHashMap();
        headers.put("_User", "test");//当前操作用户
        headers.put("_Timestamp", String.valueOf(System.currentTimeMillis()));//时间
        headers.put("_AppVersion", String.valueOf(BuildConfig.VERSION_CODE));//版本
        headers.put("_AppName", "wxDoctor");//版本
        headers.put("_Device", "test");//设备
        headers.put("_Channel", BuildConfig.FLAVOR);//渠道

        return headers;
    }

	public static HttpResult doGet(String url, Map<String,Object> param) throws MCException {
		return doGet(url, param, new HttpResult.Parser());
	}

	public static <T> T doGet(String url, Map<String,Object> param, IParser<T> parser) throws MCException {
		MCHttpRequest<T> request = new MCHttpRequest<>();
		request.setMethod(MCHttpRequest.GET);
		request.setUrl(url);
		request.addHeaders(getCommonHeaders());

		if (param != null && !param.isEmpty()) {
			MCHttpRequestParams params = new MCHttpRequestParams(MCHttpRequestParams.APPLICATION_JSON);
			params.put(param);
			request.setRequestParams(params);
		}

		try {
			T result = request.execute(parser);
			if (result instanceof HttpResult) {
				checkHttpResult(request, (HttpResult) result);
			} else {
				Logger.t(TAG).d(request.toString());
			}
			return result;
		}
		catch (IOException e) {
			Logger.t(TAG).e(e, request.toString());
			throw new MCException(MCError.ERROR_NET_POOR);
		}
		catch (MCException e) {
			Logger.t(TAG).e(e, request.toString());
			throw e;
		}
		catch (Exception e) {
			Logger.t(TAG).e(e, request.toString());
			throw new MCException(e);
		}
	}

	/**
	 * post Json数据
	 *
	 * @param url
	 * @param params
	 * @return
	 * @throws MCException
	 */
	public static HttpResult doJSONPost(String url, Map<String,Object> params) throws MCException {
		return doJSONPost(url, params, new HttpResult.Parser());
	}

	/**
	 * post Json数据
	 *
	 * @param url
	 * @param params
	 * @return
	 * @throws MCException
	 */
	public static <T> T doJSONPost(String url, Map<String, Object> params, IParser<T> parser) throws MCException {
		MCHttpRequestParams requestParams = new MCHttpRequestParams(MCHttpRequestParams.APPLICATION_JSON);
		requestParams.put(params);

		MCHttpRequest<T> request = new MCHttpRequest<>();
		request.setMethod(MCHttpRequest.POST);
		request.setUrl(url);
		request.addHeaders(getCommonHeaders());
		request.setRequestParams(requestParams);
		try {
			T result = request.execute(parser);
			if (result instanceof HttpResult) {
				checkHttpResult(request, (HttpResult) result);
			} else {
				Logger.t(TAG).d(request.toString());
			}
			return result;
		}
		catch (IOException e) {
			Logger.t(TAG).e(e, request.toString());
			throw new MCException(MCError.ERROR_NET_POOR);
		}
		catch (MCException e) {
			Logger.t(TAG).e(e, request.toString());
			throw e;
		}
		catch (Exception e) {
			Logger.t(TAG).e(e, request.toString());
			throw new MCException(e);
		}
	}

	/**
	 * post Json数据
	 *
	 * @param url
	 * @param obj
	 * @return
	 * @throws MCException
	 */
	public static HttpResult doJSONPost(String url, JSONObject obj) throws MCException {
		return doJSONPost(url, obj, new HttpResult.Parser());
	}

	/**
	 * 请求的实际发送
	 * @param url
	 * @param obj
	 * @param parser
	 * @param <T>
	 * @return
	 * @throws MCException
	 */
	public static <T> T doJSONPost(String url, JSONObject obj, IParser<T> parser) throws MCException {
		MCHttpRequestParams params = new MCHttpRequestParams(MCHttpRequestParams.APPLICATION_JSON);
		params.put(obj);

		MCHttpRequest<T> request = new MCHttpRequest<>();
		request.setMethod(MCHttpRequest.POST);
		request.setUrl(url);
		request.addHeaders(getCommonHeaders());
		request.setRequestParams(params);
		try {
			T result = request.execute(parser);
			if (result instanceof HttpResult) {
				checkHttpResult(request, (HttpResult) result);
			} else {
				Logger.t(TAG).d(request.toString());
			}
			return result;
		}
		catch (IOException e) {
			Logger.t(TAG).e(e, request.toString());
			throw new MCException(MCError.ERROR_NET_POOR);
		}
		catch (MCException e) {
			Logger.t(TAG).e(e, request.toString());
			throw e;
		}
		catch (Exception e) {
			Logger.t(TAG).e(e, request.toString());
			throw new MCException(e);
		}
	}

	/**
	 * 带缓存的请求
	 * @param url
	 * @param cacheKey
	 * @param params
	 * @return
	 * @throws MCException
	 */
	public static HttpResult doJSONHttpCache(String url, String cacheKey, Map<String,Object> params) throws MCException {
		return doJSONHttpCache(url, cacheKey, new JSONObject(params));
	}

	/**
	 * 带缓存的请求重载
	 * @param url
	 * @param cacheKey
	 * @param obj
	 * @return
	 * @throws MCException
	 */
    public static HttpResult doJSONHttpCache(String url, String cacheKey, JSONObject obj) throws MCException {
        MCHttpRequestParams params = new MCHttpRequestParams(MCHttpRequestParams.APPLICATION_JSON);
        params.put(obj);
        params.put("cacheKey", cacheKey);

        MCHttpRequest<String> request = new MCHttpRequest<>();
        request.setMethod(MCHttpRequest.POST);
        request.setUrl(url);
        request.addHeaders(getCommonHeaders());
        request.setRequestParams(params);
        try {
            RequestBody body = null;
            if (!params.isEmpty()) {
                body = params.getBody();
            }
	        Request.Builder requestBuilder = new Request.Builder();
            for (Map.Entry<String, String> entry : getCommonHeaders().entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
            //如果cacheKey为空，则不缓存，反之会缓存到本地
            if (!TextUtils.isEmpty(cacheKey)) {
                requestBuilder.addHeader("cacheKey", cacheKey);
            }
            Request cache_request = requestBuilder.url(url).post(body).build();

            Response response = OkHttp3Wrapper.getHttpCacheClientInstance().newCall(cache_request).execute();
	        HttpResult result = new HttpResult.Parser().parse(MCHttpResponse.create(response));

            return checkHttpResult(request, result);
        }
        catch (IOException e) {
	        Logger.t(TAG).e(e, request.toString());
	        throw new MCException(MCError.ERROR_NET_POOR);
        }
        catch (MCException e) {
	        Logger.t(TAG).e(e, request.toString());
	        throw e;
        }
        catch (Exception e) {
	        Logger.t(TAG).e(e, request.toString());
	        throw new MCException(e);
        }
    }

    /**
     * 上传文件,上传成功"code":0,
	 *
     * @param url
     * @param file
     * @return
     */
    public static void uploadFile(String url, File file, Map<String,Object> map) {
        if (file == null || !file.exists()) {
            return;
        }
        MCHttpRequest<HttpResult> request = new MCHttpRequest<>();
        request.setMethod(MCHttpRequest.POST);
        request.setUrl(url);
        request.addHeaders(getCommonHeaders());
        request.addHeader("fileEncrypt", Digest.fileMD5Hex(file));


        MCHttpRequestParams params = new MCHttpRequestParams();
        params.put(file.getName(), file);
		params.put(map);
        // params.put("file", file); //测试

        request.setRequestParams(params);
            request.enqueue(new HttpResult.HttpResponseHandler(){
				@Override
				public void onSuccess(MCHttpRequest request, @Nullable Map<String, String> headers, HttpResult httpResult) {
					super.onSuccess(request, headers, httpResult);
					try {
						checkHttpResult(request, httpResult);

					} catch (MCException e) {
						Logger.t(TAG).e(e, request.toString());
					}
				}
			});

    }

    /**
     * 检查http请求的结果,当code != 0 时会抛出异常,所以获取到的结果都是成功
     *
     * @param result
     * @return
     * @throws MCException
     */
    private static HttpResult checkHttpResult(@NonNull Object obj, HttpResult result) throws MCException {
	    if (result.code != 0) {
		    Logger.t(TAG).e("%s\nResult : %s", obj.toString(), result.toString());
		    throw new MCException(result.code, result.msg);
	    }
	    Logger.t(TAG).d("%s\nResult : %s", obj.toString(), result.string());
	    return result;
    }

}