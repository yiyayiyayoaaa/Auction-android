package cx.study.auction.model.rest.http;

import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.text.TextUtils;

import com.google.common.io.Files;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * User: LiangLong
 * Date: 2016-10-09
 * Time: 23:45
 * Note: com.microcardio.http
 */
public class MCHttpRequestParams implements Cloneable {

    /**
     * 默认字符编码
     */
    public final static String UTF_8_STR = "utf-8";

    /**
     * 字节流
     */
    public final static String APPLICATION_OCTET_STREAM = "application/octet-stream";

    /**
     * JSON
     */
    public final static String APPLICATION_JSON = "application/json";

    /**
     * 文本
     */
    public final static String APPLICATION_TEXT = "text/html";

    /**
     * Form 表单
     */
    public final static String APPLICATION_FORM = "application/x-www-form-urlencoded";

    @StringDef({
            APPLICATION_FORM,
            APPLICATION_JSON,
            APPLICATION_OCTET_STREAM,
            APPLICATION_TEXT,
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface ContentType {
    }

    /**
     * 表单形式,默认是form表单,即 A=a&B=b 的形式, 但是如果参数中含有流或者文件,则此设置字段也是无效的
     */
    @ContentType
    private String contentType = APPLICATION_FORM;

    //params
    private final LinkedHashMap<String, Object> objectParams = new LinkedHashMap<>();
    private final LinkedHashMap<String, StreamWrapper> streamParams = new LinkedHashMap<>();
    private final LinkedHashMap<String, FileWrapper> fileParams = new LinkedHashMap<>();
    private RequestBody postBody;

    public MCHttpRequestParams() {
        this(APPLICATION_FORM);
    }

    public MCHttpRequestParams(@ContentType String contentType) {
        this.contentType = contentType;
    }

    public MCHttpRequestParams put(RequestBody body) {
        this.postBody = body;
        return this;
    }

    public MCHttpRequestParams put(JSONObject param) {
        if (param != null && param.length() > 0) {
            Iterator<String> keys = param.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                Object value = param.opt(key);
                if (value != null) {
                    put(key, value);
                }
            }
        }
        return this;
    }

    public MCHttpRequestParams put(MCHttpRequestParams params) {
        if (params != null) {
            this.contentType = params.contentType;
            this.objectParams.putAll(params.objectParams);
            this.streamParams.putAll(params.streamParams);
            this.fileParams.putAll(params.fileParams);
        }
        return this;
    }

    public MCHttpRequestParams put(Map<String, Object> params) {
	    if (params == null || params.isEmpty()) {
		    return this;
	    }
	    for (String key : params.keySet()) {
			put(key, params.get(key));
	    }
	    return this;
    }

    public MCHttpRequestParams put(String key, Object value) {
	    if (value instanceof File) {
		    put(key, (File) value);
	    }
	    else if (value instanceof InputStream) {
		    put(key, (InputStream) value);
	    }
	    else if (value != null) {
		    objectParams.put(key, value);
	    }
        return this;
    }

    public MCHttpRequestParams put(String key, InputStream stream) {
        return put(key, stream, null);
    }

    public MCHttpRequestParams put(String key, InputStream stream, String name) {
        return put(key, stream, name, null);
    }

    public MCHttpRequestParams put(String key, InputStream stream, String name, String contentType) {
        if (!TextUtils.isEmpty(key) && stream != null) {
            streamParams.put(key, new StreamWrapper(stream, name, contentType));
        }
        return this;
    }

    public MCHttpRequestParams put(String key, File file) {
        return put(key, file, null);
    }

    public MCHttpRequestParams put(String key, File file, String contentType) {
        if (file != null && file.exists() && !TextUtils.isEmpty(key)) {
            fileParams.put(key, new FileWrapper(file, contentType));
        }
        return this;
    }

    public boolean isEmpty() {
        return postBody == null && objectParams.isEmpty() && fileParams.isEmpty() && streamParams.isEmpty();
    }

    public void clear() {
        objectParams.clear();
        fileParams.clear();
        streamParams.clear();
    }

    public void remove(String key) {
        objectParams.remove(key);
        streamParams.remove(key);
        fileParams.remove(key);
    }

    public boolean has(String key) {
        return (objectParams.containsKey(key) || streamParams.containsKey(key) || fileParams.containsKey(key));
    }

    /**
     * 该方法供子类重写用,添加公共参数
     */
    protected void appendCommonParams() {
        //供子类实现
    }

    /**
     * @return 获取请求参数的Json形式, 供子类重写, 用来实现加密等等
     */
    protected String jsonString() {
        appendCommonParams();

        return new JSONObject(objectParams).toString();
    }

    /**
     * GET请求构建query A=a&B=b..
     */
    public final String getQuery() {
        appendCommonParams();

        StringBuilder sb = new StringBuilder();
        if (!objectParams.isEmpty()) {
            sb.append("?");
            for (Map.Entry<String, Object> entry : objectParams.entrySet()) {
                Object value = entry.getValue();
                if ((value instanceof Number) || (value instanceof Boolean) || (value instanceof String)) {
                    sb.append(entry.getKey()).append("=").append(value);
                    sb.append("&");
                }
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * @return 构建请求body
     */
    public final RequestBody getBody() {
        if (postBody != null) {
            return postBody;
        }

        appendCommonParams();
//        // 2016-11-24 14:15:13 单一的文件或者单一的流,不用创建multiPart,直接传输一个part就可以了
//        if (objectParams.isEmpty()) {
//            if (fileParams.size() == 1 && streamParams.isEmpty()) {
//                return createFileRequestBody(fileParams.values().iterator().next().file);
//            }
//            if (streamParams.size() == 1 && fileParams.isEmpty()) {
//                StreamWrapper sw = streamParams.values().iterator().next();
//                return createStreamRequestBody(sw.contentType, sw.inputStream);
//            }
//        }
        //根据参数的不同创建不同的RequestBody
        if (!streamParams.isEmpty() || !fileParams.isEmpty()) {
            return createMultiPartRequestBody();
        } else if (TextUtils.equals(contentType, APPLICATION_JSON) || TextUtils.equals(contentType, APPLICATION_TEXT)) {
            return createJsonRequestBody();
        } else {
            return createFormRequestBody();
        }
    }

    /**
     * create JSON request body
     */
    private RequestBody createJsonRequestBody() {
        String type = String.format("%s; charset=%s", APPLICATION_JSON, UTF_8_STR);
        MediaType mediaType = MediaType.parse(type);
        return RequestBody.create(mediaType, jsonString());
    }

    /**
     * create form request body
     */
    private RequestBody createFormRequestBody() {
        Map<String, Object> form = objectParams;
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, Object> entry : form.entrySet()) {
            Object value = entry.getValue();
            if ((value instanceof Number) || (value instanceof Boolean) || (value instanceof String)) {
                builder.add(entry.getKey(), value.toString());
            }
        }
        return builder.build();
    }

    /**
     * create multiPart request body
     */
    private RequestBody createMultiPartRequestBody() {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        //text
        Map<String, Object> form = objectParams;
        for (Map.Entry<String, Object> entry : form.entrySet()) {
            Object value = entry.getValue();
            if ((value instanceof Number) || (value instanceof Boolean) || (value instanceof String)) {
                builder.addFormDataPart(entry.getKey(), String.valueOf(value));
            }
        }

        //contentStream
        Map<String, StreamWrapper> stream = streamParams;
        for (Map.Entry<String, StreamWrapper> entry : stream.entrySet()) {
            String key = entry.getKey();
            StreamWrapper wrapper = entry.getValue();
            RequestBody streamBody = createStreamRequestBody(wrapper.contentType, wrapper.inputStream);
            builder.addFormDataPart(key, wrapper.name, streamBody);
        }

        //file
        Map<String, FileWrapper> files = fileParams;
        for (Map.Entry<String, FileWrapper> entry : files.entrySet()) {
            String key = entry.getKey();
            FileWrapper wrapper = entry.getValue();
            RequestBody fileBody = createFileRequestBody(wrapper.file);
            builder.addFormDataPart(key, wrapper.file.getName(), fileBody);
        }

        return builder.build();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (!objectParams.isEmpty()) {
            builder.append(objectParams.toString());
        }
        if (!fileParams.isEmpty()) {
            builder.append(fileParams.toString());
        }
        if (!streamParams.isEmpty()) {
            builder.append(streamParams);
        }
        return builder.toString();
    }

    /**
     * create single bytes request body
     */
    public static RequestBody createBytesRequestBody(@NonNull final String contentType, @NonNull final byte[] bytes) {
        return RequestBody.create(MediaType.parse(contentType), bytes);
    }

    /**
     * create single stream request body
     */
    public static RequestBody createStreamRequestBody(@NonNull final String contentType, @NonNull final InputStream inputStream) {
        return new RequestBody() {

            @Override
            public MediaType contentType() {
                return MediaType.parse(contentType);
            }

            @Override
            public long contentLength() throws IOException {
                try {
                    return inputStream.available();
                } catch (Exception e) {
                    return super.contentLength();
                }
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                Source source = null;
                try {
                    source = Okio.source(inputStream);
                    sink.writeAll(source);
                } finally {
                    Util.closeQuietly(source);
                }
            }

        };
    }

    /**
     * create single file request body
     */
    public static RequestBody createFileRequestBody(@NonNull final File file) {
        return RequestBody.create(MediaType.parse(FileWrapper.getContentType(file)), file);
    }


    /**
     * 文件参数包装类
     */
    public static class FileWrapper {
        public final File file;
        public final String contentType;

        public FileWrapper(File file, String contentType) {
            this.file = file;
            if (TextUtils.isEmpty(contentType)) {
                contentType = getContentType(file);
            }
            this.contentType = contentType;
        }

        public static String getContentType(File file) {
            String contentType = APPLICATION_OCTET_STREAM;

            String ext = Files.getFileExtension(file.getName());
            if (ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("jpeg") || ext.equalsIgnoreCase("png")) {
                contentType = "image/jpeg";
            } else if (ext.equalsIgnoreCase("png")) {
                contentType = "image/png";
            } else if (ext.equalsIgnoreCase("gif")) {
                contentType = "image/gif";
            } else if (ext.equalsIgnoreCase("amr")) {
                contentType = "audio/amr";
            }
            return contentType;
        }

    }

    /**
     * 流包装类
     */
    public static class StreamWrapper {
        public final InputStream inputStream;
        public final String name;
        public final String contentType;

        public StreamWrapper(InputStream inputStream, String name, String contentType) {
            this.inputStream = inputStream;
            this.name = name;
            this.contentType = TextUtils.isEmpty(contentType) ? APPLICATION_OCTET_STREAM : contentType;
        }
    }

}