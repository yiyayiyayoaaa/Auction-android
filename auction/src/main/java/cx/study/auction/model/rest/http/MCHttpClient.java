package cx.study.auction.model.rest.http;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * User: LiangLong
 * Date: 2016-11-07
 * Time: 20:52
 * Note: com.microcardio.http
 */

public final class MCHttpClient {

    /**
     * 超时时长,单位秒
     */
    private static final long TIME_OUT = 30;

    private static OkHttpClient okHttpClient;

    static OkHttpClient getHttpClient() {
        if (okHttpClient == null) {
            synchronized (MCHttpRequest.class) {
                if (okHttpClient == null) {
                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                    builder.connectTimeout(TIME_OUT, TimeUnit.SECONDS);
                    builder.readTimeout(TIME_OUT, TimeUnit.SECONDS);
                    builder.writeTimeout(TIME_OUT, TimeUnit.SECONDS);
                    okHttpClient = builder.build();
                }
            }
        }
        return okHttpClient;
    }

    /**
     *
     * @return 克隆出一个httpClient,共享连接池
     */
    public static OkHttpClient.Builder newClientBuilder() {
        return getHttpClient().newBuilder();
    }

}
