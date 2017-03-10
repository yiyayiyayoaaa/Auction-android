package cx.study.auction.http;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * User: LiangLong
 * Date: 2016-10-09
 * Time: 23:45
 * Note: com.microcardio.http
 * http请求异步回调类,框架相关方法已经隐藏,子类根据需求自己实现转换函数,返回不同的结果
 */
public abstract class AbstractHttpResponseHandler<Result> implements Callback, Handler.Callback, IParser<Result> {
    private static final String TAG = "HttpResponseHandler";

    private static final int MSG_START      = 0;
    private static final int MSG_PROGRESS   = MSG_START + 1;
    private static final int MSG_FINISH     = MSG_PROGRESS + 1;
    private static final int MSG_SUCCESS    = MSG_FINISH + 1;
    private static final int MSG_FAILED     = MSG_SUCCESS + 1;
    private static final int MGS_CANCEL     = MSG_FAILED + 1;
    private static final int MGS_FINALLY    = MGS_CANCEL + 1;

    /**
     * 对应的请求
     */
    private MCHttpRequest request;

    /**
     * 响应码
     */
    private int httpStatusCode = 0;

    /**
     * 相应头
     */
    private Map<String, String> headers;

    /**
     * 获取 http 状态码，如果请求还没有返回则返回 0
     *
     * @return http 状态码
     */
    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    @WorkerThread
    public abstract Result parse(MCHttpResponse body) throws IOException;


    /**
     * 请求发出去之前触发
     * @param request 请求
     */
    @MainThread
    public void onStart(MCHttpRequest request) {

    }

    /**
     * 请求被取消时触发
     */
    @MainThread
    public void onCancel(MCHttpRequest request){

    }

    /**
     * 这个回调只有子类调用{@link AbstractHttpResponseHandler#setProgress(int)}才会触发
     * @param request   请求
     * @param progress  进度
     */
    @MainThread
    public void onProgress(MCHttpRequest request, @Nullable Map<String, String> headers, int progress) {

    }

    /**
     * 请求成功,获取到数据时触发
     */
    @MainThread
    public void onSuccess(MCHttpRequest request, @Nullable Map<String, String> headers, Result result) {

    }

    /**
     * 请求失败
     */
    @MainThread
    public void onFailure(MCHttpRequest request, @Nullable Map<String, String> headers, Exception e) {
        e.printStackTrace();
        Logger.t(TAG).e("Http Error ", e);
    }

    /**
     * http请求结束(读取到所有的body数据)之后触发(在成功和失败回调之前触发)
     */
    @MainThread
    public void onFinish(MCHttpRequest request) {

    }

    /**
     * 在成功或者失败触发之后触发该回调
     */
    @MainThread
    public void onFinally(MCHttpRequest request) {

    }

    @Override
    public final void onResponse(Call call, Response response) throws IOException {
        httpStatusCode = response.code();
        for (String key : response.headers().names()) {
            String val = response.header(key);
            if (headers == null) {
                headers = new HashMap<>();
            }
            if (!TextUtils.isEmpty(val)) {
                headers.put(key, val);
            }
        }
        if (response.isSuccessful()) {
            try {
                this.fireOnSuccess(parse(MCHttpResponse.create(response)));
            } catch (Exception e) {
                this.fireOnFailure(e);
            }
        } else {
            this.fireOnFailure(new IllegalStateException(String.format(Locale.getDefault(),"response code is %d", httpStatusCode)));
        }
    }

    @Override
    public final void onFailure(Call call, IOException e) {
        this.fireOnFailure(e);
    }

    /**
     *
     * @param progress 设置当前请求进度
     */
    @WorkerThread
    protected final void setProgress(int progress) {
        this.fireOnProgress(progress);
    }

    /**
     *
     * @param mRequest 关联request请求
     */
    void setRequest(MCHttpRequest mRequest) {
        this.request = mRequest;
    }

    private Handler mainHandler;

    Handler getMainHandler() {
        if (mainHandler == null) {
            mainHandler = new Handler(Looper.getMainLooper(), this);
        }
        return mainHandler;
    }

    void fireOnStart() {
        Message message = getMainHandler().obtainMessage(MSG_START);
        message.sendToTarget();
    }

    void fireOnProgress(int progress) {
        Message message = getMainHandler().obtainMessage(MSG_PROGRESS, progress, 0);
        message.sendToTarget();
    }

    void fireOnFinish() {
        Message message = getMainHandler().obtainMessage(MSG_FINISH);
        message.sendToTarget();
    }

    void fireOnSuccess(Result data) {
        fireOnFinish();
        Message message = getMainHandler().obtainMessage(MSG_SUCCESS, data);
        message.sendToTarget();
        fireOnFinally();
    }

    void fireOnFailure(Exception e) {
        fireOnFinish();
        Message message = getMainHandler().obtainMessage(MSG_FAILED, e);
        message.sendToTarget();
        fireOnFinally();
    }

    void fireOnCancel() {
        // TODO: 2016/10/9 需要测试生命周期
        Message message = getMainHandler().obtainMessage(MGS_CANCEL);
        message.sendToTarget();
    }

    void fireOnFinally() {
        Message message = getMainHandler().obtainMessage(MGS_FINALLY);
        message.sendToTarget();
    }

    @Override
    @SuppressWarnings("unchecked")
    public final boolean handleMessage(Message msg) {
        switch (msg.what) {
        case MSG_START:
            this.onStart(request);
            break;
        case MSG_PROGRESS:
            this.onProgress(request, headers, msg.arg1);
            break;
        case MSG_FINISH:
            this.onFinish(request);
            break;
        case MSG_SUCCESS:
            this.onSuccess(request, headers, (Result) msg.obj);
            break;
        case MSG_FAILED:
            this.onFailure(request, headers, (Exception) msg.obj);
            break;
        case MGS_CANCEL:
            this.onCancel(request);
            break;
        case MGS_FINALLY:
            this.onFinally(request);
            break;
        }
        return true;
    }

}
